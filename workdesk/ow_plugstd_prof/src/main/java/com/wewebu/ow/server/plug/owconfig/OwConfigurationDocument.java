package com.wewebu.ow.server.plug.owconfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.role.OwIntegratedApplicationRoleManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Role configuration.<br>
 * Created after login to the ECM System. 
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
public class OwConfigurationDocument extends OwMasterDocument
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwConfigurationDocument.class);

    public class OwResourceInfo implements Comparable
    {
        private int m_categoryId;
        private String m_resourceId;
        private String m_displayName;

        public OwResourceInfo(int categoryId_p, String resourceId_p, String displayName_p)
        {
            m_categoryId = categoryId_p;
            m_resourceId = resourceId_p;
            m_displayName = displayName_p;
        }

        public String getId()
        {
            return (m_resourceId);
        }

        public String getDisplayName()
        {
            return (m_displayName);
        }

        public int getAccessRights() throws Exception
        {
            return (m_RoleManager.getAccessRights(m_currentRoleName, m_categoryId, m_resourceId));
        }

        public void setAccessRights(int newAccessRights_p) throws Exception
        {
            m_RoleManager.setAccessRights(m_currentRoleName, m_categoryId, m_resourceId, newAccessRights_p);
        }

        public void replaceWith(String resourceId_p) throws Exception
        {
            m_RoleManager.replaceResource(m_currentRoleName, m_categoryId, m_resourceId, resourceId_p);
            this.m_resourceId = resourceId_p;
            Locale locale = getContext().getLocale();
            this.m_displayName = m_RoleManager.getResourceDisplayName(locale, m_categoryId, this.m_resourceId);
        }

        public int getAccessMask() throws OwException
        {
            return m_RoleManager.getAccessMask(m_currentRoleName, m_categoryId, m_resourceId);
        }

        public void setAccessMask(int newAccessMask_p) throws Exception
        {
            m_RoleManager.setAccessMask(m_currentRoleName, m_categoryId, m_resourceId, newAccessMask_p);
        }

        public int compareTo(Object o_p)
        {
            int result = -1;
            if (o_p instanceof OwResourceInfo)
            {
                OwResourceInfo toBeCompared = (OwResourceInfo) o_p;
                if (toBeCompared.m_displayName != null)
                {
                    result = (-1) * toBeCompared.m_displayName.compareTo(m_displayName);
                }
            }
            return result;
        }
    }

    /** inner class keeping id, displayName and resources list for each category */
    public class OwCategoryInfo
    {
        private int m_categoryId;
        private Locale m_locale;
        private String m_displayName;
        private boolean m_isPrepared;
        private List m_cachedResources;
        private Map m_cachedAccessMaskDescriptions;

        public OwCategoryInfo(int categoryId_p, String displayName_p)
        {
            m_categoryId = categoryId_p;
            m_locale = getContext().getLocale();
            m_displayName = m_RoleManager.getCategoryDisplayName(m_locale, m_categoryId);
            m_isPrepared = false;
        }

        public int getId()
        {
            return (m_categoryId);
        }

        public String getDisplayName()
        {
            return (m_displayName);
        }

        public List getResources(String role_p, int rights_p, int accessMask_p) throws Exception
        {
            List<OwResourceInfo> resources = getResources();
            List<OwResourceInfo> filteredResources = new LinkedList<OwConfigurationDocument.OwResourceInfo>();
            for (OwResourceInfo resourceInfo : resources)
            {
                int accessMask = m_RoleManager.getAccessMask(role_p, m_categoryId, resourceInfo.getId());
                int rights = m_RoleManager.getAccessRights(role_p, m_categoryId, resourceInfo.getId());
                if ((accessMask & accessMask_p) == accessMask_p && (rights & rights_p) == rights_p)
                {
                    filteredResources.add(resourceInfo);
                }
            }
            return filteredResources;
        }

        public List getResources()
        {
            try
            {
                prepare();
                return m_cachedResources;
            }
            catch (Exception e)
            {
                // log exception
                LOG.error("Error preparing category", e);
                // return empty list
                return new ArrayList();
            }
        }

        /**
         * 
         *@return true if the given category is a static resource category (i.e. categories that have a 
         *         predefined resource set associated with them - new resources can not be added directly)
         * @since 4.0.0.0
         */
        public boolean isStaticResourceCategory()
        {
            return m_RoleManager.isStaticResourceCategory(m_categoryId);
        }

        public void addNewResource(String role_p, String resourceId_p, int accessRights_p, int newAccessMask_p) throws Exception
        {
            m_RoleManager.setAccessRights(role_p, m_categoryId, resourceId_p, accessRights_p);
            m_RoleManager.setAccessMask(role_p, m_categoryId, resourceId_p, newAccessMask_p);
            m_isPrepared = false;
        }

        public Map getAccessMaskDescriptions()
        {
            try
            {
                prepare();
                return m_cachedAccessMaskDescriptions;
            }
            catch (Exception e)
            {
                // log exception
                LOG.error("Error preparing category", e);
                // return empty map
                return new HashMap();
            }
        }

        public void prepare() throws Exception
        {
            // skip if already prepared
            if (m_isPrepared)
            {
                return;
            }
            // get all resources from role manager
            Collection resources = null;
            resources = m_RoleManager.getResources(m_categoryId);
            // create result list
            m_cachedResources = new ArrayList();
            // add all resources to result list
            Iterator it = resources.iterator();
            while (it.hasNext())
            {
                String resourceId = (String) it.next();
                String displayName = m_RoleManager.getResourceDisplayName(m_locale, m_categoryId, resourceId);
                m_cachedResources.add(new OwResourceInfo(m_categoryId, resourceId, displayName));
            }
            // get Map of access mask flags
            m_cachedAccessMaskDescriptions = m_RoleManager.getAccessMaskDescriptions(m_categoryId);
            // mark as prepared
            m_isPrepared = true;
        }
    }

    /** currently selected role name */
    private String m_currentRoleName = null;

    /** define if the currently selected role is a global role */
    private boolean m_currentRoleIsGlobalRole = false;

    /** cached list of categories */
    private Map m_CategroiesList = null;

    /** currently selected category */
    private OwCategoryInfo m_currentCategory = null;

    /** reference to the list displaying the access rights for a category */
    private OwRoleConfigurationAccessRightsView m_AccessRightsView = null;

    /** the rolemanager we are using as back-end */
    private OwRoleManager m_RoleManager;

    /** defines if this master plugin offers explicit deny. this depends on the back-end role manager we are using */
    private boolean m_CanExplicitDeny = false;

    /** signals whether the role manager can persist changes to the access mask or not */
    private boolean m_CanPersistAccessMask = false;

    /** defines if the current user may modify global roles or not */
    private boolean m_isGlobalRoleModificationAllowed = false;

    /**
     * initialize this MasterDocument after the context has been set.
     */
    public void init() throws Exception
    {
        // get the back-end role manager from the context
        m_RoleManager = ((OwMainAppContext) getContext()).getRoleManager();
        // sanity check
        if (m_RoleManager == null)
        {
            String msg = "OwConfigurationDocument.init: There is no role manager defined. The owconfig master plugin needs a role manager as back-end.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        initFeatures();
    }

    private void initFeatures()
    {
        // detect explicit deny feature
        m_CanExplicitDeny = m_RoleManager.canExplicitDeny();
        // detect if changes to the access mask can be persisted
        m_CanPersistAccessMask = m_RoleManager.canPersistAccessMask();
        // check if user is allowed to modify global roles
        m_isGlobalRoleModificationAllowed = m_RoleManager.isGlobalRoleModificationAllowed();
    }

    /** 
     * This master plugin edits one role at a time. This method returns the name
     * of the currently selected role.
     * @return currently selected role name
     */
    public String getRoleName()
    {
        return (m_currentRoleName);
    }

    /**
     * This master plugin edits one role at a time. This method sets the name
     * of the currently selected role.
     * @param roleName_p role to be edited.
     */
    public void setRoleName(String roleName_p)
    {
        m_currentRoleName = roleName_p;
        m_currentRoleIsGlobalRole = m_RoleManager.isGlobalRole(roleName_p);
        if (m_AccessRightsView != null)
        {
            m_AccessRightsView.setReadOnly(m_currentRoleIsGlobalRole && (!m_isGlobalRoleModificationAllowed));
        }
    }

    /** 
     * retrieve the list of all categories from the role manager and return it as a Map of CategoryInfo objects
     * @return Map of CategoryInfo objects
     */
    public synchronized Map<Integer, OwCategoryInfo> getCategoryInfos()
    {
        if (m_CategroiesList == null)
        {
            m_CategroiesList = new HashMap();
            Collection categroyIds = m_RoleManager.getConfiguredCategories();
            if (categroyIds != null)
            {
                Iterator it = categroyIds.iterator();
                while (it.hasNext())
                {
                    Integer categoryId = (Integer) it.next();
                    OwCategoryInfo newCategoryInfo = new OwCategoryInfo(categoryId.intValue(), m_RoleManager.getCategoryDisplayName(getContext().getLocale(), categoryId.intValue()));
                    m_CategroiesList.put(categoryId, newCategoryInfo);
                }
            }
        }
        return (m_CategroiesList);
    }

    /** 
     * get the currently selected CategoryInfo object
     * @return the currently selected CategoryInfo object
     */
    public synchronized OwCategoryInfo getCurrentCategory()
    {
        if (m_currentCategory == null)
        {
            // select the first category
            Map categories = getCategoryInfos();
            if ((categories != null) && (categories.values() != null) && (categories.values().iterator().hasNext()))
            {
                m_currentCategory = (OwCategoryInfo) categories.values().iterator().next();
                // update list view
                if (m_AccessRightsView != null)
                {
                    m_AccessRightsView.setCategory(m_currentCategory);
                }
            }
        }
        return (m_currentCategory);
    }

    /** 
     * set a new currently selected CategoryInfo object
     * @param category_p the new category object
     */
    public void setCurrentCategory(OwCategoryInfo category_p) throws Exception
    {
        if (category_p != null)
        {
            // (try to) prepare the new category. Will throw Exception if the new category is not available.
            category_p.prepare();
            // set current category
            m_currentCategory = category_p;
            // update list view
            if (m_AccessRightsView != null)
            {
                m_AccessRightsView.setCategory(m_currentCategory);
            }
        }
    }

    /**
     * get the list view used to display the access rights
     * @return list view
     */
    public OwRoleConfigurationAccessRightsView getAccessRightsView()
    {
        return (m_AccessRightsView);
    }

    /**
     * set the list view used to display the access rights
     * @param accessRightsView_p list view used to display the access rights
     */
    public void setAccessRightsView(OwRoleConfigurationAccessRightsView accessRightsView_p)
    {
        m_AccessRightsView = accessRightsView_p;
        if (m_AccessRightsView != null)
        {
            m_AccessRightsView.setCategory(m_currentCategory);
            m_AccessRightsView.setSupportDeny(m_CanExplicitDeny);
            m_AccessRightsView.setPersistAccessMask(m_CanPersistAccessMask);
            m_AccessRightsView.setReadOnly(m_currentRoleIsGlobalRole && (!m_isGlobalRoleModificationAllowed));
        }
    }

    /** check if explicit deny is available. This depends on the features of the back-end rolemanager.
     * @return true = explicitDeny is available;  false = explicit deny is not available
     */
    public boolean canExplicitDeny()
    {
        return (m_CanExplicitDeny);
    }

    public boolean canPersistAccessMask()
    {
        return (m_CanPersistAccessMask);
    }

    public Map<String, String> getIntegratedApplicationNames()
    {
        Map<String, String> names = Collections.EMPTY_MAP;

        if (m_RoleManager instanceof OwIntegratedApplicationRoleManager)
        {
            OwIntegratedApplicationRoleManager integratedRoleManager = (OwIntegratedApplicationRoleManager) m_RoleManager;
            Map<String, OwString> localizableNames = integratedRoleManager.getIntegratedApplicationsNames();
            if (!localizableNames.isEmpty())
            {
                Locale locale = getContext().getLocale();
                names = new HashMap<String, String>();
                Set<Entry<String, OwString>> namesEntries = localizableNames.entrySet();
                for (Entry<String, OwString> nameEntry : namesEntries)
                {
                    names.put(nameEntry.getKey(), nameEntry.getValue().getString(locale));
                }
            }
        }

        return names;
    }

    public synchronized void setCurrentApplication(String appId) throws OwException
    {
        if (m_RoleManager instanceof OwIntegratedApplicationRoleManager)
        {
            OwIntegratedApplicationRoleManager integratedRoleManager = (OwIntegratedApplicationRoleManager) m_RoleManager;
            m_RoleManager = integratedRoleManager.createIntegratedRoleManager(appId);
            m_CategroiesList = null;
            m_CategroiesList = getCategoryInfos();

            if (!m_CategroiesList.containsKey(m_currentCategory.getId()))
            {
                m_currentCategory = null;
            }

            initFeatures();
        }
    }

    public String getApplicationId()
    {
        if (m_RoleManager instanceof OwIntegratedApplicationRoleManager)
        {
            OwIntegratedApplicationRoleManager integratedRoleManager = (OwIntegratedApplicationRoleManager) m_RoleManager;
            return integratedRoleManager.getApplicationId();
        }
        else
        {
            return null;
        }
    }

}