package com.wewebu.ow.csqlc.ast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 *<p>
 * SQL AST node : &lt;signed numeric literal&gt; syntax terminal as defined by the SQL grammar.<br/> 
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
 *@since 3.2.0.0
 */
public class OwSignedNumericLiteral implements OwLiteral
{
    private static final DecimalFormatSymbols NUMERIC_FORMAT_SYMBOLS = new DecimalFormatSymbols();
    static
    {
        NUMERIC_FORMAT_SYMBOLS.setDecimalSeparator('.');
    }
    public static final NumberFormat NUMERIC_FORMAT = new DecimalFormat("########################.############################", NUMERIC_FORMAT_SYMBOLS);

    private String m_numericSQLLiteral = null;

    /**
     * Constructor
     * @param number_p the {@link Number} value of this literal, can be null
     */
    public OwSignedNumericLiteral(Number number_p)
    {
        if (number_p != null)
        {
            m_numericSQLLiteral = NUMERIC_FORMAT.format(number_p);
        }
    }

    public StringBuilder createLiteralSQLString()
    {
        return new StringBuilder(m_numericSQLLiteral);
    }

    /**
     * 
     *@return <code>true</code> if the {@link Number} value of this literal is null<br>
     *         <code>false</code> otherwise
     */
    public boolean isNull()
    {
        return m_numericSQLLiteral == null;
    }

}
