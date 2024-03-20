package org.digit.exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeServer {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @Size(min = 2, max = 64)
    private String name;

    @JsonProperty("description")
    @Size(min = 2, max = 256)
    private String description;

    @JsonProperty("host_url")
    @Size(min = 2, max = 256)
    private String hostUrl;

    @JsonProperty("public_key")
    private String publicKey;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

}
