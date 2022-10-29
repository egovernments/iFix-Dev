package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.producer.Producer;
import org.egov.repository.ChartOfAccountRepository;
import org.egov.validator.ChartOfAccountValidator;
import org.egov.web.models.COARequest;
import org.egov.web.models.COASearchCriteria;
import org.egov.web.models.COASearchRequest;
import org.egov.web.models.ChartOfAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    @Autowired
    private ChartOfAccountRepository coaRepository;

    /**
     * upsert a COA in the Master data system.
     *
     * @param coaRequest
     * @return
     */
    public COARequest chartOfAccountV1CreatePost(COARequest coaRequest) {
        validator.validateCreatePost(coaRequest);
        enricher.enrichCreatePost(coaRequest);
        //producer.push(mdsConfig.getCoaSaveTopic(),coaRequest);
        coaRepository.save(coaRequest.getChartOfAccount());
        return coaRequest;
    }


    /**
     * Search the Chart of Account based on search criteria
     *
     * @param coaSearchRequest
     * @return
     */
    public List<ChartOfAccount> chartOfAccountV1SearchPost(COASearchRequest coaSearchRequest) {
        validator.validateSearchPost(coaSearchRequest);
        enricher.enrichSearchPost(coaSearchRequest);

        COASearchCriteria searchCriteria = coaSearchRequest.getCriteria();

        if (searchCriteria.isEmpty())
            Collections.emptyList();

        List<ChartOfAccount> chartOfAccounts = coaRepository.search(searchCriteria);

        if (chartOfAccounts == null || chartOfAccounts.isEmpty())
            Collections.emptyList();

        return chartOfAccounts;
    }
}
