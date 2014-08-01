package com.wewebu.ow.csqlc.ast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *<p>
 * SQL AST node : &lt;query statement&gt; syntax root non-terminal as defined by the SQL grammar.<br/>
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
public class OwQueryStatement
{
    private OwSimpleTable m_simpleTable;
    private OwOrderByClause m_orderByClause = null;

    private Map<String, OwColumnQualifier> m_normalizedQualifiers;

    private OwRepositoryTarget m_repositoryTarget;

    /**
     * Constructor
     * @param simpleTable_p the <b>simple table</b> non-terminal , must not be null 
     */
    public OwQueryStatement(OwRepositoryTarget repositoryTarget_p, OwSimpleTable simpleTable_p)
    {
        this(repositoryTarget_p, simpleTable_p, null);
    }

    /**
     * Constructor
     * @param simpleTable_p the <b>simple table</b> non-terminal , must not be null 
     * @param orderByClause_p the <b>order by</b> non-terminal , can be null 
     */
    public OwQueryStatement(OwRepositoryTarget repositoryTarget_p, OwSimpleTable simpleTable_p, OwOrderByClause orderByClause_p)
    {
        super();
        m_simpleTable = simpleTable_p;
        m_orderByClause = orderByClause_p;
        m_repositoryTarget = repositoryTarget_p;
    }

    public String getTargetRepositoryID()
    {
        return m_repositoryTarget.getRepositoryId();
    }

    public OwMergeType getMergeType()
    {
        return m_repositoryTarget.getMergeType();
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal
     *         the <b>order by clause</b> is omitted if null or invalid (see {@link OwOrderByClause#isValid()})   
     */
    public StringBuilder createSQLString()
    {
        StringBuilder builder = m_simpleTable.createSimpleTableSQLString();
        if (m_orderByClause != null && m_orderByClause.isValid())
        {
            builder.append(" ");
            builder.append(m_orderByClause.createOrderByClauseSQLString());
        }
        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = null;

        if (m_orderByClause != null)
        {
            qualifiers = new LinkedList<OwColumnQualifier>(m_orderByClause.getColumnQualifiers());
        }
        else
        {
            qualifiers = new LinkedList<OwColumnQualifier>();
        }
        qualifiers.addAll(m_simpleTable.getColumnQualifiers());

        return qualifiers;
    }

    @Override
    public String toString()
    {
        return createSQLString().toString();
    }

    public OwColumnQualifier getMainTableQualifier()
    {
        return m_simpleTable.getMainTableQualifier();
    }

    public void addJoin(OwCorrelatedTableName joinedTableReference_p, OwJoinSpecification joinSpec_p)
    {
        m_simpleTable.addJoin(joinedTableReference_p, joinSpec_p);
    }

    public void setNormalizedQualifiers(Set<OwColumnQualifier> qualifiers_p)
    {
        m_normalizedQualifiers = new HashMap<String, OwColumnQualifier>();
        for (OwColumnQualifier qualifier : qualifiers_p)
        {
            m_normalizedQualifiers.put(qualifier.getQualifierString(), qualifier);
        }
    }

    public Map<String, OwColumnQualifier> getNormalizedQualifiers()
    {
        return m_normalizedQualifiers;
    }

    public OwSimpleTable getSimpleTable()
    {
        return this.m_simpleTable;
    }
}
