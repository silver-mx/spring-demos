package dns.demo.config;

import dns.demo.post.JsonPlaceHolderService;
import dns.demo.post.Post;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    JsonPlaceHolderService jsonPlaceHolderService() {
        RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(JsonPlaceHolderService.class);
    }

    @Bean
    // The @Observed annotation requires "spring-boot-starter-aop" (SpringBoot 3.2)
    @Observed(name = "posts.load-all-posts", contextualName = "post-service.find-all", lowCardinalityKeyValues = {"locale", "sv-SE"})
    CommandLineRunner commandLineRunner(JsonPlaceHolderService jsonPlaceHolderService, ObservationRegistry observationRegistry) {
        // Add observability manually (this will be done automatically in SpringBoot 3.2 stable)
        return args -> {
            // See docs: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.observability
            // This is a manual version.
            /*Observation.createNotStarted("posts.load-all-posts", observationRegistry)
                    .lowCardinalityKeyValue("locale", "sv-SE")
                    .highCardinalityKeyValue("userId", "silver-mx")
                    .contextualName("post-service.find-all")
                    .observe(() -> {
                        List<Post> posts = jsonPlaceHolderService.findAll();
                        log.info("All posts size={}", posts.size());
                    });*/

            List<Post> posts = jsonPlaceHolderService.findAll();
            log.info("All posts size={}", posts.size());
        };
    }
}
