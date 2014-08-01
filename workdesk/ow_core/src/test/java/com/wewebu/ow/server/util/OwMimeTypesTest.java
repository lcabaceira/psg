package com.wewebu.ow.server.util;

import junit.framework.TestCase;

/**
 *<p>
 * Tests the OwMimeTypes utility class.
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
public class OwMimeTypesTest extends TestCase
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

    public void testGetExtensionFromMime()
    {
        String ext = OwMimeTypes.getExtensionFromMime("text/plain");
        assertEquals("txt", ext);

        ext = OwMimeTypes.getExtensionFromMime("");
        assertNull(ext);
    }

    public void testGetMimeTypeFromExtension()
    {
        String ext = OwMimeTypes.getMimeTypeFromExtension("txt");
        assertEquals("text/plain", ext);

        ext = OwMimeTypes.getMimeTypeFromExtension("");
        assertNull(ext);
    }

    public void testGetMimeParameter()
    {
        // find defined name
        String name = OwMimeTypes.getMimeParameter("text/html;name=hallo.txt", "name");
        assertEquals("hallo.txt", name);

        // find defined name somewhere
        name = OwMimeTypes.getMimeParameter("text/html;token=wie;name=hallo.txt;token2=ddd", "name");
        assertEquals("hallo.txt", name);

        // find defined name somewhere with quota
        name = OwMimeTypes.getMimeParameter("text/html;token=wie;name=\"hallo.txt\";token2=ddd", "name");
        assertEquals("hallo.txt", name);

        // find undefined name
        name = OwMimeTypes.getMimeParameter("text/html;name=hallo.txt", "nameUndef");
        assertNull(name);

        // find defined name without mimetype
        name = OwMimeTypes.getMimeParameter("name=hallo.txt", "name");
        assertEquals("hallo.txt", name);

        // find defined name with delimiter ;
        name = OwMimeTypes.getMimeParameter("name=hallo.txt;", "name");
        assertEquals("hallo.txt", name);

        // find defined name with quota
        name = OwMimeTypes.getMimeParameter("text/html;name=\"hallo.txt\"", "name");
        assertEquals("hallo.txt", name);

        // find defined name with quota and delimiter ;
        name = OwMimeTypes.getMimeParameter("text/html;name=\"hallo.txt;\"", "name");
        assertEquals("hallo.txt;", name);
    }
}
