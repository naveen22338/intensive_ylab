package by.eugenekulik.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Aspect
@Slf4j
@Service
public class LoggingAspect {


    @Pointcut("@annotation(by.eugenekulik.service.annotation.Loggable)")
    public void callLoggableMethod() {
    }

    @Around("callLoggableMethod()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder();
        builder.append("class: ").append(joinPoint.getSignature().getDeclaringType().getName());
        builder.append(", method: ").append(joinPoint.getSignature().getName());
        builder.append(", params: ");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
        log.info("before: {}", builder);
        Object retVal = joinPoint.proceed();
        log.info("after: {}", builder);
        return retVal;
    }
}
