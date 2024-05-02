package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.MessageType;
import org.digit.program.constants.Status;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.program.Program;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.utils.CommonUtil;
import org.digit.program.utils.IdGenUtil;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.digit.program.constants.ProgramConstants.*;

@Service
@Slf4j
public class EnrichmentService {

    private final IdGenUtil idGenUtil;
    private final ProgramConfiguration configs;
    private final CommonUtil commonUtil;

    public EnrichmentService(IdGenUtil idGenUtil, ProgramConfiguration configs, CommonUtil commonUtil) {
        this.idGenUtil = idGenUtil;
        this.configs = configs;
        this.commonUtil = commonUtil;
    }

    /**
     * Enriches data required for create program if not already present
     * @param header
     * @param program
     */
    public void enrichProgramForCreate(RequestHeader header, Program program) {
        log.info("Enriching Program for Create");
        if (commonUtil.isSameDomain(header.getReceiverId(), configs.getDomain())) {
            program.setProgramCode(idGenUtil.getIdList(RequestInfo.builder().build(), program.getLocationCode(),
                    configs.getIdName(), "", 1).get(0));
            program.setStatus(org.digit.program.models.Status.builder().statusCode(Status.APPROVED).build());
        } else {
            if (program.getId() == null || StringUtils.isEmpty(program.getId()))
                program.setId(UUID.randomUUID().toString());
            program.setStatus(org.digit.program.models.Status.builder().statusCode(Status.INITIATED).build());
        }
        program.setType(MessageType.PROGRAM);
        program.setStatus(org.digit.program.models.Status.builder().statusCode(Status.ACTIVE).build());
        program.setAuditDetails(getAuditDetails(header.getSenderId(), null));
        header.setMessageId(program.getId());
        header.setMessageTs(System.currentTimeMillis());
        log.info("Enrichment for create completed for id: {}", program.getId());
    }

    /**
     * Enriches audit details for program update and reply
     * @param program
     * @param header
     */
    public void enrichProgramForUpdateOrOnProgram(Program program, RequestHeader header) {
        log.info("Enrich Program for Update/OnProgram");
        program.setAuditDetails(getAuditDetails(header.getSenderId(), program.getAuditDetails()));
        header.setMessageId(program.getId());
        header.setMessageTs(System.currentTimeMillis());
        log.info("Enrichment for update/on-program completed for id: {}", program.getId());
    }

    /**
     * Set audit details and id if not present for sanction create
     * @param sanction
     * @param header
     */
    public void enrichSanctionCreate(Sanction sanction, RequestHeader header) {
        log.info("Enrich sanction create");
        for (Sanction childSanctions : sanction.getChildren()) {
            if (childSanctions.getId() == null || StringUtils.isEmpty(childSanctions.getId()))
                childSanctions.setId(UUID.randomUUID().toString());
            childSanctions.setType(MessageType.SANCTION);
            childSanctions.setAuditDetails(getAuditDetails(header.getSenderId(), null));
        }
        sanction.setId(UUID.randomUUID().toString());
        sanction.setType(MessageType.SANCTION);
        sanction.setAuditDetails(getAuditDetails(header.getSenderId(), null));
        header.setMessageId(sanction.getId());
        header.setMessageTs(System.currentTimeMillis());
        log.info("Enrichment for create sanction completed");
    }

    /**
     * Setting audit details for sanction update
     * @param sanctions
     * @param senderId
     */
    public void enrichSanctionUpdate(List<Sanction> sanctions, String senderId) {
        for (Sanction sanction : sanctions) {
            sanction.setAuditDetails(getAuditDetails(senderId, sanction.getAuditDetails()));
        }
        log.info("Enrichment for update sanction completed");
    }

    /**
     * Setting audit details and id if not present for allocation create
     * @param allocation
     * @param header
     */
    public void enrichAllocationCreate(Allocation allocation, RequestHeader header) {
        for (Allocation childAllocations : allocation.getChildren()) {
            if (childAllocations.getId() == null || StringUtils.isEmpty(childAllocations.getId()))
                childAllocations.setId(UUID.randomUUID().toString());
            childAllocations.setType(MessageType.ALLOCATION);
            childAllocations.setAuditDetails(getAuditDetails(header.getSenderId(), null));
        }
        allocation.setId(UUID.randomUUID().toString());
        allocation.setType(MessageType.ALLOCATION);
        allocation.setAuditDetails(getAuditDetails(header.getSenderId(), null));
        header.setMessageId(allocation.getId());
        header.setMessageTs(System.currentTimeMillis());
        log.info("Enrichment for create allocation completed");
    }

    /**
     * Setting audit details for allocation update
     * @param allocations
     * @param senderId
     */
    public void enrichAllocationUpdate(List<Allocation> allocations, String senderId) {
        for (Allocation allocation : allocations) {
            allocation.setAuditDetails(getAuditDetails(senderId, allocation.getAuditDetails()));
        }
        log.info("Enrichment for update allocation completed");
    }

    /**
     * Setting id(if not present) and audit details for disbursement and child disbursement and status for create
     * @param disbursement
     * @param header
     */
    public void enrichDisburseCreate(Disbursement disbursement, RequestHeader header) {
        log.info("Enrich disburse create");
        if (disbursement.getId() == null || StringUtils.isEmpty(disbursement.getId()))
            disbursement.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = getAuditDetails(header.getSenderId(), null);
        disbursement.setAuditDetails(auditDetails);
        disbursement.setStatus(org.digit.program.models.Status.builder().statusCode(Status.INITIATED).build());
        disbursement.setType(MessageType.DISBURSE);
        header.setMessageId(disbursement.getId());
        header.setMessageTs(System.currentTimeMillis());
        for (Disbursement childDisbursement : disbursement.getDisbursements()) {
            if (childDisbursement.getId() == null || StringUtils.isEmpty(childDisbursement.getId()))
                childDisbursement.setId(UUID.randomUUID().toString());
            childDisbursement.setAuditDetails(auditDetails);
            childDisbursement.setStatus(org.digit.program.models.Status.builder().statusCode(Status.INITIATED).build());
            childDisbursement.setType(MessageType.DISBURSE);
        }
        log.info("Enrichment for create completed for id: {}", disbursement.getId());
    }

    /**
     * Setting audit details for disbursement and child disbursement and status for update
     * @param disbursement
     * @param header
     * @param isReply
     */
    public void enrichDisburseUpdate(Disbursement disbursement, RequestHeader header, Boolean isReply) {
        log.info("Enrich disburse update");
        AuditDetails auditDetails = getAuditDetails(header.getSenderId(), disbursement.getAuditDetails());
        disbursement.setAuditDetails(auditDetails);
        for (Disbursement childDisbursement : disbursement.getDisbursements()) {
            childDisbursement.setAuditDetails(auditDetails);
        }
        if (Boolean.TRUE.equals(isReply)) {
            List<Status> statuses = disbursement.getDisbursements().stream()
                    .map(disbursement1 -> disbursement1.getStatus().getStatusCode()).distinct().collect(Collectors.toList());
            if (statuses.contains(Status.FAILED) && statuses.contains(Status.SUCCESSFUL)) {
                if (disbursement.getStatus() != null && disbursement.getStatus().getStatusCode() != null &&
                        !disbursement.getStatus().getStatusCode().equals(Status.COMPLETED)) {
                    disbursement.getStatus().setStatusCode(Status.PARTIAL);
                }
            }
        }
        header.setMessageId(disbursement.getId());
        log.info("Enrichment for update completed for id: {}", disbursement.getId());
    }

    /**
     * Setting audit details
     * @param senderId
     * @param prevAuditDetails
     * @return
     */
    public AuditDetails getAuditDetails(String senderId, AuditDetails prevAuditDetails) {
        Long time = System.currentTimeMillis();
        if (prevAuditDetails == null) {
            return AuditDetails.builder()
                    .createdBy(senderId)
                    .createdTime(time)
                    .lastModifiedBy(senderId)
                    .lastModifiedTime(time).build();
        } else {
            prevAuditDetails.setLastModifiedTime(time);
            prevAuditDetails.setLastModifiedBy(senderId);
            return prevAuditDetails;
        }
    }
}
