# cf-converger

Declaratively converges the runtime configuration of multiple Cloud Foundry instances.

This project is work-in-progress; the features listed below are on the roadmap. To see how far we've got, check out the [cf-converger Pivotal Tracker](https://www.pivotaltracker.com/n/projects/1590869).

## How does it work?

cf-converger runs as a web service that allows users to  post YAML files describing a Cloud Foundry organization and everything in it: spaces, users, roles, services, security groups et al. Upon posting this config, cf-converger will compare the desired state to the actual state of multiple Cloud Foundry instances, and present a plan of what actions will be performed. This can then be enacted, providing an easy way to set up everything you need in order to be able to start deploying apps.

## Example

YAML declaration:

```
---
schema_version: 1
org:
  name: trading-division
  org_managers:
  - important@example.com
  spaces:
  - name: DEV
    user_provided_services:
    - name: OracleDB
      credentials:
        username: sa
        password: secret
    space_developers:
    - developer@example.com
    network:
    - description: off-paas-oracle
      ips: [123.123.123.123, 123.123.123.124]
      ports: [8117]
  - name: PROD
    space_auditor:
    - operator@engineerbetter.com
```

```
$ curl -X POST --data @declaration.yml https://converger.cf-app.com/groups/non-production
```

## Why?

Managing multiple Cloud Foundry instances is time-consuming and error-prone.
