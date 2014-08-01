package com.wewebu.expression.language;

import java.lang.reflect.Method;

/**
 *<p>
 * Reflective functions are properties that rely on POJOs methods to return values.<br/>
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
public class OwExprReflectiveFunction implements OwExprFunction
{
    private Object m_javaObject;;
    private Method m_functionMethod;

    /**
     * Constructor
     * @param javaObject_p the underlying POJO
     * @param functionMethod_p the method for this function
     */
    public OwExprReflectiveFunction(Object javaObject_p, Method functionMethod_p)
    {
        super();
        this.m_javaObject = javaObject_p;
        this.m_functionMethod = functionMethod_p;
    }

    public OwExprValue value(OwExprValue[] arguments_p) throws OwExprEvaluationException
    {
        Object[] javaArguments = new Object[arguments_p.length];
        Class[] parameterTypes = m_functionMethod.getParameterTypes();
        for (int i = 0; i < javaArguments.length; i++)
        {
            javaArguments[i] = arguments_p[i].toJavaObject(parameterTypes[i]);
        }
        try
        {
            Object returnObject = m_functionMethod.invoke(m_javaObject, javaArguments);
            return OwExprValue.fromJavaValue(returnObject, returnObject == null ? null : returnObject.getClass());
        }
        catch (Exception e)
        {
            throw new OwExprEvaluationException(e);
        }

    }

    public OwExprType type() throws OwExprEvaluationException
    {
        return OwExprType.fromJavaType(m_functionMethod.getReturnType());
    }

}
