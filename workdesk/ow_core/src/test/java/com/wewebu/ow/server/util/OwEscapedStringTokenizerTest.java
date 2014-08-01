package com.wewebu.ow.server.util;

import java.util.Vector;

import junit.framework.TestCase;

/**
 *<p>
 * Tests the OwEscapedStringTokenizer utility class.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public class OwEscapedStringTokenizerTest extends TestCase
{
    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /** test the appender function: appendDelimiter with standard chars
     * 
     */
    public void testAppendDelimiter1()
    {
        char testDelimiter = OwEscapedStringTokenizer.STANDARD_DELIMITER;
        char testEscape = OwEscapedStringTokenizer.STANDARD_ESCAPE;

        String testString = "ABCD";
        testString += testDelimiter;
        testString += "efgh";
        testString += testEscape;
        testString += "01234";
        testString += testEscape;
        testString += testDelimiter;
        testString += "56789";

        String expectedString = "ABCD";
        expectedString += testEscape;
        expectedString += testDelimiter;
        expectedString += "efgh";
        expectedString += testEscape;
        expectedString += testEscape;
        expectedString += "01234";
        expectedString += testEscape;
        expectedString += testEscape;
        expectedString += testEscape;
        expectedString += testDelimiter;
        expectedString += "56789";

        // create token string
        Vector tokens = new Vector();

        // test the appender function, should convert the text so it can be tokenized later
        tokens.add(testString);

        String result = OwEscapedStringTokenizer.createDelimitedString(tokens);

        assertEquals(expectedString, result);
    }

    /** test the appender function: appendDelimiter with custom delimiter, excape chars
     * 
     */
    public void testAppendDelimiter2()
    {
        char testDelimiter = '&';
        char testEscape = '?';

        String testString = "ABCD";
        testString += testDelimiter;
        testString += "efgh";
        testString += testEscape;
        testString += "01234";
        testString += testEscape;
        testString += testDelimiter;
        testString += "56789";

        String expectedString = "ABCD";
        expectedString += testEscape;
        expectedString += testDelimiter;
        expectedString += "efgh";
        expectedString += testEscape;
        expectedString += testEscape;
        expectedString += "01234";
        expectedString += testEscape;
        expectedString += testEscape;
        expectedString += testEscape;
        expectedString += testDelimiter;
        expectedString += "56789";

        // create token string
        Vector tokens = new Vector();

        // test the appender function, should convert the text so it can be tokenized later
        tokens.add(testString);

        String result = OwEscapedStringTokenizer.createDelimitedString(tokens, testDelimiter, testEscape);

        assertEquals(expectedString, result);
    }

    /** test the tokenizer functionality
     * 
     */
    public void testTokenizer1()
    {
        char testDelimiter = OwEscapedStringTokenizer.STANDARD_DELIMITER;
        char testEscape = OwEscapedStringTokenizer.STANDARD_ESCAPE;

        String testString1 = "ABCD";
        testString1 += testDelimiter;
        testString1 += "efgh";
        testString1 += testEscape;
        testString1 += "01234";
        testString1 += testEscape;
        testString1 += testDelimiter;
        testString1 += "56789";

        String testString2 = "Hallo";
        testString2 += testDelimiter;
        testString2 += "wie";
        testString2 += testEscape;
        testString2 += "gehts";
        testString2 += testEscape;
        testString2 += testDelimiter;
        testString2 += "denn";

        String testString3 = "Mir";
        testString3 += testDelimiter;
        testString3 += "geht";
        testString3 += testEscape;
        testString3 += "es";
        testString3 += testEscape;
        testString3 += testDelimiter;
        testString3 += "gut";

        // create token string
        Vector tokens = new Vector();

        // test the appender function, should convert the text so it can be tokenized later
        tokens.add(testString1);
        tokens.add(testString2);
        tokens.add(testString3);

        String result = OwEscapedStringTokenizer.createDelimitedString(tokens);

        // now tokenize
        OwEscapedStringTokenizer tokenizer = new OwEscapedStringTokenizer(result);

        assertTrue(tokenizer.hasNext());
        String resultString1 = tokenizer.next();

        assertTrue(tokenizer.hasNext());
        String resultString2 = tokenizer.next();

        assertTrue(tokenizer.hasNext());
        String resultString3 = tokenizer.next();

        assertFalse(tokenizer.hasNext());

        assertEquals(testString1, resultString1);
        assertEquals(testString2, resultString2);
        assertEquals(testString3, resultString3);
    }

    /** test the tokenizer functionality with strings that have escape and delimiter chars
     * 
     */
    public void testTokenizer2()
    {
        char testDelimiter = '&';
        char testEscape = '?';

        String testString1 = "ABCD";
        testString1 += testDelimiter;
        testString1 += "efgh";
        testString1 += testEscape;
        testString1 += "01234";
        testString1 += testEscape;
        testString1 += testDelimiter;
        testString1 += "56789";

        String testString2 = "Hallo";
        testString2 += testDelimiter;
        testString2 += "wie";
        testString2 += testEscape;
        testString2 += "gehts";
        testString2 += testEscape;
        testString2 += testDelimiter;
        testString2 += "denn";

        String testString3 = "Mir";
        testString3 += testDelimiter;
        testString3 += "geht";
        testString3 += testEscape;
        testString3 += "es";
        testString3 += testEscape;
        testString3 += testDelimiter;
        testString3 += "gut";

        // create token string
        Vector tokens = new Vector();

        // test the appender function, should convert the text so it can be tokenized later
        tokens.add(testString1);
        tokens.add(testString2);
        tokens.add(testString3);

        String result = OwEscapedStringTokenizer.createDelimitedString(tokens, testDelimiter, testEscape);

        // now tokenize
        OwEscapedStringTokenizer tokenizer = new OwEscapedStringTokenizer(result, testDelimiter, testEscape);

        assertTrue(tokenizer.hasNext());
        String resultString1 = tokenizer.next();

        assertTrue(tokenizer.hasNext());
        String resultString2 = tokenizer.next();

        assertTrue(tokenizer.hasNext());
        String resultString3 = tokenizer.next();

        assertFalse(tokenizer.hasNext());

        assertEquals(testString1, resultString1);
        assertEquals(testString2, resultString2);
        assertEquals(testString3, resultString3);
    }
}
