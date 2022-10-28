# Changelog
All notable changes to this module will be documented in this file.

## 2.0.1 - 2022-04-22
- Removed the version addition into Fiscal Events. It'll get the version of fiscal events from fiscal-event-service as part of consumer.

## 2.0.0 - 2022-03-03
- Removed code related to project, department, department-entity and expenditure. These details will be stored as 
  part of additional attributes.
- Removed MongoDB Sink. `fiscal-event-service` will directly push data to MongoDB Sink topic. 
- Refactored data models: parentEventId -> linkedEventId. 

## 1.0.5 - 2022-02-03
- Sonar issues fix

## 1.0.4 - 2022-01-28
- Removed the "web" package as don't have api endpoint.

## 1.0.3 - 2022-01-19
- Upgraded the log4j version to 2.17.1 for vulnerability fix

## 1.0.2 - 2021-11-10
- Included the audit details while processing the fiscal event data to druid and hence in metabase.

## 1.0.1 - 2021-10-08
- Fiscal-Event-Post-Processor processes EventType enum of FiscalEvent model in Pascal_Case instead of ALL_CAPS. 

## 1.0.0 - 2021-09-24
- Baseline version released
