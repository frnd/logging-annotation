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
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author fernando
 */
public class MessageCacheTest {

    MessageCache instance = new MessageCache();
    Annotation exclude = new LogExclude() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return LogExclude.class;
        }
    };
    Logging defaultLogAnnotation = new Logging() {
        @Override
        public String enterText() {
            return null;
        }

        @Override
        public String returnText() {
            return DEFAULT_RETURN_TEXT;
        }

        @Override
        public String exceptionText() {
            return DEFAULT_EXCEPTION_TEXT;
        }

        @Override
        public Severity severity() {
            return Severity.INFO;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Logging.class;
        }
    };
    Logging customMessagesLogAnnotation = new Logging() {
        @Override
        public String enterText() {
            return "The method was caled.";
        }

        @Override
        public String returnText() {
            return "Return";
        }

        @Override
        public String exceptionText() {
            return "Exception";
        }

        @Override
        public Severity severity() {
            return Severity.INFO;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Logging.class;
        }
    };

    public MessageCacheTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMessage method, of class MessageCache.
     */
    @Test
    public void testGetMessage() {
        MessageType type;
        String methodName;
        Annotation[][] annotations;

        System.out.println("getMessage");

        type = MessageType.BEFORE;
        methodName = "methodName";
        annotations = new Annotation[][]{{}, {exclude}, {}, {}};
        String expResult = "Calling method {} with args {} {} {}";

        String result = instance.getMessage(type, defaultLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);

        type = MessageType.BEFORE;
        methodName = "methodExcludeAll";
        annotations = new Annotation[][]{{exclude}, {exclude}, {exclude}, {exclude}};
        expResult = "Calling method {} with args";

        result = instance.getMessage(type, defaultLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);

        type = MessageType.AFTER;
        methodName = "methodName";
        annotations = new Annotation[][]{{exclude}, {exclude}, {exclude}, {exclude}};
        expResult = "Returning method {} with {}";

        result = instance.getMessage(type, defaultLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);

        type = MessageType.EXCEPTION;
        methodName = "methodName";
        annotations = new Annotation[][]{{exclude}, {exclude}, {exclude}, {exclude}};
        expResult = "Method {} is trowing an exception: {}";

        result = instance.getMessage(type, defaultLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);
        
        
        
        type = MessageType.BEFORE;
        methodName = "methodName";
        annotations = new Annotation[][]{{}, {exclude}, {}, {}};
        expResult = "The method was caled.";

        result = instance.getMessage(type, customMessagesLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);

        type = MessageType.AFTER;
        methodName = "methodExcludeAll";
        annotations = new Annotation[][]{{exclude}, {exclude}, {exclude}, {exclude}};
        expResult = "Return";

        result = instance.getMessage(type, customMessagesLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);

        type = MessageType.EXCEPTION;
        methodName = "methodName";
        annotations = new Annotation[][]{{exclude}, {exclude}, {exclude}, {exclude}};
        expResult = "Exception";

        result = instance.getMessage(type, customMessagesLogAnnotation, methodName, annotations);

        assertEquals(expResult, result);
    }

    /**
     * Test of extractArguments method, of class MessageCache.
     */
    @Test
    public void testExtractArguments() {
        Object[] args;
        Annotation[][] annotations;
        Object[] expResult;
        Object[] result;

        System.out.println("extractArguments");

        args = new Object[]{"1", "2", "3", "4"};
        annotations = new Annotation[][]{{}, {exclude}, {}, {}};
        expResult = new Object[]{"1", "3", "4"};

        result = instance.extractArguments(args, annotations);

        assertArrayEquals(expResult, result);

        args = new Object[]{"1", "2", "3", "4"};
        annotations = new Annotation[][]{{exclude}, {exclude}, {exclude}, {exclude}};
        expResult = new Object[]{};

        result = instance.extractArguments(args, annotations);

        assertArrayEquals(expResult, result);
    }
}
