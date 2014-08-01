package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.dmsdialogs.views.OwLaunchableWorkflowSelectionView;
import com.wewebu.ow.server.dmsdialogs.views.OwLaunchableWorkflowSelectionView.OwWorkflowSelectionListener;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;

/**
 *<p>
 * A dialog that enables the selection of workflow descriptions.
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
public class OwBPMLaunchableWorkflowSelectionDialog extends OwStandardDialog implements OwWorkflowSelectionListener
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMLaunchableWorkflowSelectionDialog.class);

    /** the workflows view */
    protected OwLaunchableWorkflowSelectionView m_workflowsView;

    private Collection m_workflowDescriptions = new ArrayList();

    private OwWorkflowDescription m_selection = null;

    /**
     * Constructor
     * @param descriptions_p a collection of {@link OwWorkflowDescription}s
     */
    public OwBPMLaunchableWorkflowSelectionDialog(Collection descriptions_p)
    {
        super();
        this.m_workflowDescriptions = descriptions_p;
    }

    protected void init() throws Exception
    {
        super.init();

        // redundant, document is attached by the core already
        //getDocument().attach(getContext(), null);

        setTitle(getContext().localize("owbpm.OwBPMLaunchableWorkflowSelectionDialog.title", "Select a workflow"));

        m_workflowsView = new OwLaunchableWorkflowSelectionView(m_workflowDescriptions);
        m_workflowsView.addSelectionListener(this);
        addView(m_workflowsView, MAIN_REGION, null);

    }

    /** Called when the user wants to cancel the workflow selection process 
     *  
     *  @return always true 
     */
    public boolean onCancel(HttpServletRequest request_p) throws Exception
    {
        closeDialog();
        return true;
    }

    public void renderRegion(Writer w_p, int region_p) throws Exception
    {
        super.renderRegion(w_p, region_p);
        switch (region_p)
        {
            case MAIN_REGION:
                String eventURL = getEventURL("Cancel", null);
                String cancelLabel = getContext().localize("owbpm.OwBPMLaunchableWorkflowSelectionDialog.cancelbtn", "Cancel");
                String cancelButton = "<input type=\"button\" value=\"" + cancelLabel + "\" name=\"Cancel\" onclick=\"document.location='" + eventURL + "'\"/>";
                w_p.write("<div style=\"margin-left:10px;padding-bottom:10px;margin-top:15px;clear:left\">");
                w_p.write(cancelButton);
                w_p.write("</div>");
                break;
            default:
                //do noting
        }
    }

    public void detach()
    {
        super.detach();

        // redundant, document is attached by the core already
        //getDocument().detach();
    }

    public void descriptionSelected(OwWorkflowDescription description_p)
    {
        try
        {
            this.m_selection = description_p;
            closeDialog();
        }
        catch (Exception e)
        {
            LOG.error("OwBPMLaunchableWorkflowSelectionDialog.descriptionSelected : Could not close the dialog!", e);
        }
    }

    /**
     * 
     * @return the selected workflow description or <code>null</code> if none is selected
     */
    public final OwWorkflowDescription getSelection()
    {
        return m_selection;
    }
}
