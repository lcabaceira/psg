package com.wewebu.ow.server.roleimpl.dbrole;

import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * OwDBRoleManagerDummyAdapterTest.
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
public abstract class OwDBRoleManagerDummyAdapterTest extends OwDBRoleManagerTestBase
{

    final public static String JUNIT_TEST_ROLE = "JUNIT_TEST_ROLE";
    final public static int JUNIT_TEST_CATEGORY = 9999;
    final public static String JUNIT_TEST_RERSOURCE = "JUNIT_TEST_RERSOURCE";

    final public static int JUNIT_TEST_BITMASK_00 = 0;
    final public static int JUNIT_TEST_BITMASK_01 = 1;
    final public static int JUNIT_TEST_BITMASK_10 = 2;
    final public static int JUNIT_TEST_BITMASK_11 = 3;

    public OwDBRoleManagerDummyAdapterTest(String arg0_p)
    {
        super(arg0_p);
    }

    public void testGetCategories() throws Exception
    {
        Collection categories = m_roleManager.getCategories();
        assertNotNull(categories);
        Iterator catIt = categories.iterator();
        while (catIt.hasNext())
        {
            // we can use instanceof here. Its a test case and not productive. And we can use assertRue() this way.
            assertTrue(catIt.next() instanceof Integer);
        }
    }

    public void testGetResources() throws Exception
    {
        Collection categories = m_roleManager.getCategories();
        assertNotNull(categories);
        Iterator catIt = categories.iterator();
        while (catIt.hasNext())
        {
            Integer categrory = (Integer) catIt.next();
            Collection resources = m_roleManager.getResources(categrory.intValue());
            assertNotNull(resources);
            Iterator resIt = resources.iterator();
            while (resIt.hasNext())
            {
                Object res = resIt.next();
                // we can use instanceof here. Its a test case and not productive. And we can use assertRue() this way.
                assertTrue(res instanceof String);
                assertNotNull(m_roleManager.getResourceDisplayName(m_network.getLocale(), categrory.intValue(), (String) res));
            }
        }
    }

    public void testCanExplicitDeny() throws Exception
    {
        // we need explicit deny for the following tests
        assertTrue(m_roleManager.canExplicitDeny());
    }

    public void testAccessRightModification() throws Exception
    {
        // test ROLE_ACCESS_RIGHT_NOT_ALLOWED
        m_roleManager.setAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        assertEquals(OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED, m_roleManager.getAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // test ROLE_ACCESS_RIGHT_ALLOWED
        m_roleManager.setAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        assertEquals(OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED, m_roleManager.getAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // test ROLE_ACCESS_RIGHT_DENIED
        m_roleManager.setAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_DENIED);
        assertEquals(OwRoleManager.ROLE_ACCESS_RIGHT_DENIED, m_roleManager.getAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // reset
        m_roleManager.setAccessRights(JUNIT_TEST_ROLE, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
    }

    public void testResourceAccess() throws Exception
    {
        // get all roles of the current user.
        Collection roles = m_roleManager.getFilteredRoles();
        Iterator roleIt = roles.iterator();
        // we need at least two roles to play around with
        assertTrue(roles.size() >= 2);
        String testRoleA = (String) roleIt.next();
        String testRoleB = (String) roleIt.next();
        // clear the access rights of all role names
        roleIt = roles.iterator();
        while (roleIt.hasNext())
        {
            String roleName = (String) roleIt.next();
            m_roleManager.setAccessRights(roleName, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        }
        // test that the current user has no access to the resource
        assertFalse(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // allow for one role and validate that user has access
        m_roleManager.setAccessRights(testRoleA, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        assertTrue(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // allow for the second role and validate that user has still access
        m_roleManager.setAccessRights(testRoleB, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        assertTrue(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // dis-allow for the first role and validate that user has still access
        m_roleManager.setAccessRights(testRoleA, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        assertTrue(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // also dis-allow for second role and validate that user has no more access
        m_roleManager.setAccessRights(testRoleB, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        assertFalse(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
    }

    public void testExplicitDeny() throws Exception
    {
        // get all roles of the current user.
        Collection roles = m_roleManager.getFilteredRoles();
        Iterator roleIt = roles.iterator();
        // we need at least two roles to play around with
        assertTrue(roles.size() >= 2);
        String testRoleA = (String) roleIt.next();
        String testRoleB = (String) roleIt.next();
        // clear the access rights of all role names
        roleIt = roles.iterator();
        while (roleIt.hasNext())
        {
            String roleName = (String) roleIt.next();
            m_roleManager.setAccessRights(roleName, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        }
        // test that the current user has no access to the resource
        assertFalse(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // allow for one role and validate that user has access
        m_roleManager.setAccessRights(testRoleA, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
        assertTrue(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
        // explicit deny for one role and validate that user has no access
        m_roleManager.setAccessRights(testRoleB, JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_DENIED);
        assertFalse(m_roleManager.isAllowed(JUNIT_TEST_CATEGORY, JUNIT_TEST_RERSOURCE));
    }

    public void testAccessMaskModification() throws Exception
    {
        //
        // We have to use OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES here because the role manager limits all access masks
        // to the maximum available mask. And JUNIT_TEST_CATEGORY has a max_mask of 0.
        //
        // JUNIT_TEST_BITMASK_00
        m_roleManager.setAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_00);
        assertEquals(JUNIT_TEST_BITMASK_00, m_roleManager.getAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE));
        // JUNIT_TEST_BITMASK_01
        m_roleManager.setAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_01);
        assertEquals(JUNIT_TEST_BITMASK_01, m_roleManager.getAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE));
        // JUNIT_TEST_BITMASK_10
        m_roleManager.setAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_10);
        assertEquals(JUNIT_TEST_BITMASK_10, m_roleManager.getAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE));
        // JUNIT_TEST_BITMASK_11
        m_roleManager.setAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_11);
        assertEquals(JUNIT_TEST_BITMASK_11, m_roleManager.getAccessMask(JUNIT_TEST_ROLE, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE));
    }

    public void testAccessMask() throws Exception
    {
        //
        // We have to use OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES here because the role manager limits all access masks
        // to the maximum available mask. And JUNIT_TEST_CATEGORY has a max_mask of 0.
        //
        // get all roles of the current user.
        Collection roles = m_roleManager.getFilteredRoles();
        Iterator roleIt = roles.iterator();
        // we need at least two roles to play around with
        assertTrue(roles.size() >= 2);
        String testRoleA = (String) roleIt.next();
        String testRoleB = (String) roleIt.next();
        // activate resource in all role names and set access mask to 0
        roleIt = roles.iterator();
        while (roleIt.hasNext())
        {
            String roleName = (String) roleIt.next();
            m_roleManager.setAccessRights(roleName, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED);
            m_roleManager.setAccessMask(roleName, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_00);
        }
        // test that the current user is allowed
        assertTrue(m_roleManager.isAllowed(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE));
        // test that the current user has zero access mask
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_00));
        assertFalse(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_01));
        assertFalse(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_10));
        assertFalse(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_11));
        // set bit 1 in roleA
        m_roleManager.setAccessMask(testRoleA, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_01);
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_00));
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_01));
        assertFalse(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_10));
        assertFalse(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_11));
        // additionally set bit 2 in roleB
        m_roleManager.setAccessMask(testRoleB, OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_10);
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_00));
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_01));
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_10));
        assertTrue(m_roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, JUNIT_TEST_RERSOURCE, JUNIT_TEST_BITMASK_11));
    }

}