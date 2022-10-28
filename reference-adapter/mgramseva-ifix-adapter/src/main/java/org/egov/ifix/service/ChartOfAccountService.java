package org.egov.ifix.service;

import com.google.gson.JsonObject;
import org.egov.ifix.models.CoaMappingDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ChartOfAccountService {

    String getResolvedChartOfAccount(String clientCoaCode, JsonObject jsonObject);

    Optional<CoaMappingDTO> getMappedCoaIdByClientCoaCode(CoaMappingDTO coaMappingDTO);

    String getResolvedChartOfAccountCode(String clientCoaCode);
}
