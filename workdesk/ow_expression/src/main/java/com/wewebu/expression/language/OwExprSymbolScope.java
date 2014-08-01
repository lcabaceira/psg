package com.wewebu.expression.language;

/**
 *<p>
 * OwExprSymbolScope. 
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
public class OwExprSymbolScope
{
    private OwExprExpression m_scopedExpression;
    private OwExprSymbol m_symbol;

    public OwExprSymbolScope(OwExprSymbol symbol_p, OwExprExpression scopedExpression_p) throws OwExprTypeMissmatchException
    {
        this(false, symbol_p, scopedExpression_p);
    }

    public OwExprSymbolScope(boolean implicitScope_p, OwExprSymbol symbol_p, OwExprExpression scopedExpression_p) throws OwExprTypeMissmatchException
    {
        this.m_symbol = symbol_p;
        this.m_scopedExpression = scopedExpression_p;
        if (!implicitScope_p)
        {
            OwExprExpressionType symbolType = symbol_p.getType();
            symbolType.regressTo(OwExprType.SCOPE);
        }
    }

    public OwExprScope solveScope(OwExprScope scope_p) throws OwExprEvaluationException
    {
        return runtimeValueScope(scope_p);
    }

    public final OwExprValue runtimeValueScope(OwExprScope scope_p) throws OwExprEvaluationException
    {
        return m_scopedExpression.evaluate(scope_p);
    }

    public final OwExprSymbol getSymbol()
    {
        return m_symbol;
    }
}
