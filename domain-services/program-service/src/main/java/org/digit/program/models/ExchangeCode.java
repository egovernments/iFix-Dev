package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.constants.MessageType;

import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCode {

    @JsonProperty("id")
    @Size(min = 2, max = 64)
    private String id;

    @JsonProperty("type")
    private MessageType type;

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

}
