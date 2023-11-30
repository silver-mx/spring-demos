package dns.demo.jpa.repositories;

import dns.demo.jpa.config.BlazePersistenceConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = { "dns.demo.jpa.repositories", "dns.demo.jpa.entities" })
@Import(BlazePersistenceConfig.class)
@TestConfiguration
public class JpaTestConfig {
}
