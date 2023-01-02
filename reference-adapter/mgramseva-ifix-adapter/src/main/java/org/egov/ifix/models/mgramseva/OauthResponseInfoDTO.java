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
public class OauthResponseInfoDTO {
    @JsonProperty("api_id")
    private String apiId;
    @JsonProperty("ver")
    private String ver;
    @JsonProperty("ts")
    private String ts;
    @JsonProperty("res_msg_id")
    private String resMsgId;
    @JsonProperty("msg_id")
    private String msgId;
    @JsonProperty("status")
    private String status;
}
