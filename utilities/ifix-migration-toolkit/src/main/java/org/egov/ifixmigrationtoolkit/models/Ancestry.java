package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ancestry {

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("type")
    private String type = null;

}
