package com.wewebu.ow.server.ecmimpl.owsimpleadp;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Login module/view that will be embedded into the login page.
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
public class OwSimpleLoginSubModul extends OwUILoginModul
{
    // =============================================================================
    //
    //
    // MINIMAL IMPLEMENTATION
    //
    //
    // =============================================================================    

    /** render the module/view
     *  this part is embedded into the login page of Workdesk
     */
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("Welcome to the simple adapter");
        w_p.write("<br>");
        w_p.write("<br>");

        // create user and password text field's
        w_p.write("<input type='text' id='username' name='username'>");
        w_p.write("<br>");
        w_p.write("<input type='text' id='password' name='password'>");
        w_p.write("<br>");

        // create a login button
        w_p.write("<a href=\"");
        w_p.write(getFormEventURL("Login", null));
        w_p.write("\">");
        w_p.write("Login");
        w_p.write("</a>");

        // === optional
        // set focus to name element
        getContext().setFocusControlID("username");

        // enable keyboard support for login button event
        getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, getEventURL("Login", null), getFormName(), "Login");
    }

    /** force module/view to create a form and form event's
     *  no need to render a form element, this is done by the core ui
     * 
     */
    protected String usesFormWithAttributes()
    {
        return "";
    }

    /** called via reflection when user presses the "Login" button
     *  method name = on[eventname from getEventURL]
     *  
     * @param request_p
     * @throws Exception
     */
    public void onLogin(HttpServletRequest request_p) throws Exception
    {
        String username = request_p.getParameter("username");
        String password = request_p.getParameter("password");

        ((OwMainAppContext) getContext()).getNetwork().loginDefault(username, password);
    }

}