package com.wewebu.ow.server.ui.ua;

import junit.framework.TestCase;

/**
 *<p>
 * OwOSFamilyTest.
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
public class OwOSFamilyTest extends TestCase
{

    public void testUserAgetParse() throws Exception
    {

        OwOSFamily osX = OwOSFamily.from("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.11 (KHTML, like Geko) Chrome/23.0.1271.97 Safari/537.11");
        assertSame(OwOSFamily.OS_X, osX);
        OwOSFamily windows = OwOSFamily.from("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
        assertSame(OwOSFamily.WINDOWS, windows);
        OwOSFamily android = OwOSFamily.from("Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        assertSame(OwOSFamily.UNKNOWN, android);

        //test first occurrence detection
        OwOSFamily osXWindows = OwOSFamily.from("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Geko) Chrome/23.0.1271.97 Safari/537.11");
        assertSame(OwOSFamily.OS_X, osXWindows);

        OwOSFamily windosOsX = OwOSFamily.from("Mozilla/5.0  (Windows NT 6.1; WOW64) (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.11 (KHTML, like Geko) Chrome/23.0.1271.97 Safari/537.11");
        assertSame(OwOSFamily.WINDOWS, windosOsX);

    }

    public void testFamilyNameParse() throws Exception
    {
        OwOSFamily osX = OwOSFamily.from(OwOSFamily.OS_X.getFamilyName());
        assertSame(OwOSFamily.OS_X, osX);
        OwOSFamily windows = OwOSFamily.from(OwOSFamily.WINDOWS.getFamilyName());
        assertSame(OwOSFamily.WINDOWS, windows);
        OwOSFamily android = OwOSFamily.from("Android");
        assertSame(OwOSFamily.UNKNOWN, android);
    }

    public void testEnumNameParse() throws Exception
    {
        OwOSFamily osX = OwOSFamily.from(OwOSFamily.OS_X.name());
        assertSame(OwOSFamily.OS_X, osX);
        OwOSFamily windows = OwOSFamily.from(OwOSFamily.WINDOWS.name());
        assertSame(OwOSFamily.WINDOWS, windows);
        OwOSFamily android = OwOSFamily.from("ANDROID");
        assertSame(OwOSFamily.UNKNOWN, android);
    }
}
