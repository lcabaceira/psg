package com.wewebu.ow.server.util;

import junit.framework.TestCase;

/**
 *<p>
 * OwFileUtilTest.
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
public class OwFileUtilTest extends TestCase
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

    /** test the replaceExtension function
     * 
     */
    public void testReplaceExtension()
    {
        String sNewpath = OwFileUtil.replaceExtension("d:\\temp\\mytext.txt", ".newext");
        assertEquals("d:\\temp\\mytext.newext", sNewpath);

        sNewpath = OwFileUtil.replaceExtension("d:/temp/mytext.txt", ".newext");
        assertEquals("d:/temp/mytext.newext", sNewpath);

        sNewpath = OwFileUtil.replaceExtension("d:/temp/mytext", ".newext");
        assertEquals("d:/temp/mytext.newext", sNewpath);
    }

    /** test the replaceExtension function
     * 
     */
    public void testGetFileName()
    {
        String sName = OwFileUtil.getFileName("d:\\temp\\mytext.txt");
        assertEquals("mytext.txt", sName);

        sName = OwFileUtil.getFileName("d:/temp/mytext.txt");
        assertEquals("mytext.txt", sName);

        sName = OwFileUtil.getFileName("d:\\temp\\mytext");
        assertEquals("mytext", sName);

    }

    /** test the replaceExtension function
     * 
     */
    public void testGetFileDir()
    {
        String sName = OwFileUtil.getFileDir("d:\\temp\\mytext.txt");
        assertEquals("d:\\temp\\", sName);

        sName = OwFileUtil.getFileDir("d:/temp/mytext.txt");
        assertEquals("d:/temp/", sName);

        sName = OwFileUtil.getFileDir("d:\\temp\\");
        assertEquals("d:\\temp\\", sName);

        sName = OwFileUtil.getFileDir("d:\\temp");
        assertEquals("d:\\", sName);
    }

    /** test the replaceExtension function
     * 
     */
    public void testGetFileExt()
    {
        String sName = OwFileUtil.getFileExt("d:\\temp\\mytext.txt");
        assertEquals(".txt", sName);

        sName = OwFileUtil.getFileExt("d:/temp/mytext.txt");
        assertEquals(".txt", sName);

        sName = OwFileUtil.getFileExt("d:\\temp\\mytext");
        assertEquals("", sName);

    }

    /** test the replaceExtension function
     * 
     */
    public void testGetFileTitle()
    {
        String sName = OwFileUtil.getFileTitle("d:\\temp\\mytext.txt");
        assertEquals("mytext", sName);

        sName = OwFileUtil.getFileTitle("d:/temp/mytext.txt");
        assertEquals("mytext", sName);

        sName = OwFileUtil.getFileTitle("d:\\temp\\mytext");
        assertEquals("mytext", sName);

    }
}
