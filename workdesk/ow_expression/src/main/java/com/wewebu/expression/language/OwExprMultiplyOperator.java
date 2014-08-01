package com.wewebu.expression.language;

/**
 *<p>
 * OwExprMultiplyOperator.
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
public class OwExprMultiplyOperator extends OwExprMultiplicativeOperator
{
    public static final OwExprMultiplyOperator INSTANCE = new OwExprMultiplyOperator();

    private OwExprMultiplyOperator()
    {
        super("*");
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {

        return v1_p.mul(v2_p);
    }

}
