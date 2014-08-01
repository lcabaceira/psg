package com.wewebu.ow.server.plug.owdemo.owmain;

import java.io.Writer;

import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * OwDemoColorLabelView.
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
public class OwDemoColorLabelView extends OwView
{
    /** color between 1 to 3 */
    protected int m_iColor;

    /** border width in pixel */
    protected int m_iBorderWidth = 1;

    /** called when the view should create its HTML content to be displayed
      * 
      */
    protected void init() throws Exception
    {
        super.init();
    }

    /** set the color
     * @param iColor_p int color between 1 to 3
     */
    public void setColor(int iColor_p)
    {
        m_iColor = iColor_p;
    }

    /** set the border width
     * @param iBorderWidth_p int border width in pixel
     */
    public void setBorderWidth(int iBorderWidth_p)
    {
        m_iBorderWidth = iBorderWidth_p;
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        //store color value in in Variable iColor
        String strColor;
        switch (m_iColor)
        {
            case 1:
                strColor = "red";
                break;
            case 2:
                strColor = "green";
                break;
            case 3:
                strColor = "blue";
                break;
            default:
                strColor = "white";
        }

        //Render HTML-Text    	
        w_p.write("<table border='" + String.valueOf(m_iBorderWidth) + "' bgcolor='#EAEEF3' bordercolor='#990000' width='100%' cellspacing='0'>");

        w_p.write("<tr><td>");
        w_p.write("Class: <b>OwDemoColorLabelView</b>");
        w_p.write("</td></tr>");

        w_p.write("<tr><td height='150' align='center'>");
        w_p.write("Color: <b>" + strColor + "</b>");
        w_p.write("</td></tr>");

        w_p.write("</table>");

    }
}