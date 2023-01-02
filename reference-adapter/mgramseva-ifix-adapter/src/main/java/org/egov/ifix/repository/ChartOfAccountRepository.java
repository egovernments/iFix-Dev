package org.egov.ifix.repository;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.COASearchCriteria;
import org.egov.ifix.models.ChartOfAccount;
import org.egov.ifix.models.ChartOfAccountRequest;
import org.egov.ifix.models.ChartOfAccountResponse;
import org.egov.ifix.persistance.ChartOfAccountMap;
import org.egov.ifix.persistance.ChartOfAccountMapRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.DataWrapper;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.CLIENT_COA_CODE;
import static org.egov.ifix.utils.EventConstants.COA_CODE;

/**
 * @author mani
 * This will be resposible for providing right coa from web or db or file repository
 * As of now client (mgram) coaCode is matching with so direcly making the call .
 * When it is changed to taxheadcode it should fetch the code from database or file
 */
@Repository
@Slf4j
public class ChartOfAccountRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private RequestHeaderUtil requestHeaderUtil;

    @Autowired
    private ChartOfAccountMapRepository chartOfAccountMapRepository;

    @Autowired
    private DataWrapper dataWrapper;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * @param clientCode
     * @param jsonObject
     * @return
     */
    public Optional<ChartOfAccount> getChartOfAccount(String clientCode, JsonObject jsonObject) {
        COASearchCriteria coaCriteria = new COASearchCriteria();
        ChartOfAccountMap coaMapping = chartOfAccountMapRepository.findByClientCode(clientCode);

        if (coaMapping != null) {
            if (coaMapping.getIFixCoaCode() != null) {
                coaCriteria.setCoaCode(coaMapping.getIFixCoaCode());
            } else {
                List<String> ids = Collections.singletonList(coaMapping.getIFixId());
                coaCriteria.setIds(ids);
            }
        } else {
            wrapErrorModelAndPushKafkaTopic(CLIENT_COA_CODE, clientCode,
                    "No COA code found in Mapping table");
            throw new HttpCustomException(COA_CODE, "No COA code found in Mapping table", HttpStatus.BAD_REQUEST);
        }

        return getCoaFromIFixMasterService(coaCriteria, jsonObject);
    }

    /**
     * @param coaCriteria
     * @param jsonObject
     * @return
     */
    private Optional<ChartOfAccount> getCoaFromIFixMasterService(COASearchCriteria coaCriteria, JsonObject jsonObject) {
        if (coaCriteria != null) {
            String url = applicationConfiguration.getIfixHost() + applicationConfiguration.getCoaSearchEndpoint();
            coaCriteria.setTenantId(applicationConfiguration.getTenantId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authTokenService.getKeyCloakAuthToken());

            RequestHeader requestHeader = new RequestHeader();
            requestHeader = requestHeaderUtil.populateRequestHeader(jsonObject, requestHeader);

            ChartOfAccountRequest chartOfAccountRequest = new ChartOfAccountRequest();
            chartOfAccountRequest.setRequestHeader(requestHeader);
            chartOfAccountRequest.setCOASearchCriteria(coaCriteria);

            try {
                ResponseEntity<ChartOfAccountResponse> response = restTemplate
                        .postForEntity(url, chartOfAccountRequest, ChartOfAccountResponse.class);

                List<ChartOfAccount> coaList = response.getBody().getChartOfAccounts();

                if (coaList != null && coaList.size() == 1) {
                    return Optional.ofNullable(coaList.get(0));
                } else if (coaList == null || coaList.isEmpty()) {
                    wrapErrorModelAndPushKafkaTopic(COA_CODE, coaCriteria.getCoaCode(),
                            "No COA code found in Mapping table");
                    throw new HttpCustomException(COA_CODE, "No COA code found in Mapping table", HttpStatus.BAD_REQUEST);
                } else if (coaList.size() > 1) {
                    wrapErrorModelAndPushKafkaTopic(COA_CODE, coaCriteria.getCoaCode(),
                            "Duplicate COA code found in Mapping table");
                    throw new HttpCustomException(COA_CODE, "Duplicate COA code in Mapping table", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                wrapErrorModelAndPushKafkaTopic(COA_CODE, coaCriteria.getCoaCode(),
                        "Exception while sending request to iFix for coa search");
                throw new HttpCustomException(COA_CODE, "Exception while sending request to iFix for coa search",
                        HttpStatus.BAD_REQUEST);
            }
        }
        return Optional.empty();
    }

    /**
     * @param attributeName
     * @param attributeValue
     * @param errorMessage
     */
    private void wrapErrorModelAndPushKafkaTopic(String attributeName, String attributeValue, String errorMessage) {
//TODO: Error handling stream.
/*
        Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(NA,
                COA_CODE_DATA_NAME, attributeName, attributeValue, NON_RECOVERABLE_ERROR,
                HttpStatus.BAD_REQUEST.toString(), errorMessage);

        if (errorDataModelOptional.isPresent()) {
            kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), errorDataModelOptional.get());
        }
*/
    }


}
