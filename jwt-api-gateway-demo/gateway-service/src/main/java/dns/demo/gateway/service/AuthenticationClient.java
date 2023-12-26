package dns.demo.gateway.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import dns.demo.gateway.config.GatewayConfigProperties;
import dns.demo.gateway.config.ServiceName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


@Slf4j
@Component
public class AuthenticationClient {

    private final WebClient webClient;
    private final EurekaClient eurekaClient;
    private final String authServiceName;

    public AuthenticationClient(EurekaClient eurekaClient,
                                GatewayConfigProperties properties) {
        this.eurekaClient = eurekaClient;
        this.authServiceName = properties.getServiceNames().get(ServiceName.AUTHENTICATION);
        this.webClient = WebClient.builder().build();
    }

    public Mono<Boolean> isJwtValid(String jwtToken) {
        eurekaClient.getApplications().getRegisteredApplications()
                .forEach(app -> log.info("Application={}", app));
        InstanceInfo instanceInfo = eurekaClient.getApplication(authServiceName).getInstances().get(0);

        log.info("auth-service-info={}", instanceInfo);

        String authServiceUrl = String.format("http://%s:%s/auth/validate-jwt", instanceInfo.getHostName(), instanceInfo.getPort());

        return webClient.post()
                .uri(authServiceUrl)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwtToken))
                .retrieve()
                .bodyToMono(Void.class)
                .then(Mono.defer(() -> Mono.just(true)))
                .onErrorResume(WebClientException.class, e -> Mono.just(false));
    }
}
