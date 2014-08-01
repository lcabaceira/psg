package com.wewebu.ow.csqlc.ast;

import java.util.Arrays;
import java.util.List;

/**
 *<p>
 * SQL AST node : an select all (egg. SELECT * FROM document) implementation of the 
 * &lt;select list&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwSelectAll implements OwSelectList
{
    public static final String ALL_WILDCARD = "*";

    private OwColumnQualifier m_qualifier;

    /**
     * Constructor
     */
    public OwSelectAll()
    {
        this(new OwColumnQualifier());
    }

    /**
     * Constructor
     * @param qualifier_p
     */
    public OwSelectAll(OwColumnQualifier qualifier_p)
    {
        super();
        this.m_qualifier = qualifier_p;
    }

    public StringBuilder createSelectListSQLString()
    {
        StringBuilder builder = new StringBuilder();
        if (m_qualifier != null && m_qualifier.getQualifierString() != null)
        {
            builder.append(m_qualifier.getQualifierString());
            builder.append(".");
        }
        builder.append(ALL_WILDCARD);

        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {

        return Arrays.asList(new OwColumnQualifier[] { m_qualifier });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.csqlc.ast.OwSelectList#contains(java.lang.String, java.lang.String)
     */
    @Override
    public boolean containsColumnReference(String tableName, String columnReferenceName)
    {
        throw new RuntimeException("Not implemented yet");
    }

}
