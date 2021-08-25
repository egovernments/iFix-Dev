package org.egov.common.contract.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ErrorResponse {

    private ResponseHeader responseHeader = null;
    private List<Error> errors = null;

}
