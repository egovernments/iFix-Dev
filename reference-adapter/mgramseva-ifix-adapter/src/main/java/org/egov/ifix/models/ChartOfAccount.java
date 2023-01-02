package org.egov.ifix.models;

import org.springframework.data.redis.core.RedisHash;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 
 * @author mani
 * Used to mapping COA between IFIX-client and IFIX
 * As of now assumption  is IFIX-client will use the coaCode in its transactions and IFIX will refer id in fiscal event 
 * Since only code to id mapping is required only this fields are kept in the class
 * Later if IFIX-clinet refers to 6 coa and sents in the request it should be added as seperate code becuase 
 * of mismatch in 6 digit vs 16 digit coa
 * 
 *
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RedisHash
public class ChartOfAccount {

	private String id = null;

	private String coaCode = null;

	private String tenantId = null;

}
