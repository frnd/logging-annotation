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

import org.slf4j.Logger;

/**
 * Indicates the severity of the message used by {@link Logging}.
 */
public enum Severity {

    /**
     * Log messages will be emitted using the {@link Logger#trace(String)}
     * variants.
     */
    TRACE() {
        @Override
        void log(Logger l, String message, Object... parameters) {
            l.trace(message, parameters);
        }

        @Override
        void logException(Logger l, String message, Throwable exception) {
            l.trace(message, exception);
        }

        @Override
        boolean isEnabled(Logger l) {
            return l.isTraceEnabled();
        }
    },
    /**
     * Log messages will be emitted using the {@link Logger#debug(String)}
     * variants.
     */
    DEBUG() {
        @Override
        void log(Logger l, String message, Object... parameters) {
            l.debug(message, parameters);
        }

        @Override
        void logException(Logger l, String message, Throwable exception) {
            l.debug(message, exception);
        }
        
        @Override
        boolean isEnabled(Logger l) {
            return l.isDebugEnabled();
        }
    },
    /**
     * Log messages will be emitted using the {@link Logger#info(String)}
     * variants.
     */
    INFO {
        @Override
        void log(Logger l, String message, Object... parameters) {
            l.info(message, parameters);
        }

        @Override
        void logException(Logger l, String message, Throwable exception) {
            l.info(message, exception);
        }
        
        @Override
        boolean isEnabled(Logger l) {
            return l.isInfoEnabled();
        }
    },
    /**
     * Log messages will be emitted using the {@link Logger#warn(String)}
     * variants.
     */
    WARN {
        @Override
        void log(Logger l, String message, Object... parameters) {
            l.warn(message, parameters);
        }

        @Override
        void logException(Logger l, String message, Throwable exception) {
            l.warn(message, exception);
        }
        
        @Override
        boolean isEnabled(Logger l) {
            return l.isWarnEnabled();
        }
    },
    /**
     * Log messages will be emitted using the {@link Logger#error(String)}
     * variants.
     */
    ERROR {
        @Override
        void log(Logger l, String message, Object... parameters) {
            l.error(message, parameters);
        }

        @Override
        void logException(Logger l, String message, Throwable exception) {
            l.error(message, exception);
        }
        
        @Override
        boolean isEnabled(Logger l) {
            return l.isErrorEnabled();
        }
    };

    /**
     * Log a normal message at the appropriate level.
     *
     * @param l the logger to emit messages to.
     * @param message the log message (template) to emit.
     * @param parameters values to fill the message template with.
     */
    abstract void log(Logger l, String message, Object... parameters);

    /**
     * Log an exceptional message at the appropriate level.
     *
     * @param l the logger to emit messages to.
     * @param message the log message to emit.
     * @param exception the exception to log.
     */
    abstract void logException(Logger l, String message, Throwable exception);

    /**
     * Test if this severiti is enabled in the logger
     * @param l the logger to check the severity.
     * @return 
     */
    abstract boolean isEnabled(Logger l);
}
