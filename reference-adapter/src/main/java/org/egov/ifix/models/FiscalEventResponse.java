package org.egov.ifix.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseHeader;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains the ResponseHeader and the enriched fiscal information
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:28:42.515+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventResponse {
    @JsonProperty("responseInfo")
    private ResponseHeader responseInfo = null;

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
