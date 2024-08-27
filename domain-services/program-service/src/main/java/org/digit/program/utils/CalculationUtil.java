package org.digit.program.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.AllocationType;
import org.digit.program.constants.Status;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.service.EnrichmentService;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.digit.program.constants.Error.NO_SANCTION_AVAILABLE_FOR_AMOUNT;
import static org.digit.program.constants.Error.NO_SANCTION_AVAILABLE_FOR_AMOUNT_MSG;

@Component
@Slf4j
public class CalculationUtil {
    private final SanctionRepository sanctionRepository;
    private final DisburseRepository disburseRepository;
    private final EnrichmentService enrichmentService;
    private final ProgramConfiguration configs;

    public CalculationUtil(SanctionRepository sanctionRepository, DisburseRepository disburseRepository, EnrichmentService enrichmentService, ProgramConfiguration configs) {
        this.sanctionRepository = sanctionRepository;
        this.disburseRepository = disburseRepository;
        this.enrichmentService = enrichmentService;
        this.configs = configs;
    }

    /**
     * If status of disburse reply is failed then add the disbursement amount to sanction and return the sanction
     * @param disbursement
     * @param senderId
     * @return
     */
    public Sanction calculateAndReturnSanctionForOnDisburse(Disbursement disbursement, String senderId) {
        if (disbursement.getStatus().getStatusCode().equals(Status.FAILED)  ||
                disbursement.getStatus().getStatusCode().equals(Status.ERROR)) {
            log.info("Calculating Sanction for on disburse");
            List<Disbursement> disbursementsFromDB = disburseRepository.searchDisbursements(DisburseSearch.builder()
                    .targetId(disbursement.getTargetId()).build());
            // Sort disbursements in descending order of created time

            List<Disbursement> originalDisbursementsFromDB = filterDisbursements(disbursementsFromDB);
            if(originalDisbursementsFromDB.get(0).getStatus().equals(Status.PARTIAL)) {
                return null;
            }

            // fetch the first disbursement whose isRevised flag present in additional details is true
            SanctionSearch sanctionSearch = SanctionSearch.builder().ids(Collections.singletonList(disbursement.getSanctionId())).build();
            Sanction sanction = sanctionRepository.searchSanction(sanctionSearch, false).get(0);
            sanction.setAvailableAmount(sanction.getAvailableAmount() + disbursement.getGrossAmount());
            enrichmentService.getAuditDetails(senderId, disbursement.getAuditDetails());
            log.info("Sanctioned amount increased for Failed or Error disbursement with id: {}", disbursement.getId());
            return sanction;
        } else {
            return null;
        }
    }

    /**
     * Searches the db for disbursement on target id and if any previous disbursement is partial then return null
     * If sanction id is present, decrease the available amount and return the sanction else find a sanction with
     * available amount more than disbursement amount and decrease the available amount and return the sanction.
     *
     * @param disbursement
     * @param senderId
     * @return
     */
    public Sanction calculateAndReturnSanctionForDisburse(Disbursement disbursement, String senderId) {
        Sanction sanction = null;
        //Search disburse and return null if targetId is already present in db
        List<Disbursement> disbursementsFromDB = disburseRepository.searchDisbursements(DisburseSearch.builder()
                .targetId(disbursement.getTargetId()).build());

        if (!disbursementsFromDB.isEmpty()) {
            List<Disbursement> originalDisbursementsFromDB = filterDisbursements(disbursementsFromDB);
            List<Disbursement> disbursementWithPartialStatuses = originalDisbursementsFromDB.stream()
                    .filter(disbursementFromDB -> disbursementFromDB.getStatus().getStatusCode().equals(Status.PARTIAL) || disbursementFromDB.getStatus().getStatusCode().equals(Status.COMPLETED))
                    .collect(Collectors.toList());
            Disbursement disbursementWithPartialStatus = null;
            if (!CollectionUtils.isEmpty(disbursementWithPartialStatuses) && isValidForCORPIRequest(disbursement, disbursementWithPartialStatuses.get(0)))
                disbursementWithPartialStatus = disbursementWithPartialStatuses.get(0);

            if (disbursementWithPartialStatus != null) {
                disbursement.setSanctionId(disbursementWithPartialStatus.getSanctionId());
                for (Disbursement childDisbursement : disbursement.getDisbursements())
                    childDisbursement.setSanctionId(disbursementWithPartialStatus.getSanctionId());
                return sanction;
            }
        }
        log.info("Calculating Sanction for disburse");
        if (disbursement.getSanctionId() != null) {
            SanctionSearch sanctionSearch = SanctionSearch.builder().ids(Collections.singletonList(disbursement.getSanctionId())).build();
            sanction = sanctionRepository.searchSanction(sanctionSearch, false).get(0);
        } else {
            SanctionSearch sanctionSearch = SanctionSearch.builder().locationCode(disbursement.getLocationCode())
                    .programCode(disbursement.getProgramCode()).build();
            List<Sanction> sanctions = sanctionRepository.searchSanction(sanctionSearch, false);

            for (Sanction sanctionFromDB : sanctions) {
                if (sanctionFromDB.getAvailableAmount().compareTo(disbursement.getGrossAmount()) >= 0) {
                    sanction = sanctionFromDB;
                    break;
                }
            }

            if (sanction == null) {
                throw new CustomException(NO_SANCTION_AVAILABLE_FOR_AMOUNT, NO_SANCTION_AVAILABLE_FOR_AMOUNT_MSG +
                        disbursement.getGrossAmount());
            }

            disbursement.setSanctionId(sanction.getId());
            for (Disbursement childDisbursement : disbursement.getDisbursements())
                childDisbursement.setSanctionId(sanction.getId());
        }

        sanction.setAvailableAmount(sanction.getAvailableAmount() - disbursement.getGrossAmount());

        sanction.setAuditDetails(enrichmentService.getAuditDetails(senderId, sanction.getAuditDetails()));
        log.info("Sanction calculated for disburse");
        return sanction;
    }

    /**
     * For list of allocations fetch the sanctions and calculate sanctioned or deducted amount and update the
     * sanctioned amount and available amount and return list of sanctions.
     * @param allocations
     * @return
     */
    public List<Sanction> calculateAndReturnSanctionForAllocation(List<Allocation> allocations) {
        log.info("Calculating Sanction for allocation");
        Set<String> sanctionIds = allocations.stream().map(Allocation::getSanctionId).collect(Collectors.toSet());
        Map<String, Sanction> sanctionIdVsSanction = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).build(), false).stream().collect(Collectors.toMap(Sanction::getId, sanction -> sanction));
        for (Allocation allocation : allocations) {
            if (allocation.getAllocationType().equals(AllocationType.ALLOCATION)) {
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAllocatedAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAllocatedAmount() + allocation.getGrossAmount());
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAvailableAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAvailableAmount() + allocation.getGrossAmount());
            } else {
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAllocatedAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAllocatedAmount() - allocation.getGrossAmount());
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAvailableAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAvailableAmount() - allocation.getGrossAmount());
            }
        }
        log.info("Sanction calculated for allocation");
        return new ArrayList<>(sanctionIdVsSanction.values());
    }

    public List<Disbursement> filterDisbursements(List<Disbursement> disbursementsFromDB) {

        return disbursementsFromDB.stream()
                .filter(disbursementFromDB -> {
                    Object additionalDetailsObj = disbursementFromDB.getAdditionalDetails();
                    if (additionalDetailsObj instanceof JsonNode) {
                        JsonNode additionalDetails = (JsonNode) additionalDetailsObj;
                        JsonNode isRevisedNode = additionalDetails.get("isRevised");
                        return isRevisedNode != null && !isRevisedNode.asBoolean();
                    }
                    return false;
                })
                .sorted(Comparator.comparing(disbursementFromDB -> disbursementFromDB.getAuditDetails().getCreatedTime(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    /**
     * Method to convert epochDateTimeFormat to LocalDateTime
     * @param epochTime The epoch time in milliseconds
     * @return The corresponding LocalDateTime object
     */
    private LocalDateTime convertEpochToLocalDateTime(long epochTime) {
        Instant instant = Instant.ofEpochMilli(epochTime);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Method to get the financial year of the given date
     * @param date The date for which the financial year is needed
     * @return The financial year in the format "YYYY-YYYY"
     */
    private String getFinancialYear(LocalDateTime date){

        int year = date.getYear();
        int month = date.getMonthValue();

        // Check if the date falls in the current or next financial year
        int startYear, endYear;
        if (month < 4) { // Before April, so belongs to the previous financial year
            startYear = year - 1;
            endYear = year;
        } else {
            startYear = year;
            endYear = year + 1;
        }

        return startYear + "-" + endYear;
    }

    /**
     * This method validates the expiry time scenario and return true is COR PI request is valid else return false to take new sanction
     * @param currentDisbursement The payment instruction received from the disbursement
     * @param originalDisbursement The last payment instruction
     * @return The status of the payment
     */
    private boolean isValidForCORPIRequest(Disbursement currentDisbursement, Disbursement originalDisbursement) {

        LocalDateTime originalPICreatedDate = convertEpochToLocalDateTime(originalDisbursement.getAuditDetails().getCreatedTime());
        LocalDateTime corPICreatedDate = convertEpochToLocalDateTime(currentDisbursement.getAuditDetails().getCreatedTime());
        LocalDateTime failureDatePlusExpiryDays = originalPICreatedDate.plusDays(configs.getOriginalExpireDays());

        // Check if financial year of COR PI Request createdDate and OriginalPI createdDate is same
        if (getFinancialYear(originalPICreatedDate).equals(getFinancialYear(corPICreatedDate))) {

            // Check if corPICreatedDate <= (originalPIFailedDate + expiry days)
            if (corPICreatedDate.isBefore(failureDatePlusExpiryDays) || corPICreatedDate.isEqual(failureDatePlusExpiryDays)) {
                // Normal Flow
                log.info("Payment Instruction is valid for Correction PI Request for same financial year.");
                return true;
            } else {
                // Create New PI
                log.info("New Payment Instruction is created due to expiry days scenario.");
                return false;
            }

        } else {  // If financial year is not same

            // Check if (corPICreatedDate <= (originalPIFailedDate + 90 days)) and corPICreatedDate <= 30th April 23:59:59
            LocalDateTime endOfApril = LocalDateTime.of(corPICreatedDate.getYear(),
                    configs.getOriginalExpireFinancialYearMonth(), configs.getOriginalExpireFinancialYearDate(), 23, 59, 59);
            if (!corPICreatedDate.isAfter(endOfApril)) {
                // Normal flow
                log.info("Payment Instruction is valid for Correction PI Request.");
                return true;
            } else {
                // Create new PI
                log.info("New sanction is taken due to expiry days or 30th April scenario");
                return false;
            }

        }
    }
}
