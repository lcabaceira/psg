package com.wewebu.ow.server.ecmimpl.fncm5.ui;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * FileNet P8 Content Manager Implementation for login sub module to be created in the network (DMS) Adaptor. <br/>
 * Submodules are used to delegate DMS specific User Interactions to the DMS Adaptor, 
 * which can not be generically solved.<br/>
 * The Login View handles the authentication of the user against the underlying authentication system and
 * creates valid Credentials upon success.<br/>
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
public class OwFNCM5LoginUISubModul extends OwUILoginModul
{
    /** query ID of the name element */
    protected static final String NAME = "username";
    /** query ID of the password element */
    protected static final String PASSWORD = "password";
    /** temporary saves the login name */
    private String strUserName;

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNCM5LoginUISubModul.class);

    public OwFNCM5LoginUISubModul(OwFNCM5Network network) throws Exception
    {
        init(network);
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();
        // register to receive onRequest notifications (see onRequest) in order to handle optional single sing on
        enableRequestListener();
    }

    /** overridable to receive request notifications
     *  to receive onRequest, the target must be registered with OwAppContext.registerRequestTarget
     *
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     */
    public boolean onRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // check if we have a principal and if so, try to login using JAAS
        if (request_p.getUserPrincipal() != null)
        {
            try
            {
                ((OwFNCM5Network) getNetwork()).loginJAAS();
                // OK. we got called at least once. disable the request listener.
                disableRequestListener();
            }
            catch (Exception e)
            {
                // log the exception
                LOG.warn("Can not perform JAAS login although we have a principal", e);
                // do not stop. continue and display the default login dialog.
            }
        }
        // continue with page rendering
        return true;
    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        renderLoginForm(w_p);
    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void renderLoginForm(Writer w_p) throws Exception
    {
        // === enable return key for login
        getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, getEventURL("Login", null), "loginForm", getContext().localize("fncm.ui.OwFNCMLoginUISubModul.login", "Login"));

        // === TODO: create login form part
        // write the form
        w_p.write("<form name=\"loginForm\" method=\"post\" action=\"");
        w_p.write(getEventURL("Login", null));
        w_p.write("\">\n");
        renderErrors(w_p);
        w_p.write("\n  <div class=\"OwLoginModule\">\n\n    <div class=\"OwLoginModuleTitle\">\n\n");
        w_p.write(getContext().localize("OwFNCM5LoginUISubModul.title", "File Net P8 5 Content Manager"));
        w_p.write("\n\n    </div>\n    <div class=\"OwLoginModuleLogin\">\n     <div class=\"block blockLabel\">\n\n");
        w_p.write("          <label for=\"loginNameId\">");
        w_p.write(getContext().localize("OwFNCM5LoginUISubModul.name", "Name:"));
        w_p.write("</label>\n          <input id=\"loginNameId\" type=\"text\" size=\"30\" name=\"");
        w_p.write(NAME);
        w_p.write(strUserName == null ? "\" value=\"\"  />" : "\" value=\"" + strUserName + "\" /><br />\n");
        w_p.write("\n         <label for=\"loginPasswordId\">");
        w_p.write(getContext().localize("OwFNCM5LoginUISubModul.password", "Password:"));
        w_p.write("</label>\n         <input id=\"loginPasswordId\" type=\"password\" size=\"30\" name=\"");
        w_p.write(PASSWORD);
        w_p.write("\">\n        </div>\n\n        <div class=\"block\">\n");
        // write the button for login
        w_p.write("          <input id=\"btnLogin\" type=\"button\" onclick=\"document.loginForm.submit()\" value=\"");
        w_p.write(getContext().localize("OwFNCM5LoginUISubModul.btn.login", "Login"));
        w_p.write("\" />\n\n");
        // write the button for reset
        w_p.write("          <input id=\"btnReset\" type=\"button\" onclick=\"window.location.href='");
        w_p.write(getEventURL("Reset", null));
        w_p.write("';\" onkeydown=\"onKey(event,13,true,function(event){window.location.href='");
        w_p.write(getEventURL("Reset", null));
        w_p.write("';});\" value=\"");
        w_p.write(getContext().localize("OwFNCM5LoginUISubModul.btn.reset", "Reset"));
        w_p.write("\" />\n\n");
        w_p.write("        </div>\n      </div>\n  </div>\n</form>\n");

        // set focus to NAME element or if last name was restored to the password element, see Bug 874
        if ((strUserName != null) && (strUserName.length() > 0))
        {
            getContext().setFocusControlID(PASSWORD); // forward to password, name has been restored from last trial
        }
        else
        {
            getContext().setFocusControlID(NAME);
        }
    }

    /** event called when user pressed login in the login submodule 
     * @param request_p  a {@link HttpServletRequest}
     */
    public void onLogin(HttpServletRequest request_p) throws Exception
    {
        strUserName = request_p.getParameter(NAME);
        String strPassword = request_p.getParameter(PASSWORD);
        // log user in
        getNetwork().loginDefault(strUserName, strPassword);
        disableRequestListener();
    }

    /** event called when user pressed reset in the login submodule 
     * @param request_p a {@link HttpServletRequest}
     */
    public void onReset(HttpServletRequest request_p) throws Exception
    {
        // === Reset Login info
    }

}