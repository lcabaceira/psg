package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.ecm.OwLocation;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.plug.owaddmultidocuments.cfg.OwSecondaryTypesProcessorCfg;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Secondary types processor.
 * Will create a 
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
 *@since 4.1.1.0
 */
public class OwSecondaryTypesProcessor implements OwObjectClassProcessor
{
    private static final Logger LOG = OwLog.getLogger(OwSecondaryTypesProcessor.class);

    private Map<String, String> mapping;

    @Override
    public OwObjectClass process(OwObjectClass objCls, OwLocation location, OwAppContext ctx)
    {
        OwObjectClass processed = objCls;
        if (getMapping() != null)
        {
            String secTypes = getMapping().get(objCls.getClassName());
            if (secTypes != null)
            {
                String objClass = objCls.getClassName() + "," + secTypes;
                OwRoleManagerContext roleCtx = ctx.getRegisteredInterface(OwRoleManagerContext.class);
                if (roleCtx != null)
                {
                    try
                    {
                        processed = roleCtx.getNetwork().getObjectClass(objClass, location.getResource());
                    }
                    catch (Exception e)
                    {
                        LOG.warn("Could not retrieve specific object class = " + objClass, e);
                    }
                }
            }
        }

        return processed;
    }

    @Override
    public void init(OwXMLUtil configNode) throws OwException
    {
        mapping = OwSecondaryTypesProcessorCfg.getMapping(configNode);
    }

    /**
     * Getter for current mapping, can be null if not initialized.
     * @return Map (ObjectType to SecondaryTypes) or null.
     */
    protected Map<String, String> getMapping()
    {
        return this.mapping;
    }
}
