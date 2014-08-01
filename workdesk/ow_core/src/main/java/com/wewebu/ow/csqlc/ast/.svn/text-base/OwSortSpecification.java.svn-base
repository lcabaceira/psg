package com.wewebu.ow.csqlc.ast;

import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;sort specification&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwSortSpecification
{
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private String m_orderDirection = null;
    private OwColumnReference m_columnReference;

    /**
     * Constructor
     * @param columnReferences_p
     */
    public OwSortSpecification(OwColumnReference columnReferences_p)
    {
        this.m_columnReference = columnReferences_p;
    }

    /**
     * Constructor
     * @param columnReferences_p
     * @param ascending_p <code>true</code> for an "ASC" sort specification<br>
     *                    <code>false</code>  for a "DESC" sort specification
     */
    public OwSortSpecification(OwColumnReference columnReferences_p, boolean ascending_p)
    {
        this.m_orderDirection = ascending_p ? ASC : DESC;
        this.m_columnReference = columnReferences_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the String representation of this sort specification
     */
    public StringBuilder createSortSpecificationSQLString()
    {
        StringBuilder builder = new StringBuilder();
        if (m_columnReference != null)
        {
            builder.append(m_columnReference.createValueExpressionSQLString());
            builder.append(" ");
            builder.append(m_orderDirection);
        }
        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return m_columnReference.getColumnQualifiers();
    }
}
