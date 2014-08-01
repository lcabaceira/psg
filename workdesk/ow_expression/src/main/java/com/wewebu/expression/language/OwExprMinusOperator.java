package com.wewebu.expression.language;

import java.util.Calendar;

/**
 *<p>
 * OwExprMinusOperator.
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
public class OwExprMinusOperator extends OwExprBinaryOperator
{

    /**
     * Result types for operand types table.
     * <table class="jd"><tr></td><td class="header">&nbsp;</td><td class="header">&nbsp;STRING</td><td class="header">&nbsp;NUMERIC</td><td class="header">&nbsp;TIME</td><td class="header">&nbsp;DATE</td><td class="header">&nbsp;BOOLEAN</td><td class="header">&nbsp;SCOPE</td><td class="header">&nbsp;NULL</td></tr><tr><td class="header">&nbsp;STRING</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;NUMERIC</td><td>&nbsp;&nbsp;</td><td>&nbsp;NUMERIC&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;TIME</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;TIME&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;DATE</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;DATE&nbsp;</td><td>&nbsp;TIME&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;BOOLEAN</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;SCOPE</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;NULL</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr></tr></table>
     */
    private static final OwExprBinaryConversionTable CONVERSION_TABLE = new OwExprBinaryConversionTable();
    static
    {
        CONVERSION_TABLE.add(OwExprType.NUMERIC, OwExprType.NUMERIC, OwExprType.NUMERIC);
        CONVERSION_TABLE.add(OwExprType.DATE, OwExprType.DATE, OwExprType.TIME);
        CONVERSION_TABLE.add(OwExprType.DATE, OwExprType.TIME, OwExprType.DATE);
        CONVERSION_TABLE.add(OwExprType.TIME, OwExprType.TIME, OwExprType.TIME);
    }

    public static final OwExprMinusOperator INSTANCE = new OwExprMinusOperator(CONVERSION_TABLE);

    public OwExprMinusOperator(OwExprBinaryConversionTable conversionTable_p)
    {
        super("-", conversionTable_p);

    }

    public final OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        return v1_p.sub(v2_p);
    }

    public final OwExprValue evaluate(OwExprDateValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        Calendar c1 = v1_p.getCalendar();
        Calendar c2 = v2_p.getCalendar();
        OwExprTime diff = OwExprTime.diff(c1, c2);

        return new OwExprTimeValue(diff);
    }

    public final OwExprValue evaluate(OwExprDateValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        Calendar valueC1 = v1_p.getCalendar();
        OwExprTime t2 = v2_p.getTime();
        Calendar value = t2.substractFrom(valueC1);

        OwExprDateValue dateValue = new OwExprDateValue(value);
        return dateValue;
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        OwExprTime t1 = v1_p.getTime();
        OwExprTime t2 = v2_p.getTime();
        OwExprTime time = t1.substract(t2);
        return new OwExprTimeValue(time);
    }

}
