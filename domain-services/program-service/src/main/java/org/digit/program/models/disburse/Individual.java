package org.digit.program.models.disburse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonProperty("pin")
    private String pin;

    @JsonProperty("address")
    @NotNull
    @Size(min = 2)
    private String address;

}
