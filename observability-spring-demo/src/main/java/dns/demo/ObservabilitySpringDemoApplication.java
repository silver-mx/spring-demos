package dns.demo;

import dns.demo.client.ClientConfig;
import dns.demo.server.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.springframework.boot.WebApplicationType.NONE;
import static org.springframework.boot.WebApplicationType.SERVLET;

@Slf4j
//@SpringBootApplication
public class ObservabilitySpringDemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .parent(ParentConfig.class).web(NONE)
                .child(ClientConfig.class).web(SERVLET)
                .sibling(ServerConfig.class).web(SERVLET)
                .run(args);
    }

}
