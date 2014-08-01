package com.wewebu.ow.unittest.testsuites;

import junit.framework.TestCase;

/**
 *<p>
 * OwMockTest.
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
public class OwMockTest extends TestCase
{
    public void testGoodTest() throws Exception
    {
        System.out.println("The good test never fails!");
    }

    public void testBadTest() throws Exception
    {
        fail("The bad test always fails!");
    }

    public void testSomeTest() throws Exception
    {
        //void
    }
}
