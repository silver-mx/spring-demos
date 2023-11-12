### SpringBoot Observability Demo

This is a basic observability demo in SpringBoot 3 based on [Dan Vega's tutorial](https://www.youtube.com/watch?v=exRkiVLyPpc). It shows the following concepts:

* **Tracing**: By showing how long it takes to:
    1. Apply the Spring Security mechanism.
    2. The time it takes to call a webservice (https://jsonplaceholder.typicode.com/).

#### Try it:

1. Start the application in the IDE. It will start Zipkin via _docker-compose_.
2. Call `http://localhost:8080/api/posts` and give `silvermx/pass123` as username and password.
3. Open Zipkin at `http://localhost:9411` and click on `RUN QUERY` to show all traces.
4. See the latest trace in the list and click on `SHOW`. See how the total time gets broken into pieces for the
   authentication, secured call, etc...