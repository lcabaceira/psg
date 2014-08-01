package com.wewebu.expression.language;

import com.wewebu.expression.parser.ParseException;

/**
 *<p>
 * A fixed boolean value (one of <code>true</code> of <code>false</code>): <br/>
 * <code>true!=false</code>   
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
public class OwExprBooleanLiteral extends OwExprLiteral
{
    /**
     * Single <code>TRUE</code> or <code>FALSE</code> symbol literal creator.
     * This method will return instances of {@link OwExprBooleanLiteral} based on one of the instances of the <i>true</i>
     * or <i>false</i> symbols in the symbol table. 
     * @param booleanValue_p 
     * @param symbolTable_p global symbol table
     * @param errorTable_p global error table
     * @return a single symbol based literal instance creator.
     * @throws ParseException
     */
    public static final OwExprBooleanLiteral instance(boolean booleanValue_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p) throws ParseException
    {
        try
        {
            OwExprConstantSymbol symbol = symbolInstance("" + booleanValue_p, symbolTable_p, errorTable_p);

            if (symbol == null)
            {
                return new OwExprBooleanLiteral(booleanValue_p, symbolTable_p, errorTable_p);
            }
            else
            {
                return (OwExprBooleanLiteral) symbol.getLiteral();
            }
        }
        catch (ClassCastException e)
        {
            errorTable_p.add(new OwExprTypeMissmatchException("Invalid symbol duplicate !", e));
            throw new ParseException("Invalid symbol " + booleanValue_p);
        }
    }

    /**
     * Constructor.
     * Clients of this class should use {@link #instance(boolean, OwExprSymbolTable, OwExprErrorTable)} 
     * @param booleanValue_p
     * @param symbolTable_p
     * @param errorTable_p
     */
    private OwExprBooleanLiteral(boolean booleanValue_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super("" + booleanValue_p, OwExprBooleanValue.value(booleanValue_p), OwExprType.BOOLEAN, symbolTable_p, errorTable_p);

    }

}
