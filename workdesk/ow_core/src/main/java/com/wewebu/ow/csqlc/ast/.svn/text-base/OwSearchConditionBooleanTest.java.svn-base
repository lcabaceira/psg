package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : sub search condition embedding <br/>
 * &lt;boolean term&gt;:='(' &lt;search condition&gt ')'  syntax <br/>
 * non-terminal as defined by the SQL grammar.<br/>
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
public class OwSearchConditionBooleanTest extends OwBooleanTest
{
    private OwSearchCondition m_searchCondition;

    private boolean m_preservePriority = true;

    public OwSearchConditionBooleanTest(OwSearchCondition searchCondition_p)
    {
        this(searchCondition_p, true);
    }

    public OwSearchConditionBooleanTest(OwSearchCondition searchCondition_p, boolean preservePriority_p)
    {
        super();
        m_searchCondition = searchCondition_p;
        m_preservePriority = preservePriority_p;
    }

    @Override
    public StringBuilder createBooleanTestSQLString()
    {
        if (isValid())
        {
            StringBuilder builder = m_searchCondition.createSearchConditionSQLString();
            if (m_preservePriority)
            {
                builder.insert(0, "(");
                builder.append(")");
            }
            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }

    /**
     * 
     * @return <code>true</code> if the embedded search condition is valid
     *         <code>false</code> otherwise
     *@see OwSearchCondition#isValid() 
     */
    public boolean isValid()
    {
        return m_searchCondition.isValid();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (m_searchCondition.isValid())
        {
            return m_searchCondition.getColumnQualifiers();
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

    public boolean isXCondition()
    {
        return m_searchCondition.isXCondition();
    }

}
