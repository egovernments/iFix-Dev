package org.egov.common.contract.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Error {

    private String code = null;
    private String message = null;
    private String description = null;

}
