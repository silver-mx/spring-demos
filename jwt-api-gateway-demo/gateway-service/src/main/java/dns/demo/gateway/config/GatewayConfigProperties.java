package dns.demo.gateway.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class GatewayConfigProperties {

    @NotEmpty
    private Map<ServiceName, String> serviceNames;

    @NotEmpty
    private List<String> openEndpoints;
}
