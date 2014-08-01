package com.wewebu.expression.language;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *<p>
 * The numeric value implementation.<br>
 * Objects of this class represent floating point or integer numeric values. 
 * The implementation relays on the {@link Number} Java implementation. 
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
public class OwExprNumericValue extends OwExprValue implements Comparable
{
    private Number m_number;

    /**
     * Constructor
     * @param number_p the inner numeric value
     * @param javaType_p original java type
     * @since 1.3.0 and AWD 3.1.0
     */
    public OwExprNumericValue(Number number_p, Class<?> javaType_p)
    {
        super(javaType_p);
        this.m_number = number_p;
    }

    /**
     * Constructor
     * @param number_p the inner numeric value
     */
    public OwExprNumericValue(Number number_p)
    {
        this(number_p, Number.class);
    }

    /**
     * Constructor
     * @param number_p the inner numeric value
     */
    public OwExprNumericValue(int number_p)
    {
        this(new Integer(number_p));
    }

    /**
     * Constructor
     * @param doubleNo_p the inner numeric value
     */
    public OwExprNumericValue(double doubleNo_p)
    {
        this(new Double(doubleNo_p));
    }

    public final OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(this, v2_p);
    }

    public final OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprNumericValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public final OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprStringValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprUnaryOperator unaryOperator_p) throws OwExprEvaluationException
    {
        return unaryOperator_p.evaluate(this);
    }

    public OwExprNumericValue add(OwExprNumericValue numeric_p)
    {
        if (m_number instanceof Double || numeric_p.m_number instanceof Double)
        {
            return new OwExprNumericValue(this.m_number.doubleValue() + numeric_p.m_number.doubleValue());
        }
        else
        {
            return new OwExprNumericValue(this.m_number.intValue() + numeric_p.m_number.intValue());
        }
    }

    /**
     * 
     * @return the arithmetic negation of this numeric value (egg. <code>new OwExprNumericValue(1).negate().equals(new OwExprNumericValue(-1))</code>
     */
    public OwExprNumericValue negate()
    {
        if (m_number instanceof Double)
        {
            return new OwExprNumericValue(-this.m_number.doubleValue());
        }
        else
        {
            return new OwExprNumericValue(-this.m_number.intValue());
        }
    }

    /**
     * @param numeric_p
     * @return a new {@link OwExprNumericValue} representing this numeric value minus the numeric value of the numeric_p parameter  
     */
    public OwExprValue sub(OwExprNumericValue numeric_p)
    {
        if (m_number instanceof Double || numeric_p.m_number instanceof Double)
        {
            return new OwExprNumericValue(this.m_number.doubleValue() - numeric_p.m_number.doubleValue());
        }
        else
        {
            return new OwExprNumericValue(this.m_number.intValue() - numeric_p.m_number.intValue());
        }
    }

    /**
     * @param numeric_p
     * @return a new {@link OwExprNumericValue} representing this numeric multiplied by the numeric value of the numeric_p parameter  
     */
    public OwExprValue mul(OwExprNumericValue numeric_p)
    {
        if (m_number instanceof Double || numeric_p.m_number instanceof Double)
        {
            return new OwExprNumericValue(this.m_number.doubleValue() * numeric_p.m_number.doubleValue());
        }
        else
        {
            return new OwExprNumericValue(this.m_number.intValue() * numeric_p.m_number.intValue());
        }
    }

    /**
     * @param numeric_p
     * @return a new {@link OwExprNumericValue} representing this numeric divided by the numeric value of the numeric_p parameter  
     */
    public OwExprValue div(OwExprNumericValue numeric_p) throws ArithmeticException
    {
        double doubleNumeric = numeric_p.m_number.doubleValue();
        if (doubleNumeric == 0.0)
        {
            throw new ArithmeticException("Division by zero!");
        }
        return new OwExprNumericValue(this.m_number.doubleValue() / doubleNumeric);
    }

    /**
     * @param numeric_p
     * @return a new {@link OwExprNumericValue} representing reminder of the division of this numeric value and the numeric value of the numeric_p parameter  
     */
    public OwExprValue reminder(OwExprNumericValue numeric_p) throws ArithmeticException
    {
        if (m_number instanceof Double || numeric_p.m_number instanceof Double)
        {
            return new OwExprNumericValue(this.m_number.doubleValue() % numeric_p.m_number.doubleValue());
        }
        else
        {
            return new OwExprNumericValue(this.m_number.intValue() % numeric_p.m_number.intValue());
        }
    }

    public boolean equals(Object obj_p)
    {
        return compareTo(obj_p) == 0;
    }

    public int hashCode()
    {
        return m_number.hashCode();
    }

    public int toInt()
    {
        return m_number.intValue();
    }

    public String toString()
    {
        return m_number.toString();
    }

    public int compareTo(Object o_p)
    {
        if (o_p instanceof OwExprNumericValue)
        {
            OwExprNumericValue numericObject = (OwExprNumericValue) o_p;
            double dif = m_number.doubleValue() - numericObject.m_number.doubleValue();
            return m_number.doubleValue() - numericObject.m_number.doubleValue() < 0 ? -1 : (dif == 0 ? 0 : 1);
        }
        else
        {
            return 1;
        }
    }

    /**
     * 
     * @param javaSuperType_p
     * @return this numeric value as an {@link Integer} if javaSuperType_p is different from  <code>Double.class</code> and <code>Double.TYPE</code><br>
     *         this numeric value as a {@link Double} otherwise
     * @throws OwExprEvaluationException
     */
    public Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        if (javaSuperType_p == null || m_number == null || javaSuperType_p.isAssignableFrom(m_number.getClass()))
        {
            return m_number;
        }

        if (BigInteger.class.isAssignableFrom(javaSuperType_p))
        {
            return new BigInteger(m_number.toString());
        }
        else if (BigDecimal.class.isAssignableFrom(javaSuperType_p))
        {
            return new BigDecimal(m_number.toString());

        }
        else if (Short.class.isAssignableFrom(javaSuperType_p) || javaSuperType_p.isAssignableFrom(Short.TYPE))
        {
            return new Short(m_number.shortValue());
        }
        else if (Double.class.isAssignableFrom(javaSuperType_p) || javaSuperType_p.isAssignableFrom(Double.TYPE))
        {
            return new Double(m_number.doubleValue());
        }
        else if (Float.class.isAssignableFrom(javaSuperType_p) || javaSuperType_p.isAssignableFrom(Float.TYPE))
        {
            return new Float(m_number.floatValue());
        }
        else if (Integer.class.isAssignableFrom(javaSuperType_p) || javaSuperType_p.isAssignableFrom(Integer.TYPE))
        {
            return new Integer(m_number.intValue());
        }
        else
        {
            return m_number;
        }
    }
}
