package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Standard processor based workflow launcher.<br/> 
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
public class OwBPMStandardWorkflowLauncher extends OwBPMWorkflowLauncher
{

    /**
     * Constructor
     * @param repository_p
     * @param targetObjects_p
     * @param context_p
     * @param configNode_p
     */
    public OwBPMStandardWorkflowLauncher(OwWorkitemRepository repository_p, Collection targetObjects_p, OwMainAppContext context_p, OwXMLUtil configNode_p)
    {
        super(repository_p, targetObjects_p, context_p, configNode_p);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param launchableItem_p
     * @param refreshCtx_p
     * @return an {@link OwBPMStandardLaunchProcessorDialog}
     * @throws Exception
     */
    protected OwStandardSequenceDialog createLaunchProcessorDialog(OwWorkitem launchableItem_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        return new OwBPMStandardLaunchProcessorDialog(launchableItem_p, refreshCtx_p, getConfigNode());
    }

}
