package com.wewebu.expression.language;

/**
 *<p>
 * OwExprObjectProperty.
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
public class OwExprObjectProperty extends OwExprScopedProperty
{
    private Object m_javaObject;
    private Class<?> m_javaType;

    public OwExprObjectProperty(Object javaObject_p)
    {
        this(javaObject_p, javaObject_p == null ? null : javaObject_p.getClass());
    }

    public OwExprObjectProperty(Object javaObject_p, Class<?> javaType_p)
    {
        super();
        this.m_javaObject = javaObject_p;
        this.m_javaType = javaType_p;
    }

    public OwExprType type() throws OwExprEvaluationException
    {
        return OwExprType.fromJavaType(m_javaObject);
    }

    public OwExprValue value() throws OwExprEvaluationException
    {
        return OwExprValue.fromJavaValue(m_javaObject, m_javaType);
    }

    public Class<?> javaType() throws OwExprEvaluationException
    {
        return m_javaType;
    }

}
