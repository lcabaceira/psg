package com.wewebu.expression.language;

/**
 *<p>
 * Expression symbols are identifiers and constants alike (no separate constant 
 * table is used).<br>
 * Expression symbols should implement custom behavior according to their semantics:<br>functions , properties ,constants or runtime-anonymous symbol 
 * Symbols are organized in a tree structure with multiple roots (the top level symbols).  
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
public abstract class OwExprSymbol
{
    /**Parent symbol- <code>null</code> if this is a top level symbol*/
    protected OwExprSymbol m_parent;

    /**Symbol literal name*/
    private String m_name;

    /**Symbol expression type*/
    private OwExprExpressionType m_symbolExpressionType;

    /**Sub symbols table*/
    private OwExprSymbolTable m_symbolTable = new OwExprSymbolTable();

    /**
     * Constructor
     * @param parent_p parent symbol
     * @param name_p symbol literal name
     * @param type_p symbol type
     */
    public OwExprSymbol(OwExprSymbol parent_p, String name_p, OwExprType type_p)
    {
        this(parent_p, name_p, new OwExprType[] { type_p });
    }

    /**
     * Constructor
     * @param parent_p parent symbol
     * @param name_p symbol literal name
     * @param types_p symbol types
     */
    public OwExprSymbol(OwExprSymbol parent_p, String name_p, OwExprType[] types_p)
    {
        super();
        this.m_name = name_p;
        this.m_symbolExpressionType = new OwExprExpressionType(types_p);
        this.m_parent = parent_p;
    }

    /**
     * 
    * @param parent_p parent symbol
     * @param name_p symbol literal name
     * @param type_p symbol expression types 
     */
    public OwExprSymbol(OwExprSymbol parent_p, String name_p, OwExprExpressionType type_p)
    {
        super();
        this.m_name = name_p;
        this.m_symbolExpressionType = type_p;
    }

    /**
     * Adds a property sub-symbol to this symbol. 
     * @param name_p the property symbol name
     * @param types_p the property types
     * @return  the newly added symbol
     * @throws OwExprTypeMissmatchException if symbol is already added and the type of the new symbol doesn't match the existing symbol 
     */
    public final OwExprPropertySymbol addPropertySymbol(String name_p, OwExprType[] types_p) throws OwExprTypeMissmatchException
    {
        return m_symbolTable.addPropertySymbol(this, name_p, types_p);
    }

    /**
     * Adds a property sub-symbol to this symbol. 
     * @param name_p the property symbol name
     * @param type_p the property expression type 
     * @return  the newly added symbol
     * @throws OwExprTypeMissmatchException if symbol is already added and the type of the new symbol doesn't match the existing symbol
     */
    public final OwExprSymbol addAnonymousSymbol(String name_p, OwExprExpressionType type_p) throws OwExprTypeMissmatchException
    {
        return m_symbolTable.addAnonymousSymbol(this, name_p, type_p);
    }

    /**
     * Adds a function sub-symbol to this symbol. 
     * @param name_p the property symbol name
     * @param types_p the property  types 
     * @return  the newly added symbol
     * @throws OwExprTypeMissmatchException if symbol is already added and the type of the new symbol doesn't match the existing symbol
     */
    public final OwExprFunctionSymbol addFunctionSymbol(String name_p, OwExprType[] types_p) throws OwExprTypeMissmatchException
    {
        return m_symbolTable.addFunctionSymbol(this, name_p, types_p);
    }

    /**
     * 
     * @return a new, unique , runtime sub-symbol name for this symbol  
     */
    public final String issueRuntimeSymbolName()
    {
        return m_symbolTable.issueRuntimeSymbolName();
    }

    /**
     * 
     * @return the name of this symbol
     */
    public final String getName()
    {
        return m_name;
    }

    /**
     * 
     * @return the type of this symbol
     */
    public final OwExprExpressionType getType()
    {
        return m_symbolExpressionType;
    }

    /**
     * @param name_p 
     * @return a sub-symbol with the given name
     */
    public final OwExprSymbol getSymbol(String name_p)
    {
        return m_symbolTable.getSymbol(name_p);
    }

    /**
     * 
     * @return the sub symbols table 
     */
    public final OwExprSymbolTable getSymbolTable()
    {
        return m_symbolTable;
    }

    protected void toPrettyPrintString(StringBuffer sb_p, StringBuffer tabs_p)
    {
        sb_p.append(m_name);
        sb_p.append(":");
        sb_p.append(m_symbolExpressionType.toString());

        sb_p.append("\n");
        StringBuffer newTabs = new StringBuffer();
        newTabs.append(tabs_p);
        newTabs.append("\t");
        m_symbolTable.toPrettyPrintString(sb_p, newTabs);
    }

    /**
     * 
     * @return String representation of the fully qualified symbol name (egg. object.peer.name)
     */
    public String getSymbolFQN()
    {
        String fqn;
        if (m_parent != null)
        {
            fqn = m_parent.getSymbolFQN() + "." + getName();
        }
        else
        {
            fqn = getName();
        }

        return fqn;
    }

    /**
     * A match method used to check the validity of symbols for certain scopes.<br>
     * Can be used to discover symbol and related expressions validity before evaluation.
     * @param scope_p a scope to search the symbol on
     * @return <code>true</code> if this symbol is valid for the given scope
     * @throws OwExprEvaluationException
     */
    public abstract boolean matches(OwExprScope scope_p) throws OwExprEvaluationException;

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        toPrettyPrintString(sb, new StringBuffer());
        return sb.toString();
    }
}
