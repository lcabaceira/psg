package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.filenet.api.core.ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DefaultObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ResourceAccessor;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ThreadAccessor;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5DefaultPropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;

/**
 *<p>
 * Single instance object model. 
 * Instances are reusable across networks.   
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
public class OwFNCM5SingletonObjectModel extends OwFNCM5CachedObjectModel
{

    private static final Logger LOG = OwLog.getLogger(OwFNCM5SingletonObjectModel.class);

    private static final Map<String, OwFNCM5SingletonObjectModel> instances = new HashMap<String, OwFNCM5SingletonObjectModel>();

    public synchronized static OwFNCM5SingletonObjectModel modelFor(String objectStoreId_p)
    {
        OwFNCM5SingletonObjectModel instance = instances.get(objectStoreId_p);
        if (instance == null)
        {
            instance = new OwFNCM5SingletonObjectModel(objectStoreId_p);
            instances.put(objectStoreId_p, instance);
        }

        return instance;
    }

    private OwFNCM5Credentials singletonCredentials;
    private ObjectStore objectStore;
    private String objectStoreId;

    private OwFNCM5SingletonObjectModel(String objectStoreId_p)
    {
        this(new OwFNCM5ThreadAccessor<OwFNCM5ObjectStoreResource>(), objectStoreId_p);
    }

    public OwFNCM5SingletonObjectModel(OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p, String objectStoreId_p)
    {
        this(resourceAccessor_p, new OwFNCM5DefaultPropertyClassFactory(resourceAccessor_p), objectStoreId_p);
    }

    public OwFNCM5SingletonObjectModel(OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p, OwFNCM5PropertyClassFactory propertyClassFactory_p, String objectStoreId_p)
    {
        this(resourceAccessor_p, objectStoreId_p, propertyClassFactory_p, new OwFNCM5DefaultObjectClassFactory(propertyClassFactory_p, resourceAccessor_p));
    }

    private OwFNCM5SingletonObjectModel(OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p, String objectStoreId_p, OwFNCM5PropertyClassFactory propertyClassFactory_p, OwFNCM5ObjectClassFactory objectClassFactory_p)
    {
        super(resourceAccessor_p, propertyClassFactory_p, objectClassFactory_p);
        this.objectStoreId = objectStoreId_p;
    }

    private OwFNCM5Network getLocalNetwork()
    {
        return OwFNCM5Network.localNetwork();
    }

}
