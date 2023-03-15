package org.egov.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.models.Bill;
import org.egov.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class BillConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BillService billService;

    @KafkaListener(topics = {"${bill.kafka.topic}"})
    public void listen(HashMap<String, Object> record) {
        Bill bill = objectMapper.convertValue(record, Bill.class);
        billService.processBill(bill);
    }

}
