### SpringBoot Observability Demo

This is a basic observability demo in SpringBoot 3 based
on [Dan Vega's tutorial](https://www.youtube.com/watch?v=exRkiVLyPpc) and
a [Spring article](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3). It shows the following concepts:

The application starts two children applications, a client (port 8080) and a server (port 8081). The server calls a
cloud webservice that returns a list of posts (see https://jsonplaceholder.typicode.com/).

The application demonstrates the following concepts:

* **Metrics**: Both applications expose `/actuator/prometheus` so that Prometheus can retrieve the metrics. Prometheus
  works as a datasource for Grafana to visualize the information.
* **Tracing**: Calls between the client and server are traced and the information is sent to Tempo, that in turn works
  as a datasource for Grafana tracing charts.
  The [Spring article](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3) I followed makes use of
  different tracing libraries (`client=open-telemetry, server=zipkin-brave`) in order to show interoperability between
  tracing libraries systems.
* **Logs**: Logs are sent to Grafana-Loki and will be indexed by their `traceId`, so that we can search for the logs for
  a specific request or operation. Loki works also as a Grafana datasource, and powers the dashboard.

#### Try it:

1. Start the application in the IDE (I used IntelliJ). It will start all services needed via _docker-compose_.
2. Call `http://localhost:8080/api/posts` and set the `Authorization` header to use `user/password` as username and
   password. The file `rest-calls/http-requests.http` includes the request.
3. Open Grafana at `http://localhost:3000/dashboards`, and click on `Logs, Traces, Metrics` to show the dashboard.
4. Search by one of the traceIds found in the console logs shown in the IDE.

####

**Optional:** Go to `http://localhost:9090/` and open Prometheus. There can we execute queries depending on the data
available at `http://localhost:8080/actuator/prometheus` and `http://localhost:8081/actuator/prometheus`.