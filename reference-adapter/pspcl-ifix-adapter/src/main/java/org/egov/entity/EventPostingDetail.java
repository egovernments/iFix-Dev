package org.egov.entity;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPostingDetail {

    private Long id;

    private String tenantId;
    private String ifixEventId;
    private String referenceId;
    private String eventType;
    private String status;
    private String error;
    private String record;

    private Date createdDate;

    private Date lastModifiedDate;
}
