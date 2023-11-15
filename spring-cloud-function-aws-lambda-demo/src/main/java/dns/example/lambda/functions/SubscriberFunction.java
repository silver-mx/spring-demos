package dns.example.lambda.functions;

import dns.example.lambda.model.Subscriber;
import dns.example.lambda.model.SubscriberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class SubscriberFunction {

    private final SubscriberService subscriberService;

    public SubscriberFunction(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Bean
    public Consumer<String> create() {
        return subscriberService::create;
    }

    @Bean
    public Supplier<List<Subscriber>> findAll() {
        return subscriberService::findAll;
    }
}
