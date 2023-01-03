package org.egov.ifix.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MasterDataMappingUtil {
	
	private String headCodeToCoaMappingMasterName = "headCodeToCoaMapping";

	private String srcSysServiceCode = "srcSysServiceCode";

	private String srcSysTaxHeadCode = "srcSysTaxHeadCode";

	private String iFixCoaId = "iFixCoaId";
	
	private String projectMappingMasterName = "projectMapping";

	private String srcSysProjectId = "srcSysProjectId";

	private String iFixProjectId = "iFixProjectId";

	Map<String, List<Map<String, String>>> masterMappingMap = null;
	
	@Value("${event.config.path}")
	private String configFilePath;

	@Autowired
	private ResourceLoader resourceLoader;

	@PostConstruct
	private void readConfig() throws IOException {

		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		Resource resource = resourceLoader.getResource(configFilePath);

		masterMappingMap = objectMapper.readValue(resource.getInputStream(), Map.class);
	}

	
	public String getCoaId(String servicecode) {
		List<Map<String, String>> headCodeMappings = masterMappingMap.get(headCodeToCoaMappingMasterName);
		for(Map<String, String> headCodeMapping : headCodeMappings) {
			log.info("servicecode" +servicecode );
			if(headCodeMapping.get(srcSysServiceCode).equals(servicecode)) {
				log.info("headCodeMapping.get(iFixCoaId)" +headCodeMapping.get(iFixCoaId) );
				return headCodeMapping.get(iFixCoaId);
			}
			
		}
		return null;	
	}
	
	public String getProjectId(String projectId) {
		List<Map<String, String>> projectMappings = masterMappingMap.get("projectMapping");
		for(Map<String, String> projectMapping : projectMappings) {
			log.info("projectMapping.get(srcSysProjectId)...."+projectMapping.get(srcSysProjectId));
			log.info("projectMapping.get(iFixProjectId)...."+projectMapping.get(iFixProjectId));
			if(projectMapping.get(srcSysProjectId).equals(projectId)) {
				return projectMapping.get(iFixProjectId);
			}
		}
		return null;	
	}

}