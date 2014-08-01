package com.wewebu.expression.language;

/**
 *<p>
 * An arithmetic unary operator.
 * Defines the conversion table used for unary arithmetic operations.   
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
public abstract class OwExprArithmeticUnaryOperator extends OwExprUnaryOperator
{
    /**
     * Result types for operand types table.
     * <table class="jd"><tr><td class="header">&nbsp;Operand Type</td><td class="header">&nbsp;STRING</td><td class="header">&nbsp;NUMERIC</td><td class="header">&nbsp;TIME</td><td class="header">&nbsp;DATE</td><td class="header">&nbsp;BOOLEAN</td><td class="header">&nbsp;SCOPE</td><td class="header">&nbsp;NULL</td></tr><tr><td class="header">&nbsp;Result Type</td><td>&nbsp;&nbsp;</td><td>&nbsp;NUMERIC&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr></table>
     */
    private static final OwExprUnaryConversionTable CONVERSION_TABLE = new OwExprUnaryConversionTable();
    static
    {
        CONVERSION_TABLE.add(OwExprType.NUMERIC, OwExprType.NUMERIC);
    }

    /**
     * Constructor
     */
    public OwExprArithmeticUnaryOperator(String image_p)
    {
        super(image_p, CONVERSION_TABLE);
    }

}
