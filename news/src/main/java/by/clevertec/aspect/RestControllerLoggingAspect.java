package by.clevertec.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * Implements cross-cutting functionality for logging controller methods
 * */

@Slf4j
@Aspect
@Component
public class RestControllerLoggingAspect {

    /**
     * Defines pointcut
     */
    @Pointcut("@annotation(LogRequestResponse)")
    public void pointcut() {
    }

    /**
     * Called before the method marked @RequestMapping for logging it
     *
     * @param joinPoint Reflecting the access to the state available at the given connection point
     */
    @Before("pointcut()")
    public void logRequest(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] path = signature.getMethod().getAnnotation(RequestMapping.class).path();

        log.info("Request: path" + Arrays.toString(path));
    }

    /**
     * Called after returning the method marked @RequestMapping for logging it
     *
     * @param joinPoint Reflecting the access to the state available at the given connection point
     * @param entity entity of response
     */
    @AfterReturning(value = "pointcut()", returning = "entity")
    public void logResponse(JoinPoint joinPoint, ResponseEntity<?> entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] path = signature.getMethod().getAnnotation(RequestMapping.class).path();

        log.info("Response: path:" + Arrays.toString(path) + ", return status: " + entity.getStatusCode());
    }

    /**
     * Called after throwing the exception for logging it
     *
     * @param joinPoint Reflecting the access to the state available at the given connection point
     * @param exception exception
     */
    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] path = signature.getMethod().getAnnotation(RequestMapping.class).path();

        log.info("Response: path:" + Arrays.toString(path) + ", exception: " + exception.getClass().toString());
    }

}
