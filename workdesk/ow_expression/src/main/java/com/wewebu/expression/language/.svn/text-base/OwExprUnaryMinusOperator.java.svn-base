package com.wewebu.expression.language;

/**
 *<p>
 * Unary minus operator : <br/>
 * -2 , -4.554  ,  -property .
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
public class OwExprUnaryMinusOperator extends OwExprArithmeticUnaryOperator
{
    public static final OwExprUnaryOperator INSTANCE = new OwExprUnaryMinusOperator();

    private OwExprUnaryMinusOperator()
    {
        super("-");
    }

    public OwExprValue evaluate(OwExprNumericValue numericValue_p)
    {
        return numericValue_p.negate();
    }

}
