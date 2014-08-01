package com.wewebu.ow.server.util;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class OwHTMLHelperTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testWriterSecurHTML() throws IOException
    {
        StringWriter writer = new StringWriter();
        String toEscape = "Hello & welcome back";
        OwHTMLHelper.writeSecureHTML(writer, toEscape);
        Assert.assertEquals("Hello &amp; welcome back", writer.getBuffer().toString());
    }

    @Test
    public void testIgnoreHtmlUnicode() throws IOException
    {
        StringWriter writer = new StringWriter();
        String toEscape = "Hello &#38; &#x0026; welcome back";
        OwHTMLHelper.writeSecureHTML(writer, toEscape);
        Assert.assertEquals(toEscape, writer.getBuffer().toString());
    }

    @Test
    public void testIndexCheck() throws IOException
    {
        StringWriter writer = new StringWriter();
        String toEscape = "Hello & welcome back &";
        OwHTMLHelper.writeSecureHTML(writer, toEscape);
        Assert.assertEquals("Hello &amp; welcome back &amp;", writer.getBuffer().toString());
    }
}
