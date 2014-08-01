package com.wewebu.ow.csqlc.ast;

import java.util.Arrays;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;column reference&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwColumnReference implements OwValueExpression
{
    private OwColumnQualifier m_qualifier;
    private String m_columnName;

    /**
     * Constructor
     * @param qualifier_p the String value of the qualifier terminal , can be null
     * @param columnName_p the String value of the column name terminal
     */
    public OwColumnReference(OwColumnQualifier qualifier_p, String columnName_p)
    {
        super();
        this.m_qualifier = qualifier_p;
        this.m_columnName = columnName_p;
    }

    public StringBuilder createValueExpressionSQLString()
    {
        StringBuilder builder = new StringBuilder();
        if (m_qualifier != null)
        {
            if (m_qualifier.getQualifierString() != null)
            {
                builder.append(m_qualifier.getQualifierString());
                builder.append(".");
            }
            //            else
            //            {
            //                builder.append("?" + m_qualifier.getTargetTable() + "?");
            //                builder.append(".");
            //            }

        }
        builder.append(createColumnName());
        return builder;
    }

    protected String createColumnName()
    {
        return m_columnName;
    }

    public OwColumnQualifier getQualifier()
    {
        return m_qualifier;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (m_qualifier == null)
        {
            return Arrays.asList(new OwColumnQualifier[] {});
        }
        else
        {
            return Arrays.asList(new OwColumnQualifier[] { m_qualifier });
        }
    }

    @Override
    public int hashCode()
    {
        return m_columnName.hashCode();
    }

    @Override
    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwColumnReference)
        {
            OwColumnReference refObj = (OwColumnReference) obj_p;

            if (refObj.m_columnName.equals(m_columnName))
            {
                if (m_qualifier == null)
                {
                    return refObj.m_qualifier == null;
                }
                else
                {
                    return m_qualifier.equals(refObj.m_qualifier);
                }
            }
        }

        return false;

    }

    @Override
    public String toString()
    {
        String str = m_columnName + "@";
        if (m_qualifier != null)
        {
            str += m_qualifier;
        }
        else
        {
            str += "?";
        }

        return str;
    }

    public String getColumnName()
    {
        return m_columnName;
    }
}
