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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.digit.program.constants.ProgramConstants.ID_NAME;

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
        if (header.getReceiverId().equalsIgnoreCase(configs.getDomain()))
            program.setProgramCode(idGenUtil.getIdList(RequestInfo.builder().build(), program.getLocationCode(), configs.getIdgen().get(ID_NAME), "", 1).get(0));
        else
            program.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = AuditDetails.builder().createdTime(System.currentTimeMillis()).lastModifiedTime(System.currentTimeMillis()).createdBy("a").lastModifiedBy("b").build();
        program.setAuditDetails(auditDetails);
        org.digit.program.models.Status status = org.digit.program.models.Status.builder().statusCode(Status.RECEIVED).build();
        program.setStatus(status);
    }

    public Program enrichProgramForUpdate(Program program) {
        log.info("Enrich Program for Update");
        AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(System.currentTimeMillis()).lastModifiedBy("b").build();
        program.setAuditDetails(auditDetails);
        return program;
    }

    public void enrichProgramForSearch(ProgramSearch programSearch) {
        if (programSearch.getPagination() == null)
            programSearch.setPagination(Pagination.builder().build());

        if (programSearch.getPagination().getLimit() == Double.NaN) {
            programSearch.getPagination().setLimit(configs.getSearchlimit());
        }
        if (programSearch.getPagination().getOffset() == Double.NaN) {
            programSearch.getPagination().setOffset(0);
        }
        if (StringUtils.isEmpty(programSearch.getPagination().getSortBy())) {
            programSearch.getPagination().setSortBy("last_modified_time");
        }
        if (programSearch.getPagination().getSortOrder() == null) {
            programSearch.getPagination().setSortOrder(SortOrder.DESC);
        }


    }

}
