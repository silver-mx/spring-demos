package dns.demo.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@EnableAutoConfiguration(exclude = SpringApplicationAdminJmxAutoConfiguration.class)
@PropertySource({"classpath:application-common.properties", "classpath:application-server.properties"})
@ComponentScan
@Configuration
public class ServerConfig {
}
