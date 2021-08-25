package org.egov.common.contract.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RequestHeader {

    private Long ts = null;
    private String version = null;
    private String msgId = null;
    private UserInfo userInfo = null;
    private String correlationId = null;
    private String signature = null;

}
