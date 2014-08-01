package com.wewebu.ow.server.plug.owconfig;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwCategoryInfo;
import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwResourceInfo;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Basic implementation of {@link OwCategoryInfo} based views.
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
 *@since 4.0.0.0
 */
public abstract class OwRoleConfigurationAccessRightsView extends OwView
{
    /**
     * Map containing the keys for texts to be used as titles for combo that display groups for a category. 
     */
    private static Map TITLES_4_CATEGORIES = new HashMap();
    static
    {
        TITLES_4_CATEGORIES.put("" + OwRoleManager.ROLE_CATEGORY_PLUGIN, "owstd.OwRoleConfigurationAccessRightsListView.availablePlugins");
        TITLES_4_CATEGORIES.put("" + OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, "owstd.OwRoleConfigurationAccessRightsListView.availableClasses");
    }

    /** flag indicating if this view is rendered in read only mode */
    private boolean m_readOnly = false;

    /** the category to display */
    private OwConfigurationDocument.OwCategoryInfo m_Category = null;

    /** flag indicating if this view renders a 'deny' column */
    private boolean m_supportDeny = false;

    /** flag indicating if the access mask should be editable */
    private boolean m_persistAccessMask = false;

    /**
     * Map that holds the current selected group for each category.
     */
    private Map m_categories2CurrentGroupMap;

    /**
     * Map containing the category IDs as string (key) and a list of groups for each category.
     * The values in this map are java.util.List group objects.
     */
    private Map m_categories2GroupsMap;

    /** list of all resources in the current category */
    protected List m_resources;

    protected Map m_accessMaskDescriptions;

    /**
     * Indicates if this view is rendered in read only mode
     * @return true = do not render check boxes; false = render check boxes to change the current values
     */
    public boolean isReadOnly()
    {
        return (m_readOnly);
    }

    /**
     * Set the flag indicating if this view is rendered in read only mode
     * @param readOnly_p true = do not render check boxes; false = render check boxes to change the current values
     */
    void setReadOnly(boolean readOnly_p)
    {
        m_readOnly = readOnly_p;
    }

    /**
     * set the category to display
     * @param category_p the new category to display
     */
    void setCategory(OwConfigurationDocument.OwCategoryInfo category_p)
    {
        if (m_categories2GroupsMap == null)
        {
            m_categories2GroupsMap = new HashMap();
        }

        m_Category = category_p;
        if (category_p != null)
        {
            m_categories2GroupsMap.remove("" + category_p.getId());
            m_resources = category_p.getResources();
            m_accessMaskDescriptions = category_p.getAccessMaskDescriptions();
            createGroups(m_resources, category_p);
        }
        else
        {
            m_resources = null;
            m_accessMaskDescriptions = null;
        }
    }

    public OwConfigurationDocument.OwCategoryInfo getCategory()
    {
        return m_Category;
    }

    /**
     * Set the flag indicating if this view renders a 'deny' column
     * @param supportDeny_p true = render a 'deny' column;  false = render only a 'allow' column
     */
    public void setSupportDeny(boolean supportDeny_p)
    {
        m_supportDeny = supportDeny_p;
    }

    /**
     * Indicates if this view renders a deny column. This method is called by it's corresponding JSP page.
     * @return true = render a 'deny' column;  false = render only a 'allow' column
     */
    public boolean getSupportDeny()
    {
        return (m_supportDeny);
    }

    public void setPersistAccessMask(boolean persistAccessMask_p)
    {
        m_persistAccessMask = persistAccessMask_p;
    }

    public boolean getPersistAccessMask()
    {
        return (m_persistAccessMask);
    }

    /**
     * Creates the groups from a list of resources.
     * @param resources_p
     * @param category_p
     */
    private void createGroups(List resources_p, OwConfigurationDocument.OwCategoryInfo category_p)
    {
        if (m_categories2GroupsMap != null)
        {
            m_categories2CurrentGroupMap = new HashMap();
            if (m_categories2GroupsMap.get("" + category_p.getId()) == null)
            {
                List groups = new LinkedList();
                String lastGroup = null;
                int currentGroupId = -1;
                OwResourceInfoGroup currentGroupObj = OwResourceInfoGroup.createSurrogateGroup();
                // calculate number of columns for colspan

                for (int i = 0; i < m_resources.size(); i++)
                {
                    OwResourceInfo resInfo = (OwResourceInfo) m_resources.get(i);

                    // get the display name
                    String resourceDisplayName = resInfo.getDisplayName();
                    // detect current group name
                    String currentGroup = null;
                    if ((resourceDisplayName != null) && (resourceDisplayName.length() > 1) && (resourceDisplayName.charAt(0) == '@'))
                    {
                        String temp = resourceDisplayName.substring(1);
                        int endmarkerpos = temp.indexOf('@');
                        if (endmarkerpos >= 0)
                        {
                            currentGroup = temp.substring(0, endmarkerpos);
                            resourceDisplayName = temp.substring(endmarkerpos + 1);
                        }
                    }
                    // write new group header (if required)
                    if (((currentGroup != null) && ((lastGroup == null) || (!currentGroup.equals(lastGroup)))) || ((lastGroup != null) && ((currentGroup == null) || (!lastGroup.equals(currentGroup)))))
                    {
                        lastGroup = currentGroup;
                        currentGroupId++;
                        if (category_p.getId() == OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS)
                        {
                            currentGroupObj = new OwResourceInfoGroup(currentGroup, currentGroupId, currentGroup, new Comparator() {

                                public int compare(Object o1_p, Object o2_p)
                                {
                                    int result = -1;
                                    //compare only OwReosourceInfo
                                    if (o1_p instanceof OwResourceInfo && o2_p instanceof OwResourceInfo)
                                    {
                                        OwResourceInfo r1 = (OwResourceInfo) o1_p;
                                        OwResourceInfo r2 = (OwResourceInfo) o2_p;
                                        String name1 = r1.getDisplayName();
                                        String name2 = r2.getDisplayName();
                                        if (name1.indexOf('.') != -1 && name1.indexOf('.') != -1)
                                        {
                                            String toBeCompared1 = name1.substring(name1.lastIndexOf('.'));
                                            String toBeCompared2 = name2.substring(name2.lastIndexOf('.'));
                                            result = toBeCompared1.compareTo(toBeCompared2);
                                        }
                                        else
                                        {
                                            result = (-1) * name1.compareTo(name2);
                                        }
                                    }
                                    return result;
                                }
                            });
                        }
                        else
                        {
                            currentGroupObj = new OwResourceInfoGroup(currentGroup, currentGroupId, currentGroup);
                        }
                        groups.add(currentGroupObj);
                        // writeGroupHeader(w_p, numcols, currentGroupId, currentGroup);
                    }
                    currentGroupObj.addResourceInfo(resInfo);
                    // writeRow(w_p, currentGroupId, i, resInfo, resourceDisplayName);
                }
                if (category_p.getId() == OwRoleManager.ROLE_CATEGORY_PLUGIN)
                {
                    OwResourceInfoGroup allPluginsGroup = new OwResourceInfoGroup(OwResourceInfoGroup.GROUP_NAME_ALL, ++currentGroupId, getContext().localize("app.OwConfiguration.groupAll.name", "All"));
                    allPluginsGroup.addAllResourceInfo((OwResourceInfo[]) m_resources.toArray(new OwResourceInfo[m_resources.size()]));
                    groups.add(allPluginsGroup);
                }
                if (groups.size() == 0)
                {
                    groups.add(currentGroupObj);
                }
                Collections.sort(groups);
                m_categories2GroupsMap.put("" + category_p.getId(), groups);
            }
            m_categories2CurrentGroupMap.put("" + category_p.getId(), ((List) m_categories2GroupsMap.get("" + category_p.getId())).get(0));
        }
    }

    /**
     * Get current group for selected category.
     * @return current group for selected category.
     */
    public OwResourceInfoGroup getCurrentGroup()
    {
        return (OwResourceInfoGroup) m_categories2CurrentGroupMap.get(getCurrentCategoryId());
    }

    /**
     * set current group for selected category
     * @param group_p the new group to be the current group
     */
    public void setCurrentGroup(OwResourceInfoGroup group_p)
    {
        m_categories2CurrentGroupMap.put(getCurrentCategoryId(), group_p);
    }

    /**
     * Get current category ID as a java.lang.String, easy mode to be used in maps.
     * @return the current category ID as string.
     */
    private String getCurrentCategoryId()
    {
        String result = "-1";
        if (getCategory() != null)
        {
            result = "" + getCategory().getId();
        }
        return result;
    }

    /**
     * check if current category has multiple access groups.
     * @return <code>true</code> is current category has more than one group.
     */
    public boolean hasMultipleAccessGroups()
    {
        boolean result = false;
        if (getCategory() != null)
        {
            List groupsForCategory = (List) m_categories2GroupsMap.get(getCurrentCategoryId());
            if (groupsForCategory != null)
            {
                result = groupsForCategory.size() > 1;
            }
        }
        return result;
    }

    /**
     * Gets a list of groups for current category.
     * @return a list of groups for current category.
     */
    public List getGroups()
    {
        List groupsForCategory = (List) m_categories2GroupsMap.get(getCurrentCategoryId());
        return groupsForCategory;
    }

    /** 
     * Called from JSP to select the group.
     * @param request_p a HttpServletRequest object
     */
    public void onSelectGroup(HttpServletRequest request_p) throws Exception
    {
        int groupId = -1;
        try
        {
            groupId = Integer.parseInt(request_p.getParameter("groupId"));
        }
        catch (Exception e)
        {
            // invalid parameter. do nothing and return
            return;
        }
        List groups = (List) m_categories2GroupsMap.get(getCurrentCategoryId());
        OwResourceInfoGroup currentGroup = null;
        for (Iterator iterator = groups.iterator(); iterator.hasNext();)
        {
            OwResourceInfoGroup group = (OwResourceInfoGroup) iterator.next();
            if (group.getGroupId() == groupId)
            {
                currentGroup = group;
                break;
            }

        }
        if (currentGroup != null)
        {
            m_categories2CurrentGroupMap.put(getCurrentCategoryId(), currentGroup);
        }
    }

    /**
     * Gets the title for combo box where the groups are displayed.
     * @return the title.
     */
    public String getGroupSelectionTitle()
    {
        String titleId = (String) TITLES_4_CATEGORIES.get(getCurrentCategoryId());
        String result = "Available Groups";
        if (titleId != null)
        {
            result = getContext().localize(titleId, result);
        }
        return result;
    }
}
