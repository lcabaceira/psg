package com.wewebu.ow.csqlc.ast;

import static com.wewebu.ow.csqlc.ast.OwJoinType.DEFAULT_JOIN_TYPE;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;joined table&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwJoinedTable implements OwTableReference
{
    public static final String JOIN = "JOIN";

    private OwTableReference m_tableReference1;
    private OwJoinType m_joinType = DEFAULT_JOIN_TYPE;
    private OwTableReference m_tableReference2;
    private OwJoinSpecification m_joinSpecification;

    /**
     * Constructor - default join type
     * @param tableReference1_p the left side table reference 
     * @param tableReference2_p the right side table reference
     * @param joinSpecification_p the join specification
     */
    public OwJoinedTable(OwTableReference tableReference1_p, OwTableReference tableReference2_p, OwJoinSpecification joinSpecification_p)
    {
        this(tableReference1_p, DEFAULT_JOIN_TYPE, tableReference2_p, joinSpecification_p);
    }

    /**
     * Constructor
     * @param tableReference1_p the left side table reference 
     * @param joinType_p the join type
     * @param tableReference2_p the right side table reference
     * @param joinSpecification_p the join specification
     */
    public OwJoinedTable(OwTableReference tableReference1_p, OwJoinType joinType_p, OwTableReference tableReference2_p, OwJoinSpecification joinSpecification_p)
    {
        super();
        m_tableReference1 = tableReference1_p;
        m_joinType = joinType_p;
        m_tableReference2 = tableReference2_p;
        m_joinSpecification = joinSpecification_p;
    }

    public StringBuilder createTableReferenceSQLString()
    {
        StringBuilder builder = m_tableReference1.createTableReferenceSQLString();
        builder.append(m_joinType.getSqlString());
        builder.append(" ");
        builder.append(m_joinSpecification.getType());
        builder.append(JOIN);
        builder.append(" ");
        builder.append(m_tableReference2.createTableReferenceSQLString());
        builder.append(" ");
        builder.append(m_joinSpecification.createJoinSpectificationSQLString());
        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>(m_tableReference1.getColumnQualifiers());
        qualifiers.addAll(m_tableReference2.getColumnQualifiers());
        qualifiers.addAll(m_joinSpecification.getColumnQualifiers());
        return qualifiers;
    }

    public OwColumnQualifier getMainTableQualifier()
    {
        return m_tableReference1.getMainTableQualifier();
    }

}
