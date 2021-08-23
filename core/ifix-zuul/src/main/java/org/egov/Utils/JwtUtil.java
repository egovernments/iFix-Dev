package org.egov.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.egov.contract.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public UserInfo createUserInfoFromJwt(String jwtPayload) throws JsonProcessingException {
        JsonNode payload = objectMapper.readTree(jwtPayload);
        UserInfo userInfo = UserInfo.builder().build();

        userInfo.setUuid(payload.get("sub").asText());

        ArrayNode arrayNode = (ArrayNode) payload.get("realm_access").get("roles");
        List<String> roles = Arrays.asList(objectMapper.readValue(arrayNode.toString(), String[].class));
        userInfo.setRoles(roles);

        List<String> tenants = new ArrayList<>();
        tenants.add(payload.get("tenantId").asText());
        userInfo.setTenants(tenants);

        return userInfo;
    }

}
