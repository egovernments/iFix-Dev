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
        log.info("Response:"+response);
        HashMap<Integer, String> hierarchyLevelVsLabelMap = new HashMap<>();
        response.getDepartmentHierarchyLevel().forEach(hierarchyObject -> {
            if(!hierarchyLevelVsLabelMap.containsKey(hierarchyObject.getLevel())) {
                log.info("Level : "  +hierarchyObject.getLevel() +" label:" +hierarchyObject.getLabel());
                hierarchyLevelVsLabelMap.put(hierarchyObject.getLevel(), hierarchyObject.getLabel().replaceAll(" ", "_"));
            }
        });
        return hierarchyLevelVsLabelMap;
    }

    public void enrichFiscalData(FiscalEvent fiscalEvent){
        log.info("FISCAL EVENT :"+fiscalEvent);
        HashMap<String, Object> attributes = (HashMap<String, Object>) fiscalEvent.getAttributes();

        HashMap<String, Object> departmentEntity = new HashMap<>();

        if(attributes.containsKey("departmentEntity"))
            departmentEntity = (HashMap<String, Object>) attributes.get("departmentEntity");

        List<HashMap<String, Object>> ancestryList = new ArrayList<>();

        if(departmentEntity.containsKey("ancestry"))
            ancestryList = (List<HashMap<String, Object>>) departmentEntity.get("ancestry");

        HashMap<String, String> hierarchyMap = new HashMap<>();

        if(!tenantIdVshierarchyLevelVsLabelMap.containsKey(fiscalEvent.getTenantId())){
            tenantIdVshierarchyLevelVsLabelMap.put(fiscalEvent.getTenantId(),
                    loadDepartmentHierarchyLevel(fiscalEvent.getTenantId()));
        }
        log.info("ancestryList:" +ancestryList );
        log.info("tenantIdVshierarchyLevelVsLabelMap:" +tenantIdVshierarchyLevelVsLabelMap);
        ancestryList.forEach(ancestry -> {
            if(ancestry.containsKey("hierarchyLevel") && ancestry.containsKey("code")) {
               log.info("Code:"+(String) ancestry.get("code"));
                hierarchyMap.put(tenantIdVshierarchyLevelVsLabelMap.get(fiscalEvent.getTenantId()).get(ancestry.get(
                        "hierarchyLevel")), (String) ancestry.get("code"));
            }
        });
        log.info("hierarchyMap:" +hierarchyMap );
        fiscalEvent.setHierarchyMap(hierarchyMap);

        log.info(fiscalEvent.toString());
    }

    private StringBuilder getIfixDepartmentEntityUri(){
        StringBuilder uri = new StringBuilder(ifixDeptEntityServiceHost);
        uri.append(ifixDeptEntityServiceSearchEndpoint);
        return uri;
    }

    public void enrichComputedFields(FiscalEvent fiscalEvent, Map<String, HashSet<String>> expenditureTypeVsUuidsMap) {
        Map<String, Object> computedFieldsMap = new HashMap<>();
        if(fiscalEvent.getEventType().equals(FiscalEvent.EventTypeEnum.Demand)){
            BigDecimal totalDemandAmount = new BigDecimal(0);
            for(int i = 0; i < fiscalEvent.getAmountDetails().size(); i++){
                totalDemandAmount = totalDemandAmount.add(fiscalEvent.getAmountDetails().get(i).getAmount());
            }
            computedFieldsMap.put("netAmount", totalDemandAmount);
        }else if(fiscalEvent.getEventType().equals(FiscalEvent.EventTypeEnum.Bill)){
            BigDecimal totalBillAmount = new BigDecimal(0);
            for(int i = 0; i < fiscalEvent.getAmountDetails().size(); i++){
                totalBillAmount = totalBillAmount.subtract(fiscalEvent.getAmountDetails().get(i).getAmount());
            }
            computedFieldsMap.put("netAmount", totalBillAmount);
        }else if(fiscalEvent.getEventType().equals(FiscalEvent.EventTypeEnum.Receipt)){
            BigDecimal totalCollectionAmount = new BigDecimal(0);
            for(int i = 0; i < fiscalEvent.getAmountDetails().size(); i++){
                totalCollectionAmount = totalCollectionAmount.subtract(fiscalEvent.getAmountDetails().get(i).getAmount());
            }
            computedFieldsMap.put("netAmount", totalCollectionAmount);
        }

        BigDecimal electricityHeadAmount = new BigDecimal(0);
        BigDecimal operationsHeadAmount = new BigDecimal(0);
        BigDecimal salaryHeadAmount = new BigDecimal(0);
        BigDecimal otherHeadAmount = new BigDecimal(0);

        for(int i = 0; i < fiscalEvent.getAmountDetails().size(); i++) {
            Amount amount = fiscalEvent.getAmountDetails().get(i);
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

        if(fiscalEvent.getEventType().equals(FiscalEvent.EventTypeEnum.Payment)){
            BigDecimal electricityPaymentAmount = new BigDecimal(0);
            for(int i = 0; i < fiscalEvent.getAmountDetails().size(); i++) {
                Amount amount = fiscalEvent.getAmountDetails().get(i);
                if(expenditureTypeVsUuidsMap.get(electricityCoaHeadName).contains(amount.getCoaId()))
                    electricityPaymentAmount = electricityPaymentAmount.subtract(amount.getAmount());
            }
            computedFieldsMap.put("electricityExpenseNetAmount", electricityPaymentAmount);
        }else if(fiscalEvent.getEventType().equals(FiscalEvent.EventTypeEnum.Bill)){
            BigDecimal electricityBillAmount = new BigDecimal(0);
            for(int i = 0; i < fiscalEvent.getAmountDetails().size(); i++) {
                Amount amount = fiscalEvent.getAmountDetails().get(i);
                if(expenditureTypeVsUuidsMap.get(electricityCoaHeadName).contains(amount.getCoaId()))
                    electricityBillAmount = electricityBillAmount.add(amount.getAmount());
            }
            computedFieldsMap.put("electricityExpenseNetAmount", electricityBillAmount);
        }

        fiscalEvent.setComputedFields(computedFieldsMap);
    }
}
