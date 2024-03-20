package org.digit.exchange.controller;

import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.constants.ExchangeType;
import org.digit.exchange.exception.ResourceNotFoundException;
import org.digit.exchange.model.RequestMessage;
import org.digit.exchange.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
@Slf4j
public class ExchangeController {

    private ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping(value = "/exchange/{exchangeType}")
    public ResponseEntity<RequestMessage> program(@RequestBody RequestMessage messageRequest, @PathVariable("exchangeType") String type) {
        log.info("Received message from: {} to {}", messageRequest.getHeader().getSenderId(), messageRequest.getHeader().getReceiverId());
        try {
            RequestMessage response = exchangeService.processMessage(ExchangeType.fromValue(type), messageRequest);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
