package org.egov.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class KeyCloackData implements Serializable {

    public String client_id;
    public String client_secret;
    public String grant_type;
    public String access_token;
    public Long expires_in;
    public Long refresh_expires_in;
    public String token_type;
    public String scope;

}
