package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;simple table&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwSimpleTable
{
    public static final String SELECT = "SELECT";

    /**
     * Default constant to be used for 
     * SQL &quot;TOP n&quot; handling
     * @since 3.2.0.2
     */
    public static final String TOP = "TOP";

    private OwSelectList m_selectList;

    private OwFromClause m_fromClause;

    private OwWhereClause m_whereClause;

    private Integer maxRows;

    /**
     * Constructor
     * @param selectList_p the <b>select list</b> non-terminal, must not be null 
     * @param fromClause_p the <b>from clause</b> non-terminal, must not be null
     */
    public OwSimpleTable(OwSelectList selectList_p, OwFromClause fromClause_p)
    {
        this(selectList_p, fromClause_p, null, null);
    }

    /**
     * Constructor
     * @param selectList_p the <b>select list</b> non-terminal, must not be null 
     * @param fromClause_p the <b>from clause</b> non-terminal, must not be null
     * @param maxRows Integer the maximum rows to return can be null
     * @since 3.2.0.2
     */
    public OwSimpleTable(OwSelectList selectList_p, OwFromClause fromClause_p, Integer maxRows)
    {
        this(selectList_p, fromClause_p, null, maxRows);
    }

    /**
     * Constructor
     * @param selectList_p the <b>select list</b> non-terminal , must not be null 
     * @param fromClause_p the <b>from clause</b> non-terminal , must not be null 
     * @param whereClause_p the <b>simple table</b> non-terminal , can be null 
     */
    public OwSimpleTable(OwSelectList selectList_p, OwFromClause fromClause_p, OwWhereClause whereClause_p)
    {
        super();
        m_selectList = selectList_p;
        m_fromClause = fromClause_p;
        m_whereClause = whereClause_p;
    }

    /**
     * Constructor
     * @param selectList_p the <b>select list</b> non-terminal , must not be null 
     * @param fromClause_p the <b>from clause</b> non-terminal , must not be null 
     * @param whereClause_p the <b>simple table</b> non-terminal , can be null
     * @param maxRows Integer size of maximum rows, can be null
     * @since 3.2.0.2
     */
    public OwSimpleTable(OwSelectList selectList_p, OwFromClause fromClause_p, OwWhereClause whereClause_p, Integer maxRows)
    {
        super();
        m_selectList = selectList_p;
        m_fromClause = fromClause_p;
        m_whereClause = whereClause_p;
        this.maxRows = maxRows;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal
     *         the where clause will be omitted if null or invalid (see {@link OwWhereClause#isValid()}) 
     */
    public StringBuilder createSimpleTableSQLString()
    {
        StringBuilder builder = m_selectList.createSelectListSQLString();
        builder.insert(0, " ");
        builder.insert(0, SELECT);
        builder.append(" ");
        builder.append(m_fromClause.createFromClauseSQLString());
        if (m_whereClause != null && m_whereClause.isValid())
        {
            builder.append(" ");
            builder.append(m_whereClause.createWhereClauseSQLString());
        }

        return addRowLimit(builder);
    }

    /**(overridable)
     * Handle limitation in SQL-statement, method is called
     * at the end of {@link #createSimpleTableSQLString()}.
     * <p>By default the SQL &quot;TOP n&quot; will be 
     * inserted into statement: <code> SELECT TOP 50 ....</code></p>
     * @param statement StringBuilder the current created statement
     * @return StringBuilder which may contains row limitation
     * @see #createSimpleTableSQLString()
     * @see #getMaxRows()
     * @since 3.2.0.2
     */
    protected StringBuilder addRowLimit(StringBuilder statement)
    {
        if (this.maxRows != null)
        {
            String limit = new StringBuilder(" ").append(TOP).append(" ").append(maxRows).toString();

            statement.insert(SELECT.length(), limit);
        }
        return statement;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = null;

        if (m_whereClause != null)
        {
            qualifiers = new LinkedList<OwColumnQualifier>(m_whereClause.getColumnQualifiers());
        }
        else
        {
            qualifiers = new LinkedList<OwColumnQualifier>();
        }
        qualifiers.addAll(m_fromClause.getColumnQualifiers());
        if (m_selectList != null)
        {
            qualifiers.addAll(m_selectList.getColumnQualifiers());
        }

        return qualifiers;
    }

    public OwColumnQualifier getMainTableQualifier()
    {
        return m_fromClause.getMainTableQualifier();
    }

    public void addJoin(OwCorrelatedTableName joinedTableReference_p, OwJoinSpecification joinSpec_p)
    {
        m_fromClause.addJoin(joinedTableReference_p, joinSpec_p);
    }

    /**
     * Return current max row definition.
     * @return Integer or null if not set
     * @since 3.2.0.2
     */
    public Integer getMaxRows()
    {
        return maxRows;
    }

    /**
     * Set the max row definition. 
     * <p>Will not check for negative values.</p>
     * @param maxRows Integer or null
     * @since 3.2.0.2
     */
    public void setMaxRows(Integer maxRows)
    {
        this.maxRows = maxRows;
    }

    public OwSelectList getSelectList()
    {
        return this.m_selectList;
    }
}
