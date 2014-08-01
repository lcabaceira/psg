package com.wewebu.expression.language;

import com.wewebu.expression.parser.ParseException;

/**
 *<p>
 * A fixed reserved word <code>null</code> expression : <br/>
 * <code>a!=null</code>
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
public class OwExprNullLiteral extends OwExprLiteral
{
    private static final String NULL = "null";

    /**
     * Single symbol literal creator.
     * This method will return instances of {@link OwExprNullLiteral} based on the a single symbol instance associated 
     * with the <code>null</code> reserved word String image  
     * @param symbolTable_p global symbol table
     * @param errorTable_p global error table
     * @return a single symbol based literal instance creator.
     * @throws ParseException
     */
    public static final OwExprNullLiteral instance(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p) throws ParseException
    {
        try
        {
            OwExprConstantSymbol symbol = symbolInstance(NULL, symbolTable_p, errorTable_p);

            if (symbol == null)
            {
                return new OwExprNullLiteral(symbolTable_p, errorTable_p);
            }
            else
            {
                return (OwExprNullLiteral) symbol.getLiteral();
            }
        }
        catch (ClassCastException e)
        {
            errorTable_p.add(new OwExprTypeMissmatchException("Invalid symbol duplicate !", e));
            throw new ParseException("Invalid symbol " + NULL);
        }
    }

    /**
     * Constructor.
     * Clients of this class should use {@link #instance( OwExprSymbolTable, OwExprErrorTable)} 
     * @param symbolTable_p
     * @param errorTable_p
     */
    private OwExprNullLiteral(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super(NULL, OwExprNullValue.INSTANCE, OwExprType.NULL, symbolTable_p, errorTable_p);
    }

}
