package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.AuditDetails;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private AddressDTO address;
    @JsonProperty("owner")
    private OwnerDTO owner;
    @JsonProperty("vehicles")
    private List<Object> vehicles = null;
    @JsonProperty("drivers")
    private List<Object> drivers = null;
    @JsonProperty("source")
    private String source;
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
}
