# Local Setup

To setup the fiscal event post processor in your local system, clone the [fiscal-event-post-processor](https://github.com/egovernments/iFix-Dev/tree/develop/domain-services/fiscal-event-post-processor).


## Infra Dependency

- [x] Kafka
  - [x] Producer
  - [x] Consumer
  
## Running Locally

To run the fiscal event post processor in local system, you need to port forward as below.

```bash
 kubectl port-forward {ifix master service pod name} 8030:8080
 kubectl port-forward {fiscal event service pod name} 8021:8080
```
