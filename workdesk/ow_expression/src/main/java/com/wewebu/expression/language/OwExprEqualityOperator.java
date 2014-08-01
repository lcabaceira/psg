package com.wewebu.expression.language;

/**
 *<p>
 * OwExprEqualityOperator.
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
public class OwExprEqualityOperator extends OwExprBinaryOperator
{

    private boolean m_negate;

    /**
     * Result types for operand types table.
     * <table class="jd"><tr></td><td class="header">&nbsp;</td><td class="header">&nbsp;STRING</td><td class="header">&nbsp;NUMERIC</td><td class="header">&nbsp;TIME</td><td class="header">&nbsp;DATE</td><td class="header">&nbsp;BOOLEAN</td><td class="header">&nbsp;SCOPE</td><td class="header">&nbsp;NULL</td></tr><tr><td class="header">&nbsp;STRING</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr><tr><td class="header">&nbsp;NUMERIC</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr><tr><td class="header">&nbsp;TIME</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr><tr><td class="header">&nbsp;DATE</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr><tr><td class="header">&nbsp;BOOLEAN</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr><tr><td class="header">&nbsp;SCOPE</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr><tr><td class="header">&nbsp;NULL</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td><td>&nbsp;BOOLEAN&nbsp;</td></tr></tr></table>
     */
    private static final OwExprBinaryConversionTable CONVERSION_TABLE = new OwExprBinaryConversionTable();
    static
    {
        CONVERSION_TABLE.add(OwExprType.STRING, new OwExprType[] { OwExprType.STRING, OwExprType.NUMERIC, OwExprType.TIME, OwExprType.DATE, OwExprType.BOOLEAN, OwExprType.NULL }, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(OwExprType.NULL, new OwExprType[] { OwExprType.STRING, OwExprType.NUMERIC, OwExprType.TIME, OwExprType.DATE, OwExprType.BOOLEAN, OwExprType.NULL, OwExprType.SCOPE }, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(new OwExprType[] { OwExprType.NUMERIC, OwExprType.TIME, OwExprType.DATE, OwExprType.BOOLEAN }, OwExprType.STRING, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(new OwExprType[] { OwExprType.NUMERIC, OwExprType.TIME, OwExprType.DATE, OwExprType.BOOLEAN, OwExprType.SCOPE }, OwExprType.NULL, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(OwExprType.NUMERIC, OwExprType.NUMERIC, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(OwExprType.TIME, OwExprType.TIME, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(OwExprType.DATE, OwExprType.DATE, OwExprType.BOOLEAN);
        CONVERSION_TABLE.add(OwExprType.BOOLEAN, OwExprType.BOOLEAN, OwExprType.BOOLEAN);
    }

    public static final OwExprEqualityOperator EQUAL_INSTANCE = new OwExprEqualityOperator("=", false, CONVERSION_TABLE);

    public static final OwExprEqualityOperator NOTEQUAL_INSTANCE = new OwExprEqualityOperator("!=", true, CONVERSION_TABLE);

    private OwExprEqualityOperator(String image_p, boolean negate_p, OwExprBinaryConversionTable conversionTable_p)
    {
        super(image_p, conversionTable_p);
        this.m_negate = negate_p;
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.equals(v2_p);
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.toString().equals(v2_p.getString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.getString().equals(v2_p.toString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.equals(v2_p);
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprNullValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v2_p, v1_p);
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.getTime().equals(v2_p.getTime());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprBooleanValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.equals(v2_p);
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.equals(v2_p);
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.toString().equals(v2_p.getString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.getString().equals(v2_p.toString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.getString().equals(v2_p.toString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = v1_p.toString().equals(v2_p.getString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = ("" + v1_p.getBoolean()).equals(v2_p.getString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprBooleanValue v2_p) throws OwExprEvaluationException
    {
        boolean eq = ("" + v2_p.getBoolean()).equals(v1_p.getString());
        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v1_p, v2_p);
    }

    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v1_p, v2_p);
    }

    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v1_p, v2_p);
    }

    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v1_p, v2_p);
    }

    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v1_p, v2_p);
    }

    public OwExprValue evaluate(OwExprScopeValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        return nullTest(v1_p, v2_p);
    }

    public OwExprValue evaluate(OwExprScopeValue v1_p, OwExprScopeValue v2_p) throws OwExprEvaluationException
    {
        Object[] v1Array = (Object[]) v1_p.toJavaObject(Object[].class);
        Object[] v2Array = (Object[]) v2_p.toJavaObject(Object[].class);
        boolean eq = OwExprSystem.arrayItemEquals(v1Array, v2Array);
        return OwExprBooleanValue.value(eq);
    }

    private OwExprBooleanValue nullTest(OwExprValue v_p, OwExprNullValue nullValue_p)
    {
        boolean eq = nullValue_p.equals(v_p);

        return OwExprBooleanValue.value(m_negate ? !eq : eq);
    }

}
