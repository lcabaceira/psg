package com.wewebu.ow.server.ui;

/**
 *<p>
 * View used to display changeable inner view.
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
public class OwDelegateView extends OwView
{
    /** current view */
    private OwView view;

    /**
     * returns inner view
     * @return inner view
     */
    public OwView getView()
    {
        return view;
    }

    /**
     * change inner view 
     * @param view_p
     * @throws Exception 
     */
    public void setView(OwView view_p) throws Exception
    {
        if (this.view != null)
        {
            this.view.detach();
            getViewList().clear();
        }

        this.view = view_p;
        addView(this.view, null);
    }

    @Override
    protected void onActivate(int index_p, Object oReason_p) throws Exception
    {
        if (this.view != null)
        {
            this.view.onActivate(index_p, oReason_p);
        }
    }
}
