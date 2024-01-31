package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.digit.program.constants.Error.INVALID_ID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(builderClassName = "ExchangeCodeBuilder")
public class ExchangeCode {


    @JsonProperty("id")
    @NotBlank(message = INVALID_ID)
    private String id;

    @JsonProperty("function_code")
    @Size(min = 2, max = 128)
    private String functionCode;

    @JsonProperty("administration_code")
    @Size(min = 2, max = 128)
    private String administrationCode;

    @JsonProperty("recipient_segment_code")
    private String recipientSegmentCode;

    @JsonProperty("economic_segment_code")
    private String economicSegmentCode;

    @JsonProperty("source_of_fund_code")
    private String sourceOfFundCode;

    @JsonProperty("target_segment_code")
    private String targetSegmentCode;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("locale_code")
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
