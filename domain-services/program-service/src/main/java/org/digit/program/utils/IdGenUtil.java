package org.digit.program.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.repository.ServiceRequestRepository;
import org.egov.common.contract.idgen.IdGenerationRequest;
import org.egov.common.contract.idgen.IdGenerationResponse;
import org.egov.common.contract.idgen.IdRequest;
import org.egov.common.contract.idgen.IdResponse;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.digit.program.constants.ProgramConstants.GENERATE_ENDPOINT;
import static org.digit.program.constants.ProgramConstants.HOST_KEY;

@Component
public class IdGenUtil {
    private ObjectMapper mapper;
    private ServiceRequestRepository restRepo;
    private ProgramConfiguration configs;

    public IdGenUtil(ObjectMapper mapper, ServiceRequestRepository restRepo, ProgramConfiguration configs) {
        this.mapper = mapper;
        this.restRepo = restRepo;
        this.configs = configs;
    }

    public List<String> getIdList(RequestInfo requestInfo, String tenantId, String idName, String idformat, Integer count) {
        List<IdRequest> reqList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            reqList.add(IdRequest.builder().idName(idName).format(idformat).tenantId(tenantId).build());
        }

        IdGenerationRequest request = IdGenerationRequest.builder().idRequests(reqList).requestInfo(requestInfo).build();
        StringBuilder uri = new StringBuilder(configs.getIdgen().get(HOST_KEY)).append(configs.getIdgen().get(GENERATE_ENDPOINT));
        IdGenerationResponse response = mapper.convertValue(restRepo.fetchResult(uri, request), IdGenerationResponse.class);

        List<IdResponse> idResponses = response.getIdResponses();

        if (CollectionUtils.isEmpty(idResponses))
            throw new RuntimeException("IDGEN_ERROR - No ids returned from IdGen Service");

        return idResponses.stream().map(IdResponse::getId).collect(Collectors.toList());
    }
}