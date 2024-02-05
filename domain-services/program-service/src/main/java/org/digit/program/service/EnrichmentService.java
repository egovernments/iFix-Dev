package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.SortOrder;
import org.digit.program.constants.Status;
import org.digit.program.models.*;
import org.digit.program.utils.IdGenUtil;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EnrichmentService {

    private final IdGenUtil idGenUtil;
    private final ProgramConfiguration configs;

    public EnrichmentService(IdGenUtil idGenUtil, ProgramConfiguration configs) {
        this.idGenUtil = idGenUtil;
        this.configs = configs;
    }

    public void enrichProgramForCreate(RequestHeader header, Program program) {
        log.info("Enrich Program for Create");
        if (header.getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            program.setProgramCode(idGenUtil.getIdList(RequestInfo.builder().build(), program.getLocationCode(), configs.getIdName(), "", 1).get(0));
        else
            program.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).createdBy("a").lastModifiedBy("b").build();
        program.setAuditDetails(auditDetails);
        org.digit.program.models.Status status = org.digit.program.models.Status.builder().statusCode(Status.RECEIVED).build();
        program.setStatus(status);
        log.info("Enrichment for create completed");
    }

    public void enrichProgramForUpdate(Program program) {
        log.info("Enrich Program for Update");
        AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(System.currentTimeMillis()).lastModifiedBy("b").build();
        program.setAuditDetails(auditDetails);
        log.info("Enrichment for update completed");
    }

    public Pagination enrichSearch(Pagination pagination) {
        log.info("Enrich Program for Search");
        if (pagination == null)
            pagination = Pagination.builder().build();

        if (pagination.getLimit() == null) {
            pagination.setLimit(configs.getSearchDefaultLimit());
        } else if (pagination.getLimit() > configs.getSearchMaxLimit()) {
            pagination.setLimit(configs.getSearchMaxLimit());
        }
        if (pagination.getOffset() == null) {
            pagination.setOffset(0);
        }
        if (StringUtils.isEmpty(pagination.getSortBy())) {
            pagination.setSortBy("last_modified_time");
        }
        if (pagination.getSortOrder() == null) {
            pagination.setSortOrder(SortOrder.DESC);
        }
        log.info("Enrichment for search completed");
        return pagination;
    }

    public void enrichSanctionCreate(Sanction sanction, String receiverId) {
        log.info("Enrich sanction create");
        if (!receiverId.split("@")[1].equalsIgnoreCase(configs.getDomain()))
            sanction.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
        sanction.setAuditDetails(auditDetails);
    }

    public void enrichSanctionUpdate(Sanction sanction) {
        log.info("Enrich sanction update");
        AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(System.currentTimeMillis()).lastModifiedBy("b").build();
        sanction.setAuditDetails(auditDetails);
    }

    public Allocation enrichAllocationCreate(Allocation allocation, String receiverId) {
        log.info("Enrich allocation create");
        if (!receiverId.split("@")[1].equalsIgnoreCase(configs.getDomain()))
            allocation.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
        allocation.setAuditDetails(auditDetails);
        return allocation;
    }

    public Disbursement enrichDisburseCreate(Disbursement disbursement, String receiverId) {
        log.info("Enrich disburse create");
        if (!receiverId.split("@")[1].equalsIgnoreCase(configs.getDomain()))
            disbursement.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
        disbursement.setAuditDetails(auditDetails);
        return disbursement;
    }

}
