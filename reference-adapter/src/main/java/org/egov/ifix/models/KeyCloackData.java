package org.egov.ifix.models;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RedisHash
public class KeyCloackData implements Serializable{
	
	public String client_id;
	public String client_secret;
	public String grant_type;
	public String access_token;
	public Long expires_in;
	public Long refresh_expires_in;
	public String token_type;
	public String scope;

}
