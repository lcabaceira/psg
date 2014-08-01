package com.wewebu.ow.server.plug.owdemo.owmain;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Workdesk Demo Menu View.
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
public class OwDemoMenuView extends OwView
{
    protected OwSubMenuView m_Menu = new OwSubMenuView();

    /** called when the view should create its HTML content to be displayed */
    protected void init() throws Exception
    {
        super.init();

        // add menu
        addView(m_Menu, null);

        // add red menu item
        m_Menu.addMenuItem(this, "Red", "MenuRed", "Color Red");
        // add red menu item
        m_Menu.addMenuItem(this, "Green", "MenuGreen", "Color Green");
        // add red menu item
        m_Menu.addMenuItem(this, "Blue", "MenuBlue", "Color Blue");
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        //Render HTML-Text
        w_p.write("<table border='1'  bgcolor='#EAEEF3' bordercolor='#990000' cellspacing='0' width='100%'>");

        w_p.write("<tr><td>");
        w_p.write("Class: <b>OwDemoMenuView</b>");
        w_p.write("</td></tr>");

        w_p.write("<tr><td height='80' align='center'>");
        w_p.write("<b><a href='" + getEventURL("ClickRed", null) + "'>Red</a></b><br>");
        w_p.write("<b><a href='" + getEventURL("ClickGreen", null) + "'>Green</a></b><br>");
        w_p.write("<b><a href='" + getEventURL("ClickBlue", null) + "'>Blue</a></b>");
        w_p.write("</td></tr>");

        w_p.write("<tr><td height='70' align='center'>");
        w_p.write("<table border='1'  bgcolor='#D2DAE5' bordercolor='#D2DAE5' cellspacing='0' width='100%'>");
        w_p.write("<tr><td>");
        w_p.write("Class:<b>OwSubMenuView</b>");
        w_p.write("</td></tr>");
        w_p.write("<tr><td align='center'>");
        m_Menu.render(w_p);
        w_p.write("</td></tr>");
        w_p.write("</table>");
        w_p.write("</td></tr>");

        w_p.write("</table>");
    }

    /** click handler when user pressed the "red" button
     * @param request_p HttpServletRequest
     * */
    public void onClickRed(HttpServletRequest request_p) throws Exception
    {
        ((OwDemoDocument) getDocument()).setColor(OwDemoDocument.RED);
    }

    /** click handler when user pressed the "green" button
     * @param request_p HttpServletRequest
     * */
    public void onClickGreen(HttpServletRequest request_p) throws Exception
    {
        ((OwDemoDocument) getDocument()).setColor(OwDemoDocument.GREEN);
    }

    /** click handler when user pressed the "blue" button
     * @param request_p HttpServletRequest
     * */
    public void onClickBlue(HttpServletRequest request_p) throws Exception
    {
        ((OwDemoDocument) getDocument()).setColor(OwDemoDocument.BLUE);
    }

    /** click handler when user pressed the "red" button
     * @param request_p HttpServletRequest
     * @param reason_p Object optional reason 
     * */
    public void onMenuRed(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        ((OwDemoDocument) getDocument()).setColor(OwDemoDocument.RED);
    }

    /** click handler when user pressed the "green" button
     * @param request_p HttpServletRequest
     * @param reason_p Object optional reason 
     * */
    public void onMenuGreen(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        ((OwDemoDocument) getDocument()).setColor(OwDemoDocument.GREEN);
    }

    /** click handler when user pressed the "blue" button
     * @param request_p HttpServletRequest
     * @param reason_p Object optional reason 
     * */
    public void onMenuBlue(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        ((OwDemoDocument) getDocument()).setColor(OwDemoDocument.BLUE);
    }
}