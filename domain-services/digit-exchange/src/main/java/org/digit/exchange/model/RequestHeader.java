package org.digit.exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.digit.exchange.constants.Action;
import org.digit.exchange.constants.MessageType;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeader {
    @NotNull
    @JsonProperty("message_id")
    @NotNull
    private String messageId;
    @JsonProperty("message_ts")
    @NotNull
    private ZonedDateTime messageTs;
    @JsonProperty("message_type")
    @NotNull
    private MessageType messageType;
    @JsonProperty("action")
    @NotNull
    private Action action;
    @JsonProperty("sender_id")
    @NotNull
    private String senderId;
    @JsonProperty("senderUri")
    private String senderUri;
    @NotNull
    @JsonProperty("receiver_id")
    private String receiverId;

}
