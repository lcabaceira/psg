package com.wewebu.ow.server.app.viewer;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.viewer.OwAnnotationInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwInfoProviderRegistry;

/**
 *<p>
 * Simple OwInfoProviderRegistry implementation.
 * A simple implementation to request the network DMS prefix
 * and register the provider into a map.
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
public class OwSimpleInfoProviderRegistry implements OwInfoProviderRegistry
{
    private static final Logger LOG = OwLogCore.getLogger(OwSimpleInfoProviderRegistry.class);

    private OwRoleManagerContext roleCtxt;
    private HashMap<String, OwInfoProvider> registry;

    public OwSimpleInfoProviderRegistry(OwRoleManagerContext roleCtxt_p)
    {
        this.roleCtxt = roleCtxt_p;
        this.registry = new HashMap<String, OwInfoProvider>();
        init();
    }

    protected void init()
    {
        String fqc = OwInfoProvider.class.getCanonicalName();
        OwNetwork network = roleCtxt.getNetwork();

        if (network.hasInterface(fqc))
        {//request provider for Document specific information
            try
            {
                OwInfoProvider prov = (OwInfoProvider) network.getInterface(fqc, this);
                registerProvider(network.getDMSPrefix(), prov);
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Network has not implemented the interface, DMS prefix is = " + network.getDMSPrefix(), e);
                }
            }
        }

        fqc = OwAnnotationInfoProvider.class.getCanonicalName();
        if (network.hasInterface(fqc))
        {//request provider for Annotation specific information
            try
            {
                OwInfoProvider prov = (OwInfoProvider) network.getInterface(fqc, this);
                registerProvider(OwInfoProviderRegistry.PREFIX_ANNOT + network.getDMSPrefix(), prov);
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Network has not implemented the annotation interface, DMS prefix is = " + network.getDMSPrefix(), e);
                }
            }
        }
    }

    public OwInfoProvider getInfoProvider(String context_p)
    {
        return registry.get(context_p);
    }

    public void registerProvider(String context_p, OwInfoProvider pro_p)
    {
        registry.put(context_p, pro_p);
    }

    public OwInfoProvider unregisterProvider(String context_p)
    {
        return registry.remove(context_p);
    }

}