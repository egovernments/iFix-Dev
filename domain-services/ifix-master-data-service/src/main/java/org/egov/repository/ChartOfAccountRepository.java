package org.egov.repository;


import org.egov.repository.queryBuilder.ChartOfAccountQueryBuilder;
import org.egov.web.models.COASearchCriteria;
import org.egov.web.models.ChartOfAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChartOfAccountRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ChartOfAccountQueryBuilder coaQueryBuilder;

    public void save(ChartOfAccount chartOfAccount) {
        mongoTemplate.save(chartOfAccount);
    }

    public List<ChartOfAccount> search(COASearchCriteria searchCriteria) {
        Query searchQuery = coaQueryBuilder.buildSearchQuery(searchCriteria);
        return (mongoTemplate.find(searchQuery,ChartOfAccount.class));
    }
}
