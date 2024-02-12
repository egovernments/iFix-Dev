package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.SortOrder;
import org.digit.program.constants.Status;
import org.digit.program.models.*;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.program.Program;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.utils.IdGenUtil;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (header.getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain())) {
            program.setProgramCode(idGenUtil.getIdList(RequestInfo.builder().build(), program.getLocationCode(), configs.getIdName(), "", 1).get(0));
            program.setStatus(org.digit.program.models.Status.builder().statusCode(Status.APPROVED).build());
        } else {
            if (program.getId() == null || StringUtils.isEmpty(program.getId()))
                program.setId(UUID.randomUUID().toString());
            program.setClientHostUrl(configs.getDomain());
            program.setStatus(org.digit.program.models.Status.builder().statusCode(Status.INITIATED).build());
        }
        program.setActive(true);
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).createdBy("a").lastModifiedBy("b").build();
        program.setAuditDetails(auditDetails);
        log.info("Enrichment for create completed");
    }

    public void enrichProgramForUpdate(Program program) {
        log.info("Enrich Program for Update");
        program.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        program.getAuditDetails().setLastModifiedBy("b");
        log.info("Enrichment for update completed");
    }

    public void enrichProgramForOnProgram(Program program) {
        log.info("Enrich Program for OnProgram");
        program.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        program.getAuditDetails().setLastModifiedBy("b");
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

    public void enrichSanctionCreate(List<Sanction> sanctions, String receiverId) {
        log.info("Enrich sanction create");
        if (!receiverId.split("@")[1].equalsIgnoreCase(configs.getDomain())) {
            for (Sanction sanction : sanctions) {
                if (sanction.getId() == null || StringUtils.isEmpty(sanction.getId()))
                    sanction.setId(UUID.randomUUID().toString());
                AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
                sanction.setAuditDetails(auditDetails);
            }
        }
    }

    public void enrichSanctionUpdate(List<Sanction> sanctions) {
        log.info("Enrich sanction update");
        for (Sanction sanction : sanctions) {
            AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(System.currentTimeMillis()).lastModifiedBy("b").build();
            sanction.setAuditDetails(auditDetails);
        }
    }

    public void enrichAllocationCreate(List<Allocation> allocations, String receiverId) {
        log.info("Enrich allocation create");
        for (Allocation allocation : allocations) {
            if (!receiverId.split("@")[1].equalsIgnoreCase(configs.getDomain())) {
                if (allocation.getId() == null || StringUtils.isEmpty(allocation.getId()))
                    allocation.setId(UUID.randomUUID().toString());
                AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
                allocation.setAuditDetails(auditDetails);
            } else {
                allocation.getAuditDetails().setLastModifiedBy("b");
                allocation.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            }
        }
    }

    public void enrichAllocationUpdate(List<Allocation> allocations) {
        log.info("Enrich allocation update");
        for (Allocation allocation : allocations) {
            allocation.getAuditDetails().setLastModifiedBy("b");
            allocation.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        }
    }

    public void enrichDisburseCreate(Disbursement disbursement, String receiverId) {
        log.info("Enrich disburse create");
        if (!receiverId.split("@")[1].equalsIgnoreCase(configs.getDomain())) {
            if (disbursement.getId() == null || StringUtils.isEmpty(disbursement.getId()))
                disbursement.setId(UUID.randomUUID().toString());
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                if (childDisbursement.getId() == null || StringUtils.isEmpty(childDisbursement.getId()))
                    childDisbursement.setId(UUID.randomUUID().toString());
                AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
                childDisbursement.setAuditDetails(auditDetails);
            }
        }
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).build();
        disbursement.setAuditDetails(auditDetails);
    }

    public void enrichDisburseUpdate(Disbursement disbursement) {
        log.info("Enrich disburse update");
        disbursement.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        disbursement.getAuditDetails().setLastModifiedBy("b");
    }

}
