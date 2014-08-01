package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;where clause&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwWhereClause
{
    public static final String WHERE = "WHERE";

    private OwSearchCondition m_searchCondition;

    /**
     * Constructor
     * @param searchCondition_p the <b>search condition</b> non-terminal , must not be null
     */
    public OwWhereClause(OwSearchCondition searchCondition_p)
    {
        super();
        this.m_searchCondition = searchCondition_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal
     *         the <b>search condition</b> is omitted if invalid (see {@link OwSearchCondition#isValid()})
     */
    public StringBuilder createWhereClauseSQLString()
    {
        if (m_searchCondition.isValid() && !m_searchCondition.isXCondition())
        {
            StringBuilder builder = m_searchCondition.createSearchConditionSQLString();
            builder.insert(0, " ");
            builder.insert(0, WHERE);
            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }

    public boolean isValid()
    {
        return m_searchCondition.isValid();
    }

    public boolean isXClause()
    {
        return m_searchCondition.isXCondition();
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

}
