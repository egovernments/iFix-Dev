package org.egov.ifix.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.mgramseva.CreatePaymentRequestDTO;
import org.egov.ifix.models.mgramseva.CreatePaymentResponseDTO;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

import static org.egov.ifix.utils.EventConstants.CREATE_CHALLAN;

@Repository
@Slf4j
public class CollectionServiceRepository {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    private AuthTokenService authTokenService;

    /**
     * @param createPaymentRequestDTO
     * @return
     */
    public @NotNull CreatePaymentResponseDTO createPaymentInCollectionService(CreatePaymentRequestDTO createPaymentRequestDTO) {
        CreatePaymentResponseDTO createPaymentResponseDTO = null;

        String url = applicationConfiguration.getMgramsevaHost()
                + applicationConfiguration.getMgramsevaCollectionServicePaymentsCreateEndpoint();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreatePaymentRequestDTO> entity = new HttpEntity<>(createPaymentRequestDTO, headers);

        try {
            ResponseEntity<CreatePaymentResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                    CreatePaymentResponseDTO.class);

            createPaymentResponseDTO = response.getBody();

            if (createPaymentResponseDTO == null) {
                throw new HttpCustomException(CREATE_CHALLAN, "Unable get response from create challan",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception while sending request to create challan", e);
            throw new HttpCustomException(CREATE_CHALLAN, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return createPaymentResponseDTO;
    }
}
