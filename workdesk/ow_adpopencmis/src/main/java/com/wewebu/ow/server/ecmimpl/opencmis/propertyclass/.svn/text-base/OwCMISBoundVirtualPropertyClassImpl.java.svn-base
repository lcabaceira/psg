package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISBoundVirtualPropertyImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISVirtualProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISBoundVirtualPropertyClassImpl.
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
public class OwCMISBoundVirtualPropertyClassImpl<O> extends OwCMISDelegateVirtualPropertyClass<O, OwCMISObjectClass> implements OwCMISBoundVirtualPropertyClass<O>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISBoundVirtualPropertyClass.class);

    private String boundPropertyClass;

    public OwCMISBoundVirtualPropertyClassImpl(String className, OwPropertyClass internalPropertyClass_p, String boundPropertyClass_p, OwCMISObjectClass objectClass_p, Collection<Integer> operators_p)
    {
        super(className, internalPropertyClass_p, operators_p, objectClass_p);
        this.boundPropertyClass = boundPropertyClass_p;
    }

    protected OwCMISPropertyClass<O> getBoundPropertyClass() throws OwException
    {
        OwCMISObjectClass myObjectClass = getObjectClass();
        return (OwCMISPropertyClass<O>) myObjectClass.getPropertyClass(boundPropertyClass);
    }

    @Override
    public OwCMISVirtualProperty<O> from(OwCMISObject object_p) throws OwException
    {
        return new OwCMISBoundVirtualPropertyImpl<O>(this, object_p);
    }

    @Override
    public String getQueryName()
    {

        try
        {
            return getBoundPropertyClass().getQueryName();
        }
        catch (OwException e)
        {
            LOG.error("Invalid property binding.", e);
            return "NA";
        }
    }

    @Override
    public boolean isQueryable()
    {
        try
        {
            return getBoundPropertyClass().isQueryable();
        }
        catch (OwException e)
        {
            LOG.error("Invalid property binding.", e);
            return false;
        }
    }

    @Override
    public boolean isOrderable()
    {
        try
        {
            return getBoundPropertyClass().isOrderable();
        }
        catch (OwException e)
        {
            LOG.error("Invalid property binding.", e);
            return false;
        }
    }

    @Override
    public String getBoundPropertyClassName()
    {
        return boundPropertyClass;
    }

}
