package com.wewebu.ow.server.ecmimpl.fncm5.credentials;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.filenet.api.constants.ClassNames;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5IntegrationTest;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * OwFNCM5AuthenticationTest.
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
public class OwFNCM5AuthenticationTest extends OwFNCM5IntegrationTest
{
    private static final String[] ADMIN_LDAP_GROUPS = new String[] { "P8Admins", "GeneralUsers", "FileNetUsers" };

    private static final String[] ADMIN_ENGINE_GROUPS = new String[] { "cn=P8Admins,o=sample", "cn=GeneralUsers,o=sample" };

    private static final String[] ADMIN_ENGINE_ROLES = new String[] { "P8Admins", "OW_Authenticated", "GeneralUsers" };

    private static final String[] ADMIN_LDAP_ROLES = new String[] { "P8Admins", "OW_Authenticated", "GeneralUsers", "FileNetUsers" };

    private static final String LDAP_DB_TESTS = "LDAP_DB_TESTS";

    private static final Logger LOG = Logger.getLogger(OwFNCM5AuthenticationTest.class);

    private String methodBootstrap = null;

    @Before
    public void setUp() throws Exception
    {
        //void
    }

    @Override
    protected String getBootstrap()
    {
        return methodBootstrap;
    }

    private void methodSetUp(String bootstrap_p) throws Exception
    {
        methodBootstrap = bootstrap_p;
        super.setUp();
    }

    private void assertSameObjects(String[] expected_p, Collection<OwUserInfo> userInfos_p) throws Exception
    {
        Collection<String> stringCollection = new ArrayList<String>();

        for (OwUserInfo info : userInfos_p)
        {
            stringCollection.add(info.getUserName());
        }

        assertSameObjects(expected_p, stringCollection);
    }

    private <T> void assertSameObjects(T[] expected_p, Collection<T> collection_p)
    {
        List<Object> missingObjects = new ArrayList<Object>();
        for (int i = 0; i < expected_p.length; i++)
        {
            if (!collection_p.contains(expected_p[i]))
            {
                missingObjects.add(expected_p[i]);
            }
        }

        if (!missingObjects.isEmpty())
        {
            Assert.fail("Expected objects " + Arrays.toString(expected_p) + " not found within " + collection_p + ". Missing " + missingObjects);
        }
        else if (expected_p.length != collection_p.size())
        {
            Assert.fail(Arrays.toString(expected_p) + " do not match " + collection_p + ".");
        }

    }

    @Test
    public void testUserInfo_NONE() throws Exception
    {
        methodSetUp("/bootstrap_authentication_NONE.xml");

        try
        {

            OwFNCM5Credentials credentials = network.getCredentials();

            OwUserInfo userInfo = credentials.getUserInfo();
            assertEquals("cn=P8Admin,o=sample", userInfo.getUserName());

            assertEquals("cn=P8Admin,o=sample", userInfo.getUserLongName());

            assertEquals("P8Admin", userInfo.getUserShortName());

            assertEquals("P8Admin", userInfo.getUserDisplayName());

            Collection<OwUserInfo> groups = userInfo.getGroups();
            assertSameObjects(ADMIN_ENGINE_GROUPS, groups);

            Collection<String> roleNames = userInfo.getRoleNames();
            assertSameObjects(ADMIN_ENGINE_ROLES, roleNames);

        }
        finally
        {
            tearDown();
        }
    }

    @Test
    public void testUserInfo_LDAP() throws Exception
    {
        methodSetUp("/bootstrap_authentication_LDAP.xml");

        try
        {

            OwFNCM5Credentials credentials = network.getCredentials();
            OwUserInfo userInfo = credentials.getUserInfo();

            assertEquals("P8Admin", userInfo.getUserName());

            //            assertEquals("P8Admin",userInfo.getUserLongName());
            //            
            //            assertEquals("P8Admin",userInfo.getUserShortName());
            //            
            //            assertEquals("P8Admin",userInfo.getUserDisplayName());

            Collection<OwUserInfo> groups = userInfo.getGroups();
            assertSameObjects(ADMIN_LDAP_GROUPS, groups);

            Collection<String> roleNames = userInfo.getRoleNames();
            assertSameObjects(ADMIN_LDAP_ROLES, roleNames);

        }
        finally
        {
            tearDown();
        }
    }

    @Test
    public void testAccesRoles_LDAP() throws Exception
    {
        methodSetUp("/bootstrap_dbrole_LDAP.xml");

        try
        {
            OwFNCM5Credentials credentials = network.getCredentials();
            OwUserInfo userInfo = credentials.getUserInfo();

            Collection<String> roleNames = userInfo.getRoleNames();
            assertSameObjects(ADMIN_LDAP_ROLES, roleNames);

            OwRoleManager roleManager = this.network.getRoleManager();
            roleManager.setAccessRights(ADMIN_LDAP_ROLES[0], OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.FOLDER, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
            roleManager.setAccessMask(ADMIN_LDAP_ROLES[0], OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.FOLDER, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

            if (createManager.isFolderAvailable(LDAP_DB_TESTS))
            {
                createManager.deleteFolderByPath(LDAP_DB_TESTS);
            }

            String roleTestDMSID = createManager.createFolder(LDAP_DB_TESTS);
            network.getObjectFromDMSID(roleTestDMSID, true);

            createManager.deleteFolderByPath(LDAP_DB_TESTS);

            roleManager.setAccessRights(ADMIN_LDAP_ROLES[0], OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.FOLDER, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
            roleManager.setAccessMask(ADMIN_LDAP_ROLES[0], OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.FOLDER, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

            try
            {
                roleTestDMSID = createManager.createFolder(LDAP_DB_TESTS);
                Assert.fail("Should not be able to create Folder objects.");
            }
            catch (OwAccessDeniedException e)
            {
                LOG.debug("Create accees right denied.", e);
            }

        }
        finally
        {
            tearDown();
        }
    }
}
