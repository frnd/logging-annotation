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
		void log(Logger l, String message, Object[] parameters) {
			l.trace(message, parameters);
		}

		@Override
		void logException(Logger l, String message, Throwable exception) {
			l.trace(message, exception);
		}
	},
	/**
	 * Log messages will be emitted using the {@link Logger#debug(String)}
	 * variants.
	 */
	DEBUG() {
		@Override
		void log(Logger l, String message, Object[] parameters) {
			l.debug(message, parameters);
		}

		@Override
		void logException(Logger l, String message, Throwable exception) {
			l.debug(message, exception);
		}
	},
	/**
	 * Log messages will be emitted using the {@link Logger#info(String)}
	 * variants.
	 */
	INFO {
		@Override
		void log(Logger l, String message, Object[] parameters) {
			l.info(message, parameters);
		}

		@Override
		void logException(Logger l, String message, Throwable exception) {
			l.info(message, exception);
		}
	},
	/**
	 * Log messages will be emitted using the {@link Logger#warn(String)}
	 * variants.
	 */
	WARN {
		@Override
		void log(Logger l, String message, Object[] parameters) {
			l.warn(message, parameters);
		}

		@Override
		void logException(Logger l, String message, Throwable exception) {
			l.warn(message, exception);
		}
	},
	/**
	 * Log messages will be emitted using the {@link Logger#error(String)}
	 * variants.
	 */
	ERROR {
		@Override
		void log(Logger l, String message, Object[] parameters) {
			l.error(message, parameters);
		}

		@Override
		void logException(Logger l, String message, Throwable exception) {
			l.error(message, exception);
		}
	};

	/**
	 * Log a normal message at the appropriate level.
	 * 
	 * @param l
	 *            the logger to emit messages to.
	 * @param message
	 *            the log message (template) to emit.
	 * @param parameters
	 *            values to fill the message template with.
	 */
	abstract void log(Logger l, String message, Object[] parameters);

	/**
	 * Log an exceptional message at the appropriate level.
	 * 
	 * @param l
	 *            the logger to emit messages to.
	 * @param message
	 *            the log message to emit.
	 * @param exception
	 *            the exception to log.
	 */
	abstract void logException(Logger l, String message, Throwable exception);
}
