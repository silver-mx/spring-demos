package dns.demo.core.prototype;

import dns.demo.core.SpringCoreDemoApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class PrototypeFactoryBeanTest {

    private ApplicationContext context;
    private PrototypeFactoryBean factoryBean;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(SpringCoreDemoApplication.class);
        factoryBean = context.getBean(PrototypeFactoryBean.class);
    }


    /**
     * This test will use an interface (INTERFACES) as proxy mode
     */
    @Test
    void createPrototypeInterfaceProxyModeBean() {
        PrototypeInterface bean1 = factoryBean.createPrototypeInterfaceProxyModeBean();
        PrototypeInterface bean2 = factoryBean.createPrototypeInterfaceProxyModeBean();

        // A singleton proxy is created for each call to @Lookup, but each interaction with the proxy creates a new instance of the proxied class.
        assertThat(bean1).isSameAs(bean2);
        /* Each interaction with the proxy creates new instances.
        Calling the getter in the same object will create a new instance for each call. See the printed logs.*/
        assertThat(bean1.getUuid()).isNotEqualTo(bean1.getUuid());
    }

    /**
     * This test will use CGLIB (TARGET_CLASS) as proxy mode
     */
    @Test
    void createPrototypeCgLibProxyModeBean() {
        PrototypeCgLibProxyModeBean bean1 = factoryBean.createPrototypeCgLibProxyModeBean();
        PrototypeCgLibProxyModeBean bean2 = factoryBean.createPrototypeCgLibProxyModeBean();

        // A singleton proxy is created for each call to @Lookup, but each interaction with the proxy creates a new instance of the proxied class.
        assertThat(bean1).isSameAs(bean2);
        /* Each interaction with the proxy creates new instances.
        Calling the getter in the same object will create a new instance for each call. See the printed logs.*/
        assertThat(bean1.getUuid()).isNotEqualTo(bean1.getUuid());
    }

    /**
     * This test verifies that no proxy is created, meaning that a new instance of the prototype scope is created
     * each time @Lookup is called.
     */
    @Test
    void createPrototypeNoProxyModeBean() {
        PrototypeDefaultProxyModeBean bean1 = factoryBean.createPrototypeDefaultProxyModeBean();
        PrototypeDefaultProxyModeBean bean2 = factoryBean.createPrototypeDefaultProxyModeBean();

        // Each call to @Lookup creates a new instance of the prototype scope
        assertThat(bean1).isNotSameAs(bean2);
        // Be sure interaction with the proxy does not create new instances
        assertThat(bean1.getUuid()).isEqualTo(bean1.getUuid());
        assertThat(bean2.getUuid()).isEqualTo(bean2.getUuid());
    }

}