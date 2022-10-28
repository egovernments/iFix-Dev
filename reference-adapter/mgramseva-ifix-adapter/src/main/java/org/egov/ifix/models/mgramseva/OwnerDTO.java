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
public class OwnerDTO {
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("fatherOrHusbandName")
    private String fatherOrHusbandName;
    @JsonProperty("relationship")
    private String relationship;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("dob")
    private Long dob;
    @JsonProperty("emailId")
    private String emailId;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("roles")
    private List<RoleDTO> roles;
}
