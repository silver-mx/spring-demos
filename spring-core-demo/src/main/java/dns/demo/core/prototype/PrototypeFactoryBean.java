package dns.demo.core.prototype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrototypeFactoryBean {

    public PrototypeFactoryBean() {
    }

    @Lookup("interfaceProxyMode")
    public PrototypeInterface createPrototypeInterfaceProxyModeBean() {
        throw new UnsupportedOperationException("Spring will proxy the call and therefore this implementation must never be called");
    }

    @Lookup
    public PrototypeCgLibProxyModeBean createPrototypeCgLibProxyModeBean() {
        throw new UnsupportedOperationException("Spring will proxy the call and therefore this implementation must never be called");
    }

    @Lookup
    public PrototypeDefaultProxyModeBean createPrototypeDefaultProxyModeBean() {
        throw new UnsupportedOperationException("Spring will proxy the call and therefore this implementation must never be called");
    }
}
