package dns.demo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class GatewayConfig {

    private final AuthenticationGatewayFilter filter;

    public GatewayConfig(AuthenticationGatewayFilter filter) {
        this.filter = filter;
    }

    /* Unused at the moment as a AuthenticationGlobalFilter was created.*/
    // @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .build();
    }
}