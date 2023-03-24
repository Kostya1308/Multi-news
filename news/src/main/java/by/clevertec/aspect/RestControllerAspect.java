package by.clevertec.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class RestControllerAspect {
    @Pointcut("within(by.clevertec.controller..*) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logRequest(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] path = signature.getMethod().getAnnotation(RequestMapping.class).path();

        log.info("Request: path" + Arrays.toString(path));
    }

    @AfterReturning(value = "pointcut()", returning = "entity")
    public void logResponse(JoinPoint joinPoint, ResponseEntity<?> entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] path = signature.getMethod().getAnnotation(RequestMapping.class).path();

        log.info("Response: path:" + Arrays.toString(path) + ", return status: " + entity.getStatusCode());
    }

}
