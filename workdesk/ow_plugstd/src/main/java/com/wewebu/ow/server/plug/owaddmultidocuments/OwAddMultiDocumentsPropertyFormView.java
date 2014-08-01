package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.views.OwCheckInHandler;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwStandardCheckInHandler;

/**
 *<p>
 * View Module to edit OwObject Properties using a JSP file for rendering.
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
public class OwAddMultiDocumentsPropertyFormView extends OwObjectPropertyFormularView implements OwCheckInHandler
{
    /** checkin handler*/
    protected OwCheckInHandler m_checkInHandler = null;
    /** event listener */
    private OwMultiDocumentObjectPropertyViewListner m_EventListner;

    /**
     * Constructor
     */
    public OwAddMultiDocumentsPropertyFormView(boolean enableSaveAll_p)
    {
        m_checkInHandler = createCheckInHandler();
        m_enablesaveall = enableSaveAll_p;
    }

    protected OwCheckInHandler createCheckInHandler()
    {
        return new OwStandardCheckInHandler();
    }

    public void computeCheckinOptions(HttpServletRequest request_p)
    {
        m_checkInHandler.computeCheckinOptions(request_p);
    }

    public Object getCheckinModeObject()
    {
        return m_checkInHandler.getCheckinModeObject();
    }

    public boolean isReleaseVersion()
    {
        return m_checkInHandler.isReleaseVersion();
    }

    public void renderCheckInModes(Writer w_p, OwMainAppContext context_p) throws Exception
    {
        m_checkInHandler.renderCheckInModes(w_p, context_p);
    }

    public void renderCheckInOptions(Writer w_p, OwMainAppContext context_p) throws Exception
    {
        m_checkInHandler.renderCheckInOptions(w_p, context_p);
    }

    public void setCheckInOptions(List checkinModeList_p, boolean displayReleaseBox_p) throws Exception
    {
        m_checkInHandler.setCheckInOptions(checkinModeList_p, displayReleaseBox_p);
    }

    public void setCheckInVersionMajor(boolean checkInAsMajorVersion_p)
    {
        m_checkInHandler.setCheckInVersionMajor(checkInAsMajorVersion_p);

    }

    /** render the menu region */
    protected void renderMenuRegion(Writer w_p) throws Exception
    {
        w_p.write("<div style='clear:both'>\n");
        changeSaveAllButtonState();
        // render checkin options
        renderCheckInOptions(w_p, (OwMainAppContext) getContext());

        renderCheckInModes(w_p, (OwMainAppContext) getContext());

        // render properties by parent
        w_p.write("<div style='float:left'>\n");
        super.renderMenuRegion(w_p);
        w_p.write("</div>\n");
        w_p.write("</div>\n");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView#onSave(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    public boolean onSave(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        computeCheckinOptions(request_p);
        // on save
        return onSaveInternal(request_p, oReason_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView#onSaveInternal(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    protected boolean onSaveInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {

        // parent onSave
        boolean fRet = super.onSaveInternal(request_p, oReason_p);

        if (getUpdateStatus())
        {
            // call event
            if (m_EventListner != null)
            {
                m_EventListner.onSaveDocument();
            }
        }

        return fRet;

    }

    /**
     * Setter for event listener
     * @param eventListner_p - the listener for save event.
     */
    public void setEventListner(OwMultiDocumentObjectPropertyViewListner eventListner_p)
    {
        m_EventListner = eventListner_p;
    }

    protected void init() throws Exception
    {
        super.init();
        addSaveAllButton(getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectPropertyView.SaveAll", "Save all"));
    }

    /** event called when user clicked SaveAll button in menu 
     * 
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Optional reason object submitted in addMenuItem
     */
    public boolean onSaveAll(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        computeCheckinOptions(request_p);
        // onApply
        return onSaveAllInternal(request_p, oReason_p);
    }

    /**
     * Save current properties and notify the {@link OwMultiDocumentObjectPropertyViewListner} about this operation.
     * @param request_p
     * @param oReason_p
     * @return - <code>true</code> if the save operation was successful.
     * @throws Exception
     */
    protected boolean onSaveAllInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // parent onApply
        boolean fRet = super.onSaveInternal(request_p, oReason_p);

        if (getUpdateStatus())
        {
            // call event
            if (m_EventListner != null)
            {
                m_EventListner.onSaveAllDocument();
            }
        }

        return fRet;
    }
}
