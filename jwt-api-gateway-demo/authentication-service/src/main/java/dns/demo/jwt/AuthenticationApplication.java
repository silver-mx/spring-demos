package dns.demo.jwt;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@Slf4j
@EnableConfigurationProperties
@SpringBootApplication
public class AuthenticationApplication {

    @Autowired
    Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @PostConstruct
    void post() {
        log.info("application.jwt.expiration=" + environment.getProperty("application.jwt.expiration"));
    }
}
