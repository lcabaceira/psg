package com.wewebu.ow.csqlc.ast;

import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;select sublist&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwSelectSublist implements OwSelectList
{
    public static final String AS = "AS";

    private OwValueExpression m_valueExpression;

    private String m_columnName;

    /**
     * Constructor
     * @param valueExpression_p the value expression of this select column 
     */
    public OwSelectSublist(OwValueExpression valueExpression_p)
    {
        this(valueExpression_p, null);
    }

    /**
     * 
     *@param valueExpression_p the value expression of this select column 
     * @param columnName_p the "AS" column name , can be null
     */
    public OwSelectSublist(OwValueExpression valueExpression_p, String columnName_p)
    {
        m_valueExpression = valueExpression_p;
        m_columnName = columnName_p;
    }

    public StringBuilder createSelectListSQLString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(m_valueExpression.createValueExpressionSQLString());

        if (m_columnName != null)
        {
            builder.append(" ");
            builder.append(AS);
            builder.append(" ");
            builder.append(m_columnName);
        }

        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return m_valueExpression.getColumnQualifiers();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.csqlc.ast.OwSelectList#contains(java.lang.String, java.lang.String)
     */
    @Override
    public boolean containsColumnReference(String tableName, String columnReferenceName)
    {
        if (this.m_valueExpression instanceof OwColumnReference)
        {
            OwColumnReference columnReference = (OwColumnReference) this.m_valueExpression;
            String targetTable = columnReference.getQualifier().getTargetTable();
            return targetTable.equals(tableName) && columnReference.getColumnName().equals(columnReferenceName);
        }
        return false;
    }
}
