package dns.demo.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class SensitiveDataService {

    private final AtomicInteger execCounter1 = new AtomicInteger(0);
    private final AtomicInteger execCounter2 = new AtomicInteger(0);

    @Audit
    public int executeSensitiveOperation1(Map<String, String> data) {
      log.info("Executing executeSensitiveOperation1 {} times ...", execCounter1.incrementAndGet());
      return execCounter1.get();
    }

    @Audit(logArgs = false)
    public int executeSensitiveOperation2(Map<String, String> data) {
        log.info("Executing executeSensitiveOperation2 {} times ...", execCounter2.incrementAndGet());
        return execCounter2.get();
    }

    public void executeSensitiveOperation3(Map<String, String> data) {
        log.info("Executing executeSensitiveOperation3 ...");
    }

    public void executeSensitiveOperation4(Map<String, String> data) {
        log.info("Executing executeSensitiveOperation4 ...");
    }

}
