package dns.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = TestConfig.class)
class ObservabilitySpringDemoApplicationTests {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Container
    static DockerComposeContainer composeEnv = new DockerComposeContainer(new File("compose.yaml"))
            .withExposedService("tempo_1", 9411, Wait.forListeningPort())
            .withExposedService("loki_1", 3100, Wait.forListeningPort());

    @Test
    void verifyObservabilitySetup(CapturedOutput output) {
        ConfigurableApplicationContext applicationContext = ObservabilitySpringDemoApplication.runClientAndServerApplications(new String[]{});
        assertThat(output).contains("All posts size=100");

        // Send a request to generate more traces and logs
        ResponseEntity<String> allPostsResponse = restTemplate.withBasicAuth("user", "password")
                .getForEntity("http://localhost:8080/api/posts", String.class);
        assertThat(allPostsResponse.getStatusCode()).isEqualTo(OK);

        /* Check that tempo receives tracing information. The simplest test is to check the service.name tag that is taken
        from spring.application.name.*/
        String expectedTempoJson = """
                {
                    "tagValues": [
                        "client",
                        "server"
                    ]
                }
                """;
        await().atMost(Duration.ofMinutes(1)).until(() -> {
            ResponseEntity<String> tempoResponse = restTemplate.getForEntity("http://localhost:3200/api/search/tag/service.name/values", String.class);
            log.info("Tempo response={}", tempoResponse.getBody());
            return nonNull(tempoResponse.getBody())
                    && JSONCompare.compareJSON(expectedTempoJson, tempoResponse.getBody(), JSONCompareMode.STRICT).passed();
        });

        /* Check the labels registered in loki are as expected. The labels are defined in logback-spring.xml.*/
        String expectedLokiJson = """
                {
                    "status": "success",
                    "data": [
                        "app",
                        "host",
                        "level",
                        "traceID"
                    ]
                }
                """;

        await().atMost(Duration.ofMinutes(1)).until(() -> {
            ResponseEntity<String> lokiResponse = restTemplate.getForEntity("http://localhost:3100/loki/api/v1/labels", String.class);
            return nonNull(lokiResponse.getBody())
                    && JSONCompare.compareJSON(expectedLokiJson, lokiResponse.getBody(), JSONCompareMode.STRICT).passed();
        });

        // Exit the applications that we started manually
        SpringApplication.exit(applicationContext, () -> 0);

        // Interrupt the background threads that keep ryuk (testcontainers) waiting.
        Thread.getAllStackTraces().keySet().forEach(t -> {
            if (t.getName().contains("BatchSpanProcessor") || t.getName().contains("loki")) {
                t.interrupt();
            }
        });
    }

}
