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
import org.egov.web.models.EAT;
import org.egov.web.models.FiscalEventRequest;
import org.egov.web.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EatUtil {
    @Autowired
    FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @param tenantId
     * @param eatId
     * @param requestHeader
     * @return
     */
    public List<EAT> getEatReference(String tenantId, String eatId, RequestHeader requestHeader) {
        if (!StringUtils.isEmpty(tenantId) && !StringUtils.isEmpty(eatId)) {
            Map<String, Object> eatValueMap = new HashMap<>();
            eatValueMap.put(MasterDataConstants.IDS, Collections.singletonList(eatId));
            eatValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID, tenantId);

            Map<String, Object> eatMap = new HashMap<>();
            eatMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            eatMap.put(MasterDataConstants.CRITERIA, eatValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchEatUrl(), eatMap);

            try{
                List<EAT>  eatList = JsonPath.read(response, MasterDataConstants.EAT_JSON_PATH);

                return objectMapper.convertValue(eatList, new TypeReference<List<EAT>>() {});
            }catch (Exception e){
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR,"Failed to parse project response for projectId");
            }
        }
        return Collections.emptyList();
    }

    private String createSearchEatUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processorConfig.getIfixMasterEatHost())
                .append(processorConfig.getIfixMasterEatContextPath())
                .append(processorConfig.getIfixMasterEatSearchPath());
        return uriBuilder.toString();
    }
}
