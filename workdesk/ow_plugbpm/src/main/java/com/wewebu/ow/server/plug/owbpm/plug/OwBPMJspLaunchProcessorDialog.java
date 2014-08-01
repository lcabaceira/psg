package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Standard workflow launch dialog.
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
public class OwBPMJspLaunchProcessorDialog extends OwBPMJspProcessorDialog
{

    public OwBPMJspLaunchProcessorDialog(Collection objects_p, OwXMLUtil configNode_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        super(objects_p, configNode_p, refreshCtx_p);
    }

    /**
     * 
     * @param configNode_p
     * @return always an {@link OwBPMJspLaunchProcessorView}
     * @throws Exception
     */
    protected OwBPMJspProcessorView createJspProcessorView(OwXMLUtil configNode_p) throws Exception
    {
        return new OwBPMJspLaunchProcessorView(this, configNode_p);
    }

}
