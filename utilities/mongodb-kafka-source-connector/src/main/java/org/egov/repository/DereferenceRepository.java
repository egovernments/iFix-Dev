package org.egov.repository;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.egov.util.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DereferenceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GlobalProperties globalProperties;

    /**
     * @return
     */
    public FindIterable<Document> findAllFiscalEvent() {
        return mongoTemplate.getDb()
                .getCollection(globalProperties.getMongoDBCollectionName())
                .find();
    }

}
