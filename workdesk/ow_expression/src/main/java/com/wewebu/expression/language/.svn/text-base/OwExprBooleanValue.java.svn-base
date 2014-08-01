package com.wewebu.expression.language;

/**
 *<p>
 * The standard boolean value. 
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
public class OwExprBooleanValue extends OwExprValue
{
    /**Singleton instance of TRUE*/
    public static final OwExprBooleanValue TRUE = new OwExprBooleanValue(true);
    /**Singleton instance of TRUE*/
    public static final OwExprBooleanValue FALSE = new OwExprBooleanValue(false);

    /**
     * Singleton value instances accessor
     * @param bool_p the wrapped boolean vale  
     * @return {@link #TRUE} if bool_p==true and {@link #FALSE} if bool_p==false
     */
    public static final OwExprBooleanValue value(boolean bool_p)
    {
        return bool_p ? TRUE : FALSE;
    }

    /**
     * 
     * @param bool_p
     * @return    * @return {@link #TRUE} if bool_p.booleanValue()==true and {@link #FALSE} if bool_p.booleanValue()==false
     */
    public static final OwExprBooleanValue value(Boolean bool_p)
    {
        return value(bool_p.booleanValue());
    }

    private boolean m_boolean;

    /**
     * Constructor
     * @param bool_p the wrapped boolean values
     */
    private OwExprBooleanValue(boolean bool_p)
    {
        super(Boolean.class);
        this.m_boolean = bool_p;
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(this, v2_p);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprBooleanValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprStringValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public OwExprValue accept(OwExprUnaryOperator unaryOperator_p) throws OwExprEvaluationException
    {
        return unaryOperator_p.evaluate(this);
    }

    /**
     * 
     * @return the wrapped boolean value
     */
    public boolean getBoolean()
    {
        return m_boolean;
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprBooleanValue)
        {
            OwExprBooleanValue booleanObject = (OwExprBooleanValue) obj_p;
            return this.m_boolean == booleanObject.m_boolean;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return new Boolean(m_boolean).hashCode();
    }

    public String toString()
    {
        return "" + m_boolean;
    }

    public Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        return new Boolean(m_boolean);
    }

}
