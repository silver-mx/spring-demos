package dns.demo.core.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConvertersConfig.class)
class UseConverterDemoTest {

    @Autowired
    private UseConverterDemo converterDemo;

    @Autowired
    private StringToByteArrayConverter stringToByteArrayConverter;

    @Test
    void testConverter() {
        assertThat(converterDemo.getStringToByteArrayValue()).isEqualTo(new byte[]{97, 45, 118, 97, 108, 117, 101});
        assertThat(stringToByteArrayConverter.getCounter().get()).isEqualTo(1);
    }
}