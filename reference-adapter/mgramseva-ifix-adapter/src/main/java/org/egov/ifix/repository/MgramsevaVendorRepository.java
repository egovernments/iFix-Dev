package org.egov.ifix.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.mgramseva.*;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.*;

@Repository
@Slf4j
public class MgramsevaVendorRepository {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private RequestHeaderUtil requestHeaderUtil;

    @Autowired
    private AdapterCache<String> vendorCache;


    /**
     * @param tenantId
     * @return
     */
    public Optional<SearchVendorResponseDTO> searchVendor(@NotNull String tenantId, @NotNull String name) {
        SearchVendorResponseDTO searchVendorResponse = null;

        String url = applicationConfiguration.getMgramsevaHost()
                + applicationConfiguration.getMgramsevaVendorSearchEndpoint();

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(MGRAMSEVA_TENANT_ID, tenantId)
                .queryParam("limit", -1)
                .queryParam("name", name)
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SearchVendorRequestDTO searchVendorRequestDTO = wrapSearchVendorRequestDTO();

        HttpEntity<SearchVendorRequestDTO> entity = new HttpEntity<>(searchVendorRequestDTO, headers);

        try {

            ResponseEntity<SearchVendorResponseDTO> response = restTemplate.exchange(urlTemplate, HttpMethod.POST, entity,
                    SearchVendorResponseDTO.class);

            searchVendorResponse = response.getBody();

            if (searchVendorResponse == null) {
                throw new HttpCustomException(CREATE_CHALLAN, "Unable get response from search vendor",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new HttpCustomException(CREATE_CHALLAN, "Exception while sending request to search vendor",
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return Optional.ofNullable(searchVendorResponse);
    }

    /**
     * @return
     */
    private SearchVendorRequestDTO wrapSearchVendorRequestDTO() {
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        requestInfoDTO.setApiId(MGRAMSEVA_CHALLAN_API_ID);
        requestInfoDTO.setVer(MGRAMSEVA_CHALLAN_VERSION);
        requestInfoDTO.setAction(MGRAMSEVA_CHALLAN_CREATE_ACTION);
        requestInfoDTO.setDid(MGRAMSEVA_CHALLAN_DID);
        requestInfoDTO.setMsgId(MGRAMSEVA_CHALLAN_MSG_ID);
        requestInfoDTO.setAuthToken(authTokenService.getMgramsevaAccessToken());

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        return new SearchVendorRequestDTO(requestInfoDTO, userInfoDTO);
    }

    /**
     * @param vendorCreateRequestDTO
     * @return
     */
    public Optional<VendorCreateResponseDTO> createVendor(VendorCreateRequestDTO vendorCreateRequestDTO) {
        VendorCreateResponseDTO vendorCreateResponseDTO = null;

        String url = applicationConfiguration.getMgramsevaHost()
                + applicationConfiguration.getMgramsevaVendorCreateEndpoint();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VendorCreateRequestDTO> entity = new HttpEntity<>(vendorCreateRequestDTO, headers);

        try {
            ResponseEntity<VendorCreateResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                    VendorCreateResponseDTO.class);

            vendorCreateResponseDTO = response.getBody();

            if (vendorCreateResponseDTO == null) {
                throw new HttpCustomException(CREATE_CHALLAN, "Unable get response from create challan",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception while sending request to create challan", e);
            throw new HttpCustomException(CREATE_CHALLAN, e.getMessage(), HttpStatus.BAD_REQUEST);
        }


        return Optional.ofNullable(vendorCreateResponseDTO);
    }
}
