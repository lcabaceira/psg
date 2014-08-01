package com.wewebu.expression.language;

/**
 *<p>
 * OwExprScopeSelector. 
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
public class OwExprScopeSelector extends OwExprExpression implements OwExprPrimaryPrefix
{

    private OwExprPropertySymbol m_propertySymbol;
    private OwExprFunctionSymbol m_functionSymbol;

    private OwExprPrimaryPrefix m_prefix;
    private String m_scopeMemberName;

    private static final String replaceLegacySelectorPlaceHolders(String selector_p)
    {
        String minus = selector_p.replaceAll("\\$M", "-");
        String dot = minus.replaceAll("\\$D", ".");
        String colon = dot.replaceAll("\\$C", ":");
        String dollar = colon.replaceAll("\\$S", "\\$");

        return dollar;
    }

    private static final String replaceSelectorPlaceHolders(String selector_p)
    {
        String legacyReplacedSelector = replaceLegacySelectorPlaceHolders(selector_p);

        String minus = legacyReplacedSelector.replaceAll("\\\\-", "-");
        String dot = minus.replaceAll("\\\\\\u002E", ".");
        String colon = dot.replaceAll("\\\\:", ":");
        String bs = colon.replaceAll("\\\\\\\\", "\\\\");
        return bs;
    }

    public OwExprScopeSelector(OwExprSymbolTable symTable_p, OwExprErrorTable errorTable_p, String scopeMemberName_p)
    {
        this(symTable_p, errorTable_p, scopeMemberName_p, null);
    }

    public OwExprScopeSelector(OwExprSymbolTable symTable_p, OwExprErrorTable errorTable_p, String scopeMemberName_p, OwExprPrimaryPrefix prefix_p)
    {
        super(symTable_p, errorTable_p);
        this.m_prefix = prefix_p;
        this.m_scopeMemberName = replaceSelectorPlaceHolders(scopeMemberName_p);

    }

    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {

        if (m_propertySymbol != null)
        {
            return m_propertySymbol.getValue(scope_p);
        }
        else
        {
            throw new OwExprEvaluationException("Invalid function call !");
        }
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        if (!m_typeError)
        {
            if (m_propertySymbol != null)
            {
                return m_propertySymbol.getType();
            }
            else if (m_functionSymbol != null)
            {
                return m_functionSymbol.getType();
            }
            else
            {
                throw new OwExprTypeMissmatchException("Unknonw symbol type : " + m_scopeMemberName);
            }
        }
        else
        {
            return OwExprExpressionType.NOTYPE;
        }
    }

    public OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException
    {

        if (m_propertySymbol == null)
        {
            if (m_prefix != null)
            {
                OwExprSymbolScope symbolScope = m_prefix.regressToScope();
                m_propertySymbol = symbolScope.getSymbol().addPropertySymbol(m_scopeMemberName, OwExprType.ALL_TYPE_TYPES);
                m_propertySymbol.setSymbolScope(symbolScope);
            }
            else
            {
                m_propertySymbol = m_symbolTable.addPropertySymbol(null, m_scopeMemberName, OwExprType.ALL_TYPE_TYPES);
            }
        }

        return m_propertySymbol;

    }

    public OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException
    {
        regressToPorperty();

        return new OwExprSymbolScope(true, m_propertySymbol, this.expression());

    }

    public OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException
    {
        if (m_functionSymbol == null)
        {
            if (m_prefix != null)
            {
                OwExprSymbolScope symbolScope = m_prefix.regressToScope();
                m_functionSymbol = symbolScope.getSymbol().addFunctionSymbol(m_scopeMemberName, OwExprType.ALL_TYPE_TYPES);
                m_functionSymbol.setSymbolScope(symbolScope);
            }
            else
            {
                m_functionSymbol = m_symbolTable.addFunctionSymbol(null, m_scopeMemberName, OwExprType.ALL_TYPE_TYPES);
            }
        }

        return m_functionSymbol;

    }

    public OwExprExpression expression()
    {
        if (!m_typeError)
        {
            try
            {
                regressToPorperty();
                initType();
            }
            catch (OwExprTypeMissmatchException e)
            {
                m_errorTable.add(e);
                m_typeError = true;
            }
        }

        return this;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (m_prefix != null)
        {
            sb.append(m_prefix.toString());
            sb.append(".");
        }
        sb.append(m_scopeMemberName);
        return sb.toString();
    }
}
