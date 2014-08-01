package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwIntegrationTest;
import test.com.wewebu.ow.server.ecm.OwIntegrationTestSetup;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.exceptions.OwException;

public abstract class OwCMISIntegrationTest extends OwIntegrationTest<OwCMISNetwork>
{
    private static final Logger LOG = Logger.getLogger(OwCMISIntegrationTest.class);
    protected OwCMISTestFixture fixture = null;

    public OwCMISIntegrationTest(String name_p)
    {
        super(name_p);
    }

    public OwCMISIntegrationTest(String sessionName_p, String name_p)
    {
        super(sessionName_p, name_p);
    }

    protected OwCMISTestFixture createFixture()
    {
        return OwCMISTestFixture.NO_FIXTURE;

    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        fixture = createFixture();
        fixture.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (fixture != null)
        {
            fixture.tearDown();
        }
        super.tearDown();

    }

    protected Session getCmisSession() throws OwException
    {
        OwCMISNativeSession session = (OwCMISNativeSession) getNetwork().getSession(null);
        return session.getOpenCMISSession();
    }

    @Override
    public void login(String user, String password) throws Exception
    {
        super.login(user, password);
        Session session = getCmisSession();
        Field f = session.getClass().getDeclaredField("parameters"); //NoSuchFieldException
        f.setAccessible(true);
        Map<String, String> parameters = (Map<String, String>) f.get(session);
        String url = parameters.get(SessionParameter.ATOMPUB_URL) == null ? parameters.get(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE) : parameters.get(SessionParameter.ATOMPUB_URL);
        LOG.info("CMIS session connected to : " + parameters.get(SessionParameter.USER) + " @ " + url);
    }

    @Override
    protected Class<? extends OwIntegrationTestSetup> getSetupClass()
    {
        return OwCMISBasicIntegrationTestSetup.class;
    }

    @Override
    protected String getConfigBootstrapName()
    {
        return OwCMISBasicIntegrationTestSetup.DEFAULT_CMIS_CONFIG_BOOTSTRAP;
    }

    @Override
    protected String getResoucesFolderPath()
    {
        return OwCMISBasicIntegrationTestSetup.TEST_CMIS_RESOUCES_FOLDER_PATH;
    }
}
