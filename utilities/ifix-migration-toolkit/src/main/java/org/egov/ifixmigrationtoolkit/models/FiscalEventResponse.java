package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("fiscalEvent")
    @Valid
    private List<FiscalEvent> fiscalEvent = null;


    public FiscalEventResponse addFiscalEventItem(FiscalEvent fiscalEventItem) {
        if (this.fiscalEvent == null) {
            this.fiscalEvent = new ArrayList<>();
        }
        this.fiscalEvent.add(fiscalEventItem);
        return this;
    }

}