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
public class ExpenditureUtil {
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
    public boolean validateExpenditure(String tenantId, List<String> idList, RequestHeader requestHeader) {
        if (StringUtils.isNotBlank(tenantId) && idList != null && !idList.isEmpty()) {
            Map<String, Object> expenditureValueMap = new HashMap<>();
            expenditureValueMap.put(MasterDataConstants.IDS, idList);
            expenditureValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID, tenantId);

            Map<String, Object> expenditureMap = new HashMap<>();
            expenditureMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            expenditureMap.put(MasterDataConstants.CRITERIA, expenditureValueMap);

            Object response = searchRequestRepository.fetchResult(createSearchExpenditureUrl(), expenditureMap);
            try {
                List list = JsonPath.read(response, MasterDataConstants.EXPENDITURE_LIST);

                return list != null && !list.isEmpty();
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse expenditure response");
            }
        }

        return false;
    }

    /**
     * @return
     */
    private String createSearchExpenditureUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(configuration.getIfixMasterExpenditureHost())
                .append(configuration.getIfixMasterExpenditureContextPath())
                .append(configuration.getIfixMasterExpenditureSearchPath());
        return uriBuilder.toString();
    }
}
