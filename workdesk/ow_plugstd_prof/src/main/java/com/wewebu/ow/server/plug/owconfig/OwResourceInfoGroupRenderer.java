package com.wewebu.ow.server.plug.owconfig;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwCategoryInfo;
import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwResourceInfo;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Render the content of a OwResourceInfoGroup.
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
public class OwResourceInfoGroupRenderer
{
    public static final String ALLOW = "allow";
    public static final String IMPLDENY = "impldeny";
    public static final String DENY = "deny";

    /**
     * the group to be rendered.
     */
    private OwResourceInfoGroup m_group;
    /**
     * number of columns.
     */
    private int m_numberOfColumns;

    /**
     * Constructor
     */
    public OwResourceInfoGroupRenderer()
    {
    }

    /**
     * set the group to be rendered
     * @param group_p
     */
    public void setGroup(OwResourceInfoGroup group_p)
    {
        this.m_group = group_p;
    }

    /**
     * set number of columns for this group
     * @param numberOfColumns_p
     */
    public void setNumberOfColumns(int numberOfColumns_p)
    {
        this.m_numberOfColumns = numberOfColumns_p;
    }

    /**
     * render the group.
     * @param w_p
     * @param context_p
     * @param category_p
     * @param maskDescriptions_p
     * @param readOnly_p
     * @param supportDenny_p
     * @param persistAccessMask_p
     * @throws Exception
     */
    public void render(Writer w_p, OwAppContext context_p, OwCategoryInfo category_p, Map maskDescriptions_p, boolean readOnly_p, boolean supportDenny_p, boolean persistAccessMask_p) throws Exception
    {
        if (m_group == null)
        {
            w_p.write("Null group!");
            return;
        }
        Iterator resourceInfoIterator = m_group.getResources().iterator();
        if (canRenderMultipleGroups())
        {
            writeGroupHeader(w_p, context_p);
        }
        int currentRowIndex = 0;
        while (resourceInfoIterator.hasNext())
        {
            OwResourceInfo resInfo_p = (OwResourceInfo) resourceInfoIterator.next();
            renderResourceInfo(w_p, context_p, category_p, maskDescriptions_p, readOnly_p, supportDenny_p, persistAccessMask_p, currentRowIndex, resInfo_p);
            currentRowIndex++;
        }
    }

    /**
     * render a resource info object
     * @param w_p
     * @param context_p
     * @param category_p
     * @param maskDescriptions_p
     * @param readOnly_p
     * @param supportDenny_p
     * @param persistAccessMask_p
     * @param currentRowIndex_p
     * @param resInfo_p
     * @throws Exception
     * @throws IOException
     */
    private void renderResourceInfo(Writer w_p, OwAppContext context_p, OwCategoryInfo category_p, Map maskDescriptions_p, boolean readOnly_p, boolean supportDenny_p, boolean persistAccessMask_p, int currentRowIndex_p, OwResourceInfo resInfo_p)
            throws Exception, IOException
    {
        // detect current group name

        String resourceDisplayName = resInfo_p.getDisplayName();
        String resourceId = resInfo_p.getId();

        boolean isSelectiveConfiguration = category_p.getId() == OwRoleManager.ROLE_CATEGORY_SELECTIVE_CONFIGURATION;

        if (isSelectiveConfiguration)
        {
            resourceDisplayName = resourceDisplayName.substring(0, resourceDisplayName.lastIndexOf('.'));
            resourceId = resourceId.substring(resourceId.lastIndexOf('.') + 1, resourceId.length());
        }

        if ((resourceDisplayName != null) && (resourceDisplayName.length() > 1) && (resourceDisplayName.charAt(0) == '@'))
        {
            String temp = resourceDisplayName.substring(1);
            int endmarkerpos = temp.indexOf('@');
            if (endmarkerpos >= 0)
            {
                resourceDisplayName = temp.substring(endmarkerpos + 1);
            }
        }
        int accessRights = resInfo_p.getAccessRights();
        String rowid = (m_group.getGroupId() < 0) ? Integer.toString(currentRowIndex_p) : Integer.toString(m_group.getGroupId()) + "_" + Integer.toString(currentRowIndex_p);
        w_p.write("<tr id='" + rowid + "' ");
        w_p.write(((currentRowIndex_p % 2) == 0) ? "class='OwGeneralList_RowEven' " : "class='OwGeneralList_RowOdd' ");
        if (canRenderMultipleGroups())
        {
            w_p.write((m_group.getGroupId() >= 0) ? "style='display:none;'" : "style=''");
        }
        w_p.write(">");
        // allow
        w_p.write("<td align='center'>");
        w_p.write("<img style='width:9px;height:9px;padding-right:5px;' src='");
        OwHTMLHelper.writeSecureHTML(w_p, context_p.getDesignURL());
        String tooltip = context_p.localize("owconfig.OwResourceInfoGroupRenderer.userrightallow", "User right allowed.");
        if (accessRights == OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED)
        {
            w_p.write("/images/plug/owconfig/checked.png'");
        }
        else
        {
            w_p.write("/images/plug/owconfig/unchecked.png'");
            tooltip = context_p.localize("owconfig.OwResourceInfoGroupRenderer.userrightdeny", "User right denied");
        }
        w_p.write(" alt='");
        w_p.write(tooltip);
        w_p.write("' title='");
        w_p.write(tooltip);
        w_p.write("'/>");

        if (!readOnly_p)
        {
            w_p.write("<input type='radio' name='allowdeny_");
            w_p.write(Integer.toString(currentRowIndex_p));
            w_p.write("' title='");
            w_p.write(context_p.localize1("app.OwResourceInfoGroupRenderer.checkbox.allow.title", "Allow %1", resourceDisplayName));
            w_p.write("' value='" + ALLOW + "'");
            if (accessRights == OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED)
            {
                w_p.write(" checked");
            }
            w_p.write(">");
        }
        w_p.write("</td>");
        // deny
        if (supportDenny_p)
        {
            renderDeny(w_p, context_p, DENY, readOnly_p, currentRowIndex_p, resourceDisplayName, accessRights, OwRoleManager.ROLE_ACCESS_RIGHT_DENIED);
            renderDeny(w_p, context_p, IMPLDENY, readOnly_p, currentRowIndex_p, resourceDisplayName, accessRights, OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        }
        // name
        w_p.write("<td align='left'>");

        OwHTMLHelper.writeSecureHTML(w_p, resourceDisplayName);
        w_p.write("</td>");
        // ID 
        w_p.write("<td align='left'>");
        OwHTMLHelper.writeSecureHTML(w_p, resourceId);
        w_p.write("</td>");
        // category
        w_p.write("<td align='left'>");
        OwHTMLHelper.writeSecureHTML(w_p, category_p.getDisplayName());
        w_p.write("</td>");
        // access mask flags
        int accessMask = resInfo_p.getAccessMask();
        Iterator itAccessMaskFlags = maskDescriptions_p.entrySet().iterator();
        while (itAccessMaskFlags.hasNext())
        {
            Entry accessMaskEntry = (Entry) itAccessMaskFlags.next();
            int accessMaskFlag = ((Integer) accessMaskEntry.getKey()).intValue();
            boolean hasFlag = (accessMask & accessMaskFlag) == accessMaskFlag;
            w_p.write("<td align='center'>");
            w_p.write("<img style='width:9px;height:9px;padding-right:5px;' src='");
            OwHTMLHelper.writeSecureHTML(w_p, context_p.getDesignURL());
            tooltip = context_p.localize("owconfig.OwResourceInfoGroupRenderer.userrightallow", "User right allowed");
            if (hasFlag)
            {
                w_p.write("/images/plug/owconfig/checked.png'");
            }
            else
            {
                tooltip = context_p.localize("owconfig.OwResourceInfoGroupRenderer.userrightdeny", "User right denied");
                w_p.write("/images/plug/owconfig/unchecked.png'");
            }
            w_p.write(" alt='");
            w_p.write(tooltip);
            w_p.write("' title='");
            w_p.write(tooltip);
            w_p.write("'/>");

            if (persistAccessMask_p && (!readOnly_p))
            {

                String accessMaskFlagID = "accessMaskFlag_" + Integer.toString(accessMaskFlag) + "_" + Integer.toString(currentRowIndex_p);
                String maskTitle = context_p.localize("owconfig.OwResourceInfoGroupRenderer.allowDenyUserRight", "Allow/Deny user right");
                w_p.write("<input type='checkbox' name='");
                w_p.write(accessMaskFlagID);
                w_p.write("' title='");
                w_p.write(maskTitle);
                w_p.write("' value='set'");
                if (hasFlag)
                {
                    w_p.write(" checked");
                }
                w_p.write(">");
            }
            w_p.write("</td>");
        }
        // line end
        w_p.write("</tr>");
        w_p.flush();
    }

    private void renderDeny(Writer w_p, OwAppContext context_p, String value_p, boolean readOnly_p, int currentRowIndex_p, String resourceDisplayName_P, int accessRights_p, int deniedAccesRightType_p) throws IOException, Exception
    {
        String tooltip;
        w_p.write("<td align='center'>");
        w_p.write("<img style='width:9px;height:9px;padding-right:5px;' src='");
        OwHTMLHelper.writeSecureHTML(w_p, context_p.getDesignURL());

        if (accessRights_p == deniedAccesRightType_p)
        {
            w_p.write("/images/plug/owconfig/checked.png'");
            tooltip = context_p.localize("owconfig.OwResourceInfoGroupRenderer.userrightdeny", "User right denied");

        }
        else
        {
            w_p.write("/images/plug/owconfig/unchecked.png'");
            tooltip = context_p.localize("owconfig.OwResourceInfoGroupRenderer.userrightallow", "User right allowed.");
        }
        w_p.write(" alt='");
        w_p.write(tooltip);
        w_p.write("' title='");
        w_p.write(tooltip);
        w_p.write("'/>");

        if (!readOnly_p)
        {
            w_p.write("<input type='radio' name='allowdeny_");
            w_p.write(Integer.toString(currentRowIndex_p));
            w_p.write("' title='");
            w_p.write(context_p.localize1("app.OwResourceInfoGroupRenderer.checkbox.deny.title", "Deny %1", resourceDisplayName_P));
            w_p.write("' value='" + value_p + "'");
            if (accessRights_p == deniedAccesRightType_p)
            {
                w_p.write(" checked");
            }
            w_p.write(">");
        }
        w_p.write("</td>");
    }

    /**
     * render group header
     * @param w_p
     * @param context_p
     * @throws Exception
     */
    private void writeGroupHeader(java.io.Writer w_p, OwAppContext context_p) throws Exception
    {
        if (!m_group.isSurrogate())
        {
            String tooltipPlus = context_p.localize("image.plus", "Expand");
            String tooltipMinus = context_p.localize("image.minus", "Collapse");

            w_p.write("<tr id=\"control_" + m_group.getGroupId() + "\" class='OwGeneralList_RowEven'>");
            w_p.write("<td colspan=" + Integer.toString(m_numberOfColumns) + "><a href=\"javascript:toggleAccessRightsLines('" + m_group.getGroupId() + "');\">");
            w_p.write("<img style='width:9px;height:9px;padding-right:5px;' id='imgplus_");
            w_p.write(Integer.toString(m_group.getGroupId()));
            w_p.write("' src='");
            OwHTMLHelper.writeSecureHTML(w_p, context_p.getDesignURL());
            w_p.write("/images/plug/owconfig/plus.png' alt='" + tooltipPlus + "' title='" + tooltipPlus + "'/>");
            w_p.write("<img style='width:9px;height:9px;padding-right:5px;display:none;' id='imgminus_");
            w_p.write(Integer.toString(m_group.getGroupId()));
            w_p.write("' src='");
            OwHTMLHelper.writeSecureHTML(w_p, context_p.getDesignURL());
            w_p.write("/images/plug/owconfig/minus.png'" + " alt='" + tooltipMinus + "' title='" + tooltipMinus + "'/>");
            w_p.write(m_group.getGroupDisplayName());
            w_p.write("</a></td>");
            w_p.write("</tr>");
        }
    }

    /**
     * Check if this rendered have the ability to render multiple groups.
     * @return <code>true</code> if this renderer can handle multiple groups.
     */
    public boolean canRenderMultipleGroups()
    {
        return false;
    }
}