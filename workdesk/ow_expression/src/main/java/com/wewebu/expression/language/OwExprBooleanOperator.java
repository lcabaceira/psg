package com.wewebu.expression.language;

/**
 *<p>
 * A boolean operator base class.   
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
public abstract class OwExprBooleanOperator extends OwExprBinaryOperator
{

    /**
     * Result types for operand types table.
     * <table class="jd"><tr></td><td class="header">&nbsp;</td><td class="header">&nbsp;STRING</td><td class="header">&nbsp;NUMERIC</td><td class="header">&nbsp;TIME</td><td class="header">&nbsp;DATE</td><td class="header">&nbsp;BOOLEAN</td><td class="header">&nbsp;SCOPE</td><td class="header">&nbsp;NULL</td></tr><tr><td class="header">&nbsp;STRING</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;NUMERIC</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;TIME</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;DATE</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;BOOLEAN</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;SCOPE</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;NULL</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr></tr></table>
     */
    private static final OwExprBinaryConversionTable CONVERSION_TABLE = new OwExprBinaryConversionTable();
    static
    {
        CONVERSION_TABLE.add(OwExprType.BOOLEAN, OwExprType.BOOLEAN, OwExprType.BOOLEAN);
    }

    /**
     * Constructor 
     * @param image_p literal representation of this operator 
     */
    public OwExprBooleanOperator(String image_p)
    {
        super(image_p, CONVERSION_TABLE);
    }
}
