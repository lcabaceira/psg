package com.wewebu.ow.server.plug.owdms;

import java.io.Writer;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.field.OwEnum;

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
public class OwSaveDlgObjectPropertyView extends OwObjectPropertyView
{

    /**
     *<p>
     * The EventListener interface for Events fired by this OwSaveDlgObjectPropertyView.
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
    public interface OwSaveDlgObjectPropertyViewListner extends EventListener
    {

        /**
         * Event fired when user clicks save and all properties are correct
         */
        public abstract void onSaveDocument() throws Exception;

    }

    /** name of the HTML release checkbox */
    protected static final String CHECKIN_RELEASE_CONTROL_NAME = "OwCheckInReleaseCtrl";

    /** name of the HTML release checkbox */
    protected static final String CHECKIN_MODES_CONTROL_NAME = "OwCheckInModesCtrl";

    /** Collection of modes as defined in OwVersion */
    protected List m_CheckInModes = null;

    /** boolean true = display a release checkbox */
    protected boolean m_fDisplayReleaseBox = false;

    /** boolean true = checkin as major version */
    protected boolean m_checkInAsMajorVersion = true;

    /** user selected release version yes/no */
    protected boolean m_ReleaseVersion = false;

    /** user selected checkin mode */
    protected Object m_ModeObject = null;

    /** event listener */
    private OwSaveDlgObjectPropertyViewListner m_EventListner = null;

    /**
     * Create a new <code>OwSaveDlgObjectPropertyView</code>
     */
    public OwSaveDlgObjectPropertyView()
    {
        super();

        // disable internal modes of property view, we display a own mode box here
        setModeType(OwObjectClass.OPERATION_TYPE_UNDEF);
    }

    /**
     * Set the EventListener that receives the <code>onSaveDocument</code> Event
     * 
     * @param eventListner_p the new EventListener that receives the <code>onSaveDocument</code> Event
     */
    public void setEventListener(OwSaveDlgObjectPropertyViewListner eventListner_p)
    {
        m_EventListner = eventListner_p;
    }

    /**
     * Returns the user selected ReleaseVersion yes/no
     * 
     * @return the user selected ReleaseVersion yes/no
     */
    public boolean getReleaseVersion()
    {
        return (m_ReleaseVersion);
    }

    /**
     * Returns the user selected checkin mode
     * 
     * @return the user selected checkin mode
     */
    public Object getModeObject()
    {
        return (m_ModeObject);
    }

    /**
     * Initialize the target after the context is set.
     */
    protected void init() throws Exception
    {
        // initialize parent
        super.init();
    }

    /**
     * Event called when user clicked Apply button in menu 
     * 
     * @param request_p a {@link HttpServletRequest}
     * @param oReason_p Optional reason object submitted in addMenuItem
     * 
     * @return true = fields have been saved, false = nothing has been saved
     */
    public boolean onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // get checkin options from request
        m_ReleaseVersion = m_checkInAsMajorVersion;
        m_ModeObject = null;
        if (m_fDisplayReleaseBox)
        {
            m_ReleaseVersion = request_p.getParameter(CHECKIN_RELEASE_CONTROL_NAME) != null && request_p.getParameter(CHECKIN_RELEASE_CONTROL_NAME).equals("true");

        }
        if (((m_CheckInModes != null) && (m_CheckInModes.size() > 0)))
        {
            int iCheckInModeIndex = Integer.parseInt(request_p.getParameter(CHECKIN_MODES_CONTROL_NAME));
            m_ModeObject = ((OwEnum) m_CheckInModes.get(iCheckInModeIndex)).getValue();
        }
        // onApply
        return onApplyInternal(request_p, oReason_p);
    }

    /**
     * Internal Event handler for <code>{@link #onApply(HttpServletRequest, Object)}</code>
     * 
     * @param request_p a {@link HttpServletRequest}
     * @param oReason_p Optional reason object submitted in addMenuItem
     * 
     * @return true = fields have been saved, false = nothing has been saved
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
     * display checkin options
     * 
     * @param modes_p
     *            List of modes as defined in OwVersion
     * @param fDisplayReleaseBox_p
     *            boolean true = display a release checkbox
     */
    public void setCheckInOptions(List modes_p, boolean fDisplayReleaseBox_p) throws Exception
    {
        m_CheckInModes = modes_p;
        m_fDisplayReleaseBox = fDisplayReleaseBox_p;
    }

    /**
     *  set if check-in Version is major or minor
     * @param checkInAsMajorVersion_p
     */
    public void setCheckInVersionMajor(boolean checkInAsMajorVersion_p)
    {
        m_checkInAsMajorVersion = checkInAsMajorVersion_p;
    }

    /** render the menu region with the release box and checkin modes
     * overrides the OwObjectPropertyView.renderMenuRegion method in order to display checkin options
     * @param w_p Writer object to write HTML to
     */
    protected void renderMenuRegion(Writer w_p) throws Exception
    {
        // render checkin options
        if (m_fDisplayReleaseBox)
        {
            OwComboModel comboModel = new OwDefaultComboModel("" + this.m_checkInAsMajorVersion, new String[] { "" + true, "" + false }, new String[] {
                    getContext().localize("owsavedialog.impl.OwSaveDlgObjectPropertyView.releaseversion", "Release Version"), getContext().localize("owsavedialog.impl.OwSaveDlgObjectPropertyView.noreleaseversion", "Keine Release Version") });
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, CHECKIN_RELEASE_CONTROL_NAME, null, null, null);
            String checkinReleaseDisplayName = getContext().localize("OwSaveDlgObjectPropertyView.checkinReleaseDisplayName", "Select checkin release");
            OwInsertLabelHelper.insertLabelValue(w_p, checkinReleaseDisplayName, CHECKIN_RELEASE_CONTROL_NAME);
            renderer.renderCombo(w_p);
        }

        if ((m_CheckInModes != null) && (m_CheckInModes.size() > 0))
        {

            List comboItems = new LinkedList();

            for (int i = 0; i < m_CheckInModes.size(); i++)
            {
                OwEnum oItem = (OwEnum) m_CheckInModes.get(i);
                OwDefaultComboItem item = new OwDefaultComboItem(String.valueOf(i), oItem.getDisplayName(getContext().getLocale()));
                comboItems.add(item);
            }
            OwComboModel model = new OwDefaultComboModel(false, false, null, comboItems);
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, CHECKIN_MODES_CONTROL_NAME, null, null, null);
            String checkinModesDisplayName = getContext().localize("OwSaveDlgObjectPropertyView.checkinModesDisplayName", "Select checkin mode");
            OwInsertLabelHelper.insertLabelValue(w_p, checkinModesDisplayName, CHECKIN_MODES_CONTROL_NAME);
            renderer.renderCombo(w_p);

        }

        // render properties by parent
        super.renderRegion(w_p, MENU_REGION);
    }

}