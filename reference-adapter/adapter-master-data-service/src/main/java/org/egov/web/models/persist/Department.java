package org.egov.web.models.persist;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {
    private String id = null;

    private String tenantId = null;

    private String code = null;

    private String name = null;

    private Boolean isNodal = false;

    private String parent = null;

    private String createdBy = null;
    private String lastModifiedBy = null;
    private Long createdTime = null;
    private Long lastModifiedTime = null;
}

