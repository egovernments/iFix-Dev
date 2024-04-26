package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Status;
import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.repository.SanctionRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.digit.program.constants.Error.*;

@Component
@Slf4j
public class DisbursementValidator {

    private final DisburseRepository disburseRepository;
    private final SanctionRepository sanctionRepository;

    public DisbursementValidator(DisburseRepository disburseRepository, SanctionRepository sanctionRepository) {
        this.disburseRepository = disburseRepository;
        this.sanctionRepository = sanctionRepository;
    }

    /**
     * Validates disbursement amount, child disbursements and id
     * @param disbursement
     * @param isCreate
     */
    public void validateDisbursement(Disbursement disbursement, Boolean isCreate, Boolean isOnDisburseCreate) {
        log.info("Validating disbursement");
        List<Disbursement> childDisbursements = disbursement.getDisbursements();
        if (childDisbursements == null || childDisbursements.isEmpty())
            throw new CustomException(CHILD_DISBURSEMENT_ERROR, CHILD_DISBURSEMENT_ERROR_MSG);
        for (Disbursement childDisbursement : childDisbursements) {
            validateChildDisbursement(childDisbursement);
        }
        validateChildDisbursements(disbursement, childDisbursements);
        validateAmount(disbursement);
        validateId(disbursement, isCreate);
        if (Boolean.TRUE.equals(isCreate)) {
            List<Disbursement> disbursementsFromDB = validateTargetId(disbursement);
            validateSanctionAmount(disbursement, disbursementsFromDB);
        }

        if (Boolean.TRUE.equals(isOnDisburseCreate))
            validateTransactionId(disbursement);
        log.info("Validation of disbursement completed");
    }

    /**
     * Validates individual and account code for child disbursement
     * @param disbursement
     */
    public void validateChildDisbursement(Disbursement disbursement) {
        if (disbursement == null)
            throw new CustomException(CHILD_DISBURSEMENT_ERROR, CHILD_DISBURSEMENT_ERROR_MSG);
        if (disbursement.getIndividual() == null)
            throw new CustomException(INDIVIDUAL_ERROR, INDIVIDUAL_ERROR_MSG);
        if (disbursement.getAccountCode() == null || disbursement.getAccountCode().isEmpty())
            throw new CustomException(ACCOUNT_CODE_ERROR, ACCOUNT_CODE_ERROR_MSG);
        if (disbursement.getDisbursements() != null)
            throw new CustomException(MULTIPLE_CHILD_DISBURSEMENT_ERROR, MULTIPLE_CHILD_DISBURSEMENT_ERROR_MSG);
    }

    /**
     * Validate if disbursement and child disbursement have same location_code and program_code
     * @param disbursement
     * @param childDisbursements
     */
    public void validateChildDisbursements(Disbursement disbursement, List<Disbursement> childDisbursements) {
        List<String> locationCodes = childDisbursements.stream().map(Disbursement::getLocationCode).distinct().collect(Collectors.toList());
        if (locationCodes.size() > 1)
            throw new CustomException(DISBURSEMENT_LOCATION_CODE_ERROR, DISBURSEMENT_LOCATION_CODE_ERROR_MSG);
        List<String> programCodes = childDisbursements.stream().map(Disbursement::getProgramCode).distinct().collect(Collectors.toList());
        if (programCodes.size() > 1)
            throw new CustomException(DISBURSEMENT_PROGRAM_CODE_ERROR, DISBURSEMENT_PROGRAM_CODE_ERROR_MSG);

        if (!disbursement.getLocationCode().equals(locationCodes.get(0)))
            throw new CustomException(DISBURSEMENT_LOCATION_CODE_ERROR, DISBURSEMENT_LOCATION_CODE_ERROR_MSG);
        if (!disbursement.getProgramCode().equals(programCodes.get(0)))
            throw new CustomException(DISBURSEMENT_PROGRAM_CODE_ERROR, DISBURSEMENT_PROGRAM_CODE_ERROR_MSG);
    }

    /**
     * Validates if disbursement amount is equal to sum of child disbursement amount
     * @param disbursement
     */
    public void validateAmount(Disbursement disbursement) {
        Double netAmountSum = 0d;
        Double grossAmountSum = 0d;
        for (Disbursement childDisbursement : disbursement.getDisbursements()) {
            netAmountSum += childDisbursement.getNetAmount();
            grossAmountSum += childDisbursement.getGrossAmount();
        }
        if (Double.compare(disbursement.getNetAmount(), netAmountSum) != 0)
            throw new CustomException(DISBURSEMENT_NET_AMOUNT_ERROR, DISBURSEMENT_NET_AMOUNT_ERROR_MSG);
        if (Double.compare(disbursement.getGrossAmount(), grossAmountSum) != 0)
            throw new CustomException(DISBURSEMENT_GROSS_AMOUNT_ERROR, DISBURSEMENT_GROSS_AMOUNT_ERROR_MSG);
    }

    /**
     * Validates sanction and available amount for given sanction id
     * @param disbursement
     */
    public void validateSanctionAmount(Disbursement disbursement, List<Disbursement> disbursementsFromDB) {
        if (disbursement.getSanctionId() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                if (childDisbursement.getSanctionId() == null || !childDisbursement.getSanctionId().equalsIgnoreCase(disbursement.getSanctionId()))
                    throw new CustomException(DISBURSEMENT_SANCTION_ID_ERROR, DISBURSEMENT_SANCTION_ID_ERROR_MSG);
            }
            List<Sanction> sanctions = sanctionRepository.searchSanction(SanctionSearch.builder()
                    .ids(Collections.singletonList(disbursement.getSanctionId()))
                    .locationCode(disbursement.getLocationCode())
                    .build(), false);
            if (sanctions.isEmpty())
                throw new CustomException(NO_SANCTION_FOUND, NO_SANCTION_FOUND_MSG + disbursement.getSanctionId());
            if(disbursementsFromDB.isEmpty()) {
                if (sanctions.get(0).getAvailableAmount() < disbursement.getGrossAmount())
                    throw new CustomException(SANCTION_AVAILABLE_AMOUNT_ERROR, SANCTION_AVAILABLE_AMOUNT_ERROR_MSG);
            } else {
                List<Status> statusesFromDb = disbursementsFromDB.stream().map(disbursement1 -> disbursement1.getStatus().getStatusCode()).collect(Collectors.toList());
                if (!statusesFromDb.contains(Status.PARTIAL)) {
                    if (sanctions.get(0).getAvailableAmount() < disbursement.getGrossAmount())
                        throw new CustomException(SANCTION_AVAILABLE_AMOUNT_ERROR, SANCTION_AVAILABLE_AMOUNT_ERROR_MSG);
                }
            }

        }
    }

    /**
     * Validates if disbursement id is unique for create and disbursement exists for update
     * @param disbursement
     * @param isCreate
     */
    public void validateId(Disbursement disbursement, Boolean isCreate) {
        if (Boolean.FALSE.equals(isCreate) && (disbursement.getId() == null || disbursement.getId().isEmpty()))
            throw new CustomException(DISBURSEMENT_ID_NULL_ERROR, DISBURSEMENT_ID_NULL_ERROR_MSG);
        if (disbursement.getId() == null || disbursement.getId().isEmpty())
            return;
        List<Disbursement> disbursementsFromSearch = disburseRepository.searchDisbursements(DisburseSearch.builder()
                .ids(Collections.singletonList(disbursement.getId())).build());

        if (Boolean.TRUE.equals(isCreate) && !disbursementsFromSearch.isEmpty())
            throw new CustomException(DISBURSEMENT_ID_ERROR, DISBURSEMENT_ID_ERROR_MSG);
        if (Boolean.FALSE.equals(isCreate) && disbursementsFromSearch.isEmpty())
            throw new CustomException(NO_DISBURSEMENT_FOUND, NO_DISBURSEMENT_FOUND_MSG + disbursement.getId());

        if (Boolean.FALSE.equals(isCreate)) {
            List<String> childDisbursementIds = disbursement.getDisbursements().stream().map(Disbursement::getId).collect(Collectors.toList());
            if (childDisbursementIds.contains(null))
                throw new CustomException(CHILD_DISBURSEMENT_ERROR, CHILD_DISBURSEMENT_ERROR_MSG);
            if (!disbursementsFromSearch.get(0).getDisbursements().stream().map(Disbursement::getId).collect(Collectors.toList()).containsAll(childDisbursementIds))
                throw new CustomException(CHILD_DISBURSEMENT_ID_ERROR, CHILD_DISBURSEMENT_ID_ERROR_MSG);
        }
    }

    /**
     * Validates if disbursement exists in workflow for target id for create
     * @param disbursement
     */
    public List<Disbursement> validateTargetId(Disbursement disbursement) {
        List<Disbursement> disbursementsFromDB = disburseRepository.searchDisbursements(DisburseSearch.builder()
                .targetId(disbursement.getTargetId()).build());
        List<Status> statuses = disbursementsFromDB.stream().map(disbursement1 -> disbursement1.getStatus()
                .getStatusCode()).collect(Collectors.toList());
        if (statuses.contains(Status.INITIATED) || statuses.contains(Status.INPROCESS))
            throw new CustomException(DISBURSEMENT_ALREADY_PRESENT_ERROR, DISBURSEMENT_ALREADY_PRESENT_ERROR_MSG
                    + disbursement.getTargetId());
        return disbursementsFromDB;
    }

    /**
     * Validates if transaction id is present for disbursement reply
     * @param disbursement
     */
    public void  validateTransactionId(Disbursement disbursement) {
        if (disbursement.getStatus().getStatusCode().equals(Status.INITIATED) || disbursement.getStatus().getStatusCode().equals(Status.FAILED)) {
            List<String> transactionIds = disbursement.getDisbursements().stream().map(Disbursement::getTransactionId).collect(Collectors.toList());
            transactionIds.add(disbursement.getTransactionId());
            if (transactionIds.contains(null))
                throw new CustomException(TRANSACTION_ID_MANDATORY, TRANSACTION_ID_MANDATORY_MSG);
        }

    }

}
