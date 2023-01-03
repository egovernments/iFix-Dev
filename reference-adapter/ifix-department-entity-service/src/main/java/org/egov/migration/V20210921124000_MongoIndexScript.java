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
public class V20210921124000_MongoIndexScript {
    public static final String DE_INDEX_AUTHOR = "iFIX-Department-Entity-Service";

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914184200_createDepartmentHierarchyIndex", author = DE_INDEX_AUTHOR, order = "001")
    public void createBasicDepartmentEntityIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("departmentHierarchyLevel");

        mongoCollection.createIndex(Indexes.ascending("departmentId"));
        mongoCollection.createIndex(Indexes.ascending("label"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("level"));
        mongoCollection.createIndex(Indexes.ascending("parent"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914185000_createDepartmentEntityIndex", author = DE_INDEX_AUTHOR, order = "002")
    public void createDepartmentEntityIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("departmentEntity");

        mongoCollection.createIndex(Indexes.ascending("departmentId"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("level"));
        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("children"));
    }

}
