package org.egov.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.RequestInfo;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.mdms.model.*;
import org.egov.mdms.service.MdmsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CoaUtil {

    @Autowired
    private MasterDataServiceConfiguration mdsConfiguration;
    @Autowired
    private MdmsClientService mdmsClientService;
    @Autowired
    private ObjectMapper objectMapper;

    final private static String TENANT_MODULE_NAME = "tenant";
    final private static String TENANT_MASTER_NAME = "tenants";

    public boolean validateTenant(String tenantId, RequestHeader requestHeader) {
        if (tenantId != null && !tenantId.isEmpty() && requestHeader != null) {

            MasterDetail masterDetail = MasterDetail.builder().name(TENANT_MASTER_NAME).filter("$.*.code").build();
            ModuleDetail moduleDetail =
                    ModuleDetail.builder().moduleName(TENANT_MODULE_NAME).masterDetails(Collections.singletonList(masterDetail)).build();
            MdmsCriteria mdmsCriteria =
                    MdmsCriteria.builder().moduleDetails(Collections.singletonList(moduleDetail)).tenantId(mdsConfiguration.getRootLevelTenantId()).build();
            MdmsCriteriaReq mdmsCriteriaReq =
                    MdmsCriteriaReq.builder().requestInfo(RequestInfo.builder().build()).mdmsCriteria(mdmsCriteria).build();

            MdmsResponse mdmsResponse = mdmsClientService.getMaster(mdmsCriteriaReq);
            JSONArray tenantCodes = mdmsResponse.getMdmsRes().get(TENANT_MODULE_NAME).get(TENANT_MASTER_NAME);

            List<String> validTenantIds = objectMapper.convertValue(tenantCodes, new TypeReference<List<String>>() {});

            return validTenantIds.contains(tenantId);
        }
        return false;
    }

}
