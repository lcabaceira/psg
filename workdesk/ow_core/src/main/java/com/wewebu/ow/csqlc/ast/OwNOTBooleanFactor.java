package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : NOT operator implementation of the &lt;boolean factor&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
 *@since 3.2.0.0
 */
public class OwNOTBooleanFactor extends OwBooleanFactor
{
    public static final String NOT = "NOT";

    private OwBooleanTest m_booleanTest;

    /**
     * 
     * @param booleanTest_p the <b>boolean test</b> to negate
     */
    public OwNOTBooleanFactor(OwBooleanTest booleanTest_p)
    {
        this.m_booleanTest = booleanTest_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal <br>
     *         an empty  {@link StringBuilder} if this non-terminal is inValid
     *         
     * @see #isValid() 
     */
    public StringBuilder createBooleanFactorSQLString()
    {
        if (isValid())
        {
            StringBuilder builder = m_booleanTest.createBooleanFactorSQLString();
            builder.insert(0, " ");
            builder.insert(0, NOT);
            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }

    /**
     * 
     * @return <code>true</code> if the negated <b>boolean test</b> is valid<br>
     *         <code>false</code> otherwise
     *  
     */
    public boolean isValid()
    {
        return m_booleanTest.isValid();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (m_booleanTest.isValid())
        {
            return m_booleanTest.getColumnQualifiers();
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

    public boolean isXCondition()
    {
        return m_booleanTest.isXCondition();
    }

    public OwBooleanTest asBooleanTest()
    {
        return new OwSearchConditionBooleanTest(this);
    }
}
