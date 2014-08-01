package com.wewebu.ow.server.app;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Settings value for the window / viewer positions when the viewer is opened via autoopen feature. <br/>
 * (see autoviewermode in owmimetable.xml, getWindowPositions() in {@link OwMainAppContext} and openObject in {@link OwMimeManager})
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
public abstract class OwWindowPositions
{
    /**
     *<p>
     * Undefined window position.
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
    private static class OwWindowPositionsUndef extends OwWindowPositions
    {
        public int getViewerWidth()
        {

            return 0;
        }

        public int getViewerHeight()
        {

            return 0;
        }

        public int getViewerTopX()
        {

            return 0;
        }

        public int getViewerTopY()
        {

            return 0;
        }

        public int getWindowWidth()
        {

            return 0;
        }

        public int getWindowHeight()
        {

            return 0;
        }

        public int getWindowTopX()
        {

            return 0;
        }

        public int getWindowTopY()
        {

            return 0;
        }

        public boolean getPositionMainWindow()
        {

            return false;
        }

        public int getUnits()
        {

            return OwWindowPositions.UNITS_PIXEL;
        }

        public void setFromRequest(HttpServletRequest request_p, String strBaseID_p) throws Exception
        {

            throw new OwInvalidOperationException("OwWindowPositions$OwWindowPositionsUndef.setFromRequest: Not implemented.");
        }
    }

    /** undefined window position */
    private static OwWindowPositionsUndef m_undef = new OwWindowPositionsUndef();

    /** units, see getUnits */
    public static final int UNITS_PIXEL = 0;
    /** units, see getUnits */
    public static final int UNITS_PERCENT = 1;

    /** get a undefined window position */
    public static OwWindowPositions getUndefWindowPosition()
    {
        return m_undef;
    }

    // === get the viewer position
    public abstract int getViewerWidth();

    public abstract int getViewerHeight();

    public abstract int getViewerTopX();

    public abstract int getViewerTopY();

    //	 === get the main window position
    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public abstract int getWindowTopX();

    public abstract int getWindowTopY();

    // === flag indicating if main window should be positioned to when viewer is opened
    public abstract boolean getPositionMainWindow();

    /** return units as defined with UNITS_... */
    public abstract int getUnits();

    /** set value from request
    *
    * @param request_p HttpServletRequest with form data to update the property
    * @param strBaseID_p String the HTML form element base ID of the requested value
    * 
     * @throws OwInvalidOperationException 
    */
    public abstract void setFromRequest(HttpServletRequest request_p, String strBaseID_p) throws Exception;
}