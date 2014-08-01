package com.wewebu.expression.language;

import java.util.Calendar;
import java.util.Date;

/**
 *<p>
 * Atomic value-types enumeration.
 * Defines evaluated values possible types and indexes them for faster type conversion 
 * table usage (see {@link OwExprBinaryConversionTable} {@link OwExprUnaryConversionTable}). 
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
public class OwExprType
{
    /** STRING type denotes UTF8 character arrays similar to the Java String type (see {@link OwExprStringValue}) */
    public static final OwExprType STRING = new OwExprType(0, "string");

    /** NUMERIC type denotes integer and floating point values (see {@link OwExprNumericValue}) */
    public static final OwExprType NUMERIC = new OwExprType(1, "numeric");

    /** TIME type denotes time spans (see {@link OwExprTimeValue}) */
    public static final OwExprType TIME = new OwExprType(2, "time");

    /** DATE type denotes calendar based dates (see {@link OwExprDateValue})*/
    public static final OwExprType DATE = new OwExprType(3, "date");

    /** BOOLEAN type denotes standard logical atomic values expressed as true and false (see {@link OwExprBooleanValue})*/
    public static final OwExprType BOOLEAN = new OwExprType(4, "boolean");

    /** SCOPE type denotes values interpreted as scopes (see {@link OwExprScopeValue}*/
    public static final OwExprType SCOPE = new OwExprType(5, "scope");

    /** NULL type denotes an unknown-value or the no-value (see {@link OwExprNullValue})*/
    public static final OwExprType NULL = new OwExprType(6, "null");

    /** NOTYPE type denotes an unknown type resulted form the compilation process. 
     * It serves as an error indicator and has no corresponding value.*/
    public static final OwExprType NOTYPE = new OwExprType(7, "notype");

    /**
     * Static java object type to expression language type conversion helper.<br><br>
     * 
     * <table class="jd">   
     * <tr>
     * <td class="header">Expression Type</td> <td class="header">Java Peer Type</td>
     * </tr>
     * <tr>   
     * <td>{@link #NUMERIC}</td> <td>java.lang.Number</td>
     * </tr>
     * <tr>   
     * <td>{@link #STRING}</td> <td>java.lang.String</td>
     * </tr>
     * <tr>   
     * <td>{@link #BOOLEAN}</td> <td>java.lang.Boolean</td>
     * </tr>
     * <tr>   
     * <td>{@link #TIME}</td> <td> {@link OwExprTime} </td>
     * </tr>
     * <tr>   
     * <td>{@link #DATE}</td> <td>java.util.Calendar</td>
     * </tr>
     * <tr>   
     * <td> {@link #NULL}</td> <td>Java's null  literal type</td>
     * </tr>
     * </table>
     *<br>
     * If the javaObject_p's type does not match any of the previous table entries it will be converted to {@link #SCOPE}
     * 
     * @param javaObject_p an object who's class is to be converted to its expression language peer type
     * 
     * @return the expression language corresponding type for the passed object's type 
     */
    public static final OwExprType fromJavaType(Object javaObject_p)
    {
        if (javaObject_p == null)
        {
            return NULL;
        }
        else if (javaObject_p instanceof Number)
        {
            return NUMERIC;
        }
        else if (javaObject_p instanceof String)
        {
            return STRING;
        }
        else if (javaObject_p instanceof Character)
        {
            return STRING;
        }
        else if (javaObject_p instanceof Calendar)
        {
            return DATE;
        }
        else if (javaObject_p instanceof OwExprTime)
        {
            return TIME;
        }
        else if (javaObject_p instanceof Date)
        {
            return DATE;
        }
        else if (javaObject_p instanceof Boolean)
        {
            return BOOLEAN;
        }
        else if (javaObject_p instanceof OwExprScope)
        {
            return SCOPE;
        }
        else
        {
            return SCOPE;
        }
    }

    /** All types enumeration contents*/
    public static final OwExprType[] ALL_TYPES = new OwExprType[] { STRING, NUMERIC, TIME, DATE, BOOLEAN, NULL, SCOPE, NOTYPE };

    /** All types enumeration contents except the {@link #NOTYPE} error marking type*/
    public static final OwExprType[] ALL_TYPE_TYPES = new OwExprType[] { STRING, NUMERIC, TIME, DATE, BOOLEAN, SCOPE, NULL };

    /**Type index to be used with conversion tables*/
    final int m_conversionIndex;

    private String m_name;

    /**
     * Constructor
     * @param conversionIndex_p conversion index ( {@link #m_conversionIndex} )
     * @param name_p type name
     */
    private OwExprType(int conversionIndex_p, String name_p)
    {
        this.m_conversionIndex = conversionIndex_p;
        this.m_name = name_p;
    }

    public String toString()
    {
        return this.m_name;
    }

    public int hashCode()
    {
        return m_conversionIndex;
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprType)
        {
            OwExprType typeObj = (OwExprType) obj_p;
            return typeObj.m_conversionIndex == this.m_conversionIndex;
        }
        else
        {
            return false;
        }
    }
}
