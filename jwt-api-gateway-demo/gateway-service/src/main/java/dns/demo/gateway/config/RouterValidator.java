package dns.demo.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiPredicate;

@Slf4j
@RefreshScope
@Component
public class RouterValidator {

    private static final BiPredicate<ServerHttpRequest, List<String>> IS_SECURED_ENDPOINT_FUNC = (request, nonSecuredEndpoints) -> {
        log.debug("Validating request[method={}, uri={}]", request.getMethod(), request.getURI());
        return nonSecuredEndpoints.stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    };

    private final List<String> openEndpoints;

    public RouterValidator(GatewayConfigProperties properties) {
        this.openEndpoints = properties.getOpenEndpoints();
    }

    public boolean isSecuredEndpoint(ServerHttpRequest serverHttpRequest) {
        return IS_SECURED_ENDPOINT_FUNC.test(serverHttpRequest, openEndpoints);
    }
}
