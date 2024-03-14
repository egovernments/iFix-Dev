package org.digit.program.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.repository.ServiceRequestRepository;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.digit.program.constants.ProgramConstants.*;

@Component
@Slf4j
public class MdmsUtil {

    private final ProgramConfiguration configs;
    private final ServiceRequestRepository restRepo;
    private final ObjectMapper mapper;

    public MdmsUtil(ProgramConfiguration configs, ServiceRequestRepository restRepo, ObjectMapper mapper) {
        this.configs = configs;
        this.restRepo = restRepo;
        this.mapper = mapper;
    }

    /**
     * Creates mdms request, calls mdms and converts response to mdmsResponse
     * @param tenantId
     * @return
     */
    public MdmsResponse callMdms(String tenantId) {
        MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(RequestInfo.builder().build())
                .mdmsCriteria(MdmsCriteria.builder().tenantId(tenantId)
                        .moduleDetails(Collections.singletonList(ModuleDetail.builder()
                                .masterDetails(Collections.singletonList(MasterDetail.builder()
                                        .name(MDMS_MASTER_NAME)
                                        .filter(FILTER_CODE_START+configs.getUrlCode()+FILTER_CODE_END)
                                        .build()))
                                .moduleName(MDMS_MODULE_NAME)
                                .build()))
                        .build())
                .build();
        Object response = restRepo.fetchResult(new StringBuilder(configs.getMdmsHost()).append(configs.getMdmsPath()), mdmsCriteriaReq);
        return mapper.convertValue(response, MdmsResponse.class);
    }
}
