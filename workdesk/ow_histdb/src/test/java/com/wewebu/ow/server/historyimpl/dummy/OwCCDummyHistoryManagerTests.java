package com.wewebu.ow.server.historyimpl.dummy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *<p>
 * OwCCDummyHistoryManagerTests.
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
public class OwCCDummyHistoryManagerTests extends TestCase
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("All Dummy History Tests");
        //$JUnit-BEGIN$
        suite.addTestSuite(OwDummyHistoryManagerTest.class);
        suite.addTestSuite(TestOwTouchConfiguration.class);
        //$JUnit-END$
        return suite;
    }
}