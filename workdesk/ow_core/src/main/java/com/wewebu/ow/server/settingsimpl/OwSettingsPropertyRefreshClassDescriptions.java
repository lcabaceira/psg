package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * Settings Property for Class Descriptions Refresh Button.<br/>
 * <b>NOTE</b>: Property does not edit data, it just displays a button.
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
public class OwSettingsPropertyRefreshClassDescriptions extends OwSettingsPropertyBaseImpl
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
        w_p.write("<div class=\"OwSettingsPropertyRefreshClassDescriptions\">");
        // we use this settings property just for a button
        if (((OwMainAppContext) getContext()).getNetwork().canRefreshStaticClassdescriptions())
        {
            w_p.write("<div class=\"OwBlock\">");
            w_p.write("<strong>");

            w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshClassDescriptions.title", "DMS Class Description"));

            w_p.write("</strong><br />");

            w_p.write("<input type=\"button\" value=\"");
            w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshClassDescriptions.update", "Update"));
            w_p.write("\" onclick=\"navigateHREF(window,'");
            w_p.write(getEventURL("Click", null));
            w_p.write("');\">");

            w_p.write("<span class=\"OwInstructionName\">");

            w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyRefreshClassDescriptions.description",
                    "New or changed document classes in the DMS systems are available in Workdesk after an update. Reload the changes here. They are then available to all users after the next log-in."));

            w_p.write("</span></div>");
        }
        else
        {
            w_p.write("<span class='OwPropertyName'>" + getContext().localize("settingsimpl.OwSettingsPropertyRefreshClassDescriptions.notenabled", "You do not have to update the class descriptions for the adapter.") + "</span>");
        }
        w_p.write("</div>");
    }

    /** called, when user clicked the button
     * @param request_p HttpServletRequest 
     */
    public void onClick(HttpServletRequest request_p) throws Exception
    {
        // === refresh class descriptions
        ((OwMainAppContext) getContext()).getNetwork().refreshStaticClassdescriptions();
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("settingsimpl.OwSettingsPropertyRefreshClassDescriptions.ok", "DMS class description has been updated and is available after the next log-in."));
    }

    @Override
    public void insertLabel(Writer w_p) throws Exception
    {
        w_p.write(getDisplayName() + ":");
    }
}