package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import org.digit.program.constants.Action;
import org.digit.program.constants.MessageType;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestHeader{
    @JsonProperty("message_id")
    @NotNull
    private String messageId;

    @JsonProperty("message_ts")
    @NotNull
    private long messageTs;

    @JsonProperty("message_type")
    @NotNull
    private MessageType messageType;

    @JsonProperty("action")
    @NotNull(message = "Action must be valid")
    private Action action;

    @JsonProperty("sender_id")
    @NotNull
    private String senderId;

    @JsonProperty("sender_uri")
    private String senderUri;

    @NotNull
    @JsonProperty("receiver_id")
    private String receiverId;

    @JsonProperty("is_msg_encrypted")
    private boolean isMsgEncrypted;

}
