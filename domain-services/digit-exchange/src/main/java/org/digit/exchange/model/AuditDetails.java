package org.digit.exchange.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDetails {
    @JsonProperty("created_by")
    private String createdBy = null;
    @JsonProperty("last_modified_by")
    private String lastModifiedBy = null;
    @JsonProperty("created_time")
    private Long createdTime = null;
    @JsonProperty("last_modified_time")
    private Long lastModifiedTime = null;
}
