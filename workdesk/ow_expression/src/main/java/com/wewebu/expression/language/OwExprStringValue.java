package com.wewebu.expression.language;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *<p>
 * The string value implementation.<br>
 * The implementation relays on the {@link String} Java implementation.  
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
public class OwExprStringValue extends OwExprValue
{
    private String m_string;

    private Map m_stringPropeties = new HashMap();

    /**
     * Constructor
     * @param string_p inner String value
     */
    public OwExprStringValue(String string_p)
    {
        this(string_p, String.class);
    }

    /**
     * Constructor
     * @param string_p inner String value
     * @param javaType_p original java type
     * @since 1.3.0 and AWD 3.1.0
     */
    public OwExprStringValue(String string_p, Class<?> javaType_p)
    {
        super(javaType_p);
        this.m_string = string_p;
        m_stringPropeties.put("length", new OwExprScopedProperty() {

            public OwExprType type() throws OwExprEvaluationException
            {
                return OwExprType.NUMERIC;
            }

            public OwExprValue value() throws OwExprEvaluationException
            {
                return new OwExprNumericValue(OwExprStringValue.this.m_string.length());
            }

            public Class<?> javaType() throws OwExprEvaluationException
            {
                return Integer.class;
            }

        });
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

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprBooleanValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprDateValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprTimeValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public final boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprStringValue)
        {
            OwExprStringValue stringObject = (OwExprStringValue) obj_p;
            return this.m_string.equals(stringObject.m_string);
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return m_string.hashCode();
    }

    public String getString()
    {
        return m_string;
    }

    public String toString()
    {
        return "'" + m_string + "'";
    }

    /**
     * 
     * @param javaSuperType_p
     * @return the inner string value
     * @throws OwExprEvaluationException
     */
    public final Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        return m_string;
    }

    protected Map getValuePropeties()
    {
        return m_stringPropeties;
    }

    /**
     * SQL Like function implementation
     * @param aString_p the like matching pattern
     * @return <code>true</code> if this string matches the given pattern <br>
     *         <code>false</code> otherwise
     */
    public boolean like(String aString_p)
    {
        String expr = escapeSQLToRegExp(aString_p);
        Pattern pattern = Pattern.compile(expr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return pattern.matcher(m_string).matches();

    }

    /**
     * SQL to Java regular expression pattern conversion and escaping method. 
     * @param sqlLike_p an SQL like pattern
     * @return the Java regular expression String corresponding to the given String
     * @since version 1.1.0 and AWD 3.1.0.0
     */
    protected String escapeSQLToRegExp(String sqlLike_p)
    {
        int length = sqlLike_p.length();
        StringBuffer buffer = new StringBuffer(length * 2);
        boolean sqlEscaped = false;
        boolean sqlRange = false;
        for (int i = 0; i < length; i++)
        {
            char c = sqlLike_p.charAt(i);
            if (sqlRange)
            {
                buffer.append(c);
                sqlRange = (']' != c);
            }
            else
            {

                if (sqlEscaped)
                {
                    if ("[](){}.*+?$^|#\\".indexOf(c) != -1)
                    {
                        buffer.append("\\");
                    }

                    buffer.append(c);

                    sqlEscaped = false;
                }
                else
                {

                    if ('%' == c)
                    {
                        buffer.append(".*");
                    }
                    else if ('_' == c)
                    {
                        buffer.append(".?");
                    }
                    else if ('!' == c)
                    {
                        sqlEscaped = true;
                    }
                    else
                    {
                        if ("(){}.*+?$^|#\\".indexOf(c) != -1)
                        {
                            buffer.append("\\");
                        }

                        buffer.append(c);
                    }
                    sqlRange = ('[' == c);
                }

            }

        }
        return buffer.toString();
    }

}
