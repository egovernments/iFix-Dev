package org.egov.service;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.util.*;
import org.egov.web.models.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FiscalEventDereferenceService {

    @Autowired
    private FiscalEventDereferenceEnrichmentService enricher;

    @Autowired
    private CoaUtil coaUtil;

    @Autowired
    private GovernmentUtil governmentUtil;

    @Autowired
    private ProjectUtil projectUtil;

    @Autowired
    private ExpenditureUtil expenditureUtil;

    @Autowired
    private DepartmentUtil departmentUtil;

    public FiscalEventDeReferenced dereference(FiscalEventRequest fiscalEventRequest) {
        FiscalEventDeReferenced fiscalEventDeReferenced = new FiscalEventDeReferenced();
        dereferenceTenantId(fiscalEventRequest, fiscalEventDeReferenced);
        dereferenceCoaId(fiscalEventRequest, fiscalEventDeReferenced);
        dereferenceProjectId(fiscalEventRequest, fiscalEventDeReferenced);
        enricher.enrich(fiscalEventRequest, fiscalEventDeReferenced);
        return fiscalEventDeReferenced;
    }

    private void dereferenceTenantId(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        List<Government> governments = governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest);
        if (!governments.isEmpty()) {
            fiscalEventDeReferenced.setGovernment(governments.get(0));
        }
        fiscalEventDeReferenced.setTenantId(fiscalEventRequest.getFiscalEvent().getTenantId());
    }

    private void dereferenceCoaId(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        //copy the amount details except chart of account in deReferenced amount
        List<AmountDetailsDeReferenced> amtDetailsDereferenced = new ArrayList<>();
        if (fiscalEventRequest.getFiscalEvent() != null
                && fiscalEventRequest.getFiscalEvent().getAmountDetails() != null
                && !fiscalEventRequest.getFiscalEvent().getAmountDetails().isEmpty()) {
            for (Amount amount : fiscalEventRequest.getFiscalEvent().getAmountDetails()) {
                AmountDetailsDeReferenced amountDetailsDeReferenced = new AmountDetailsDeReferenced();
                amountDetailsDeReferenced.setAmount(amount.getAmount());
                amountDetailsDeReferenced.setFromBillingPeriod(amount.getFromBillingPeriod());
                amountDetailsDeReferenced.setToBillingPeriod(amount.getToBillingPeriod());
                amountDetailsDeReferenced.setId(amount.getId());
                ChartOfAccount coa = new ChartOfAccount();
                coa.setId(amount.getCoaId());
                //coaIds.add(amount.getCoaId());
                amountDetailsDeReferenced.setCoa(coa);

                amtDetailsDereferenced.add(amountDetailsDeReferenced);
            }
        }

        //Get the chart of account details
        List<ChartOfAccount> chartOfAccounts = coaUtil.getCOAIdsFromCOAService(fiscalEventRequest.getRequestHeader(),
                fiscalEventRequest.getFiscalEvent());
        //copy the amount details except chart of account in deReferenced amount
        List<AmountDetailsDeReferenced> updatedAmtDereferences = new ArrayList<>();
        if (!chartOfAccounts.isEmpty()) {
            for (AmountDetailsDeReferenced amtDeReferenced : amtDetailsDereferenced) {
                AmountDetailsDeReferenced newAmtDereference = new AmountDetailsDeReferenced();
                BeanUtils.copyProperties(amtDeReferenced, newAmtDereference);

                for (ChartOfAccount chartOfAccount : chartOfAccounts) {
                    if (chartOfAccount.getId().equals(amtDeReferenced.getCoa().getId())) {
                        newAmtDereference.setCoa(chartOfAccount);
                        break;
                    }
                }

                updatedAmtDereferences.add(newAmtDereference);
            }
        }

        fiscalEventDeReferenced.setAmountDetails(updatedAmtDereferences);
    }

    /**
     * @param fiscalEventRequest
     * @param fiscalEventDeReferenced
     */
    private void dereferenceProjectId(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        if (isValidProjectIdParam(fiscalEventRequest) && fiscalEventDeReferenced != null) {

            JsonNode jsonNode = projectUtil.getProjectReference(fiscalEventRequest);
            JsonNode projectListNode = jsonNode.get("project");
            if (projectListNode != null && !projectListNode.isEmpty()) {
                JsonNode projectNode = projectListNode.get(0);

                String expenditureId = null;
                String departmentId = null;
                if (projectNode != null && !projectNode.isEmpty()) {
                    expenditureId = projectNode.get("expenditureId").asText();
                    departmentId = projectNode.get("departmentEntity").get("departmentId").asText();

                    fiscalEventDeReferenced.setProject(getProjectDetails(projectNode));
                    fiscalEventDeReferenced.setDepartmentEntity(projectUtil.getDepartmentEntityFromProject(projectNode.get("departmentEntity")));
                }

                List<Expenditure> expenditureList = null;
                if (StringUtils.isNotBlank(expenditureId)) {
                    expenditureList = expenditureUtil.getExpenditureReference(fiscalEventRequest.getFiscalEvent().getTenantId(),
                            expenditureId, fiscalEventRequest.getRequestHeader());
                }

                if (expenditureList != null && !expenditureList.isEmpty()) {
                    fiscalEventDeReferenced.setExpenditure(expenditureList.get(0));
                }

                List<Department> departmentList = null;
                if (StringUtils.isNotBlank(departmentId)) {
                    departmentList = departmentUtil
                            .getDepartmentReference(fiscalEventRequest.getFiscalEvent().getTenantId(),
                                    departmentId, fiscalEventRequest.getRequestHeader());
                }

                if (departmentList != null && !departmentList.isEmpty()) {
                    fiscalEventDeReferenced.setDepartment(departmentList.get(0));
                }
            }
        }
    }

    private Project getProjectDetails(JsonNode projectNode) {
        return Project.builder().id(projectNode.get("id").asText())
                .code(projectNode.get("code").asText())
                .name(projectNode.get("name").asText()).build();
    }

    /**
     * @param fiscalEventRequest
     * @return
     */
    private boolean isValidProjectIdParam(FiscalEventRequest fiscalEventRequest) {
        if (fiscalEventRequest != null && fiscalEventRequest.getFiscalEvent() != null
                && fiscalEventRequest.getRequestHeader() != null
                && !StringUtils.isBlank(fiscalEventRequest.getFiscalEvent().getProjectId())
                && !StringUtils.isBlank(fiscalEventRequest.getFiscalEvent().getTenantId())
        ) {
            return true;
        }

        return false;
    }
}
