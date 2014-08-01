package org.alfresco.wd.plug.owbpm;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSequenceView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Add Claim/Release to pool functionality.
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
 *@since 4.2.0.0
 */
public class OwAlfrescoBPMStandardProcessorView extends OwBPMStandardProcessorView
{
    private int m_iClaimButton;
    private int m_iReleaseToPoolButton;

    /**
     * @param seqview_p
     * @param configNode_p
     */
    public OwAlfrescoBPMStandardProcessorView(OwSequenceView seqview_p, OwXMLUtil configNode_p)
    {
        super(seqview_p, configNode_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorView#init()
     */
    @Override
    protected void init() throws Exception
    {
        super.init();
        // add Claim button
        m_iClaimButton = getMenu().addFormMenuItem(this, getContext().localize("owbpm.OwAlfrescoBPMStandardProcessorView.claimbtn", "Claim"), "Claim", null, getFormName(), false);

        // add release to pool button
        m_iReleaseToPoolButton = getMenu().addFormMenuItem(this, getContext().localize("owbpm.OwAlfrescoBPMStandardProcessorView.releasetopoolbtn", "Release To Pool"), "ReleaseToPool", null, getFormName(), false);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorView#setObjectRef(com.wewebu.ow.server.ecm.OwObject, boolean)
     */
    @Override
    public void setObjectRef(OwObject objectRef_p, boolean showSystemProperties_p) throws Exception
    {
        super.setObjectRef(objectRef_p, showSystemProperties_p);
        updateButtonState();
    }

    private void updateButtonState() throws Exception
    {
        boolean isClaimable = isCurrentWoritemClaimable();
        boolean isReleasable = isCurrentWorkItemReleasable();

        getMenu().enable(m_iClaimButton, isClaimable);
        getMenu().enable(m_iReleaseToPoolButton, isReleasable);
    }

    private Boolean isCurrentWorkItemReleasable() throws Exception
    {
        OwWorkitem workItem = getWorkItem();
        boolean fLocked = workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        OwProperty isReleasableProp = null;
        Boolean isReleasableValue = Boolean.FALSE;
        try
        {
            isReleasableProp = workItem.getProperty("isReleasable");
            if (null != isReleasableProp.getValue())
            {
                isReleasableValue = (Boolean) isReleasableProp.getValue();
            }
        }
        catch (OwObjectNotFoundException objectNotFoundException)
        {
            // go on
        }

        return fLocked && isReleasableValue;
    }

    private Boolean isCurrentWoritemClaimable() throws Exception
    {
        OwWorkitem workItem = getWorkItem();
        boolean fLocked = workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);

        OwProperty isClaimableProp = null;
        Boolean isClaimableValue = Boolean.FALSE;
        try
        {
            isClaimableProp = workItem.getProperty("isClaimable");
            if (null != isClaimableProp.getValue())
            {
                isClaimableValue = (Boolean) isClaimableProp.getValue();
            }
        }
        catch (OwObjectNotFoundException objectNotFoundException)
        {
            // go on
        }

        return fLocked && isClaimableValue;
    }

    public void onClaim(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwWorkitem wi = getWorkItem();
        OwUserInfo userInfo = ((OwMainAppContext) getContext()).getCredentials().getUserInfo();
        wi.reassignToUserContainer(userInfo.getUserID(), false);
        updateButtonState();
    }

    public void onReleaseToPool(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwWorkitem wi = getWorkItem();
        wi.returnToSource();
        m_seqview.next(true);
    }
}
