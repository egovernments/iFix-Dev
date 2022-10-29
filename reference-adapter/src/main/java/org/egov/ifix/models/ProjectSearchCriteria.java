package org.egov.ifix.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author 
 * mani As of now we are searching with code when you want search with
 *         other attributes copy from ifix core
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSearchCriteria {
	@JsonProperty("Ids")
	private List<String> ids = null;

	private String tenantId = null;

	private String name = null;

	private String code = null;
}