package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfoDTO {
    @JsonProperty("apiId")
    private String apiId;
    @JsonProperty("ver")
    private Integer ver;
    @JsonProperty("ts")
    private String ts;
    @JsonProperty("action")
    private String action;
    @JsonProperty("did")
    private Integer did;
    @JsonProperty("key")
    private String key;
    @JsonProperty("msgId")
    private String msgId;
    @JsonProperty("authToken")
    private String authToken;
}
