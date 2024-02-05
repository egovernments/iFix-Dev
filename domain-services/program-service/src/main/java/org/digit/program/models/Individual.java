package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.user.enums.Gender;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class Individual {

    @JsonProperty("name")
    @NotNull
    @Size(min = 2)
    private String name;


    @JsonProperty("email")
    @Size(min = 2)
    private String email;

    @JsonProperty("phone")
    @NotNull
    @Size(min = 2)
    private String phone;

    @JsonProperty("gender")
    @Size(min = 2)
    private Gender gender;

    @JsonProperty("pin")
    private String pin;

    @JsonProperty("address")
    @NotNull
    @Size(min = 2)
    private String address;

}
