package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Domain;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5DomainResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DomainClass;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5MutableAccessor;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ObjectStoreClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * {@link Domain} AWD representation.
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
public class OwFNCM5Domain extends OwFNCM5EngineObject<Domain, OwFNCM5DomainResource>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Domain.class);

    private List<OwFNCM5ObjectStore> objectStores;
    private Map<String, OwFNCM5ObjectStore> resourcesById;
    private Map<String, OwFNCM5ObjectStore> resourcesBySymbolicName;
    private Map<String, OwFNCM5ObjectStore> resourcesByDisplayName;

    public OwFNCM5Domain(Domain domain_p, OwFNCM5DomainClass domainObjectClass_p)
    {
        super(domain_p, domainObjectClass_p);
        clearOSCache();
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            if (iObjectTypes_p[i] == OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS || iObjectTypes_p[i] == OwObjectReference.OBJECT_TYPE_FOLDER)
            {
                return true;
            }

        }
        return false;
    }

    @Override
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwStandardObjectCollection children = new OwStandardObjectCollection();
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                    children.addAll(getObjectStores());
                    break;
                default:
                    ; //DO NOTHING
            }
        }
        return children;
    }

    public String getMIMEType()
    {
        return "ow_root/filenet_cm";
    }

    public String getName()
    {
        OwFNCM5EngineState<Domain> myself = getSelf();
        Domain domain = myself.getEngineObject();
        return domain.get_Name();
    }

    public synchronized List<OwFNCM5ObjectStore> getObjectStores() throws OwException
    {
        if (this.objectStores == null)
        {
            this.objectStores = new LinkedList<OwFNCM5ObjectStore>();

            Domain domain = getNativeObject();
            ObjectStoreSet objectStoreSet = domain.get_ObjectStores();

            Iterator<?> osIt = objectStoreSet.iterator();
            while (osIt.hasNext())
            {
                ObjectStore objectStore = (ObjectStore) osIt.next();
                try
                {
                    OwFNCM5ObjectStore osObject = getObjectStore(objectStore.get_Id().toString());
                    osObject.getNativeObject().get_TopFolders();//test access to objectstore

                    this.objectStores.add(osObject);
                }
                catch (EngineRuntimeException erex)
                {
                    if (ExceptionCode.E_ACCESS_DENIED.equals(erex.getExceptionCode()))
                    {
                        //ignore OS cannot be browsed in root directory
                        LOG.info("ObjectStore (" + objectStore.get_SymbolicName() + ") will be filtered out since no acces for Topfolders exist");
                    }
                    else
                    {
                        throw erex;
                    }

                }
            }
        }

        return this.objectStores;
    }

    public OwFNCM5Version<?> getVersion() throws OwException
    {
        return null;
    }

    public boolean hasVersionSeries() throws OwException
    {
        return false;
    }

    public OwVersionSeries getVersionSeries() throws OwException
    {
        return null;
    }

    public String getID()
    {
        OwFNCM5EngineState<Domain> myself = getSelf();
        Domain domain = myself.getEngineObject();
        Id id = domain.get_Id();
        return id.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (getNativeObject() != null ? 0 : getNativeObject().get_Name().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        OwFNCM5Domain other = (OwFNCM5Domain) obj;
        if (objectStores == null)
        {
            if (other.objectStores != null)
            {
                return false;
            }
        }
        else
        {
            if (!objectStores.equals(other.objectStores))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieve an object store as OwObject representation, will compare the parameter with symbolic name and id
     * of available native object stores.<br />
     * Can throw an OwObjectNotFoundException if no object store could be found for provided parameter value.
     * @param nameOrId String
     * @return OwFNCM5ObjectStore or null if not found
     * @throws OwException if could not wrap native object store into OwObject interface
     * @since 3.2.0.3
     */
    public synchronized OwFNCM5ObjectStore getObjectStore(String nameOrId) throws OwException
    {
        OwFNCM5ObjectStore os = getOSFromCache(nameOrId);
        if (os == null)
        {
            Domain domain = getNativeObject();
            ObjectStoreSet objectStoreSet = domain.get_ObjectStores();
            Iterator osIt = objectStoreSet.iterator();
            int osIndex = 0;
            while (osIt.hasNext())
            {
                osIndex++;
                ObjectStore objectStore = (ObjectStore) osIt.next();
                if (nameOrId.equals(objectStore.get_SymbolicName()) || nameOrId.equals(objectStore.get_Id().toString()))
                {
                    os = createObjectStore(objectStore, osIndex);
                    cacheOS(os);
                    break;
                }
            }
        }
        return os;
    }

    /**
     * Get ObjectStore representation from local cache for provided id.
     * @param nameOrId String symbolic name or Id
     * @return OwFNCM5ObjectStore or null if not in cache
     * @since 3.2.0.3
     */
    protected synchronized OwFNCM5ObjectStore getOSFromCache(String nameOrId)
    {
        OwFNCM5ObjectStore os = resourcesById.get(nameOrId);
        if (os == null)
        {
            os = resourcesBySymbolicName.get(nameOrId);
        }
        if (os == null)
        {
            os = resourcesByDisplayName.get(nameOrId);
        }
        return os;
    }

    /**
     * Put provided ObjectStore into local cache. 
     * @param store OwFNCM5ObjectStore
     * @since 3.2.0.3
     */
    protected synchronized void cacheOS(OwFNCM5ObjectStore store)
    {
        resourcesById.put(store.getID(), store);
        resourcesBySymbolicName.put(store.getSymbolicName(), store);
        resourcesByDisplayName.put(store.getName(), store);
    }

    /**
     * Clear local ObjectStore cache.
     */
    public synchronized void clearOSCache()
    {
        if (resourcesById != null)
        {
            resourcesById.clear();
        }

        if (resourcesBySymbolicName != null)
        {
            resourcesBySymbolicName.clear();
        }

        resourcesById = new HashMap<String, OwFNCM5ObjectStore>();
        resourcesBySymbolicName = new HashMap<String, OwFNCM5ObjectStore>();
        resourcesByDisplayName = new HashMap<String, OwFNCM5ObjectStore>();
    }

    /**
     * Create object store from specific native instance.
     * @param objectStore ObjectStore native object
     * @param osIndex int index of object store
     * @return OwFNCM5ObjectStore
     * @throws OwException
     * @since 3.2.0.3
     */
    protected OwFNCM5ObjectStore createObjectStore(ObjectStore objectStore, int osIndex) throws OwException
    {
        OwFNCM5MutableAccessor<OwFNCM5ObjectStoreResource> resourceAccessor = new OwFNCM5MutableAccessor<OwFNCM5ObjectStoreResource>();

        OwFNCM5ObjectStoreClass osObjectClass = new OwFNCM5ObjectStoreClass(objectStore, osIndex, resourceAccessor);
        OwFNCM5ObjectStore osObject = osObjectClass.from(objectStore, OwFNCM5DefaultObjectFactory.INSTANCE);
        OwFNCM5ObjectStoreResource osResource = new OwFNCM5ObjectStoreResource(osObject, getNetwork());
        resourceAccessor.set(osResource);

        return osObject;
    }

    /**
     * Get an iterator which provides the possible available ObjectStore-Id's.
     * @return Iterator of String which represents resource id's
     * @since 3.2.0.3
     */
    public Iterator<String> getObjectStoreIds()
    {
        Domain domain = getNativeObject();
        ObjectStoreSet objectStoreSet = domain.get_ObjectStores();

        return new OsIdIterator(objectStoreSet);
    }

    /**
     *<p>
     * Helper for iteration over object store id's handling.
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
     *@since 3.2.0.3
     */
    private static class OsIdIterator implements Iterator<String>
    {
        Iterator<Object> osIterator;

        @SuppressWarnings("unchecked")
        public OsIdIterator(ObjectStoreSet osSet)
        {
            osIterator = osSet.iterator();
        }

        public boolean hasNext()
        {
            return osIterator.hasNext();
        }

        public String next()
        {
            ObjectStore objectStore = (ObjectStore) osIterator.next();
            return objectStore.get_Id().toString();
        }

        public void remove()
        {
            osIterator.remove();
        }

    }

}
