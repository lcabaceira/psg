package org.alfresco.wd.ui.profile;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwGlobalParametersConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwSmallSubNavigationView;

/**
 *<p>
 * Dialog to show User Profile.
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
public class OwProfileDialog extends OwStandardDialog
{
    public static final String ALLOW_CHANGE_PASSWORD = "AllowChangePassword";

    private OwChangePasswordView changePwdView;

    @Override
    protected void init() throws Exception
    {
        super.init();
        OwSmallSubNavigationView navView = new OwSmallSubNavigationView(true);
        addView(navView, OwStandardDialog.MAIN_REGION, null);

        OwUserProfileView profileInfo = createUserProfileView();
        navView.addView(profileInfo, getContext().localize("profile.OwProfileDialog.tab.info", "Overview"), null, null, null, getContext().localize("profile.OwProfileDialog.tab.info.tooltip", "Show User Information"));
        navView.enable(0, true);
        OwMainAppContext ctx = (OwMainAppContext) getContext();
        OwGlobalParametersConfiguration globalParameters = ctx.getConfiguration().getGlobalParameters();
        if (globalParameters.getSafeBoolean(ALLOW_CHANGE_PASSWORD, false))
        {
            changePwdView = createChangePasswordView();
            navView.addView(changePwdView, getContext().localize("profile.OwProfileDialog.tab.changePwd", "Change Password"), null, null, null, getContext().localize("profile.OwProfileDialog.tab.changePwd.tooltip", "Change User Password"));
            navView.enable(1, true);
        }

        navView.navigate(0);
    }

    @Override
    public String getTitle()
    {
        return getContext().localize("profile.OwProfileDialog.title", "My Profile");
    }

    protected OwUserProfileView createUserProfileView()
    {
        return new OwUserProfileView();
    }

    protected OwChangePasswordView createChangePasswordView()
    {
        return new OwChangePasswordView();
    }

    @Override
    public void onClose(HttpServletRequest request_p) throws Exception
    {
        OwAppContext ctx = getContext();
        super.onClose(request_p);
        if (isPasswordChanged())
        {
            ctx.logout();
        }
    }

    /**
     * Method to check if a password change was processed. 
     * @return boolean
     */
    protected boolean isPasswordChanged()
    {
        return this.changePwdView != null && this.changePwdView.isPasswordChanged();
    }
}
