package org.egov.ifix.models;

import java.util.List;

import org.egov.common.contract.response.ResponseHeader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartOfAccountResponse {
	ResponseHeader responseHeader;
	List<ChartOfAccount> chartOfAccounts;
	

}
 