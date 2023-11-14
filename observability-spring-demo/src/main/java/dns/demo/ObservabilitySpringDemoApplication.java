package dns.demo;

import dns.demo.client.ClientConfig;
import dns.demo.server.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.WebApplicationType.NONE;
import static org.springframework.boot.WebApplicationType.SERVLET;

@Slf4j
public class ObservabilitySpringDemoApplication {

    public static void main(String[] args) {
        runClientAndServerApplications(args);
    }

    //@VisibilityForTesting
    static ConfigurableApplicationContext runClientAndServerApplications(String[] args) {
        return new SpringApplicationBuilder()
                .parent(ParentConfig.class).web(NONE)
                .child(ServerConfig.class).web(SERVLET)
                .sibling(ClientConfig.class).web(SERVLET)
                .run(args);
    }

}
