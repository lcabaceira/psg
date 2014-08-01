package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Arrays;
import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * JSP processor based workflow launcher.
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
public class OwBPMJspWorkflowLauncher extends OwBPMWorkflowLauncher
{

    /**
     * Constructor
     * @param repository_p
     * @param targetObjects_p
     * @param context_p
     * @param configNode_p
     */
    public OwBPMJspWorkflowLauncher(OwWorkitemRepository repository_p, Collection targetObjects_p, OwMainAppContext context_p, OwXMLUtil configNode_p)
    {
        super(repository_p, targetObjects_p, context_p, configNode_p);
    }

    /**
     * 
     * @param launchableItem_p
     * @param refreshCtx_p
     * @return an {@link OwBPMJspLaunchProcessorDialog}
     * @throws Exception
     */
    protected OwStandardSequenceDialog createLaunchProcessorDialog(OwWorkitem launchableItem_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        return new OwBPMJspLaunchProcessorDialog(Arrays.asList(new Object[] { launchableItem_p }), getConfigNode(), refreshCtx_p);
    }

}
