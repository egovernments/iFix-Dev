package org.egov.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MasterDepartmentUtil {

    @Autowired
    private MasterDataServiceConfiguration configuration;

    @Autowired
    private ServiceRequestRepository searchRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @param departmentIds
     * @param tenantId
     * @param requestHeader
     * @return
     */
    public List<Department> fetchDepartment(List<String> departmentIds, String tenantId, RequestHeader requestHeader) {
        if ((departmentIds != null && !departmentIds.isEmpty()) && StringUtils.isNotBlank(tenantId) && requestHeader != null) {
            Map<String, Object> departmentValueMap = new HashMap<>();
            departmentValueMap.put(MasterDataConstants.IDS, departmentIds);
            departmentValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID, tenantId);

            Map<String, Object> departmentMap = new HashMap<>();
            departmentMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            departmentMap.put(MasterDataConstants.CRITERIA, departmentValueMap);

            Object response = searchRequestRepository.fetchResult(createSearchDepartmentUrl(), departmentMap);
            try {
                List<Department> departmentList = JsonPath.read(response, MasterDataConstants.DEPARTMENT_JSON_PATH);

                return objectMapper.convertValue(departmentList, new TypeReference<List<Department>>() {
                });
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse expenditure response");
            }
        }
        return Collections.emptyList();
    }

    private String createSearchDepartmentUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(configuration.getIfixMasterDepartmenteHost())
                .append(configuration.getIfixMasterDepartmentContextPath())
                .append(configuration.getIfixMasterDepartmentSearchPath());
        return uriBuilder.toString();
    }
}
