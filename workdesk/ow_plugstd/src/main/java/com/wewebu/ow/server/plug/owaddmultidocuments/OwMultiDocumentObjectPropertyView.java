package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.views.OwCheckInHandler;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.OwStandardCheckInHandler;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwProperty;

/**
 *<p>
 * View Module to edit OwObject Properties.
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
public class OwMultiDocumentObjectPropertyView extends OwObjectPropertyView implements OwCheckInHandler
{
    private Collection<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> virtualParentRestriction;

    /**
     * 
     * @param enablesaveall_p flag to enable save all button option when more than one documents are selected
     */
    public OwMultiDocumentObjectPropertyView(boolean enablesaveall_p)
    {
        super();

        this.m_enablesaveall = enablesaveall_p;
        this.m_saveAllActive = true;

        // disable internal modes of property view, we display a own mode box here
        setModeType(OwObjectClass.OPERATION_TYPE_UNDEF);
        m_checkInHandler = createCheckInHandler();
    }

    /**
     * Creates the {@link OwCheckInHandler} object
     * @return the newly created {@link OwCheckInHandler} object
     * @since 3.1.0.0
     */
    protected OwCheckInHandler createCheckInHandler()
    {
        return new OwStandardCheckInHandler();
    }

    /** event listener */
    private OwMultiDocumentObjectPropertyViewListner m_EventListner = null;

    public void setEventListener(OwMultiDocumentObjectPropertyViewListner eventListener_p)
    {
        m_EventListner = eventListener_p;
    }

    /** menu ID of the SaveAll button */
    protected int m_saveAllButtonIndex;

    /**
     * the check in handler
     */
    protected OwCheckInHandler m_checkInHandler = null;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        // initialize parent
        super.init();

        // add the save all button
        if (getMenu() != null)
        {
            // apply button
            if (m_enablesaveall)
            {
                m_saveAllButtonIndex = getMenu().addFormMenuItem(this, getContext().localize("plug.owaddmultidocuments.OwMultiDocumentObjectPropertyView.SaveAll", "Save all"), "SaveAll", null);
            }

            // initially disable
            getMenu().enable(m_iAppyBtnIndex, false);
        }
    }

    /** event called when user clicked Apply button in menu 
     * 
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Optional reason object submitted in addMenuItem
     */
    public boolean onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        computeCheckinOptions(request_p);
        // onApply
        return onApplyInternal(request_p, oReason_p);
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
     * Compute the checkin options.
     * @param request_p - the {@link HttpServletRequest} object
     */
    public void computeCheckinOptions(HttpServletRequest request_p)
    {
        m_checkInHandler.computeCheckinOptions(request_p);
    }

    /** 
     * event called to save changes
     *  
     *   call getUpdateStatus() after save to check if fields are invalid
     *   
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   
     * 
     *  @return true = fields have been saved, false = nothing has been saved
     */
    protected boolean onApplyInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // parent onApply
        boolean fRet = super.onApplyInternal(request_p, oReason_p);

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

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        // render internal regions
        switch (iRegion_p)
        {
            case MENU_REGION:
                renderMenuRegion(w_p);
                break;

            default:
                // render attached views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** 
     * event called to save changes
     *  
     *   call getUpdateStatus() after save to check if fields are invalid
     *   
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   
     * 
     *  @return true = fields have been saved, false = nothing has been saved
     */
    protected boolean onSaveAllInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // parent onApply
        boolean fRet = super.onApplyInternal(request_p, oReason_p);

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

    /**
     * display checkin options
     * 
     * @param modes_p
     *            List of modes as defined in OwVersion
     * @param fDisplayReleaseBox_p
     *            boolean true = display a release checkbox
     */
    public void setCheckInOptions(List modes_p, boolean fDisplayReleaseBox_p) throws Exception
    {
        m_checkInHandler.setCheckInOptions(modes_p, fDisplayReleaseBox_p);
    }

    /**
     *  set if check-in Version is major or minor
     * @param checkInAsMajorVersion_p
     */
    public void setCheckInVersionMajor(boolean checkInAsMajorVersion_p)
    {
        m_checkInHandler.setCheckInVersionMajor(checkInAsMajorVersion_p);
    }

    /** render the main region with the property list
     * overrides the OwObjectPropertyView.renderMainRegin method in order to display checkin options
     * @param w_p Writer object to write HTML to
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        // render properties by parent
        super.renderMainRegion(w_p);
        // disable / enable SaveAll button
        if (getMenu() != null)
        {
            // disable apply button if not editable
            if (m_enablesaveall)
            {
                boolean visualyEnabled = !m_fReadOnly && m_saveAllActive;
                getMenu().enable(m_saveAllButtonIndex, visualyEnabled);
            }
        }
    }

    /** render the menu region with the release box and checkin modes
     * overrides the OwObjectPropertyView.renderMenuRegion method in order to display checkin options
     * @param w_p Writer object to write HTML to
     */
    protected void renderMenuRegion(Writer w_p) throws Exception
    {
        w_p.write("<div style='clear:both'>\n");
        // render checkin options
        renderCheckInOptions(w_p, (OwMainAppContext) getContext());

        renderCheckInModes(w_p, (OwMainAppContext) getContext());

        // render properties by parent
        w_p.write("<div style='float:left'>\n");
        super.renderRegion(w_p, MENU_REGION);
        w_p.write("</div>\n");
        w_p.write("</div>\n");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owaddmultidocuments.OwCheckInHandler#renderCheckInModes(java.io.Writer, com.wewebu.ow.server.app.OwMainAppContext)
     */
    public void renderCheckInModes(Writer w_p, OwMainAppContext context_p) throws Exception
    {
        m_checkInHandler.renderCheckInModes(w_p, context_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owaddmultidocuments.OwCheckInHandler#getCheckinModeObject()
     */
    public Object getCheckinModeObject()
    {
        return m_checkInHandler.getCheckinModeObject();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owaddmultidocuments.OwCheckInHandler#isReleaseVersion()
     */
    public boolean isReleaseVersion()
    {
        return m_checkInHandler.isReleaseVersion();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owaddmultidocuments.OwCheckInHandler#renderCheckInOptions(java.io.Writer, com.wewebu.ow.server.app.OwMainAppContext)
     */
    public void renderCheckInOptions(Writer w_p, OwMainAppContext context_p) throws Exception
    {
        m_checkInHandler.renderCheckInOptions(w_p, context_p);
    }

    /**
     * Set a list of virtual parent defined restrictions
     * @param propInfoList List of OwPropertyInfo
     * @since 4.2.0.0
     */
    public void setVirtualParentRestriction(Collection<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> propInfoList)
    {
        this.virtualParentRestriction = propInfoList;
    }

    /**
     * Get defined restrictions
     * @return List of OwPropertyInfo
     * @since 4.2.0.0
     */
    public Collection<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> getVirtualParentRestriction()
    {
        if (this.virtualParentRestriction == null)
        {
            return Collections.emptyList();
        }
        return this.virtualParentRestriction;
    }

    @Override
    protected boolean isPropertyReadonly(OwProperty property_p) throws Exception
    {
        Collection<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> vfLst = getVirtualParentRestriction();
        if (!vfLst.isEmpty())
        {
            for (org.alfresco.wd.ui.conf.prop.OwPropertyInfo propInfo : vfLst)
            {
                if (propInfo.getPropertyName().equals(property_p.getPropertyClass().getClassName()))
                {
                    return true;
                }
            }
        }
        return super.isPropertyReadonly(property_p);
    }
}