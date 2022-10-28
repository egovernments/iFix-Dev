package org.egov.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.config.TestDataFormatter;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.models.FiscalEventRequest;
import org.egov.producer.Producer;
import org.egov.service.FiscalEventDereferenceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventDereferenceConsumerTest {

    @InjectMocks
    private FiscalEventDereferenceConsumer fiscalEventDereferenceConsumer;

    @Mock
    private FiscalEventDereferenceService fiscalEventDereferenceService;

    @Mock
    private FiscalEventPostProcessorConfig fiscalEventPostProcessorConfig;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Producer producer;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventRequest fiscalEventRequest;

    @BeforeAll
    void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventValidatedData();
    }

    @Test
    void testListenWithInvalidFiscalEventData() throws IllegalArgumentException {
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenThrow(new RuntimeException("foo"));
        HashMap<String, Object> hMap = new HashMap<String, Object>(1);
        assertThrows(RuntimeException.class,
                () -> this.fiscalEventDereferenceConsumer.listen(hMap, "Topic"));
        verify(this.objectMapper).convertValue((Object) any(), (Class<Object>) any());
    }

    @Test
    void testListenWithValidFiscalEventData() throws IllegalArgumentException {
        doNothing().when(this.producer).push((String) any(), (Object) any());
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(fiscalEventRequest);
        when(this.fiscalEventPostProcessorConfig.getFiscalEventDereferenceTopic())
                .thenReturn("Fiscal Event Dereference Topic");
        when(this.fiscalEventDereferenceService.dereference((FiscalEventRequest) any()))
                .thenReturn(new FiscalEventDeReferenced());
        this.fiscalEventDereferenceConsumer.listen(new HashMap<String, Object>(1), "fiscal-event-request-validated");
        verify(this.producer, atLeast(1)).push((String) any(), (Object) any());
        verify(this.objectMapper).convertValue((Object) any(), (Class<Object>) any());
        verify(this.fiscalEventPostProcessorConfig).getFiscalEventDereferenceTopic();
        verify(this.fiscalEventDereferenceService).dereference((FiscalEventRequest) any());
    }

    @Test
    void testListenWithUnknownTopic() throws IllegalArgumentException {
        doNothing().when(this.producer).push((String) any(), (Object) any());
        when(this.objectMapper.convertValue((Object) any(), (Class<Object>) any())).thenReturn(fiscalEventRequest);
        when(this.fiscalEventPostProcessorConfig.getFiscalEventDereferenceTopic()).thenThrow(new RuntimeException("foo"));
        when(this.fiscalEventDereferenceService.dereference((FiscalEventRequest) any()))
                .thenReturn(new FiscalEventDeReferenced());
        HashMap<String, Object> hMap = new HashMap<String, Object>(1);
        assertThrows(RuntimeException.class,
                () -> this.fiscalEventDereferenceConsumer.listen(hMap, "Topic"));
        verify(this.objectMapper).convertValue((Object) any(), (Class<Object>) any());
        verify(this.fiscalEventPostProcessorConfig).getFiscalEventDereferenceTopic();
        verify(this.fiscalEventDereferenceService).dereference((FiscalEventRequest) any());
    }
}

