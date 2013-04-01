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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fernando
 */
public enum MessageCache {

    BEFORE() {
        @Override
        protected String create(Logging logAnnotation, String methodName, Annotation[][] annotations) {
            String message = logAnnotation.enterText();
            //If no message create the default message.
            if (Logging.DEFAULT_TEXT.equals(message)) {
                message = "Calling method " + methodName + " with args";
                message = message.concat(extractParams(annotations));
            }
            return message;
        }

        private String extractParams(Annotation[][] annotations) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < annotations.length; i++) {
                if (!containsExclude(annotations[i])) {
                    buffer.append(" {}");
                }
            }
            return buffer.toString();
        }

        private boolean containsExclude(Annotation[] annotations) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
                if (LogExclude.class.isInstance(annotation)) {
                    return true;
                }
            }
            return false;
        }
    },
    AFTER() {
        @Override
        protected String create(Logging logAnnotation, String methodName, Annotation[][] annotations) {
            String message = logAnnotation.returnText();
            //If no message create the default message.
            if (Logging.DEFAULT_TEXT.equals(message)) {
                message = "Returning method " + methodName + " with {}";
            }
            return message;
        }
    },
    EXCEPTION() {
        @Override
        protected String create(Logging logAnnotation, String methodName, Annotation[][] annotations) {
            return logAnnotation.exceptionText();
        }
    };

    public static Object[] extractArguments(Object[] args, Annotation[][] annotations) {
        List<Object> asList = Arrays.asList(args);
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] annotation = annotations[i];
            if (LogExclude.class.isInstance(annotation)) {
                asList.remove(i);
            }
        }
        return asList.toArray();
    }

    /**
     * Abstract methos for creating message depending on the specific pointcut.
     *
     * @param logAnnotation
     * @param call
     * @return
     */
    protected abstract String create(Logging logAnnotation, String methodName, Annotation[][] annotations);

    protected String getMessage(Logging logAnnotation, String methodName, Annotation[][] annotations) {
        // TODO caching the messages! (call.getStaticPart().getId();)
        return create(logAnnotation, methodName, annotations);
    }
}
