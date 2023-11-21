package dns.demo.core.prototype;

import dns.demo.core.singleton.SingletonUtil;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Getter
@Component
@Scope(value = SCOPE_PROTOTYPE, proxyMode = TARGET_CLASS)
public class PrototypeCgLibProxyModeBean implements PrototypeInterface {

    private final UUID uuid;

    public PrototypeCgLibProxyModeBean(SingletonUtil singletonUtil) {
        uuid = singletonUtil.generateUUID();
        printInfo(uuid);
    }

}
