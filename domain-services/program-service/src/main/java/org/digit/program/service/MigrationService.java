package org.digit.program.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.Action;
import org.digit.program.constants.MessageType;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MigrationService {
    private final ProgramConfiguration programConfiguration;
    private final ObjectMapper objectMapper;
    private final ServiceRequestRepository serviceRequestRepository;
    @Autowired
    public MigrationService(ProgramConfiguration programConfiguration, ObjectMapper objectMapper, ServiceRequestRepository serviceRequestRepository) {
        this.programConfiguration = programConfiguration;
        this.objectMapper = objectMapper;
        this.serviceRequestRepository = serviceRequestRepository;
    }
    public void createProgram() {
        log.info("Started Creating Program For Each Tenant");
        JsonNode response = getTenantIds();
        List<String> tenantIds = getChildTenantIds(response);
        String stateLevelTenantId = response.path("tenantId").asText();
        String signature = "Signature:  namespace=\\\"g2p\\\", kidId=\\\"{sender_id}|{unique_key_id}|{algorithm}\\\", algorithm=\\\"ed25519\\\", created=\\\"1606970629\\\", expires=\\\"1607030629\\\", headers=\\\"(created) (expires) digest\\\", signature=\\\"Base64(signing content)";
        RequestHeader requestHeader = RequestHeader.builder()
                .messageId("123")
                .messageTs(System.currentTimeMillis())
                .action(Action.CREATE)
                .messageType(MessageType.PROGRAM)
                .senderId("program@https://unified-dev.digit.org")
                .senderUri("https://spp.example.org/{namespace}/callback/on-search")
                .receiverId("program@https://unified-qa.digit.org")
                .isMsgEncrypted(false)
                .build();
        StringBuilder programCreateUrl = new StringBuilder();
        programCreateUrl.append(programConfiguration.getProgramServiceHost()).append(programConfiguration.getProgramServiceCreatePath());
        ProgramRequest stateLevelProgram = createStateLevelProgram(stateLevelTenantId, signature, requestHeader,programCreateUrl);
        Program program = new Program();
        for(String tenantId: tenantIds) {
            program.setName("Mukta");
            program.setLocationCode(tenantId);
            program.setParentId(stateLevelProgram.getProgram().getId());
            program.setStartDate(System.currentTimeMillis());
            ProgramRequest programRequest = ProgramRequest.builder()
                    .signature(signature)
                    .header(requestHeader)
                    .program(program)
                    .build();
            Object programResponse = serviceRequestRepository.fetchResult(programCreateUrl, programRequest);
            log.info("Program Created Successfully: "+ programResponse);
        }
    }

    private ProgramRequest createStateLevelProgram(String stateLevelTenantId, String signature, RequestHeader requestHeader, StringBuilder programCreateUrl) {
        log.info("Creating State Level Program");
        Program program = new Program();
        program.setName("Mukta");
        program.setLocationCode(stateLevelTenantId);
        program.setStartDate(System.currentTimeMillis());
        ProgramRequest programRequest = ProgramRequest.builder()
                .signature(signature)
                .header(requestHeader)
                .program(program)
                .build();
        Object programResponse = serviceRequestRepository.fetchResult(programCreateUrl, programRequest);
        log.info("State Level Program Created Successfully: "+ programResponse);
        return objectMapper.convertValue(programResponse, ProgramRequest.class);
    }

    private JsonNode getTenantIds() {
        log.info("Started Fetching TenantIds From File");
        JsonNode response = null;
        try {
            File tenantIdsFile = new File(programConfiguration.getTenantIdFilePath());
            response = objectMapper.readTree(tenantIdsFile);
        }catch (Exception e) {
            log.error("Exception occurred while fetching tenantIds from file: ", e);
            throw  new CustomException("TENANT_IDS_FILE_NOT_FOUND", "TenantIds file not found");
        }
        return response;
    }
    private List<String> getChildTenantIds(JsonNode response) {
        List<String> tenantIds = new ArrayList<>();
        log.info("Filtering and extracting codes from the response data.");

        JsonNode tenantsArray = response.path("tenants");
        JsonNode stateLevelTenantId = response.path("tenantId");
        for (JsonNode tenant : tenantsArray) {
            String code = tenant.path("code").asText();
            if(!code.equals(stateLevelTenantId.asText())) {
                tenantIds.add(code);
            }
        }
        log.info("TenantIds Fetched Successfully: "+ tenantIds);
        return tenantIds;
    }
}
