# Local Setup

To setup the ifix department entity service in your local system, clone the [ifix-department-entity-service](https://github.com/egovernments/iFix-Dev/tree/develop/domain-services/ifix-department-entity-service).


## Infra Dependency

- [x] Mongo DB
  
  
## Running Locally

To run the ifix department entity service in local system, you need to port forward as below.

```bash
 kubectl port-forward {ifix master service pod name} 8030:8080
```
