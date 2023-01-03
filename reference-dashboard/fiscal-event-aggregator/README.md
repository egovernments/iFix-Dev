# fiscal event aggregator

Fiscal Event Aggregator is a java standalone application, which will be running as cron job to aggregate the fiscal event data from Druid data store to Postgres DB.It will pull all the fiscal event data as 

1. Group by of attributes_departmentEntity_ancestry_6_id, coa id and event type And sum of amounts.
2. Pending collection(s) w.r.t distinct attributes_departmentEntity_ancestry_6_id(s).
3. Pending payment(s) w.r.t distinct attributes_departmentEntity_ancestry_6_id(s).

Aggregate these all data and push it to postgres DB.

# Running in Specific environment

This service has been configured as cron job. Hence to run this service , first check in the specific environment that this service has been configured as cron job or not. To check run the below command

```bash
kubectl get cronjobs
```
If you want to run this program manually, use the below command

```bash
kubectl create job --from=cronjob/fiscal-event-aggregator <manual-job-name>
```
You can use custom name in place of "manual-job-name".