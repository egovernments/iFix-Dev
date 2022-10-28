package org.egov.ifix.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.models.fiscalEvent.FiscalEvent;
import org.egov.ifix.models.fiscalEvent.FiscalEventResponseDTO;
import org.egov.ifix.models.mdms.Tenant;
import org.egov.ifix.models.mgramseva.ChallanResponseDTO;
import org.egov.ifix.models.mgramseva.SearchChallanResponseDTO;
import org.egov.ifix.repository.FiscalEventRepository;
import org.egov.ifix.repository.MdmsRepository;
import org.egov.ifix.repository.MgramsevaChallanRepository;
import org.egov.ifix.repository.MgramsevaVendorRepository;
import org.egov.ifix.service.*;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.*;

@Slf4j
@Service
public class PspclEventServiceImpl implements PspclEventService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private FiscalEventRepository fiscalEventRepository;

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private MgramsevaChallanRepository mgramsevaChallanRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private EventPostingDetailService eventPostingDetailService;

    @Autowired
    private PspclEventPersistenceService pspclEventPersistenceService;

    @Autowired
    private MgramsevaVendorRepository mgramsevaVendorRepository;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private MgramsevaChallanService mgramsevaChallanService;

    /**
     * @param eventType
     */
    @Override
    public void processPspclEventForMgramseva(String eventType) {
        FiscalEventResponseDTO fiscalEventResponseDTO = fiscalEventRepository
                .collectFiscalEvent(applicationConfiguration.getPspclIfixEventReceiverName(), eventType);

        if (fiscalEventResponseDTO == null || fiscalEventResponseDTO.getFiscalEvent() == null
                || fiscalEventResponseDTO.getFiscalEvent().isEmpty()) {
            pspclEventPersistenceService.saveFailedPspclEventDetail(NA, eventType, NA,
                    "No new PSPCL event found from fiscal event service (iFix Core)");
            log.error("No new PSPCL event found from fiscal event service (iFix Core)");
        } else {
            List<FiscalEvent> fiscalEventList = fiscalEventRepository
                    .resolveDuplicateEvent(fiscalEventResponseDTO.getFiscalEvent(), eventType);

            if (fiscalEventList.isEmpty()) {
                pspclEventPersistenceService.saveFailedPspclEventDetail(NA, eventType, NA,
                        "No new event from fiscal event (iFix Core) - Duplicate data issue");
                throw new GenericCustomException(PSPCL, "No new event from fiscal event (iFix Core) - Duplicate data issue");
            } else {
                for (FiscalEvent fiscalEvent : fiscalEventList) {
                    Optional<String> departmentEntityCodeOptional = getDepartmentEntityCode(fiscalEvent);

                    if (!departmentEntityCodeOptional.isPresent()) {
                        pspclEventPersistenceService.saveFailedPspclEventDetail(NA, eventType,
                                NA, "Unable to get department Entity code");
                        log.error("Unable to get department Entity code from fiscal event");
                    } else {
                        List<Tenant> tenantList = mdmsRepository.getMdmsTenantByCityCode(departmentEntityCodeOptional.get());

                        if (tenantList == null || tenantList.isEmpty()) {
                            pspclEventPersistenceService.saveFailedPspclEventDetail(NA, eventType,
                                    new Gson().toJson(fiscalEvent), "Unable to get tenant details from MDMS");
                            log.error("Unable to get tenant details from MDMS for department entity code:: "
                                    + departmentEntityCodeOptional.get());
                        } else {
                            for (Tenant tenant : tenantList) {
                                pushPspclEventToMgramseva(fiscalEvent, tenant, eventType);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param fiscalEvent
     * @param tenant
     * @param eventType
     */
    private void pushPspclEventToMgramseva(FiscalEvent fiscalEvent, Tenant tenant, String eventType) {
        if (fiscalEvent == null || tenant == null || StringUtils.isEmpty(eventType)) {
            pspclEventPersistenceService.saveFailedPspclEventDetail(NA, eventType,
                    new Gson().toJson(fiscalEvent), "Any of fiscal event or tenant or eventType data is/are invalid");
            log.error("Any of fiscal event or tenant or eventType data is/are invalid");
        } else {
            if (FISCAL_EVENT_RECEIPT.equalsIgnoreCase(eventType)) {
                try {
                    collectionService.makePspclPaymentByCollectionService(eventType, fiscalEvent, tenant.getCode(),
                            getPspclAccountNoFromFiscalEvent(fiscalEvent));

                } catch (Exception e) {
                    log.error("Unable to make payment in collection service", e);
                    pspclEventPersistenceService.saveFailedPspclEventDetail(tenant.getCode(), eventType,
                            new Gson().toJson(fiscalEvent), e.getMessage());
                }
            } else if (FISCAL_EVENT_DEMAND.equalsIgnoreCase(eventType)) {
                try {
                    mgramsevaChallanService.createChallan(eventType, fiscalEvent, tenant.getCode(),
                            getPspclAccountNoFromFiscalEvent(fiscalEvent));

                } catch (Exception e) {
                    log.error("Unable to push create challan into challan service", e);

                    pspclEventPersistenceService.saveFailedPspclEventDetail(tenant.getCode(), eventType,
                            new Gson().toJson(fiscalEvent), e.getMessage());
                }
            }
        }
    }

    /**
     * @param fiscalEvent
     * @return
     */
    private Optional<String> getDepartmentEntityCode(FiscalEvent fiscalEvent) {
        String departmentEntityCode = null;

        if (fiscalEvent != null) {
            try {
                ObjectNode objectNodeAttribute = objectMapper
                        .readValue(objectMapper.writeValueAsString(fiscalEvent.getAttributes()), ObjectNode.class);

                if (!objectNodeAttribute.has("departmentEntity")) {
                    pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                            "Department Entity is missing from attributes section of fiscal event");
                    throw new GenericCustomException(PSPCL, "Department Entity is missing from attributes section");
                } else {
                    JsonNode departmentEntityJsonNode = objectNodeAttribute.get("departmentEntity");

                    departmentEntityCode = departmentEntityJsonNode.get("code").asText();
                }
            } catch (JsonMappingException e) {
                pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                        "Json parsing error while to process department Entity code in fiscal event");
                throw new GenericCustomException(PSPCL, "Json parsing error while to process department Entity code in fiscal event");
            } catch (JsonProcessingException e) {
                pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                        "Json parsing error while to process department Entity code in fiscal event");
                throw new GenericCustomException(PSPCL, "Json parsing error while to process department Entity code in fiscal event");
            } catch (Exception e) {
                pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                        "Unable to process department Entity code in fiscal event");
                throw new GenericCustomException(PSPCL, "Unable to process department Entity code in fiscal event");
            }
        }

        return Optional.ofNullable(departmentEntityCode);
    }

    /**
     * @param fiscalEvent
     * @return
     */
    public @NotNull String getPspclAccountNoFromFiscalEvent(@NotNull FiscalEvent fiscalEvent) {
        try {
            ObjectNode objectNodeAttribute = objectMapper
                    .readValue(objectMapper.writeValueAsString(fiscalEvent.getAttributes()), ObjectNode.class);

            if (!objectNodeAttribute.has("pspclAccountNumber") ||
                    StringUtils.isEmpty(objectNodeAttribute.get("pspclAccountNumber").asText())) {

                pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                        "PSPCL account number is missing from attributes section of fiscal event");
                throw new GenericCustomException(PSPCL, "PSPCL account number is missing from attributes section");
            } else {
                return StringUtils.trimAllWhitespace(objectNodeAttribute.get("pspclAccountNumber").asText());
            }
        } catch (JsonProcessingException e) {
            pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                    "Json parsing error while to process PSPCL account number in fiscal event");
            throw new GenericCustomException(PSPCL,
                    "Json parsing error while to process PSPCL account number in fiscal event");
        } catch (Exception e) {
            pspclEventPersistenceService.saveFailedPspclEventDetail(NA, NA, NA,
                    "Unable to process PSPCL account number in fiscal event");
            throw new GenericCustomException(PSPCL, "Unable to process PSPCL account number in fiscal event");
        }
    }
}
