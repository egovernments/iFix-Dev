CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_tenantid ON eg_ifix_fiscal_event(tenantid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_sender ON eg_ifix_fiscal_event(sender);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_eventtype ON eg_ifix_fiscal_event(eventtype);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_ingestiontime ON eg_ifix_fiscal_event(ingestiontime);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_eventtime ON eg_ifix_fiscal_event(eventtime);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_referenceid ON eg_ifix_fiscal_event(referenceid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_linkedEventid ON eg_ifix_fiscal_event(linkedEventid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_linkedreferenceid ON eg_ifix_fiscal_event(linkedreferenceid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_createdby ON eg_ifix_fiscal_event(createdby);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_lastmodifiedtime ON eg_ifix_fiscal_event(lastmodifiedtime);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_lastmodifiedby ON eg_ifix_fiscal_event(lastmodifiedby);
CREATE INDEX IF NOT EXISTS index_eg_ifix_fiscal_event_createdtime ON eg_ifix_fiscal_event(createdtime);

CREATE INDEX IF NOT EXISTS index_eg_ifix_receivers_fiscaleventid ON eg_ifix_receivers(fiscaleventid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_receivers_receiver ON eg_ifix_receivers(receiver);

CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_tenantid ON eg_ifix_amount_detail(tenantid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_fiscaleventid ON eg_ifix_amount_detail(fiscaleventid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_amount ON eg_ifix_amount_detail(amount);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_coaid ON eg_ifix_amount_detail(coaid);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_frombillingperiod ON eg_ifix_amount_detail(frombillingperiod);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_tobillingperiod ON eg_ifix_amount_detail(tobillingperiod);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_createdtime ON eg_ifix_amount_detail(createdtime);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_createdby ON eg_ifix_amount_detail(createdby);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_lastmodifiedtime ON eg_ifix_amount_detail(lastmodifiedtime);
CREATE INDEX IF NOT EXISTS index_eg_ifix_amount_detail_lastmodifiedby ON eg_ifix_amount_detail(lastmodifiedby);
