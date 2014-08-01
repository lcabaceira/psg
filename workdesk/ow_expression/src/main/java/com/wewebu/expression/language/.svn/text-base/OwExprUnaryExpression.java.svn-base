package com.wewebu.expression.language;

/**
 *<p>
 * OwExprUnaryExpression.
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
public class OwExprUnaryExpression extends OwExprExpression
{
    private OwExprExpression m_operand;
    private OwExprUnaryOperator m_operator;

    public OwExprUnaryExpression(OwExprSymbolTable symTable_p, OwExprErrorTable errorTable_p, OwExprUnaryOperator operator_p, OwExprExpression operand_p)
    {
        super(symTable_p, errorTable_p);
        this.m_operator = operator_p;
        this.m_operand = operand_p;

        initType();
    }

    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        return m_operator.evaluate(scope_p, m_operand);
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        if (!m_typeError)
        {

            return m_operator.computeExpressionType(m_operand);
        }
        else
        {
            return OwExprExpressionType.NOTYPE;
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(m_operator.toString());
        sb.append(m_operand.toString());
        return sb.toString();
    }

}
