package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.producer.Producer;
import org.egov.validator.ChartOfAccountValidator;
import org.egov.web.models.COARequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChartOfAccountService {

    @Autowired
    private ChartOfAccountValidator validator;

    @Autowired
    private COAEnrichmentService enricher;

    @Autowired
    private Producer producer;

    @Autowired
    private MasterDataServiceConfiguration mdsConfig;

    /**
     * upsert a COA in the Master data system.
     * @param coaRequest
     * @return
     */
    public COARequest chartOfAccountV1CreatePost(COARequest coaRequest) {
        validator.validateCreatePost(coaRequest);
        enricher.enrichCreatePost(coaRequest);
        producer.push(mdsConfig.getCoaSaveTopic(),coaRequest);
        return coaRequest;
    }
}
