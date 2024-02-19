package org.digit.program.validator;

import org.digit.program.constants.Status;
import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.repository.DisburseRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DisbursementValidator {

    private final DisburseRepository disburseRepository;

    public DisbursementValidator(DisburseRepository disburseRepository) {
        this.disburseRepository = disburseRepository;
    }

    /**
     * Validates disbursement amount, child disbursements and id
     * @param disbursement
     * @param isCreate
     */
    public void validateDisbursement(Disbursement disbursement, Boolean isCreate) {
        List<Disbursement> childDisbursements = disbursement.getDisbursements();
        for (Disbursement childDisbursement : childDisbursements) {
            validateChildDisbursement(childDisbursement);
        }
        validateAmount(disbursement);
        validateId(disbursement, isCreate);
        validateTargetId(disbursement);
    }

    /**
     * Validates individual and account code for child disbursement
     * @param disbursement
     */
    public void validateChildDisbursement(Disbursement disbursement) {
        if (disbursement.getIndividual() == null)
            throw new CustomException("INDIVIDUAL_ERROR", "Individual should not be null");
        if (disbursement.getAccountCode() == null || disbursement.getAccountCode().isEmpty())
            throw new CustomException("ACCOUNT_CODE_ERROR", "Account code should not be null or empty");
        if (disbursement.getDisbursements() != null)
            throw new CustomException("DISBURSEMENT_ERROR", "Currently child disbursement should not have child disbursements");
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
     * Validates if disbursement exists in workflow for target id
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

}
