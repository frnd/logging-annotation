/*
 * The MIT License
 *
 * Copyright 2013 fernando.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package es.frnd.logging;

import es.frnd.logging.MessageCache.MessageType;
import java.lang.annotation.Annotation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts method calls and logs the appropriate messages for each of the
 * logging annotations in this package.
 *
 */
@Aspect
public class MethodLoggingAdvice {

    MessageCache messageCache = new MessageCache();

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
        final Severity severity = logAnnotation.severity();
        if (!severity.isEnabled(logger)) {
            return;
        }
        MethodSignature signature = (MethodSignature) call.getSignature();
        Annotation[][] annotations = signature.getMethod().getParameterAnnotations();
        String methodName = signature.getName();
        String message = messageCache.getMessage(MessageType.BEFORE, logAnnotation, methodName, annotations);
        final Object[] args = messageCache.extractArguments(call.getArgs(), annotations);
        severity.log(logger, message, args);
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
        final Severity severity = logAnnotation.severity();
        if (!severity.isEnabled(logger)) {
            return;
        }
        MethodSignature signature = (MethodSignature) call.getSignature();
        Annotation[][] annotations = signature.getMethod().getParameterAnnotations();
        String methodName = signature.getName();
        String message = messageCache.getMessage(MessageType.AFTER, logAnnotation, methodName, annotations);
        severity.log(logger, message, returnValue);
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
        final Severity severity = logAnnotation.severity();
        if (!severity.isEnabled(logger)) {
            return;
        }
        MethodSignature signature = (MethodSignature) call.getSignature();
        Annotation[][] annotations = signature.getMethod().getParameterAnnotations();
        String methodName = signature.getName();
        String message = messageCache.getMessage(MessageType.EXCEPTION, logAnnotation, methodName, annotations);
        severity.logException(logger, message, exception);
    }

    /**
     * Extract the logger for the specific class of the poincut
     *
     * @param call
     * @return
     */
    private Logger extractLogger(JoinPoint call) {
        Class<?> declaringType;
        Signature signature;
        Logger logger;

        signature = call.getSignature();
        declaringType = signature.getDeclaringType();
        logger = LoggerFactory.getLogger(declaringType);

        return logger;
    }
}
