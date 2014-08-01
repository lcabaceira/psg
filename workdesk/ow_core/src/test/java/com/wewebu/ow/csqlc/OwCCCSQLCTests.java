package com.wewebu.ow.csqlc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OwCCCSQLCTests extends TestCase
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite(OwCCCSQLCTests.class.getName());
        //$JUnit-BEGIN$
        suite.addTestSuite(OwFromClauseTest.class);
        suite.addTestSuite(OwComparisonPredicateTest.class);
        suite.addTestSuite(OwWhereClauseTest.class);
        suite.addTestSuite(OwSimpleTableTest.class);
        suite.addTestSuite(OwQueryStatementTest.class);
        suite.addTestSuite(OwSelectListTest.class);
        //$JUnit-END$
        return suite;
    }

}
