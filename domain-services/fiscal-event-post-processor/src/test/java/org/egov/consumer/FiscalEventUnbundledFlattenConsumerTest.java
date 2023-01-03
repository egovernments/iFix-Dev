package org.egov.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.config.TestDataFormatter;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.producer.Producer;
import org.egov.service.FiscalEventFlattenService;
import org.egov.service.FiscalEventUnbundleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventUnbundledFlattenConsumerTest {

    @Mock
    private FiscalEventFlattenService fiscalEventFlattenService;

    @Mock
    private FiscalEventPostProcessorConfig fiscalEventPostProcessorConfig;

    @Mock
    private FiscalEventUnbundleService fiscalEventUnbundleService;

    @InjectMocks
    private FiscalEventUnbundledFlattenConsumer fiscalEventUnbundledFlattenConsumer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Producer producer;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventDeReferenced fiscalEventDeReferenced;

    @BeforeAll
    void init() throws IOException {
        fiscalEventDeReferenced = testDataFormatter.getFiscalEventDereferencedData();
    }

    @Test
    void testListen() {
        when(this.objectMapper.enable((com.fasterxml.jackson.databind.DeserializationFeature) any()))
                .thenReturn(this.objectMapper);
        this.fiscalEventUnbundledFlattenConsumer.listen(new HashMap<String, Object>(1), "fiscal-event-request-dereferenced");
        verify(this.objectMapper).enable((com.fasterxml.jackson.databind.DeserializationFeature) any());
    }

    @Test
    void testListenWithUnbundledList() {
        when(this.objectMapper.enable((com.fasterxml.jackson.databind.DeserializationFeature) any()))
                .thenReturn(this.objectMapper);
        List<String> strings = new ArrayList<>();
        strings.add("id");
        when(fiscalEventFlattenService.getFlattenData(any())).thenReturn(strings);
        this.fiscalEventUnbundledFlattenConsumer.listen(new HashMap<String, Object>(1), "Topic");
        verify(this.objectMapper).enable((com.fasterxml.jackson.databind.DeserializationFeature) any());
    }

    @Test
    void testListenWithConversionException() {
        when(this.objectMapper.enable((com.fasterxml.jackson.databind.DeserializationFeature) any()))
                .thenReturn(this.objectMapper);
        when(this.objectMapper.convertValue((Map) any(), (Class<Object>) any()))
                .thenThrow(new IllegalArgumentException());
        List<String> strings = new ArrayList<>();
        strings.add("id");
        when(fiscalEventFlattenService.getFlattenData(any())).thenReturn(strings);
        HashMap<String, Object> hashMap = new HashMap<String, Object>(1);
        assertThrows(RuntimeException.class,
                () -> this.fiscalEventUnbundledFlattenConsumer.listen(hashMap, "Topic"));
        verify(this.objectMapper).enable((com.fasterxml.jackson.databind.DeserializationFeature) any());
    }
}

