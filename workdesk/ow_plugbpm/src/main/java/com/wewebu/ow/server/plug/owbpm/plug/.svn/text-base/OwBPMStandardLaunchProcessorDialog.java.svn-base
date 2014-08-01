package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Arrays;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Standard workflow launch dialog.<br/>
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
public class OwBPMStandardLaunchProcessorDialog extends OwBPMStandardProcessorDialog
{

    public OwBPMStandardLaunchProcessorDialog(OwWorkitem object_p, OwClientRefreshContext refreshCtx_p, OwXMLUtil configNode_p) throws Exception
    {
        super(Arrays.asList(new OwWorkitem[] { object_p }), refreshCtx_p, configNode_p);
    }

    /**
     * 
     * @param configNode_p
     * @return always an {@link OwBPMStandardLaunchProcessorView}
     * @throws Exception
     */
    protected OwBPMStandardProcessorView createProcessorView(OwXMLUtil configNode_p) throws Exception
    {
        return new OwBPMStandardLaunchProcessorView(this, configNode_p);
    }
}
