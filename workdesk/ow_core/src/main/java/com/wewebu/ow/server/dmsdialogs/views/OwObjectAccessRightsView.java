package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * View Module to edit the OwObject Access rights via OwNetwork.getEditAccessRightsSubModul(...).
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
public class OwObjectAccessRightsView extends OwView
{
    /** reference to the DMS specific AccessRights UI */
    protected OwUIAccessRightsModul m_AccessRightsSubModul;

    /** object reference the view is working on */
    protected OwObject m_ObjectRef;

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        if (m_ObjectRef == null)
        {
            throw new OwInvalidOperationException("OwObjectAccessRightsView.init: ObjectRef==null, set Object with OwObjectAccessRightsView.setObjectRef().");
        }

        // create AccessRights sub module from DMS Adaptor
        if (((OwMainAppContext) getContext()).getNetwork().canEditAccessRights(m_ObjectRef))
        {
            m_AccessRightsSubModul = ((OwMainAppContext) getContext()).getNetwork().getEditAccessRightsSubModul(m_ObjectRef);
            addView(m_AccessRightsSubModul, null);
        }
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
        m_AccessRightsSubModul.setReadOnly(fReadOnly_p);
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
        if (m_ObjectRef != null)
        {
            throw new OwInvalidOperationException("OwObjectAccessRightsView.setObjectRef: Change Object not supported yet");
        }

        m_ObjectRef = obj_p;
    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (((OwMainAppContext) getContext()).getNetwork().canEditAccessRights(m_ObjectRef))
        {
            super.onRender(w_p);
        }
        else
        {
            w_p.write("<span class='OwEmptyTextMessage'>" + getContext().localize("dmsdialogs.views.OwObjectAccessRightsView.invalidobject", "Authorizations cannot be set for this object.") + " " + m_ObjectRef.getName() + "</span>");
        }
    }

    //    /**
    //     * Reinitializes this view.
    //     * @param index_p
    //     * @param reason_p
    //     * @throws Exception
    //     */
    //    protected void onActivate(int index_p, Object reason_p) throws Exception
    //    {
    /*bug 1797 not correct handling of the init method,
     * the view should not be reinitialized during onActivate method,
     * if it should, also all other information MUST be recopied
     * --> Here we loose the isReadonly flag of m_AccessRightsSubModul*/
    //        List views = getViewList();
    //
    //        for (Iterator i = views.iterator(); i.hasNext();)
    //        {
    //            OwView view = (OwView) i.next();
    //            view.detach();
    //            i.remove();
    //        }
    //        init();
    //    }
}