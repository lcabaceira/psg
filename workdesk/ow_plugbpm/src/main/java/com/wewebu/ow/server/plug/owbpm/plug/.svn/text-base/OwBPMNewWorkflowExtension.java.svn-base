package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwAbstractToolExtension;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Extension of the new-plugin that launches context free workflows.<br/>
 * The launched workflow is processed using the {@link OwBPMJspLaunchProcessorDialog} (if a JspForm is configured)
 * or using the {@link OwBPMJspLaunchProcessorDialog} as 
 * processor dialog.
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
 *@since 3.1.0.0
 */
public class OwBPMNewWorkflowExtension extends OwAbstractToolExtension
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMNewWorkflowExtension.class);
    /** the repository interface*/
    private static final String BPM_WORKITEM_REPOSITORY_INTERFACE = "com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository";
    /** default big icon path*/
    protected static final String BIGICON_DESIGN_PATH = "/images/plug/owbpm/gear_new_24.png";
    /** default icon path*/
    protected static final String ICON_DESIGN_PATH = "/images/plug/owbpm/gear_new.png";

    /** 
     * Get the default big icon path for this Extension.
     * The default big icon path is used if no big icon path is provided in the configuration.
     * @return a {@link String} representing the default big icon path.
     */
    protected String getDefaultBigIcon()
    {
        return BIGICON_DESIGN_PATH;
    }

    /** 
     * Get the default icon path for this Extension.
     * The default icon path is used if no icon path is provided in the configuration.
     * @return a {@link String} representing the default icon path.
     */
    protected String getDefaultIcon()
    {
        return ICON_DESIGN_PATH;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwToolExtension#onClickEvent()
     */
    @SuppressWarnings("rawtypes")
    public void onClickEvent() throws Exception
    {
        final OwWorkitemRepository bpmRepository = (OwWorkitemRepository) m_context.getNetwork().getInterface(BPM_WORKITEM_REPOSITORY_INTERFACE, null);

        if (!bpmRepository.canLaunch())
        {
            String msg = "OwBPMNewWorkflowExtension.onClickEvent: The configured work item repository does not support the launching workflows.This extension will be disabled!";
            LOG.warn(msg);
            return;
        }

        Collection workflowDescriptions = bpmRepository.getLaunchableWorkflowDescriptions(null);
        Collection<OwWorkflowDescription> allowedWorkflowDescriptions = new LinkedList<OwWorkflowDescription>();
        for (Object object : workflowDescriptions)
        {
            OwWorkflowDescription description = (OwWorkflowDescription) object;
            if (this.m_context.isAllowed(OwRoleManager.ROLE_CATEGORY_BPM_PROCESS_DEFINITION, description.getId()))
            {
                allowedWorkflowDescriptions.add(description);
            }
        }
        final OwBPMLaunchableWorkflowSelectionDialog workflowSelectionDialog = new OwBPMLaunchableWorkflowSelectionDialog(allowedWorkflowDescriptions);

        m_context.openDialog(workflowSelectionDialog, new OwDialogListener() {
            /*
             * (non-Javadoc)
             * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
             */
            public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
            {
                // ignore, used for update events from documents
            }

            public void onDialogClose(OwDialog dialogView_p) throws Exception
            {
                OwWorkflowDescription selection = workflowSelectionDialog.getSelection();
                if (selection != null)
                {
                    OwRoleManager roleManager = m_context.getNetwork().getRoleManager();
                    if (!roleManager.isAllowed(OwRoleManager.ROLE_CATEGORY_BPM_PROCESS_DEFINITION, selection.getId()))
                    {
                        OwString1 message = new OwString1("plug.owbpm.err.startWorkflow.accessDenied", "The current user does not have permission to start workflow '%1'.", selection.getName());
                        throw new OwAccessDeniedException(message);
                    }
                    OwWorkitem workItem = bpmRepository.createLaunchableItem(selection, null);

                    OwStandardSequenceDialog standardProcessorDialog = createProcessorDialog(workItem);

                    standardProcessorDialog.setTitle(m_context.localize("owbpm.OwBPMLaunchProcessorFunction.launchdialogtitle", "Launch:") + selection.getName());
                    m_context.openDialog(standardProcessorDialog, null);
                }
            }

        });
    }

    /**
     * Creates the process dialog to launch the workflow.
     * @param workItem_p - the {@link OwWorkitem} object
     * @return the dialog for handling the launch workflow operation.
     * @throws Exception
     */
    protected OwStandardSequenceDialog createProcessorDialog(OwWorkitem workItem_p) throws Exception
    {
        OwStandardSequenceDialog result = null;
        if (m_jspForm == null)
        {
            result = new OwBPMStandardLaunchProcessorDialog(workItem_p, null, m_confignode);
        }
        else
        {
            result = new OwBPMJspLaunchProcessorDialog(Arrays.asList(new Object[] { workItem_p }), m_confignode, null);
        }
        return result;
    }

}
