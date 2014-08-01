package com.wewebu.ow.server.ecm;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.unittest.util.AbstractCredentialsAdapter;
import com.wewebu.ow.unittest.util.AbstractNetworkAdapter;
import com.wewebu.ow.unittest.util.OwTestUserInfo;

/**
 *<p>
 * OwStandardCrossMappingsTest.
 *</p>
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
*/
public class OwStandardCrossMappingsTest extends TestCase
{
    OwXMLUtil config;
    public static final String USER_NAME = "User";
    public static final String GROUP_NAME = "UserGroup";

    protected void setUp() throws Exception
    {
        super.setUp();
        if (config == null)
        {
            config = new OwStandardXMLUtil(System.class.getResourceAsStream("/resources/utils/stdCrossMapping.xml"), "XMapping");
        }
    }

    public void testGetUsermappings() throws Exception
    {
        OwStandardCrossMappings crossMap = new OwStandardCrossMappings(config);
        assertTrue(crossMap.getUsermappings().size() == 1);
        //The internal CrossLogin class is private, use reflection
        Object login = crossMap.getUsermappings().get(USER_NAME);
        assertNotNull(login);
        //cannot get the OwCrossLogin which is internal used
        Method method = login.getClass().getDeclaredMethod("getUser", new Class[0]);
        String user = (String) method.invoke(login, new Object[0]);
        assertNotNull(user);
        assertTrue(user.equals(USER_NAME));
    }

    public void testGetGroupmappings() throws Exception
    {
        OwStandardCrossMappings crossMap = new OwStandardCrossMappings(config);
        assertTrue(crossMap.getGroupmappings().size() == 1);

        Object login = crossMap.getGroupmappings().get(GROUP_NAME);
        assertNotNull(login);
        //cannot get the OwCrossLogin which is internal used
        Method method = login.getClass().getDeclaredMethod("getUser", new Class[0]);
        String group = (String) method.invoke(login, new Object[0]);
        assertNotNull(group);
        assertTrue(group.equals(GROUP_NAME));
    }

    public void testDoXLogin() throws Exception
    {
        OwStandardCrossMappings crossMap = new OwStandardCrossMappings(config);
        /*This instance will be used as parent and x-network instance*/
        UserInfoNetwork network = new UserInfoNetwork(config);
        //test user mapping
        crossMap.doXLogin(network, network, USER_NAME, null);

        /* test now user group mapping
         * the user not matching the defined user to user map
         * the groups from network.getCredentials should be requested
         * with current logged in user.*/
        crossMap.doXLogin(network, network, GROUP_NAME, null);
    }

    public void testDoXLoginWithoutUserMapping() throws Exception
    {
        /* Create Document builder for node creation*/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        /* Create node which is just empty, called user mappings*/
        Node emptyMapping = builder.newDocument().createElement("usermappings");

        /*Starting test*/
        OwStandardXMLUtil util = new OwStandardXMLUtil(emptyMapping);
        OwStandardCrossMappings crossMap = new OwStandardCrossMappings(util);

        UserInfoNetwork network = new UserInfoNetwork(config) {
            /*overwrite to test if the same name and password path through*/
            public void loginDefault(String strUser_p, String strPassword_p) throws Exception
            {
                assertEquals(USER_NAME, strUser_p);
                assertEquals(GROUP_NAME, strPassword_p);
            }
        };
        try
        {
            crossMap.doXLogin(network, network, USER_NAME, GROUP_NAME);
        }
        catch (Exception ex)
        {
            fail("CrossLogin without mappings faild: " + ex.getMessage());
        }
    }

    /**
     * Helper class for cross mappings test.
     * Just implementing the two methods
     * loginDefault and getUserFromID.
     */
    protected static class UserInfoNetwork extends AbstractNetworkAdapter
    {
        protected OwXMLUtil config;
        protected OwCredentials cred;

        public UserInfoNetwork(OwXMLUtil config_p) throws Exception
        {
            config = config_p;
            config.getNode();//just test if this is a null pointer
            cred = new ConfigCredentials(config_p);
        }

        public void loginDefault(String strUser_p, String strPassword_p) throws Exception
        {
            List lst = this.config.getSubUtil("usermappings").getSafeUtilList("usermap");
            if (lst != null && !lst.isEmpty())
            {
                for (int i = 0; i < lst.size(); i++)
                {
                    OwXMLUtil usermap = (OwXMLUtil) lst.get(i);
                    if (usermap.getSafeTextValue("user", "").equals(strUser_p) && usermap.getSafeTextValue("password", "").equals(strPassword_p))
                    {
                        return;
                    }
                }
            }
            //if no usermappings exist or matches throw an exception
            throw new OwInvalidOperationException("No usermappings were found");
        }

        public OwUserInfo getUserFromID(String strID_p) throws Exception
        {
            return cred.getUserInfo();
        }

        public OwCredentials getCredentials() throws Exception
        {
            return cred;
        }

        protected void finalize() throws Throwable
        {
            config = null;
            super.finalize();
        }

        public String getDMSPrefix()
        {
            return "doXLoginHelper";
        }
    }

    public static class ConfigCredentials extends AbstractCredentialsAdapter
    {
        OwTestUserInfo usr;

        public ConfigCredentials(OwXMLUtil config_p) throws Exception
        {
            init(config_p);
        }

        protected void init(OwXMLUtil config_p) throws Exception
        {
            final LinkedList lstGroup = new LinkedList();
            Iterator it = config_p.getSubUtil("usermappings").getSafeUtilList("usermap").iterator();
            while (it.hasNext())
            {
                OwXMLUtil util = (OwXMLUtil) it.next();
                String group = util.getSafeStringAttributeValue("group", null);
                if (group != null)
                {
                    OwTestUserInfo usrInfo = new OwTestUserInfo(group);
                    lstGroup.add(usrInfo);
                }
            }
            usr = new OwTestUserInfo(OwStandardCrossMappingsTest.USER_NAME) {
                public java.util.Collection getGroups() throws Exception
                {
                    return lstGroup;
                };
            };
        }

        @Override
        public OwUserInfo getUserInfo() throws Exception
        {
            return usr;
        }
    }

}