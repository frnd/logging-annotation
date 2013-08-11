/*
 * The MIT License
 *
 * Copyright 2013 Fernando González.
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
import java.util.ArrayList;
import java.util.List;

/**
 * A cache for the message formatting pattern.
 *
 * @author fernando
 */
public class MessageCache {

    public enum MessageType {

        BEFORE() {
            @Override
            protected String create(Logging logAnnotation, String methodName, Annotation[][] annotations) {
                String message;
                message = logAnnotation.enterText();
                if (isDefaultMessage(logAnnotation)) {
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

            private boolean isDefaultMessage(Logging logAnnotation) {
                return Logging.DEFAULT_ENTER_TEXT.equals(logAnnotation.enterText());
            }
        },
        AFTER() {
            @Override
            protected String create(Logging logAnnotation, String methodName, Annotation[][] annotations) {
                String returnText = logAnnotation.returnText();
                return returnText;
            }
        },
        EXCEPTION() {
            @Override
            protected String create(Logging logAnnotation, String methodName, Annotation[][] annotations) {
                final String exceptionText = logAnnotation.exceptionText();
                return exceptionText;
            }
        };

        /**
         * Abstract method for creating message depending on the specific
         * pointcut.
         *
         * @param logAnnotation
         * @param call
         * @return
         */
        protected abstract String create(Logging logAnnotation, String methodName, Annotation[][] annotations);
    }

    protected String getMessage(MessageType type, Logging logAnnotation, String methodName, Annotation[][] annotations) {
        // TODO caching the messages! (call.getStaticPart().getId();)
        final String messagePattern = type.create(logAnnotation, methodName, annotations);
        return messagePattern;
    }

    protected Object[] extractArguments(Object[] args, Annotation[][] annotations) {
        List<Object> asList = new ArrayList<Object>();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] annotation = annotations[i];
            if (!containsExclude(annotation)) {
                asList.add(args[i]);
            }
        }
        return asList.toArray();
    }

    private static boolean containsExclude(Annotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            boolean exclude = LogExclude.class.equals(annotation.annotationType());
			if (exclude){
				return true;
			}
        }
        return false;
    }
}
