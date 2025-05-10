package dns.demo.core.autoconfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import static org.assertj.core.api.Assertions.assertThat;

class DemoAutoConfigurationTest {

    @Test
    void demoBeanCreatedWithPropertiesFiles() {
        new ApplicationContextRunner()
                .withInitializer(applicationContext -> {
                    try {
                        applicationContext.getEnvironment().getPropertySources().addLast(
                                new ResourcePropertySource(new ClassPathResource("application-demo.properties")));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .withConfiguration(AutoConfigurations.of(DemoAutoConfiguration.class))
                .run(context -> assertThat(context).hasBean("demoBean"));
    }

    @Test
    void demoBeanCreatedWithPropertyTrue() {
        new ApplicationContextRunner()
                .withPropertyValues("demo.enabled=true")
                .withConfiguration(AutoConfigurations.of(DemoAutoConfiguration.class))
                .run(context -> assertThat(context).hasBean("demoBean"));
    }

    @Test
    void demoBeanNotCreatedWithPropertyFalse() {
        new ApplicationContextRunner()
                .withPropertyValues("demo.enabled=false")
                .withConfiguration(AutoConfigurations.of(DemoAutoConfiguration.class))
                .run(context -> assertThat(context).doesNotHaveBean("demoBean"));
    }

    @Test
    void demoBeanNotCreatedWithPropertyMissing() {
        new ApplicationContextRunner()
                .withPropertyValues("demo.enabled=false")
                .withConfiguration(AutoConfigurations.of(DemoAutoConfiguration.class))
                .run(context -> assertThat(context).doesNotHaveBean("demoBean"));
    }
}