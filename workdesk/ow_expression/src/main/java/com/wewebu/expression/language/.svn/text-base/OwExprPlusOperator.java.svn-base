package com.wewebu.expression.language;

import java.util.Calendar;

/**
 *<p>
 * OwExprPlusOperator.
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
public class OwExprPlusOperator extends OwExprBinaryOperator
{

    /**
     * Result types for operand types table.
     * <table class="jd"><tr></td><td class="header">&nbsp;</td><td class="header">&nbsp;STRING</td><td class="header">&nbsp;NUMERIC</td><td class="header">&nbsp;TIME</td><td class="header">&nbsp;DATE</td><td class="header">&nbsp;BOOLEAN</td><td class="header">&nbsp;SCOPE</td><td class="header">&nbsp;NULL</td></tr><tr><td class="header">&nbsp;STRING</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;NUMERIC</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;NUMERIC&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;TIME</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;TIME&nbsp;</td><td>&nbsp;DATE&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;DATE</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;DATE&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;BOOLEAN</td><td>&nbsp;STRING&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;SCOPE</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr><tr><td class="header">&nbsp;NULL</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td></tr></tr></table>
     */
    private static final OwExprBinaryConversionTable CONVERSION_TABLE = new OwExprBinaryConversionTable();

    static
    {
        CONVERSION_TABLE.add(OwExprType.STRING, new OwExprType[] { OwExprType.STRING, OwExprType.NUMERIC, OwExprType.TIME, OwExprType.DATE, OwExprType.BOOLEAN }, OwExprType.STRING);
        CONVERSION_TABLE.add(new OwExprType[] { OwExprType.NUMERIC, OwExprType.TIME, OwExprType.DATE, OwExprType.BOOLEAN }, OwExprType.STRING, OwExprType.STRING);
        CONVERSION_TABLE.add(OwExprType.NUMERIC, OwExprType.NUMERIC, OwExprType.NUMERIC);
        CONVERSION_TABLE.add(OwExprType.TIME, OwExprType.TIME, OwExprType.TIME);
        CONVERSION_TABLE.add(OwExprType.TIME, OwExprType.DATE, OwExprType.DATE);
        CONVERSION_TABLE.add(OwExprType.DATE, OwExprType.TIME, OwExprType.DATE);

    }

    public static final OwExprPlusOperator INSTANCE = new OwExprPlusOperator(CONVERSION_TABLE);

    private OwExprPlusOperator(OwExprBinaryConversionTable conversionTable_p)
    {
        super("+", conversionTable_p);
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        return v1_p.add(v2_p);
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.toString() + v2_p.getString());
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.getString() + v2_p.toString());
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.getString() + v2_p.getString());
    }

    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        Calendar calendar = v1_p.getCalendar();
        OwExprTime time = v2_p.getTime();
        Calendar value = time.addTo(calendar);
        OwExprDateValue dateValue = new OwExprDateValue(value);
        return dateValue;
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        Calendar calendar = v2_p.getCalendar();
        OwExprTime time = v1_p.getTime();
        Calendar value = time.addTo(calendar);
        OwExprDateValue dateValue = new OwExprDateValue(value);
        return dateValue;
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        OwExprTime t1 = v1_p.getTime();
        OwExprTime t2 = v2_p.getTime();
        OwExprTime time = t1.add(t2);
        return new OwExprTimeValue(time);

    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprBooleanValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.getString() + v2_p.getBoolean());
    }

    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.getBoolean() + v2_p.getString());
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.getString() + v2_p.toString());
    }

    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.toString() + v2_p.getString());
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.getString() + v2_p.toString());
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        return new OwExprStringValue(v1_p.toString() + v2_p.getString());
    }
}
