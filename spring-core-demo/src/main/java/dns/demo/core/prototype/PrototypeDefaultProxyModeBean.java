package dns.demo.core.prototype;

import dns.demo.core.singleton.SingletonUtil;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Getter
@Component
@Scope(value = SCOPE_PROTOTYPE)
public class PrototypeDefaultProxyModeBean implements PrototypeInterface {

    private final UUID uuid;
    public PrototypeDefaultProxyModeBean(SingletonUtil singletonUtil) {
        uuid = singletonUtil.generateUUID();
        printInfo(uuid);
    }

}
