package org.egov.ifixespipeline.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifixespipeline.models.*;
import org.egov.ifixespipeline.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class FiscalDataEnrichmentService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${ifix.department.entity.service.host}")
    private String ifixDeptEntityServiceHost;

    @Value("${ifix.department.entity.search.endpoint}")
    private String ifixDeptEntityServiceSearchEndpoint;

    @Value("${coa.electricity.head.name}")
    private String electricityCoaHeadName;

    @Value("${coa.operations.head.name}")
    private String operationsCoaHeadName;

    @Value("${coa.salary.head.name}")
    private String salaryCoaHeadName;

    Map<String, HashMap<Integer, String>> tenantIdVshierarchyLevelVsLabelMap = new HashMap<>();

    private HashMap<Integer, String> loadDepartmentHierarchyLevel(String tenantId){
        DepartmentHierarchyLevelSearchCriteria criteria = DepartmentHierarchyLevelSearchCriteria.builder().tenantId(tenantId).build();
        DepartmentHierarchyLevelSearchRequest request = DepartmentHierarchyLevelSearchRequest.builder().criteria(criteria).requestHeader(new RequestHeader()).build();
        Object result = serviceRequestRepository.fetchResult(getIfixDepartmentEntityUri(), request);
        DepartmentHierarchyLevelResponse response = objectMapper.convertValue(result, DepartmentHierarchyLevelResponse.class);
        HashMap<Integer, String> hierarchyLevelVsLabelMap = new HashMap<>();
        response.getDepartmentHierarchyLevel().forEach(hierarchyObject -> {
            if(!hierarchyLevelVsLabelMap.containsKey(hierarchyObject.getLevel()))
                hierarchyLevelVsLabelMap.put(hierarchyObject.getLevel(), hierarchyObject.getLabel().replaceAll(" ", "_"));
        });
        return hierarchyLevelVsLabelMap;
    }

    public void enrichFiscalData(FiscalEventRequest incomingData){
        HashMap<String, Object> attributes = (HashMap<String, Object>) incomingData.getFiscalEvent().getAttributes();

        HashMap<String, Object> departmentEntity = new HashMap<>();

        if(attributes.containsKey("departmentEntity"))
            departmentEntity = (HashMap<String, Object>) attributes.get("departmentEntity");

        List<HashMap<String, Object>> ancestryList = new ArrayList<>();

        if(departmentEntity.containsKey("ancestry"))
            ancestryList = (List<HashMap<String, Object>>) departmentEntity.get("ancestry");

        HashMap<String, String> hierarchyMap = new HashMap<>();

        if(!tenantIdVshierarchyLevelVsLabelMap.containsKey(incomingData.getFiscalEvent().getTenantId())){
            tenantIdVshierarchyLevelVsLabelMap.put(incomingData.getFiscalEvent().getTenantId(), loadDepartmentHierarchyLevel(incomingData.getFiscalEvent().getTenantId()));
        }

        ancestryList.forEach(ancestry -> {
            if(ancestry.containsKey("hierarchyLevel") && ancestry.containsKey("code"))
                hierarchyMap.put(tenantIdVshierarchyLevelVsLabelMap.get(incomingData.getFiscalEvent().getTenantId()).get(ancestry.get("hierarchyLevel")), (String)ancestry.get("code"));
        });

        incomingData.getFiscalEvent().setHierarchyMap(hierarchyMap);

        log.info(incomingData.getFiscalEvent().toString());
    }

    private StringBuilder getIfixDepartmentEntityUri(){
        StringBuilder uri = new StringBuilder(ifixDeptEntityServiceHost);
        uri.append(ifixDeptEntityServiceSearchEndpoint);
        return uri;
    }

    public void enrichComputedFields(FiscalEventRequest incomingData, Map<String, HashSet<String>> expenditureTypeVsUuidsMap) {
        Map<String, Object> computedFieldsMap = new HashMap<>();
        if(incomingData.getFiscalEvent().getEventType().equals(FiscalEvent.EventTypeEnum.Demand)){
            BigDecimal totalDemandAmount = new BigDecimal(0);
            for(int i = 0; i < incomingData.getFiscalEvent().getAmountDetails().size(); i++){
                totalDemandAmount = totalDemandAmount.add(incomingData.getFiscalEvent().getAmountDetails().get(i).getAmount());
            }
            computedFieldsMap.put("netAmount", totalDemandAmount);
        }else if(incomingData.getFiscalEvent().getEventType().equals(FiscalEvent.EventTypeEnum.Bill)){
            BigDecimal totalBillAmount = new BigDecimal(0);
            for(int i = 0; i < incomingData.getFiscalEvent().getAmountDetails().size(); i++){
                totalBillAmount = totalBillAmount.subtract(incomingData.getFiscalEvent().getAmountDetails().get(i).getAmount());
            }
            computedFieldsMap.put("netAmount", totalBillAmount);
        }else if(incomingData.getFiscalEvent().getEventType().equals(FiscalEvent.EventTypeEnum.Receipt)){
            BigDecimal totalCollectionAmount = new BigDecimal(0);
            for(int i = 0; i < incomingData.getFiscalEvent().getAmountDetails().size(); i++){
                totalCollectionAmount = totalCollectionAmount.subtract(incomingData.getFiscalEvent().getAmountDetails().get(i).getAmount());
            }
            computedFieldsMap.put("netAmount", totalCollectionAmount);
        }

        BigDecimal electricityHeadAmount = new BigDecimal(0);
        BigDecimal operationsHeadAmount = new BigDecimal(0);
        BigDecimal salaryHeadAmount = new BigDecimal(0);
        BigDecimal otherHeadAmount = new BigDecimal(0);

        for(int i = 0; i < incomingData.getFiscalEvent().getAmountDetails().size(); i++) {
            Amount amount = incomingData.getFiscalEvent().getAmountDetails().get(i);
            if(expenditureTypeVsUuidsMap.get(electricityCoaHeadName).contains(amount.getCoaId()))
                electricityHeadAmount = electricityHeadAmount.add(amount.getAmount());
            else if(expenditureTypeVsUuidsMap.get(operationsCoaHeadName).contains(amount.getCoaId()))
                operationsHeadAmount = operationsHeadAmount.add(amount.getAmount());
            else if(expenditureTypeVsUuidsMap.get(salaryCoaHeadName).contains(amount.getCoaId()))
                salaryHeadAmount = salaryHeadAmount.add(amount.getAmount());
            else
                otherHeadAmount = otherHeadAmount.add(amount.getAmount());
        }

        computedFieldsMap.put("electricityHeadAmount", electricityHeadAmount);
        computedFieldsMap.put("operationsHeadAmount", operationsHeadAmount);
        computedFieldsMap.put("salaryHeadAmount", salaryHeadAmount);
        computedFieldsMap.put("otherHeadAmount", otherHeadAmount);

        if(incomingData.getFiscalEvent().getEventType().equals(FiscalEvent.EventTypeEnum.Payment)){
            BigDecimal electricityPaymentAmount = new BigDecimal(0);
            for(int i = 0; i < incomingData.getFiscalEvent().getAmountDetails().size(); i++) {
                Amount amount = incomingData.getFiscalEvent().getAmountDetails().get(i);
                if(expenditureTypeVsUuidsMap.get(electricityCoaHeadName).contains(amount.getCoaId()))
                    electricityPaymentAmount = electricityPaymentAmount.subtract(amount.getAmount());
            }
            computedFieldsMap.put("electricityExpenseNetAmount", electricityPaymentAmount);
        }else if(incomingData.getFiscalEvent().getEventType().equals(FiscalEvent.EventTypeEnum.Bill)){
            BigDecimal electricityBillAmount = new BigDecimal(0);
            for(int i = 0; i < incomingData.getFiscalEvent().getAmountDetails().size(); i++) {
                Amount amount = incomingData.getFiscalEvent().getAmountDetails().get(i);
                if(expenditureTypeVsUuidsMap.get(electricityCoaHeadName).contains(amount.getCoaId()))
                    electricityBillAmount = electricityBillAmount.add(amount.getAmount());
            }
            computedFieldsMap.put("electricityExpenseNetAmount", electricityBillAmount);
        }

        incomingData.getFiscalEvent().setComputedFields(computedFieldsMap);
    }
}
