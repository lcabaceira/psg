package com.wewebu.ow.server.ecmimpl.owdummy.ui;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyNetwork;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Dummy Implementation for login sub module to be created in the network (DMS) Adaptor. Submodules are used to delegate
 * DMS specific User Interactions to the DMS Adaptor, which can not be generically solved.<br/>
 * The Login View handles the authentication of the user against the underlying authentication system and
 * creates valid Credentials upon success.<br/><br/>
 * To be implemented with the specific DMS system.
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
public class OwDummyLoginUISubModul extends OwUILoginModul
{
    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // register to receive onRequest notifications (see onRequest) in order to handle optional single sing on
        enableRequestListener();
    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // === enable return key for login
        getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, getEventURL("Login", null), "loginForm", getContext().localize("dummy.ui.label.username.login", "Login"));

        // === TODO: create login form part
        // write the form
        w_p.write("<form id='loginForm' name='loginForm' method='post' action='" + getEventURL("Login", null) + "'>\n");
        renderErrors(w_p);
        w_p.write("\n");
        w_p.write("  <div class='OwLoginModule'>\n");
        w_p.write("\n");
        w_p.write("    <div class=\"OwLoginModuleTitle\">ECM Dummy Adapter</div>\n");
        w_p.write("<div class=\"OwLoginModuleLogin\">");
        w_p.write("    <h2>");
        w_p.write(getContext().localize("dummy.ui.headline", "For Testing and Development Only!"));
        w_p.write("</h2>\n");
        w_p.write("\n");
        w_p.write("    <div class=\"block\">" + getContext().localize("dummy.ui.label.username.Guest", "USE Name: \"Guest\" or leave empty to run all plugins.") + "<br />\n");
        w_p.write(getContext().localize("dummy.ui.label.username.A", "Use name: \"A\" to simulate a search application.") + "<br />\n");
        w_p.write(getContext().localize("dummy.ui.label.username.B", "USE Name: \"B\" to simulate a browse application.") + "<br />\n");
        w_p.write(getContext().localize("dummy.ui.label.username.Admin", "USE Name: \"Admin\" to simulate an Administrator.") + "</div>\n");
        w_p.write("\n");
        w_p.write("    <div class=\"block blockLabel\">");
        w_p.write("<label for='loginNameId'>" + getContext().localize("dummy.ui.label.username.name", "Name:") + "</label>");
        w_p.write("<input id='loginNameId' type=\"text\" name=\"username\"><br />\n");
        w_p.write("\n");
        w_p.write("<label for='loginPasswordId'>" + getContext().localize("dummy.ui.label.username.password", "Password:") + "</label>");
        w_p.write("<input id='loginPasswordId' type='password' name='password'></div>\n");
        w_p.write("\n");
        // write the button for login
        w_p.write("    <div class=\"block\"><input type=\"button\" onclick=\"document.loginForm.submit()\" value=\"");
        w_p.write(getContext().localize("dummy.ui.label.username.login", "Login"));
        w_p.write("\" />");
        // write the button for reset
        w_p.write(" <input type=\"button\" onclick=\"window.location.href = '" + getEventURL("Reset", null) + "';\" onkeydown=\"return onKey(event,13,true,function(event){window.location.href='" + getEventURL("Reset", null) + "';});\" value=\""
                + getContext().localize("dummy.ui.label.username.reset", "Reset") + "\" />\n");
        w_p.write("</div>\n");
        w_p.write("</div>\n");
        w_p.write("  </div>\n");
        w_p.write("\n");
        w_p.write("</form>\n");
        // set focus to NAME element
        getContext().setFocusControlID("username");
    }

    /** event called when user pressed login in the login submodule 
     * @param request_p  {@link HttpServletRequest}
     * @throws Exception 
     */
    public void onLogin(HttpServletRequest request_p) throws Exception
    {
        // === log on
        String strUserName = request_p.getParameter("username");
        String strPassword = request_p.getParameter("password");
        // log user in
        ((OwDummyNetwork) getNetwork()).doLogin(strUserName, strPassword);
    }

    /** event called when user pressed reset in the login submodule 
     * @param request_p  {@link HttpServletRequest}
     * @throws Exception 
     */
    public void onReset(HttpServletRequest request_p) throws Exception
    {
        // === Reset Login info
    }

    /** event called when user pressed change password in the login submodule 
     * @param request_p  {@link HttpServletRequest}
     * @throws Exception 
     */
    public void onChangePassword(HttpServletRequest request_p) throws Exception
    {
        // === Reset Login info
    }

    /** overridable to receive request notifications
     *  to receive onRequest, the target must be registered with OwAppContext.registerRequestTarget
     *
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     */
    public boolean onRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // === single sing on ?
        if (null != request_p.getRemoteUser())
        {
            // log user in
            ((OwDummyNetwork) getNetwork()).doLogin(request_p.getRemoteUser(), "");

            // after we are logged on, we don't need to listen to this request again.
            disableRequestListener();
        }

        return true;
    }
}