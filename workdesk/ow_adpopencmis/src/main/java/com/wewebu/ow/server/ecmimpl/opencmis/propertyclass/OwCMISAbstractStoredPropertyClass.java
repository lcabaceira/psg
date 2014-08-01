package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;

/**
 *<p>
 * OwCMISAbstractStoredPropertyClass.
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
public abstract class OwCMISAbstractStoredPropertyClass<O, C extends OwCMISObjectClass> extends OwCMISDelegateVirtualPropertyClass<O, C> implements OwCMISStoredVirtualPropertyClass<O>
{

    public OwCMISAbstractStoredPropertyClass(String className, OwPropertyClass internalPropertyClass, Collection<Integer> operators, C objectClass)
    {
        super(className, internalPropertyClass, operators, objectClass);
    }

    @Override
    public String getQueryName()
    {
        return null;
    }

    @Override
    public boolean isQueryable()
    {
        return false;
    }

    @Override
    public boolean isOrderable()
    {
        return false;
    }
}
