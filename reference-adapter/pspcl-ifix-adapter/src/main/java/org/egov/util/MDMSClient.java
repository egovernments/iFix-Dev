package org.egov.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.mdms.model.*;
import org.egov.model.AccountNumberGpMappingVO;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class MDMSClient {

    @Autowired
    private PspclIfixAdapterConfiguration adapterConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private List<AccountNumberGpMappingVO> acnGpMappingVOs = null;

    @PostConstruct
    protected void loadAccToGPMappingFromMdms() {
        try {
            MasterDetail masterDetail = MasterDetail.builder().name(PspclIfixAdapterConstant.MDMS_ACCOUNT_GP_MAPPING_MASTER_NAME).build();
            ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PspclIfixAdapterConstant.MDMS_MODULE_NAME)
                    .masterDetails(Arrays.asList(masterDetail)).build();

            MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(adapterConfiguration.getTenantId())
                    .moduleDetails(Arrays.asList(moduleDetail)).build();

            MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(RequestInfo.builder().build())
                    .mdmsCriteria(mdmsCriteria).build();

            //header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //request
            HttpEntity<MdmsCriteriaReq> request = new HttpEntity<>(mdmsCriteriaReq, headers);

            ResponseEntity<MdmsResponse> response =
                    restTemplate.postForEntity(adapterConfiguration.getEgovMdmsHost() + adapterConfiguration.getEgovMdmsSearchUrl(),
                            request, MdmsResponse.class);

            MdmsResponse mdmsResponse = null;
            if (response != null && response.getBody() != null) {
                mdmsResponse = response.getBody();
            }else {
                return;
            }

            if (mdmsResponse != null && (mdmsResponse.getMdmsRes() == null || mdmsResponse.getMdmsRes().isEmpty())) {
                log.error("account number to GP mapping config file is missing in MDMS!");
            } else {
                if (mdmsResponse.getMdmsRes().get(PspclIfixAdapterConstant.MDMS_MODULE_NAME) != null && mdmsResponse.getMdmsRes().get(PspclIfixAdapterConstant.MDMS_MODULE_NAME)
                        .get(PspclIfixAdapterConstant.MDMS_ACCOUNT_GP_MAPPING_MASTER_NAME) != null) {
                    JSONArray acnGpMapping = mdmsResponse.getMdmsRes().get(PspclIfixAdapterConstant.MDMS_MODULE_NAME)
                            .get(PspclIfixAdapterConstant.MDMS_ACCOUNT_GP_MAPPING_MASTER_NAME);

                    ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                            AccountNumberGpMappingVO.class));

                    acnGpMappingVOs = reader.readValue(acnGpMapping.toString());
                }

            }

        } catch (IOException e) {
            log.error("Error occurred while getting the account to gp mapping from MDMS", e);
            throw new ServiceCallException("Error occurred while getting the account to gp mapping from MDMS");
        }
    }

    public List<AccountNumberGpMappingVO> getAcnGpMappingVOs() {
        if (acnGpMappingVOs == null || acnGpMappingVOs.isEmpty()) {
            return Collections.emptyList();
        }
        return acnGpMappingVOs;
    }
}
