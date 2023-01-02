package org.egov.ifix.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.models.fiscalEvent.FiscalEvent;
import org.egov.ifix.models.fiscalEvent.FiscalEventAmountDTO;
import org.egov.ifix.models.mgramseva.AmountDTO;
import org.egov.ifix.models.mgramseva.ChallanRequestDTO;
import org.egov.ifix.models.mgramseva.CreateChallanRequestDTO;
import org.egov.ifix.models.mgramseva.RequestInfoDTO;
import org.egov.ifix.repository.MgramsevaChallanRepository;
import org.egov.ifix.repository.MgramsevaVendorRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.service.MgramsevaChallanService;
import org.egov.ifix.service.MgramsevaVendorService;
import org.egov.ifix.service.PspclEventPersistenceService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Collections;

import static org.egov.ifix.utils.EventConstants.*;

@Slf4j
@Service
public class MgramsevaChallanServiceImpl implements MgramsevaChallanService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MgramsevaChallanRepository mgramsevaChallanRepository;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private MgramsevaVendorRepository mgramsevaVendorRepository;

    @Autowired
    private MgramsevaVendorService mgramsevaVendorService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private PspclEventPersistenceService pspclEventPersistenceService;

    /**
     * TODO: As of now, vendor id is mandatory to create challan.
     *      It gets vendor list by tenant id and pick first vendor from vendor list.
     *      In near future, challan may be created without vendor info.
     *
     * @param eventType
     * @param fiscalEvent
     * @param mgramsevaTenantId
     * @param pspclAccountNumber
     */
    @Override
    public void createChallan(String eventType, FiscalEvent fiscalEvent, String mgramsevaTenantId,
                              @NotNull String pspclAccountNumber) {
        if (fiscalEvent != null && fiscalEvent.getAmountDetails() != null && !fiscalEvent.getAmountDetails().isEmpty()
                && !StringUtils.isEmpty(mgramsevaTenantId)) {

            for (FiscalEventAmountDTO fiscalEventAmount : fiscalEvent.getAmountDetails()) {
                ChallanRequestDTO challanRequestDTO = new ChallanRequestDTO();
                challanRequestDTO.setTenantId(mgramsevaTenantId);
                challanRequestDTO.setBusinessService(applicationConfiguration.getMgramsevaPspclBusinessService());
                challanRequestDTO.setConsumerType(applicationConfiguration.getMgramsevaPspclConsumerType());
                challanRequestDTO.setTypeOfExpense(applicationConfiguration.getMgramsevaPspclTypeOfExpense());
                challanRequestDTO.setVendor(mgramsevaVendorService.getVendorIdByTenantId(mgramsevaTenantId,
                        applicationConfiguration.getMgramsevaPspclVendorName()));
                challanRequestDTO.setVendorName(applicationConfiguration.getMgramsevaPspclVendorName());
                challanRequestDTO.setBillDate(fiscalEvent.getEventTime());
                challanRequestDTO.setIsBillPaid(false);
                challanRequestDTO.setTaxPeriodFrom(fiscalEventAmount.getFromBillingPeriod());
                challanRequestDTO.setTaxPeriodTo(fiscalEventAmount.getToBillingPeriod());
                challanRequestDTO.setReferenceId(pspclAccountNumber);

                AmountDTO amountDTO = new AmountDTO();
                amountDTO.setTaxHeadCode(applicationConfiguration.getMgramsevaPspclTaxHeadCode());
                amountDTO.setAmount(fiscalEventAmount.getAmount().doubleValue());

                challanRequestDTO.setAmount(Collections.singletonList(amountDTO));

                mgramsevaChallanRepository.pushMgramsevaCreateChallanAPI(
                        new CreateChallanRequestDTO(getMgramsevaRequestInfo(), challanRequestDTO));

                pspclEventPersistenceService.saveSuccessPspclEventDetail(mgramsevaTenantId,
                        eventType, fiscalEvent.getId(), pspclAccountNumber,
                        fiscalEventAmount.getAmount().doubleValue());
            }
        } else {
            throw new GenericCustomException(PSPCL, "Content missing from fiscal event " + fiscalEvent);
        }
    }

    /**
     * @return
     */
    private RequestInfoDTO getMgramsevaRequestInfo() {
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        requestInfoDTO.setApiId(MGRAMSEVA_CHALLAN_API_ID);
        requestInfoDTO.setVer(MGRAMSEVA_CHALLAN_VERSION);
        requestInfoDTO.setAction(MGRAMSEVA_CHALLAN_CREATE_ACTION);
        requestInfoDTO.setDid(MGRAMSEVA_CHALLAN_DID);
        requestInfoDTO.setMsgId(MGRAMSEVA_CHALLAN_MSG_ID);
        requestInfoDTO.setAuthToken(authTokenService.getMgramsevaAccessToken());

        return requestInfoDTO;
    }
}
