package dns.demo.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock({
        @ConfigureWireMock(name = "auth-service", property = "auth-service.url")
})
class GatewayApplicationTest {

    @InjectWireMock("auth-service")
    private WireMockServer wiremock;

    @LocalServerPort
    private int port = 0;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        String baseUri = "http://localhost:" + port;
        webTestClient = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(10))
                .baseUrl(baseUri).build();
    }

    @Test
    void contextLoads() {
    }

    @ParameterizedTest
    @ValueSource(ints = {401, 201})
    void testAuthenticatedEndpoint(int statusCode) {
        wiremock.stubFor(post("/auth/validate-jwt").willReturn(aResponse()
                .withStatus(statusCode == 401 ? statusCode : 204)
        ));
        wiremock.stubFor(post("/auth/register-user").willReturn(aResponse()
                .withStatus(statusCode)
        ));

        webTestClient.post()
                .uri("/auth/register-user")
                .bodyValue("""
                        {
                          "username": "user",
                          "password": "pass",
                          "roles": ["read", "write"]
                        }
                        """)
                .headers(httpHeaders -> httpHeaders.setBearerAuth("a-token"))
                .exchange()
                .expectStatus()
                .isEqualTo(statusCode);
    }

    @Test
    void testOpenEndpoint() {
        String tokenResponse = """
                {
                  "token": "eyJraWQiOiI4OGEyYWMyYy1hNzhmLTQ3MGMtODU3Yi1jZGY3M2FiMTU3ZWEiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJkaWVnbyIsInN1YiI6ImRudW5leiIsImV4cCI6MTcwMzc4ODcwNiwiaWF0IjoxNzAzNzAyMzA2LCJzY29wZSI6IiJ9.WgerPAQNOsh0ZnkEWLcdw0BwEsvnu1Ls0sHLSW7hMv1Qv_rlYJc6Ft_j2Z8BByLh2vYKDl0eefft6boLBaelR6bLhOOzxtTzHG-3R7QWNLt97moi2irTW08rLhnGcmxJQpwjH3pokL_wlHxEsqn6na2lUVbgibcbj_KHVGz-e-XZ3XKVdH2Ijl3m4YpLPZYi6qpM0SlBBYsQDoxP8r8IE7u0c9zNd6iotn9rtnUuReoa2oxm5maxV3Ms7OrXBJKEj4l8i4Xdq6SwLS1J9KnEKmoStLCi7cxffWYhrGsQK5uQr9FC1suSa_Kk0jaZEyJtSZRCqhDeMZ2b3gAR_7BlqQ",
                  "expiresAt": 1703788706
                }
                """;

        String createTokenUri = "/auth/token";

        wiremock.stubFor(post(createTokenUri).willReturn(aResponse()
                .withStatus(OK.value())
                .withBody(tokenResponse)
        ));

        webTestClient.post()
                .uri(createTokenUri)
                .bodyValue("""
                        {
                          "username": "dnunez",
                          "password": "pass"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(OK);
    }

}