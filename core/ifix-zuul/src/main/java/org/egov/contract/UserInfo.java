package org.egov.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 3446500028655161135L;

    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("roles")
    private List<String> roles = null;

    @JsonProperty("tenants")
    private List<String> tenants = null;

    @JsonProperty("attributes")
    private Object attributes = null;

}
