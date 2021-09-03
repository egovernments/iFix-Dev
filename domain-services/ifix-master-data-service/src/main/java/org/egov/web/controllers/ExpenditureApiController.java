package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.service.ExpenditureService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.Expenditure;
import org.egov.web.models.ExpenditureRequest;
import org.egov.web.models.ExpenditureResponse;
import org.egov.web.models.ExpenditureSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Controller
@RequestMapping("/expenditure/v1")
public class ExpenditureApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ExpenditureService expenditureService;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    @Autowired
    public ExpenditureApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<ExpenditureResponse> eatV1CreatePost(@ApiParam(value = "Details for the new expenditure RequestHeader" +
            " (meta data of the API).", required = true) @Valid @RequestBody ExpenditureRequest body) {
        ExpenditureRequest expenditureRequest = expenditureService.createV1Expenditure(body);
        ResponseHeader responseHeader = responseHeaderCreator.createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);
        ExpenditureResponse expenditureResponse = ExpenditureResponse.builder().responseHeader(responseHeader)
                .expenditure(Collections.singletonList(expenditureRequest.getExpenditure())).build();
        return new ResponseEntity<ExpenditureResponse>(expenditureResponse, HttpStatus.OK);
    }

    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "_search", method = RequestMethod.POST)
    public ResponseEntity<ExpenditureResponse> eatV1SearchPost(@ApiParam(value = "Details for the expenditure search criteria " +
            "RequestHeader (meta data of the API).", required = true) @Valid @RequestBody ExpenditureSearchRequest body) {

        List<Expenditure> expenditureList = expenditureService.findAllByCriteria(body);

        ResponseHeader responseHeader = responseHeaderCreator.createResponseHeaderFromRequestHeader(body.getRequestHeader(),
                true);

        ExpenditureResponse eatResponse = ExpenditureResponse.builder().responseHeader(responseHeader).expenditure(expenditureList).build();

        return new ResponseEntity<>(eatResponse, HttpStatus.ACCEPTED);
    }

}
