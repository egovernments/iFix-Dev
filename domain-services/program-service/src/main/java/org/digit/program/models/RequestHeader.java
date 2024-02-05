package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.digit.program.constants.Action;
import org.digit.program.constants.MessageType;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestHeader{
    private String id;
    @NotNull
    @JsonProperty("version")
    private String version;
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
    private Action action;
    @JsonProperty("sender_id")
    @NotNull
    private String senderId;
    @JsonProperty("senderUri")
    @NotNull
    private String senderUri;
    @NotNull
    @JsonProperty("receiver_id")
    private String receiverId;
    @JsonProperty("total_count")
    private int totalCount;
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
