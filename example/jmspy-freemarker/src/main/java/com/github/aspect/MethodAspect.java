package com.github.aspect;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class MethodAspect {

    private static final String ALL_METHODS_IN_RESOURCES_PACKAGE = "execution(* com.github.example..*.*(..))";

    private MethodInvocationRecorder methodInvocationRecorder;

    @Autowired
    public void setMethodInvocationRecorder(MethodInvocationRecorder methodInvocationRecorder) {
        this.methodInvocationRecorder = methodInvocationRecorder;
    }

    /**
     * All methods pointcut.
     */
    @Pointcut(ALL_METHODS_IN_RESOURCES_PACKAGE)
    public void anyMethodPointcut() {
    }

    @Around("anyMethodPointcut() && @annotation(spyable)")
    public Object doCache(final ProceedingJoinPoint pjp, Spyable spyable) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return methodInvocationRecorder.record(signature.getMethod(), pjp.proceed());
    }
}
