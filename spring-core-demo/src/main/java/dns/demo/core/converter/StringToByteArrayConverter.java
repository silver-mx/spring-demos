package dns.demo.core.converter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
@Component
public class StringToByteArrayConverter implements Converter<String, byte[]> {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public byte[] convert(String source) {
        byte[] convertedValue = source.getBytes(StandardCharsets.UTF_8);
        log.info("Converting {} to {}", source, convertedValue);
        counter.incrementAndGet();
        return convertedValue;
    }
}
