package com.wewebu.ow.server.ecmimpl.opencmis.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Helper class for resource cache handling.
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
public class OwCMISResourceCache
{
    private Map<String, OwCMISResource> idResourceCache;
    private Map<String, OwCMISResource> nameResourceCache;
    private Map<String, OwCMISSession> idSessionCache;
    private Map<String, OwCMISSession> nameSessionCache;

    public OwCMISResourceCache()
    {
        idResourceCache = new HashMap<String, OwCMISResource>();
        nameResourceCache = new HashMap<String, OwCMISResource>();
        idSessionCache = new HashMap<String, OwCMISSession>();
        nameSessionCache = new HashMap<String, OwCMISSession>();
    }

    /**
     * Return CMIS resource for given name or id.
     * @param nameOrId_p String representing name or id
     * @return OwCMISResource or null if not found/cached
     */
    public synchronized OwCMISResource getResource(String nameOrId_p)
    {
        OwCMISResource resource = idResourceCache.get(nameOrId_p);
        if (resource != null)
        {
            return resource;
        }
        else
        {
            return nameResourceCache.get(nameOrId_p);
        }
    }

    public synchronized OwCMISSession getSession(String nameOrId_p)
    {
        OwCMISSession session = idSessionCache.get(nameOrId_p);
        if (session != null)
        {
            return session;
        }
        else
        {
            return nameSessionCache.get(nameOrId_p);
        }
    }

    /**
     * Adds a resource to the cache. 
     * @param resource_p OwCMISResource
     * @throws OwException 
     */
    public synchronized void add(OwCMISResource resource_p) throws OwException
    {
        idResourceCache.put(resource_p.getID(), resource_p);
        nameResourceCache.put(resource_p.getName(), resource_p);
        OwCMISSession session = resource_p.createSession();
        idSessionCache.put(resource_p.getID(), session);
        nameSessionCache.put(resource_p.getName(), session);
    }

    /** get a Iterator of available resource IDs
     * 
     * @return Collection of cached resource IDs 
     */
    public synchronized Iterator<String> getResourceIDs()
    {
        Set<String> idSetCopy = new HashSet<String>(idResourceCache.keySet());
        return idSetCopy.iterator();
    }

    public synchronized Collection<OwCMISSession> getSessions()
    {
        return idSessionCache.values();
    }

    /**
     * Clear current cache.
     */
    public synchronized void clear()
    {
        idResourceCache.clear();
        idResourceCache = new HashMap<String, OwCMISResource>();
        nameResourceCache.clear();
        nameResourceCache = new HashMap<String, OwCMISResource>();
        idSessionCache.clear();
        idSessionCache = new HashMap<String, OwCMISSession>();
        nameSessionCache.clear();
        nameSessionCache = new HashMap<String, OwCMISSession>();
    }

}
