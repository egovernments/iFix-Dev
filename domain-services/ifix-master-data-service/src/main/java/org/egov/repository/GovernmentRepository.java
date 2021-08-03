package org.egov.repository;

import org.egov.web.models.Government;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GovernmentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Government government) {
        mongoTemplate.save(government);
    }

}
