package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Collection;
import java.util.Locale;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormatAdapter;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwEnumCollection;

/**
 *<p>
 * OwCMISDelegateVirtualPropertyClass.
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
public abstract class OwCMISDelegateVirtualPropertyClass<O, C extends OwCMISObjectClass> extends OwCMISAbstractPropertyClass<O, C> implements OwCMISVirtualPropertyClass<O>
{
    private OwPropertyClass internalPropertyClass;
    private Collection<Integer> operators;

    public OwCMISDelegateVirtualPropertyClass(String className, OwPropertyClass internalPropertyClass, Collection<Integer> operators, C objectClass)
    {
        super(className, objectClass);
        this.internalPropertyClass = internalPropertyClass;
        this.operators = operators;
    }

    @Override
    public boolean isArray() throws OwException
    {
        try
        {
            return internalPropertyClass.isArray();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid operation.", e);
        }
    }

    @Override
    public boolean isReadOnly(int iContext_p) throws OwException
    {
        try
        {
            return internalPropertyClass.isReadOnly(iContext_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid operation.", e);
        }
    }

    @Override
    public String getNonQualifiedName()
    {
        return internalPropertyClass.getClassName();
    }

    @Override
    public boolean isNameProperty() throws Exception
    {
        try
        {
            return internalPropertyClass.isNameProperty();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid operation.", e);
        }

    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return internalPropertyClass.getDisplayName(locale_p);
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return internalPropertyClass.getDescription(locale_p);
    }

    @Override
    public String getJavaClassName()
    {
        return internalPropertyClass.getJavaClassName();
    }

    @Override
    public Object getNativeType() throws Exception
    {
        return internalPropertyClass.getNativeType();
    }

    @Override
    public boolean isRequired() throws Exception
    {
        return internalPropertyClass.isRequired();
    }

    @Override
    public Object getMaxValue() throws Exception
    {
        return internalPropertyClass.getMaxValue();
    }

    @Override
    public Object getMinValue() throws Exception
    {
        return internalPropertyClass.getMinValue();
    }

    @Override
    public Object getDefaultValue() throws Exception
    {
        return internalPropertyClass.getDefaultValue();
    }

    @Override
    public Collection getOperators() throws Exception
    {
        if (operators != null)
        {
            return operators;
        }
        else
        {
            return internalPropertyClass.getOperators();
        }
    }

    @Override
    public OwCMISFormat getFormat()
    {
        return new OwCMISFormatAdapter(internalPropertyClass.getFormat());
    }

    @Override
    public boolean isHidden(int iContext_p) throws OwException
    {
        try
        {
            return internalPropertyClass.isHidden(iContext_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid operation.", e);
        }
    }

    @Override
    public OwEnumCollection getEnums() throws OwException
    {
        try
        {
            return internalPropertyClass.getEnums();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid operation.", e);
        }
    }

    @Override
    public boolean isSystem() throws OwException
    {
        try
        {
            return internalPropertyClass.isSystemProperty();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid operation.", e);
        }

    }

    @Override
    public final OwCMISProperty<O> from(O... value_p) throws OwException
    {
        //TODO: very BAD... whay to do  ?!?!? 

        throw new OwNotSupportedException("Can not cerate stored-virtula-properties.");
    }

    @Override
    public OwCMISVirtualPropertyClass<O> createProxy(String className)
    {
        return new OwCMISVirtualPropertyClassProxy<O, C>(className, this, getObjectClass());
    }

    @Override
    public String toString()
    {
        StringBuilder strRep = new StringBuilder(getClassName());
        strRep.append("(");
        strRep.append(getObjectClass().getClassName());
        strRep.append(")");
        return strRep.toString();
    }
}
