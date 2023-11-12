package dns.demo.server;

import dns.demo.common.CommonConfig;
import dns.demo.server.post.JsonPlaceHolderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.tracing.OpenTelemetryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@EnableAutoConfiguration(exclude = OpenTelemetryAutoConfiguration.class)
@PropertySource("classpath:application-server.properties")
@ComponentScan
@Import(CommonConfig.class)
@Configuration(proxyBeanMethods = false)
public class ServerConfig {

    @Bean
    JsonPlaceHolderClient jsonPlaceHolderClient() {
        RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(JsonPlaceHolderClient.class);
    }

}
