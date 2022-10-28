package org.egov.ifix.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

import static org.egov.ifix.utils.EventConstants.*;

@Component
public class RequestHeaderUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public RequestHeader populateRequestHeader(JsonObject jsonObject, RequestHeader header) {
        if (jsonObject.get(REQUEST_HEADER) != null) {
            JsonElement requestHeader = jsonObject.get(REQUEST_HEADER);
            JsonObject requestJsonObject = requestHeader.getAsJsonObject();
            if (requestJsonObject.get(VERSION) != null) {
                header.setVersion(requestJsonObject.get(VERSION).getAsString());
            }
            if (requestJsonObject.get(MSG_ID) != null) {
                header.setMsgId(requestJsonObject.get(MSG_ID).getAsString());
            }
            header.setTs(Instant.now().toEpochMilli());

            UserInfo user = new UserInfo();
            user.setUuid(UUID.randomUUID().toString());
            header.setUserInfo(user);

        }

        return header;
    }

}
