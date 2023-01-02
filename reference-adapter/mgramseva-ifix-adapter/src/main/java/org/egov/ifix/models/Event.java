package org.egov.ifix.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Captures the finacial event attributes
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-06T14:55:47.021Z[GMT]")

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event   {
  @JsonProperty("id")
  private String id;

  @NotEmpty
  @JsonProperty("tenantId")
  private String tenantId;
  
  @NotEmpty
  @JsonProperty("projectId")
  private String projectId;

  @NotNull
  @JsonProperty("eventType")
  private EventTypeEnum eventType;

  @NotNull
  @JsonProperty("entity")
  @Valid
  @NotEmpty
  private List<Object> entity;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails;

  
}
