package com.wewebu.expression;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.wewebu.expression.docsamples.OwExprDocSamplesTests;
import com.wewebu.expression.language.OwExprValueTest;
import com.wewebu.expression.parser.OwExprParserTests;

/**
*<p>
* OwExprAllTests. 
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
public class OwExprAllTests extends TestCase
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("test.com.wewebu.expression.OwExprAllTests");
        //$JUnit-BEGIN$
        suite.addTestSuite(OwExprParserTests.class);
        suite.addTestSuite(OwExprDocSamplesTests.class);
        suite.addTestSuite(OwExprValueTest.class);
        //$JUnit-END$
        return suite;
    }
}
