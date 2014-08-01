package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Collection;
import java.util.LinkedList;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base class for workflow launchers.<br/>
 * The workflow launching scenario (workflow choosing and launch step processor dialog displaying) is
 * encapsulated in this class in order to be reused under different use cases (like launch document function
 * and new workflow extension). 
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
public abstract class OwBPMWorkflowLauncher
{
    private Collection m_targetObjects;
    private OwMainAppContext m_context;
    private OwWorkitemRepository m_repository;
    private OwXMLUtil m_configNode;

    /**
     * Constructor
     * @param repository_p
     * @param targetObjects_p
     * @param context_p
     * @param configNode_p
     */
    public OwBPMWorkflowLauncher(OwWorkitemRepository repository_p, Collection targetObjects_p, OwMainAppContext context_p, OwXMLUtil configNode_p)
    {
        this.m_targetObjects = targetObjects_p;
        this.m_context = context_p;
        this.m_repository = repository_p;
        this.m_configNode = configNode_p;
    }

    /**
     * Starts the launching scenario. 
     * @param refreshCtx_p
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public void launch(final OwClientRefreshContext refreshCtx_p) throws Exception
    {
        Collection workflowDescriptions = m_repository.getLaunchableWorkflowDescriptions(m_targetObjects);
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
                    dispatchLaunchStepProcessor(selection, refreshCtx_p);
                }
            }

        });
    }

    /** (overridable) 
     *  Creates a custom launch item processing dialog
     *  
     * @param launchableItem_p
     * @param refreshCtx_p
     * @return an {@link OwStandardSequenceDialog} to process the newly created launch item
     * @throws Exception
     */
    protected abstract OwStandardSequenceDialog createLaunchProcessorDialog(OwWorkitem launchableItem_p, OwClientRefreshContext refreshCtx_p) throws Exception;

    /**
     * Creates the launch item based on the given description and dispatches the custom processing dialog.
     * 
     * @param selectedDescription_p
     * @param refreshCtx_p
     * @throws Exception
     */
    protected void dispatchLaunchStepProcessor(OwWorkflowDescription selectedDescription_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwRoleManager roleManager = m_context.getNetwork().getRoleManager();
        if (!roleManager.isAllowed(OwRoleManager.ROLE_CATEGORY_BPM_PROCESS_DEFINITION, selectedDescription_p.getId()))
        {
            OwString1 message = new OwString1("plug.owbpm.err.startWorkflow.accessDenied", "The current user does not have permission to start workflow '%1'.", selectedDescription_p.getName());
            throw new OwAccessDeniedException(message);
        }
        OwWorkitem launchWorkItem = m_repository.createLaunchableItem(selectedDescription_p, m_targetObjects);
        OwStandardSequenceDialog processorDialog = createLaunchProcessorDialog(launchWorkItem, refreshCtx_p);

        processorDialog.setTitle(m_context.localize("owbpm.OwBPMWorkflowLauncher.launchdialogtitle", "Launch:") + selectedDescription_p.getName());
        m_context.openDialog(processorDialog, null);

    }

    protected OwXMLUtil getConfigNode()
    {
        return m_configNode;
    }
}
