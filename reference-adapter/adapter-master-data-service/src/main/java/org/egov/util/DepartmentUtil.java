package org.egov.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DepartmentUtil {
    @Autowired
    private MasterDataServiceConfiguration configuration;

    @Autowired
    private ServiceRequestRepository searchRequestRepository;

    /**
     * @param tenantId
     * @param idList
     * @param requestHeader
     * @return
     */
    public boolean validateDepartmentEntityIds(String tenantId, List<String> idList, RequestHeader requestHeader) {
        if (StringUtils.isNotBlank(tenantId) && idList != null && !idList.isEmpty()) {
            Map<String, Object> departmentEntityValueMap = new HashMap<>();
            departmentEntityValueMap.put(MasterDataConstants.IDS, idList);
            departmentEntityValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID, tenantId);
            departmentEntityValueMap.put(MasterDataConstants.GET_ANCESTRY, false);

            Map<String, Object> departmentEntityMap = new HashMap<>();
            departmentEntityMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            departmentEntityMap.put(MasterDataConstants.CRITERIA, departmentEntityValueMap);

            Object response = searchRequestRepository.fetchResult(createDepartmentEntitySearchUrl(), departmentEntityMap);

            try {
                List list = JsonPath.read(response, MasterDataConstants.DEPARTMENT_ENTITY_LIST);

                return list != null && list.size() == idList.size();
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse department response");
            }
        }

        return false;
    }

    /**
     * @return
     */
    private String createDepartmentEntitySearchUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(configuration.getDepartmentEntityHost())
                .append(configuration.getDepartmentEntityContextPath())
                .append(configuration.getDepartmentEntitySearchPath());
        return uriBuilder.toString();
    }
}
