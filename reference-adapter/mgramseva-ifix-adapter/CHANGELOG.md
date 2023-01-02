# Changelog
All notable changes to this module will be documented in this file.

## 2.1.0 - 2022-09-14

- It runs quartz cron job for regular event (PSPCL) check on fiscal event service.
- After event (PSPCL) collection.
  - It reaches to mgramseva echallan service for challan creation.
  - It updates payment status by collection service.
- It updates every event(PSPCL) status into table.

## 2.0.1 - 2022-05-11
- Fixed EventTime mapping in the Event Mapper

## 2.0.0 - 2022-03-11
- Refactored Code.
- Multiple department entity support in project.
- Enrich additional attribute from Adapter Master Data Service.
- COA code replace COA id (Amount details).

## 1.1.1 - 2022-01-19
- Upgraded the log4j version to 2.17.1 for vulnerability fix

## 1.1.0 - 2021-11-19
- Support for Bulk fiscal event push to iFIX-Core

## 1.0.0
- Base Version
  added _push api. This will be used by IFIX clients like mgramseva
