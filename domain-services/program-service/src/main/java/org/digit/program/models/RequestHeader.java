package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

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
//     // @OneToOne(cascade = CascadeType.ALL)
//     @JsonProperty("meta")
//     @Embedded
//     private ExchangeMessage exchangeMessage;

//    public RequestHeader(){
//        UUID uuid = UUID.randomUUID();
//        this.id = uuid.toString();
//        this.version = "1.0.0";
//    }

//    public RequestHeader(String to, String from, ExchangeCode message, MessageType messageType){
//        this.senderId = from;
//        this.receiverId = to;
//        this.messageType = messageType;
//        // this.exchangeMessage = message;
//        //Set MessageID
//        UUID uuid = UUID.randomUUID();
//        this.messageId = uuid.toString();
//        //Set Timestamp
//        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
//        ZonedDateTime now = LocalDateTime.now().atZone(zoneId);
//        this.messageTs = now;
//
//    }

//    static public RequestHeader fromString(String json){
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        try {
//            return mapper.readValue(json, RequestHeader.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error parsing RequestHeader fromString", e);
//        }
//    }
}
