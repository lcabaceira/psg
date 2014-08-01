package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.Collection;
import java.util.Collections;

import org.apache.chemistry.opencmis.commons.data.Ace;

import com.wewebu.ow.server.ecm.OwPrivilege;

/**
 *<p>
 * Simple OwPrivilege implementation.
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
public class OwCMISPrivilege implements OwPrivilege
{
    private Ace ace;
    private String name;

    public OwCMISPrivilege(Ace ace, String name)
    {
        this.ace = ace;
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getDescription()
    {
        return name;
    }

    @Override
    public Collection getChilds(boolean recursive_p)
    {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean hasChilds()
    {
        return false;
    }

    /**
     * Get the Ace for current object.
     * Can be null if there is no Ace associated. 
     * @return Ace or null
     */
    protected Ace getAce()
    {
        return ace;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else
        {
            if (obj == null)
            {
                return false;
            }
            else
            {
                if (obj instanceof OwCMISPrivilege)
                {
                    OwCMISPrivilege other = (OwCMISPrivilege) obj;
                    if (other.getName() != null && getName() != null)
                    {
                        return getName().equals(other.getName());
                    }
                }
                return false;
            }
        }
    }

    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }
}
