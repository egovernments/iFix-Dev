package org.egov.ifix.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.mgramseva.FetchBillRequestDTO;
import org.egov.ifix.models.mgramseva.FetchBillResponseDTO;
import org.egov.ifix.models.mgramseva.RequestInfoDTO;
import org.egov.ifix.models.mgramseva.UserInfoDTO;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.*;

@Repository
@Slf4j
public class BillingServiceRepository {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    private AuthTokenService authTokenService;

    public Optional<FetchBillResponseDTO> fetchBillFromBillingService(String tenantId, String consumerCode) {
        FetchBillResponseDTO searchVendorResponse = null;

        String url = applicationConfiguration.getMgramsevaHost()
                + applicationConfiguration.getMgramsevaBillingServiceFetchBillEndpoint();

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(MGRAMSEVA_TENANT_ID, tenantId)
                .queryParam(BILLING_SERVICE_CONSUMER_CODE, consumerCode)
                .queryParam(BILLING_SERVICE_BUSINESS_SERVICE, EXPENSE_ELECTRICITY_BILL)
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        FetchBillRequestDTO fetchBillRequestDTO = wrapFetchBillRequestDTO();
        HttpEntity<FetchBillRequestDTO> entity = new HttpEntity<>(fetchBillRequestDTO, headers);

        try {
            ResponseEntity<FetchBillResponseDTO> response = restTemplate.exchange(urlTemplate, HttpMethod.POST, entity,
                    FetchBillResponseDTO.class);

            searchVendorResponse = response.getBody();

            if (searchVendorResponse == null) {
                throw new HttpCustomException(CREATE_CHALLAN, "Unable get response from fetch bill",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new HttpCustomException(CREATE_CHALLAN, "Exception while sending request to fetch bill",
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return Optional.ofNullable(searchVendorResponse);
    }

    /**
     * @return
     */
    private FetchBillRequestDTO wrapFetchBillRequestDTO() {
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        requestInfoDTO.setApiId(MGRAMSEVA_CHALLAN_API_ID);
        requestInfoDTO.setVer(MGRAMSEVA_CHALLAN_VERSION);
        requestInfoDTO.setAction(MGRAMSEVA_CHALLAN_CREATE_ACTION);
        requestInfoDTO.setDid(MGRAMSEVA_CHALLAN_DID);
        requestInfoDTO.setMsgId(MGRAMSEVA_CHALLAN_MSG_ID);
        requestInfoDTO.setAuthToken(authTokenService.getMgramsevaAccessToken());

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        return new FetchBillRequestDTO(requestInfoDTO, userInfoDTO);
    }
}
