package org.egov.ifix.service;

import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.models.ChartOfAccount;
import org.egov.ifix.repository.ChartOfAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author mani 
 *         This will manage getting the right chartofaccount in running
 *         environment. This will cache the data once received from repository.
 *         Expiry is not set it will be global config which will clear cache on
 *         specified time It depends on repository to provide COA object for
 *         given code This will include validation whether the adapter is
 *         autherised to post chartofaccount to IFIX Validated coas put to cache
 *         invalid are logged
 */
@Service
@Slf4j
public class ChartOfAccountService {

	@Autowired
	private ChartOfAccountRepository chartOfAccountRepository;

	@Autowired
	private AdapterCache<ChartOfAccount> coaCache;

	private static final Long TIMEBEFOREEXPIRY = 5l * 60 * 1000;

	public String getChartOfAccount(String code,JsonObject jsonObject) {

		ChartOfAccount coa = coaCache.getValue(code);
		if (coa == null) {
			coa = chartOfAccountRepository.getChartOfAccount(code,jsonObject);

			if (coa != null) {
				if (isPostingAllowed(coa)) {
					coaCache.putValue(code, coa);
				} else {
					log.error("Received a unautherised coa for code " + code + " and COA is " + coa);

				}
			}else
			{
				throw new RuntimeException("Error while Finding COA");
			}
		} else {
			log.info("got coa from Cache", coa.getId());
		}

		return coa.getId();

	}

	/**
	 * 
	 * @param coa
	 * @return Api to get data from registry and validate whether the posting is
	 *         allowed
	 */

	public boolean isPostingAllowed(ChartOfAccount coa) {

		return true;
	}

}
