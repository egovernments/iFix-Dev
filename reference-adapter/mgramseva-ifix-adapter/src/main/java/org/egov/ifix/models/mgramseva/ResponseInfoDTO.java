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
public class ResponseInfoDTO {
    @JsonProperty("apiId")
    private String apiId;
    @JsonProperty("ver")
    private String ver;
    @JsonProperty("ts")
    private Object ts;
    @JsonProperty("resMsgId")
    private String resMsgId;
    @JsonProperty("msgId")
    private String msgId;
    @JsonProperty("status")
    private String status;
}
