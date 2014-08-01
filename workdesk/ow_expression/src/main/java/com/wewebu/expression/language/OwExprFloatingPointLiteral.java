package com.wewebu.expression.language;

import com.wewebu.expression.parser.ParseException;

/**
 *<p>
 * A fixed floating point value : <br>
 * <code>1.3211</code>.
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
public class OwExprFloatingPointLiteral extends OwExprLiteral
{
    /**
     * Single symbol literal creator.
     * This method will return instances of {@link OwExprFloatingPointLiteral} based on the a single symbol instance associated 
     * with the image_p floating point image  
     * @param image_p a floating point literal image 
     * @param symbolTable_p global symbol table
     * @param errorTable_p global error table
     * @return a single symbol based literal instance creator.
     * @throws ParseException
     */
    public static final OwExprFloatingPointLiteral instance(String image_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p) throws ParseException
    {
        try
        {
            OwExprConstantSymbol symbol = symbolInstance(image_p, symbolTable_p, errorTable_p);

            if (symbol == null)
            {
                return new OwExprFloatingPointLiteral(image_p, symbolTable_p, errorTable_p);
            }
            else
            {
                return (OwExprFloatingPointLiteral) symbol.getLiteral();
            }
        }
        catch (ClassCastException e)
        {
            errorTable_p.add(new OwExprTypeMissmatchException("Invalid symbol duplicate !", e));
            throw new ParseException("Invalid symbol " + image_p);
        }
    }

    /**
     * Constructor.
     * Clients of this class should use {@link #instance(String, OwExprSymbolTable, OwExprErrorTable)} 
     * @param image_p
     * @param symbolTable_p
     * @param errorTable_p
     */
    private OwExprFloatingPointLiteral(String image_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super(image_p, new OwExprNumericValue(Double.parseDouble(image_p)), OwExprType.NUMERIC, symbolTable_p, errorTable_p);

    }
}
