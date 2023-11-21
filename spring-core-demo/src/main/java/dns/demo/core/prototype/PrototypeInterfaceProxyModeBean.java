package dns.demo.core.prototype;

import dns.demo.core.singleton.SingletonUtil;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;

@Getter
@Component("interfaceProxyMode")
@Scope(value = SCOPE_PROTOTYPE, proxyMode = INTERFACES)
public class PrototypeInterfaceProxyModeBean implements PrototypeInterface {

    private final UUID uuid;

    public PrototypeInterfaceProxyModeBean(SingletonUtil singletonUtil) {
        uuid = singletonUtil.generateUUID();
        printInfo(uuid);
    }
}
