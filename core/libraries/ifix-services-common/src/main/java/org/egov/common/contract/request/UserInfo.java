package org.egov.common.contract.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserInfo {

    private String uuid = null;
    private List<String> roles = null;
    private List<String> tenants = null;
    private Object attributes = null;

}
