package dns.demo.core.converter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UseConverterDemo {

    private final byte[] stringToByteArrayValue;

    public UseConverterDemo(@Value("a-value") byte[] stringToByteArrayValue) {
        this.stringToByteArrayValue = stringToByteArrayValue;
    }
}
