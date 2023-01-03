package org.egov.service;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.models.*;
import org.egov.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FiscalEventDereferenceService {

    public static final String DEPARTMENT_ENTITY = "departmentEntity";
    @Autowired
    private FiscalEventDereferenceEnrichmentService enricher;

    @Autowired
    private CoaUtil coaUtil;

    @Autowired
    private GovernmentUtil governmentUtil;

    public FiscalEventDeReferenced dereference(FiscalEventRequest fiscalEventRequest) {
        FiscalEventDeReferenced fiscalEventDeReferenced = new FiscalEventDeReferenced();
        dereferenceTenantId(fiscalEventRequest, fiscalEventDeReferenced);
        dereferenceCoaId(fiscalEventRequest, fiscalEventDeReferenced);
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

}
