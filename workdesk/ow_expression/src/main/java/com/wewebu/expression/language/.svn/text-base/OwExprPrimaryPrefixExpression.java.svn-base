package com.wewebu.expression.language;

/**
 *<p>
 * OwExprPrimaryPrefixExpression.
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
public class OwExprPrimaryPrefixExpression extends OwExprExpression implements OwExprPrimaryPrefix
{
    private OwExprExpression m_expression;

    public OwExprPrimaryPrefixExpression(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, OwExprExpression expression_p)
    {
        super(symbolTable_p, errorTable_p);
        this.m_expression = expression_p;
    }

    public OwExprExpression asExpression(OwExprSymbolTable symbolTable_p)
    {
        return m_expression;
    }

    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        return m_expression.evaluate(scope_p);
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        return m_expression.type();
    }

    public final OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Expression functions are not supported (functions must have names)!");

    }

    public OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("N/A");
    }

    public OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException
    {
        OwExprSymbol symbol = m_symbolTable.addAnonymousSymbol(null, m_symbolTable.issueRuntimeSymbolName(), m_expression.type());
        return new OwExprSymbolScope(true, symbol, this.expression());
    }

    public OwExprExpression expression()
    {
        if (!m_typeError)
        {
            initType();
        }
        return this;
    }

    public String toString()
    {
        return m_expression.toString();
    }

}
