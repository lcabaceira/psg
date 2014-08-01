package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.commons.data.Ace;

/**
 *<p>
 * Simple implementation of ACL-Differential helper object.
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
public class OwCMISAclDiffImpl implements OwCMISAclDiff
{
    private List<Ace> added;
    private List<Ace> deleted;

    public OwCMISAclDiffImpl()
    {
        this(null, null);
    }

    public OwCMISAclDiffImpl(List<Ace> added, List<Ace> deleted)
    {
        this.added = added;
        this.deleted = deleted;
    }

    @Override
    public List<Ace> getAdded()
    {
        return added;
    }

    /**
     * Set a list of added ACE objects, will overwrite existing definitions. 
     * @param addedAce List<Ace> or null
     */
    public void setAdded(List<Ace> addedAce)
    {
        this.added = addedAce;
    }

    @Override
    public List<Ace> getDeleted()
    {
        return deleted;
    }

    public void setDeleted(List<Ace> deletedAce)
    {
        this.deleted = deletedAce;
    }

    @Override
    public boolean add(Ace newAce)
    {
        boolean wasAdd = false;
        if (newAce != null)
        {
            if (getAdded() == null)
            {
                added = new LinkedList<Ace>();
                wasAdd = added.add(newAce);
            }
            else
            {
                boolean exists = false;
                for (Ace ace : added)
                {
                    if (ace.getPrincipalId().equals(newAce.getPrincipalId()))
                    {
                        exists = true;
                        for (String newPerm : newAce.getPermissions())
                        {
                            if (!ace.getPermissions().contains(newPerm))
                            {
                                ace.getPermissions().add(newPerm);
                                wasAdd = true;
                            }
                        }
                    }
                }
                if (!exists)
                {
                    added.add(newAce);
                    wasAdd = true;
                }
            }
        }
        return wasAdd;
    }

    @Override
    public boolean remove(Ace removeAce)
    {
        boolean remove = false;
        if (removeAce != null)
        {
            boolean fromAdded = false;
            if (getAdded() != null)
            {
                fromAdded = getAdded().remove(removeAce);
            }
            if (!fromAdded)
            {
                if (deleted == null)
                {
                    deleted = new LinkedList<Ace>();
                    deleted.add(removeAce);
                    remove = true;
                }
                else
                {
                    deleted.add(removeAce);
                    remove = true;
                }
            }
        }
        return remove;
    }
}