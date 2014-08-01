package com.wewebu.ow.server.ecm;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ao.OwAOContext;
import com.wewebu.ow.server.ao.OwConfigurableSupport;
import com.wewebu.ow.server.ao.OwDBAttributeBagsSupport;
import com.wewebu.ow.server.ao.OwSupportConfiguration;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * Attribute bag support with automatic DB connection detection.
 * If DB is supported in the current context it will delegate all 
 * functionality to a {@link OwDBAttributeBagsSupport} instance.
 * If DB is not supported  in the current context will delegate all
 * functionality to a {@link OwTransientBagsSupport} instance.
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
public class OwAutoAttributeBagSupport implements OwAttributeBagsSupport, OwConfigurableSupport
{
    private static Logger LOG = OwLogCore.getLogger(OwAutoAttributeBagSupport.class);

    private OwAttributeBagsSupport delegateSupport;

    @Override
    public OwAttributeBag getNameKeyAttributeBag(OwNetworkContext networkContext_p, String name_p, String userID_p) throws Exception
    {
        return delegateSupport.getNameKeyAttributeBag(networkContext_p, name_p, userID_p);
    }

    @Override
    public OwAttributeBagWriteable getUserKeyAttributeBagWriteable(OwNetworkContext networkContext_p, String name_p, String userID_p) throws Exception
    {
        return delegateSupport.getUserKeyAttributeBagWriteable(networkContext_p, name_p, userID_p);
    }

    @Override
    public void init(OwSupportConfiguration configuration, OwAOContext context) throws OwConfigurationException
    {
        try
        {
            delegateSupport = OwDBAttributeBagsSupport.createAndCheckDBSupport(context.getNetwork().getContext());
        }
        catch (OwNotSupportedException e)
        {
            LOG.warn("OwAutoAttributeBagSupport.init: DB bags are not supported - transient attribute bags will be used!");
            delegateSupport = new OwTransientBagsSupport();
        }

    }

}
