package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.resposioty.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Department;
import org.egov.web.models.EAT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DepartmentUtil {

    @Autowired
    FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    ObjectMapper objectMapper;

    public List<Department> getDepartmentReference(String tenantId, String departmentId, RequestHeader requestHeader) {
        if (!StringUtils.isEmpty(tenantId) && !StringUtils.isEmpty(departmentId)) {
            Map<String, Object> departmentValueMap = new HashMap<>();
            departmentValueMap.put(MasterDataConstants.IDS, Collections.singletonList(departmentId));
            departmentValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID, tenantId);

            Map<String, Object> departmentMap = new HashMap<>();
            departmentMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            departmentMap.put(MasterDataConstants.CRITERIA, departmentValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchDepartmentUrl(), departmentMap);

            try{
                List<Department>  departmentList = JsonPath.read(response, MasterDataConstants.DEPARTMENT_JSON_PATH);

                return objectMapper.convertValue(departmentList, new TypeReference<List<Department>>() {});
            }catch (Exception e){
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR,"Failed to parse project response for projectId");
            }
        }
        return Collections.emptyList();
    }

    private String createSearchDepartmentUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processorConfig.getIfixMasterDepartmentHost())
                .append(processorConfig.getIfixMasterDepartmentContextPath())
                .append(processorConfig.getIfixMasterDepartmentSearchPath());
        return uriBuilder.toString();
    }
}
