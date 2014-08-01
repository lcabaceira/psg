package com.wewebu.expression.language;

/**
 *<p>
 * OwExprDebugBracketsExpression.   
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
public class OwExprDebugBracketsExpression extends OwExprExpression
{
    private OwExprExpression m_expression;

    public OwExprDebugBracketsExpression(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, OwExprExpression expression_p)
    {
        super(symbolTable_p, errorTable_p);
        this.m_expression = expression_p;
    }

    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        return m_expression.evaluate(scope_p);
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        return m_expression.type();
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(m_expression.toString());
        sb.append(")");

        return sb.toString();
    }
}
