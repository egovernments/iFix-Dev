package org.digit.exchange.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.digit.exchange.constants.ExchangeType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessageWrapper {
    @JsonProperty("type")
    private ExchangeType type;

    @JsonProperty("message")
    private RequestMessage requestMessage;

}
