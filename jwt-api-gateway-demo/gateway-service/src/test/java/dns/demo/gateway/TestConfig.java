package dns.demo.gateway;

import dns.demo.gateway.config.WebClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.ServiceInstanceListSuppliers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import static java.util.Objects.requireNonNull;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@LoadBalancerClient(name = "auth-service")
@Import({GatewayApplication.class, WebClientConfig.class})
public class TestConfig {

    /* For the load balanced web client configuration that otherwise would use
     * the config fetch from the discovery client.
     * */
    //@Primary
    @Bean
    public ServiceInstanceListSupplier fixedServiceInstanceListSupplier(Environment env) {
        String authServiceUrl = env.getProperty("auth-service.url");
        log.info("URL for auth-service={}", authServiceUrl);
        int authServicePort = Integer.parseInt(requireNonNull(authServiceUrl).substring(authServiceUrl.lastIndexOf(":") + 1));
        return ServiceInstanceListSuppliers.from("auth-service",
                new DefaultServiceInstance("wiremock:auth-service:" + authServicePort, "auth-service-1", "localhost", authServicePort, false));
    }

}



