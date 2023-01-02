package org.egov.migration;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;


@ChangeLog(order = "V20220215113000")
@Slf4j
public class V20220215113000_MongoIndexScript {

    public static final String MDS_INDEX_AUTHOR = "iFIX-Master-Data-Service";

    /**
     * @param db
     */
    @ChangeSet(id = "V20220215113000_createGovernmentIndex", author = MDS_INDEX_AUTHOR, order = "001")
    public void createGovernmentIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("government");

        mongoCollection.createIndex(Indexes.ascending("name"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20220215113000_createChartOfAccountIndex", author = MDS_INDEX_AUTHOR, order = "002")
    public void createChartOfAccountIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("chartOfAccount");

        mongoCollection.createIndex(Indexes.ascending("coaCode"));
        mongoCollection.createIndex(Indexes.ascending("majorHead"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("subMajorHead"));
        mongoCollection.createIndex(Indexes.ascending("minorHead"));
        mongoCollection.createIndex(Indexes.ascending("subHead"));
        mongoCollection.createIndex(Indexes.ascending("groupHead"));
        mongoCollection.createIndex(Indexes.ascending("objectHead"));
    }
}
