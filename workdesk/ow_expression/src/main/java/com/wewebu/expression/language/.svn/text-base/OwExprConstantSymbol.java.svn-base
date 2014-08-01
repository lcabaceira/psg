package com.wewebu.expression.language;

/**
 *<p>
 * Constant symbols represent symbol table recordings of fixed values.
 * They are associated with literals.  
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
public class OwExprConstantSymbol extends OwExprSymbol
{

    private OwExprLiteral m_literal;

    /**
     * Constructor
     * @param literal_p the associated literal 
     * @param name_p name of the symbol 
     * @param types_p the symbol possible types
     */
    public OwExprConstantSymbol(OwExprLiteral literal_p, String name_p, OwExprType[] types_p)
    {
        super(null, name_p, types_p);
        this.m_literal = literal_p;
    }

    /**
     * 
     * @return the {@link OwExprValue} associated with this symbol's literal
     */
    public final OwExprValue getValue()
    {
        return m_literal.getValue();
    }

    /**
     * Pretty-print utility
     * @param sb_p
     * @param tabs_p
     */
    protected void toPrettyPrintString(StringBuffer sb_p, StringBuffer tabs_p)
    {
        sb_p.append("literal ");
        super.toPrettyPrintString(sb_p, tabs_p);
    }

    /**
     * 
     * @return the literal associated with this constant symbol
     */
    public final OwExprLiteral getLiteral()
    {
        return m_literal;
    }

    /**
     * 
     * @param scope_p
     * @return always true
     */
    public boolean matches(OwExprScope scope_p)
    {
        return true;
    }

}
