package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCode {


    @JsonProperty("id")
    private String id;

    @JsonProperty("function_code")
    @Size(min = 2, max = 64)
    private String functionCode;

    @JsonProperty("administration_code")
    @Size(min = 2, max = 64)
    private String administrationCode;

    @JsonProperty("recipient_segment_code")
    @Size(min = 2, max = 64)
    private String recipientSegmentCode;

    @JsonProperty("economic_segment_code")
    @Size(min = 2, max = 64)
    private String economicSegmentCode;

    @JsonProperty("source_of_fund_code")
    @Size(min = 2, max = 64)
    private String sourceOfFundCode;

    @JsonProperty("target_segment_code")
    @Size(min = 2, max = 64)
    private String targetSegmentCode;

    @JsonProperty("currency_code")
    @Size(min = 2, max = 64)
    private String currencyCode;

    @JsonProperty("locale_code")
    @Size(min = 2, max = 64)
    private String localeCode;

    @JsonProperty("status")
    private Status status;

//    public ExchangeMessage() {
//        // Initialize any default values if needed
//    }


//    public ExchangeMessage(){
//        UUID uuid = UUID.randomUUID();
//        this.id = uuid.toString();
//        this.schemaVersion = "1.0.0";
//    }

}
