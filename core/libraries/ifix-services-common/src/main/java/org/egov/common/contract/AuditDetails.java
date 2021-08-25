package org.egov.common.contract;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AuditDetails {

    private String createdBy = null;
    private String lastModifiedBy = null;
    private Long createdTime = null;
    private Long lastModifiedTime = null;

}
