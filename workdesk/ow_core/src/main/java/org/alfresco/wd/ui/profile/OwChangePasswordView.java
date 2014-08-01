package org.alfresco.wd.ui.profile;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.alfresco.wd.ui.profile.restlet.AlfrescoRestFactory;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwGlobalParametersConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwView;

/**
 * 
 *<p>
 * View displayed for capability to change password.<br />
 * Requires the &quot;EcmBaseUrl&quot; parameter name in bootstrap->globalParameters to work.<br />
 * <p>Note: Default implementation work only with stand-alone Alfresco installation</p>
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
 *@since 4.2.0.0
 */
public class OwChangePasswordView extends OwView
{
    private static final Logger LOG = OwLogCore.getLogger(OwChangePasswordView.class);

    public static final String OLD_PWD = "oldPwd";
    public static final String NEW_PWD = "newPwd";
    public static final String CONFIRM_PWD = "confirmPwd";

    private boolean passwordChanged;

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"OwPane changePwd\">");
        renderPasswordRow(w_p, getContext().localize("profile.OwChangePasswordView.lbl.oldPwd", "Old Password"), OLD_PWD, "even");
        renderPasswordRow(w_p, getContext().localize("profile.OwChangePasswordView.lbl.newPwd", "New Password"), NEW_PWD, "odd");
        renderPasswordRow(w_p, getContext().localize("profile.OwChangePasswordView.lbl.confirmPwd", "Confirm Password"), CONFIRM_PWD, "even");
        w_p.write("</div><div class=\"menu OwInlineMenu\">");

        String url = getEventURL("ChangePwd", null);
        w_p.write("<button type=\"submit\" form=\"");
        w_p.write(getFormName());
        w_p.write("\" formaction=\"");
        w_p.write(url);
        w_p.write("\" />");
        w_p.write(getContext().localize("profile.OwChangePasswordView.btn.submit", "Change Password"));
        w_p.write("</button></div>");
    }

    protected void renderPasswordRow(Writer w, String label, String inputId, String addRowClass) throws IOException
    {
        renderRow(w, "password", label, inputId, addRowClass);
    }

    protected void renderRow(Writer w, String inputType, String label, String inputId, String addRowClass) throws IOException
    {
        w.append("<div class=\"row");
        if (addRowClass != null)
        {
            w.append(" ").append(addRowClass);
        }
        w.append("\"><span class=\"label\">");
        w.append(label);
        w.append("</span><span class=\"value\">");
        w.append("<input type=\"");
        w.append(inputType);
        w.append("\" id=\"");
        w.append(inputId);
        w.append("\" name=\"");
        w.append(inputId);
        w.append("\" class=\"OwInputControl\" />");
        w.append("</span></div>\n");
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";
    }

    public void onChangePwd(HttpServletRequest req) throws Exception
    {
        String old = req.getParameter(OLD_PWD);
        String newPwd = req.getParameter(NEW_PWD);
        String confirm = req.getParameter(CONFIRM_PWD);

        if (newPwd == null || confirm == null || !newPwd.equals(confirm))
        {
            throw new OwInvalidOperationException(getContext().localize("profile.OwChangePasswordView.msg.err.confirm", "New Password and confirmation does not match."));
        }

        this.passwordChanged = changePassword(old, newPwd);

        if (this.passwordChanged)
        {
            OwMainAppContext mainCtx = ((OwMainAppContext) getContext());
            mainCtx.postMessage(mainCtx.localize("profile.OwChangePasswordView.msg.relogin", "Your password was changed successfully. After closing this dialog you need to log in again with your new password. Your current session is now invalid."));
        }

    }

    /** (overridable)
     * Process change Password for with provided information.
     * @param oldPwd String current user password
     * @param newPwd String new user password
     * @return boolean if change password was correct.
     * @throws OwException if change password cannot be proceed
     */
    protected boolean changePassword(String oldPwd, String newPwd) throws OwException
    {
        OwRoleManagerContext roleCtx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
        if (roleCtx.getNetwork().hasInterface(OwRestletAuthenticationHandler.class.getCanonicalName()))
        {
            OwRestletAuthenticationHandler authHandler;
            try
            {
                authHandler = (OwRestletAuthenticationHandler) roleCtx.getNetwork().getInterface(OwRestletAuthenticationHandler.class.getCanonicalName(), null);
            }
            catch (OwException ex)
            {
                throw ex;
            }
            catch (Exception ex)
            {
                throw new OwNotSupportedException(getContext().localize("profile.OwChangePasswordView.err.authHandler", "Cannot change password, unable to get authentication handler."), ex);
            }
            OwGlobalParametersConfiguration util = ((OwMainAppContext) getContext()).getConfiguration().getGlobalParameters();
            String url = util.getSafeString("EcmBaseUrl", null);
            if (url != null)
            {
                AlfrescoRestFactory connection = new AlfrescoRestFactory(url, authHandler);
                connection.changePassword(getUserName(), oldPwd, newPwd);
                return true;
            }

        }
        else
        {
            LOG.warn("OwChangePasswordView.changePassword: Network des not support " + OwRestletAuthenticationHandler.class.getCanonicalName() + " interface.");
            throw new OwInvalidOperationException(getContext().localize("profile.OwChangePasswordView.err.missingAuthHandler", "Missing authentication handler support."));
        }
        return false;
    }

    /**
     * Used in {@link #changePassword(String, String)} method, to send the identification user  name.
     * @return String userName to use for password change
     * @throws OwException
     */
    protected String getUserName() throws OwException
    {
        try
        {
            return getContext().getUserInfo().getUserName();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException(getContext().localize("profile.OwChangePasswordView.err.getUserName", "Unable to retrieve user name."), e);
        }
    }

    /**
     * Return flag if password was changed or not.
     * @return true only if {@link #changePassword(String, String)} returned true
     */
    public boolean isPasswordChanged()
    {
        return this.passwordChanged;
    }
}
