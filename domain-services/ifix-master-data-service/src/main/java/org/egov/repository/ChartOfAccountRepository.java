package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.queryBuilder.ChartOfAccountQueryBuilder;
import org.egov.tracer.model.ServiceCallException;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

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
}
