package es.frnd.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts method calls and logs the appropriate messages for each of the
 * logging annotations in this package.
 *
 */
@Aspect
public class MethodLoggingAdvice {

    /**
     * Emits the log message from a {@link Logging} annotation, using the method
     * call's parameter list as the formatting parameters.
     *
     * @param call the method call being intercepted.
     * @param logAnnotation the log annotation.
     */
    @Before(value = "execution(* *(..)) and @annotation(logAnnotation)", argNames = "logAnnotation")
    public void logBefore(JoinPoint call, Logging logAnnotation) {
        Logger logger = extractLogger(call);
        if (isSeverityEnabled(logAnnotation.severity(), logger)) {
            return;
        }
        String message = MessageType.BEFORE.getMessage(logAnnotation, call);
        emit(call, message, logAnnotation.severity(),
                call.getArgs());
    }

    /**
     * Emits the log message from a {@link Logging} annotation, using the
     * method's return value as the (sole) formatting parameter.
     *
     * @param call the method call being intercepted.
     * @param logAnnotation the log annotation.
     */
    @AfterReturning(pointcut = "execution(* *(..)) and @annotation(logAnnotation)", returning = "returnValue", argNames = "logAnnotation, returnValue")
    public void logReturn(JoinPoint call, Logging logAnnotation,
            Object returnValue) {
        Logger logger = extractLogger(call);
        if (isSeverityEnabled(logAnnotation.severity(), logger)) {
            return;
        }
        String message = MessageType.AFTER.getMessage(logAnnotation, call);
        emit(call, message, logAnnotation.severity(),
                returnValue);
    }

    /**
     * Emits the log message from a {@link Logging} annotation, including the
     * method call's thrown exception.
     *
     * @param call the method call being intercepted.
     * @param logAnnotation the log annotation.
     */
    @AfterThrowing(pointcut = "execution(* *(..)) and @annotation(logAnnotation)", throwing = "exception", argNames = "logAnnotation, exception")
    public void logException(JoinPoint call, Logging logAnnotation,
            Throwable exception) {
        Logger logger = extractLogger(call);
        if (isSeverityEnabled(Severity.ERROR, logger)) {
            return;
        }
        String message = MessageType.EXCEPTION.getMessage(logAnnotation, call);
        emitException(call, message,
                logAnnotation.severity(), exception);
    }
    
    private void emit(JoinPoint call, String message, Severity severity,
            Object... values) {
        Logger logger = extractLogger(call);
        severity.log(logger, message, values);
    }
    
    private void emitException(JoinPoint call, String message,
            Severity severity, Throwable exception) {
        Logger logger = extractLogger(call);
        severity.logException(logger, message, exception);
    }
    
    private Logger extractLogger(JoinPoint call) {
        Signature signature = call.getSignature();
        Class<?> declaringType = signature.getDeclaringType();
        Logger logger = LoggerFactory.getLogger(declaringType);
        return logger;
    }
    
    private Boolean isSeverityEnabled(Severity severity, Logger logger) {
        Boolean status;
        
        switch (severity) {
            case TRACE:
                status = logger.isTraceEnabled();
                break;
            case DEBUG:
                status = logger.isDebugEnabled();
                break;
            case INFO:
                status = logger.isInfoEnabled();
                break;
            case WARN:
                status = logger.isWarnEnabled();
                break;
            case ERROR:
                status = logger.isErrorEnabled();
                break;
            default:
                status = false;
        }
        
        return status;
    }
    
    private enum MessageType {
        
        BEFORE() {
            @Override
            protected String create(Logging logAnnotation, JoinPoint call) {
                String message = logAnnotation.enterText();
                //If no message create the default message.
                if (Logging.DEFAULT_TEXT.equals(message)) {
                    message = "Calling method " + call.getSignature().getName() + " with args";
                    for (int i = 0; i < call.getArgs().length; i++) {
                        message = message.concat(" {}");
                    }
                }
                return message;
            }
        },
        AFTER() {
            @Override
            protected String create(Logging logAnnotation, JoinPoint call) {
                String message = logAnnotation.returnText();
                //If no message create the default message.
                if (Logging.DEFAULT_TEXT.equals(message)) {
                    message = "Returning method " + call.getSignature().getName() + " with {}";
                }
                return message;
            }
        },
        EXCEPTION() {
            @Override
            protected String create(Logging logAnnotation, JoinPoint call) {
                return logAnnotation.exceptionText();
            }
        };

        /**
         * Abstract methos for creating message depending on the specific
         * pointcut.
         *
         * @param logAnnotation
         * @param call
         * @return
         */
        protected abstract String create(Logging logAnnotation, JoinPoint call);
        
        protected String getMessage(Logging logAnnotation, JoinPoint call) {
            // TODO caching the messages!
            return create(logAnnotation, call);
        }
    }
}
