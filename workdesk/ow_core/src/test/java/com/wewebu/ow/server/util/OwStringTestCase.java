package com.wewebu.ow.server.util;

import junit.framework.TestCase;

/**
 * Test for OwString, checking also the replacement functionality.
 */
public class OwStringTestCase extends TestCase
{

    public void testReplaceAllString()
    {
        String source = "Hello* world*";
        assertEquals("Hello.* world.*", OwString.replaceAll(source, "*", ".*"));

        assertEquals(source, OwString.replaceAll(source, ".", "not my day"));

        assertEquals(source.replace('l', 'R'), OwString.replaceAll(source, "l", "R"));
    }

    public void testReplaceAllStringBuffer()
    {
        StringBuffer val = new StringBuffer("Hello* world*");
        OwString.replaceAll(val, ".", "not my day");
        assertEquals("Hello* world*", val.toString());

        OwString.replaceAll(val, "*", ".*");
        assertEquals("Hello.* world.*", val.toString());

        val = new StringBuffer("Hello* world*");
        OwString.replaceAll(val, "l", "R");
        assertEquals("HeRRo* worRd*", val.toString());
    }

    public void testReplaceAllStringBuilder()
    {
        StringBuilder val = new StringBuilder("Hello* world*");
        OwString.replaceAll(val, ".", "not my day");
        assertEquals("Hello* world*", val.toString());

        OwString.replaceAll(val, "*", ".*");
        assertEquals("Hello.* world.*", val.toString());

        val = new StringBuilder("Hello* world*");
        OwString.replaceAll(val, "l", "R");
        assertEquals("HeRRo* worRd*", val.toString());

        val = new StringBuilder("{base}/my/Owd{base}");
        OwString.replaceAll(val, "{base}", "\"");
        assertEquals("\"/my/Owd\"", val.toString());
    }

    public void testPerformanceReplaceString()
    {
        String test = "Hello World";
        long start = System.nanoTime();
        for (int i = 0; i < 100; i += 1)
        {
            OwString.replaceAll(test, "W", "w");
        }
        System.out.println("Time String single replacement = " + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < 100; i += 1)
        {
            OwString.replaceAll(test, "l", "L");
        }
        System.out.println("Time String multiple replacement = " + (System.nanoTime() - start));
    }

    public void testPerformanceReplaceStringBuffer()
    {
        StringBuffer test = new StringBuffer("Hello World");
        long start = System.nanoTime();
        for (int i = 0; i < 100; i += 1)
        {
            OwString.replaceAll(test, "W", "w");
        }
        System.out.println("Time StringBuffer single replacement = " + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < 100; i += 1)
        {
            OwString.replaceAll(test, "l", "L");
        }
        System.out.println("Time StringBuffer multiple replacement = " + (System.nanoTime() - start));
    }

    public void testPerformanceReplaceStringBuilder()
    {
        StringBuilder test = new StringBuilder("Hello World");
        long start = System.nanoTime();
        for (int i = 0; i < 100; i += 1)
        {
            OwString.replaceAll(test, "W", "w");
        }
        System.out.println("Time StringBuilder single replacement = " + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < 100; i += 1)
        {
            OwString.replaceAll(test, "l", "L");
        }
        System.out.println("Time StringBuilder multiple replacement = " + (System.nanoTime() - start));
    }

}
