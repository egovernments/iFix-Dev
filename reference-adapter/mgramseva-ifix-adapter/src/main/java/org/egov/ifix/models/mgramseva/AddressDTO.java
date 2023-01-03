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
public class AddressDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("doorNo")
    private String doorNo;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("addressId")
    private String addressId;
    @JsonProperty("addressNumber")
    private String addressNumber;
    @JsonProperty("type")
    private String type;
    @JsonProperty("addressLine1")
    private String addressLine1;
    @JsonProperty("addressLine2")
    private String addressLine2;
    @JsonProperty("landmark")
    private String landmark;
    @JsonProperty("city")
    private String city;
    @JsonProperty("pincode")
    private String pincode;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("buildingName")
    private String buildingName;
    @JsonProperty("street")
    private String street;
    @JsonProperty("locality")
    private LocalityDTO locality;
    @JsonProperty("plotNo")
    private String plotNo;
    @JsonProperty("district")
    private String district;
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private String country;
    @JsonProperty("region")
    private String region;

}
