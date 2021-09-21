package org.egov.migration;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


@ChangeLog(order = "001")
@Slf4j
public class MongoIndexScript {
    public static final String FE_INDEX_AUTHOR = "iFIX-Fiscal-Event-Service";

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914184200_createBasicFiscalEventIndex", author = FE_INDEX_AUTHOR, order = "001")
    public void createBasicFiscalEventIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("fiscal_event");

        mongoCollection.createIndex(Indexes.ascending("eventType"));
        mongoCollection.createIndex(Indexes.ascending("eventTime"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("referenceId"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914185000_createFiscalEventIndex", author = FE_INDEX_AUTHOR, order = "002")
    public void createFiscalEventIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("fiscal_event");

        mongoCollection.createIndex(Indexes.ascending("ingestionTime"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.hierarchyLevel"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.code"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.name"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.id"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.code"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.name"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.hierarchyLevel"));
        mongoCollection.createIndex(Indexes.ascending("expenditure.id"));
        mongoCollection.createIndex(Indexes.ascending("project.id"));
        mongoCollection.createIndex(Indexes.ascending("parentEventId"));
        mongoCollection.createIndex(Indexes.ascending("amountDetails.coa.id"));
    }

}
