package org.digit.exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeServerSearch {
    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("name")
    private String name;

    @JsonProperty("host_url")
    private String hostUrl;

    @JsonProperty("is_active")
    private Boolean isActive;

}
