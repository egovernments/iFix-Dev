package org.egov.repository;

import org.egov.repository.queryBuilder.ExpenditureQueryBuilder;
import org.egov.web.models.Expenditure;
import org.egov.web.models.ExpenditureSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExpenditureRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    ExpenditureQueryBuilder expenditureQueryBuilder;

    /**
     * @param expenditureSearchCriteria
     * @return
     */
    public List<Expenditure> findAllByCriteria(ExpenditureSearchCriteria expenditureSearchCriteria) {
        return mongoTemplate.find(expenditureQueryBuilder.buildQuerySearch(expenditureSearchCriteria), Expenditure.class);
    }

    public void save(Expenditure expenditure) {
        mongoTemplate.save(expenditure);
    }
}
