package test.com.wewebu.ow.server.ecm;

import java.lang.reflect.Constructor;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.unittest.util.OwTestContext;

/**
 *<p>
 * Base class for integration tests that need a properly configured {@link OwNetwork} instance. 
 * All the configuration is based on property files and a test ow_bootstrap.xml file.
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
 *
 * @see OwIntegrationTestSetup
 * @see OwIntegrationTestSession
 */
public abstract class OwIntegrationTest<N extends OwNetwork> extends TestCase
{
    private N network;
    protected String adminUser;
    protected String adminPassword;
    protected String sessionName;

    public OwIntegrationTest(String name_p)
    {
        this(null, name_p);
    }

    public OwIntegrationTest(String sessionName_p, String name_p)
    {
        super(name_p);
        this.sessionName = sessionName_p;
        if (this.sessionName == null)
        {
            this.sessionName = getClass().getName();
        }
    }

    protected abstract String getConfigBootstrapName();

    protected abstract String getResoucesFolderPath();

    protected abstract Class<? extends OwIntegrationTestSetup> getSetupClass() throws ClassNotFoundException;

    protected OwIntegrationTestSetup createSetup() throws Exception
    {
        try
        {
            String configBootstrapName = getConfigBootstrapName();
            String resoucesFolderPath = getResoucesFolderPath();

            Class<? extends OwIntegrationTestSetup> setupClass = getSetupClass();
            Constructor<? extends OwIntegrationTestSetup> c = setupClass.getConstructor(String.class);

            OwIntegrationTestSetup setup = c.newInstance(configBootstrapName);
            setup.initializeTesterData(resoucesFolderPath);
            return setup;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    protected void postSetUp() throws Exception
    {

    }

    public void setUpNetwork(N network_p)
    {
        this.network = network_p;
    }

    public void setUpAdminUser(String user_p)
    {
        this.adminUser = user_p;
    }

    public void setUpAdminPassword(String password_p)
    {
        this.adminPassword = password_p;
    }

    public N getNetwork()
    {
        return network;
    }

    public OwIntegrationTestSession getSession()
    {
        return OwIntegrationTestSession.getSession(this.sessionName);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwIntegrationTestSession mySession = getSession();
        OwIntegrationTestSetup setup = mySession.getSetup(getClass().getName());
        if (setup == null)
        {
            setup = createSetup();
            mySession.addSetup(getClass().getName(), setup);
        }

        setup.testSetUp(mySession, getName(), this);

        postSetUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void loginAdmin() throws Exception
    {

        String user = this.adminUser;
        String password = this.adminPassword;
        login(user, password);
    }

    public void login(String user, String password) throws Exception
    {
        if (user == null || user.length() == 0 || password == null || password.length() == 0)
        {
            throw new OwInvalidOperationException("Invalid admin credentials configuration!");
        }
        getNetwork().logout();
        getNetwork().loginDefault(user, password);
        ((OwTestContext) getNetwork().getContext()).loginInit();
    }
}