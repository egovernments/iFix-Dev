package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.producer.Producer;
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
    private Producer producer;

    @Autowired
    private FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    private FiscalEventDereferenceEnrichmentService enricher;

    @Autowired
    private CoaUtil coaUtil;

    @Autowired
    private GovernmentUtil governmentUtil;

    @Autowired
    private ProjectUtil projectUtil;

    @Autowired
    private EatUtil eatUtil;

    @Autowired
    private DepartmentUtil departmentUtil;

    public void dereference(FiscalEventRequest fiscalEventRequest) {
        FiscalEventDeReferenced fiscalEventDeReferenced =  new FiscalEventDeReferenced();
        dereferenceTenantId(fiscalEventRequest,fiscalEventDeReferenced);
        dereferenceCoaId(fiscalEventRequest,fiscalEventDeReferenced);
        dereferenceProjectId(fiscalEventRequest, fiscalEventDeReferenced);
        enricher.enrich(fiscalEventRequest,fiscalEventDeReferenced);
        producer.push(processorConfig.getEventProcessorMongoDB(),fiscalEventDeReferenced);
    }

    private void dereferenceTenantId(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        List<Government> governments = governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest);
        if(!governments.isEmpty()){
            fiscalEventDeReferenced.setGovernment(governments.get(0));
        }
        fiscalEventDeReferenced.setTenantId(fiscalEventRequest.getFiscalEvent().getTenantId());
    }

    private void dereferenceCoaId(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        //copy the amount details except chart of account in deReferenced amount
        List<AmountDetailsDeReferenced> amtDetailsDereferenced = new ArrayList<>();
        if(fiscalEventRequest.getFiscalEvent() != null
                && fiscalEventRequest.getFiscalEvent().getAmountDetails()!=null
                && !fiscalEventRequest.getFiscalEvent().getAmountDetails().isEmpty()){
            for(Amount amount : fiscalEventRequest.getFiscalEvent().getAmountDetails()){
                AmountDetailsDeReferenced amountDetailsDeReferenced =  new AmountDetailsDeReferenced();
                amountDetailsDeReferenced.setAmount(amount.getAmount());
                amountDetailsDeReferenced.setFromBillingPeriod(amount.getFromBillingPeriod());
                amountDetailsDeReferenced.setToBillingPeriod(amount.getToBillingPeriod());
                amountDetailsDeReferenced.setId(amount.getId());
                ChartOfAccount coa =  new ChartOfAccount();
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
        if(!chartOfAccounts.isEmpty()){
            for(AmountDetailsDeReferenced amtDeReferenced : amtDetailsDereferenced){
                AmountDetailsDeReferenced newAmtDereference = new AmountDetailsDeReferenced();
                BeanUtils.copyProperties(amtDeReferenced,newAmtDereference);

                for(ChartOfAccount chartOfAccount : chartOfAccounts){
                    if(chartOfAccount.getId().equals(amtDeReferenced.getCoa().getId())){
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
            List<Project> projectList = projectUtil.getProjectReference(fiscalEventRequest);

            if (projectList != null && !projectList.isEmpty()) {
                Project project = projectList.get(0);
                fiscalEventDeReferenced.setProject(project);

                List<EAT> eatList = eatUtil.getEatReference(fiscalEventRequest.getFiscalEvent().getTenantId(),
                        project.getEatId(), fiscalEventRequest.getRequestHeader());
                if (eatList != null && !eatList.isEmpty()) {
                    fiscalEventDeReferenced.setEat(eatList.get(0));
                }

                List<Department> departmentList = departmentUtil
                        .getDepartmentReference(fiscalEventRequest.getFiscalEvent().getTenantId(),
                                project.getDepartmentId(), fiscalEventRequest.getRequestHeader());
                if (departmentList != null && !departmentList.isEmpty()) {
                    fiscalEventDeReferenced.setDepartment(departmentList.get(0));
                }
            }
        }
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
