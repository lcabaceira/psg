package com.wewebu.ow.server.roleimpl.dbrole;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *<p>
 * All Testcases for the DB Role.<br/>
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
public class OwCCDBRoleTests extends TestCase

{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("DB Role J-Unit Tests");

        suite.addTestSuite(OwDBRoleManagerCategoriesConfigurationTest.class);
        suite.addTestSuite(OwDBRoleManagerDummyAdapterTest_DB2.class);
        suite.addTestSuite(OwDBRoleManagerDummyAdapterTest_MsSql.class);
        suite.addTestSuite(OwDBRoleManagerDummyAdapterTest_Oracle.class);
        return suite;
    }

}
