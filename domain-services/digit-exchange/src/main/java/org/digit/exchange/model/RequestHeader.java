package org.digit.exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.exchange.constants.Action;
import org.digit.exchange.constants.ExchangeType;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeader {
    @JsonProperty("message_id")
    @NotNull
    private String messageId;

    @JsonProperty("message_ts")
    @NotNull
    private long messageTs;

    @JsonProperty("message_type")
    @NotNull
    private ExchangeType messageType;

    @JsonProperty("action")
    @NotNull
    private Action action;

    @JsonProperty("sender_id")
    @NotNull
    private String senderId;

    @JsonProperty("sender_uri")
    private String senderUri;

    @JsonProperty("receiver_id")
    @NotNull
    private String receiverId;

    @JsonProperty("is_msg_encrypted")
    private boolean isMsgEncrypted;

}
