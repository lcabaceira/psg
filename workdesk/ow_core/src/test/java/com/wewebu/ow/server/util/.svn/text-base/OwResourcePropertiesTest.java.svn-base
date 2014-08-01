package com.wewebu.ow.server.util;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

public class OwResourcePropertiesTest extends TestCase
{
    public static final String ISO = "_ISO.properties";

    public static final String UTF8 = "_UTF8.properties";

    public void testISOLoading() throws IOException
    {
        URL testFile = OwResourcePropertiesTest.class.getResource(OwResourcePropertiesTest.class.getSimpleName() + ISO);
        OwResourceProperties prop = new OwResourceProperties();
        prop.load(testFile);

        assertEquals(3, prop.size());

        assertNotNull(prop.get("owlabel.browse.id"));
    }

    public void testUT8Loading() throws IOException
    {
        URL testFile = OwResourcePropertiesTest.class.getResource(OwResourcePropertiesTest.class.getSimpleName() + UTF8);
        OwResourceProperties prop = new OwResourceProperties();
        prop.load(testFile);

        assertEquals(3, prop.size());

        assertNotNull("Test key changed or not available", prop.get("owlabel.browse.id"));
    }

    public void testEncodingURL() throws IOException
    {
        URL testFile = OwResourcePropertiesTest.class.getResource(OwResourcePropertiesTest.class.getSimpleName() + UTF8);
        assertEquals("UTF-8", OwResourceProperties.getResourceEncoding(testFile));
        testFile = OwResourcePropertiesTest.class.getResource(OwResourcePropertiesTest.class.getSimpleName() + ISO);
        assertEquals(null, OwResourceProperties.getResourceEncoding(testFile));
    }

    public void testEncodingValues()
    {
        byte[] enc = new byte[4];
        enc[2] = (byte) 0xFE;
        enc[3] = (byte) 0xFF;

        assertEquals("UTF-32BE", OwResourceProperties.getResourceEncoding(enc));
        enc[0] = enc[3];
        enc[1] = enc[2];
        enc[2] = (byte) 0x00;
        enc[3] = enc[2];
        assertEquals("UTF-32LE", OwResourceProperties.getResourceEncoding(enc));
        enc[2] = (byte) 0x55;
        assertEquals("UTF-16LE", OwResourceProperties.getResourceEncoding(enc));
        enc[0] = enc[1];
        enc[1] = (byte) 0xFF;
        assertEquals("UTF-16BE", OwResourceProperties.getResourceEncoding(enc));
    }
}
