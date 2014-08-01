package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * Settings Property for boot configuration refresh button.<br/>
 * <b>NOTE: Property does not edit data, it just displays a button</b>
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
public class OwSettingsPropertyRefreshBootConfig extends OwSettingsPropertyBaseImpl
{

    /** overridable to insert a single value into a edit HTML form
     *
     * @param w_p Writer to write HTML code to
     * @param value_p the property value to edit
     * @param strID_p String the ID of the HTML element for use in onApply
    * @param iIndex_p int Index of item if it is a list
    */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        // === we use this settings property just for update buttons

        w_p.write("<div class=\"OwSettingsPropertyRefreshBootConfig\">");
        // role config update button
        if (((OwMainAppContext) getContext()).getRoleManager().canRefreshStaticConfiguration())
        {
            w_p.write("<div class=\"OwBlock\">");
            w_p.write("<strong>");

            w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.roleconfig.title", "Plugin and MIME Configuration"));

            w_p.write("</strong> <br />");

            w_p.write("<input type=\"button\" value=\"");
            w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.roleconfig.update", "Update"));
            w_p.write("\" onclick=\"navigateHREF(window,'");
            w_p.write(getEventURL("RoleConfigClick", null));
            w_p.write("');\">");

            w_p.write("<span class=\"OwInstructionName\">");

            w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.roleconfig.description",
                    "After editing Plugin and MIME configurations (owplugins.xml and owmimetable.xml), they must be updated in Workdesk. Reload the changes here. The changes will then be available to all users with the next log-in."));

            w_p.write("</span>");
            w_p.write("</div>");
        }
        else
        {
            w_p.write("<span class=\"OwPropertyName\">" + getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.roleconfig.notenabled", "Plugin and MIME configuration cannot be updated.") + "</span>");
        }

        // boot config update button
        w_p.write("<div class=\"OwBlock\">");
        w_p.write("<strong>");
        w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.bootconfig.title", "Boot Configuration"));
        w_p.write("</strong> <br />");

        w_p.write("<input type=\"button\" value=\"");
        w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.bootconfig.update", "Update"));
        w_p.write("\" onclick=\"navigateHREF(window,'");
        w_p.write(getEventURL("BootConfigClick", null));
        w_p.write("');\">");

        w_p.write("<span class=\"OwInstructionName\">");

        w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.bootconfig.description",
                "After changing the boot configuration (owbootstrap.xml), it must be updated in Workdesk. Reload the changes here. They are then available to all users after the next log-in."));

        w_p.write("</span>");
        w_p.write("</div>");
        w_p.write("</div>");
    }

    /** called, when user clicked the role config update button
     * @param request_p HttpServletRequest 
     */
    public void onRoleConfigClick(HttpServletRequest request_p) throws Exception
    {
        // === refresh configuration
        ((OwMainAppContext) getContext()).getRoleManager().refreshStaticConfiguration();
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.roleconfig.ok", "Plugin and MIME configuration have been updated and are available after the next log-in."));
    }

    /** called, when user clicked the boot config update button
     * @param request_p HttpServletRequest 
     */
    public void onBootConfigClick(HttpServletRequest request_p) throws Exception
    {
        // === refresh configuration
        ((OwMainAppContext) getContext()).getConfiguration().refreshStaticConfiguration();
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("settingsimpl.OwSettingsPropertyRefreshBootConfig.bootconfig.ok", "Boot configuration has been updated and is available after the next log-in."));
    }

    @Override
    public void insertLabel(Writer w_p) throws Exception
    {
        w_p.write(getDisplayName() + ":");
    }
}
