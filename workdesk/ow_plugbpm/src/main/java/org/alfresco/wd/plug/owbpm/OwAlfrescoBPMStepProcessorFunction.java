package org.alfresco.wd.plug.owbpm;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorDialog;
import com.wewebu.ow.server.plug.owbpm.plug.OwBPMStepProcessorFunction;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Alfresco extension to the Step Processor function to add support for Claim/Release to Pool.
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
 *@since 4.2.0.0
 */
public class OwAlfrescoBPMStepProcessorFunction extends OwBPMStepProcessorFunction
{
    @Override
    protected OwBPMStandardProcessorDialog createProcessorDialog(Collection objects_p, OwClientRefreshContext refreshCtx_p, OwXMLUtil configNode_p) throws Exception
    {
        return new OwAlfrescoBPMStandardProcessorDialog(objects_p, refreshCtx_p, configNode_p);
    }
}
