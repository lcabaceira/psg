package com.wewebu.ow.server.ao;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Standard application object managers registry implementation.
 * Does not allow duplicate managers of the same type , allows the replacement of 
 * already registered managers.
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
 *@since 3.2.0.0
 */
public class OwDefaultRegistry implements OwAOManagerRegistry
{
    private static final Logger LOG = OwLogCore.getLogger(OwSearchTemplatesManager.class);

    private Map<Integer, OwAOManager> managers = new HashMap<Integer, OwAOManager>();

    /**
     * 
     * @param manager_p the manager to register 
     * @throws OwInvalidOperationException if another manager is already registered for the 
     *                                     given managers application object type (see {@link OwAOManager#getManagedType()}). 
     */
    public synchronized void registerManager(OwAOManager manager_p) throws OwInvalidOperationException
    {
        int managedType = manager_p.getManagedType();
        if (this.managers.containsKey(managedType))
        {
            String msg = "Mutiple managers are registered for the same application object type " + manager_p.getManagedType();
            LOG.error("OwDefaultRegistry.registerManager():" + msg);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.registry.error", "Invalid application object manager registry operation!"));
        }
        else
        {
            this.managers.put(managedType, manager_p);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDefaultRegistry.registerManager(): manager for AO type " + managedType + " configured as " + manager_p);
            }
        }
    }

    /**
     * Replaces an existing manager with the given manager of the same type (see {@link OwAOManager#getManagedType()}).
     * @param manager_p the new manager 
     * @throws OwInvalidOperationException
     */
    public synchronized void replaceManager(OwAOManager manager_p) throws OwInvalidOperationException
    {
        int managedType = manager_p.getManagedType();

        if (LOG.isDebugEnabled())
        {
            OwAOManager oldManager = this.managers.get(managedType);
            LOG.debug("OwDefaultRegistry.replaceManager(): about to replace manager for AO type " + managedType + " old=" + oldManager + " new=" + manager_p);
        }

        this.managers.put(managedType, manager_p);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwDefaultRegistry.replaceManager(): manager for AO type " + managedType + " configured as " + manager_p);
        }
    }

    /**
     * 
     * @param type_p
     * @return the registered manager for the given object type 
     * @throws OwInvalidOperationException if no manager is registered for the given 
     *                                     application object type 
     */
    public synchronized OwAOManager getManager(int type_p) throws OwInvalidOperationException
    {
        OwAOManager manager = this.managers.get(type_p);
        if (manager == null)
        {
            String msg = "No managers for application objects of type " + type_p + " are registered!";
            LOG.error("OwDefaultRegistry.getManager():" + msg);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.registry.error", "Invalid application object manager registry operation!"));
        }
        return manager;
    }

}
