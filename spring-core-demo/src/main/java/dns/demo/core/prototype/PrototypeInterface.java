package dns.demo.core.prototype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public interface PrototypeInterface {

    Logger log = LoggerFactory.getLogger(PrototypeInterface.class);

    default void printInfo(UUID uuid) {
        log.info("Creating instance of {} with UUID {}", this, uuid);
    }
    UUID getUuid();
}
