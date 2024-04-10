package org.digit.program.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.Action;
import org.digit.program.constants.AllocationType;
import org.digit.program.constants.MessageType;
import org.digit.program.models.Migration.*;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.Status;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.repository.ServiceRequestRepository;
import org.digit.program.utils.MdmsUtil;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MigrationService {
    private final ProgramConfiguration programConfiguration;
    private final ObjectMapper objectMapper;
    private final ServiceRequestRepository serviceRequestRepository;
    private final MdmsUtil mdmsUtil;

    @Autowired
    public MigrationService(ProgramConfiguration programConfiguration, ObjectMapper objectMapper,
                            ServiceRequestRepository serviceRequestRepository, MdmsUtil mdmsUtil) {
        this.programConfiguration = programConfiguration;
        this.objectMapper = objectMapper;
        this.serviceRequestRepository = serviceRequestRepository;
        this.mdmsUtil = mdmsUtil;
    }

    public void createProgram() {
        log.info("Started Creating Program For Each Tenant");
        String stateLevelTenantId = "pg";
        JsonNode response = getTenantIds(stateLevelTenantId);
        List<String> tenantIds = getChildTenantIds(response,stateLevelTenantId);
        String signature = "Signature:  namespace=\\\"g2p\\\", kidId=\\\"{sender_id}|{unique_key_id}|{algorithm}\\\", algorithm=\\\"ed25519\\\", created=\\\"1606970629\\\", expires=\\\"1607030629\\\", headers=\\\"(created) (expires) digest\\\", signature=\\\"Base64(signing content)";
        RequestHeader requestHeader = RequestHeader.builder()
                .messageId("123")
                .messageTs(System.currentTimeMillis())
                .action(Action.CREATE)
                .messageType(MessageType.PROGRAM)
                .senderId("program@https://unified-dev.digit.org/ifms/digit-exchange")
                .senderUri("https://spp.example.org/{namespace}/callback/on-search")
                .receiverId("program@https://unified-dev.digit.org/mukta/digit-exchange")
                .isMsgEncrypted(false)
                .build();
        StringBuilder programCreateUrl = new StringBuilder();
        programCreateUrl.append(programConfiguration.getProgramServiceHost())
                .append(programConfiguration.getProgramServiceCreatePath());
        ProgramRequest stateLevelProgram = createStateLevelProgram(stateLevelTenantId, signature, requestHeader,
                programCreateUrl);
        Program program = new Program();
        for (String tenantId : tenantIds) {
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
            log.info("Program Created Successfully: " + programResponse);
        }
    }

    private ProgramRequest createStateLevelProgram(String stateLevelTenantId, String signature,
            RequestHeader requestHeader, StringBuilder programCreateUrl) {
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
        log.info("State Level Program Created Successfully: " + programResponse);
        return objectMapper.convertValue(programResponse, ProgramRequest.class);
    }

    private JsonNode getTenantIds(String stateLevelTenantId) {
        Object response = mdmsUtil.fetchTenants(stateLevelTenantId);
        return objectMapper.convertValue(response, JsonNode.class);
    }

    private List<String> getChildTenantIds(JsonNode response,String stateLevelTenantId) {
        List<String> tenantIds = new ArrayList<>();
        log.info("Filtering and extracting codes from the response data.");

        JsonNode tenants = response.path("MdmsRes").path("tenant").path("tenants");
        if (tenants.isArray()) {
            for (JsonNode tenant : tenants) {
                if(tenant.path("code").asText().equals(stateLevelTenantId))
                    continue;
                tenantIds.add(tenant.path("code").asText());
            }
        }
        log.info("TenantIds Fetched Successfully: " + tenantIds);
        return tenantIds;
    }

    public void createSanctionAllocation(RequestInfo requestInfo) {
        log.info("Started Creating Sanction Allocation For Each Tenant");
        String stateLevelTenantId = "pg";
        JsonNode response = getTenantIds(stateLevelTenantId);
        List<String> tenantIds = getChildTenantIds(response,stateLevelTenantId);
        for (String tenantId : tenantIds) {
            callOnSanctionAllocation(tenantId, requestInfo);
        }
    }

    private void callOnSanctionAllocation(String tenantId, RequestInfo requestInfo) {
        log.info("Creating Sanction Allocation For Tenant: " + tenantId);
        FundsSearchRequest fundsSearchRequest = FundsSearchRequest.builder()
                .requestInfo(requestInfo)
                .searchCriteria(SanctionDetailsSearchCriteria.builder().tenantId(tenantId).build())
                .build();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(programConfiguration.getIfmsServiceHost())
                .append(programConfiguration.getIfmsServiceFundsSearchPath());
        Object response = serviceRequestRepository.fetchResult(stringBuilder, fundsSearchRequest);
        FundsSearchResponse fundsSearchResponse = objectMapper.convertValue(response, FundsSearchResponse.class);
        callOnSanctionAllocationForFunds(fundsSearchResponse, tenantId);
    }

    private void callOnSanctionAllocationForFunds(FundsSearchResponse fundsSearchResponse, String tenantId) {
        log.info("Calling On Sanction Allocation For Tenant: " + tenantId);
        List<SanctionDetail> sanctionDetails = fundsSearchResponse.getFunds();
        if (sanctionDetails.isEmpty()) {
            log.info("No Sanction Details Found For Tenant: " + tenantId);
            return;
        }
        List<Sanction> sanctionList = new ArrayList<>();
        List<Allocation> allocationList = new ArrayList<>();
        String sanctionId = UUID.randomUUID().toString();
        Double totalSanctionedAmount = 0.0;
        Double totalAvailableAmount = 0.0;
        for (SanctionDetail sanctionDetail : sanctionDetails) {
            Sanction sanction = new Sanction();
            sanction.setId(sanctionDetail.getId());
            sanction.setLocationCode(sanctionDetail.getTenantId());
            sanction.setProgramCode(sanctionDetail.getProgramCode());
            sanction.setNetAmount(sanctionDetail.getSanctionedAmount().doubleValue());
            sanction.setGrossAmount(sanctionDetail.getSanctionedAmount().doubleValue());
            sanction.setAvailableAmount(sanctionDetail.getFundsSummary().getAvailableAmount().doubleValue());
            sanction.setAllocatedAmount((double) 0);
            sanction.setStatus(Status.builder().statusCode(org.digit.program.constants.Status.INITIATED).statusMessage("INITIATED").build());
            sanctionList.add(sanction);
            totalSanctionedAmount += sanctionDetail.getSanctionedAmount().doubleValue();
            totalAvailableAmount += sanctionDetail.getFundsSummary().getAvailableAmount().doubleValue();
            for (Allotment allotment : sanctionDetail.getAllotmentDetails()) {
                Allocation allocation = new Allocation();
                allocation.setId(allotment.getId());
                allocation.setSanctionId(allotment.getSanctionId());
                allocation.setGrossAmount(allotment.getDecimalAllottedAmount().doubleValue());
                allocation.setNetAmount(allotment.getDecimalAllottedAmount().doubleValue());
                allocation.setLocationCode(allotment.getTenantId());
                allocation.setAuditDetails(allotment.getAuditDetails());
                allocation.setProgramCode(sanctionDetail.getProgramCode());
                allocation.setStatus(Status.builder().statusCode(org.digit.program.constants.Status.INITIATED).build());
                if (allotment.getAllotmentTxnType().equals("Allotment withdrawal")) {
                    allocation.setAllocationType(AllocationType.DEDUCTION);
                } else {
                    allocation.setAllocationType(AllocationType.ALLOCATION);
                }
                allocationList.add(allocation);
            }
        }
        Sanction sanction = new Sanction();
        sanction.setId(sanctionId);
        sanction.setChildren(sanctionList);
        sanction.setNetAmount(totalSanctionedAmount);
        sanction.setGrossAmount(totalSanctionedAmount);
        sanction.setType(MessageType.ON_SANCTION);
        sanction.setAvailableAmount(totalAvailableAmount);
        sanction.setProgramCode(sanctionList.get(0).getProgramCode());
        sanction.setLocationCode(tenantId);
        sanction.setStatus(Status.builder().statusCode(org.digit.program.constants.Status.INITIATED).statusMessage("INITIATED").build());
        String signature = "Signature:  namespace=\\\"g2p\\\", kidId=\\\"{sender_id}|{unique_key_id}|{algorithm}\\\", algorithm=\\\"ed25519\\\", created=\\\"1606970629\\\", expires=\\\"1607030629\\\", headers=\\\"(created) (expires) digest\\\", signature=\\\"Base64(signing content)";
        RequestHeader requestHeader = RequestHeader.builder()
                .messageId("123")
                .messageTs(System.currentTimeMillis())
                .action(Action.CREATE)
                .messageType(MessageType.ON_SANCTION)
                .senderId("program@https://unified-dev.digit.org/ifms/digit-exchange")
                .senderUri("https://spp.example.org/{namespace}/callback/on-search")
                .receiverId("program@https://unified-dev.digit.org/mukta/digit-exchange")
                .isMsgEncrypted(false)
                .build();

        SanctionRequest sanctionRequest = SanctionRequest.builder()
                .signature(signature)
                .header(requestHeader)
                .sanction(sanction)
                .build();

        StringBuilder sanctionCreateUrl = new StringBuilder();
        sanctionCreateUrl.append(programConfiguration.getProgramServiceHost())
                .append(programConfiguration.getProgramServiceOnSanctionEndpoint());
        Object sanctionResponse = serviceRequestRepository.fetchResult(sanctionCreateUrl, sanctionRequest);
        log.info("Sanction Created Successfully: " + sanctionResponse);
        if (sanctionResponse != null) {
            Allocation allocation = new Allocation();
            allocation.setChildren(allocationList);
            allocation.setNetAmount(totalSanctionedAmount);
            allocation.setGrossAmount(totalSanctionedAmount);
            allocation.setType(MessageType.ON_ALLOCATION);
            requestHeader.setMessageType(MessageType.ON_ALLOCATION);
            allocation.setAllocationType(AllocationType.ALLOCATION);
            allocation.setLocationCode(tenantId);
            allocation.setSanctionId(sanctionId);
            allocation.setProgramCode(sanctionList.get(0).getProgramCode());
            StringBuilder allocationCreateUrl = new StringBuilder();
            allocationCreateUrl.append(programConfiguration.getProgramServiceHost())
                    .append(programConfiguration.getProgramServiceOnAllocationEndpoint());
            AllocationRequest allocationRequest = AllocationRequest.builder()
                    .signature(signature)
                    .header(requestHeader)
                    .allocation(allocation)
                    .build();
            Object allocationResponse = serviceRequestRepository.fetchResult(allocationCreateUrl, allocationRequest);
            log.info("Allocation Created Successfully: " + allocationResponse);
        }
    }
}
