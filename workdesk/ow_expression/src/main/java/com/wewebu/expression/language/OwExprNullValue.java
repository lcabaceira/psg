package com.wewebu.expression.language;

/**
 *<p>
 * Null value class.<br/>
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
public final class OwExprNullValue extends OwExprValue
{
    public static final OwExprNullValue INSTANCE = new OwExprNullValue();

    /**
     * Constructor<br>
     * Clients of this class should always use the {@link #INSTANCE} singleton
     */
    private OwExprNullValue()
    {
        super(null);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(this, v2_p);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprScopeValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprStringValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprNumericValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprTimeValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprBooleanValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprDateValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public boolean equals(Object obj_p)
    {
        return obj_p == INSTANCE;
    }

    public int hashCode()
    {
        return this.getClass().hashCode();
    }

    /**
     * 
     * @param javaSuperType_p
     * @return always <code>null</code>
     * @throws OwExprEvaluationException
     */
    public Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        return null;
    }

    public String toString()
    {
        return "null";
    }

}
