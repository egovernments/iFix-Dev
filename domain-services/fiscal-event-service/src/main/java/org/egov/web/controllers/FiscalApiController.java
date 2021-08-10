package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.FiscalEventGetRequest;
import org.egov.web.models.FiscalEventRequest;
import org.egov.web.models.FiscalEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.FiscalEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:28:42.515+05:30")

@Controller
@RequestMapping("/events/v1")
public class FiscalApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private FiscalEventService fiscalEventService;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    @Autowired
    public FiscalApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_push", method = RequestMethod.POST)
    public ResponseEntity<FiscalEventResponse> fiscalEventsV1PushPost(@ApiParam(value = "Details for the new fiscal event + RequestHeader (meta data of the API).", required = true) @Valid @RequestBody FiscalEventRequest body) {
        FiscalEventRequest fiscalEventRequest = fiscalEventService.fiscalEventsV1PushPost(body);
        ResponseHeader responseHeader = responseHeaderCreator.createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);
        FiscalEventResponse fiscalEventResponse = FiscalEventResponse.builder().responseInfo(responseHeader)
                .fiscalEvent(Collections.singletonList(fiscalEventRequest.getFiscalEvent())).build();
        return new ResponseEntity<FiscalEventResponse>(fiscalEventResponse, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<FiscalEventResponse> fiscalEventsV1SearchPost(@ApiParam(value = "RequestHeader meta data.", required = true) @Valid @RequestBody FiscalEventGetRequest body) {
        return new ResponseEntity<FiscalEventResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
