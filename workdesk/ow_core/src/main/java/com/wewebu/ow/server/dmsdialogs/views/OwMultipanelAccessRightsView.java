package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ui.OwMenu;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Access rights view.
 * Implementing the OwMutlipanel interface, so
 * it will work also in sub menu structures.
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
 *@since 3.1.0.0
 */
public class OwMultipanelAccessRightsView extends OwView implements OwMultipanel
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

    /**OwMultipanel implementation previous view reference*/
    protected OwView m_PrevActiveView;

    /**OwMultipanel implementation next view reference*/
    protected OwView m_NextActiveView;

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onActivate(int, java.lang.Object)
     */
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        super.onActivate(index_p, reason_p);
        boolean canEditAcc = canEditAccessRights();
        // create AccessRights sub module from DMS Adaptor
        if (canEditAcc)
        {
            getViewList().remove(m_AccessRightsSubModul);
            m_AccessRightsSubModul = ((OwMainAppContext) getContext()).getNetwork().getEditAccessRightsSubModul(getObjectRef());

            m_AccessRightsSubModul.setReadOnly(m_fReadOnly);
            m_AccessRightsSubModul.setLiveUpdate(true);

            addView(m_AccessRightsSubModul, null);
            m_MainMenu = m_AccessRightsSubModul.getMenu();
        }

        if (m_iNextButtonIndex == -1)
        {
            if (m_MainMenu == null)
            {
                m_MainMenu = new OwSubMenuView();
                addView(m_MainMenu, null);
            }
            setNextActivateView(m_NextActiveView);
        }
    }

    /**
     * Check if access rights can be edited.
     * @return <code>true</code> if access rights can be edited.
     * @throws Exception
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
        //        if (null != m_AccessRightsSubModul)
        //        {
        //            m_AccessRightsSubModul.detach();
        //            getViewList().remove(m_AccessRightsSubModul);
        //            m_AccessRightsSubModul = null;
        //        }

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
                if (canEditAccessRights())
                {
                    super.onRender(w_p);
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
        else
        {
            renderUnavailableRights(w_p);
        }
    }

    /**
     * Render the error message and next button in case that access rights cannot be edited.
     * @param w_p - the {@link Writer} object.
     * @throws Exception
     */
    private void renderUnavailableRights(Writer w_p) throws Exception
    {
        w_p.write("<div>\n<div class='OwEmptyTextMessage' style='float:left;padding-left:15px;padding-top:15px;'>");
        w_p.write(getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectAccessRightsView.InvalidObject", "You cannot change the authorizations for this object."));
        w_p.write("</div>\n<div style='clear:left;float:left;padding-left:15px;padding-top:15px;'>\n");
        if (m_MainMenu != null)
        {
            m_MainMenu.render(w_p);
        }
        w_p.write("</div>\n</div>\n");
    }

    public boolean isValidated() throws Exception
    {
        // OwNavigation does not cascade validations. It checks only if the directly previous view is valid.
        // So we have to cascade the validation by ourself and check our previous view.
        if (null != m_PrevActiveView && m_PrevActiveView instanceof OwMultipanel)
        {
            return ((OwMultipanel) m_PrevActiveView).isValidated();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwMultipanel#setNextActivateView(com.wewebu.ow.server.ui.OwView)
     */
    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        m_NextActiveView = nextView_p;
        // add 'next' to the main menu
        if (null != m_MainMenu && m_iNextButtonIndex == -1)
        {
            m_iNextButtonIndex = m_MainMenu.addMenuItem(this, getContext().localize("views.OwMultipanelAccessRightsView.btn.Next", "Next"), null, "Next", null, null);
            m_MainMenu.setDefaultMenuItem(m_iNextButtonIndex);

        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwMultipanel#setPrevActivateView(com.wewebu.ow.server.ui.OwView)
     */
    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        m_PrevActiveView = prevView_p;
    }

    /** 
     * Event called when user clicked Next button in menu 
     * @param oReason_p Optional reason object submitted in addMenuItem
     * @param request_p a {@link HttpServletRequest}
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        m_iNextButtonIndex = -1;
        m_NextActiveView.activate();
    }
}