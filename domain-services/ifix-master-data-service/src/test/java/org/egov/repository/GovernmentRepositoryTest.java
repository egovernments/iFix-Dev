package org.egov.repository;

import org.egov.config.TestDataFormatter;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.GovernmentResponse;
import org.egov.web.models.GovernmentSearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class GovernmentRepositoryTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private GovernmentRepository governmentRepository;

    private GovernmentRequest governmentCreateRequest;
    private GovernmentSearchRequest governmentSearchRequest;
    private GovernmentResponse governmentSearchResponse;

    private ArrayList<String> idList;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("GOVERNME-NT0B-CDEF-FEDC-ENTITY0UID10");

        governmentCreateRequest = testDataFormatter.getGovernmentRequestData();
        governmentSearchRequest = testDataFormatter.getGovernmentSearchRequestData();
        governmentSearchResponse = testDataFormatter.getGovernmentSearchResponseData();
    }


    @Test
    void testSave() {
        assertNotNull(governmentCreateRequest.getGovernment());
        doReturn(governmentCreateRequest.getGovernment()).when(mongoTemplate)
                .save(governmentCreateRequest.getGovernment());

        governmentRepository.save(governmentCreateRequest.getGovernment());

        verify(mongoTemplate).save(governmentCreateRequest.getGovernment());
    }

    @Test
    void testFindById() {
        assertNotNull(governmentCreateRequest.getGovernment());
        assertNotNull(governmentCreateRequest.getGovernment().getId());

        doReturn(governmentCreateRequest.getGovernment()).when(mongoTemplate)
                .findById(governmentCreateRequest.getGovernment().getId(), Government.class);

        Government actualGovernment = governmentRepository.findById(governmentCreateRequest.getGovernment().getId());

        assertSame(governmentCreateRequest.getGovernment(), actualGovernment);
    }

    @Test
    void testFindAllByIdList() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(idList));

        doReturn(governmentSearchResponse.getGovernment()).when(mongoTemplate).find(query, Government.class);

        List<Government> actualGovernmentList = governmentRepository.findAllByIdList(idList);

        assertSame(governmentSearchResponse.getGovernment(), actualGovernmentList);
    }
}