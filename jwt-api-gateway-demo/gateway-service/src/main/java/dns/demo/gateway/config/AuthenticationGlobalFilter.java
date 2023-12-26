package dns.demo.gateway.config;

import dns.demo.gateway.service.AuthenticationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
public class AuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private final RouterValidator validator;
    private final AuthenticationClient authenticationClient;

    public AuthenticationGlobalFilter(RouterValidator validator, AuthenticationClient authenticationClient) {
        this.validator = validator;
        this.authenticationClient = authenticationClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        log.info("Filtering request[method={}, uri={}]", request.getMethod(), request.getURI());

        if (validator.isSecuredEndpoint(request)) {

            log.info ("Request[method={}, uri={}] needs authentication", request.getMethod(), request.getURI());

            if (authMissing(request)) {
                return onError(exchange, UNAUTHORIZED);
            }

            String token = request.getHeaders()
                    .getOrEmpty("Authorization").get(0).replace("Bearer ", "");

            return authenticationClient.isJwtValid(token)
                    .flatMap(isValid -> {

                        log.info("Is JWT valid={}", isValid);

                        if (isValid) {
                            return chain.filter(exchange);
                        }

                        return onError(exchange, UNAUTHORIZED);

                    });
        }

        log.info("Request[method={}, uri={}] is an open endpoint, no authentication needed", request.getMethod(), request.getURI());

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

    @Override
    public int getOrder() {
        return -1;
    }
}