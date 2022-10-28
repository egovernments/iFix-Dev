package org.egov.ifix.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.ifix.models.mdms.Tenant;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.mdms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class MdmsRepository {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    /**
     * @param cityCode It is Department Entity Code -in magramseva it refers
     * @return
     */
    public List<Tenant> getMdmsTenantByCityCode(@NotNull String cityCode) {
        List<Tenant> tenantList = null;

        try {
            MasterDetail masterDetail = MasterDetail.builder().name("tenants")
                    .filter("[?(@.city.code=='" + cityCode + "')]").build();

            ModuleDetail moduleDetail = ModuleDetail.builder().moduleName("tenant")
                    .masterDetails(Arrays.asList(masterDetail)).build();

            MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(applicationConfiguration.getTenantId())
                    .moduleDetails(Arrays.asList(moduleDetail)).build();

            MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(RequestInfo.builder().build())
                    .mdmsCriteria(mdmsCriteria).build();

            ResponseEntity<MdmsResponse> response =
                    restTemplate.postForEntity(applicationConfiguration.getMgramsevaHost()
                            + applicationConfiguration.getMdmsSearchEndpoint(), mdmsCriteriaReq, MdmsResponse.class);

            JSONArray tenantJsonArray = response.getBody().getMdmsRes().get("tenant").get("tenants");

            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                    Tenant.class));

            tenantList = reader.readValue(tenantJsonArray.toString());
        } catch (IOException e) {
            log.error("IO Error occurred while fetching tenant id from MDMS", e);
        } catch (Exception e) {
            log.error("Error occurred while fetching tenant id from MDMS", e);
        }

        return tenantList;
    }
}
