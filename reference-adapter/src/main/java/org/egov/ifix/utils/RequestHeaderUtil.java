package org.egov.ifix.utils;

import java.time.Instant;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class RequestHeaderUtil {
	
	private static final String REQUEST_HEADER = "requestHeader";
	
	private static final String MSG_ID = "msgId";

	private static final String VERSION = "version";
	
	private static final String CORELATIONID = "version";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	
	public  RequestHeader polulateRequestHeader(JsonObject jsonObject, RequestHeader header) {
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
			/*if(header.getUserInfo()==null && jsonObject.get("event")!=null  && 
			   jsonObject.get("event").getAsJsonObject().get("entity")!=null && 
			   jsonObject.get("event").getAsJsonObject().get("entity").getAsJsonArray().get(0).getAsJsonObject().get("RequestInfo").getAsJsonObject().get("userInfo") !=null  )
			{
				
				UserInfo userInfo=null;
				JsonObject user=jsonObject.get("event").getAsJsonObject().get("entity").getAsJsonArray().get(0).getAsJsonObject().get("RequestInfo").getAsJsonObject().get("userInfo").getAsJsonObject();
				try {
					userInfo=	objectMapper.readValue(user.getAsString(),UserInfo.class);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				header.setUserInfo(userInfo);
			}*/
			
			UserInfo user=new UserInfo();
			user.setUuid("111111");
			header.setUserInfo(user);
			 
		}
		
		return header;
	}

}
