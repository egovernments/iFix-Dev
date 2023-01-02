package org.egov.ifix.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.fiscalEvent.FiscalEvent;
import org.egov.ifix.models.fiscalEvent.FiscalEventResponseDTO;
import org.egov.ifix.models.fiscalEvent.FiscalEventSearchRequestDTO;
import org.egov.ifix.models.fiscalEvent.FiscalSearchCriteriaDTO;
import org.egov.ifix.persistance.PspclEventDetailRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.DataWrapper;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.egov.ifix.utils.EventConstants.CREATE_CHALLAN;

@Repository
@Slf4j
public class FiscalEventRepository {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private RequestHeaderUtil requestHeaderUtil;

    @Autowired
    private DataWrapper dataWrapper;

    @Autowired
    private PspclEventDetailRepository pspclEventDetailRepository;


    public FiscalEventResponseDTO collectFiscalEvent(String receivers, String eventType) {
        FiscalEventResponseDTO fiscalEventResponseDTO = null;
        String url = applicationConfiguration.getIfixHost() + applicationConfiguration.getIfixEventSearchEndpoint();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authTokenService.getKeyCloakAuthToken());

        FiscalEventSearchRequestDTO fiscalEventSearchRequestDTO = wrapFiscalEventSearchRequest(receivers, eventType);

        HttpEntity<FiscalEventSearchRequestDTO> entity = new HttpEntity<>(fiscalEventSearchRequestDTO, headers);

        try {
            ResponseEntity<FiscalEventResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                    FiscalEventResponseDTO.class);

            fiscalEventResponseDTO = response.getBody();

            if (fiscalEventResponseDTO == null) {
                throw new HttpCustomException(CREATE_CHALLAN, "Unable get response from fiscal event",
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            throw new HttpCustomException(CREATE_CHALLAN, "Exception while requesting from fiscal event search",
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return fiscalEventResponseDTO;
    }

    /**
     * @param receiver
     * @param eventType
     * @return
     */
    private FiscalEventSearchRequestDTO wrapFiscalEventSearchRequest(String receiver, String eventType) {
        FiscalEventSearchRequestDTO fiscalEventSearchRequestDTO = new FiscalEventSearchRequestDTO();

        Long intervalTimeInMilliSecond = dataWrapper.translateCronExpressionIntoMilliSecond();

        Long toIngestionTime = System.currentTimeMillis();
        Long fromIngestionTime = toIngestionTime - intervalTimeInMilliSecond;

        FiscalSearchCriteriaDTO fiscalSearchCriteriaDTO = new FiscalSearchCriteriaDTO();
        fiscalSearchCriteriaDTO.setTenantId(applicationConfiguration.getTenantId());
        fiscalSearchCriteriaDTO.setEventType(eventType);
        fiscalSearchCriteriaDTO.setReceiver(receiver);
        fiscalSearchCriteriaDTO.setFromIngestionTime(fromIngestionTime);
        fiscalSearchCriteriaDTO.setToIngestionTime(toIngestionTime);
        fiscalEventSearchRequestDTO.setRequestHeader(new RequestHeader());
        fiscalEventSearchRequestDTO.setCriteria(fiscalSearchCriteriaDTO);

        return fiscalEventSearchRequestDTO;
    }

    /**
     * @param fiscalEventList
     * @return
     */
    public List<FiscalEvent> resolveDuplicateEvent(List<FiscalEvent> fiscalEventList, String eventType) {
        List<FiscalEvent> filteredFiscalEvent = new ArrayList<>();

        if (fiscalEventList != null && !fiscalEventList.isEmpty()) {
            Long intervalTimeInMilliSecond = dataWrapper.translateCronExpressionIntoMilliSecond();
            long endTime = System.currentTimeMillis();
            long startTime = endTime - intervalTimeInMilliSecond;

            List<String> pspclEventDetailList = pspclEventDetailRepository
                    .findAllByCreatedDateRangeAndEventType(new Date(startTime), new Date(endTime), true, eventType);

            filteredFiscalEvent = fiscalEventList.stream()
                    .filter(fiscalEvent -> !pspclEventDetailList.contains(fiscalEvent.getId()))
                    .collect(Collectors.toList());
        }
        return filteredFiscalEvent;
    }
}
