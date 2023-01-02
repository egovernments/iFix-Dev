package org.egov.ifix.service.impl;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.ChartOfAccount;
import org.egov.ifix.models.CoaMappingDTO;
import org.egov.ifix.persistance.ChartOfAccountMap;
import org.egov.ifix.persistance.ChartOfAccountMapRepository;
import org.egov.ifix.repository.ChartOfAccountRepository;
import org.egov.ifix.service.ChartOfAccountService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.COA_CODE;
import static org.egov.ifix.utils.EventConstants.LOG_INFO_PREFIX;

/**
 * @author mani
 * This will manage getting the right chartofaccount in running
 * environment. This will cache the data once received from repository.
 * Expiry is not set it will be global config which will clear cache on
 * specified time It depends on repository to provide COA object for
 * given code This will include validation whether the adapter is
 * autherised to post chartofaccount to IFIX Validated coas put to cache
 * invalid are logged
 */
@Service
@Slf4j
public class ChartOfAccountServiceImpl implements ChartOfAccountService {

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private AdapterCache<ChartOfAccount> coaCache;

    @Autowired
    private AdapterCache<String> coaCodeCache;

    @Autowired
    private DataWrapper dataWrapper;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ChartOfAccountMapRepository chartOfAccountMapRepository;


    /**
     * @param clientCoaCode
     * @return
     */
    @Override
    public String getResolvedChartOfAccountCode(String clientCoaCode) {
        String cachedCoaCode = coaCodeCache.getValue(clientCoaCode);

        if (StringUtils.isEmpty(cachedCoaCode)) {
            ChartOfAccountMap coaMapping = chartOfAccountMapRepository.findByClientCode(clientCoaCode);

            if (coaMapping != null && !StringUtils.isEmpty(coaMapping.getIFixCoaCode())) {
                coaCodeCache.putValue(clientCoaCode, coaMapping.getIFixCoaCode());

                cachedCoaCode = coaMapping.getIFixCoaCode();
            } else {
                throw new HttpCustomException(COA_CODE, "Unable to find iFix COA code by client coa code",
                        HttpStatus.BAD_REQUEST);
            }
        }else {
            log.info(LOG_INFO_PREFIX + "got coa from Cache" + cachedCoaCode);
        }

        return cachedCoaCode;
    }

    /**
     * @param clientCoaCode
     * @param jsonObject
     * @return
     */
    @Override
    public String getResolvedChartOfAccount(String clientCoaCode, JsonObject jsonObject) {
        ChartOfAccount coa = coaCache.getValue(clientCoaCode);

        if (coa == null) {
            Optional<ChartOfAccount> coaOptional = chartOfAccountRepository.getChartOfAccount(clientCoaCode, jsonObject);

            if (coaOptional.isPresent()) {
                coaCache.putValue(clientCoaCode, coaOptional.get());
            } else {
//TODO: Error handling stream.
/*
                Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(NA,
                        COA_CODE_DATA_NAME, CLIENT_COA_CODE, clientCoaCode, NON_RECOVERABLE_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        "Unable to receive iFix coa corresponding to mgramseva coa code");

                if (errorDataModelOptional.isPresent()) {
                    kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), errorDataModelOptional.get());
                }
*/
                throw new HttpCustomException(COA_CODE, "Unable to find COA by client coa code", HttpStatus.BAD_REQUEST);
            }
        } else {
            log.info(LOG_INFO_PREFIX + "got coa from Cache" + coa);
        }
        return coa.getId();
    }


    /**
     * @param clientCoaCode
     * @return
     */
    @Override
    public Optional<CoaMappingDTO> getMappedCoaIdByClientCoaCode(CoaMappingDTO coaMappingDTO) {

        if (coaMappingDTO != null) {
            ChartOfAccountMap coaMapping = chartOfAccountMapRepository.findByClientCode(coaMappingDTO.getClientCode());

            if (coaMapping != null) {
                CoaMappingDTO newCoaMappingDTO = CoaMappingDTO.builder()
                        .iFixCoaCode(coaMapping.getIFixCoaCode())
                        .clientCode(coaMappingDTO.getClientCode())
                        .build();

                return Optional.ofNullable(newCoaMappingDTO);
            }
        }

        return Optional.empty();
    }
}
