package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwSequenceView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A standard launch-step processor view.<br/>
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
public class OwBPMStandardLaunchProcessorView extends OwBPMStandardProcessorView
{
    /** menu ID of the apply button */
    protected int m_iCancelBtnIndex;

    public OwBPMStandardLaunchProcessorView(OwSequenceView seqview_p, OwXMLUtil configNode_p)
    {
        super(seqview_p, configNode_p);
    }

    public void renderRegion(Writer w_p, int region_p) throws Exception
    {
        getMenu().enable(m_iAppyBtnIndex, false);
        super.renderRegion(w_p, region_p);
    }

    protected String getDispatchButtonTitle()
    {
        return getContext().localize("owbpm.OwBPMStandardLaunchProcessorView.launchbtn", "Launch");
    }

    protected void init() throws Exception
    {
        super.init();
        m_iCancelBtnIndex = getMenu().addFormMenuItem(this, getContext().localize("owbpm.OwBPMStandardLaunchProcessorView.cancelbtn", "Cancel"), "Cancel", null, getFormName());
        getMenu().setDefaultMenuItem(m_iDispatchButton);
    }

    /** event called when user clicked cancel button in menu 
     *   @param request_p a  {@link HttpServletRequest}
     *   @param oReason_p Optional reason object submitted in addMenuItem
     */
    public void onCancel(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        m_seqview.next(true);
    }
}
