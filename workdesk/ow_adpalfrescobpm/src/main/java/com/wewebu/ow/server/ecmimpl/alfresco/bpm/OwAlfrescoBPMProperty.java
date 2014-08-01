package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * {@link OwProperty} implementation for Alfresco BPM objects.
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
 *@since 4.0.0.0
 */
public class OwAlfrescoBPMProperty implements OwProperty, Cloneable
{
    private OwPropertyClass propertyClass;
    private Object nativeValue;
    private Object javaValue;
    private NativeValueConverterFactory converterFactory;

    public OwAlfrescoBPMProperty(Object nativeValue, OwPropertyClass propertyClass, NativeValueConverterFactory converterFactory) throws OwException
    {
        this.converterFactory = converterFactory;
        this.propertyClass = propertyClass;
        this.setNativeValue(nativeValue);
    }

    protected OwAlfrescoBPMProperty(Object javaValue, Object nativeValue, OwPropertyClass propertyClass, NativeValueConverterFactory converter)
    {
        this.converterFactory = converter;
        this.propertyClass = propertyClass;
        this.javaValue = javaValue;
        this.nativeValue = nativeValue;
    }

    private NativeValueConverter getConverter() throws OwException
    {
        return this.converterFactory.converterFor(this.propertyClass);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwField#getValue()
     */
    public Object getValue() throws Exception
    {
        return this.javaValue;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwField#setValue(java.lang.Object)
     */
    public void setValue(Object javaValue) throws Exception
    {
        if (null == javaValue)
        {
            this.nativeValue = null;
            this.javaValue = null;
        }
        else
        {
            this.nativeValue = this.getConverter().fromJava(javaValue);
            this.javaValue = javaValue;
        }
    }

    private void setNativeValue(Object nativeValue) throws OwException
    {
        if (null == nativeValue)
        {
            this.nativeValue = null;
            this.javaValue = null;
        }
        else
        {
            this.nativeValue = nativeValue;
            this.javaValue = this.getConverter().fromNative(this.nativeValue);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwField#getFieldDefinition()
     */
    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return this.propertyClass;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwProperty#getPropertyClass()
     */
    public OwPropertyClass getPropertyClass() throws Exception
    {
        return this.propertyClass;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwProperty#isReadOnly(int)
     */
    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return this.propertyClass.isReadOnly(iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwProperty#isHidden(int)
     */
    public boolean isHidden(int iContext_p) throws Exception
    {
        return this.propertyClass.isHidden(iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwProperty#getNativeObject()
     */
    public Object getNativeObject() throws Exception
    {
        return this.nativeValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        try
        {
            // === array clone
            return new OwAlfrescoBPMProperty(((Object[]) nativeValue).clone(), propertyClass, this.converterFactory);
        }
        catch (Exception e)
        {
            // not an array
        }

        try
        {
            // === try to call the clone method
            java.lang.reflect.Method method = nativeValue.getClass().getMethod("clone", new Class[] {});
            return new OwAlfrescoBPMProperty(method.invoke(nativeValue, new Object[] {}), propertyClass, this.converterFactory);
        }
        catch (Exception e)
        {
            // ignore, it is a scalar that is cloned by call by value calling convention
        }

        // scalar clones with call by value automatically
        return new OwAlfrescoBPMProperty(this.javaValue, this.nativeValue, this.propertyClass, this.converterFactory);
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int compareTo(Object obj_p)
    {
        Object myValue = null;
        Object value2 = null;

        try
        {
            myValue = getValue();
            value2 = ((OwProperty) obj_p).getValue();

            // compare the values using String interface
            try
            {
                return ((String) myValue).compareToIgnoreCase((String) value2);
            }
            catch (ClassCastException e)
            {
                // try next interface
            }

            try
            {
                // compare the values using java.lang.Comparable interface
                return ((java.lang.Comparable) myValue).compareTo(value2);
            }
            catch (ClassCastException e)
            {
                // try next interface
            }

            // compare the converted values using String
            return myValue.toString().compareToIgnoreCase(value2.toString());
        }
        catch (Exception e)
        {
            if ((myValue == null) && (value2 == null))
            {
                return 0;
            }

            // null values are regarded bigger so they show always up at the end
            return (myValue == null ? 1 : -1);
        }
    }

    @Override
    public String toString()
    {
        return "Alfresco BPM Property " + propertyClass.getClassName();
    }
}
