package org.alfresco.wd.plug.owbpm;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorDialog;
import com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Extension to add Claim/Release to pool buttons.
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
public class OwAlfrescoBPMStandardProcessorDialog extends OwBPMStandardProcessorDialog
{

    /**
     * @param objects_p
     * @param refreshCtx_p
     * @param configNode_p
     * @throws Exception
     */
    public OwAlfrescoBPMStandardProcessorDialog(Collection objects_p, OwClientRefreshContext refreshCtx_p, OwXMLUtil configNode_p) throws Exception
    {
        super(objects_p, refreshCtx_p, configNode_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMStandardProcessorDialog#createProcessorView(com.wewebu.ow.server.util.OwXMLUtil)
     */
    @Override
    protected OwBPMStandardProcessorView createProcessorView(OwXMLUtil configNode_p) throws Exception
    {
        return new OwAlfrescoBPMStandardProcessorView(this, configNode_p);
    }
}
