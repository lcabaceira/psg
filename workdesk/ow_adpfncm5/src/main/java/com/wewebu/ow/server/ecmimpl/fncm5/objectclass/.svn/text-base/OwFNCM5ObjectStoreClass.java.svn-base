package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import org.apache.log4j.Logger;

import com.filenet.api.core.ObjectStore;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectStore;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwFNCM5ObjectStoreClass.
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
public class OwFNCM5ObjectStoreClass extends OwFNCM5IndependentObjectClass<ObjectStore, OwFNCM5ObjectStoreResource>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5ObjectStoreClass.class);

    private Integer index = null;
    private OwFNCM5ObjectStore instance;
    private ObjectStore objectStore;

    public OwFNCM5ObjectStoreClass(ObjectStore objectStore_p, int osIndex_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(OwFNCM5ClassDescription.from(objectStore_p, resourceAccessor_p), resourceAccessor_p);
        this.objectStore = objectStore_p;
    }

    @Override
    public synchronized OwFNCM5ObjectStore from(ObjectStore nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        if (this.objectStore.equals(nativeObject_p))
        {
            if (this.instance == null)
            {
                this.instance = factory_p.create(OwFNCM5ObjectStore.class, new Class[] { ObjectStore.class, OwFNCM5ObjectStoreClass.class }, new Object[] { nativeObject_p, this });
                //                this.instance = new OwFNCM5ObjectStore(nativeObject_p, this);
            }
            return this.instance;
        }
        else
        {
            throw new OwInvalidOperationException("ObjectStore object-classes can have exactly 1 instance !");
        }
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER;
    }

    @Override
    public String getClassName()
    {
        if (this.index != null)
        {
            return super.getClassName() + "_" + this.index.toString();
        }
        else
        {
            return super.getClassName();
        }
    }

}
