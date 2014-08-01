package com.wewebu.ow.server.ecmimpl.fncm5.rolemanager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.filenet.api.constants.ClassNames;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5IntegrationTest;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * OwFNCM5DBRoleManagerTest.
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
public class OwFNCM5DBRoleManagerTest extends OwFNCM5IntegrationTest
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5DBRoleManagerTest.class);

    private static final String ROLE_TESTS = "RoleTests";

    private final String ADMINS_ROLE = "P8Admins";
    private final String EMAIL = "Email";

    private String testFolderPath;

    @Override
    protected String getBootstrap()
    {
        return "/bootstrap_dbrole.xml";
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        OwRoleManager roleManager = this.network.getRoleManager();
        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.FOLDER, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.FOLDER, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

        if (createManager.isFolderAvailable(ROLE_TESTS))
        {
            createManager.deleteFolderByPath(ROLE_TESTS);
        }
        String roleTestDMSID = createManager.createFolder(ROLE_TESTS);
        network.getObjectFromDMSID(roleTestDMSID, true);

        testFolderPath = createManager.getRootPath() + ROLE_TESTS;
    }

    @Override
    public void tearDown() throws Exception
    {
        if (createManager.isFolderAvailable(ROLE_TESTS))
        {
            createManager.deleteFolderByPath(ROLE_TESTS);
        }

        super.tearDown();
    }

    private OwFNCM5Object<?> createTestObject(String className_p) throws OwException
    {
        return createTestObject(className_p, "a" + className_p);
    }

    private OwFNCM5Object<?> createTestObject(String className_p, String name_p) throws OwException
    {
        OwObject folder = network.getObjectFromPath(testFolderPath, true);
        OwPropertyCollection props = new OwStandardPropertyCollection();

        OwFNCM5Class<?, ?> clazz = network.getObjectClass(className_p, null);
        OwFNCM5PropertyClass namePropertyClazz = clazz.getPropertyClass(clazz.getNamePropertyName());
        OwProperty name = new OwStandardProperty(name_p, namePropertyClazz);

        props.put(namePropertyClazz.getClassName(), name);

        String dmsID = network.createNewObject(true, null, null, className_p, props, null, null, folder, "", "", false);
        assertNotNull(dmsID);
        return network.getObjectFromDMSID(dmsID, true);
    }

    @Test
    public void testCreateAccessRight() throws Exception
    {
        OwRoleManager roleManager = this.network.getRoleManager();
        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

        try
        {
            OwFNCM5Class<?, ?> emailClass = network.getObjectClass(EMAIL, null);
            assertFalse(emailClass.canCreateNewObject());
            createTestObject(EMAIL);
            Assert.fail("Should not be able to create Email objects.");
        }
        catch (OwAccessDeniedException e)
        {
            LOG.debug("Create accees right denied.", e);
        }

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

        {
            OwFNCM5Class<?, ?> emailClass = network.getObjectClass(EMAIL, null);
            assert (emailClass.canCreateNewObject());
            createTestObject(EMAIL);
        }
    }

    @Test
    public void testViewAccessRight_get() throws Exception
    {
        OwRoleManager roleManager = this.network.getRoleManager();
        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);
        OwFNCM5Object<?> emailObject = createTestObject(EMAIL);

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW);

        try
        {
            OwObject pathAccessedObject = network.getObjectFromPath(emailObject.getPath(), true);
            assertEquals("Bad test!", emailObject.getDMSID(), pathAccessedObject.getDMSID());
            Assert.fail("Should not be able to access Email objects by path.");
        }
        catch (OwAccessDeniedException e)
        {
            LOG.debug("Path accees right denied.", e);
        }

        try
        {
            OwObject dmsidAccessedObject = network.getObjectFromDMSID(emailObject.getDMSID(), true);
            assertNotNull(dmsidAccessedObject);
            Assert.fail("Should not be able to access Email objects by DMSID.");
        }
        catch (OwAccessDeniedException e)
        {
            LOG.debug("DMSID accees right denied.", e);
        }

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW);

        {
            OwObject pathAccessedObject = network.getObjectFromPath(emailObject.getPath(), true);
            assertEquals("Bad test!", emailObject.getDMSID(), pathAccessedObject.getDMSID());

            OwObject dmsidAccessedObject = network.getObjectFromDMSID(emailObject.getDMSID(), true);
            assertNotNull(dmsidAccessedObject);
        }

    }

    @Test
    public void testViewAccessRight_collections() throws Exception
    {
        OwFNCM5ObjectStoreResource defaultResource = network.getResource(null);
        String resourceId = defaultResource.getName();
        String searchPath = testFolderPath.substring(1 + resourceId.length());
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode(ClassNames.DOCUMENT, null, searchPath, new OwEcmUtil.OwSimpleSearchClause[] {}, network);

        OwRoleManager roleManager = this.network.getRoleManager();

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.DOCUMENT, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.DOCUMENT, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);

        final int childrenBatchesCount = 3;
        for (int i = 0; i < childrenBatchesCount; i++)
        {
            createTestObject(EMAIL, "an" + EMAIL + "_" + i);
            createTestObject(ClassNames.DOCUMENT, "a" + ClassNames.DOCUMENT + "_" + i);
        }

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW);

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.DOCUMENT, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, ClassNames.DOCUMENT, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW);

        {
            OwObject testFolder = network.getObjectFromPath(testFolderPath, true);

            OwObjectCollection folderChildren = testFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 100, -1, null);
            OwObjectCollection searchedObjects = network.doSearch(search, null, null, 100, -1);

            assertEquals(childrenBatchesCount, folderChildren.size());
            assertEquals(childrenBatchesCount, searchedObjects.size());
        }

        roleManager.setAccessRights(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        roleManager.setAccessMask(ADMINS_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, EMAIL, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW);

        {
            OwObject testFolder = network.getObjectFromPath(testFolderPath, true);

            OwObjectCollection children = testFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 100, -1, null);
            OwObjectCollection searchedObjects = network.doSearch(search, null, null, 100, -1);

            assertEquals(childrenBatchesCount * 2, children.size());
            assertEquals(childrenBatchesCount * 2, searchedObjects.size());
        }

    }

    private String toPropertyResource(int context_p, String objectClass_p, String propertyClass_p)
    {
        return Integer.toString(context_p) + "." + objectClass_p + "." + propertyClass_p;
    }

    private void setAllRolesAccessRights(OwRoleManager roleManager_p, int category_p, String resourceId_p, int accessRights_p) throws Exception
    {
        OwFNCM5Credentials credentials = network.getCredentials();
        OwUserInfo ui = credentials.getUserInfo();
        Collection<String> roleNames = ui.getRoleNames();
        for (String name : roleNames)
        {
            roleManager_p.setAccessRights(name, category_p, resourceId_p, accessRights_p);
        }
    }

    private void setAllRolesAccessMask(OwRoleManager roleManager_p, int category_p, String resourceId_p, int accessMask_p) throws Exception
    {
        OwFNCM5Credentials credentials = network.getCredentials();
        OwUserInfo ui = credentials.getUserInfo();
        Collection<String> roleNames = ui.getRoleNames();
        for (String name : roleNames)
        {
            roleManager_p.setAccessMask(name, category_p, resourceId_p, accessMask_p);
        }
    }

    @Test
    public void testViewAccessRight_properties() throws Exception
    {
        OwRoleManager roleManager = this.network.getRoleManager();
        OwFNCM5Class<?, ?> emailClass = network.getObjectClass(EMAIL, null);

        final String to = "To";
        final String from = "From";

        //TODO readonly vs hidden (access mask)

        setAllRolesAccessRights(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_VIEW, EMAIL, to), OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        setAllRolesAccessMask(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_VIEW, EMAIL, to), OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW);

        setAllRolesAccessRights(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_CREATE, EMAIL, to), OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        setAllRolesAccessMask(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_CREATE, EMAIL, to), OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_MODIFY);

        setAllRolesAccessRights(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_VIEW, EMAIL, from), OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        setAllRolesAccessMask(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_VIEW, EMAIL, from), OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW);

        setAllRolesAccessRights(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_CREATE, EMAIL, from), OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        setAllRolesAccessMask(roleManager, OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, toPropertyResource(OwRoleManager.ROLE_RESOURCE_CONTEXT_CREATE, EMAIL, from), OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW);

        {
            OwFNCM5PropertyClass toPropertyClass = emailClass.getPropertyClass(to);
            assertFalse(toPropertyClass.isReadOnly(OwPropertyClass.CONTEXT_NORMAL));
            assertFalse(toPropertyClass.isHidden(OwPropertyClass.CONTEXT_NORMAL));

            assertTrue(toPropertyClass.isReadOnly(OwPropertyClass.CONTEXT_ON_CREATE));
            assertTrue(toPropertyClass.isHidden(OwPropertyClass.CONTEXT_ON_CREATE));

        }

        {
            OwFNCM5PropertyClass fromPropertyClass = emailClass.getPropertyClass(from);
            assertTrue(fromPropertyClass.isReadOnly(OwPropertyClass.CONTEXT_NORMAL));
            assertTrue(fromPropertyClass.isHidden(OwPropertyClass.CONTEXT_NORMAL));

            assertFalse(fromPropertyClass.isReadOnly(OwPropertyClass.CONTEXT_ON_CREATE));
            assertFalse(fromPropertyClass.isHidden(OwPropertyClass.CONTEXT_ON_CREATE));

        }
    }

}
