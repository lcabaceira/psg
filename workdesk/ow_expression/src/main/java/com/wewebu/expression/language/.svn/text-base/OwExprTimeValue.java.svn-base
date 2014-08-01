package com.wewebu.expression.language;

import java.util.HashMap;
import java.util.Map;

/**
 *<p>
 * A date value built around the {@link OwExprTime} implementation.<br>
 * Time values represent time spans. <br>
 * The following properties are supported by values of this type : <br>
 * <table class="jd">   
 *     <tr><td>seconds</td></tr>
 *     <tr><td>minutes</td></tr>
 *     <tr><td>hours</td></tr>
 *     <tr><td>inDays</td></tr>
 *     <tr><td>inUTCDays</td></tr>
 *     <tr><td>days</td></tr>
 *     <tr><td>months</td></tr>
 *     <tr><td>years</td></tr>
 * </table>
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
public class OwExprTimeValue extends OwExprValue
{
    private OwExprTime m_time;

    /**
     *{@link OwExprTime} based value property base class. 
     */
    private abstract class TimeProperty extends OwExprScopedProperty
    {
        public OwExprType type() throws OwExprEvaluationException
        {
            return OwExprType.NUMERIC;
        }

        public OwExprValue value() throws OwExprEvaluationException
        {

            return new OwExprNumericValue(numberValue());
        }

        protected abstract Number numberValue();

        public Class<?> javaType() throws OwExprEvaluationException
        {
            return Number.class;
        };
    };

    private Map m_timePropeties = new HashMap();

    /**
     * Constructor
     * @param time_p the inner time value
     */
    public OwExprTimeValue(OwExprTime time_p)
    {
        this(time_p, OwExprTime.class);
    }

    /**
     * Constructor
     * @param time_p the inner time value
     * @param javaType_p original java type
     * @since 1.3.0 and AWD 3.1.0
     */
    public OwExprTimeValue(OwExprTime time_p, Class<?> javaType_p)
    {
        super(javaType_p);
        this.m_time = time_p;
        this.m_timePropeties.put("seconds", new TimeProperty() {

            protected Number numberValue()
            {
                return new Double(OwExprTimeValue.this.m_time.getSeconds());
            }
        });
        this.m_timePropeties.put("minutes", new TimeProperty() {

            protected Number numberValue()
            {
                return new Double(OwExprTimeValue.this.m_time.getMinutes());
            }
        });
        this.m_timePropeties.put("hours", new TimeProperty() {

            protected Number numberValue()
            {
                return new Double(OwExprTimeValue.this.m_time.getHours());
            }
        });
        this.m_timePropeties.put("inDays", new TimeProperty() {

            protected Number numberValue()
            {
                return new Double(OwExprTimeValue.this.m_time.getInDays());
            }
        });
        this.m_timePropeties.put("inUTCDays", new TimeProperty() {

            protected Number numberValue()
            {
                return new Double(OwExprTimeValue.this.m_time.getInUTCDays());
            }
        });
        this.m_timePropeties.put("days", new TimeProperty() {

            protected Number numberValue()
            {
                return new Integer(OwExprTimeValue.this.m_time.getDays());
            }
        });
        this.m_timePropeties.put("months", new TimeProperty() {

            protected Number numberValue()
            {
                return new Integer(OwExprTimeValue.this.m_time.getMonths());
            }
        });
        this.m_timePropeties.put("years", new TimeProperty() {

            protected Number numberValue()
            {
                return new Integer(OwExprTimeValue.this.m_time.getYears());
            }
        });

    }

    protected final Map getValuePropeties()
    {
        return m_timePropeties;
    }

    public final OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v2_p.accept(binaryOperator_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprDateValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprTimeValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprStringValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    /**
     * 
     * @param javaSuperType_p
     * @return the inner {@link OwExprTime} 
     * @throws OwExprEvaluationException
     */
    public Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        return m_time;
    }

    public final OwExprTime getTime()
    {
        return m_time;
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprTimeValue)
        {
            OwExprTimeValue timeObject = (OwExprTimeValue) obj_p;
            return this.m_time.equals(timeObject.m_time);
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return m_time.hashCode();
    }

    public String toString()
    {
        return m_time.toString();
    }

}
