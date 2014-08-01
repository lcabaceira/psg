package com.wewebu.ow.unittest.testsuites;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *<p>
 * All Testcases for the Alfresco Workdesk Product.<br/>
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
public class OwCCAllTests extends TestCase
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("All Alfresco Workdesk J-Unit Tests");

        // you can find the J-Unit Test Suite in file: /ow_test/build.xml
        // <target name="basic-test-run" depends="build-tests">

        // if you change something please also add the test case/suite to:
        // /ow_test/unittest/com/wewebu/ow/unittest/testsuites/OwAllTests.java

        return suite;
    }

}
