package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.List;

import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Folder;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5CustomObject;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SoftObject;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * {@link CustomObject} AWD object representation.
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
public class OwFNCM5CustomObjectClass extends OwFNCM5IndependentlyPersistableObjectClass<CustomObject, OwFNCM5ObjectStoreResource>
{
    public OwFNCM5CustomObjectClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_CUSTOM;
    }

    @Override
    public OwFNCM5Object<CustomObject> from(CustomObject nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5CustomObject customObject = factory_p.create(OwFNCM5CustomObject.class, new Class[] { CustomObject.class, OwFNCM5CustomObjectClass.class }, new Object[] { nativeObject_p, this });
        return OwFNCM5SoftObject.asSoftObject(customObject);
    }

    @Override
    protected OwFNCM5VirtualPropertyClass virtualPropertyClass(String className_p) throws OwException
    {
        if (OwResource.m_ObjectPathPropertyClass.getClassName().equals(className_p))
        {
            return new OwFNCM5VirtualPropertyClass(OwResource.m_ObjectPathPropertyClass);
        }
        else
        {
            return super.virtualPropertyClass(className_p);
        }
    }

    @Override
    public List<String> getVirtualPropertiesClassNames() throws OwException
    {
        List<String> names = super.getVirtualPropertiesClassNames();
        names.add(OwResource.m_ObjectPathPropertyClass.getClassName());
        return names;
    }

    @Override
    protected OwFNCM5EngineState<CustomObject> newSelf(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        OwFNCM5EngineState<CustomObject> self = super.newSelf(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);
        if (permissions_p != null)
        {
            self.getEngineObject().set_Permissions(((OwFNCM5Permissions) permissions_p).getNativeObject());
        }
        return self;
    }
}
