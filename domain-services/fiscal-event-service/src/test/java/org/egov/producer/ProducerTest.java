//package org.egov.producer;
//
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.TopicPartition;
//import org.egov.tracer.kafka.CustomKafkaTemplate;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.mockito.Mockito.*;
//
//@ContextConfiguration(classes = {Producer.class, CustomKafkaTemplate.class})
//@ExtendWith(SpringExtension.class)
//class ProducerTest {
//    @MockBean(name = "customKafkaTemplate")
//    private CustomKafkaTemplate<String, Object> customKafkaTemplate;
//
//    @Autowired
//    private Producer producer;
//
//    @Test
//    void testPush() {
//        ProducerRecord<String, Object> producerRecord = new ProducerRecord<String, Object>("Topic", "Value");
//
//        when(this.customKafkaTemplate.send((String) any(), (Object) any())).thenReturn(new SendResult<String, Object>(
//                producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1L, 10L, 1L, 3, 3)));
//        this.producer.push("Topic", "Value");
//        verify(this.customKafkaTemplate).send((String) any(), (Object) any());
//    }
//}
//
