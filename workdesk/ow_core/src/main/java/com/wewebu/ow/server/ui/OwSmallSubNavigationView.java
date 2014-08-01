package com.wewebu.ow.server.ui;

import java.io.Writer;

import com.wewebu.ow.server.app.OwSubNavigationView;

/**
 *<p>
 * View: OwSmallSubNavigationView.
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
public class OwSmallSubNavigationView extends OwSubNavigationView
{
    private boolean m_renderVeiewReference = false;

    public OwSmallSubNavigationView()
    {
        this(false);
    }

    public OwSmallSubNavigationView(boolean renderVeiewReference_p)
    {
        this.m_renderVeiewReference = renderVeiewReference_p;
    }

    protected String getIncludeJsp()
    {
        return "OwSmallSubNavigationView.jsp";
    }

    protected void onRender(Writer w_p) throws Exception
    {
        super.onRender(w_p);

        if (m_renderVeiewReference)
        {
            w_p.write("<div style='float:left;clear:left;overflow:hidden;'>");
            OwBaseView currentViewReference = getViewReference();
            currentViewReference.render(w_p);
            w_p.write("</div>");

        }

    }
}
