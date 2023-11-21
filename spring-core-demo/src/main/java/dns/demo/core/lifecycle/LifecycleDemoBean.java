package dns.demo.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Slf4j
public class LifecycleDemoBean implements InitializingBean, DisposableBean {
    private DependencyBean depBean;

    public LifecycleDemoBean() {
        log.info("Stage 1: Creating instance of the bean");
    }

    @Autowired
    public void setDepBean(DependencyBean depBean) {
        log.info("Stage 2: Injecting dependencies");
        this.depBean = depBean;
    }

    public void initBean() {
        log.info("Stage 3: Initialization via @Bean");
    }

    @PostConstruct
    public void initPostConstruct() {
        log.info("Stage 3: Initialization via @PostConstruct");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("Stage 3: Initialization via InitializingBean.afterPropertiesSet()");
    }

    public void destroyBean() {
        log.info("Stage 4: Destroy via @Bean");
    }

    @PreDestroy
    public void destroyPreDestroy() {
        log.info("Stage 4: Destroy via @PreDestroy");
    }

    @Override
    public void destroy() {
        log.info("Stage 4: Destroy via DisposableBean.destroy()");
    }
}
