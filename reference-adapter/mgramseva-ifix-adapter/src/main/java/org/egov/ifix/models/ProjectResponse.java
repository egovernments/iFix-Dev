package org.egov.ifix.models;

import java.util.List;

import org.egov.common.contract.response.ResponseHeader;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponse {
	private ResponseHeader responseHeader;
  
	@JsonProperty("project")
	private List<Project> projects;

}
