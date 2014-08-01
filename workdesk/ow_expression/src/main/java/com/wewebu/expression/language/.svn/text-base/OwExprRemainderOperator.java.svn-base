package com.wewebu.expression.language;

/**
 *<p>
 * OwExprRemainderOperator. 
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
public class OwExprRemainderOperator extends OwExprMultiplicativeOperator
{

    public static final OwExprRemainderOperator INSTANCE = new OwExprRemainderOperator();

    private OwExprRemainderOperator()
    {
        super("%");
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        try
        {
            return v1_p.reminder(v2_p);
        }
        catch (ArithmeticException e)
        {
            throw new OwExprEvaluationException(e);
        }
    }

}
