package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.field.OwEnum;

/**
 *<p>
 * Standard implementation for {@link OwCheckInHandler}. 
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
public class OwStandardCheckInHandler implements OwCheckInHandler
{
    /** check in modes list*/
    protected List m_checkInModes;
    /** flag for displaying release checkbox*/
    protected boolean m_displayReleaseBox;
    /** major version check in flag */
    protected boolean m_checkInAsMajorVersion;
    /** release version flag*/
    protected boolean m_releaseVersion = false;
    /** current checkin mode selection*/
    protected Object m_ModeObject = null;

    /** name of the HTML release checkbox */
    public static final String CHECKIN_RELEASE_CONTROL_NAME = "OwCheckInReleaseCtrl";
    /** name of the HTML release checkbox */
    public static final String CHECKIN_MODES_CONTROL_NAME = "OwCheckInModesCtrl";

    public void setCheckInOptions(List modes_p, boolean fDisplayReleaseBox_p) throws Exception
    {
        m_checkInModes = modes_p;
        m_displayReleaseBox = fDisplayReleaseBox_p;
    }

    /**
     *  set if check-in Version is major or minor
     * @param checkInAsMajorVersion_p
     */
    public void setCheckInVersionMajor(boolean checkInAsMajorVersion_p)
    {
        m_checkInAsMajorVersion = checkInAsMajorVersion_p;
    }

    /**
     * Compute the checkin options.
     * @param request_p - the {@link HttpServletRequest} object
     */
    public void computeCheckinOptions(HttpServletRequest request_p)
    {

        m_releaseVersion = m_checkInAsMajorVersion;
        if (m_displayReleaseBox)
        {
            m_releaseVersion = request_p.getParameter(CHECKIN_RELEASE_CONTROL_NAME) != null && request_p.getParameter(CHECKIN_RELEASE_CONTROL_NAME).equals("true");
        }
        if (((m_checkInModes != null) && (m_checkInModes.size() > 0)))
        {
            int iCheckInModeIndex = Integer.parseInt(request_p.getParameter(CHECKIN_MODES_CONTROL_NAME));
            m_ModeObject = ((OwEnum) m_checkInModes.get(iCheckInModeIndex)).getValue();
        }
    }

    public Object getCheckinModeObject()
    {
        return m_ModeObject;
    }

    public boolean isReleaseVersion()
    {
        return m_releaseVersion;
    }

    public void renderCheckInOptions(Writer w_p, OwMainAppContext context_p) throws Exception
    {
        if (m_displayReleaseBox)
        {
            w_p.write("<div style='float:left'>\n");
            OwComboModel comboModel = new OwDefaultComboModel("" + this.m_checkInAsMajorVersion, new String[] { "" + true, "" + false }, new String[] {
                    context_p.localize("owsavedialog.impl.OwSaveDlgObjectPropertyView.releaseversion", "Create Release Version"), context_p.localize("owsavedialog.impl.OwSaveDlgObjectPropertyView.noreleaseversion", "Do not create Release Version") });

            OwInsertLabelHelper.insertLabelValue(w_p, context_p.localize("OwStandardCheckInHandler.checkInOption", "CheckIn Option"), CHECKIN_RELEASE_CONTROL_NAME);
            OwComboboxRenderer renderer = context_p.createComboboxRenderer(comboModel, CHECKIN_RELEASE_CONTROL_NAME, null, null, null);
            renderer.renderCombo(w_p);
            w_p.write("</div>\n");
        }
    }

    public void renderCheckInModes(Writer w_p, OwMainAppContext context_p) throws Exception
    {
        if ((m_checkInModes != null) && (m_checkInModes.size() > 0))
        {
            w_p.write("<div style='float:left'>\n");

            List comboItems = new LinkedList();

            for (int i = 0; i < m_checkInModes.size(); i++)
            {
                OwEnum oItem = (OwEnum) m_checkInModes.get(i);
                OwDefaultComboItem item = new OwDefaultComboItem(String.valueOf(i), oItem.getDisplayName(context_p.getLocale()));
                comboItems.add(item);
            }
            OwComboModel model = new OwDefaultComboModel(null, comboItems);
            OwComboboxRenderer renderer = context_p.createComboboxRenderer(model, CHECKIN_MODES_CONTROL_NAME, null, null, null);
            OwInsertLabelHelper.insertLabelValue(w_p, context_p.localize("OwStandardCheckInHandler.checkInModes", "CheckIn Modes"), CHECKIN_MODES_CONTROL_NAME);
            renderer.renderCombo(w_p);

            w_p.write("</div>\n");
        }

    }
}
