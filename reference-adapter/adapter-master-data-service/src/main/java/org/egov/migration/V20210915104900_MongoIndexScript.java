package org.egov.migration;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;


@ChangeLog(order = "V20220214104900")
@Slf4j
public class V20210915104900_MongoIndexScript {

    public static final String MDS_INDEX_AUTHOR = "Adapter-Master-Data-Service";

    /**
     * @param db
     */
    @ChangeSet(id = "V20220214104900_createDepartmentIndex", author = MDS_INDEX_AUTHOR, order = "001")
    public void createDepartmentIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("department");

        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20220214104900_createExpenditureIndex", author = MDS_INDEX_AUTHOR, order = "002")
    public void createExpenditureIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("expenditure");

        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20220214104900_createProjectIndex", author = MDS_INDEX_AUTHOR, order = "003")
    public void createProjectIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("project");

        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("expenditureId"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.id"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.code"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.name"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.departmentId"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.hierarchyLevel"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.code"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.name"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.hierarchyLevel"));
    }

}
