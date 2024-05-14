package com.example.petProject.config.aspects;

import com.example.petProject.constants.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut(Constants.LOGGING_POINT_CUT)
    private void publicMethodsFromLoggingPackage() {
    }

    @Around("publicMethodsFromLoggingPackage()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        long start = System.currentTimeMillis();
        if(!pjp.getSignature().getName().equals("initBinder")) {
            logger.info(" ------ Executing method {} ------ ", methodName);
//            logger.info(" ------ Executing method {} ------ ", className);
        }
        Object output = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        if(!methodName.equals("initBinder")) {
            logger.info(" ------ Exiting method {} ::  Execution time (ms): {} ----- ", methodName, elapsedTime);
        }
        return output;
    }
}
