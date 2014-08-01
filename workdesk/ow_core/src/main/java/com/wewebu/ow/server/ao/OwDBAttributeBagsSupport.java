package com.wewebu.ow.server.ao;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwAttributeBagResolver;
import com.wewebu.ow.server.ecm.OwAttributeBagsSupport;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * An application {@link OwAttributeBag} support to be used 
 * with Alfresco Workdesk DB connections.
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
public class OwDBAttributeBagsSupport implements OwAttributeBagsSupport
{
    private static final Logger LOG = OwLogCore.getLogger(OwDBAttributeBagsSupport.class);

    private OwAttributeBagResolver attributeBagResolver;

    /**
     * Returns an {@link OwDBAttributeBagsSupport} instance  if such 
     * an attribute bag support is usable in the given context. Fails with {@link OwNotSupportedException}
     * otherwise.
     * @param networkContext_p
     * @return  an {@link OwDBAttributeBagsSupport} instance  
     * @throws OwNotSupportedException if the DB attribute bags can not be used (egg. no DB connection is defined)
     */
    public static OwDBAttributeBagsSupport createAndCheckDBSupport(OwNetworkContext networkContext_p) throws OwNotSupportedException
    {
        OwAttributeBagResolver.createAndCheckResolver(networkContext_p);
        return new OwDBAttributeBagsSupport();
    }

    /**
     * Constructor
     */
    public OwDBAttributeBagsSupport()
    {
        super();
        this.attributeBagResolver = new OwAttributeBagResolver();
    }

    public OwAttributeBag getNameKeyAttributeBag(OwNetworkContext networkContext_p, String name_p, String userID_p) throws OwException
    {
        try
        {
            return this.attributeBagResolver.getNameKeyAttributeBag(networkContext_p, name_p, userID_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String msg = "Could not resolve name key bage name=" + name_p + ";userID=" + userID_p;
            LOG.error("OwDBAttributeBagsSupport.getNameKeyAttributeBag:" + msg, e);
            throw new OwInvalidOperationException(networkContext_p.localize2("ecmimpl.AttributeBagsSupport.getNameKeyAttributeBag.error", "Could not resolve name key bag name=%1;userID=%2", name_p, userID_p), e);
        }
    }

    public OwAttributeBagWriteable getUserKeyAttributeBagWriteable(OwNetworkContext networkContext_p, String name_p, String userID_p) throws OwException
    {
        try
        {
            return this.attributeBagResolver.getUserKeyAttributeBagWriteable(networkContext_p, name_p, userID_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String msg = "Could not resolve user key bage name=" + name_p + ";userID=" + userID_p;
            LOG.error("OwDBAttributeBagsSupport.getUserKeyAttributeBag:" + msg, e);
            throw new OwInvalidOperationException(networkContext_p.localize2("ecmimpl.AttributeBagsSupport.getUserKeyAttributeBag.error", "Could not resolve user key bag name=%1;userID=%2", name_p, userID_p), e);
        }
    }

    @Override
    public String toString()
    {
        return "OwDBAttributeBagsSupport";
    }

}
