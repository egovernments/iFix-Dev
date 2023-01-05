/*
 * package org.egov.util;
 * 
 * import com.fasterxml.jackson.databind.JsonNode; import
 * com.fasterxml.jackson.databind.ObjectMapper; import
 * com.fasterxml.jackson.databind.node.ArrayNode; import
 * com.fasterxml.jackson.databind.node.BigIntegerNode; import
 * com.fasterxml.jackson.databind.node.JsonNodeFactory; import
 * org.egov.config.TestDataFormatter; import
 * org.egov.web.models.FiscalEventGetRequest; import
 * org.egov.web.models.FiscalEventResponse; import
 * org.junit.jupiter.api.BeforeAll; import org.junit.jupiter.api.Test; import
 * org.junit.jupiter.api.TestInstance; import org.mockito.InjectMocks; import
 * org.mockito.Mock; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.context.SpringBootTest;
 * 
 * import java.io.IOException; import java.math.BigInteger; import
 * java.util.ArrayList; import java.util.List;
 * 
 * import static org.junit.jupiter.api.Assertions.assertTrue; import static
 * org.mockito.Mockito.*;
 * 
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 * 
 * @SpringBootTest class FiscalEventMapperUtilTest {
 * 
 * @InjectMocks private FiscalEventMapperUtil fiscalEventMapperUtil;
 * 
 * @Mock private ObjectMapper objectMapper;
 * 
 * @Autowired private TestDataFormatter testDataFormatter;
 * 
 * private FiscalEventGetRequest fiscalEventGetRequest; private
 * FiscalEventResponse fiscalEventSearchResponse; private JsonNode jsonNode;
 * 
 * @BeforeAll void init() throws IOException { fiscalEventGetRequest =
 * testDataFormatter.getFiscalEventSearchRequestData();
 * fiscalEventSearchResponse =
 * testDataFormatter.getFiscalEventSearchResponseData(); ObjectMapper mapper =
 * new ObjectMapper(); jsonNode =
 * mapper.convertValue(fiscalEventSearchResponse.getFiscalEvent(),
 * JsonNode.class); }
 * 
 * @Test void testMapDereferencedFiscalEventToFiscalEventWithEmptyList() throws
 * IllegalArgumentException { when(this.objectMapper.convertValue((Object)
 * any(), (Class<Object>) any())) .thenReturn(new ArrayNode(new
 * JsonNodeFactory(true)));
 * assertTrue(this.fiscalEventMapperUtil.mapDereferencedFiscalEventToFiscalEvent
 * (new ArrayList<Object>()).isEmpty());
 * verify(this.objectMapper).convertValue((Object) any(), (Class<Object>)
 * any()); }
 * 
 * @Test void testMapDereferencedFiscalEventToFiscalEvent() throws
 * IllegalArgumentException {
 * doReturn(jsonNode).when(objectMapper).convertValue((Object) any(),
 * (Class<Object>) any());
 * assertTrue(this.fiscalEventMapperUtil.mapDereferencedFiscalEventToFiscalEvent
 * (new ArrayList<Object>()).size()>0);
 * 
 * verify(this.objectMapper).convertValue((Object) any(), (Class<Object>)
 * any()); } }
 * 
 */