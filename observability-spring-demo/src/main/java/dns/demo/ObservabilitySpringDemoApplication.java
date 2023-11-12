package dns.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class ObservabilitySpringDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservabilitySpringDemoApplication.class, args);
	}

}
