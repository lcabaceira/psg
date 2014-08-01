package com.wewebu.ow.server.ecmimpl.opencmis.ui.acl;

import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Standard CMIS user selection dialog.
 * Returning only the users from RepositoryInfo object,
 * which is for CMIS V1.0 without LDAP the anonymous and anyone user.
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
public class OwCMISRestrictedUserSelectDialog extends OwUserSelectDialog
{
    protected static final String USER_ID = "userId";
    private RepositoryInfo repositoryInfo;

    public OwCMISRestrictedUserSelectDialog(RepositoryInfo repoType)
    {
        super(null, false);
        repositoryInfo = repoType;
    }

    @Override
    public void init() throws Exception
    {
        //super.init();
        setExternalFormTarget(this);
        setTitle(getContext().localize("opencmis.ui.acl.OwCMISRestrictedUserSelectDialog.title", "CMIS User Selection"));
    }

    @Override
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        if (iRegion_p == MAIN_REGION)
        {
            w_p.write("<div id=\"OwMainContent\" class=\"OwUIUserSelectModul OwCMISUserSelectModul\">\n");
            renderButton(w_p);
            w_p.write("</div>");
        }
        else
        {
            super.renderRegion(w_p, iRegion_p);
        }
    }

    protected void renderButton(Writer w_p) throws Exception
    {
        if (repositoryInfo.getPrincipalIdAnyone() != null)
        {
            w_p.write("<div><input type=\"radio\" name=\"");
            w_p.write(USER_ID);
            w_p.write("\" value=\"");
            String name = OwHTMLHelper.encodeToSecureHTML(repositoryInfo.getPrincipalIdAnyone());
            w_p.write(name);
            w_p.write("\">&nbsp;");
            w_p.write(name);
            w_p.write("</input></div><br>");
        }

        if (repositoryInfo.getPrincipalIdAnonymous() != null)
        {
            w_p.write("<div><input type=\"radio\" name=\"");
            w_p.write(USER_ID);
            w_p.write("\" value=\"");
            String name = OwHTMLHelper.encodeToSecureHTML(repositoryInfo.getPrincipalIdAnonymous());
            w_p.write(name);
            w_p.write("\">&nbsp;");
            w_p.write(name);
            w_p.write("</input></div><br>");
        }

        w_p.write("<div><input type=\"button\" value=\"");
        w_p.write(OwHTMLHelper.encodeToSecureHTML(getContext().localize("opencmis.acl.OwCMISSimpleUserSelectDialog.btn.select", "Select")));
        w_p.write("\" onclick=\"");
        w_p.write(getFormEventURL("Select", null));
        w_p.write("\" /></div>");
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";
    }

    public void onSelect(HttpServletRequest request_p) throws Exception
    {
        String user = request_p.getParameter(USER_ID);
        if (user != null)
        {
            List<OwUserInfo> lst = new LinkedList<OwUserInfo>();
            lst.add(new OwCMISUserInfo(user, null));
            onSelectUsers(lst);
        }
    }

}
