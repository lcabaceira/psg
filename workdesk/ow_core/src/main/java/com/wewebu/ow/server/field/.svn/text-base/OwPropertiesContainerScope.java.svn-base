package com.wewebu.ow.server.field;

import java.lang.reflect.Array;

import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprObjectProperty;
import com.wewebu.expression.language.OwExprProperty;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstract property container based scope for Expression Language expressions.<br/>
 * Rules Engine for Highlighting in Hit List.
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
 *@since 3.1.0
 */
public abstract class OwPropertiesContainerScope extends OwExprExternalScope
{

    public OwPropertiesContainerScope(String name_p)
    {
        super(name_p);
    }

    /**(overridable)
     * 
     * @param propertyName_p
     * @return the property with the given name
     * @throws OwException
     */
    protected abstract OwProperty getProperty(String propertyName_p) throws OwException;

    protected OwExprProperty prelevateProperty(String propertyName_p) throws OwExprEvaluationException
    {
        try
        {
            OwProperty objectProperty = getProperty(propertyName_p);
            Object value = objectProperty.getValue();
            OwPropertyClass propertyClass = objectProperty.getPropertyClass();
            String javaClassName = propertyClass.getJavaClassName();
            Class<?> valueClass = Class.forName(javaClassName);
            if (OwObject.class.isAssignableFrom(valueClass) && value instanceof OwObject)
            {
                value = new OwObjectScope(propertyName_p, (OwObject) value);
            }
            else
            {
                if (propertyClass.isComplex())
                {
                    Object[] arrayValue = (Object[]) value;
                    OwComplexPropertyChildScope[] scopes = new OwComplexPropertyChildScope[arrayValue.length];
                    for (int i = 0; i < scopes.length; i++)
                    {
                        scopes[i] = new OwComplexPropertyChildScope("" + i, (Object[]) arrayValue[i], propertyClass);
                    }
                    value = scopes;
                    valueClass = Array.class;
                }
            }
            return new OwExprObjectProperty(value, valueClass);
        }
        catch (Exception e)
        {
            throw new OwExprEvaluationException("Could not get property " + propertyName_p + " from object " + this.toString(), e);
        }
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        try
        {
            OwProperty property = getProperty(propertyName_p);
            return property != null;
        }
        catch (Exception e)
        {
            //TODO: we need a hasProperty method in OwObject
            //no other way to check property availability by name
            return false;
        }

    }

}
