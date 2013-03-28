/**
 *
 */
package es.frnd.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Fernando Gonzalez de Diego <fernando@excelsit.es>
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {

    public static final String DEFAULT_TEXT = "";
    public static final String DEFAULT_EXCEPTION_TEXT = "Trowing exception {}";

    String enterText() default DEFAULT_TEXT;

    String returnText() default DEFAULT_TEXT;

    String exceptionText() default DEFAULT_EXCEPTION_TEXT;

    /**
     * The logging severity for the message.
     *
     * @return the log severity for the message.
     */
    Severity severity() default Severity.DEBUG;

    /**
     *
     * @return
     */
    boolean isDefaultEnterText() default false;
}
