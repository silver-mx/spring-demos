package dns.demo.core.aop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
@ContextConfiguration(classes = AopConfig.class)
class AuditAspectTest {

    @Autowired
    private SensitiveDataService service;

    @Test
    void logAroundSensitiveMethodWithArgs(CapturedOutput capturedOutput) {
        var params = Map.of("key1", "value1");
        service.executeSensitiveOperation1(params);
        assertThat(capturedOutput).contains("Logging AOP @Around method=class dns.demo.core.aop.SensitiveDataService.executeSensitiveOperation1");
        assertThat(capturedOutput).contains("Should log args=true");
        assertThat(capturedOutput).contains("Arguments=" + params);
    }

    @Test
    void logAroundSensitiveMethodWithoutArgs(CapturedOutput capturedOutput) {
        var params = Map.of("key2", "value2");
        service.executeSensitiveOperation2(params);
        assertThat(capturedOutput).contains("Logging AOP @Around method=class dns.demo.core.aop.SensitiveDataService.executeSensitiveOperation2");
        assertThat(capturedOutput).contains("Should log args=false");
        assertThat(capturedOutput).contains("Arguments not logged for security reasons...");
    }

    @Test
    void logBefore(CapturedOutput capturedOutput) {
        var params = Map.of("key3", "value3");
        service.executeSensitiveOperation3(params);
        assertThat(capturedOutput).contains("Logging AOP @Before method=class dns.demo.core.aop.SensitiveDataService.executeSensitiveOperation3");
    }

    @Test
    void logNothing(CapturedOutput capturedOutput) {
        var params = Map.of("key4", "value4");
        service.executeSensitiveOperation4(params);
        assertThat(capturedOutput).contains("Executing executeSensitiveOperation4 ...");
        String[] lines = capturedOutput.getAll().split("\n");
        long count = Arrays.stream(lines).filter(line -> line.contains("dns.demo.core.aop")).count();
        assertThat(count).isEqualTo(1);
    }
}