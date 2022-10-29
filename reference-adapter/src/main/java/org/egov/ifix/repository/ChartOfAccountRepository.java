package org.egov.ifix.repository;

import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestHeader;
import org.egov.ifix.models.COASearchCriteria;
import org.egov.ifix.models.ChartOfAccount;
import org.egov.ifix.models.ChartOfAccountRequest;
import org.egov.ifix.models.ChartOfAccountResponse;
import org.egov.ifix.persistance.ChartOfAccountMap;
import org.egov.ifix.persistance.ChartOfAccountMapRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author mani
 * This will be resposible for providing right coa from web or db or file repository 
 * As of now client (mgram) coaCode is matching with so direcly making the call . 
 * When it is changed to taxheadcode it should fetch the code from database or file 
 * 
 *
 */
@Repository
@Slf4j
public class ChartOfAccountRepository {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private	ApplicationConfiguration applicationConfiguration;
	
	@Autowired
	private	AuthTokenService authTokenService;
	
	@Autowired
	private RequestHeaderUtil requestHeaderUtil;
	
	@Autowired
	private ChartOfAccountMapRepository chartOfAccountMapRepository;
	
	
	public ChartOfAccount getChartOfAccount(String clientCode,JsonObject jsonObject)
	{
		COASearchCriteria coaCriteria=new COASearchCriteria();
		List<ChartOfAccountMap> coaMaps = chartOfAccountMapRepository.findByClientCode(clientCode);
		
		if(coaMaps.size()>1)
		{
			throw new RuntimeException("Multiple ClientCodes found . Could not find Unique map for cleintCode : " +clientCode);
		}
		else if(coaMaps.size()==1)
		{
			ChartOfAccountMap coaMap=coaMaps.get(0);
			if(coaMap.getIFixCoaCode()!=null)
			{
				coaCriteria.setCoaCode(coaMap.getIFixCoaCode());
			}else
			{
				List<String> ids = Arrays.asList(new String[]{coaMap.getIFixId()});
				coaCriteria.setIds(ids);
			}
			
					
			
		}else
		{
			throw new  RuntimeException("COA mapping not  found  for  cleintCode : " +clientCode);
		}
		
		log.debug("getting COA from ifix .........");
		
		String url = applicationConfiguration.getIfixHost()
				+ applicationConfiguration.getCoaSearchApi();

		log.info("Posting to IFix ");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authTokenService.getAuthToken());
		
		 
		RequestHeader requestHeader=new RequestHeader();
		
		requestHeader=	requestHeaderUtil.polulateRequestHeader(jsonObject, requestHeader);
		
		
		coaCriteria.setTenantId(applicationConfiguration.getTenantId());
		
		ChartOfAccountRequest chartOfAccountRequest=new ChartOfAccountRequest();
		
		chartOfAccountRequest.setRequestHeader(requestHeader);
		chartOfAccountRequest.setCOASearchCriteria(coaCriteria);
		
		 
		
		 
		// ResponseEntity<GenericResponse<ResponseHeader, ChartOfAccount>> response =null; 
		 ResponseEntity<ChartOfAccountResponse> response = restTemplate.postForEntity(url, chartOfAccountRequest, ChartOfAccountResponse.class );
	
		log.info("Posting to IFix status" +response.getStatusCode());
		 List<ChartOfAccount> coaList = response.getBody().getChartOfAccounts();
		 if(coaList.size()>1)
		 {
			
			log.error("Duplicate COAs found in IFIX  for the mapped clientcode "+clientCode);
			 
		 }else if(coaList.size()==1)
		 {
			 return coaList.get(0);
			 
		 }else
		 {
			 log.error("COAs not  found in IFIX  for the mapped clientcode "+clientCode); 
		 }
		 

		return null;
	}

}
