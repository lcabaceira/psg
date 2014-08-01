package com.wewebu.expression.language;

/**
 *<p>
 * OwExprXorOperator.
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
public class OwExprXorOperator extends OwExprBooleanOperator
{
    public static final OwExprXorOperator INSTANCE = new OwExprXorOperator();

    private OwExprXorOperator()
    {
        super(" xor ");
    }

    /**
     * Evaluates boolean values using java's <code>^</code> operator
     * @param v1_p leftside operand
     * @param v2_p rightside operand
     * @return an {@link OwExprBooleanValue} as computed in an <code>XOR</code> operation  
     * @throws OwExprEvaluationException
     */
    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprBooleanValue v2_p) throws OwExprEvaluationException
    {
        return OwExprBooleanValue.value(v1_p.getBoolean() ^ v2_p.getBoolean());
    }
}
