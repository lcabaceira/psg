package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Search template defined ObjectStore data.
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
 *@since 3.0.0.0
 */
public class OwSearchObjectStore
{
    private String id;
    private String name;

    public OwSearchObjectStore(String id_p, String name_p)
    {
        super();
        this.id = id_p;
        this.name = name_p;
    }

    /**
     * Unifies the state of this ObjectStore and the given ObjectStore parameter and 
     * returns their unified state as a new ObjectStore.
     * The state unification is done by replacing null id or name values from either ObjectStores
     * with non null values found in any of the two ObjectStores.
     * If non null values are set and they don't match an {@link OwInvalidOperationException} is thrown.
     * @param objectStore_p
     * @throws OwInvalidOperationException if non null id or name values of the two ObjectStores are not equal
     */
    public OwSearchObjectStore unify(OwSearchObjectStore objectStore_p) throws OwInvalidOperationException
    {
        String unifiedId = null;
        if (this.id != null && objectStore_p.id != null)
        {
            if (!objectStore_p.id.equals(this.id))
            {
                throw new OwInvalidOperationException("ID missmatch!");
            }
            else
            {
                unifiedId = this.id;
            }
        }
        else
        {
            if (this.id == null)
            {
                unifiedId = objectStore_p.id;
            }
            else
            {
                unifiedId = this.id;
            }
        }

        String unifiedName = null;
        if (this.name != null && objectStore_p.name != null)
        {
            if (!objectStore_p.name.equals(this.name))
            {
                throw new OwInvalidOperationException("NAME missmatch!");
            }
            else
            {
                unifiedName = this.name;
            }
        }
        else
        {
            if (this.name == null)
            {
                unifiedName = objectStore_p.name;
            }
            else
            {
                unifiedName = this.name;
            }
        }

        return new OwSearchObjectStore(unifiedId, unifiedName);
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean equals(Object obj_p)
    {

        if (obj_p instanceof OwSearchObjectStore)
        {
            OwSearchObjectStore osObject = (OwSearchObjectStore) obj_p;
            if (this.id != null)
            {
                if (!this.id.equals(osObject.id))
                {
                    return false;
                }
            }
            else
            {
                if (null != osObject.id)
                {
                    return false;
                }
            }

            if (this.name != null)
            {
                return this.name.equals(osObject.name);

            }
            else
            {
                return null == osObject.name;
            }
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        if (this.id != null)
        {
            return this.id.hashCode();
        }
        else if (this.name != null)
        {
            return this.name.hashCode();
        }
        else
        {
            return 0;
        }
    }

    public String toString()
    {
        return "[Object Store(id=" + this.id + ";name=" + this.name + ")]";
    }
}