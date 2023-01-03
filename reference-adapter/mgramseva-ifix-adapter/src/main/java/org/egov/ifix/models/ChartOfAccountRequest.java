package org.egov.ifix.models;

import java.io.Serializable;

import org.egov.common.contract.request.RequestHeader;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChartOfAccountRequest {
	private RequestHeader requestHeader;
	
	@JsonProperty("criteria")
	private COASearchCriteria cOASearchCriteria;

}
