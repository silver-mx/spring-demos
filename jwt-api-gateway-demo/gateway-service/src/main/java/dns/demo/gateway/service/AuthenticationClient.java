package dns.demo.gateway.service;

import dns.demo.gateway.config.GatewayConfigProperties;
import dns.demo.gateway.config.ServiceName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class AuthenticationClient {

    private final WebClient webClient;

    public AuthenticationClient(GatewayConfigProperties properties, WebClient.Builder loadBalancedWebClientBuilder) {
        String authServiceName = properties.getServiceNames().get(ServiceName.AUTHENTICATION);
        // See WebClientConfig to see how the webClientBuilder is configured as load balanced.
        this.webClient = loadBalancedWebClientBuilder
                .baseUrl("http://" + authServiceName + "/auth/validate-jwt")
                .build();
    }

    public Mono<Boolean> isJwtValid(String jwtToken) {
        return webClient.post()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwtToken))
                .retrieve()
                .bodyToMono(Void.class)
                .then(Mono.defer(() -> {
                    log.info("Got a positive response from auth-service for the JWT validation.");
                    return Mono.just(true);
                }))
                .onErrorResume(WebClientException.class, e -> {
                    log.error("Got error from auth-service", e);
                    return Mono.just(false);
                });
    }
}
