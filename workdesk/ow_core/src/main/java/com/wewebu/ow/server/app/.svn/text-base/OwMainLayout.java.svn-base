package com.wewebu.ow.server.app;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.alfresco.wd.ui.profile.OwProfileDialog;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwBaseView;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwDialogManager;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwNavigationView;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwExceptionManager;

/**
 *<p>
 * Main Layout for Workdesk. Instance stays active during session.
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
public class OwMainLayout extends OwLayout implements OwDialogListener
{
    /**
     * The AJAX update event name.
     * @since 3.1.0.0
     */
    private static final String AJAX_UPDATE_EVENT_NAME = "Update";
    /**
     * The parameter name associated with a container ID.
     * @since 3.1.0.0
     */
    private static final String CONTAINER_ID = "containerId";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwMainLayout.class);

    /** name of the navigation region */
    public static final int NAVIGATION_REGION = 0;
    /** name of the main region shared with DIALOG_REGION */
    public static final int MAIN_REGION = 1;
    /** name of the dialog region. shared with MAIN_REGION */
    public static final int DIALOG_REGION = 2;
    /** name of the login info region. */
    public static final int ERROR_REGION = 4;
    /** name of the maximized region. I.e. used if a view requests to be displayed maximized */
    public static final int MAXIMIZED_REGION = 5;
    /** name of the maximized region. I.e. used if a view requests to be displayed maximized */
    public static final int CLIPBOARD_REGION = 6;
    /** name of the region that describes the registered keyboard keys */
    public static final int KEYBOARD_REGION = 7;
    /** name of the region that displays the available roles */
    public static final int ROLE_SELECT_REGION = 8;
    /** name of the logout button region. */
    public static final int LOGOUT_BUTTON_REGION = 9;
    /** name of the user name region. */
    public static final int USERNAME_REGION = 10;
    /** name of the date region. */
    public static final int CURRENT_DATE_REGION = 11;
    /** name of the login info region. */
    public static final int MESSAGE_REGION = 12;
    /** name of the mandator region. */
    public static final int MANDATOR_REGION = 13;

    public static final int PROFILE_ACTION_REGION = 14;

    /**the HTML error container ID*/
    public static final String ERROR_CONTAINER_ID = "OwMainLayout_ERROR_container";
    /**the HTML message container ID*/
    public static final String MESSAGE_CONTAINER_ID = "OwMainLayout_MESSAGE_container";
    /**the HTML key info container ID*/
    public static final String KEY_INFO_CONTAINER_ID = "owkeyinfo";
    /** the registerd scripts container ID*/
    public static final String REGISTERED_KEYS_SCRIPTS_CONTAINER_ID = "registeredKeysScripts";

    /** ID of the role select combobox */
    private static final String ROLE_SELECT_ID = "roleselect";

    public static final String LOGOUT_STATUS = "LOGOUT_STATUS";

    public static final String LOGOUT_DISABLED = "disabled";

    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;
    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;

    private OwMainNavigationView m_MainNaviView;

    /** currently maximized view if set */
    protected OwView m_MaximizedView;

    /** create the master plugins and add them to the given navigation view
     * @param strPreferedStartupID_p String ID of plugin to show first or null to use the startup ID defined in settings
     */
    private void addMasterPlugins(String strPreferedStartupID_p) throws Exception
    {
        // get the startupID from app setting
        String strStartupID = ((OwMainAppContext) getContext()).getStartupID();
        int iStartupNavigationIndex = 0;
        int iPreferredStartupNavigationIndex = -1;

        // get the main area plugins
        List mainPlugins = ((OwMainAppContext) getContext()).getConfiguration().getMasterPlugins(true);

        if (mainPlugins.size() == 0)
        {
            return;
        }

        // === iterate over the plugins
        for (int i = 0; i < mainPlugins.size(); i++)
        {
            OwConfiguration.OwMasterPluginInstance mainPlugin = (OwConfiguration.OwMasterPluginInstance) mainPlugins.get(i);

            try
            {
                // add plugin to main navigation
                m_MainNaviView.addPluginView(mainPlugin.getView(), mainPlugin.getViewID());
            }
            catch (Exception e)
            {
                // exception occurred, display a error view instead of the plugin
                m_MainNaviView.addPluginView(mainPlugin.getErrorView(e, mainPlugin.getPluginTitle()), null);
            }

            // found startup id, so set the index for navigation
            if ((mainPlugin.getPluginID() != null) && (mainPlugin.getPluginID().equals(strStartupID)))
            {
                iStartupNavigationIndex = i;
            }

            if ((mainPlugin.getPluginID() != null) && (mainPlugin.getPluginID().equals(strPreferedStartupID_p)))
            {
                iPreferredStartupNavigationIndex = i;
            }
        }

        // activate the startup view
        if (iPreferredStartupNavigationIndex != -1)
        {
            m_MainNaviView.navigate(iPreferredStartupNavigationIndex);
        }
        else
        {
            m_MainNaviView.navigate(iStartupNavigationIndex);
        }
    }

    /** overridable function to retrieve the view that is shown maximized if any
     * @return view_p OwView that is shown maximized, null otherwise
     */
    protected OwView getMaximizeView()
    {
        return m_MaximizedView;
    }

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();
        // get and cast appcontext
        m_MainContext = (OwMainAppContext) getContext();
        m_MainContext.addAjaxUpdateContainer(ERROR_CONTAINER_ID, getAjaxEventURL(AJAX_UPDATE_EVENT_NAME, CONTAINER_ID + "=" + ERROR_CONTAINER_ID));
        m_MainContext.addAjaxUpdateContainer(MESSAGE_CONTAINER_ID, getAjaxEventURL(AJAX_UPDATE_EVENT_NAME, CONTAINER_ID + "=" + MESSAGE_CONTAINER_ID));
        m_MainContext.addAjaxUpdateContainer(KEY_INFO_CONTAINER_ID, getAjaxEventURL(AJAX_UPDATE_EVENT_NAME, CONTAINER_ID + "=" + KEY_INFO_CONTAINER_ID));
        m_MainContext.addAjaxUpdateContainer(REGISTERED_KEYS_SCRIPTS_CONTAINER_ID, getAjaxEventURL(AJAX_UPDATE_EVENT_NAME, CONTAINER_ID + "=" + REGISTERED_KEYS_SCRIPTS_CONTAINER_ID));
        // get application m_Configuration
        m_Configuration = m_MainContext.getConfiguration();

        // === create main layout
        // === create Navigation
        m_MainNaviView = createMainNavigationView();
        addView(m_MainNaviView, NAVIGATION_REGION, null);
        // Main
        addViewReference(m_MainNaviView.getViewReference(), MAIN_REGION);

        // === dialog manager
        addViewReference(m_MainContext.getDialogManagerViewReference(), DIALOG_REGION);

        // === create master plugins navigation
        // add master plugins to the layout
        addMasterPlugins(null);

        // clipboard
        addView(new OwClipboardView(), CLIPBOARD_REGION, null);
    }

    /** determine if region contains a view or must be rendered
     * @param iRegion_p ID of the region
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
        // === check internal regions here
            case KEYBOARD_REGION:
                return (((OwMainAppContext) getContext()).getKeyEvents() != null);

            case MAXIMIZED_REGION:
            {
                return (null != getMaximizeView());
            }

            case MANDATOR_REGION:
                return ((m_MainContext.getMandator() != null) && (m_MainContext.getMandator().getName().length() > 0));

            case DIALOG_REGION:
            {
                if (m_MainContext != null)
                {
                    OwDialogManager dlgman = m_MainContext.getDialogManager();
                    if (null != dlgman)
                    {
                        return dlgman.isDialogOpen();
                    }
                    else
                    {
                        LOG.warn("OwMainLayout.isRegion: DIALOG_REGION: No dialogmanager available, probably due to missing masterplugins.");
                    }
                }
                return false;
            }

            case MESSAGE_REGION:
            {
                return !m_MainContext.getMessages().isEmpty();
            }

            case ERROR_REGION:
            {
                return (m_MainContext.getError() != null);
            }

            case CLIPBOARD_REGION:
            {
                return (OwClipboard.CONTENT_TYPE_EMPTY != m_MainContext.getClipboard().getContentType()) && m_MainContext.doShowClipboard();
            }

            case ROLE_SELECT_REGION:
            {
                try
                {
                    return ((OwMainAppContext) getContext()).getRoleManager().hasMasterRoles();
                }
                catch (Exception e)
                {
                    return false;
                }
            }

            default:
            {
                // === check registered regions
                return super.isRegion(iRegion_p);
            }
        }
    }

    /** event called when user clicked LogOut
     *   @param request_p  HttpServletRequest
     */
    public void onLogOut(HttpServletRequest request_p) throws Exception
    {
        // === dispatch event to the network logout function
        m_MainContext.logout();
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwMainLayout.jsp", w_p);
    }

    /** called when user selects a role in the combobox
     * */
    public void onSelectRole(HttpServletRequest request_p) throws Exception
    {
        String strRole = request_p.getParameter(ROLE_SELECT_ID);

        if (((OwMainAppContext) getContext()).getConfiguration().setMasterRole(strRole))
        {
            updateMasterPlugins();
        }
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        HttpSession httpSession = m_MainContext.getHttpSession();
        Object logoutStatus = httpSession.getAttribute(LOGOUT_STATUS);

        switch (iRegion_p)
        {
        // === render the role select region
            case ROLE_SELECT_REGION:
            {
                renderRoleSelectRegion(w_p);
            }
                break;

            // === render internal regions here
            //            case KEYBOARD_REGION:
            //                java.util.Collection keys = ((OwMainAppContext) getContext()).getKeyEvents();
            //                if (keys != null)
            //                {
            //                    Iterator it = keys.iterator();
            //                    w_p.write("<dl>");
            //                    while (it.hasNext())
            //                    {
            //                        OwMainAppContext.OwKeyEvent keyevent = (OwMainAppContext.OwKeyEvent) it.next();
            //                        w_p.write("<dt>[" + keyevent.getKeyDescription(getContext().getLocale()) + "]</dt><dd>" + keyevent.getDescription() + "</dd>");
            //                    }
            //                    w_p.write("</dl>");
            //                }
            //                break;

            case MAXIMIZED_REGION:
            {
                // render the view that requested to be displayed maximized
                if (m_MaximizedView != null)
                {
                    m_MaximizedView.render(w_p);
                }
            }
                break;

            case LOGOUT_BUTTON_REGION:
                // logout button
                if (!LOGOUT_DISABLED.equals(logoutStatus))
                {
                    w_p.write("<a class=\"OwHeaderView_Info\" href=\"" + this.getEventURL("LogOut", null) + "\">" + getContext().localize("app.OwMainLayout.logout", "Logout") + "</a>");
                }
                break;

            case USERNAME_REGION:
                // display user name
                w_p.write(m_MainContext.getCredentials().getUserInfo().getUserLongName());
                break;

            case MANDATOR_REGION:
            // display mandator name
            {
                String name = m_MainContext.getMandator().getName();

                if (name.length() > 0)
                {
                    w_p.write("<span title=\"");
                    w_p.write(m_MainContext.getMandator().getDescription());
                    w_p.write("\">");
                    w_p.write(name);
                    w_p.write("</span>");
                }
            }
                break;

            case CURRENT_DATE_REGION:
            {
                // display date
                java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL, getContext().getLocale());
                w_p.write(df.format(new java.util.Date()));
            }
                break;

            case MESSAGE_REGION:
            {
                Collection messages = m_MainContext.getMessages();
                if (null != messages)
                {
                    // === display the messages
                    Iterator it = messages.iterator();

                    while (it.hasNext())
                    {
                        String sText = (String) it.next();

                        w_p.write("<div class=\"OwMessages\">");
                        w_p.write(sText);
                        w_p.write("</div>");
                    }
                }
            }
                break;

            case ERROR_REGION:
            {
                Throwable e = m_MainContext.getError();
                if (e != null)
                {
                    // print error which occurred upon recent request
                    OwExceptionManager.PrintCatchedException(getContext().getLocale(), e, new PrintWriter(w_p), "OwErrorStack");
                }
            }
                break;

            case PROFILE_ACTION_REGION:
            {
                String url = getEventURL("ShowProfile", null);
                w_p.write(url);
            }
            default:
            {
                // === render registered regions
                super.renderRegion(w_p, iRegion_p);
            }
        }
    }

    /** render a combobox for the selectable roles
     * */
    private void renderRoleSelectRegion(Writer w_p) throws Exception
    {

        if (isRegion(ROLE_SELECT_REGION))
        {
            Collection roles = ((OwMainAppContext) getContext()).getRoleManager().getMasterRoles();
            String strSelectedRole = ((OwMainAppContext) getContext()).getRoleManager().getMasterRole();

            StringBuffer selectRoleJS = new StringBuffer();
            selectRoleJS.append("<script>\n");
            selectRoleJS.append("function selectRole() {\n");
            selectRoleJS.append("document.");
            selectRoleJS.append(getFormName());
            selectRoleJS.append(".action=\"");
            selectRoleJS.append(getEventURL("SelectRole", null));
            selectRoleJS.append("\";document.");
            selectRoleJS.append(getFormName());
            selectRoleJS.append(".submit();\n");
            selectRoleJS.append("}\n");
            selectRoleJS.append("</script>\n");
            w_p.write(selectRoleJS.toString());

            List roleItems = new LinkedList();

            Iterator it = roles.iterator();
            while (it.hasNext())
            {
                String strRole = (String) it.next();
                String strRoleDisplayName = ((OwMainAppContext) getContext()).getRoleManager().getMasterRoleDisplayName(getContext().getLocale(), strRole);
                OwComboItem item = new OwDefaultComboItem(strRole, strRoleDisplayName);
                roleItems.add(item);
            }

            OwComboModel comboModel = new OwDefaultComboModel(false, false, strSelectedRole, roleItems);
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, ROLE_SELECT_ID, null, null, null);
            renderer.addEvent("onchange", "selectRole();");

            String selectRoleDisplayName = getContext().localize("OwMainLayout.selectRoleDisplayName", "Select Role");
            OwInsertLabelHelper.insertLabelValue(w_p, selectRoleDisplayName, ROLE_SELECT_ID);
            renderer.renderCombo(w_p);
        }
    }

    /** overridable function to set a view as a maximized view, i.e. the submitted view should be drawn maximized.
     * @param view_p OwView that requests to be shown maximized
     */
    protected void setMaximizeView(OwView view_p)
    {
        m_MaximizedView = view_p;
    }

    public String getTitle()
    {
        return ((OwBaseView) this.m_Regions.get(Integer.valueOf(MAIN_REGION))).getTitle();
    }

    public String getBreadcrumbPart()
    {
        return ((OwBaseView) this.m_Regions.get(Integer.valueOf(MAIN_REGION))).getBreadcrumbPart();
    }

    /** update master plugins after role change
     * */
    private void updateMasterPlugins() throws Exception
    {
        String preferedStartupID = "";
        List tablist = m_MainNaviView.getTabList();

        if (tablist.size() > 0)
        {
            // compute the last selected master plugin
            OwNavigationView.OwTabInfo info = (OwNavigationView.OwTabInfo) tablist.get(m_MainNaviView.getNavigationIndex());

            // use it as the preferred startup ID for the switched masterrole, so Alfresco Workdesk starts up in the previous master plugin if possible
            preferedStartupID = ((OwMasterView) info.getView()).getPluginID();
        }

        m_MainNaviView.clear();

        addMasterPlugins(preferedStartupID);
    }

    /**
     * AJAX handler for update the error message.
     * @param request_p - the {@link HttpServletRequest} request object.
     * @param response_p - the {@link HttpServletResponse} response object.
     * @throws Exception 
     * @since 3.1.0.0
     */
    public void onAjaxUpdate(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        String container = request_p.getParameter(CONTAINER_ID);

        if (ERROR_CONTAINER_ID.equals(container))
        {
            serverSideInclude(getContext().getDesignDir() + "/OwMainLayoutError.jsp", response_p.getWriter());
        }
        if (MESSAGE_CONTAINER_ID.equals(container))
        {
            serverSideInclude(getContext().getDesignDir() + "/OwMainLayoutMessage.jsp", response_p.getWriter());
        }
        if (KEY_INFO_CONTAINER_ID.equals(container))
        {
            serverSideInclude(getContext().getDesignDir() + "/OwMainLayoutKeyboard.jsp", response_p.getWriter());
        }
        if (REGISTERED_KEYS_SCRIPTS_CONTAINER_ID.equals(container))
        {
            serverSideInclude(getContext().getDesignDir() + "/OwKeyboardScriptsRenderer.jsp", response_p.getWriter());
        }

    }

    /**
     * Check if the context has an error set.
     * @return - true, if the context has an error associated with it.
     * @since 3.1.0.0
     */
    public boolean hasError()
    {
        return m_MainContext.getError() != null;
    }

    /**
     * Check if the context has messages set.
     * @return - true, if the context has an messages associated with it.
     * @since 3.1.0.0
     */
    public boolean hasMessages()
    {
        Collection messages = m_MainContext.getMessages();
        return messages != null && messages.size() > 0;
    }

    /**(overridable)
     * Factory for OwMainNavigationView
     * @return OwMainNavigationView new instance
     * @since 4.2.0.0
     */
    protected OwMainNavigationView createMainNavigationView()
    {
        return new OwMainNavigationView();
    }

    /**(overridable)
     * Factory for OwProfileDialog used in {@link #onShowProfile(HttpServletRequest)}
     * @return OwProfileDialog
     * @since 4.2.0.0
     */
    protected OwProfileDialog createUserProfileView()
    {
        return new OwProfileDialog();
    }

    /**
     * Open a Dialog which will display the current User Profile.
     * @param req HttpServletRequest
     * @throws Exception
     * @since 4.2.0.0
     */
    public void onShowProfile(HttpServletRequest req) throws Exception
    {
        OwProfileDialog dlg = createUserProfileView();

        getContext().openDialog(dlg, this);
        dlg.showMaximized();
    }

    @Override
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        dialogView_p.showMinimized();
    }

}