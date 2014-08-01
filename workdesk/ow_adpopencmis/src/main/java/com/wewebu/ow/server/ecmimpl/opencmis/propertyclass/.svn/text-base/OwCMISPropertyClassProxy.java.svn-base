package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Collection;
import java.util.Locale;

import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwEnumCollection;

/**
 *<p>
 * Property class delegate-proxy.
 * Subclasses can customize behavior and/or data handling through method or accessor overriding.  
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
public class OwCMISPropertyClassProxy<V, C extends OwCMISObjectClass> extends OwCMISAbstractPropertyClass<V, C>
{
    private OwCMISPropertyClass<V> propertyClass;

    public OwCMISPropertyClassProxy(String className, OwCMISPropertyClass<V> propertyClass, C objectClass)
    {
        super(className, objectClass);
        this.propertyClass = propertyClass;
    }

    protected OwCMISPropertyClass<V> getPropertyClass()
    {
        return propertyClass;
    }

    @Override
    public boolean isArray() throws OwException
    {
        return propertyClass.isArray();
    }

    @Override
    public boolean isHidden(int context) throws OwException
    {
        return propertyClass.isHidden(context);
    }

    @Override
    public boolean isReadOnly(int context) throws OwException
    {
        return propertyClass.isReadOnly(context);
    }

    @Override
    public String getNonQualifiedName()
    {
        return propertyClass.getNonQualifiedName();
    }

    @Override
    public String getQueryName()
    {
        return propertyClass.getQueryName();
    }

    @Override
    public boolean isQueryable()
    {
        return propertyClass.isQueryable();
    }

    @Override
    public boolean isOrderable()
    {
        return propertyClass.isOrderable();
    }

    @Override
    public OwCMISProperty<V> from(V... value_p) throws OwException
    {
        return propertyClass.from(value_p);
    }

    @Override
    public OwEnumCollection getEnums() throws OwException
    {
        return propertyClass.getEnums();
    }

    @Override
    public boolean isSystem() throws OwException
    {
        return propertyClass.isSystemProperty();
    }

    @Override
    public boolean isNameProperty() throws Exception
    {
        return propertyClass.isNameProperty();
    }

    @Override
    public String getDisplayName(Locale locale)
    {
        return propertyClass.getDisplayName(locale);
    }

    @Override
    public String getDescription(Locale locale)
    {
        return propertyClass.getDescription(locale);
    }

    @Override
    public String getJavaClassName()
    {
        return propertyClass.getJavaClassName();
    }

    @Override
    public Object getNativeType() throws Exception
    {
        return propertyClass.getNativeType();
    }

    @Override
    public boolean isRequired() throws Exception
    {
        return propertyClass.isRequired();
    }

    @Override
    public Object getMaxValue() throws Exception
    {
        return propertyClass.getMaxValue();
    }

    @Override
    public Object getMinValue() throws Exception
    {
        return propertyClass.getMinValue();
    }

    @Override
    public Object getDefaultValue() throws Exception
    {
        return propertyClass.getDefaultValue();
    }

    @Override
    public Collection getOperators() throws Exception
    {
        return propertyClass.getOperators();
    }

    @Override
    public OwCMISFormat getFormat()
    {
        return propertyClass.getFormat();
    }

}
