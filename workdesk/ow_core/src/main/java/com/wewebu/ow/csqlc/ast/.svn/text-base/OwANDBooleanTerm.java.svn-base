package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : AND operator implementation of the &lt;boolean term&gt; syntax non-terminal as defined by the  SQL grammar.<br>
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
public class OwANDBooleanTerm extends OwBooleanTerm
{
    public static final String AND = "AND";

    private OwBooleanTerm m_leftSideTerm;
    private OwBooleanFactor m_rightSideFactor;

    /**
     * 
     * @param leftSideTerm_p the left side term of the AND operation
     * @param rightSideFactor_p the right side factor of the AND operation
     */
    public OwANDBooleanTerm(OwBooleanTerm leftSideTerm_p, OwBooleanFactor rightSideFactor_p)
    {
        super();
        m_leftSideTerm = leftSideTerm_p;
        m_rightSideFactor = rightSideFactor_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal <br>
     *         invalid operands are omitted  
     */
    public StringBuilder createBooleanTermSQLString()
    {
        StringBuilder builder;
        if (m_leftSideTerm.isValid())
        {
            builder = m_leftSideTerm.createBooleanTermSQLString();
        }
        else
        {
            builder = new StringBuilder();
        }
        if (m_rightSideFactor.isValid())
        {
            if (m_leftSideTerm.isValid())
            {
                builder.append(" ");
                builder.append(AND);
                builder.append(" ");
            }
            StringBuilder rightSideBuider = m_rightSideFactor.createBooleanFactorSQLString();
            builder.append(rightSideBuider);
        }
        return builder;
    }

    /**
     * 
     * @return <code>true</code> if either of the operands is valid
     */
    public boolean isValid()
    {
        return m_leftSideTerm.isValid() || m_rightSideFactor.isValid();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>();
        if (m_leftSideTerm.isValid())
        {
            qualifiers.addAll(m_leftSideTerm.getColumnQualifiers());
        }

        if (m_rightSideFactor.isValid())
        {
            qualifiers.addAll(m_rightSideFactor.getColumnQualifiers());
        }

        return qualifiers;
    }

    public boolean isXCondition()
    {
        return m_rightSideFactor.isXCondition() || m_leftSideTerm.isXCondition();
    }

    public OwBooleanTest asBooleanTest()
    {
        return new OwSearchConditionBooleanTest(this, false);
    }
}
