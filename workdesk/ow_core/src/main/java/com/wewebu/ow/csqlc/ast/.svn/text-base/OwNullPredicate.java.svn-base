package com.wewebu.ow.csqlc.ast;

import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;null predicatet&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwNullPredicate extends OwPredicate
{

    public static final String IS_NULL = "IS NULL";
    public static final String IS_NOT_NULL = "IS NOT NULL";

    private String m_predicateString = IS_NULL;

    private OwColumnReference m_columnReference;

    /**
     * Constructor 
     * @param columnReference_p the column reference operand, must not be null
     */
    public OwNullPredicate(OwColumnReference columnReference_p, boolean negated_p)
    {
        super();
        m_columnReference = columnReference_p;
        m_predicateString = negated_p ? IS_NOT_NULL : IS_NULL;
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        StringBuilder builder = m_columnReference.createValueExpressionSQLString();
        builder.append(" ");
        builder.append(m_predicateString);
        return builder;
    }

    /**
     * 
     * @return always <code>true</code>
     */
    public boolean isValid()
    {
        return true;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return m_columnReference.getColumnQualifiers();
    }

}
