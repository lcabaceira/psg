package com.wewebu.expression.language;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *<p>
 * OwExprSymbolTable.
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
public class OwExprSymbolTable
{
    private Map m_symbols = new HashMap();

    private int m_issuedRuntimeSymbols = 0;

    public final String issueRuntimeSymbolName()
    {
        m_issuedRuntimeSymbols += 1;
        return "0$" + m_issuedRuntimeSymbols;
    }

    public void addSymbol(OwExprSymbol symbol_p) throws OwExprTypeMissmatchException
    {
        m_symbols.put(symbol_p.getName(), symbol_p);
    }

    public OwExprConstantSymbol addConstantSymbol(OwExprLiteral literal_p, String name_p, OwExprType types_p) throws OwExprTypeMissmatchException
    {
        try
        {
            OwExprConstantSymbol symbol = (OwExprConstantSymbol) m_symbols.get(name_p);
            if (symbol == null)
            {
                symbol = new OwExprConstantSymbol(literal_p, name_p, new OwExprType[] { types_p });
                addSymbol(symbol);
            }
            return symbol;
        }
        catch (ClassCastException e)
        {
            throw new OwExprTypeMissmatchException("Symbol " + literal_p + " can should only be used as literal !");
        }
    }

    public OwExprSymbol addAnonymousSymbol(OwExprSymbol parent_p, String name_p, OwExprExpressionType type_p) throws OwExprTypeMissmatchException
    {
        try
        {
            OwExprSymbol symbol = (OwExprSymbol) m_symbols.get(name_p);
            if (symbol == null)
            {
                symbol = new OwExprAnonymousSymbol(parent_p, name_p, type_p);
                addSymbol(symbol);
            }
            return symbol;
        }
        catch (ClassCastException e)
        {
            throw new OwExprTypeMissmatchException("Invalid Symbol " + name_p + "  !");
        }
    }

    public OwExprPropertySymbol addPropertySymbol(OwExprSymbol parent_p, String name_p, OwExprType[] types_p) throws OwExprTypeMissmatchException
    {
        try
        {
            OwExprPropertySymbol symbol = (OwExprPropertySymbol) m_symbols.get(name_p);
            if (symbol == null)
            {
                symbol = new OwExprPropertySymbol(parent_p, name_p, types_p);
                addSymbol(symbol);
            }
            return symbol;
        }
        catch (ClassCastException e)
        {
            throw new OwExprTypeMissmatchException("Symbol " + parent_p.getSymbolFQN() + "." + name_p + " can be either property or function !");
        }
    }

    public OwExprFunctionSymbol addFunctionSymbol(OwExprSymbol parent_p, String name_p, OwExprType[] types_p) throws OwExprTypeMissmatchException
    {
        try
        {
            OwExprFunctionSymbol symbol = (OwExprFunctionSymbol) m_symbols.get(name_p);
            if (symbol == null)
            {
                symbol = new OwExprFunctionSymbol(parent_p, name_p, types_p);
                addSymbol(symbol);
            }
            return symbol;
        }
        catch (ClassCastException e)
        {
            throw new OwExprTypeMissmatchException("Symbol " + parent_p.getSymbolFQN() + "." + name_p + " can be either property or function !");
        }

    }

    public OwExprSymbol getSymbol(String name_p)
    {
        return (OwExprSymbol) m_symbols.get(name_p);
    }

    /**
     * 
     * @return internal Map of name-symbol pairs
     * @since version 1.1.1 and AWD 3.1.0.0
     */
    public Map<String, OwExprSymbol> getSymbols()
    {
        return m_symbols;
    }

    protected void toPrettyPrintString(StringBuffer sb_p, StringBuffer tabs_p)
    {
        Set entries = m_symbols.entrySet();
        for (Iterator i = entries.iterator(); i.hasNext();)
        {
            Entry entry = (Entry) i.next();
            OwExprSymbol symbol = (OwExprSymbol) entry.getValue();
            sb_p.append(tabs_p);
            symbol.toPrettyPrintString(sb_p, tabs_p);
            sb_p.append("\n");
        }

    }

    public boolean matchesScope(OwExprScope scope_p) throws OwExprEvaluationException
    {
        Collection symbols = m_symbols.values();
        for (Iterator i = symbols.iterator(); i.hasNext();)
        {
            OwExprSymbol symbol = (OwExprSymbol) i.next();
            if (!symbol.matches(scope_p))
            {
                return false;
            }
        }

        return true;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        toPrettyPrintString(sb, new StringBuffer());
        return sb.toString();
    }
}
