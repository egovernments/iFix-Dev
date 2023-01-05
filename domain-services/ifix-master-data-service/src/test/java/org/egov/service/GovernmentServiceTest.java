/*
 * package org.egov.service;
 * 
 * import org.egov.config.TestDataFormatter; import
 * org.egov.repository.GovernmentRepository; import
 * org.egov.validator.GovernmentValidator; import
 * org.egov.web.models.Government; import org.egov.web.models.GovernmentRequest;
 * import org.egov.web.models.GovernmentResponse; import
 * org.egov.web.models.GovernmentSearchRequest; import
 * org.junit.jupiter.api.BeforeAll; import org.junit.jupiter.api.Test; import
 * org.junit.jupiter.api.TestInstance; import org.mockito.InjectMocks; import
 * org.mockito.Mock; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.context.SpringBootTest;
 * 
 * import java.io.IOException; import java.util.List;
 * 
 * import static org.junit.jupiter.api.Assertions.assertEquals; import static
 * org.mockito.Mockito.doNothing; import static org.mockito.Mockito.doReturn;
 * 
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 * 
 * @SpringBootTest() class GovernmentServiceTest {
 * 
 * @Mock GovernmentValidator governmentValidator;
 * 
 * @Mock GovernmentRepository governmentRepository;
 * 
 * @Mock GovernmentEnrichmentService governmentEnrichmentService;
 * 
 * @Autowired private TestDataFormatter testDataFormatter; private
 * GovernmentRequest governmentRequest; private GovernmentSearchRequest
 * governmentSearchRequest; private GovernmentResponse governmentSearchResponse;
 * private GovernmentRequest headlessGovernmentRequest;
 * 
 * @InjectMocks private GovernmentService governmentService;
 * 
 * @BeforeAll public void init() throws IOException { governmentRequest =
 * testDataFormatter.getGovernmentRequestData(); headlessGovernmentRequest =
 * testDataFormatter.getHeadlessGovernmentRequestData(); governmentSearchRequest
 * = testDataFormatter.getGovernmentSearchRequestData();
 * 
 * governmentSearchResponse =
 * testDataFormatter.getGovernmentSearchResponseData(); }
 * 
 * 
 * @Test void addGovernment() {
 * doNothing().when(governmentValidator).validateGovernmentRequestData(
 * governmentRequest);
 * doNothing().when(governmentEnrichmentService).enrichGovernmentData(
 * governmentRequest);
 * doNothing().when(governmentRepository).save(governmentRequest.getGovernment()
 * );
 * 
 * GovernmentRequest savedGovernmentRequest =
 * governmentService.addGovernment(governmentRequest);
 * 
 * assertEquals(savedGovernmentRequest, governmentRequest); }
 * 
 * @Test void searchAllGovernmentByIdList() {
 * 
 * doNothing().when(governmentValidator).validateGovernmentSearchRequestData(
 * governmentSearchRequest);
 * 
 * doReturn(governmentSearchResponse.getGovernment()).when(governmentRepository)
 * .findAllByIdList(governmentSearchRequest.getCriteria().getIds());
 * 
 * List<Government> governmentList =
 * governmentService.searchAllGovernmentByIdList(governmentSearchRequest);
 * 
 * assertEquals(governmentList, governmentSearchResponse.getGovernment()); } }
 */