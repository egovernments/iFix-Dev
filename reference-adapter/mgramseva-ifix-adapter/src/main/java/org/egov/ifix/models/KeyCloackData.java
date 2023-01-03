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

	private String client_id;
	private String client_secret;
	private String grant_type;
	private String access_token;
	private Long expires_in;
	private Long refresh_expires_in;
	private String token_type;
	private String scope;

}
