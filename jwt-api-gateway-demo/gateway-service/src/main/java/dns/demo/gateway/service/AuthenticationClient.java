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
                .baseUrl("lb://" + authServiceName + "/auth/validate-jwt")
                .build();
    }

    public Mono<Boolean> isJwtValid(String jwtToken) {
        return webClient.post()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwtToken))
                .retrieve()
                .bodyToMono(Void.class)
                .then(Mono.defer(() -> Mono.just(true)))
                .onErrorResume(WebClientException.class, e -> Mono.just(false));
    }
}
