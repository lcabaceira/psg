package com.wewebu.expression.language;

/**
 *<p>
 * OwExprUnaryNegateOperator.
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
public class OwExprUnaryNegateOperator extends OwExprUnaryOperator
{
    /**
     * Resut types for operand types table.
     * <table class="jd"><tr><td class="header">&nbsp;Operand Type</td><td class="header">&nbsp;STRING</td><td class="header">&nbsp;NUMERIC</td><td class="header">&nbsp;TIME</td><td class="header">&nbsp;DATE</td><td class="header">&nbsp;BOOLEAN</td><td class="header">&nbsp;SCOPE</td><td class="header">&nbsp;NULL</td></tr><tr><td class="header">&nbsp;Result Type</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr></table>
     */
    private static final OwExprUnaryConversionTable CONVERSION_TABLE = new OwExprUnaryConversionTable();
    static
    {
        CONVERSION_TABLE.add(OwExprType.BOOLEAN, OwExprType.BOOLEAN);
    }

    public static final OwExprUnaryNegateOperator INSTANCE = new OwExprUnaryNegateOperator(CONVERSION_TABLE);

    private OwExprUnaryNegateOperator(OwExprUnaryConversionTable conversionTable_p)
    {
        super(" not ", conversionTable_p);
    }

    public OwExprValue evaluate(OwExprBooleanValue booleanValue_p) throws OwExprEvaluationException
    {
        return OwExprBooleanValue.value(!booleanValue_p.getBoolean());
    }

}
