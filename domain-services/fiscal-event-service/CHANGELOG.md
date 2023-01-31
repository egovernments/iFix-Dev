# Changelog
All notable changes to this module will be documented in this file.

## 2.2.0 - 2023-01-06
- Shifted from mongodb to postgres database to store fiscal events.

## 2.1.0 - 2022-06-16
- Added the 'receivers', 'fromIngestionTime','toIngestionTime' search criteria in fiscal event search api

## 2.0.1 - 2022-04-22
- Added the version to the Fiscal Events.

## 2.0.0 - 2022-02-25
- Removed the project.
- added the coaCode in 'amountDetails' attribute in place of 'coaId' while publishing fiscal event(s).

## 1.1.2 - 2022-02-01
- Sonar issues fix

## 1.1.1 - 2022-01-19
- Upgraded the log4j version to 2.17.1 for vulnerability fix

## 1.1.0 - 2021-10-29
- Fiscal-Event-Service will receive bulk fiscal event request data in push api request. And it will validate all the bulk data in request and in case of any missing required or invalid attributes , will throw the corresponding error message and stop processing.

## 1.0.1 - 2021-10-08
- Fiscal-Event-Service receives the EventType enum of FiscalEvent model in ALL_CAPS but it will process it in 
  Pascal_Case. The record in the kafka topic will get the enum in Pascal_Case. 

## 1.0.0 - 2021-09-24
- Baseline version released
