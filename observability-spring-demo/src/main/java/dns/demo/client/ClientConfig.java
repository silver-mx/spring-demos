package dns.demo.client;

import dns.demo.client.post.PostClient;
import dns.demo.common.CommonConfig;
import dns.demo.common.post.Post;
import dns.demo.server.ServerConfig;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.tracing.BraveAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@EnableAutoConfiguration(exclude = {BraveAutoConfiguration.class, SpringApplicationAdminJmxAutoConfiguration.class})
@PropertySource("classpath:application-client.properties")
@ComponentScan
@Import(CommonConfig.class)
@Configuration(proxyBeanMethods = false)
public class ClientConfig {

    public static final String POST_SERVICE_FIND_ALL_CONTEXT_NAME = "posts.load-all-posts";

    @Bean
    PostClient postClient(ObservationRegistry observationRegistry) {
        RestClient restClient = RestClient.builder().baseUrl("http://localhost:8081/api/").requestInterceptor((request, body, execution) -> {
                    if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attribs
                            && nonNull(attribs.getRequest().getHeader(AUTHORIZATION))) {
                        String authValue = attribs.getRequest().getHeader(AUTHORIZATION);
                        request.getHeaders().set(AUTHORIZATION, authValue);
                        log.info("Instrumenting server call with current request authorization[{}]", authValue);
                    } else {
                        request.getHeaders().setBasicAuth("user", "password");
                        log.info("Instrumenting server call with default authorization");
                    }

                    return execution.execute(request, body);
                })
                .observationRegistry(observationRegistry)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(PostClient.class);
    }

    @Bean
    // The @Observed annotation requires "spring-boot-starter-aop" (SpringBoot 3.2)
    @Observed(name = POST_SERVICE_FIND_ALL_CONTEXT_NAME, contextualName = "post-service.find-all", lowCardinalityKeyValues = {"userType", "clientUserType"})
    CommandLineRunner commandLineRunner(PostClient postClient, ObservationRegistry observationRegistry) {
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

            List<Post> posts = postClient.findAll();
            log.info("All posts size={}", posts.size());
        };
    }
}
