package com.wewebu.expression.language;

/**
 *<p>
 * Equal to or greater than operator implementation.
 * It is based on Java's {@link Comparable} implementation.
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
public class OwExprGreaterEqOperator extends OwExprComparisonOperator
{
    /**Single public instance of comparator*/
    public static final OwExprGreaterEqOperator INSTANCE = new OwExprGreaterEqOperator();

    /**
     * Constructor
     */
    private OwExprGreaterEqOperator()
    {
        super(">=");

    }

    /**
     * Evaluates numeric values using Java's {@link Comparable} implementations
     * @param v1_p leftside operand
     * @param v2_p rightside operand
     * @return a <code>true</code> {@link OwExprBooleanValue} if leftside operand is equal to or greater than the right side operand  
     * @throws OwExprEvaluationException
     */
    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        return OwExprBooleanValue.value(v1_p.compareTo(v2_p) >= 0);
    }

    /**
     * Evaluates date values using Java's {@link Comparable} implementations
     * @param v1_p leftside operand
     * @param v2_p rightside operand
     * @return a <code>true</code> {@link OwExprBooleanValue} if leftside date operand is equal to or greater than ...  
     * @throws OwExprEvaluationException
     */
    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        boolean value = v1_p.compareTo(v2_p) >= 0;
        return OwExprBooleanValue.value(value);
    }

    /**
     * Evaluates time values using Java's {@link Comparable} implementations
     * @param v1_p leftside operand
     * @param v2_p rightside operand
     * @return a <code>true</code> {@link OwExprBooleanValue} if leftside timespan operand is equal to or greater that right side timespan operand  
     * @throws OwExprEvaluationException
     */
    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        boolean value = v1_p.getTime().compareTo(v2_p.getTime()) >= 0;
        return OwExprBooleanValue.value(value);
    }
}
