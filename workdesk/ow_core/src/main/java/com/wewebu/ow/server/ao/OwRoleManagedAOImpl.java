package com.wewebu.ow.server.ao;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * Simple implementation of OwAOType for role 
 * managed application objects.
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
 *@since 4.0.0.0
 */
public abstract class OwRoleManagedAOImpl<T> extends OwAOTypeImpl<T> implements OwRoleManagedAOType<T>
{
    private static final Logger LOG = OwLogCore.getLogger(OwRoleManagedAOImpl.class);

    private int category;

    public OwRoleManagedAOImpl(OwAOTypesEnum type, Class<T> classType, int category)
    {
        super(type, classType);
        this.category = category;
    }

    public boolean isAllowed(T object, OwRoleManager roleManager)
    {
        try
        {
            return roleManager.isAllowed(category, resourceFromObject(object));
        }
        catch (Exception e)
        {
            LOG.error("Denied access to application object due to runtime error.", e);
            return false;
        }
    }

    protected abstract String resourceFromObject(T object);

}
