package dns.demo.core.singleton;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SingletonUtil {

    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
