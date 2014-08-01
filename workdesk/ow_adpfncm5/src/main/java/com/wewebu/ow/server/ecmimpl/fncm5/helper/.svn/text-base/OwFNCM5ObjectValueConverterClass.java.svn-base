package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import com.filenet.api.core.EngineObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.converter.OwFNCM5EngineListFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ResourceAccessor;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwFNCM5ObjectValueConverterClass.
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
public class OwFNCM5ObjectValueConverterClass extends OwFNCM5MetaConverterClass<EngineObject, OwObject>
{
    private OwFNCM5ResourceAccessor<? extends OwFNCM5Resource> resourceAccessor;

    public OwFNCM5ObjectValueConverterClass(OwFNCM5ResourceAccessor<? extends OwFNCM5Resource> resourceAccessor_p)
    {
        super(OwObject.class, OwFNCM5EngineListFactory.EngineCollection);
        this.resourceAccessor = resourceAccessor_p;

    }

    public EngineObject toNativeValue(OwObject owdValue_p) throws OwException
    {
        if (owdValue_p != null)
        {
            try
            {
                Object nativeObject = owdValue_p.getNativeObject();
                return (EngineObject) nativeObject;
            }
            catch (ClassCastException e)
            {
                throw new OwInvalidOperationException("Invalid value class. Expected native EngineObject but found " + owdValue_p.getClass().getName(), e);
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not convert OwObject value.", e);
            }
        }
        else
        {
            return null;
        }

    }

    public OwObject convertNativeValue(EngineObject nativeValue_p) throws OwException
    {
        if (nativeValue_p != null)
        {
            OwFNCM5Resource resource = resourceAccessor.get();
            OwFNCM5Network network = resource.getNetwork();
            return network.fromNativeObject(nativeValue_p, resource);
        }
        else
        {
            return null;
        }
    }

}
