package org.egov.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
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
    private IfixDepartmentEntityConfig entityConfig;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;


    /**
     *
     * @param tenantId
     * @param departmentId
     * @param requestHeader
     * @return
     */
    public List<String> getDepartmentFromDepartmentService(String tenantId,String departmentId, RequestHeader requestHeader) {
        if (StringUtils.isNotBlank(tenantId) && StringUtils.isNotBlank(departmentId)) {

            Map<String, Object> departmentValueMap = new HashMap<>();
            departmentValueMap.put(DepartmentEntityConstant.IDS, Collections.singletonList(departmentId.trim()));
            departmentValueMap.put(DepartmentEntityConstant.CRITERIA_TENANT_ID, tenantId.trim());

            Map<String, Object> departmentMap = new HashMap<>();
            departmentMap.put(DepartmentEntityConstant.REQUEST_HEADER, requestHeader);
            departmentMap.put(DepartmentEntityConstant.CRITERIA, departmentValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchDepartmentUrl(), departmentMap);

            try {
                List<String> departmentList = JsonPath.read(response, DepartmentEntityConstant.DEPARTMENT_JSON_PATH);
                return departmentList;
            } catch (Exception e) {
                throw new CustomException(DepartmentEntityConstant.JSONPATH_ERROR, "Failed to parse department response for department Id");
            }
        }
        return Collections.emptyList();
    }

    private String createSearchDepartmentUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(entityConfig.getIfixMasterDepartmentHost())
                .append(entityConfig.getIfixMasterDepartmentContextPath())
                .append(entityConfig.getIfixMasterDepartmentSearchPath());
        return uriBuilder.toString();
    }
}
