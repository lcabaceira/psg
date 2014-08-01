package com.wewebu.expression.language;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *<p>
 * A date value built around the {@link Calendar} Java implementation.  
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
public class OwExprDateValue extends OwExprValue implements Comparable
{
    private Calendar m_calendar;

    /**
     * Generic date value property implementation.<br>
     * Uses calendar field settings in {@link #m_addition} to acquire the property value.
     */
    private class DateProperty extends OwExprScopedProperty
    {
        private int m_calendarField;
        private int m_addition = 0;

        /**
         * Constructor 
         * @param calendarField_p field index of this property as defined by {@link Calendar} (egg. {@link Calendar#DAY_OF_MONTH}) 
         */
        public DateProperty(int calendarField_p)
        {
            this(calendarField_p, 0);
        }

        /**
         * 
         * @param calendarField_p field index of this property as defined by {@link Calendar} (egg. {@link Calendar#DAY_OF_MONTH})
         * @param addition_p integer value to add to the value extracted through {@link Calendar#get(int)} (useful for month 0 based value) 
         */
        public DateProperty(int calendarField_p, int addition_p)
        {
            super();
            this.m_calendarField = calendarField_p;
            this.m_addition = addition_p;
        }

        /**
         * 
         * @return always {@link OwExprType#NUMERIC}
         * @throws OwExprEvaluationException
         */
        public OwExprType type() throws OwExprEvaluationException
        {
            return OwExprType.NUMERIC;
        }

        /**
         * 
         * @return the value as extracted through {@link Calendar#get(int)}
         * @throws OwExprEvaluationException
         */
        public OwExprValue value() throws OwExprEvaluationException
        {
            int day = OwExprDateValue.this.m_calendar.get(m_calendarField) + m_addition;
            return new OwExprNumericValue(day);
        }

        public Class<?> javaType() throws OwExprEvaluationException
        {
            return Integer.class;
        }
    };

    /**
     * Date value properties
     */
    private Map m_datePropeties = new HashMap();

    /**
     * Constructor
     * @param calendar_p inner calendar value
     */
    public OwExprDateValue(Calendar calendar_p)
    {
        this(calendar_p, Calendar.class);
    }

    /**
     * Constructor
     * @param calendar_p inner calendar value
     * @param javaType_p original java type
     * @since 1.3.0 and AWD 3.1.0
     */
    public OwExprDateValue(Calendar calendar_p, Class<?> javaType_p)
    {
        super(javaType_p);
        this.m_calendar = calendar_p;
        this.m_datePropeties.put("second", new DateProperty(Calendar.SECOND));
        this.m_datePropeties.put("minute", new DateProperty(Calendar.MINUTE));
        this.m_datePropeties.put("hour", new DateProperty(Calendar.HOUR_OF_DAY));
        this.m_datePropeties.put("day", new DateProperty(Calendar.DAY_OF_MONTH));
        this.m_datePropeties.put("month", new DateProperty(Calendar.MONTH, 1));
        this.m_datePropeties.put("year", new DateProperty(Calendar.YEAR));
    }

    /**
     * 
     * @param javaSuperType_p
     * @return always the inner calendar data - {@link #m_calendar}
     * @throws OwExprEvaluationException
     */
    public Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        if (Date.class.isAssignableFrom(javaSuperType_p))
        {
            return m_calendar.getTime();
        }
        else
        {
            return m_calendar;
        }
    }

    protected final Map getValuePropeties()
    {
        return m_datePropeties;
    }

    public final OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(this, v2_p);
    }

    public final OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprDateValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprStringValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprTimeValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    /**
     * 
     * @return the inner calendar data 
     */
    public final Calendar getCalendar()
    {
        return m_calendar;
    }

    public int compareTo(Object o_p)
    {
        if (o_p instanceof OwExprDateValue)
        {
            OwExprDateValue dateValue = (OwExprDateValue) o_p;
            int[] compareFields = new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND };
            for (int i = 0; i < compareFields.length; i++)
            {
                int dif = m_calendar.get(compareFields[i]) - dateValue.m_calendar.get(compareFields[i]);
                if (dif != 0)
                {
                    return dif;
                }
            }
            return 0;
        }
        else
        {
            return 1;
        }
    }

    public boolean equals(Object obj_p)
    {
        return this.compareTo(obj_p) == 0;
    }

    public int hashCode()
    {
        return m_calendar.hashCode();
    }

    public String toString()
    {
        SimpleDateFormat format = new SimpleDateFormat(OwExprSystem.DATE_FORMAT_STRING);
        format.setTimeZone(m_calendar.getTimeZone());
        return format.format(m_calendar.getTime());
    }

}
