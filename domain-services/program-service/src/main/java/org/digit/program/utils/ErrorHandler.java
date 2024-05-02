package org.digit.program.utils;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("Handling Program Error");
        programRequest.getProgram().setStatus(setErrorStatus(programRequest.getProgram().getStatus(), exception));
        commonUtil.updateUri(programRequest.getHeader());
        try {
            dispatcherUtil.dispatchOnProgram(programRequest);
            log.info("Program Error dispatched");
        } catch (Exception e) {
            log.error("Error while dispatching program", e);
            producer.push(configs.getErrorTopic(), programRequest);
        }
    }

    public void handleProgramReplyError(ProgramRequest programRequest, CustomException exception) {
        log.info("Handling Program Reply Error");
        programRequest.getProgram().setStatus(setErrorStatus(programRequest.getProgram().getStatus(), exception));
        producer.push(configs.getErrorTopic(), programRequest);
        log.info("Program Reply Error pushed to error topic");
    }

    public void handleSanctionError(SanctionRequest sanctionRequest, CustomException exception) {
        log.info("Handling Sanction Error");
        for (Sanction sanction : sanctionRequest.getSanction().getChildren()) {
            sanction.setStatus(setErrorStatus(sanction.getStatus(), exception));
        }
        producer.push(configs.getErrorTopic(), sanctionRequest);
        log.info("Sanction Error pushed to error topic");
    }

    public void handleAllocationError(AllocationRequest allocationRequest, CustomException exception) {
        log.info("Handling Allocation Error");
        for (Allocation allocation : allocationRequest.getAllocation().getChildren()) {
            allocation.setStatus(setErrorStatus(allocation.getStatus(), exception));
        }
        producer.push(configs.getErrorTopic(), allocationRequest);
        log.info("Allocation Error pushed to error topic");
    }

    public void handleDisburseError(DisbursementRequest disbursementRequest, CustomException exception) {
        log.info("Handling Disburse Error");
        for (Disbursement disbursement : disbursementRequest.getDisbursement().getDisbursements()) {
            disbursement.setStatus(setErrorStatus(disbursement.getStatus(), exception));
        }
        disbursementRequest.getDisbursement().setStatus(setErrorStatus(disbursementRequest.getDisbursement().getStatus(), exception));
        commonUtil.updateUri(disbursementRequest.getHeader());
        try {
            dispatcherUtil.dispatchDisburse(disbursementRequest);
            log.info("Disburse Error dispatched");
        } catch (Exception e) {
            log.error("Error while dispatching error disbursement", e);
            producer.push(configs.getErrorTopic(), disbursementRequest);
        }
    }

    public void handleDisburseWhenIfmsAdapterThrowsError (DisbursementRequest disbursementRequest, CustomException exception) {
        commonUtil.updateUri(disbursementRequest.getHeader());
        for (Disbursement disbursement : disbursementRequest.getDisbursement().getDisbursements()) {
            disbursement.setStatus(setErrorStatus(disbursement.getStatus(), exception));
        }
        disbursementRequest.getDisbursement().setStatus(setErrorStatus(disbursementRequest.getDisbursement().getStatus(), exception));
    }

    public void handleDisburseReplyError(DisbursementRequest disbursementRequest, CustomException exception) {
        log.info("Handling Disburse Reply Error");
        disbursementRequest.getDisbursement().setStatus(setErrorStatus(disbursementRequest.getDisbursement().getStatus(), exception));
        for (Disbursement disbursement : disbursementRequest.getDisbursement().getDisbursements()) {
            disbursement.setStatus(setErrorStatus(disbursement.getStatus(), exception));
        }
        producer.push(configs.getErrorTopic(), disbursementRequest);
        log.info("Disburse Reply Error pushed to error topic");
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
