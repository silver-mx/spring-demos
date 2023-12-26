package dns.demo.gateway.config;

import dns.demo.gateway.service.AuthenticationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RefreshScope
@Component
public class AuthenticationGatewayFilter implements GatewayFilter {

    private final RouterValidator validator;
    private final AuthenticationClient authenticationClient;

    public AuthenticationGatewayFilter(RouterValidator validator, AuthenticationClient authenticationClient) {
        this.validator = validator;
        this.authenticationClient = authenticationClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isSecuredEndpoint(request)) {
            if (authMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String token = request.getHeaders()
                    .getOrEmpty("Authorization").get(0).replace("Bearer ", "");

            return authenticationClient.isJwtValid(token)
                    .flatMap(isValid -> {
                        log.info("Is JWT valid={}", isValid);

                        if (isValid) {
                            return chain.filter(exchange);
                        }

                        return onError(exchange, HttpStatus.UNAUTHORIZED);

                    });
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

}