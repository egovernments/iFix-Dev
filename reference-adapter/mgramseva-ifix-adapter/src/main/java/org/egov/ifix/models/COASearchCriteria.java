package org.egov.ifix.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author mani
 * As of now we are searching with coaCode 
 * when you want search with other attributes copy from ifix core
 *
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class COASearchCriteria   {
		
	    @JsonProperty("Ids")
        private List<String> ids = null;
	    
		private String coaCode = null;
	    
		private String tenantId = null;
}
