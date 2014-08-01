package com.wewebu.expression.language;

import java.lang.reflect.Method;

/**
 *<p>
 * Reflective properties are properties that rely on POJOs getters to return values.<br/>
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
public class OwExprReflectiveProperty implements OwExprProperty
{
    private Object m_javaObject;
    private Method m_propertyMethod;
    private String m_name;

    /**
     * Constructor
     * @param name_p the name of the property 
     * @param javaObject_p the underlying POJO
     * @param propertyMethod_p the getter for this property
     */
    public OwExprReflectiveProperty(String name_p, Object javaObject_p, Method propertyMethod_p)
    {
        super();
        this.m_javaObject = javaObject_p;
        this.m_propertyMethod = propertyMethod_p;
        this.m_name = name_p;
    }

    /**
     * Performs type conversion using {@link OwExprType#fromJavaType(Object)}
     * @return the type of this property
     * @throws OwExprEvaluationException
     */
    public OwExprType type() throws OwExprEvaluationException
    {
        return OwExprType.fromJavaType(m_propertyMethod.getReturnType());
    }

    /**
     * 
     * @return the java object as obtained from the underlying POJO using this properties' getter
     * @throws OwExprEvaluationException
     */
    private final Object javaValue() throws OwExprEvaluationException
    {
        Object javaValue;
        try
        {
            javaValue = m_propertyMethod.invoke(m_javaObject, new Object[] {});
            return javaValue;
        }
        catch (Exception e)
        {
            throw new OwExprEvaluationException(e);
        }

    }

    public final OwExprValue value() throws OwExprEvaluationException
    {

        return OwExprValue.fromJavaValue(javaValue(), javaType());

    }

    private OwExprScope valueScope() throws OwExprEvaluationException
    {
        //        Object javaValue = javaValue();
        //        OwExprReflectiveScope valueScope = new OwExprReflectiveScope(m_name, javaValue);
        return value();
    }

    public final OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTypes_p) throws OwExprEvaluationException
    {
        return valueScope().function(functionName_p, argunmentTypes_p);
    }

    public OwExprProperty property(String propertyName_p) throws OwExprEvaluationException
    {
        return valueScope().property(propertyName_p);
    }

    public OwExprScope solveScope(String name_p) throws OwExprEvaluationException
    {
        return valueScope().property(name_p);
    }

    public OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        Object javaValue = javaValue();
        if (javaValue.getClass().isArray())
        {
            Object[] javaArray = (Object[]) javaValue;
            if (javaArray.length <= index_p)
            {
                throw new OwExprEvaluationException("Index out of bounds : " + index_p + " for property " + m_name);
            }
            final Object javaIndexedObject = javaArray[index_p];
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
        Object javaValue = javaValue();
        if (javaValue.getClass().isArray())
        {
            Object[] javaArray = (Object[]) javaValue;
            return javaArray.length;
        }
        else
        {
            return 0;
        }
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return value().hasProperty(propertyName_p);
    }

    public Class<?> javaType() throws OwExprEvaluationException
    {
        Object v = javaValue();
        return v == null ? null : v.getClass();
    }

}
