package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * ChartOfAccount
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartOfAccount {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("coaCode")
    private String coaCode = null;

    @JsonProperty("majorHead")
    private String majorHead = null;

    @JsonProperty("majorHeadName")
    private String majorHeadName = null;

    @JsonProperty("majorHeadType")
    private String majorHeadType = null;

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


}

