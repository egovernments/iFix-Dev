package org.egov.util;

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
public class GovernmentUtil {

    @Autowired
    private IfixDepartmentEntityConfig configuration;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    /**
     *
     * @param tenantId
     * @param requestHeader
     * @return
     */
    public List<String> getGovernmentFromGovernmentService(String tenantId, RequestHeader requestHeader) {
        if (StringUtils.isNotBlank(tenantId)) {
            Map<String, Object> tenantValueMap = new HashMap<>();
            tenantValueMap.put(DepartmentEntityConstant.IDS,
                    Collections.singletonList(tenantId.trim()));

            Map<String, Object> tenantMap = new HashMap<>();
            tenantMap.put(DepartmentEntityConstant.REQUEST_HEADER, requestHeader);
            tenantMap.put(DepartmentEntityConstant.CRITERIA, tenantValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchGovernmentUrl(), tenantMap);
            try {
                List<String> governments = JsonPath.read(response, DepartmentEntityConstant.GOVERNMENT_LIST);
                return governments;
            } catch (Exception e) {
                throw new CustomException(DepartmentEntityConstant.JSONPATH_ERROR, "Failed to parse government response for tenantId");
            }
        }
        return Collections.emptyList();
    }


    /**
     * @return
     */
    private String createSearchGovernmentUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(configuration.getIfixMasterGovernmentHost())
                .append(configuration.getIfixMasterGovernmentContextPath())
                .append(configuration.getIfixMasterGovernmentSearchPath());
        return uriBuilder.toString();
    }

}