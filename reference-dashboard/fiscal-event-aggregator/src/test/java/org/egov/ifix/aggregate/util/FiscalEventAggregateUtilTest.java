package org.egov.ifix.aggregate.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.egov.ifix.aggregate.config.ConfigProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Application;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
class FiscalEventAggregateUtilTest {

    @Mock
    private ConfigProperties configProperties;

    @InjectMocks
    private FiscalEventAggregateUtil fiscalEventAggregateUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void testGetProjectDetailsMapWithEmptyNodeArray() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new ArrayNode(new JsonNodeFactory(true)));
        assertTrue(this.fiscalEventAggregateUtil.getProjectDetailsMap(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetProjectDetailsMapWithDefaultNodeValue() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new BigIntegerNode(BigInteger.valueOf(1L)));
        assertTrue(this.fiscalEventAggregateUtil.getProjectDetailsMap(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetProjectDetailsMapWithValues() throws IllegalArgumentException {
        ArrayNode arrayNode = mock(ArrayNode.class);
        DoubleNode valueOfResult = DoubleNode.valueOf(10.0);
        DoubleNode valueOfResult1 = DoubleNode.valueOf(10.0);
        when(arrayNode.elements())
                .thenReturn(new ArrayIterator<>(new JsonNode[]{valueOfResult, valueOfResult1, DoubleNode.valueOf(10.0)}));
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(arrayNode);
        assertTrue(this.fiscalEventAggregateUtil.getProjectDetailsMap(new ArrayList<>()).isEmpty());
        verify(arrayNode).elements();
    }

    @Test
    void testGetCOADetailsMapWithEmptyNode() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new ArrayNode(new JsonNodeFactory(true)));
        assertTrue(this.fiscalEventAggregateUtil.getCOADetailsMap(new ArrayList<>()).isEmpty());
        verify(this.objectMapper).convertValue((Object) any(), (Class<Object>) any());
    }

    @Test
    void testGetCOADetailsMapWithDefaultNode() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new BigIntegerNode(BigInteger.valueOf(1L)));
        assertTrue(this.fiscalEventAggregateUtil.getCOADetailsMap(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetCOADetailsMapWithNodeValues() throws IllegalArgumentException {
        ArrayNode arrayNode = mock(ArrayNode.class);
        DoubleNode valueOfResult = DoubleNode.valueOf(10.0);
        DoubleNode valueOfResult1 = DoubleNode.valueOf(10.0);
        when(arrayNode.elements())
                .thenReturn(new ArrayIterator<>(new JsonNode[]{valueOfResult, valueOfResult1, DoubleNode.valueOf(10.0)}));
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(arrayNode);
        assertTrue(this.fiscalEventAggregateUtil.getCOADetailsMap(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetFiscalEventAggregateDataWithEmptyAggregatedList() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new ArrayNode(new JsonNodeFactory(true)));
        ArrayList<Object> groupByResponses = new ArrayList<>();
        HashMap<String, JsonNode> projectNodeMap = new HashMap<>(1);
        assertTrue(
                this.fiscalEventAggregateUtil.getFiscalEventAggregateData(groupByResponses, projectNodeMap, new HashMap<>(1), 1)
                        .isEmpty());
    }

    @Test
    void testGetFiscalEventAggregateDataWithDefaultList() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new BigIntegerNode(BigInteger.valueOf(1L)));
        ArrayList<Object> groupByResponses = new ArrayList<>();
        HashMap<String, JsonNode> projectNodeMap = new HashMap<>(1);
        assertTrue(
                this.fiscalEventAggregateUtil.getFiscalEventAggregateData(groupByResponses, projectNodeMap, new HashMap<>(1), 1)
                        .isEmpty());
    }

    @Test
    void testGetFiscalEventAggregateDataWithAggregatedValues() throws IllegalArgumentException {
        ArrayNode arrayNode = mock(ArrayNode.class);
        DoubleNode valueOfResult = DoubleNode.valueOf(10.0);
        DoubleNode valueOfResult1 = DoubleNode.valueOf(10.0);
        when(arrayNode.elements())
                .thenReturn(new ArrayIterator<>(new JsonNode[]{valueOfResult, valueOfResult1, DoubleNode.valueOf(10.0)}));
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(arrayNode);
        ArrayList<Object> groupByResponses = new ArrayList<>();
        HashMap<String, JsonNode> projectNodeMap = new HashMap<>(1);
        assertTrue(
                this.fiscalEventAggregateUtil.getFiscalEventAggregateData(groupByResponses, projectNodeMap, new HashMap<>(1), 1)
                        .isEmpty());
    }

    @Test
    void testGetEventTypeMap() {
        assertTrue(this.fiscalEventAggregateUtil.getEventTypeMap(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetEventTypeMapWithEmptyValues() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(new ArrayNode(new JsonNodeFactory(true)));

        ArrayList<Object> objectList = new ArrayList<>();
        objectList.add("42");
        assertTrue(this.fiscalEventAggregateUtil.getEventTypeMap(objectList).isEmpty());
    }

    @Test
    void testGetEventTypeMapWithValues() throws IllegalArgumentException {
        ArrayNode arrayNode = mock(ArrayNode.class);
        DoubleNode valueOfResult = DoubleNode.valueOf(10.0);
        DoubleNode valueOfResult1 = DoubleNode.valueOf(10.0);
        when(arrayNode.elements())
                .thenReturn(new ArrayIterator<>(new JsonNode[]{valueOfResult, valueOfResult1, DoubleNode.valueOf(10.0)}));
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(arrayNode);

        ArrayList<Object> objectList = new ArrayList<>();
        objectList.add("42");
        assertTrue(this.fiscalEventAggregateUtil.getEventTypeMap(objectList).isEmpty());
    }

    @Test
    void testGetPendingCollectionFiscalEventAggregatedDataWithEmptyValues() {
        HashMap<String, JsonNode> firstEventTypeNodeMap = new HashMap<>(1);
        HashMap<String, JsonNode> secondEventTypeNodeMap = new HashMap<>(1);
        assertTrue(this.fiscalEventAggregateUtil
                .getPendingCollectionFiscalEventAggregatedData(firstEventTypeNodeMap, secondEventTypeNodeMap, new HashMap<>(1),
                        "Pending Event Type", 1)
                .isEmpty());
    }

    @Test
    void testGetPendingCollectionFiscalEventAggregatedDataWithValues() {
        ArrayNode arrayNode = mock(ArrayNode.class);
        when(arrayNode.get((String) any())).thenReturn(DoubleNode.valueOf(10.0));

        HashMap<String, JsonNode> stringJsonNodeMap = new HashMap<>(1);
        stringJsonNodeMap.put("foo", arrayNode);
        HashMap<String, JsonNode> secondEventTypeNodeMap = new HashMap<>(1);
        assertEquals(1,
                this.fiscalEventAggregateUtil
                        .getPendingCollectionFiscalEventAggregatedData(stringJsonNodeMap, secondEventTypeNodeMap, new HashMap<>(1),
                                "Pending Event Type", Integer.MIN_VALUE)
                        .size());
        verify(arrayNode, atLeast(1)).get((String) any());
    }

    @Test
    void testGetFiscalYear() {
        Map<String, Integer> actualFiscalYear = this.fiscalEventAggregateUtil.getFiscalYear();
        assertEquals(2, actualFiscalYear.size());
        Integer expectedGetResult = new Integer(2021);
        assertEquals(expectedGetResult, actualFiscalYear.get(FiscalEventAggregateConstants.PREVIOUS_FISCAL_YEAR));
        Integer expectedGetResult1 = new Integer(2022);
        assertEquals(expectedGetResult1, actualFiscalYear.get(FiscalEventAggregateConstants.CURRENT_FISCAL_YEAR));
    }
}

