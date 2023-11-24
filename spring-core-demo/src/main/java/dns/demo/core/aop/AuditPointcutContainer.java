package dns.demo.core.aop;

import org.aspectj.lang.annotation.Pointcut;

import java.util.Map;

public class AuditPointcutContainer {

    @Pointcut(value = "@annotation(dns.demo.core.aop.Audit) && args(params) && target(service) && @annotation(audit)", argNames = "params,service,audit")
    public void auditAnnotationPointcut(Map<String, String> params, SensitiveDataService service, Audit audit) {

    }

    @Pointcut(value = "execution(public * dns.demo.core.aop.*Service*.executeSensitiveOperation3(*)) && args(params) && target(service) && bean(sensitiveDataService)", argNames = "params,service")
    public void onlyExecuteSensitiveOperation3PointCut(Map<String, String> params, SensitiveDataService service) {

    }
}
