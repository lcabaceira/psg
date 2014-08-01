package org.alfresco.wd.ui.profile;

import java.io.Writer;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Simple user profile info view. 
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
public class OwUserProfileView extends OwView
{

    @Override
    protected void init() throws Exception
    {
        super.init();
    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("/profile/OwUserProfileView.jsp", w_p);
    }

    public String[][] getData() throws Exception
    {
        OwBaseUserInfo info = getContext().getUserInfo();
        String[][] data = { { getContext().localize("OwUserProfile.lbl.displayName", "Display Name"), info.getUserDisplayName() }, { getContext().localize("OwUserProfile.lbl.email", "eMail"), info.getUserEmailAdress() },
                { getContext().localize("OwUserProfile.lbl.longName", "Long Name"), info.getUserLongName() }, { getContext().localize("OwUserProfile.lbl.shortName", "Short Name"), info.getUserShortName() },
                { getContext().localize("OwUserProfile.lbl.userName", "User Name"), info.getUserName() }, { getContext().localize("OwUserProfile.lbl.userId", "User Id"), info.getUserID() } };

        return data;
    }

}
