# monitoring-controller
This service is responsible for monitoring the health of the deployed mini services, similar to Kubernetes. Upon receiving health checks from a deployed service, this service takes actions such as changing calling the deployment service to spawn another instance, change message queues, set max thread pool, etc.
