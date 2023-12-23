package dns.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    @Autowired
    Environment environment;
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    @PostConstruct
    void post() {
        System.out.println("prop=" + environment.getProperty("a.b"));
    }
}
