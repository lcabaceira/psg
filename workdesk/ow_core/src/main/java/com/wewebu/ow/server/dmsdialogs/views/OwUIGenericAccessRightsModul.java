package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.ui.OwMenu;
import com.wewebu.ow.server.ui.OwSmallSubNavigationView;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Generic access rights module. 
 * Displays two navigation tabs (policies and privileges) the permissions of a given object.
 * In case that only one of the navigation tabs should be displayed, no tabs are rendered.
 * Also uses the underlying objects and permissions methods to perform the editing of the given permissions.  
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
public class OwUIGenericAccessRightsModul extends OwUIAccessRightsModul
{
    public static final int POLICIES_ONLY = 1;
    public static final int PRIVILEGES_ONLY = 2;
    public static final int BOTH = 3;

    private boolean m_readOnly = false;

    private OwPolicyLevelView m_policyLevelView;
    private OwPrivilegesView m_privilegesView;
    private OwSubNavigationView m_subNavigationView;
    private OwMenuView m_menu;
    private int m_iSaveMenuBtnIndex;
    private OwPermissionCollection m_permissions;
    private String m_policyViewTitle = null;
    private String m_privilegesViewTitle = null;
    private Map<String, String> m_privilegeDisplayNames = new HashMap<String, String>();
    private boolean m_policiesEnabled = true;
    private boolean m_privilegesEnabled = true;
    private boolean m_canGetPermissions;
    private boolean m_canSetPermissions;
    private String policyViewMessage;
    private String aclViewMessage;
    private boolean useTabs = true;
    private OwView activeView = null;

    //   private OwSubLayout subLayoutView
    public OwUIGenericAccessRightsModul(OwNetwork network_p, OwObject object_p) throws OwException
    {
        this(network_p, object_p, BOTH);
    }

    /**
     * Constructor
     * 
     * @param network_p the {@link OwNetwork} of this module
     * @param object_p the {@link OwObject} to display and/or edit the permissions for.
     * @param views_p one of {@link #POLICIES_ONLY}, {@link #PRIVILEGES_ONLY} or {@link #BOTH}
     * @since 3.2.0.0
     */
    public OwUIGenericAccessRightsModul(OwNetwork network_p, OwObject object_p, int views_p) throws OwException
    {
        this(network_p, object_p, null, null, views_p);
    }

    /**
     * Constructor
     * 
     * @param network_p the {@link OwNetwork} of this module
     * @param object_p the {@link OwObject} to display and/or edit the permissions for.
     * @param policyViewTitle_p title of the policy tab
     * @param privilegesViewTitle_p tite of the privileges view tab
     * @param views_p one of {@link #POLICIES_ONLY}, {@link #PRIVILEGES_ONLY} or {@link #BOTH}
     * @since 3.1.0.0
     */
    public OwUIGenericAccessRightsModul(OwNetwork network_p, OwObject object_p, String policyViewTitle_p, String privilegesViewTitle_p, int views_p) throws OwException
    {
        this(network_p, object_p, policyViewTitle_p, privilegesViewTitle_p, null, views_p);
    }

    /**
     * Constructor
     * 
     * @param network_p the {@link OwNetwork} of this module
     * @param object_p the {@link OwObject} to display and/or edit the permissions for.
     * @param policyViewTitle_p title of the policy tab
     * @param privilegesViewTitle_p tite of the privileges view tab
     * @param views_p one of {@link #POLICIES_ONLY}, {@link #PRIVILEGES_ONLY} or {@link #BOTH}
     * @param privilegeDisplayNames_p display name of privileges mapped by name (can be null) 
     * @since 3.2.0.0
     */
    public OwUIGenericAccessRightsModul(OwNetwork network_p, OwObject object_p, String policyViewTitle_p, String privilegesViewTitle_p, Map<String, String> privilegeDisplayNames_p, int views_p) throws OwException
    {
        OwPermissionsDocument document = new OwPermissionsDocument(object_p);
        if (document != null)
        {
            setDocument(document);
        }
        this.m_policyViewTitle = policyViewTitle_p;
        this.m_privilegesViewTitle = privilegesViewTitle_p;
        this.m_policiesEnabled = true;
        this.m_privilegesEnabled = true;

        if (POLICIES_ONLY == views_p)
        {
            this.m_privilegesEnabled = false;
        }

        if (PRIVILEGES_ONLY == views_p)
        {
            this.m_policiesEnabled = false;
        }
        if (m_privilegesEnabled && m_policiesEnabled)
        {
            useTabs = true;
        }
        else
        {
            useTabs = false;
        }

        m_privilegeDisplayNames = (privilegeDisplayNames_p == null) ? new HashMap<String, String>() : privilegeDisplayNames_p;

        init(network_p);

    }

    @Override
    public void init(OwNetwork theNetwork_p) throws OwException
    {
        try
        {
            super.init(theNetwork_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Unknown error", e);
        }
    }

    protected void init() throws Exception
    {
        detach();

        super.init();

        m_canGetPermissions = getDocument().getObject().canGetPermissions();
        m_canSetPermissions = getDocument().getObject().canSetPermissions();
        boolean canGetPolicies = false;
        boolean canGetPrivileges = false;
        m_permissions = null;
        if (m_canGetPermissions)
        {
            m_permissions = getDocument().getObject().getPermissions();
            canGetPolicies = m_permissions.canGetPolicies();
            canGetPrivileges = m_permissions.canGetPrivileges();
        }

        if (m_permissions != null)
        {
            m_policyLevelView = createPolicyLevelView();
            m_policyLevelView.setMessage(policyViewMessage);
            m_policyLevelView.setExternalFormTarget(getFormTarget());

            m_privilegesView = createPrivilegesView();
            m_privilegesView.setMessage(aclViewMessage);
            m_privilegesView.setExternalFormTarget(getFormTarget());
            if (m_policyViewTitle == null)
            {
                m_policyViewTitle = getContext().localize("owdocprops.OwUIGenericAccessRightsModul.policies", "Policies");
            }
            if (m_privilegesViewTitle == null)
            {
                m_privilegesViewTitle = getContext().localize("owdocprops.OwUIGenericAccessRightsModul.privileges", "Privileges");
            }
        }
        if (useTabs)
        {
            m_subNavigationView = createNavigationView();
            addView(m_subNavigationView, null);
        }
        else if (m_permissions != null)
        {
            if (m_policiesEnabled)
            {
                activeView = m_policyLevelView;
                addView(m_policyLevelView, null);
            }
            else if (m_privilegesEnabled)
            {
                activeView = m_privilegesView;
                addView(m_privilegesView, null);
            }
        }

        m_menu = new OwSubMenuView();
        addView(m_menu, null);
        m_iSaveMenuBtnIndex = m_menu.addFormMenuItem(this, getContext().localize("owdocprops.OwUIGenericAccessRightsModul.policies.save", "Save"), "Save", null);
        m_menu.enable(m_iSaveMenuBtnIndex, m_canSetPermissions && !m_readOnly);
        if (useTabs)
        {
            if (m_permissions != null)
            {
                if (m_policiesEnabled)
                {
                    m_subNavigationView.addView(m_policyLevelView, m_policyViewTitle, null, null, null, null);
                    m_subNavigationView.enable(0, canGetPolicies);
                }

                if (m_privilegesEnabled)
                {
                    m_subNavigationView.addView(m_privilegesView, m_privilegesViewTitle, null, null, null, null);
                    m_subNavigationView.enable(1, canGetPrivileges);
                }
            }

            m_subNavigationView.setValidatePanels(true);
            m_subNavigationView.navigate(0);
            activeView = m_subNavigationView;
        }
    }

    public void setReadOnly(boolean readOnly_p)
    {
        m_readOnly = readOnly_p;

        if (m_menu != null)
        {
            m_menu.enable(m_iSaveMenuBtnIndex, m_canSetPermissions && !m_readOnly);
        }

        if (m_policyLevelView != null)
        {
            m_policyLevelView.setReadOnly(readOnly_p);
        }

        if (m_privilegesView != null)
        {
            m_privilegesView.setReadOnly(readOnly_p);
        }
    }

    /**
     * (overridable) {@link OwPolicyLevelView} factory method.
     * 
     * @return an {@link OwPolicyLevelView} to be used within this module
     * @throws Exception
     */
    protected OwPolicyLevelView createPolicyLevelView() throws Exception
    {
        OwNetwork network = getNetwork();
        OwCredentials credentials = network.getCredentials();
        OwPolicyLevelView owPolicyLevelView = new OwPolicyLevelView(this, credentials.getUserInfo(), m_readOnly, getLiveUpdate());
        return owPolicyLevelView;
    }

    /**
     * (overridable) {@link OwPrivilegesView} factory method.
     * 
     * @return an {@link OwPrivilegesView} to be used within this module
     */
    protected OwPrivilegesView createPrivilegesView() throws Exception
    {
        OwPrivilegesView owPrivilegesView = new OwPrivilegesView(this, this.m_privilegeDisplayNames, getLiveUpdate());
        return owPrivilegesView;
    }

    /**
     * (overridable) {@link OwSubNavigationView} factory method.
     * @return an {@link OwSubNavigationView} to be used within this module to display the policies and the privileges views
     */
    protected OwSubNavigationView createNavigationView()
    {
        return new OwSmallSubNavigationView(true);
    }

    protected String usesFormWithAttributes()
    {
        return "";
    }

    public OwMenu getMenu()
    {
        return m_menu;
    }

    protected void onRender(Writer w_p) throws Exception
    {
        if (activeView != null)
        {
            activeView.render(w_p);
        }
        if (useTabs)
        {
            w_p.write("<div style='float:left;clear:left;margin-left:10px;margin-top:5px;'>");
        }
        else
        {
            w_p.write("<div class='OwObjectPropertyView_MENU OwInlineMenu'>");
        }
        m_menu.render(w_p);
        w_p.write("</div>");
    }

    /**
     * OnSave form action handler 
     * @param request_p
     * @param reason_p
     * @throws Exception
     */
    public void onSave(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        if (useTabs)
        {
            int currentIndex = m_subNavigationView.getNavigationIndex();

            if (currentIndex == 0 && m_policiesEnabled)
            {
                m_policyLevelView.onSave(request_p, reason_p);
            }
            else
            {
                m_privilegesView.onSave(request_p, reason_p);
            }
        }
        else
        {
            if (m_policiesEnabled)
            {
                m_policyLevelView.onSave(request_p, reason_p);
            }
            else
            {
                m_privilegesView.onSave(request_p, reason_p);
            }
        }

        //        getObject().setPermissions(m_permissions);
        OwPermissionsDocument document = getDocument();
        document.savePermissions();
    }

    /**
     * Setter for {@link OwPolicyLevelView} message
     * @param message_p - the message
     * @since 3.1.0.0
     */
    public void setPolicyViewMessage(String message_p)
    {
        policyViewMessage = message_p;
    }

    /**
     * Setter for {@link OwPrivilegesView} message
     * @param message_p - the message
     * @since 3.1.0.0
     */
    public void setACLViewMessage(String message_p)
    {
        aclViewMessage = message_p;
    }

    /**
     * Return the display names which represents the
     * native privilege to display names.
     * @return Map of Strings, native name to display name
     * @since 3.2.0.0
     */
    protected Map<String, String> getPrivilegeDisplayNames()
    {
        return m_privilegeDisplayNames;
    }

    @Override
    public OwPermissionsDocument getDocument()
    {
        return (OwPermissionsDocument) super.getDocument();
    }

    /**
     * Return current work object. 
     * @return OwObject
     * @since 3.2.0.0
     * @deprecated since 4.0.0.0 use the view's {@link OwPermissionsDocument} 
     */
    protected final OwObject getObject()
    {
        OwPermissionsDocument document = getDocument();
        return document.getObject();
    }
}