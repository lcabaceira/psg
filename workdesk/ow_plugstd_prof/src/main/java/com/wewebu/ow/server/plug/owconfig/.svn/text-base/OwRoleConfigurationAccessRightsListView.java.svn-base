package com.wewebu.ow.server.plug.owconfig;

import java.util.Map;

/**
 *<p>
 * OwRoleConfigurationAccessRightsListView.
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
public class OwRoleConfigurationAccessRightsListView extends OwRoleConfigurationAccessRightsView
{

    /**
     * Group renderer. Use the same group renderer object for all groups.
     */
    private OwResourceInfoGroupRenderer m_groupRenderer;

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("owconfig/OwRoleConfigurationAccessRightsListView.jsp", w_p);
    }

    public Map getAccessMaskDescriptions()
    {
        return m_accessMaskDescriptions;
    }

    /** render the rows of the access rights list
     * @param w_p Writer object to write HTML to
     */
    public void renderRows(java.io.Writer w_p) throws Exception
    {
        // sanity check
        if (getCategory() == null)
        {
            return;
        }
        int numcols = 4 + (getSupportDeny() ? 1 : 0) + m_accessMaskDescriptions.size();
        if (m_groupRenderer == null)
        {
            m_groupRenderer = createGroupRenderer();
        }
        m_groupRenderer.setNumberOfColumns(numcols);
        m_groupRenderer.setGroup(getCurrentGroup());
        m_groupRenderer.render(w_p, getContext(), getCategory(), m_accessMaskDescriptions, isReadOnly(), getSupportDeny(), getPersistAccessMask());
    }

    /**
     * Create group renderer. Factory method, can be used to create different group renderers.
     * @return the newly created group renderer.
     */
    private OwResourceInfoGroupRenderer createGroupRenderer()
    {
        return new OwResourceInfoGroupRenderer();
    }

    /**
     * 
     * @param w_p
     * @throws Exception
     */
    public void renderTableCaption(java.io.Writer w_p) throws Exception
    {
        String title = getContext().localize("app.OwLaunchableWorkflowSelectionView.tableCaption", "Table of launchable workflows");
        w_p.write(title);

    }

}