package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwIntegrationTestSession;

public class OwCMISIntegrationTestsSuite extends TestSuite
{
    private static final Logger LOG = Logger.getLogger(OwCMISIntegrationTestsSuite.class);

    public OwCMISIntegrationTestsSuite(Class<? extends OwCMISIntegrationTest> testClass_p)
    {
        super(testClass_p);
    }

    public OwCMISIntegrationTestsSuite(String name_p)
    {
        super(name_p);
    }

    public void addIntegrationTestSuite(Class<? extends OwCMISIntegrationTest> testClass_p)
    {
        addIntegrationTest(new OwCMISIntegrationTestsSuite(testClass_p));
    }

    public void addIntegrationTest(OwCMISIntegrationTestsSuite test_p)
    {
        super.addTest(test_p);
    }

    @Override
    public void run(TestResult result_p)
    {
        HashSet<OwIntegrationTestSession> sessions = new HashSet<OwIntegrationTestSession>();
        for (Enumeration e = tests(); e.hasMoreElements();)
        {
            if (result_p.shouldStop())
            {
                break;
            }
            Test test = (Test) e.nextElement();
            if (test instanceof OwCMISIntegrationTest)
            {
                OwCMISIntegrationTest integrationTest = (OwCMISIntegrationTest) test;
                sessions.add(integrationTest.getSession());
            }
            runTest(test, result_p);
        }

        for (Iterator i = sessions.iterator(); i.hasNext();)
        {
            OwIntegrationTestSession session = (OwIntegrationTestSession) i.next();
            try
            {
                session.tearDown();
            }
            catch (Throwable t)
            {
                LOG.error("Could not tearDown session " + session.getSessionName(), t);
            }
        }
    }
}
