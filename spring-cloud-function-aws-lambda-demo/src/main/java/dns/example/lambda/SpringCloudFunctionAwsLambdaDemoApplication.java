package dns.example.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class SpringCloudFunctionAwsLambdaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFunctionAwsLambdaDemoApplication.class, args);
	}

	@Bean
	Function<String, String> uppercase() {
		return value -> value.toUpperCase(Locale.getDefault());
	}

	@Bean
	Supplier<String> hello() {
		return () -> "Hello Spring Cloud Function!";
	}

}
