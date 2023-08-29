package org.egov.ifixespipeline.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixespipeline.models.FiscalClientInput;
import org.egov.ifixespipeline.models.FiscalEvent;
import org.egov.ifixespipeline.models.FiscalEventBulkRequest;
import org.egov.ifixespipeline.service.FiscalDataEnrichmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/es")
public class TestController {

    @Autowired
    FiscalDataEnrichmentService fiscalDataEnrichmentService;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value="/v1/_test", method = RequestMethod.POST)
    public ResponseEntity<FiscalEventBulkRequest> testNewAuditFlow(@RequestBody @Valid FiscalClientInput input) throws JsonProcessingException {
        log.info("Received request: " + input.toString());
//        List<Integer> responseHash = ingestService.ingestData(ingestRequest);
//        //log.info("############ Completed before pushing data");
//        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(ingestRequest.getRequestInfo(), true);
//        IngestResponse response = IngestResponse.builder().responseInfo(responseInfo).responseHash(responseHash).build();
        FiscalEventBulkRequest fiscalEventBulkRequest = objectMapper.readValue(input.getJson(), FiscalEventBulkRequest.class);
        for(FiscalEvent fiscalEvent : fiscalEventBulkRequest.getFiscalEvent()) {
            fiscalDataEnrichmentService.enrichFiscalData(fiscalEvent);
//            fiscalDataEnrichmentService.enrichComputedFields(fiscalEvent);
        }
        return new ResponseEntity<>(fiscalEventBulkRequest, HttpStatus.OK);
    }
}
