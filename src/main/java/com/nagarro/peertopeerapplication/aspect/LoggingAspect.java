package com.nagarro.peertopeerapplication.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {
//<<<<<<< Updated upstream
////    private static final Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
////
////    @Before("execution(* com.nagarro.peertopeerapplication.services..*(..))")
////    public void logBeforeServiceMethods(JoinPoint joinPoint) {
////        logger.info("Executing: " + joinPoint.getSignature());
////    }
////
////    @After("execution(public * com.nagarro.peertopeerapplication.services.UserService.registerUser(..))")
////    public void logAfterServiceMethods(JoinPoint joinPoint) {
////        logger.info("Completed: " + joinPoint.getSignature());
////    }
//=======
////    private static final Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
////
////    @Before("execution(* com.nagarro.peertopeerapplication.services..*(..))")
////    public void logBeforeServiceMethods(JoinPoint joinPoint) {
////        logger.info("Executing: " + joinPoint.getSignature());
////    }
////
////    @After("execution(public * com.nagarro.peertopeerapplication.services.UserService.registerUser(..))")
////    public void logAfterServiceMethods(JoinPoint joinPoint) {
////        logger.info("Completed: " + joinPoint.getSignature());
////    }
//>>>>>>> Stashed changes
}
