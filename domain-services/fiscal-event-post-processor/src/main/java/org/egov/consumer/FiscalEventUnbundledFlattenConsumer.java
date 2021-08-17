package org.egov.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.producer.Producer;
import org.egov.service.FiscalEventUnbundleService;
import org.egov.web.models.FiscalEventDeReferenced;
import org.egov.web.models.FiscalEventLineItemUnbundled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class FiscalEventUnbundledFlattenConsumer {

    @Autowired
    private FiscalEventUnbundleService unbundleService;

    @Autowired
    private FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    private Producer producer;

    @Autowired
    private ObjectMapper mapper;

    @KafkaListener(topics = {"${fiscal.event.kafka.dereferenced.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            FiscalEventDeReferenced fiscalEventDeReferenced = mapper.convertValue(record, FiscalEventDeReferenced.class);
            List<FiscalEventLineItemUnbundled> fiscalEventLineItemUnbundledList  = unbundleService.unbundle(fiscalEventDeReferenced);
//           TODO: flatten before push

            if (fiscalEventLineItemUnbundledList != null) {
                fiscalEventLineItemUnbundledList.stream()
                        .forEach(fiscalEventLineItemUnbundled ->
                                producer.push(processorConfig.getFiscalEventDruidTopic(), fiscalEventLineItemUnbundled));
            }

//            producer.push(processorConfig.getFiscalEventDruidTopic(), fiscalEventLineItemUnbundledList);
        } catch (Exception e) {
            log.error("Error occurred while processing the record from topic : " + topic, e);
        }
    }
}
