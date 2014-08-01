package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Just a wrapper Property that can respond with the proper PropertyClass  
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
 *@since 4.2.0.0
 */
public class OwAlfrescoBPMProxyProperty implements OwProperty
{
    private OwProperty property;
    private OwPropertyClass propertyClass;

    public OwAlfrescoBPMProxyProperty(OwProperty property, OwPropertyClass propertyClass)
    {
        this.property = property; //used for value
        this.propertyClass = propertyClass; //used for class
    }

    public Object getValue() throws Exception
    {
        return property.getValue();
    }

    public OwPropertyClass getPropertyClass() throws Exception
    {
        return this.propertyClass;
    }

    public void setValue(Object oValue_p) throws Exception
    {
        property.setValue(oValue_p);
    }

    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return property.isReadOnly(iContext_p);
    }

    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return this.propertyClass;
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        return property.isHidden(iContext_p);
    }

    public Object clone() throws CloneNotSupportedException
    {
        OwProperty clonedProperty = (OwProperty) property.clone();
        return new OwAlfrescoBPMProxyProperty(clonedProperty, this.propertyClass);
    }

    public Object getNativeObject() throws Exception
    {
        return property.getNativeObject();
    }

    @SuppressWarnings("unchecked")
    public int compareTo(Object o)
    {
        return property.compareTo(o);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        try
        {
            return String.format("%s[\n- m_value\t=%s\n- ClassName = %s", this.getClass(), this.property.getValue(), this.propertyClass.getClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "????";
        }
    }
}
