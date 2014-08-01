package com.wewebu.ow.server.mock;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyComparator;
import com.wewebu.ow.server.field.OwFieldDefinition;

public class OwMockProperty implements OwProperty
{

    private Object value;
    private OwFieldDefinition fieldDefinition;
    private OwPropertyClass propertyClass;
    private boolean readOnly;
    private boolean hidden;
    private Object nativeObject;

    public int compareTo(Object o)
    {
        return OwPropertyComparator.getInstance().compare(this, (OwProperty) o);
    }

    public Object getValue() throws Exception
    {
        return value;
    }

    public void setValue(Object oValue_p) throws Exception
    {
        this.value = oValue_p;

    }

    public void setFieldDefinition(OwFieldDefinition fieldDefinition)
    {
        this.fieldDefinition = fieldDefinition;
    }

    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return this.fieldDefinition;
    }

    public void setPropertyClass(OwPropertyClass propertyClass)
    {
        this.propertyClass = propertyClass;
    }

    public OwPropertyClass getPropertyClass() throws Exception
    {
        return this.propertyClass;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return this.readOnly;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        return this.hidden;
    }

    public void setNativeObject(Object nativeObject)
    {
        this.nativeObject = nativeObject;
    }

    public Object getNativeObject() throws Exception
    {
        return this.nativeObject;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString()
    {
        Object myValue;
        try
        {
            myValue = getValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            myValue = "N/A(exception " + e.getMessage() + ")";
        }

        String myName;

        try
        {
            myName = getPropertyClass() == null ? "null-class" : getPropertyClass().getClassName();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            myName = "NoName(exception " + e.getMessage() + ")";
        }

        return myName + "=" + (myValue == null ? "<null value>" : myValue.toString());
    }
}
