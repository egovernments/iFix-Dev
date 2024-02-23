package org.digit.program.validator;

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

@Component
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
        List<Disbursement> childDisbursements = disbursement.getDisbursements();
        for (Disbursement childDisbursement : childDisbursements) {
            validateChildDisbursement(childDisbursement);
        }
        validateChildDisbursements(disbursement, childDisbursements);
        validateAmount(disbursement);
        validateId(disbursement, isCreate);
        if (Boolean.TRUE.equals(isCreate)) {
            validateTargetId(disbursement);
            validateSanctionAmount(disbursement);
        }

        if (Boolean.TRUE.equals(isOnDisburseCreate))
            validateTransactionId(disbursement);
    }

    /**
     * Validates individual and account code for child disbursement
     * @param disbursement
     */
    public void validateChildDisbursement(Disbursement disbursement) {
        if (disbursement == null)
            throw new CustomException("DISBURSEMENT_ERROR", "Child disbursement should not be null");
        if (disbursement.getIndividual() == null)
            throw new CustomException("INDIVIDUAL_ERROR", "Individual should not be null");
        if (disbursement.getAccountCode() == null || disbursement.getAccountCode().isEmpty())
            throw new CustomException("ACCOUNT_CODE_ERROR", "Account code should not be null or empty");
        if (disbursement.getDisbursements() != null)
            throw new CustomException("DISBURSEMENT_ERROR", "Currently child disbursement should not have child disbursements");
    }

    /**
     * Validate if disbursement and child disbursement have same location_code and program_code
     * @param disbursement
     * @param childDisbursements
     */
    public void validateChildDisbursements(Disbursement disbursement, List<Disbursement> childDisbursements) {
        List<String> locationCodes = childDisbursements.stream().map(Disbursement::getLocationCode).distinct().collect(Collectors.toList());
        if (locationCodes.size() > 1)
            throw new CustomException("DISBURSEMENT_LOCATION_CODE_ERROR", "Disbursement location code should be same as child disbursement location code");
        List<String> programCodes = childDisbursements.stream().map(Disbursement::getProgramCode).distinct().collect(Collectors.toList());
        if (programCodes.size() > 1)
            throw new CustomException("DISBURSEMENT_PROGRAM_CODE_ERROR", "Disbursement program code should be same as child disbursement program code");

        if (!disbursement.getLocationCode().equals(locationCodes.get(0)))
            throw new CustomException("DISBURSEMENT_LOCATION_CODE_ERROR", "Disbursement location code should be same as child disbursement location code");
        if (!disbursement.getProgramCode().equals(programCodes.get(0)))
            throw new CustomException("DISBURSEMENT_PROGRAM_CODE_ERROR", "Disbursement program code should be same as child disbursement program code");
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
            throw new CustomException("DISBURSEMENT_NET_AMOUNT_ERROR", "Disbursement amount should be equal to child disbursement net amount");
        if (Double.compare(disbursement.getGrossAmount(), grossAmountSum) != 0)
            throw new CustomException("DISBURSEMENT_GROSS_AMOUNT_ERROR", "Disbursement amount should be equal to child disbursement gross amount");
    }


    public void validateSanctionAmount(Disbursement disbursement) {
        if (disbursement.getSanctionId() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                if (childDisbursement.getSanctionId() == null || !childDisbursement.getSanctionId().equalsIgnoreCase(disbursement.getSanctionId()))
                    throw new CustomException("DISBURSEMENT_SANCTION_ID_ERROR", "Disbursement sanction id should be same as child disbursement sanction id");
            }
            List<Sanction> sanctions = sanctionRepository.searchSanction(SanctionSearch.builder()
                    .ids(Collections.singletonList(disbursement.getSanctionId())).build());
            if (sanctions.isEmpty())
                throw new CustomException("NO_SANCTION_FOUND", "No sanction found for id: " + disbursement.getSanctionId());
            if (sanctions.get(0).getAvailableAmount() < disbursement.getNetAmount())
                throw new CustomException("SANCTION_AVAILABLE_AMOUNT_ERROR", "Sanction available amount should be greater than disbursement amount");
        }
    }

    /**
     * Validates if disbursement id is unique and disbursement exists for update
     * @param disbursement
     * @param isCreate
     */
    public void validateId(Disbursement disbursement, Boolean isCreate) {
        if (Boolean.FALSE.equals(isCreate) && (disbursement.getId() == null || disbursement.getId().isEmpty()))
            throw new CustomException("DISBURSEMENT_ID_ERROR", "Disbursement id should not be null or empty");
        if (disbursement.getId() == null || disbursement.getId().isEmpty())
            return;
        List<Disbursement> disbursementsFromSearch = disburseRepository.searchDisbursements(DisburseSearch.builder()
                .ids(Collections.singletonList(disbursement.getId())).build());

        if (Boolean.TRUE.equals(isCreate) && !disbursementsFromSearch.isEmpty())
            throw new CustomException("DISBURSEMENT_ID_ERROR", "Disbursement id should be unique");
        if (Boolean.FALSE.equals(isCreate) && disbursementsFromSearch.isEmpty())
            throw new CustomException("NO_DISBURSEMENT_FOUND", "No disbursement found for id: " + disbursement.getId());
    }

    /**
     * Validates if disbursement exists in workflow for target id for create
     * @param disbursement
     */
    public void validateTargetId(Disbursement disbursement) {
        List<Disbursement> disbursementsFromDB = disburseRepository.searchDisbursements(DisburseSearch.builder()
                .targetId(disbursement.getTargetId()).build());
        List<Status> statuses = disbursementsFromDB.stream().map(disbursement1 -> disbursement1.getStatus()
                .getStatusCode()).collect(Collectors.toList());
        if (statuses.contains(Status.INITIATED) || statuses.contains(Status.INPROCESS))
            throw new CustomException("DISBURSEMENT_ALREADY PRESENT_ERROR", "Disbursement already present for target id: "
                    + disbursement.getTargetId());
    }

    public void  validateTransactionId(Disbursement disbursement) {
        if (disbursement.getStatus().getStatusCode().equals(Status.INITIATED) || disbursement.getStatus().getStatusCode().equals(Status.FAILED)) {
            List<String> transactionIds = disbursement.getDisbursements().stream().map(Disbursement::getTransactionId).collect(Collectors.toList());
            transactionIds.add(disbursement.getTransactionId());
            if (transactionIds.contains(null))
                throw new CustomException("TRANSACTION_ID_MANDATORY", "Transaction id is mandatory");
        }

    }

}
