package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISVirtualProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Allows multiple name reference of the same virtual property class (for example 
 * it can be used for qualified and non-qualified name reference).
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
public class OwCMISVirtualPropertyClassProxy<V, C extends OwCMISObjectClass> extends OwCMISPropertyClassProxy<V, C> implements OwCMISVirtualPropertyClass<V>
{

    /**
     * Constructor
     * @param className new proxy-name of the given property class
     * @param propertyClass the main property class
     * @param objectClass proxy-object-class of the given property class 
     */
    public OwCMISVirtualPropertyClassProxy(String className, OwCMISVirtualPropertyClass<V> propertyClass, C objectClass)
    {
        super(className, propertyClass, objectClass);
    }

    @Override
    protected OwCMISVirtualPropertyClass<V> getPropertyClass()
    {
        return (OwCMISVirtualPropertyClass<V>) super.getPropertyClass();
    }

    @Override
    public OwCMISVirtualProperty<V> from(OwCMISObject object_p) throws OwException
    {
        return getPropertyClass().from(object_p);
    }

    @Override
    public OwCMISVirtualPropertyClass<V> createProxy(String className)
    {
        return new OwCMISVirtualPropertyClassProxy<V, C>(className, this, getObjectClass());
    }

}
