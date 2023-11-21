package dns.demo.core.lifecycle;

import dns.demo.core.SpringCoreDemoApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LifecycleDemoBeanTest {

    private ConfigurableApplicationContext context;
    private LifecycleDemoBean lifecycleDemoBean;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(SpringCoreDemoApplication.class);
        lifecycleDemoBean = context.getBean(LifecycleDemoBean.class);
    }

    /**
     * The log must show something like:
     * <p>
     * 14:17:34.274 [main] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 1: Creating instance of the bean
     * 14:17:34.286 [main] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 2: Injecting dependencies
     * 14:17:34.287 [main] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 3: Initialization via @PostConstruct
     * 14:17:34.287 [main] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 3: Initialization via InitializingBean.afterPropertiesSet()
     * 14:17:34.288 [main] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 3: Initialization via @Bean
     * 14:17:34.564 [SpringContextShutdownHook] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 4: Destroy via @PreDestroy
     * 14:17:34.564 [SpringContextShutdownHook] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 4: Destroy via DisposableBean.destroy()
     * 14:17:34.564 [SpringContextShutdownHook] INFO dns.demo.core.lifecycle.LifecycleDemoBean -- Stage 4: Destroy via @Bean
     */
    @Test
    void lifecycle() {
        context.registerShutdownHook();// Tell Spring context to shut down and dispose all beans
        assertNotNull(lifecycleDemoBean);
        assertNotNull(lifecycleDemoBean.getDepBean());
    }

}