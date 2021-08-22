package org.egov.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.contract.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetailResponse {

    @JsonProperty("responseheader")
    ResponseHeader responseHeader;

    @JsonProperty("user")
    List<User> user;

}
