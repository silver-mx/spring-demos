package dns.demo.common.observability;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static dns.demo.client.ClientConfig.POST_SERVICE_FIND_ALL_CONTEXT_NAME;

/**
 * Example of plugging in a custom handler that will print logs for the different observation events/hooks
 **/
@Slf4j
@Component
public class ObservabilityHandler implements ObservationHandler<Observation.Context> {
    @Override
    public void onStart(Observation.Context context) {
        log.info("Before running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
    }

    @Override
    public void onError(Observation.Context context) {
        //log.info("An error happened for context [{}]", context.getName());
    }

    @Override
    public void onEvent(Observation.Event event, Observation.Context context) {
        //log.info("Event [{}] occurred for context [{}]", event.getName(), context.getName());
    }

    @Override
    public void onScopeOpened(Observation.Context context) {
        //log.info("Scope opened for context [{}]", context.getName());
    }

    @Override
    public void onScopeClosed(Observation.Context context) {
        //log.info("Scope closed for context [{}]", context.getName());
    }

    @Override
    public void onScopeReset(Observation.Context context) {
        //log.info("Scope resetted for context [{}]", context.getName());
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("After running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return POST_SERVICE_FIND_ALL_CONTEXT_NAME.equals(context.getName());
    }

    private String getUserTypeFromContext(Observation.Context context) {
        return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                //.peek(keyValue -> log.debug("keyValue={}", keyValue))
                .filter(keyValue -> "userType".equals(keyValue.getKey()))
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN");
    }
}
