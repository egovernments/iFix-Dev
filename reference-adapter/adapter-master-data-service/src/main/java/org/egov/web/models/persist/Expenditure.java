package org.egov.web.models.persist;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Expenditure {
    private String id = null;

    private String tenantId = null;

    private String code = null;

    private String name = null;

    private String type = null;

    private String departmentId = null;

    private String createdBy = null;
    private String lastModifiedBy = null;
    private Long createdTime = null;
    private Long lastModifiedTime = null;
}

