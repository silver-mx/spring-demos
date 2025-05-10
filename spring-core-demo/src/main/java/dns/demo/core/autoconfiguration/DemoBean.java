package dns.demo.core.autoconfiguration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoBean {

    @PostConstruct
    public void init() {
        log.info("DemoBean initialized");
    }
}
