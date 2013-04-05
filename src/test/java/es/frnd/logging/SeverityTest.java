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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.CompareEqual;
import org.slf4j.Logger;

/**
 *
 * @author fernando
 */
public class SeverityTest {

    @Mock
    private Logger l;

    public SeverityTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class Severity.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        Severity[] expResult = new Severity[]{Severity.TRACE, Severity.DEBUG, Severity.INFO, Severity.WARN, Severity.ERROR};
        Severity[] result = Severity.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class Severity.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");

        String name;

        name = "TRACE";
        Severity expResult = Severity.TRACE;
        Severity result = Severity.valueOf(name);
        assertEquals(expResult, result);

        name = "DEBUG";
        expResult = Severity.DEBUG;
        result = Severity.valueOf(name);
        assertEquals(expResult, result);

        name = "INFO";
        expResult = Severity.INFO;
        result = Severity.valueOf(name);
        assertEquals(expResult, result);

        name = "WARN";
        expResult = Severity.WARN;
        result = Severity.valueOf(name);
        assertEquals(expResult, result);


        name = "ERROR";
        expResult = Severity.ERROR;
        result = Severity.valueOf(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of log method, of class Severity.
     */
    @Test
    public void testLog() {
        System.out.println("log");

        String message = "Calling method {} with args {} {} {}";
        String methodName = "methodName";
        Object[] parameters = new Object[]{"1", 2, 3d};
        Severity.TRACE.log(l, message, methodName, parameters);
        Severity.DEBUG.log(l, message, methodName, parameters);
        Severity.ERROR.log(l, message, methodName, parameters);
        Severity.INFO.log(l, message, methodName, parameters);
        Severity.WARN.log(l, message, methodName, parameters);

        ArgumentMatcher<String> messageMatcher = new CompareEqual<String>(message);
        ArgumentMatcher<Object[]> parametersMatcher = new ArgumentMatcher<Object[]>() {
            @Override
            public boolean matches(Object argument) {
                if (argument instanceof Object[]) {
                    Object[] args = (Object[]) argument;
                    return args.length == 4
                            && "methodName".equals(args[0])
                            && "1".equals(args[1])
                            && Integer.valueOf(2).equals(args[2])
                            && Double.valueOf(3d).equals(args[3]);
                }
                return false;
            }
        };
        Mockito.verify(l).trace(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).debug(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).error(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).info(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).warn(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
    }

    /**
     * Test of logException method, of class Severity.
     */
    @Test
    public void testLogException() {
        System.out.println("logException");
        String message = "Method {} is trowing an exception: {}";
        String methodName = "methodName";
        final Throwable exception = new Exception();
        
        Severity.TRACE.logException(l, message, methodName, exception);
        Severity.DEBUG.logException(l, message, methodName, exception);
        Severity.WARN.logException(l, message, methodName, exception);
        Severity.ERROR.logException(l, message, methodName, exception);
        Severity.INFO.logException(l, message, methodName, exception);

        ArgumentMatcher<String> messageMatcher = new CompareEqual<String>(message);
        ArgumentMatcher<Object[]> parametersMatcher = new ArgumentMatcher<Object[]>() {
            @Override
            public boolean matches(Object argument) {
                if (argument instanceof Object[]) {
                    Object[] args = (Object[]) argument;
                    return args.length == 2
                            && "methodName".equals(args[0])
                            && exception.equals(args[1]);
                }
                return false;
            }
        };
        Mockito.verify(l).trace(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).debug(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).error(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).info(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
        Mockito.verify(l).warn(Mockito.argThat(messageMatcher), Mockito.argThat(parametersMatcher));
    }

    /**
     * Test of isEnabled method, of class Severity.
     */
    @Test
    public void testIsEnabled() {
        System.out.println("isEnabled");

        Severity.DEBUG.isEnabled(l);
        Severity.TRACE.isEnabled(l);
        Severity.INFO.isEnabled(l);
        Severity.WARN.isEnabled(l);
        Severity.ERROR.isEnabled(l);
        
        Mockito.verify(l).isDebugEnabled();
        Mockito.verify(l).isTraceEnabled();
        Mockito.verify(l).isInfoEnabled();
        Mockito.verify(l).isWarnEnabled();
        Mockito.verify(l).isErrorEnabled();
    }
}
