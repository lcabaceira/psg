package com.wewebu.ow.csqlc.ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;order by clause&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwOrderByClause
{
    public static final String ORDER_BY = "ORDER BY";

    private List<OwSortSpecification> m_sortSpecifications = new ArrayList<OwSortSpecification>();

    /**
     * Adds the given sort specification to this order by clause
     * @param sortSpecification_p 
     */
    public void add(OwSortSpecification sortSpecification_p)
    {
        m_sortSpecifications.add(sortSpecification_p);
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the String representation of this order by clause;
     *         an empty {@link String} for an invalid order by clause 
     * 
     */
    public StringBuilder createOrderByClauseSQLString()
    {
        if (!m_sortSpecifications.isEmpty())
        {
            OwSortSpecification sortSpecification = m_sortSpecifications.get(0);
            StringBuilder builder = sortSpecification.createSortSpecificationSQLString();
            builder.insert(0, " ");
            builder.insert(0, ORDER_BY);
            for (int i = 1; i < m_sortSpecifications.size(); i++)
            {
                OwSortSpecification specification = m_sortSpecifications.get(i);

                builder.append(",");
                builder.append(specification.createSortSpecificationSQLString());
            }
            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }

    /**
     * 
     * @return <code>true</code> if this order by clause contains at least one sort specification<br>
     *         <code>false</code> otherwise
     */
    public boolean isValid()
    {
        return !m_sortSpecifications.isEmpty();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>();
        if (isValid())
        {
            for (OwSortSpecification sort : m_sortSpecifications)
            {
                qualifiers.addAll(sort.getColumnQualifiers());
            }
        }

        return qualifiers;
    }
}
