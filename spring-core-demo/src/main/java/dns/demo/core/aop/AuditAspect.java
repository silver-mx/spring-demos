package dns.demo.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Slf4j
@Component
public class AuditAspect {

    @Around(value = "dns.demo.core.aop.AuditPointcutContainer.auditAnnotationPointcut(params, service, audit)", argNames = "joinPoint,params,service,audit")
    public Object logSensitiveMethodAround(ProceedingJoinPoint joinPoint, Map<String, String> params, SensitiveDataService service, Audit audit) throws Throwable {
        log.info("Logging AOP @Around method={}.{}", service.getClass(), joinPoint.getSignature().getName());
        log.info("Should log args={}", audit.logArgs());

        if(audit.logArgs()) {
            log.info("Arguments={}", params);
        } else{
            log.info("Arguments not logged for security reasons...");
        }

        Object returnValue = joinPoint.proceed();

        log.info("Method executed correctly and returned value={}", returnValue);

        return returnValue;
    }

    @Before(value = "dns.demo.core.aop.AuditPointcutContainer.onlyExecuteSensitiveOperation3PointCut(params, service)", argNames = "joinPoint,params,service")
    public void logBefore(JoinPoint joinPoint, Map<String, String> params, SensitiveDataService service) {
        log.info("Logging AOP @Before method={}.{}", service.getClass(), joinPoint.getSignature().getName());
    }
}
