package org.egov.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountNumberGpMappingVO {
    private String accountNumber;
    private String departmentEntityName;
    private String departmentEntityCode;
}
