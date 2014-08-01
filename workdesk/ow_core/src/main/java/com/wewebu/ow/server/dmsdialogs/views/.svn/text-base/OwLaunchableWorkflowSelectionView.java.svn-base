package com.wewebu.ow.server.dmsdialogs.views;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * A view that renders a list of selectable workflow descriptions.
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
 *@since 2.5.2.0
 */
public class OwLaunchableWorkflowSelectionView extends OwView
{
    private static final String ROW_INDEX_PARAMETER = "dRowIndex";

    private List m_workflowDescriptions = new ArrayList();

    private List m_selectionListeners = new ArrayList();

    /**
     *<p>
     * Selection listener interface.
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
     *@see #addSelectionListener
     */
    public static interface OwWorkflowSelectionListener
    {
        void descriptionSelected(OwWorkflowDescription description_p);
    }

    /**
     * Constructor
     * @param workflowDescriptions_p a collection of {@link OwWorkflowDescription}s
     */
    public OwLaunchableWorkflowSelectionView(Collection workflowDescriptions_p)
    {
        if (workflowDescriptions_p != null)
        {
            this.m_workflowDescriptions.addAll(workflowDescriptions_p);
        }
    }

    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("dmsdialogs/OwLaunchableWorkflowSelectionView.jsp", w_p);
    }

    /**
     * 
     * @return the viewed descriptions 
     */
    public final List getWorkflowDescriptions()
    {
        return m_workflowDescriptions;
    }

    /**
     * 
     * @param rowIndex_p
     * @return an URL to be used as anchor target for row click events 
     */
    public String createRowLinkURL(int rowIndex_p)
    {
        return getEventURL("RowClick", ROW_INDEX_PARAMETER + "=" + rowIndex_p);
    }

    /**
     * Return the name of the workflow description,
     * where all HTML/XML tags are escaped.
     * @param i_p int index of workflow description
     * @return String representing the name of Workflow
     */
    public String getWorkflowDescriptionName(int i_p)
    {
        OwWorkflowDescription desc = (OwWorkflowDescription) m_workflowDescriptions.get(i_p);
        String localizedName = ((OwMainAppContext) getContext()).localizeLabel(desc.getName());

        try
        {
            return OwHTMLHelper.encodeToSecureHTML(localizedName);
        }
        catch (IOException ex)
        {
            return desc.getName();
        }
    }

    /**
     * Return the amount of descriptions. 
     * @return int amount (quantity) of available descriptions
     */
    public int getWorkflowDescriptionCount()
    {
        return m_workflowDescriptions.size();
    }

    /**
     * Return if there are available workflow descriptions.
     * @return boolean true only if amount of descriptions.
     */
    public boolean hasWorkflowDescrptions()
    {
        return m_workflowDescriptions.size() > 0;
    }

    /**
     * 
     * @param request_p
     * @throws Exception
     */
    public void onRowClick(HttpServletRequest request_p) throws Exception
    {
        String rowIndexParameter = request_p.getParameter(ROW_INDEX_PARAMETER);
        if (rowIndexParameter != null)
        {
            int rowIndex = Integer.parseInt(rowIndexParameter);

            OwWorkflowDescription description = (OwWorkflowDescription) m_workflowDescriptions.get(rowIndex);
            fireDescriptionSelected(description);
        }
    }

    /**
     * Registers a selection listener with this view.
     * @param listener_p
     */
    public synchronized void addSelectionListener(OwWorkflowSelectionListener listener_p)
    {
        m_selectionListeners.add(listener_p);
    }

    /**
     * Unregisters a selection listener from this view.
     * @param listener_p
     */
    public synchronized void removeSelectionListener(OwWorkflowSelectionListener listener_p)
    {
        m_selectionListeners.remove(listener_p);
    }

    /**
     * Fires the {@link OwWorkflowSelectionListener#descriptionSelected(OwWorkflowDescription)} 
     * of all registered listeners for a given description.
     * @param description_p
     */
    protected synchronized void fireDescriptionSelected(OwWorkflowDescription description_p)
    {
        for (Iterator i = m_selectionListeners.iterator(); i.hasNext();)
        {
            OwWorkflowSelectionListener listener = (OwWorkflowSelectionListener) i.next();
            listener.descriptionSelected(description_p);
        }
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
