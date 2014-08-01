package com.wewebu.ow.server.plug.owdemo.owmain;

import java.io.Writer;

import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Workdesk Demo View.
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
public class OwDemoView extends OwMasterView
{
    /** reference to the label view */
    OwDemoColorLabelView m_colorLabelView;

    /** called when the view should create its HTML content to be displayed*/
    protected void init() throws Exception
    {
        super.init();

        // attached LAYOUT-View
        OwDemoLayoutView layoutView = new OwDemoLayoutView();
        addView(layoutView, null);

        // attached MENU-View to LAYOUT-View
        OwDemoMenuView menuView = new OwDemoMenuView();
        layoutView.addView(menuView, OwDemoLayoutView.MENU_REGION, null);

        // attached DISPLAY-View to LAYOUT-View
        OwDemoDisplayView displayView = new OwDemoDisplayView();
        layoutView.addView(displayView, OwDemoLayoutView.DISPLAY_REGION, null);

        //attached COLORLABEL-View to DISPLAY-View
        m_colorLabelView = new OwDemoColorLabelView();
        layoutView.addView(m_colorLabelView, OwDemoLayoutView.LABEL_REGION, null);
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        super.onRender(w_p);
    }

    /** called by the framework to update the view when OwDocument.Update was called
    *
    *  NOTE:   We can not use the onRender method to update,
    *          because we do not know the call order of onRender.
    *          onUpdate is always called before all onRender methods.
    *
    *  @param caller_p OwEventTarget target that called update
    *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_SETTINGS:
                // get the border width from plugin settings set
                int iBorderWidth = ((Integer) ((OwMasterDocument) getDocument()).getSafeSetting("borderwidth", Integer.valueOf(1))).intValue();
                // set in view
                m_colorLabelView.setBorderWidth(iBorderWidth);
                break;

            default:
                m_colorLabelView.setColor(((OwDemoDocument) getDocument()).getColor());
                break;
        }
    }
}