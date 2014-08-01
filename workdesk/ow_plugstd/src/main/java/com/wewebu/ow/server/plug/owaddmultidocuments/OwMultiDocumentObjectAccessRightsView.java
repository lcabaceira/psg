package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwReason;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwMenu;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Access rights view.
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
public class OwMultiDocumentObjectAccessRightsView extends OwView implements OwMultipanel
{

    /** reference to the DMS specific AccessRights UI */
    protected OwUIAccessRightsModul m_AccessRightsSubModul;

    /** object reference the view is working on */
    protected OwObject m_ObjectRef;

    /** main menu of ECM Adapters AccessReightsUI */
    protected OwMenu m_MainMenu;

    /** index of the next button in the menu */
    protected int m_iNextButtonIndex = -1;

    /** read only flag */
    protected boolean m_fReadOnly = false;

    protected boolean m_initDone = false;

    /**OwMultipanel implementation previous view reference*/
    protected OwView m_PrevActiveView;

    /**OwMultipanel implementation next view reference*/
    protected OwView m_NextActiveView;

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();
        m_initDone = true;
    }

    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        super.onActivate(index_p, reason_p);
        boolean canEditAccessRights = canEditAccessRights();
        if (m_initDone && (m_ObjectRef != null))
        {
            // create AccessRights sub module from DMS Adaptor
            if (m_AccessRightsSubModul != null)
            {
                getViewList().remove(m_AccessRightsSubModul);
                m_AccessRightsSubModul.detach();
            }
            OwRoleManagerContext roleCtx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
            m_AccessRightsSubModul = roleCtx.getNetwork().getEditAccessRightsSubModul(m_ObjectRef);
            if (m_AccessRightsSubModul != null)
            {
                m_AccessRightsSubModul.setLiveUpdate(true);
                //attention simple OR conjunction do not change, both must be validated
                m_AccessRightsSubModul.setReadOnly(m_fReadOnly | !canEditAccessRights);
                addView(m_AccessRightsSubModul, null);
                if (canEditAccessRights)
                {
                    m_MainMenu = m_AccessRightsSubModul.getMenu();
                    if ((null != m_MainMenu) && (null != m_NextActiveView))
                    {
                        m_iNextButtonIndex = m_MainMenu.addMenuItem(this, getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectAccessRightsView.Next", "Next"), null, "Next", m_NextActiveView, null);
                        m_MainMenu.setDefaultMenuItem(m_iNextButtonIndex);
                    }
                }
            }
        }
        if (m_ObjectRef == null || m_AccessRightsSubModul == null || !canEditAccessRights)
        {
            if (null != m_NextActiveView)
            {
                if (m_MainMenu == null)
                {
                    m_MainMenu = new OwSubMenuView();
                    addView(m_MainMenu, null);
                }
                if (m_iNextButtonIndex == -1)
                {
                    m_iNextButtonIndex = m_MainMenu.addMenuItem(this, getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectAccessRightsView.Next", "Next"), null, "Next", m_NextActiveView, null);
                    m_MainMenu.setDefaultMenuItem(m_iNextButtonIndex);
                }
                else
                {
                    m_MainMenu.enable(m_iNextButtonIndex, true);
                }
            }
        }

    }

    /**
     * Check if access rights can be edited.
     * @return <code>true</code> if access rights can be edited.
     * @throws Exception
     * @since 3.0.0.0
     */
    private boolean canEditAccessRights() throws Exception
    {
        boolean result = false;
        if (m_ObjectRef != null)
        {
            result = ((OwMainAppContext) getContext()).getNetwork().canEditAccessRights(m_ObjectRef);
        }
        return result;
    }

    /** get the form used for the edit fields
     *
     * @return String form name
     */
    public String getFormName()
    {
        // target has no form defined by default, override in derived class
        return null;
    }

    /** set the view read-only 
     */
    public void setReadOnly(boolean fReadOnly_p)
    {
        m_fReadOnly = fReadOnly_p;
        if (null != m_AccessRightsSubModul)
        {
            m_AccessRightsSubModul.setReadOnly(fReadOnly_p);
        }
    }

    /** get the object, the access rights view is working on
     * @return OwObject
     */
    public OwObject getObjectRef()
    {
        return m_ObjectRef;
    }

    /** set the object, the access rights view is working on
     * @param obj_p OwObject
     */
    public void setObjectRef(OwObject obj_p) throws Exception
    {
        if (null != m_AccessRightsSubModul)
        {
            m_AccessRightsSubModul.detach();
            getViewList().remove(m_AccessRightsSubModul);
            m_AccessRightsSubModul = null;
        }

        m_ObjectRef = obj_p;

    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (null != m_ObjectRef)
        {
            if (null != m_AccessRightsSubModul)
            {
                OwPermissionCollection permissons = m_ObjectRef.getPermissions();
                OwReason canEditPermissions = permissons.canEditPermissions();
                if (canEditPermissions.isAllowed())
                {
                    super.onRender(w_p);
                }
                else
                {
                    for (int i = 0; i < m_iNextButtonIndex; i++)
                    {
                        m_AccessRightsSubModul.getMenu().enable(i, false);
                    }
                    renderEmptyTextMessage(w_p, m_ObjectRef.getPermissions().canEditPermissions().getReasonDescription());
                }
            }
            else
            {
                renderUnavailableRights(w_p);
            }
        }
        else
        {
            renderUnavailableRights(w_p);
        }
    }

    /**
     * Render the error message and next button in case that access rights cannot be edited.
     * @param w_p - the {@link Writer} object.
     * @throws Exception
     * @since 3.0.0.0
     */
    private void renderUnavailableRights(Writer w_p) throws Exception
    {
        renderEmptyTextMessage(w_p, getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectAccessRightsView.InvalidObject", "You cannot change the authorizations for this object."));
    }

    /**
     * Render the empty message text and afterwards the menu if exist.
     * <p>This is not a 
     * @param w_p Writer to be used for rendering
     * @param msg_p String message to write
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void renderEmptyTextMessage(Writer w_p, String msg_p) throws Exception
    {
        w_p.write("<div>\n     <div class=\"OwEmptyTextMessage\" >");//style='float:left;padding-left:15px;padding-top:15px;'
        w_p.write(msg_p);
        w_p.write("</div>\n     <div style='clear:left;float:left;padding-left:15px;padding-top:15px;'>\n");
        if (m_MainMenu != null)
        {
            m_MainMenu.render(w_p);
        }
        w_p.write("     </div>\n</div>\n");
    }

    public boolean isValidated() throws Exception
    {
        // OwNavigation does not cascade validations. It checks only if the directly previous view is valid.
        // So we have to cascade the validation by ourself and check our previous view.
        if (null != m_PrevActiveView)
        {
            try
            {
                return (((OwMultipanel) m_PrevActiveView).isValidated());
            }
            catch (Exception e)
            {
            }
        }
        return true;
    }

    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        m_NextActiveView = nextView_p;
        // add 'next' to the main menu
        if (null != m_MainMenu)
        {
            m_iNextButtonIndex = m_MainMenu.addMenuItem(this, getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectAccessRightsView.Next", "Next"), null, "Next", nextView_p, null);
            m_MainMenu.setDefaultMenuItem(m_iNextButtonIndex);

        }
    }

    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        m_PrevActiveView = prevView_p;
    }

    /** event called when user clicked Lock button in menu 
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p a {@link HttpServletRequest}
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        ((OwView) oReason_p).activate();
    }

}