package org.digit.program.utils;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.Status;
import org.digit.program.kafka.ProgramProducer;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
    private final ProgramProducer producer;
    private final DispatcherUtil dispatcherUtil;
    private final ProgramConfiguration configs;
    private final CommonUtil commonUtil;

    public ErrorHandler(ProgramProducer producer, DispatcherUtil dispatcherUtil, ProgramConfiguration configs,
                        CommonUtil commonUtil) {
        this.producer = producer;
        this.dispatcherUtil = dispatcherUtil;
        this.configs = configs;
        this.commonUtil = commonUtil;
    }

    public void handleProgramError(ProgramRequest programRequest, CustomException exception) {
        programRequest.getProgram().setStatus(setErrorStatus(programRequest.getProgram().getStatus(), exception));
        commonUtil.updateUri(programRequest.getHeader());
        try {
            dispatcherUtil.dispatchOnProgram(programRequest);
        } catch (Exception e) {
            producer.push(configs.getErrorTopic(), programRequest);
        }
    }

    public void handleProgramReplyError(ProgramRequest programRequest, CustomException exception) {
        programRequest.getProgram().setStatus(setErrorStatus(programRequest.getProgram().getStatus(), exception));
        producer.push(configs.getErrorTopic(), programRequest);
    }

    public void handleSanctionError(SanctionRequest sanctionRequest, CustomException exception) {
        for (Sanction sanction : sanctionRequest.getSanctions()) {
            sanction.setStatus(setErrorStatus(sanction.getStatus(), exception));
        }
        producer.push(configs.getErrorTopic(), sanctionRequest);
    }

    public void handleAllocationError(AllocationRequest allocationRequest, CustomException exception) {
        for (Allocation allocation : allocationRequest.getAllocations()) {
            allocation.setStatus(setErrorStatus(allocation.getStatus(), exception));
        }
        producer.push(configs.getErrorTopic(), allocationRequest);
    }

    public void handleDisburseError(DisbursementRequest disbursementRequest, CustomException exception) {
        for (Disbursement disbursement : disbursementRequest.getDisbursement().getDisbursements()) {
            disbursement.setStatus(setErrorStatus(disbursement.getStatus(), exception));
        }
        disbursementRequest.getDisbursement().setStatus(setErrorStatus(disbursementRequest.getDisbursement().getStatus(), exception));
        commonUtil.updateUri(disbursementRequest.getHeader());
        try {
            dispatcherUtil.dispatchDisburse(disbursementRequest);
        } catch (Exception e) {
            producer.push(configs.getErrorTopic(), disbursementRequest);
        }
    }

    public void handleOnDisburseError(DisbursementRequest disbursementRequest, CustomException exception) {
        disbursementRequest.getDisbursement().setStatus(setErrorStatus(disbursementRequest.getDisbursement().getStatus(), exception));
        producer.push(configs.getErrorTopic(), disbursementRequest);
    }

    /**
     * Sets error status and message. If status is null, creates a new status with required parameters.
     * @param status
     * @param exception
     * @return
     */
    public org.digit.program.models.Status setErrorStatus(org.digit.program.models.Status status, CustomException exception) {
        if (status == null)
            return new org.digit.program.models.Status(Status.ERROR, exception.getCode() + " " + exception.getMessage());
        status.setStatusCode(Status.ERROR);
        status.setStatusMessage(exception.getCode() + " " + exception.getMessage());
        return status;
    }

}
