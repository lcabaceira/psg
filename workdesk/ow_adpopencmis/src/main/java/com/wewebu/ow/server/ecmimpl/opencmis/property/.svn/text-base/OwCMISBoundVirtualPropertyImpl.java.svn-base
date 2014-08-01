package com.wewebu.ow.server.ecmimpl.opencmis.property;

import com.wewebu.ow.server.ecmimpl.opencmis.exception.OwCMISRuntimeException;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISBoundVirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 *  OwCMISBoundVirtualPropertyImpl.
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
public class OwCMISBoundVirtualPropertyImpl<O> extends OwCMISAbstractVirtualProperty<O, OwCMISBoundVirtualPropertyClass<O>> implements OwCMISBoundVirtualProperty<O>
{

    private OwCMISProperty<O> boudProperty;

    public OwCMISBoundVirtualPropertyImpl(OwCMISBoundVirtualPropertyClass<O> propertyClass_p, OwCMISObject object_p) throws OwException
    {
        super(propertyClass_p, object_p);

        OwCMISBoundVirtualPropertyClass<O> clazz = getPropertyClass();
        OwCMISObject theObject = getObject();

        boudProperty = (OwCMISProperty<O>) theObject.getProperty(clazz.getBoundPropertyClassName());
    }

    @Override
    public Object getNativeObject() throws OwException
    {
        return boudProperty.getNativeObject();
    }

    @Override
    public OwCMISProperty<O> getBoundProperty()
    {
        return boudProperty;
    }

    @Override
    protected O toValue() throws OwException
    {
        return (O) getBoundProperty().getValue();
    }

    @Override
    protected O[] toArrayValue() throws OwException
    {
        return (O[]) getBoundProperty().getValue();
    }

    @Override
    protected void fromValue(O value_p) throws OwException
    {
        getBoundProperty().setValue(value_p);
    }

    @Override
    protected void fromArrayValue(O[] value_p) throws OwException
    {
        getBoundProperty().setValue(value_p);
    }

    @Override
    public OwCMISBoundVirtualProperty<O> clone() throws CloneNotSupportedException
    {
        try
        {
            return new OwCMISBoundVirtualPropertyImpl<O>(getPropertyClass(), getObject());
        }
        catch (OwException e)
        {
            throw new OwCMISRuntimeException("Could not clone virtula bound property", e);
        }
    }

}
