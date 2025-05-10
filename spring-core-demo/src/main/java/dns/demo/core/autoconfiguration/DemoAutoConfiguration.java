package dns.demo.core.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "demo.enabled", havingValue = "true")
public class DemoAutoConfiguration {

    @Bean
    public DemoBean demoBean() {
        return new DemoBean();
    }
}
