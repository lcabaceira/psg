package com.wewebu.expression.language;

/**
 *<p>
 * An expression representing a fixed value (egg. strings , numeric values so.)
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
 *@see com.wewebu.expression.parser.OwExprParser
 *@see OwExprValue
 */
public abstract class OwExprLiteral extends OwExprExpression implements OwExprPrimaryPrefix
{
    protected OwExprConstantSymbol m_symbol;
    private OwExprValue m_value;

    /**
     * Constant symbol single flyweight instance accessor.
     * If the symbol is not a constant symbol an error is added to the global error table. 
     * @param image_p the constant symbol image
     * @param symbolTable_p the global symbol table
     * @param errorTable_p the global error table 
     * @return a single symbol instance for the designated image_p or <code>null</code> if no such instance exists
     */
    protected static final OwExprConstantSymbol symbolInstance(String image_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        try
        {
            return (OwExprConstantSymbol) symbolTable_p.getSymbol(image_p);
        }
        catch (ClassCastException e)
        {
            errorTable_p.add(new OwExprTypeMissmatchException("Imvalid symbol : " + image_p, e));
            return null;
        }
    }

    /**
     * Constructor
     * @param symbolName_p the name of the symbol this literal relays on
     * @param value_p the value of this literal
     * @param symbolType_p the type of the symbol
     * @param symbolTable_p the global symbol table
     * @param errorTable_p the global error table
     */
    public OwExprLiteral(String symbolName_p, OwExprValue value_p, OwExprType symbolType_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super(symbolTable_p, errorTable_p);
        this.m_value = value_p;

        try
        {
            this.m_symbol = symbolTable_p.addConstantSymbol(this, symbolName_p, symbolType_p);
        }
        catch (OwExprTypeMissmatchException e)
        {
            errorTable_p.add(e);
        }
    }

    /**
     * 
     * @return the type of this symbol
     * @throws OwExprTypeMissmatchException
     */
    public final OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        return m_symbol.getType();
    }

    /**
     * 
     * @return the value of this symbol
     */
    public final OwExprValue getValue()
    {
        return m_value;
    }

    /**
     * 
     * @param scope_p
     * @return the value of this symbol
     * @throws OwExprEvaluationException
     */
    public final OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        return m_value;
    }

    /**
     * @return N/A
     * @throws OwExprTypeMissmatchException always
     */
    public final OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Invalid property access on literal " + this);
    }

    /**
     * @return N/A
     * @throws OwExprTypeMissmatchException always
     */
    public final OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Invalid function call on literal " + this);
    }

    /**
     * @return an {@link OwExprSymbolScope} based on this literals symbol
     */

    public OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException
    {
        return new OwExprSymbolScope(true, m_symbol, this);
    }

    /**
     * 
     * @return this literal
     */
    public final OwExprExpression expression()
    {
        return this;
    }

    public String toString()
    {
        return m_symbol.getName();
    }

}
