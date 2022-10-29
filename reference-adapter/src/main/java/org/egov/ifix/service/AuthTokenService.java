package org.egov.ifix.service;

import java.util.concurrent.TimeUnit;

import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.models.KeyCloackData;
import org.egov.ifix.repository.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author mani
 * This will provide authtoken. First time it will ask repository for authtoken .
 * Once token received from repository it will cache it . Cache expiry is set to 5 minutes before the
 * token expiry. 
 */
@Service
@Slf4j
public class AuthTokenService {
	
	private static final String AUTHTOKEN = "authToken";

	@Autowired
	private AuthTokenRepository authTokenRepository;
	
	@Autowired
	private AdapterCache<KeyCloackData> adapterCache; 
	
	private static final  Long TIMEBEFOREEXPIRY=5l*60*1000;
	
	
	/**
	 * 
	 * @return authtoken
	 * First search in cache . if found return else ask repository to provide . 
	 * Cache in redis with expiry time . 
	 */
	
	public String getAuthToken()
	{
		
		KeyCloackData data = adapterCache.getValue(AUTHTOKEN);
		if(data==null)
		{
		 data = authTokenRepository.getAuthToken();
		 if(data.getExpires_in()>TIMEBEFOREEXPIRY)		 
			 adapterCache.putValueWithExpireTime(AUTHTOKEN, data,data.getExpires_in()-TIMEBEFOREEXPIRY,TimeUnit.MILLISECONDS);
		 else
			 adapterCache.putValueWithExpireTime(AUTHTOKEN, data,data.getExpires_in(),TimeUnit.MILLISECONDS);	 
		} else
		{
			log.info("got token from Cache",data.getAccess_token());
		}
		
		return data.getAccess_token();
		
		

	
	}

}
