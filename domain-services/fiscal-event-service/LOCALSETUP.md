# Local Setup

To setup the fiscal event service in your local system, clone the [fiscal-event-service](https://github.com/misdwss/iFix-Dev/tree/master/domain-services/fiscal-event-service).


## Infra Dependency

- [x] Mongo DB
- [x] Kafka
  - [x] Producer

## Running Locally

To run the fiscal event service in local system, you need to port forward as below.

```bash
 kubectl port-forward {ifix master service pod name} 8030:8080
```
