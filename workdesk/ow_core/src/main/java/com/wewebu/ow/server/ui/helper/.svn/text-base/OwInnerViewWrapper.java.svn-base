package com.wewebu.ow.server.ui.helper;

import java.io.Writer;

import com.wewebu.ow.server.ui.OwBaseView;
import com.wewebu.ow.server.ui.OwLayout.OwLayoutViewRefrence;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Helper for wrapping different OwView.
 * This class delegates to the wrapped OwView,
 * or returns default values if not set.
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
 *@since 3.0.0.0
 */
public class OwInnerViewWrapper implements OwLayoutViewRefrence
{
    /** wrapped view contained the inner view */
    private OwView m_WrappedView;

    public OwInnerViewWrapper()
    {
        this(null);
    }

    public OwInnerViewWrapper(OwView wrappedView_p)
    {
        setView(wrappedView_p);
    }

    public String getTitle()
    {
        return m_WrappedView == null ? OwBaseView.EMPTY_STRING : m_WrappedView.getTitle();
    }

    /**
     * Get the view which is wrapped into this class.
     * @return OwView or null if no view is set.
     */
    public OwView getView()
    {
        return m_WrappedView;
    }

    public boolean isNamedRegion(String strRegion_p) throws Exception
    {
        if (m_WrappedView != null)
        {
            return m_WrappedView.isNamedRegion(strRegion_p);
        }
        else
        {
            return false;
        }
    }

    /** determine if region exists
     *
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p) throws Exception
    {
        if (m_WrappedView != null)
        {
            return m_WrappedView.isRegion(iRegion_p);
        }
        else
        {
            return false;
        }
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    public void render(Writer w_p) throws Exception
    {
        if (m_WrappedView != null)
        {
            m_WrappedView.render(w_p);
        }
    }

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param strRegion_p named region to render
     */
    public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
    {
        renderRegion(w_p, Integer.parseInt(strRegion_p));
    }

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        if (m_WrappedView != null)
        {
            m_WrappedView.renderRegion(w_p, iRegion_p);
        }
    }

    /** set inner view
     */
    public void setView(OwView view_p)
    {
        m_WrappedView = view_p;
    }

    public String getBreadcrumbPart()
    {
        return m_WrappedView == null ? getTitle() : m_WrappedView.getBreadcrumbPart();
    }

    @Override
    public boolean isEnabled()
    {
        return m_WrappedView != null;
    }

}