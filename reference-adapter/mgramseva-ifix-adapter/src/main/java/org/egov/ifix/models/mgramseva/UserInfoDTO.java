package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("emailId")
    private String emailId;
    @JsonProperty("locale")
    private String locale;
    @JsonProperty("type")
    private String type;
    @JsonProperty("roles")
    private List<RoleDTO> roles = null;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("permanentCity")
    private String permanentCity;
}
