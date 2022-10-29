package org.egov.ifix.mapper.impl.bill;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.ifix.mapper.EventMapper;
import org.egov.ifix.models.Amount;
import org.egov.ifix.models.EventTypeEnum;
import org.egov.ifix.models.FiscalEvent;
import org.egov.ifix.service.ChartOfAccountService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.EventConstants;
import org.egov.ifix.utils.MasterDataMappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BillEventTypeImpl implements EventMapper {

	
	
	private static final String DEMAND = "Demands";

	private static final String REFERANCE_ID = "consumerCode";

	private static final String DEMAND_DETAILS = "demandDetails";

	private static final String TAX_AMOUNT = "taxAmount";

	private static final String TAX_PERIOD_FROM = "taxPeriodFrom";

	private static final String TAX_PERIOD_TO = "taxPeriodTo";

	private ApplicationConfiguration applicationConfiguration;
	
	private ChartOfAccountService chartOfAccountService;
	
	 

	@Autowired
	public BillEventTypeImpl(ApplicationConfiguration applicationConfiguration,ChartOfAccountService chartOfAccountService ) {
		this.applicationConfiguration = applicationConfiguration;
		this.chartOfAccountService= chartOfAccountService;
		 
	}

	@Override
	public List<FiscalEvent> transformData(JsonObject data) {
		log.info("Bill event impl executing");
		JsonArray entities = data.getAsJsonObject(EventConstants.EVENT).getAsJsonArray(EventConstants.ENTITY);
		List<FiscalEvent> fiscalEvents = new ArrayList<FiscalEvent>();
		for (int i = 0; i < entities.size(); i++) {
			JsonArray demands = entities.get(i).getAsJsonObject().getAsJsonArray(DEMAND);

			for (int j = 0; j < demands.size(); j++) {
			
				JsonObject	demand=demands.get(j).getAsJsonObject();
			   FiscalEvent fiscalEvent = FiscalEvent.builder().tenantId(applicationConfiguration.getTenantId())
					.eventType(getEventType()).eventTime(Instant.now().toEpochMilli())
					.referenceId(demand.get(REFERANCE_ID).getAsString()).parentEventId(null).parentReferenceId(null)
					.amountDetails(getAmounts(demand,data)).build();
			   fiscalEvents.add(fiscalEvent);
			}
			

		}
		return fiscalEvents;
	}

	@Override
	public String getEventType() {
		return EventTypeEnum.BILL.toString();
	}

	private List<Amount> getAmounts(JsonObject demand,JsonObject data) {
		Long taxPeriodFrom = demand.get(TAX_PERIOD_FROM).getAsLong();
		Long taxPeriodTo = demand.get(TAX_PERIOD_TO).getAsLong();

		JsonArray demandDetails = demand.getAsJsonArray(DEMAND_DETAILS);
		List<Amount> amounts = new ArrayList<Amount>();
		for (int i = 0; i < demandDetails.size(); i++) {
			JsonObject demanddetail = demandDetails.get(i).getAsJsonObject();
			
			String coaCode = demanddetail.get("taxHeadMasterCode").getAsString();

			Amount amount = Amount.builder().
					amount(demanddetail.get(TAX_AMOUNT).getAsBigDecimal()).
					
					coaId(chartOfAccountService.getChartOfAccount(coaCode,data)).
					fromBillingPeriod(taxPeriodFrom).
					toBillingPeriod(taxPeriodTo).build();

			amounts.add(amount);

		}
		return amounts;

	}
	

	
	
	
	/* as of now expense bill is same as demand so it is comented as per ghanshyam decision
	 * 
	 * private static final String BILL = "bill";

	private static final String REFERANCE_ID = "consumerCode";

	private static final String BILL_DETAILS = "billDetails";

	private static final String BILL_ACCOUNT_DETAILS = "billAccountDetails";

	private static final String AMOUNT = "amount";

	private static final String FROMPERIOD = "fromPeriod";

	private static final String TOPERIOD = "toPeriod";

	private ApplicationConfiguration applicationConfiguration;

	@Autowired
	public BillEventTypeImpl(ApplicationConfiguration applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}

	@Override
	public String getEventType() {
		return EventTypeEnum.BILL.toString();
	}

	@Override
	public List<FiscalEvent> transformData(JsonObject data) {
		JsonArray bills = data.getAsJsonObject(EventConstants.EVENT).getAsJsonArray(EventConstants.ENTITY);
		List<FiscalEvent> fiscalEvents = new ArrayList<FiscalEvent>();
		for (int i = 0; i < bills.size(); i++) {
			JsonObject bill = bills.get(i).getAsJsonObject().getAsJsonObject(BILL);

			FiscalEvent fiscalEvent = FiscalEvent.builder().tenantId(applicationConfiguration.getTenantId())
					.eventType(getEventType()).eventTime(Instant.now().toEpochMilli())
					.referenceId(bill.get(REFERANCE_ID).getAsString()).parentEventId(null).parentReferenceId(null)
					.amountDetails(getAmounts(bill)).build();

			fiscalEvents.add(fiscalEvent);

		}
		return fiscalEvents;
	}

	private List<Amount> getAmounts(JsonObject demand) {

		JsonArray billDetails = demand.getAsJsonArray(BILL_DETAILS);
		Long fromPeriod=0l;
		Long toPeriod=0l;
		List<Amount> amounts = new ArrayList<Amount>();
		for (int i = 0; i < billDetails.size(); i++) {

			 fromPeriod = billDetails.get(i).getAsJsonObject().get(FROMPERIOD).getAsLong();
			 toPeriod =  billDetails.get(i).getAsJsonObject().get(TOPERIOD).getAsLong();

			JsonArray billAccountDetails = billDetails.get(i).getAsJsonObject().getAsJsonArray(BILL_ACCOUNT_DETAILS);

			for (int j = 0; j < billAccountDetails.size(); j++) {
				JsonObject demanddetail = billAccountDetails.get(j).getAsJsonObject();

				Amount amount = Amount.builder().amount(demanddetail.get(AMOUNT).getAsBigDecimal()).coaId(null)
						.fromBillingPeriod(toPeriod).toBillingPeriod(toPeriod).build();

				amounts.add(amount);

			}
		}
		return amounts;

	}*/

}
