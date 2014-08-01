package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * Handler for checkin operation. 
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
public interface OwCheckInHandler
{

    /**
     * Set the check in version mode. 
     * @param checkInAsMajorVersion_p
     */
    void setCheckInVersionMajor(boolean checkInAsMajorVersion_p);

    /**
     * Set the check in options.
     * @param checkinModeList_p - the list of checkin objects.
     * @param displayReleaseBox_p - flag for displaying a check box for release.
     */
    void setCheckInOptions(List checkinModeList_p, boolean displayReleaseBox_p) throws Exception;

    /**
     * Get selected check in mode.
     * @return the selected check in mode.
     */
    public Object getCheckinModeObject();

    /**
     * Getter for release version flag.
     * @return - <code>true</code> if the created document is released
     */
    public boolean isReleaseVersion();

    /**
     * Compute the checkin options, based on user selection.
     * @param request_p - the {@link HttpServletRequest} object
     */
    public void computeCheckinOptions(HttpServletRequest request_p);

    /**
     * Render the check in option (checkbox for creating a released version)
     * @param w_p - the {@link Writer} object
     * @param context_p - the {@link OwMainAppContext} object
     * @throws Exception
     */
    public void renderCheckInOptions(Writer w_p, OwMainAppContext context_p) throws Exception;

    /**
     * Render the check in modes.
     * @param w_p
     * @param context_p
     * @throws Exception
     */
    void renderCheckInModes(Writer w_p, OwMainAppContext context_p) throws Exception;
}
