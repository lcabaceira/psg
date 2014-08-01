package com.wewebu.ow.server.app;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.ui.button.OwImageButton;
import com.wewebu.ow.server.ui.button.OwImageButtonView;

/**
 *<p>
 * View Module to display a maximize minimize button and maximize minimize the attached view.
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
public class OwMaxMinButtonControlView extends OwImageButtonView
{
    private static final Logger LOG = OwLogCore.getLogger(OwMaxMinButtonControlView.class);

    /** attached OwView to be maximized minimized */
    protected OwView m_view;

    /** flag for maximize button */
    public static final int MODE_MAXIMIZE_BUTTON = 0x0001;
    /** flag for maximize button */
    public static final int MODE_MINIMIZE_BUTTON = 0x0002;

    /** display mode */
    private int m_iMode = MODE_MAXIMIZE_BUTTON;

    /** attach a view to the control use maximize button
     * @param view_p OwView to be maximized minimized
     */
    public OwMaxMinButtonControlView(OwView view_p)
    {
        m_view = view_p;
    }

    /** attach a view to the control, select button mode
     * @param view_p OwView to be maximized minimized
     * @param iMode_p int any combination of MODE_...
     */
    public OwMaxMinButtonControlView(OwView view_p, int iMode_p)
    {
        m_view = view_p;
        m_iMode = iMode_p;
    }

    /* called when the view should create its HTML content to be displayed
     *
     * @param w_p Writer object to write HTML to
     *

    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwMaxMinButtonControlView.jsp", w_p);
    }     */

    @Override
    protected void init() throws Exception
    {
        super.init();
        getDesignClasses().add("OwMaxMinButtonControlView");
    }

    /** overridable get the icon URL 
     * @return String URL to the icon
     * @throws Exception 
     * */
    public String getMaximizeIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/OwMaxMinButtonControlView/maximize.png";
    }

    /** overridable get the icon URL 
     * @return String URL to the icon
     * @throws Exception 
     * */
    public String getMinimizeIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/OwMaxMinButtonControlView/minimize.png";
    }

    /** overridable get the icon URL 
     * @return String URL to the icon
     * @throws Exception 
     * */
    public String getNormalIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/OwMaxMinButtonControlView/normal.png";
    }

    /** get the title of the controlled view
     * @return String
     * */
    public String getControlledViewTitle()
    {
        return m_view.getTitle();
    }

    /** get the display mode
     * @return int
     * */
    public int getDisplayMode()
    {
        return m_iMode;
    }

    /** get the isShowNormal flag
     * @return boolean
     * */
    public boolean getIsShowNormal()
    {
        return m_view.isShowNormal();
    }

    /** event called when user clicked maximize
     *   @param request_p  HttpServletRequest
     */
    public void onMaximize(HttpServletRequest request_p) throws Exception
    {
        m_view.showMaximized();
    }

    /** event called when user clicked minimize
     *   @param request_p  HttpServletRequest
     */
    public void onNormal(HttpServletRequest request_p) throws Exception
    {
        m_view.showNormal();
    }

    /** event called when user clicked minimize
     *   @param request_p  HttpServletRequest
     */
    public void onMinimize(HttpServletRequest request_p) throws Exception
    {
        m_view.showMinimized();
    }

    public List<OwImageButton> getButtonList()
    {
        LinkedList<OwImageButton> buttons = new LinkedList<OwImageButton>();
        String tooltip = null, eventUrl = null, image = "";
        if (!getIsShowNormal())
        {
            tooltip = getContext().localize("app.OwMaxMinButtonControlView.normal", "Default view");
            eventUrl = getEventURL("Normal", null);
            try
            {
                image = getNormalIcon();
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Image could not be retrieved", e);
                }
                else
                {
                    LOG.warn("Image could not be retrieved.");
                }
            }
        }
        else
        {
            if ((getDisplayMode() & OwMaxMinButtonControlView.MODE_MINIMIZE_BUTTON) != 0)
            {
                tooltip = getContext().localize("app.OwMaxMinButtonControlView.minimize", "Minimize");
                eventUrl = getEventURL("Minimize", null);
                try
                {
                    image = getMinimizeIcon();
                }
                catch (Exception e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Image could not be retrieved", e);
                    }
                    else
                    {
                        LOG.warn("Image could not be retrieved.");
                    }
                }
            }

            if ((getDisplayMode() & OwMaxMinButtonControlView.MODE_MAXIMIZE_BUTTON) != 0)
            {
                tooltip = getContext().localize("app.OwMaxMinButtonControlView.maximize", "Maximize");
                eventUrl = getEventURL("Maximize", null);
                try
                {
                    image = getMaximizeIcon();
                }
                catch (Exception e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Image could not be retrieved", e);
                    }
                    else
                    {
                        LOG.warn("Image could not be retrieved.");
                    }
                }
            }
        }
        buttons.add(new OwImageButton(eventUrl, image, tooltip));
        return buttons;
    }
}