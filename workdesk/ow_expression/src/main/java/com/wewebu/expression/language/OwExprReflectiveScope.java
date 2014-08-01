package com.wewebu.expression.language;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *<p>
 * Reflective scopes are external scopes that rely on POJOs to prelevate 
 * functions and properties.<br>
 * Getters of the underlying POJO will be interpreted as properties and methods will be 
 * interpreted as functions. 
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
public class OwExprReflectiveScope extends OwExprExternalScope
{
    private static final String GETTER_PREFIX = "get";

    /**
     * The underlying POJO
     */
    private Object m_scopedObject;

    /**
     * Constructor
     * @param name_p the name of this scope
     */
    public OwExprReflectiveScope(String name_p)
    {
        super(name_p);
        this.m_scopedObject = this;
    }

    /**
     * Constructor 
     * @param name_p the name of this scope 
     * @param javaObject_p underlying POJO
     */
    public OwExprReflectiveScope(String name_p, Object javaObject_p)
    {
        super(name_p);
        this.m_scopedObject = javaObject_p;
    }

    protected final OwExprProperty prelevateProperty(String propertyName_p) throws OwExprEvaluationException
    {
        OwExprProperty property = super.prelevateProperty(propertyName_p);
        if (property == null)
        {
            return prelevateReflectiveProperty(propertyName_p);
        }
        else
        {
            return property;
        }

    }

    protected final OwExprFunction prelevateFunction(String functionName_p, OwExprExpressionType[] argumentTypes_p) throws OwExprEvaluationException
    {
        OwExprFunction function = super.prelevateFunction(functionName_p, argumentTypes_p);
        if (function == null)
        {
            return prelevateReflectiveFunction(functionName_p, argumentTypes_p);
        }
        else
        {
            return function;
        }
    }

    private Method prelevateGetter(String propertyName_p)
    {

        Class scopedClass = m_scopedObject.getClass();
        Method[] allMethods = scopedClass.getMethods();
        String capitalizedPropertyName = propertyName_p.substring(0, 1).toUpperCase() + (propertyName_p.length() > 1 ? propertyName_p.substring(1) : "");
        for (int i = 0; i < allMethods.length; i++)
        {
            if (Modifier.isPublic(allMethods[i].getModifiers()))
            {
                String methodName = allMethods[i].getName();
                if (methodName.equals(GETTER_PREFIX + capitalizedPropertyName))
                {
                    if (allMethods[i].getParameterTypes().length == 0)
                    {
                        return allMethods[i];
                    }
                }
            }
        }

        return null;
    }

    /**
     * Prelevates getter for a give property form the underlying POJO.
     * @param propertyName_p the requested property
     * @return a property based on the getter of the given property
     * @throws OwExprEvaluationException if the underlying POJO does not define the requested property
     */
    private OwExprReflectiveProperty prelevateReflectiveProperty(String propertyName_p) throws OwExprEvaluationException
    {

        Method getter = prelevateGetter(propertyName_p);
        if (getter != null)
        {
            return new OwExprReflectiveProperty(propertyName_p, m_scopedObject, getter);
        }
        else
        {
            throw new OwExprEvaluationException("No such reflective property  " + propertyName_p + " in scope " + getName() + " aka " + m_scopedObject);
        }
    }

    /**
     * Prelevates method for a give function and parameter types form the underlying POJO
     * @param functionName_p the requested function name 
     * @return a function based on the method with the given name and parameter types
     * @throws OwExprEvaluationException if the underlying POJO does not define the requested property
     */
    private OwExprReflectiveFunction prelevateReflectiveFunction(String functionName_p, OwExprExpressionType[] argumentTypes_p) throws OwExprEvaluationException
    {
        Class scopedClass = m_scopedObject.getClass();
        Method[] allMethods = scopedClass.getMethods();
        for (int i = 0; i < allMethods.length; i++)
        {
            if (Modifier.isPublic(allMethods[i].getModifiers()))
            {
                String methodName = allMethods[i].getName();
                if (methodName.equals(functionName_p))
                {
                    Class[] parameterTypes = allMethods[i].getParameterTypes();
                    if (parameterTypes.length == argumentTypes_p.length)
                    {
                        boolean parametersMatch = true;
                        for (int j = 0; j < parameterTypes.length; j++)
                        {
                            OwExprType type = OwExprType.fromJavaType(parameterTypes[j]);
                            if (!argumentTypes_p[j].canInfer(type))
                            {
                                parametersMatch = false;
                                break;
                            }
                        }
                        if (parametersMatch)
                        {
                            return new OwExprReflectiveFunction(m_scopedObject, allMethods[i]);
                        }
                    }
                }
            }

        }
        throw new OwExprEvaluationException("No such reflective function " + functionName_p + OwExprExpressionType.printTypes(argumentTypes_p) + " in scope " + getName() + " aka " + m_scopedObject);
    }

    /**
     * Indexed scope access method form an array POJO.
     * 
     * @param index_p <code>int</code> index of the requested property
     * 
     * @return the {@link OwExprProperty} property found at the requested index 
     * 
     * @throws OwExprEvaluationException if the requested indexed access has failed (index out of bounds, 
     *         the creation of {@link OwExprProperty} has failed)  
     */
    public OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        if (m_scopedObject.getClass().isArray())
        {
            if (Array.getLength(m_scopedObject) <= index_p)
            {
                throw new OwExprEvaluationException("Index out of bounds : " + index_p + " for scope " + getName());
            }
            final Object javaIndexedObject = Array.get(m_scopedObject, index_p);
            final Class<?> javaClass = javaIndexedObject == null ? null : javaIndexedObject.getClass();
            final OwExprValue value = OwExprValue.fromJavaValue(javaIndexedObject, javaClass);
            OwExprProperty valueScope = new OwExprScopedProperty() {

                public OwExprType type() throws OwExprEvaluationException
                {

                    return OwExprType.fromJavaType(javaIndexedObject.getClass());
                }

                public OwExprValue value() throws OwExprEvaluationException
                {
                    return value;
                }

                public Class<?> javaType() throws OwExprEvaluationException
                {
                    return javaClass;
                }

            };
            return valueScope;
        }
        else
        {
            throw new OwExprEvaluationException("Array access on non array scope!");
        }
    }

    public int length() throws OwExprEvaluationException
    {
        if (m_scopedObject.getClass().isArray())
        {
            return Array.getLength(m_scopedObject);
        }
        else
        {
            return 0;
        }

    }

    public String toString()
    {
        if (m_scopedObject == this)
        {
            return "R$" + super.toString();
        }
        else
        {
            return "R$" + m_scopedObject.toString();
        }
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        if (super.hasProperty(propertyName_p))
        {
            return true;
        }
        else
        {
            Method getter = prelevateGetter(propertyName_p);
            return getter != null;
        }
    }
}
