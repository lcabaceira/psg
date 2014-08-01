package com.wewebu.ow.server.roleimpl.dbrole;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * .
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
public class OwDBRoleManagerCategoriesConfigurationTest extends OwDBRoleManagerTestBase
{
    private static final Logger LOG = Logger.getLogger(OwDBRoleManagerCategoriesConfigurationTest.class);

    //<ConfigCategories>ROLE_CATEGORY_PLUGIN, ROLE_CATEGORY_VIRTUAL_QUEUE,  ROLE_CATEGORY_SEARCH_TEMPLATE,</ConfigCategories>
    private static final Set<Integer> CONFIGURED_CATEGORIES = new LinkedHashSet<>();

    public OwDBRoleManagerCategoriesConfigurationTest(String arg0_p)
    {
        super(arg0_p);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        CONFIGURED_CATEGORIES.add(Integer.valueOf(OwRoleManager.ROLE_CATEGORY_PLUGIN));
        CONFIGURED_CATEGORIES.add(Integer.valueOf(OwRoleManager.ROLE_CATEGORY_VIRTUAL_QUEUE));
        CONFIGURED_CATEGORIES.add(Integer.valueOf(OwRoleManager.ROLE_CATEGORY_SEARCH_TEMPLATE));
    }

    public void testGetConfiguredCategories()
    {
        Collection<Object> categories = m_roleManager.getConfiguredCategories();
        assertNotNull(categories);
        assertEquals(CONFIGURED_CATEGORIES.size(), categories.size());
        Iterator<Object> catIt = categories.iterator();
        while (catIt.hasNext())
        {
            Integer configuredCategory = (Integer) catIt.next();
            assertTrue(CONFIGURED_CATEGORIES.remove(configuredCategory));
        }
        assertTrue(CONFIGURED_CATEGORIES.isEmpty());
    }

    /**
     * Test if the categories that are not configurated are allowed to use any resource.
     * @throws Exception
     */
    public void testIsAllowedForNonConfiguredCategory() throws Exception
    {
        Collection categories = m_roleManager.getConfiguredCategories();
        Collection<?> allowedCategories = m_roleManager.getCategories();
        allowedCategories.removeAll(categories);
        assertNotNull(allowedCategories);
        Iterator<?> catIt = allowedCategories.iterator();
        while (catIt.hasNext())
        {
            Integer allowedCategory = (Integer) catIt.next();
            if (m_roleManager.isStaticResourceCategory(allowedCategory))
            {
                assertTrue(m_roleManager.isAllowed(allowedCategory.intValue(), "dummy_resource_id"));
            }
            else
            {
                try
                {

                    m_roleManager.isAllowed(allowedCategory.intValue(), "dummy_resource_id");
                    fail("Should not be possible to call isAllowed for dynamic categories.");
                }
                catch (OwInvalidOperationException e)
                {
                    LOG.info(e.getMessage() + " as expected.");
                }

            }
        }

    }

    protected String getTestBasename()
    {
        return "dbrole_mssql_goodConfiguration";
    }

}
