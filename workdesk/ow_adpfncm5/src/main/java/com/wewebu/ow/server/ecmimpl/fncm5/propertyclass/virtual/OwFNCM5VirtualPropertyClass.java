package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5VirtualObject;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EngineBinding;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5VirtualBinding;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * Abstract class for virtual properties.
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
public class OwFNCM5VirtualPropertyClass implements OwFNCM5PropertyClass, OwFNCM5VirtualBinding
{
    private OwPropertyClass propertyClass;
    private OwFNCM5EngineBinding<?, ?, ?> engineBinding;

    public OwFNCM5VirtualPropertyClass(OwPropertyClass propertyClass_p)
    {
        this(propertyClass_p, null);
    }

    public OwFNCM5VirtualPropertyClass(OwPropertyClass propertyClass_p, OwFNCM5EngineBinding<?, ?, ?> engineBinding_p)
    {
        this.propertyClass = propertyClass_p;
        this.engineBinding = engineBinding_p;
    }

    public OwFNCM5EngineBinding<?, ?, ?> getEngineBinding() throws OwException
    {
        return this.engineBinding;
    }

    public boolean isSystemProperty() throws OwException
    {
        try
        {
            return propertyClass.isSystemProperty();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve property class system status.", e);
        }
    }

    public boolean isNameProperty() throws Exception
    {
        return propertyClass.isNameProperty();
    }

    public boolean isReadOnly(int iContext_p) throws OwException
    {
        try
        {
            return propertyClass.isReadOnly(iContext_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve property class readonly status.", e);
        }
    }

    public boolean isHidden(int iContext_p) throws OwException
    {
        try
        {
            return this.propertyClass.isHidden(iContext_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve property class hidden status.", e);
        }
    }

    public String getCategory() throws Exception
    {
        return propertyClass.getCategory();
    }

    public String getClassName()
    {
        return propertyClass.getClassName();
    }

    public String getDisplayName(Locale locale_p)
    {
        return propertyClass.getDisplayName(locale_p);
    }

    public String getDescription(Locale locale_p)
    {
        return propertyClass.getDescription(locale_p);
    }

    public String getJavaClassName()
    {
        return propertyClass.getJavaClassName();
    }

    public Object getNativeType() throws OwException
    {
        try
        {
            return propertyClass.getNativeType();
        }
        catch (Exception e)
        {
            throw new OwServerException("Cannot retrieve native type", e);
        }
    }

    public boolean isEnum() throws Exception
    {
        return propertyClass.isEnum();
    }

    public OwEnumCollection getEnums() throws Exception
    {
        return propertyClass.getEnums();
    }

    /**
     * A virtual property is by default 
     * not required.
     * @return boolean, default <code>false</code>
     */
    public boolean isRequired() throws Exception
    {
        return false;
    }

    public Object getMaxValue() throws Exception
    {
        return propertyClass.getMaxValue();
    }

    public Object getMinValue() throws Exception
    {
        return propertyClass.getMinValue();
    }

    public Object getDefaultValue() throws Exception
    {
        return propertyClass.getDefaultValue();
    }

    public boolean isArray() throws OwException
    {
        try
        {
            return propertyClass.isArray();
        }
        catch (Exception ex)
        {
            throw new OwServerException("Cannot retrieve isArray information", ex);
        }
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {
        return propertyClass.getValueFromNode(node_p);
    }

    public Object getValueFromString(String text_p) throws Exception
    {
        return propertyClass.getValueFromString(text_p);
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        return propertyClass.getNodeFromValue(value_p, doc_p);
    }

    public OwFormat getFormat()
    {
        return propertyClass.getFormat();
    }

    /**
     * By default a virtual property does not have any operators.
     * @return Collection, null by default
     */
    public Collection getOperators() throws Exception
    {
        return null;
    }

    public List getComplexChildClasses() throws Exception
    {
        return propertyClass.getComplexChildClasses();
    }

    public boolean isComplex()
    {
        return propertyClass.isComplex();
    }

    public OwProperty propertyOf(OwFNCM5VirtualObject<?> virtualObject_p) throws OwException
    {
        return virtualObject_p.createVirtualProperty(this);
    }

    public final OwFNCM5VirtualBinding getVirtualBinding() throws OwException
    {
        return this;
    }
}
