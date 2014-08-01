package com.wewebu.ow.unittest.util;

import junit.framework.TestCase;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *<p>
 * ANTTest.
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
public class ANTTest extends TestCase
{
    /**
    * Logger for this class
    */
    //private static final Logger LOG = Logger.getLogger(ANTTest.class);
    /**
     * @param name_p
     */
    public ANTTest(String name_p)
    {
        super(name_p);
    }

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

    public void testSystemOut()
    {
        System.out.println("IS OK");
        ClassPathUtil classPathUtil = new ClassPathUtil();
        classPathUtil.printClasspath();
        classPathUtil.validate();
        classPathUtil.testClass("/org/apache/commons/configuration/PropertiesConfiguration.class");

        //LOG.info("testSystemOut() START - PropertiesConfiguration configuration = new PropertiesConfiguration();");
        PropertiesConfiguration conf = new PropertiesConfiguration();
        conf.getString("do nothing");
        //LOG.info("testSystemOut() END - PropertiesConfiguration configuration = new PropertiesConfiguration();");
        //String configFile = ApplicationConfiguration.getTestSuiteProperties();
        assertTrue(true);
    }
}
