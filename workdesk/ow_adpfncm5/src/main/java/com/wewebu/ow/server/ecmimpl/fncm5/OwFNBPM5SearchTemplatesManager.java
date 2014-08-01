package com.wewebu.ow.server.ecmimpl.fncm5;

import com.wewebu.ow.server.ao.OwAOContext;
import com.wewebu.ow.server.ao.OwAOSupport;
import com.wewebu.ow.server.ao.OwAOType;
import com.wewebu.ow.server.ao.OwSearchTemplateFactory;
import com.wewebu.ow.server.ao.OwSearchTemplatesManager;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;

/**
 *<p>
 * File Net BPM search template manager.
 * Uses {@link OwFNBPM5SearchTemplateFactory} to produce search template application objects. 
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
public class OwFNBPM5SearchTemplatesManager extends OwSearchTemplatesManager
{

    public OwFNBPM5SearchTemplatesManager()
    {

    }

    public OwFNBPM5SearchTemplatesManager(OwNetworkContext context, OwAOSupport aoSupport_p, String basePath_p, String singlePath_p, String multiplePath_p)
    {
        super(aoSupport_p, basePath_p, createFactory(context), singlePath_p, multiplePath_p);
    }

    private static OwFNBPM5SearchTemplateFactory createFactory(OwNetworkContext context)
    {
        return new OwFNBPM5SearchTemplateFactory(context);
    }

    @Override
    protected OwSearchTemplateFactory initSearchTemplateFactory(OwAOContext context)
    {
        return createFactory(context.getNetwork().getContext());
    }

    @Override
    public int getManagedType()
    {
        return OwAOTypesEnum.SEARCHTEMPLATE_BPM.type;
    }

    @Override
    public OwAOType<?> getType()
    {
        return OwAOConstants.AO_SEARCHTEMPLATE_BPM;
    }
}
