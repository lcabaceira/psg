package com.wewebu.ow.server.historyimpl.dbhistory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.wewebu.ow.server.historyimpl.dummy.OwCCDummyHistoryManagerTests;

/**
 *<p>
 * All Testcases for the DB History.<br/>
 * Testcases uses to run from the build server.
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
public class OwCCDBHistoryTests extends TestCase
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("DB History J-Unit Tests");

        suite.addTestSuite(OwDBHistoryDummyAdapterFilterTest_CustomFilter.class);
        suite.addTestSuite(OwDBHistoryDummyAdapterFilterTest_DB2.class);
        suite.addTestSuite(OwDBHistoryDummyAdapterFilterTest_MsSql.class);
        suite.addTestSuite(OwDBHistoryDummyAdapterFilterTest_Oracle.class);
        suite.addTestSuite(OwDBHistoryDummyAdapterTest_DB2.class);
        suite.addTestSuite(OwDBHistoryDummyAdapterTest_MsSql.class);
        suite.addTestSuite(OwDBHistoryDummyAdapterTest_Oracle.class);

        suite.addTest(OwCCDummyHistoryManagerTests.suite());

        return suite;
    }

}
