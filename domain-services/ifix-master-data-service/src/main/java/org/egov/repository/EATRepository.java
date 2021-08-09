package org.egov.repository;

import org.egov.repository.queryBuilder.EATQueryBuilder;
import org.egov.web.models.EAT;
import org.egov.web.models.EatSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EATRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    EATQueryBuilder eatQueryBuilder;

    /**
     * @param eatSearchCriteria
     * @return
     */
    public List<EAT> findAllByCriteria(EatSearchCriteria eatSearchCriteria) {
        return mongoTemplate.find(eatQueryBuilder.buildQuerySearch(eatSearchCriteria), EAT.class);
    }
}
