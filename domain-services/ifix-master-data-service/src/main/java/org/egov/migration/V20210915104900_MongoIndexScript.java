package org.egov.migration;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.egov.config.MasterDataServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;


@ChangeLog(order = "V20210915104900")
@Slf4j
public class V20210915104900_MongoIndexScript {

    public static final String MDS_INDEX_AUTHOR = "iFIX-Master-Data-Service";

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914162000_createGovernmentIndex", author = MDS_INDEX_AUTHOR, order = "001")
    public void createGovernmentIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("government");

        mongoCollection.createIndex(Indexes.ascending("name"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914164000_createDepartmentIndex", author = MDS_INDEX_AUTHOR, order = "002")
    public void createDepartmentIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("department");

        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914174400_createChartOfAccountIndex", author = MDS_INDEX_AUTHOR, order = "003")
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

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914175000_createExpenditureIndex", author = MDS_INDEX_AUTHOR, order = "004")
    public void createExpenditureIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("expenditure");

        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
    }

    /**
     * @param db
     */
    @ChangeSet(id = "V20210914175400_createProjectIndex", author = MDS_INDEX_AUTHOR, order = "005")
    public void createProjectIndex(MongoDatabase db) {
        MongoCollection<Document> mongoCollection = db.getCollection("project");

        mongoCollection.createIndex(Indexes.ascending("name"));
        mongoCollection.createIndex(Indexes.ascending("code"));
        mongoCollection.createIndex(Indexes.ascending("tenantId"));
        mongoCollection.createIndex(Indexes.ascending("expenditureId"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.code"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.name"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.departmentId"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.hierarchyLevel"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.code"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.name"));
        mongoCollection.createIndex(Indexes.ascending("departmentEntity.ancestry.hierarchyLevel"));
    }

}
