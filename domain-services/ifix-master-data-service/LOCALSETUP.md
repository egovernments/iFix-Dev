# Local Setup

To setup the ifix master data service in your local system, clone the [ifix-master-data-service](https://github.com/egovernments/iFix-Dev/tree/develop/domain-services/ifix-master-data-service).


## Infra Dependency

- [x] Mongo DB

## Running Locally

To run the ifix master data service in local system, you need to port forward as below.

```bash
 kubectl port-forward {ifix master service pod name} 8030:8080
 kubectl port-forward {ifix department entity service pod name} 8032:8080
```
