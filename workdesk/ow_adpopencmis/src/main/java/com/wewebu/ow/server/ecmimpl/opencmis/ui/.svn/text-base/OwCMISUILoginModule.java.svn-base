package com.wewebu.ow.server.ecmimpl.opencmis.ui;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISAuthenticatedNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Login module for basic authentication.
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
public class OwCMISUILoginModule<N extends OwCMISAuthenticatedNetwork> extends OwUILoginModul<N>
{
    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwCMISUILoginModule.class);

    public static final String INPUT_NAME = "loginName";

    public static final String INPUT_PWD = "loginPwd";

    /** temporary saves the login name */
    private String strUserName;

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        renderLoginForm(w_p);
    }

    public void init(N theNetwork_p) throws OwException
    {
        try
        {
            super.init(theNetwork_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            LOG.fatal("OwCMISUILoginModule.init(): Failed to initialize the CMIS login module", ex);
            throw new OwServerException(new OwString("opencmis.ui.OwCMISUILoginModule.err.init", "Failed to initialize the CMIS login module!"), ex);
        }
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        if (getNetwork().getAuthInterceptor() != null)
        {
            enableRequestListener();
        }
    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void renderLoginForm(Writer w_p) throws Exception
    {
        String submitEvent = getEventURL("Login", null);
        // === enable return key for login
        getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, submitEvent, "loginForm", getContext().localize("opencmis.ui.OwCMISUILoginModule.login", "Login"));

        // write the form
        w_p.write("<form name=\"loginForm\" method=\"post\" action=\"");
        w_p.write(submitEvent);
        w_p.write("\">\n");
        renderErrors(w_p);
        w_p.write("\n  <div class=\"OwLoginModule\">\n");
        w_p.write("    <div class=\"OwLoginModuleTitle\">\n      ");
        w_p.write(getContext().localize("opencmis.ui.OwCMISUILoginModule.title", "CMIS Login"));
        w_p.write("\n    </div>\n    <div class=\"OwLoginModuleLogin\">\n       <div class=\"block blockLabel\">\n          <label for=\"loginNameId\">");
        w_p.write(getContext().localize("opencmis.ui.OwCMISUILoginModule.name", "Name:"));
        w_p.write("</label>\n          <input id=\"loginNameId\" type=\"text\" size=\"30\" name=\"");
        w_p.write(INPUT_NAME);
        w_p.write("\" value=\"");
        if (strUserName != null)
        {
            OwHTMLHelper.writeSecureHTML(w_p, strUserName);
        }
        w_p.write("\" />");
        w_p.write("\n          <label for=\"loginPasswordId\">");
        w_p.write(getContext().localize("opencmis.ui.OwCMISUILoginModule.password", "Password:"));
        w_p.write("</label>\n          <input id=\"loginPasswordId\" type=\"password\" size=\"30\" name=\"");
        w_p.write(INPUT_PWD);
        w_p.write("\" />\n      </div>\n      <div class=\"block\">\n");
        // write the button for login
        w_p.write("          <input type=\"submit\" value=\"");
        w_p.write(getContext().localize("opencmis.ui.OwCMISUILoginModule.login", "Login"));
        w_p.write("\" />\n");
        // write the button for reset
        w_p.write("          <input type=\"button\" onclick=\"window.location.href ='");
        String resetEvent = getEventURL("Reset", null);
        w_p.write(resetEvent);
        w_p.write("';\" onkeydown=\"return onKey(event,13,true,function(event){window.location.href='");
        w_p.write(resetEvent);
        w_p.write("';});\" value=\"");
        w_p.write(getContext().localize("opencmis.ui.OwCMISUILoginModule.reset", "Reset"));
        w_p.write("\" />\n        </div>\n    </div>\n  </div>\n\n");
        w_p.write("</form>\n");

        // set focus to NAME element or if last name was restored to the password element, see Bug 874
        if ((strUserName != null) && (strUserName.length() > 0))
        {// forward to password, name has been restored from last trial
            getContext().setFocusControlID(INPUT_PWD);
        }
        else
        {
            getContext().setFocusControlID(INPUT_NAME);
        }
    }

    /** event called when user pressed login in the login submodule 
     * @param request_p  a {@link HttpServletRequest}
     */
    public void onLogin(HttpServletRequest request_p) throws Exception
    {
        strUserName = request_p.getParameter(INPUT_NAME);
        // log user in
        getNetwork().loginDefault(strUserName, request_p.getParameter(INPUT_PWD));
        disableRequestListener();
        strUserName = null;
    }

    /** event called when user pressed reset in the login submodule 
     * @param request_p a {@link HttpServletRequest}
     */
    public void onReset(HttpServletRequest request_p) throws Exception
    {
        this.strUserName = null;
    }

    @Override
    public boolean onRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        if (getNetwork().getAuthInterceptor().onRequest(request_p, response_p))
        {
            getNetwork().loginDefault("", "");
            disableRequestListener();
            return true;
        }
        else
        {
            return getNetwork().getAuthInterceptor().processRendering(request_p, response_p);
        }
    }
}
