//package org.egov.producer;
//
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.TopicPartition;
//import org.egov.tracer.kafka.CustomKafkaTemplate;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.support.SendResult;
//
//import static org.mockito.Mockito.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
//class ProducerTest {
//
//    @Mock
//    private CustomKafkaTemplate<String, Object> customKafkaTemplate;
//
//    @InjectMocks
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
