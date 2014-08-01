package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwSequenceView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A JSP launch-step processor view.<br/>
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
public class OwBPMJspLaunchProcessorView extends OwBPMJspProcessorView
{
    /** menu ID of the apply button */
    protected int m_iCancelBtnIndex;

    public OwBPMJspLaunchProcessorView(OwSequenceView seqview_p, OwXMLUtil configNode_p)
    {
        super(seqview_p, configNode_p);
    }

    protected void init() throws Exception
    {
        super.init();
        m_iCancelBtnIndex = getMenu().addFormMenuItem(this, getContext().localize("owbpm.OwBPMJspLaunchProcessorView.cancelbtn", "Cancel"), "Cancel", null, getFormName());
        getMenu().setDefaultMenuItem(m_iDispatchButton);
        getMenu().enable(m_iSaveButton, false);
    }

    protected String getDispatchButtonTitle()
    {
        return getContext().localize("owbpm.OwBPMJspLaunchProcessorView.launchbtn", "Launch");
    }

    /** event called when user clicked cancel button in menu 
     *   @param request_p a  {@link HttpServletRequest}
     *   @param oReason_p Optional reason object submitted in addMenuItem
     */
    public void onCancel(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        m_seqview.next(true);
    }

    public void setObjectRef(OwObject objectRef_p) throws Exception
    {
        super.setObjectRef(objectRef_p);
        getMenu().enable(getSaveBtnIndex(), false);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMJspProcessorView#renderMenuRegion(java.io.Writer)
     */
    @Override
    protected void renderMenuRegion(Writer w_p) throws Exception
    {
        getMenu().enable(getSaveBtnIndex(), false);
        super.renderMenuRegion(w_p);
    }
}
