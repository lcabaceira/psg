package com.wewebu.ow.server.plug.owdemo.owmain;

import java.io.Writer;

import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Workdesk Demo Display View.
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
public class OwDemoDisplayView extends OwView
{

    /** called when the view should create its HTML content to be displayed */
    protected void init() throws Exception
    {
        super.init();

    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        //store color value in in Variable iColor
        int iColor = ((OwDemoDocument) getDocument()).getColor();

        // get the border width from plugin settings set
        int iBorderWidth = ((Integer) ((OwMasterDocument) getDocument()).getSafeSetting("borderwidth", Integer.valueOf(1))).intValue();

        String strColor = "FFFFFF";

        switch (iColor)
        {
            case 1:
                strColor = "CC0000";
                break;
            case 2:
                strColor = "009900";
                break;
            case 3:
                strColor = "003366";
                break;
            default:
                strColor = "FFFFFF";
        }

        //Render HTML-Text    	
        w_p.write("<table border='" + String.valueOf(iBorderWidth) + "' bgcolor='#EAEEF3' bordercolor='#990000' width='100%' cellspacing='0'>");

        w_p.write("<tr><td>");
        w_p.write("Class: <b>OwDemoDisplayView</b>");
        w_p.write("</td></tr>");

        w_p.write("<tr><td bgcolor='#" + strColor + "' height='150' align='center'>");
        w_p.write("Color: " + String.valueOf(((OwDemoDocument) getDocument()).getColor()));
        w_p.write("</td></tr>");

        w_p.write("</table>");

    }

}