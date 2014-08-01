package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST base node : &lt;content based retrieval&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwTextSearchPredicate extends OwPredicate
{
    protected OwColumnQualifier contentJoinedColumn;
    protected OwColumnReference columnReference;
    private OwCharacterStringLiteral searchExpression;
    private String dialect;

    /**
     * Constructor for a text search predicate where every parameter can be null,
     * but for a valid predicate at least the search expression should be a 
     * non-null and non-empty element. 
     * @param searchExpression_p OwCharacterStringLiteral (can be null)
     * @param columnReference_p OwColumnReference (can be null)
     * @param contentJoinedColumn_p OwColumnQualifier (can be null)
     * @param dialect_p (can be null)
     */
    public OwTextSearchPredicate(OwCharacterStringLiteral searchExpression_p, OwColumnReference columnReference_p, OwColumnQualifier contentJoinedColumn_p, String dialect_p)
    {
        this.searchExpression = searchExpression_p;
        this.columnReference = columnReference_p;
        this.dialect = dialect_p;
        this.contentJoinedColumn = contentJoinedColumn_p;

    }

    public boolean isValid()
    {
        return getSearchExpression() != null && !getSearchExpression().isNull();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (isValid())
        {
            List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>();
            if (contentJoinedColumn != null)
            {
                qualifiers.add(contentJoinedColumn);
            }
            if (columnReference != null)
            {
                qualifiers.addAll(columnReference.getColumnQualifiers());
            }
            return qualifiers;
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        StringBuilder builder = getSearchExpression().createLiteralSQLString();

        if (getDialect() != null)
        {
            builder.append(",");
            builder.append(getDialect());
        }

        if (columnReference != null)
        {
            builder.insert(0, ",");
            builder.insert(0, columnReference.createValueExpressionSQLString());
        }
        else if (contentJoinedColumn != null && contentJoinedColumn.getQualifierString() != null)
        {
            builder.insert(0, ",");
            builder.insert(0, contentJoinedColumn.getQualifierString());
        }

        builder.insert(0, "CONTAINS(");
        builder.append(")");

        return builder;
    }

    /**
     * Return the current search expression, defined during instantiation. 
     * @return OwCharacterStringLiteral or null
     */
    protected OwCharacterStringLiteral getSearchExpression()
    {
        return this.searchExpression;
    }

    /**
     * Get the defined dialect,
     * which was provided through constructor.
     * @return String or null
     */
    protected String getDialect()
    {
        return this.dialect;
    }

}
