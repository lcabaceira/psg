package com.wewebu.ow.server.ecmimpl.owdummy.ui;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyUserInfo;

/**
 *<p>
 * Class that implements a user select module for the dummy adapter.
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
public class OwDummyUIUserSelectModul extends OwUIUserSelectModul
{
    private boolean m_fGroups;
    private boolean m_fUsers;
    private boolean m_fRoles;

    /** construct user select dialog
     * @param strID_p the ID of the currently set user or null if no user is selected
     * @param types_p array of type identifiers as defined in OwUIUserSelectModul
     */
    public OwDummyUIUserSelectModul(String strID_p, int[] types_p)
    {
        for (int i = 0; i < types_p.length; i++)
        {
            if (types_p[i] == OwUIUserSelectModul.TYPE_GROUP)
            {
                m_fGroups = true;
            }
            if (types_p[i] == OwUIUserSelectModul.TYPE_USER)
            {
                m_fUsers = true;
            }
            if (types_p[i] == OwUIUserSelectModul.TYPE_ROLE)
            {
                m_fRoles = true;
            }
        }
    }

    /** render the module */
    public void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div id=\"OwMainContent\" >");
        w_p.write("<h3>Dummy User / Group Select View</h3>");
        if (m_fUsers)
        {
            w_p.write("<a href='" + getEventURL("SelectUser", "name=guest&type=0") + "'>Guest</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Admin&type=0") + "'>Administrator</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=ramu&type=0") + "'>Dennis Pförtsch</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=bese&type=0") + "'>Alexander Haag</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=vahe&type=0") + "'>Valentin Hemmert</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Harald&type=0") + "'>Christian Finzel</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Peter&type=0") + "'>Frank Becker</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Hans&type=0") + "'>Hans Peter</a><br>");
        }

        if (m_fGroups)
        {
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Banken&type=1") + "'>Banks</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Versicherungen&type=1") + "'>Insurance</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Mitarbeiter&type=1") + "'>Employee</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Gast&type=1") + "'>Guest</a><br>");
        }
        if (m_fRoles)
        {
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Banken&type=2") + "'>Banks</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Versicherungen&type=2") + "'>Insurance</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Mitarbeiter&type=2") + "'>Employee</a><br>");
            w_p.write("<a href='" + getEventURL("SelectUser", "name=Gast&type=2") + "'>Guest</a><br>");
        }
        w_p.write("</div>");
    }

    /** called when user selects an item 
     */
    public void onSelectUser(HttpServletRequest request_p) throws Exception
    {
        String strName = request_p.getParameter("name");
        boolean fGroup = request_p.getParameter("type").equals("1") ? true : false;
        boolean fRole = request_p.getParameter("type").equals("2") ? true : false;

        if (getEventListner() != null)
        {
            if (fRole)
            {
                // === role was selected    
                List roles = new ArrayList();
                roles.add(strName);
                getEventListner().onSelectRoles(roles);
            }
            else
            {
                // === group or user was selected
                OwDummyUserInfo user = new OwDummyUserInfo(strName);
                user.setStrLongUserName(strName);
                user.setUserId(strName);
                user.setGroup(fGroup);

                List roles = new ArrayList();
                roles.add("Banken");
                roles.add("Versicherungen");
                roles.add("Gast");
                roles.add("Mitarbeiter");
                user.setRoleNames(roles);

                List users = new ArrayList();
                users.add(user);
                getEventListner().onSelectUsers(users);
            }
        }
    }
}