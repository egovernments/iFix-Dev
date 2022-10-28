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
public class CitizenResponseDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private Object password;
    @JsonProperty("salutation")
    private Object salutation;
    @JsonProperty("name")
    private String name;
    @JsonProperty("gender")
    private Object gender;
    @JsonProperty("mobileNumber")
    private Object mobileNumber;
    @JsonProperty("emailId")
    private Object emailId;
    @JsonProperty("altContactNumber")
    private Object altContactNumber;
    @JsonProperty("pan")
    private Object pan;
    @JsonProperty("aadhaarNumber")
    private Object aadhaarNumber;
    @JsonProperty("permanentAddress")
    private Object permanentAddress;
    @JsonProperty("permanentCity")
    private Object permanentCity;
    @JsonProperty("permanentPinCode")
    private Object permanentPinCode;
    @JsonProperty("correspondenceCity")
    private Object correspondenceCity;
    @JsonProperty("correspondencePinCode")
    private Object correspondencePinCode;
    @JsonProperty("correspondenceAddress")
    private Object correspondenceAddress;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("dob")
    private Object dob;
    @JsonProperty("pwdExpiryDate")
    private Object pwdExpiryDate;
    @JsonProperty("locale")
    private Object locale;
    @JsonProperty("type")
    private Object type;
    @JsonProperty("signature")
    private Object signature;
    @JsonProperty("accountLocked")
    private Object accountLocked;
    @JsonProperty("roles")
    private Object roles;
    @JsonProperty("fatherOrHusbandName")
    private Object fatherOrHusbandName;
    @JsonProperty("bloodGroup")
    private Object bloodGroup;
    @JsonProperty("identificationMark")
    private Object identificationMark;
    @JsonProperty("photo")
    private Object photo;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("createdDate")
    private Long createdDate;
    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy;
    @JsonProperty("lastModifiedDate")
    private Long lastModifiedDate;
    @JsonProperty("otpReference")
    private Object otpReference;
    @JsonProperty("tenantId")
    private Object tenantId;
    @JsonProperty("idToken")
    private Object idToken;
    @JsonProperty("email")
    private Object email;
    @JsonProperty("primaryrole")
    private List<Object> primaryrole = null;
    @JsonProperty("additionalroles")
    private Object additionalroles;
}
