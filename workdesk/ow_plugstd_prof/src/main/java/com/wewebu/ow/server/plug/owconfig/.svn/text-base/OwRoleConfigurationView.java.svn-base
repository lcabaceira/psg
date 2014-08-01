package com.wewebu.ow.server.plug.owconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwCategoryInfo;
import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwResourceInfo;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwDelegateView;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * This class renders the possible settings which can be edit for the given role (group) of users.
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
public class OwRoleConfigurationView extends OwLayout implements OwDialog.OwDialogListener
{

    /** name of the access rights region */
    public static final int ACCESS_RIGHTS_REGION = 1;
    /** layout region definition for the menu region */
    public static final int MENU_REGION = 2;

    private static final String ALLOWDENY_PARAMETER_PREFIX = "allowdeny_";
    public static final String RESOURCE_PREFIX = "r_";

    /** reference to the role dialog */
    protected OwUserSelectDialog m_dlgRole;

    /** recent list of role names */
    private List m_recentRoleNames = new ArrayList();

    /** size of recent list */
    private static final int RECENT_LIST_MAX_SIZE = 25;

    private OwDelegateView m_delegateAccessRightsView = null;

    /** view to display the list of access rights */
    private OwRoleConfigurationAccessRightsListView m_listView = null;
    private OwRoleConfigurationAccessRightsInfoView m_infoView = null;

    /** Menu for buttons in the view */
    protected OwSubMenuView m_MenuView;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_delegateAccessRightsView = new OwDelegateView();
        // create and add access rights list view

        m_listView = new OwRoleConfigurationAccessRightsListView();
        m_infoView = new OwRoleConfigurationAccessRightsInfoView();

        m_delegateAccessRightsView.setExternalFormTarget(this);
        addView(m_delegateAccessRightsView, ACCESS_RIGHTS_REGION, null);

        // create and add menu view
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, MENU_REGION, null);

        // add apply button
        int defaultButtonIndex = m_MenuView.addFormMenuItem(this, getContext().localize("app.OwRoleConfigurationView.apply", "Save"), "Apply", null);
        m_MenuView.setDefaultMenuItem(defaultButtonIndex);

        changeView(getCurrentCategory().getId());

    }

    /** to get additional form attributes used for the form
     *  Base class will then render a form automatically if this method is overwritten
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        return "";
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("owconfig/OwRoleConfigurationView.jsp", w_p);
    }

    /** called from JSP to get role from role dialogue
     * @param request_p a HttpServletRequest object
     * */
    public void onOpenRoleDialog(HttpServletRequest request_p) throws Exception
    {
        m_dlgRole = new OwUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_ROLE }, false);
        getContext().openDialog(m_dlgRole, this);
    }

    /** called from JSP to change to a recent role
     * @param request_p a HttpServletRequest object
     * */
    public void onChangeToRecentRole(HttpServletRequest request_p) throws Exception
    {
        int recentId = -1;
        try
        {
            recentId = Integer.parseInt(request_p.getParameter("recentRoleId"));
        }
        catch (Exception e)
        {
            // invalid parameter. do nothing and return
            return;
        }
        if ((recentId >= 0) && (recentId < m_recentRoleNames.size()))
        {
            // get recent role name from recent list
            String recentRoleName = (String) m_recentRoleNames.get(recentId);
            // remove all previous occurrences in the recent list
            m_recentRoleNames.remove(recentRoleName);
            // add to the end of the recent list
            m_recentRoleNames.add(recentRoleName);
            // crop the recent list to its maximum size
            while (m_recentRoleNames.size() > RECENT_LIST_MAX_SIZE)
            {
                m_recentRoleNames.remove(0);
            }
            // set new current role name
            ((OwConfigurationDocument) getDocument()).setRoleName(recentRoleName);
        }
    }

    /**
     * is called when the used closes the role selection dialog
     * @param dialogView_p the role selection dialog
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        List roles = ((OwUserSelectDialog) dialogView_p).getSelectedRoles();
        if (roles != null)
        {
            String selRole = (String) roles.get(0);
            if (selRole != null)
            {
                // remove all previous occurrences in the recent list
                m_recentRoleNames.remove(selRole);
                // add to the end of the recent list
                m_recentRoleNames.add(selRole);
                // crop the recent list to its maximum size
                while (m_recentRoleNames.size() > RECENT_LIST_MAX_SIZE)
                {
                    m_recentRoleNames.remove(0);
                }
                // set new current role name
                ((OwConfigurationDocument) getDocument()).setRoleName(selRole);
            }
        }
        m_dlgRole = null;
    }

    public String getCurrentRoleName()
    {
        return (((OwConfigurationDocument) getDocument()).getRoleName());
    }

    public List getRecentRoleNames()
    {
        return (m_recentRoleNames);
    }

    public Map<String, String> getIntegratedApplicationsNames()
    {
        return (((OwConfigurationDocument) getDocument()).getIntegratedApplicationNames());
    }

    public String getCurrentApplicationId()
    {
        return (((OwConfigurationDocument) getDocument()).getApplicationId());
    }

    public Map getCategoryInfos()
    {
        return (((OwConfigurationDocument) getDocument()).getCategoryInfos());
    }

    public OwCategoryInfo getCurrentCategory()
    {
        return (((OwConfigurationDocument) getDocument()).getCurrentCategory());
    }

    private void changeView(int categoryId_p) throws Exception
    {
        OwRoleConfigurationAccessRightsView view = null;

        if (OwRoleManager.ROLE_CATEGORY_STARTUP_FOLDER == categoryId_p)
        {
            view = m_infoView;
        }
        else
        {
            view = m_listView;
        }
        m_delegateAccessRightsView.setView(view);

        ((OwConfigurationDocument) getDocument()).setAccessRightsView(view);

    }

    public void onSelectApplication(HttpServletRequest request_p) throws Exception
    {
        try
        {
            String appId = request_p.getParameter("appId");
            ((OwConfigurationDocument) getDocument()).setCurrentApplication(appId);
            OwCategoryInfo category = getCurrentCategory();
            changeView(category.getId());
        }
        catch (Exception e)
        {
            // invalid parameter. do nothing and return
            return;
        }
    }

    /** called from JSP to select the category
     * @param request_p a HttpServletRequest object
     * */
    public void onSelectCategory(HttpServletRequest request_p) throws Exception
    {
        int categoryId = -1;
        try
        {
            categoryId = Integer.parseInt(request_p.getParameter("categoryId"));
            changeView(categoryId);
        }
        catch (Exception e)
        {
            // invalid parameter. do nothing and return
            return;
        }
        Map categories = getCategoryInfos();
        Object element = categories.get(Integer.valueOf(categoryId));
        if (element != null)
        {
            ((OwConfigurationDocument) getDocument()).setCurrentCategory((OwCategoryInfo) element);
        }
    }

    protected void applyStaticResouceCategory(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwConfigurationDocument configurationDocument = ((OwConfigurationDocument) getDocument());
        OwCategoryInfo currentCategory = configurationDocument.getCurrentCategory();
        OwRoleConfigurationAccessRightsView accessRightsView = ((OwConfigurationDocument) getDocument()).getAccessRightsView();

        OwResourceInfoGroup currentGroup = accessRightsView.getCurrentGroup();
        List allResources = (List) currentGroup.getResources();

        boolean isSelectiveConfiguration = currentCategory.getId() == OwRoleManager.ROLE_CATEGORY_SELECTIVE_CONFIGURATION;
        if (isSelectiveConfiguration)
        {
            Map selectiveConfigurationGroup = new HashMap();
            for (int i = 0; i < allResources.size(); i++)
            {
                // get old (current) access rights
                OwResourceInfo resInfo = (OwResourceInfo) allResources.get(i);
                // get new access rights
                int newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED;
                String allowDeny = request_p.getParameter(ALLOWDENY_PARAMETER_PREFIX + Integer.toString(i));

                if (OwResourceInfoGroupRenderer.IMPLDENY.equals(allowDeny))
                {
                    newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED;
                }
                else if (OwResourceInfoGroupRenderer.DENY.equals(allowDeny))
                {
                    newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_DENIED;
                }
                else if (OwResourceInfoGroupRenderer.ALLOW.equals(allowDeny))
                {
                    newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED;
                }

                // perform additional validation for category SELECTIVE_CONFIGURATION
                if (newAccessRights == OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED)
                {
                    int pointPos = resInfo.getId().lastIndexOf('.');
                    if (pointPos > -1)
                    {
                        String resourceGroupName = resInfo.getId().substring(0, pointPos);
                        if (selectiveConfigurationGroup.containsKey(resourceGroupName))
                        {
                            throw new OwInvalidOperationException(getContext().localize1("app.OwRoleConfigurationView.onlyoneselectiveconfigurationpergroup", "Only one selective configuration may be activated per group (here: %1).",
                                    resourceGroupName));
                        }
                        selectiveConfigurationGroup.put(resourceGroupName, Boolean.TRUE);
                    }
                }
            }
        }

        // get map of access mask flags
        Map accessMaskFlags = currentCategory.getAccessMaskDescriptions();

        // save changes to all resources
        for (int i = 0; i < allResources.size(); i++)
        {
            // get old (current) access rights
            OwResourceInfo resInfo = (OwResourceInfo) allResources.get(i);
            int oldAccessRights = resInfo.getAccessRights();
            // get new access rights
            int newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED;
            String allowDeny = request_p.getParameter(ALLOWDENY_PARAMETER_PREFIX + Integer.toString(i));

            if (OwResourceInfoGroupRenderer.IMPLDENY.equals(allowDeny))
            {
                newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED;
            }
            else if (OwResourceInfoGroupRenderer.DENY.equals(allowDeny))
            {
                newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_DENIED;
            }
            else if (OwResourceInfoGroupRenderer.ALLOW.equals(allowDeny))
            {
                newAccessRights = OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED;
            }
            // set new access rights if changed
            if (oldAccessRights != newAccessRights)
            {
                resInfo.setAccessRights(newAccessRights);
            }
            if (((OwConfigurationDocument) getDocument()).canPersistAccessMask())
            {
                // get current (old) access mask
                int oldAccessMask = resInfo.getAccessMask();
                // get new access mask
                int newAccessMask = 0;
                Iterator itFlags = accessMaskFlags.keySet().iterator();
                while (itFlags.hasNext())
                {
                    int flag = ((Integer) itFlags.next()).intValue();
                    String paramFlag = request_p.getParameter("accessMaskFlag_" + Integer.toString(flag) + "_" + Integer.toString(i));
                    if ((paramFlag != null) && paramFlag.equals("set"))
                    {
                        newAccessMask |= flag;
                    }
                }
                // set new access mask if changed
                if (oldAccessMask != newAccessMask)
                {
                    resInfo.setAccessMask(newAccessMask);
                }
            }
        }

        postSuccessMessage();

    }

    private void postSuccessMessage()
    {
        //post message that the changes will only take effect after re-login
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("owconfig.OwRoleConfigurationView.applyPostMsg", "Settings have been saved. Please logout and then login again to make these changes effective."));
    }

    protected void applyDynamicResouceCategory(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwConfigurationDocument configurationDocument = ((OwConfigurationDocument) getDocument());
        OwCategoryInfo currentCategory = configurationDocument.getCurrentCategory();
        OwRoleConfigurationAccessRightsView accessRightsView = ((OwConfigurationDocument) getDocument()).getAccessRightsView();

        String resourceParamenterName = OwRoleConfigurationView.RESOURCE_PREFIX + currentCategory.getId();
        String resource = request_p.getParameter(resourceParamenterName);

        String role = configurationDocument.getRoleName();

        int expectedAccessMask = OwRoleManager.ROLE_ACCESS_MASK_FLAG_DYNAMIC_RESOURCE_MODIFY;
        List allResources = currentCategory.getResources(role, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED, expectedAccessMask);

        if (allResources.isEmpty())
        {
            currentCategory.addNewResource(role, resource, OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED, expectedAccessMask);
        }
        else
        {
            for (Iterator i = allResources.iterator(); i.hasNext();)
            {
                OwResourceInfo resourceInfo = (OwResourceInfo) i.next();
                int right = resourceInfo.getAccessRights();
                int accessMask = resourceInfo.getAccessMask();
                int viewMask = accessMask & expectedAccessMask;
                if ((right == OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED) && (viewMask == expectedAccessMask))
                {
                    resourceInfo.replaceWith(resource);
                }
            }
        }
        accessRightsView.setCategory(currentCategory);

        postSuccessMessage();
    }

    /** event called when user clicks the Apply button in the menu 
     *  @param request_p a {@link HttpServletRequest}
     *  @param oReason_p Optional reason object submitted in addMenuItem
     */
    public void onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwConfigurationDocument configurationDocument = ((OwConfigurationDocument) getDocument());
        OwCategoryInfo currentCategory = configurationDocument.getCurrentCategory();

        if (!currentCategory.isStaticResourceCategory())
        {
            applyDynamicResouceCategory(request_p, oReason_p);
        }
        else
        {
            applyStaticResouceCategory(request_p, oReason_p);
        }
    }
}