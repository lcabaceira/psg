package com.wewebu.expression.language;

import com.wewebu.expression.parser.ParseException;

/**
 *<p>
 * A fixed string value : <br>
 * <code>'I am a string'</code>.
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
public class OwExprStringLiteral extends OwExprLiteral
{
    /**
     * Single symbol literal creator.
     * This method will return instances of {@link OwExprStringLiteral} based on the a single symbol instance associated 
     * with the string image  
     * @param image_p the string's image
     * @param symbolTable_p global symbol table
     * @param errorTable_p global error table
     * @return a single symbol based literal instance creator.
     * @throws ParseException
     */
    public static final OwExprStringLiteral instance(String image_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p) throws ParseException
    {
        try
        {
            OwExprConstantSymbol symbol = symbolInstance(image_p, symbolTable_p, errorTable_p);

            if (symbol == null)
            {
                return new OwExprStringLiteral(image_p, symbolTable_p, errorTable_p);
            }
            else
            {
                return (OwExprStringLiteral) symbol.getLiteral();
            }
        }
        catch (ClassCastException e)
        {
            errorTable_p.add(new OwExprTypeMissmatchException("Invalid symbol duplicate !", e));
            throw new ParseException("Invalid symbol " + image_p);
        }
    }

    /**
     * 
     * @param image_p
     * @return the image stripped of the first and last characters - assumed to be <b>'</b>
     */
    private static String stringImageToString(String image_p)
    {
        return image_p.substring(1, image_p.length() - 1);
    }

    /**
     * Constructor.
     * Clients of this class should use {@link #instance( String,OwExprSymbolTable, OwExprErrorTable)} 
     * @param symbolTable_p
     * @param errorTable_p
     */
    private OwExprStringLiteral(String image_p, OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super(image_p, new OwExprStringValue(stringImageToString(image_p)), OwExprType.STRING, symbolTable_p, errorTable_p);
    }

}
