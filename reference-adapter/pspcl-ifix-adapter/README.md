# pspcl ifix adapter

Pspcl iFix Adapter is a java standalone application, which will be running as cron job to fetch the bills and payments from PSPCL system and reconcile the same . And finally publish them to iFix core system. Below is the high level approach :

1. Get the PSPCL bills and payments based on account number (account number and Gram panchayat mapping could be found in MDMS).
2. Reconcile the bills and payments. and log the reconciled current month bill and payment in Postgres DB.
3. Once reconciliation is done.Publish the fiscal events to the iFix Core system.
4. Log all the success or failure published fiscal events in Postgres DB.

# Running in Specific environment

This service has been configured as cron job. Hence, to run this service , first check in the specific environment that this service has been configured as cron job or not. To check run the below command

```bash
kubectl get cronjobs
```
If you want to run this program manually, use the below command

```bash
kubectl create job --from=cronjob/pspcl-ifix-adapter <manual-job-name>
```
You can use custom name in place of "<manual-job-name>".
