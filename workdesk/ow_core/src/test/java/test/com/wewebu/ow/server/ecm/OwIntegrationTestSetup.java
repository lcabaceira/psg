package test.com.wewebu.ow.server.ecm;

/**
 *<p>
 * Knows how to read the test configuration data (from property files) and setup an {@link OwIntegrationTest}.
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
public interface OwIntegrationTestSetup
{
    public static final String DEFAULT_TESTER_PROPERTIES = "default_tester.properties";
    public static final String BOOTSRAP_ROOT = "bootstrap";
    public static final String ECMADAPTER_CONFIG_NODE = "EcmAdapter";
    public static final String ROLEMANAGER_CONFIG_NODE = "RoleManager";
    public static final String CLASSNAME_CONFIG_NODE = "ClassName";

    void checkDependencies(OwIntegrationTestSession session_p) throws IllegalStateException;

    void sessionSetUp(OwIntegrationTestSession session_p) throws Exception;

    void sessionTearDown(OwIntegrationTestSession session_p) throws Exception;

    @SuppressWarnings("rawtypes")
    void testSetUp(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception;

    @SuppressWarnings("rawtypes")
    void testTearDown(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception;

    public void initializeTesterData(String resoucesFolderPath) throws Exception;
}