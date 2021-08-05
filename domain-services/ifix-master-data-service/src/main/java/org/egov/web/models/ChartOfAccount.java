package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

/**
 * Captures the COA data as map
 */
@ApiModel(description = "Captures the COA data as map")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ChartOfAccount {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("coaCode")
    private String coaCode = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("majorHead")
    private String majorHead = null;

    @JsonProperty("majorHeadName")
    private String majorHeadName = null;

    @JsonProperty("majorHeadtype")
    private String majorHeadtype = null;

    @JsonProperty("subMajorHead")
    private String subMajorHead = null;

    @JsonProperty("subMajorHeadName")
    private String subMajorHeadName = null;

    @JsonProperty("minorHead")
    private String minorHead = null;

    @JsonProperty("minorHeadName")
    private String minorHeadName = null;

    @JsonProperty("subHead")
    private String subHead = null;

    @JsonProperty("subHeadName")
    private String subHeadName = null;

    @JsonProperty("groupHead")
    private String groupHead = null;

    @JsonProperty("groupHeadName")
    private String groupHeadName = null;

    @JsonProperty("objectHead")
    private String objectHead = null;

    @JsonProperty("objectHeadName")
    private String objectHeadName = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;


}

