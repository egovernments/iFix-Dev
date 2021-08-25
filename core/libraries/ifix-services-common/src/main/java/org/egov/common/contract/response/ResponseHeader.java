package org.egov.common.contract.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseHeader {

    private Long ts = null;
    private String correlationId = null;
    private String msgId = null;
    private String status = null;
    private String signature = null;
    private String version = null;

}
