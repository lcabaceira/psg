package com.wewebu.ow.server.ecm;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwString2;
import com.wewebu.ow.server.util.OwTransientBagRepository;

/**
 *<p>
 * An application {@link OwAttributeBag} support to be used 
 * with {@link OwTransientBagRepository}. Should only be used for testing.
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
public class OwTransientBagsSupport implements OwAttributeBagsSupport
{
    private static final Logger LOG = OwLogCore.getLogger(OwTransientBagsSupport.class);

    public OwTransientBagsSupport()
    {
    }

    public OwAttributeBag getNameKeyAttributeBag(OwNetworkContext networkContext_p, String name_p, String userID_p) throws OwException
    {
        OwTransientBagRepository bagsRepository = OwTransientBagRepository.getInstance();
        return bagsRepository.getBag(userID_p, name_p);
    }

    public OwAttributeBagWriteable getUserKeyAttributeBagWriteable(OwNetworkContext networkContext_p, String name_p, String userID_p) throws OwException
    {
        try
        {
            String userID = userID_p;
            if (userID == null)
            {
                OwBaseUserInfo userInfo = networkContext_p.getCurrentUser();
                userID = userInfo.getUserID();
            }

            OwTransientBagRepository bagsRepository = OwTransientBagRepository.getInstance();
            return bagsRepository.getBag(userID, name_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String msg = "Could not retrieve user key attribute bag name=" + name_p + "; userID=" + userID_p;
            LOG.error("OwTransientBagsSupport.getUserKeyAttributeBag:" + msg, e);
            throw new OwInvalidOperationException(new OwString2("ecmimpl.AttributeBagsSupport.getUserKeyAttributeBag.error", "Could not resolve user key bag name=%1;userID=%2", name_p, userID_p), e);
        }
    }

    public String toString()
    {
        return "OwTransientBagsSupport";
    }
}
