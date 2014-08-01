package com.wewebu.ow.csqlc.ast;

import java.util.List;

/**
 *<p>
 *SQL AST node : &lt;from clause&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwFromClause
{
    public static final String FROM = "FROM";

    private OwTableReference m_tableReference;

    /**
     * Constructor
     * @param tableReference_p the <b>table reference</b> of this form clause
     */
    public OwFromClause(OwTableReference tableReference_p)
    {
        super();
        m_tableReference = tableReference_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the String representation of this form clause
     */
    public StringBuilder createFromClauseSQLString()
    {
        StringBuilder builder = m_tableReference.createTableReferenceSQLString();
        builder.insert(0, " ");
        builder.insert(0, FROM);
        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return m_tableReference.getColumnQualifiers();
    }

    public OwColumnQualifier getMainTableQualifier()
    {
        return m_tableReference.getMainTableQualifier();
    }

    public void addJoin(OwCorrelatedTableName joinedTableReference_p, OwJoinSpecification joinSpec_p)
    {
        m_tableReference = new OwJoinedTable(m_tableReference, joinedTableReference_p, joinSpec_p);
    }
}
