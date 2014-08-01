package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : OR operator implementation of the &lt;search condition&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwORSearchCondition implements OwSearchCondition
{
    public static final String OR = "OR";

    private OwSearchCondition m_leftSideCondition;
    private OwBooleanTerm m_rightSideTerm;

    public OwORSearchCondition(OwSearchCondition leftSideCondition_p, OwBooleanTerm rightSideTerm_p)
    {
        super();
        m_leftSideCondition = leftSideCondition_p;
        m_rightSideTerm = rightSideTerm_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal <br>
     *         invalid operands are omitted  
     */
    public StringBuilder createSearchConditionSQLString()
    {
        StringBuilder builder;
        boolean left = m_leftSideCondition.isValid() && !m_leftSideCondition.isXCondition();
        if (left)
        {
            builder = m_leftSideCondition.createSearchConditionSQLString();
        }
        else
        {
            builder = new StringBuilder();
        }
        if (m_rightSideTerm.isValid() && !m_rightSideTerm.isXCondition())
        {
            if (left)
            {
                builder.append(" ");
                builder.append(OR);
                builder.append(" ");
            }
            StringBuilder builderRight = m_rightSideTerm.createBooleanTermSQLString();
            builder.append(builderRight);
        }
        return builder;
    }

    /**
     * 
     * @return <code>true</code> if either of the operands is valid
     */
    public boolean isValid()
    {
        return (m_leftSideCondition.isValid() && !m_leftSideCondition.isXCondition()) || (m_rightSideTerm.isValid() && !m_rightSideTerm.isXCondition());
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>();
        boolean left = m_leftSideCondition.isValid() && !m_leftSideCondition.isXCondition();
        if (left)
        {
            qualifiers.addAll(m_leftSideCondition.getColumnQualifiers());
        }
        if (m_rightSideTerm.isValid() && !m_rightSideTerm.isXCondition())
        {
            qualifiers.addAll(m_rightSideTerm.getColumnQualifiers());
        }

        return qualifiers;
    }

    public boolean isXCondition()
    {
        return m_rightSideTerm.isXCondition() && m_leftSideCondition.isXCondition();
    }

    public OwBooleanTest asBooleanTest()
    {
        return new OwSearchConditionBooleanTest(this);
    }

}
