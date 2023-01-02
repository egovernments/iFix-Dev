package org.egov.ifix.aggregate.processor;


import com.fasterxml.jackson.databind.JsonNode;
import in.zapr.druid.druidry.aggregator.CountAggregator;
import in.zapr.druid.druidry.aggregator.DoubleSumAggregator;
import in.zapr.druid.druidry.aggregator.DruidAggregator;
import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.exception.QueryException;
import in.zapr.druid.druidry.dataSource.TableDataSource;
import in.zapr.druid.druidry.dimension.DefaultDimension;
import in.zapr.druid.druidry.dimension.DruidDimension;
import in.zapr.druid.druidry.dimension.enums.OutputType;
import in.zapr.druid.druidry.filter.SelectorFilter;
import in.zapr.druid.druidry.granularity.Granularity;
import in.zapr.druid.druidry.granularity.PredefinedGranularity;
import in.zapr.druid.druidry.granularity.SimpleGranularity;
import in.zapr.druid.druidry.query.DruidQuery;
import in.zapr.druid.druidry.query.aggregation.DruidGroupByQuery;
import in.zapr.druid.druidry.query.config.Interval;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.aggregate.config.ConfigProperties;
import org.egov.ifix.aggregate.model.FiscalEventAggregate;
import org.egov.ifix.aggregate.util.FiscalEventAggregateConstants;
import org.egov.ifix.aggregate.util.FiscalEventAggregateUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.egov.ifix.aggregate.util.FiscalEventAggregateConstants.GP_ID;

@Component
@Slf4j
public class DruidDataQueryProcessor {

    public static final String PROJECT_ID = "attributes.project.id";
    public static final String EVENT_TYPE = "eventType";
    public static final String AMOUNT = "amount";
    public static final String COA_ID = "coa.id";
    @Autowired
    private DruidClient druidClient;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private FiscalEventAggregateUtil aggregateUtil;

    /**
     * Fetch the fiscal event data with total sum amount , count from druid date store
     * based on the group of project id, coa id, event type
     *
     * @return
     */
    public List<FiscalEventAggregate> fetchFiscalEventFromDruid() {
        Map<String, Integer> fiscalYearMap = aggregateUtil.getFiscalYear();
        List<FiscalEventAggregate> finalFiscalEventAggregates = new ArrayList<>();

        for (String fyKey : fiscalYearMap.keySet()) {
            int fiscalYear = fiscalYearMap.get(fyKey);

            //DruidQuery groupByQuery = getDruidQueryForGroupbyProjectIdAndCoaIdAndEventType(fiscalYear);
            DruidQuery groupByQuery = getDruidQueryForGroupbyGPIdAndCoaIdAndEventType(fiscalYear,GP_ID);
            DruidQuery distinctProjectQuery = getDruidQueryForProjectDetails(fiscalYear);
            DruidQuery distinctCoaIdQuery = getDruidQueryForCoaDetails(fiscalYear);
            //department entity 6--> id
            DruidQuery demandEventTypeQuery = getDruidQueryForProjectIdAndSumAmountBy(FiscalEventAggregateConstants.EVENT_TYPE_DEMAND, fiscalYear);
            DruidQuery receiptEventTypeQuery = getDruidQueryForProjectIdAndSumAmountBy(FiscalEventAggregateConstants.EVENT_TYPE_RECEIPT, fiscalYear);
            DruidQuery billEventTypeQuery = getDruidQueryForProjectIdAndSumAmountBy(FiscalEventAggregateConstants.EVENT_TYPE_BILL, fiscalYear);
            DruidQuery paymentEventTypeQuery = getDruidQueryForProjectIdAndSumAmountBy(FiscalEventAggregateConstants.EVENT_TYPE_PAYMENT, fiscalYear);

            List<Object> groupByResponses = null;
            List<Object> distinctProjectResponses = null;
            List<Object> distinctCoaIdResponses = null;
            List<Object> demandEventTypeResponses = null;
            List<Object> receiptEventTypeResponses = null;
            List<Object> billEventTypeResponses = null;
            List<Object> paymentEventTypeResponses = null;
            try {
                groupByResponses = druidClient.query(groupByQuery, Object.class);
                log.info("Size of record returned from Group by query : {} for fiscal year : {}", groupByResponses.size(), fiscalYear);
                distinctProjectResponses = druidClient.query(distinctProjectQuery, Object.class);
                log.info("Size of record returned from distinct project id query : {} for fiscal year : {}", distinctProjectResponses.size(), fiscalYear);
                distinctCoaIdResponses = druidClient.query(distinctCoaIdQuery, Object.class);
                log.info("Size of record returned from distinct coa id query : {} for fiscal year : {}", distinctCoaIdResponses.size(), fiscalYear);

                demandEventTypeResponses = druidClient.query(demandEventTypeQuery, Object.class);
                log.info("Size of record returned from demand event type query : {} for fiscal year : {}", demandEventTypeResponses.size(), fiscalYear);
                receiptEventTypeResponses = druidClient.query(receiptEventTypeQuery, Object.class);
                log.info("Size of record returned from receipt event type query : {} for fiscal year : {}", receiptEventTypeResponses.size(), fiscalYear);

                billEventTypeResponses = druidClient.query(billEventTypeQuery, Object.class);
                log.info("Size of record returned from bill event type query : {} for fiscal year : {}", billEventTypeResponses.size(), fiscalYear);
                paymentEventTypeResponses = druidClient.query(paymentEventTypeQuery, Object.class);
                log.info("Size of record returned from payment event type query : {} for fiscal year : {}", paymentEventTypeResponses.size(), fiscalYear);
            } catch (QueryException e) {
                log.error("Exception occurred while querying the data from druid data store : {}", e.getDruidError());
            }
            log.debug("Group by Response : {}", groupByResponses);
            if (groupByResponses == null || groupByResponses.isEmpty()) {
                log.info("There are no fiscal event data with group by of project id, event type and coa id for fiscal year : {}", fiscalYear);
                continue;
            }

            //Create a map of key as project id and event node details as value
            Map<String, JsonNode> projectNodeMap = aggregateUtil.getProjectDetailsMap(distinctProjectResponses);
            //Create a map of key as coa id and event node details as value
            Map<String, JsonNode> coaNodeMap = aggregateUtil.getCOADetailsMap(distinctCoaIdResponses);

            //Create a map of key as project id and demand event node details as value
            Map<String, JsonNode> demandEventTypeNodeMap = aggregateUtil.getEventTypeMap(demandEventTypeResponses);
            //Create a map of key as project id and receipt event node details as value
            Map<String, JsonNode> receiptEventTypeNodeMap = aggregateUtil.getEventTypeMap(receiptEventTypeResponses);

            //Create a map of key as project id and bill event node details as value
            Map<String, JsonNode> billEventTypeNodeMap = aggregateUtil.getEventTypeMap(billEventTypeResponses);
            //Create a map of key as project id and payment event node details as value
            Map<String, JsonNode> paymentEventTypeNodeMap = aggregateUtil.getEventTypeMap(paymentEventTypeResponses);

            //Get the PENDING_COLLECTION aggregated fiscal event data
            List<FiscalEventAggregate> pendingCollectionAggregatedList = aggregateUtil.getPendingCollectionFiscalEventAggregatedData(demandEventTypeNodeMap, receiptEventTypeNodeMap, projectNodeMap,
                    FiscalEventAggregateConstants.EVENT_TYPE_PENDING_COLLECTION, fiscalYear);
            //Get the PENDING PAYMENT aggregated fiscal event data
            List<FiscalEventAggregate> pendingPaymentAggregatedList = aggregateUtil.getPendingCollectionFiscalEventAggregatedData(billEventTypeNodeMap, paymentEventTypeNodeMap, projectNodeMap,
                    FiscalEventAggregateConstants.EVENT_TYPE_PENDING_PAYMENT, fiscalYear);
            //Get the details by project id and coa id map key and create a List<FiscalEventAggregate>
            List<FiscalEventAggregate> fiscalEventAggregates = aggregateUtil.getFiscalEventAggregateData(groupByResponses
                    , projectNodeMap, coaNodeMap, fiscalYear);

            finalFiscalEventAggregates.addAll(pendingCollectionAggregatedList);
            finalFiscalEventAggregates.addAll(pendingPaymentAggregatedList);
            finalFiscalEventAggregates.addAll(fiscalEventAggregates);
        }
        return finalFiscalEventAggregates;
    }

    private DruidQuery getDruidQueryForProjectIdAndSumAmountBy(String eventType, int fiscalYear) {
        TableDataSource dataSource = new TableDataSource(configProperties.getFiscalEventDataSource());

        DateTime startTime = new DateTime(fiscalYear, 04, 1, 0, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(fiscalYear + 1, 03, 31, 0, 0, 0, DateTimeZone.UTC);
        Interval interval = new Interval(startTime, endTime);

        Granularity granularity = new SimpleGranularity(PredefinedGranularity.ALL);

//        DruidDimension druidDimension = new DefaultDimension(PROJECT_ID, PROJECT_ID, OutputType.STRING);
        DruidDimension druidDimension = new DefaultDimension(GP_ID, GP_ID, OutputType.STRING);

        SelectorFilter filter = new SelectorFilter(EVENT_TYPE, eventType);

        List<DruidAggregator> aggregators = new ArrayList<>();
        aggregators.add(new DoubleSumAggregator(AMOUNT, AMOUNT));

        return (DruidGroupByQuery.builder()
                .dataSource(dataSource)
                .dimensions(Collections.singletonList(druidDimension))
                .granularity(granularity)
                .filter(filter)
                .aggregators(aggregators)
                .intervals(Collections.singletonList(interval))
                .build());
    }


    private DruidQuery getDruidQueryForCoaDetails(int fiscalYear) {
        TableDataSource dataSource = new TableDataSource(configProperties.getFiscalEventDataSource());

        DateTime startTime = new DateTime(fiscalYear, 04, 1, 0, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(fiscalYear + 1, 03, 31, 0, 0, 0, DateTimeZone.UTC);
        Interval interval = new Interval(startTime, endTime);

        Granularity granularity = new SimpleGranularity(PredefinedGranularity.ALL);

        List<DruidDimension> druidDimensions = new ArrayList<>();
        druidDimensions.add(new DefaultDimension(COA_ID, COA_ID, OutputType.STRING));

        druidDimensions.add(new DefaultDimension("coa.coaCode", "coa.coaCode", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.groupHead", "coa.groupHead", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.groupHeadName", "coa.groupHeadName", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.majorHead", "coa.majorHead", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.majorHeadName", "coa.majorHeadName", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.minorHead", "coa.minorHead", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.minorHeadName", "coa.minorHeadName", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.objectHead", "coa.objectHead", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.objectHeadName", "coa.objectHeadName", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.subHead", "coa.subHead", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.subHeadName", "coa.subHeadName", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.subMajorHead", "coa.subMajorHead", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("coa.subMajorHeadName", "coa.subMajorHeadName", OutputType.STRING));

//        List<DruidAggregator> aggregators = new ArrayList<>();
//        aggregators.add(new DistinctCountAggregator("coa.id","coa.id"));

        return (DruidGroupByQuery.builder()
                .dataSource(dataSource)
                .dimensions(druidDimensions)
                .granularity(granularity)
                .filter(null)
                //.aggregators(aggregators)
                .intervals(Collections.singletonList(interval))
                .build());
    }


    @Deprecated
    private DruidQuery getDruidQueryForProjectDetails(int fiscalYear) {
        TableDataSource dataSource = new TableDataSource(configProperties.getFiscalEventDataSource());

        DateTime startTime = new DateTime(fiscalYear, 04, 1, 0, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(fiscalYear + 1, 03, 31, 0, 0, 0, DateTimeZone.UTC);
        Interval interval = new Interval(startTime, endTime);

        Granularity granularity = new SimpleGranularity(PredefinedGranularity.ALL);

        List<DruidDimension> druidDimensions = new ArrayList<>();
        druidDimensions.add(new DefaultDimension(PROJECT_ID, PROJECT_ID, OutputType.STRING));

        druidDimensions.add(new DefaultDimension("attributes.department.code", "attributes.department.code", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.department.id", "attributes.department.id", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.department.name", "attributes.department.name", OutputType.STRING));

        druidDimensions.add(new DefaultDimension("attributes.expenditure.code", "attributes.expenditure.code", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.expenditure.id", "attributes.expenditure.id", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.expenditure.name", "attributes.expenditure.name", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.expenditure.type", "attributes.expenditure.type", OutputType.STRING));

        druidDimensions.add(new DefaultDimension("tenantId", "tenantId", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("government.id", "government.id", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("government.name", "government.name", OutputType.STRING));

        druidDimensions.add(new DefaultDimension("attributes.departmentEntity.id", "attributes.departmentEntity.id", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.departmentEntity.code", "attributes.departmentEntity.code", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.departmentEntity.name", "attributes.departmentEntity.name", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.departmentEntity.hierarchyLevel", "attributes.departmentEntity.hierarchyLevel", OutputType.STRING));

        druidDimensions.add(new DefaultDimension("attributes.project.code", "attributes.project.code", OutputType.STRING));
        druidDimensions.add(new DefaultDimension("attributes.project.name", "attributes.project.name", OutputType.STRING));

        int hierarchyLevel = FiscalEventAggregateConstants.DEFAULT_HIERARCHY_LEVEL;

        for (int i = 0; i <= hierarchyLevel; i++) {
            druidDimensions.add(new DefaultDimension("attributes.departmentEntity.ancestry[" + i + "].code", "attributes.departmentEntity.ancestry[" + i + "].code", OutputType.STRING));
            druidDimensions.add(new DefaultDimension("attributes.departmentEntity.ancestry[" + i + "].hierarchyLevel", "attributes.departmentEntity.ancestry[" + i + "].hierarchyLevel", OutputType.STRING));
            druidDimensions.add(new DefaultDimension("attributes.departmentEntity.ancestry[" + i + "].id", "attributes.departmentEntity.ancestry[" + i + "].id", OutputType.STRING));
            druidDimensions.add(new DefaultDimension("attributes.departmentEntity.ancestry[" + i + "].name", "attributes.departmentEntity.ancestry[" + i + "].name", OutputType.STRING));
        }

//        List<DruidAggregator> aggregators = new ArrayList<>();
//        aggregators.add(new DistinctCountAggregator("project.id","project.id"));
//        aggregators.add(new DoubleSumAggregator("amount", "amount"));

        return (DruidGroupByQuery.builder()
                .dataSource(dataSource)
                .dimensions(druidDimensions)
                .granularity(granularity)
                .filter(null)
                //.aggregators(aggregators)
                .intervals(Collections.singletonList(interval))
                .build());
    }

//    @Deprecated
//    private DruidGroupByQuery getDruidQueryForGroupbyProjectIdAndCoaIdAndEventType(int fiscalYear) {
//        TableDataSource dataSource = new TableDataSource(configProperties.getFiscalEventDataSource());
//
//        DateTime startTime = new DateTime(fiscalYear, 04, 1, 0, 0, 0, DateTimeZone.UTC);
//        DateTime endTime = new DateTime(fiscalYear + 1, 03, 31, 0, 0, 0, DateTimeZone.UTC);
//        Interval interval = new Interval(startTime, endTime);
//
//        Granularity granularity = new SimpleGranularity(PredefinedGranularity.ALL);
//
//        List<DruidDimension> druidDimensions = new ArrayList<>();
//        druidDimensions.add(new DefaultDimension(PROJECT_ID, PROJECT_ID, OutputType.STRING));
//        druidDimensions.add(new DefaultDimension(COA_ID, COA_ID, OutputType.STRING));
//        druidDimensions.add(new DefaultDimension(EVENT_TYPE, EVENT_TYPE, OutputType.STRING));
//
//        List<DruidAggregator> aggregators = new ArrayList<>();
//        aggregators.add(new CountAggregator("Count"));
//        aggregators.add(new DoubleSumAggregator(AMOUNT, AMOUNT));
//
//        return (DruidGroupByQuery.builder()
//                .dataSource(dataSource)
//                .dimensions(druidDimensions)
//                .granularity(granularity)
//                .filter(null)
//                .aggregators(aggregators)
//                .intervals(Collections.singletonList(interval))
//                .build());
//    }


    private DruidQuery getDruidQueryForGroupbyGPIdAndCoaIdAndEventType(int fiscalYear, String gpId) {
        TableDataSource dataSource = new TableDataSource(configProperties.getFiscalEventDataSource());

        DateTime startTime = new DateTime(fiscalYear, 04, 1, 0, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(fiscalYear + 1, 03, 31, 0, 0, 0, DateTimeZone.UTC);
        Interval interval = new Interval(startTime, endTime);

        Granularity granularity = new SimpleGranularity(PredefinedGranularity.ALL);

        List<DruidDimension> druidDimensions = new ArrayList<>();
        druidDimensions.add(new DefaultDimension(gpId, gpId, OutputType.STRING));
        druidDimensions.add(new DefaultDimension(COA_ID, COA_ID, OutputType.STRING));
        druidDimensions.add(new DefaultDimension(EVENT_TYPE, EVENT_TYPE, OutputType.STRING));

        List<DruidAggregator> aggregators = new ArrayList<>();
        aggregators.add(new CountAggregator("Count"));
        aggregators.add(new DoubleSumAggregator(AMOUNT, AMOUNT));

        return (DruidGroupByQuery.builder()
                .dataSource(dataSource)
                .dimensions(druidDimensions)
                .granularity(granularity)
                .filter(null)
                .aggregators(aggregators)
                .intervals(Collections.singletonList(interval))
                .build());
    }

}
