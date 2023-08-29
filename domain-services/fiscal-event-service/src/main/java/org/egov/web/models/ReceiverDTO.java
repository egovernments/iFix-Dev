package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReceiverDTO {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("fiscalEventId")
    private String fiscalEventId = null;

    @JsonProperty("receiver")
    private String receiver = null;

}
