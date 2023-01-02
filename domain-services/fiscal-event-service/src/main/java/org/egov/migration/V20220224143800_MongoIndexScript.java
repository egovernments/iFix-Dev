package org.egov.migration;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;


@ChangeLog(order = "001")
@Slf4j
public class V20220224143800_MongoIndexScript {
    public static final String FE_INDEX_AUTHOR = "iFIX-Fiscal-Event-Service";

    /**
     * @param db
     */
    @ChangeSet(id = "V20220224185000_createFiscalEventIndex", author = FE_INDEX_AUTHOR, order = "001")
    public void createFiscalEventIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("fiscal_event");
        mongoCollection.createIndex(Indexes.ascending("eventType"));
        mongoCollection.createIndex(Indexes.ascending("eventTime"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("referenceId"));
        mongoCollection.createIndex(Indexes.ascending("ingestionTime"));
        mongoCollection.createIndex(Indexes.ascending("linkedEventId"));
        mongoCollection.createIndex(Indexes.ascending("amountDetails.coa.id"));
        mongoCollection.createIndex(Indexes.ascending("amountDetails.amount"));
    }

}
