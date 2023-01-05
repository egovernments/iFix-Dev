/*
 * package org.egov.repository;
 * 
 * import org.egov.web.models.Government; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.data.mongodb.core.MongoTemplate; import
 * org.springframework.data.mongodb.core.query.Criteria; import
 * org.springframework.data.mongodb.core.query.Query; import
 * org.springframework.stereotype.Repository;
 * 
 * import java.util.List;
 * 
 * @Repository public class GovernmentRepository {
 * 
 * @Autowired private MongoTemplate mongoTemplate;
 * 
 * public void save(Government government) { mongoTemplate.save(government); }
 * 
 * public Government findById(String governmentId) { return
 * mongoTemplate.findById(governmentId, Government.class); }
 * 
 * public List<Government> findAllByIdList(List<String> idList) { Query query =
 * new Query(); query.addCriteria(Criteria.where("id").in(idList)); return
 * mongoTemplate.find(query, Government.class); }
 * 
 * }
 */