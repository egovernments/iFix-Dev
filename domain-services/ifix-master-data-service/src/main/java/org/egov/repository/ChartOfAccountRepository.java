package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.queryBuilder.ChartOfAccountQueryBuilder;
import org.egov.tracer.model.ServiceCallException;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ChartOfAccountRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ChartOfAccountQueryBuilder coaQueryBuilder;

    @Autowired
    private RestTemplate restTemplate;

    public void save(ChartOfAccount chartOfAccount) {
        mongoTemplate.save(chartOfAccount);
    }

    public List<ChartOfAccount> search(COASearchCriteria searchCriteria) {
        Query searchQuery = coaQueryBuilder.buildSearchQuery(searchCriteria);
        return (mongoTemplate.find(searchQuery,ChartOfAccount.class));
    }

    /**
     *
     * @param url
     * @param govtSearchRequest
     * @return
     */
    public List<Government> searchTenants(String url, GovernmentSearchRequest govtSearchRequest) {

        GovernmentResponse governmentResponse = null;
        try {
            governmentResponse = restTemplate.postForObject(url, govtSearchRequest, GovernmentResponse.class);
        } catch (HttpClientErrorException e) {
            log.error("Government Service threw an Exception: ", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception while fetching from searcher: ", e);
        }

        if (governmentResponse != null)
            return governmentResponse.getGovernment();
        return Collections.emptyList();
    }
}
