package com.wewebu.ow.server.plug.owdemo.owmain;

import java.io.Writer;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Layout of the Record plugin area.
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
public class OwDemoLayoutView extends OwLayout
{
    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;
    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;

    /** region for the menu */
    public static final int MENU_REGION = 1;
    /** region for the display*/
    public static final int DISPLAY_REGION = 2;
    /** region for the label view */
    public static final int LABEL_REGION = 3;

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // get and cast appcontext
        m_MainContext = (OwMainAppContext) getContext();
        // get application m_Configuration
        m_Configuration = m_MainContext.getConfiguration();
    }

    /** get the color for the background of the frame
     */
    public String getBGColor()
    {
        ////////////////////////////////////////////////////////
        // Demoline from Handbook, comment in to test the "MyColorValue" control.
        return "#FFFF99"; //(String)((OwMasterDocument)getDocument()).getSafeSetting("MyColorValue","red");
        ////////////////////////////////////////////////////////
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("demo/OwDemoLayoutView.jsp", w_p);
    }

}