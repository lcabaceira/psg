package com.wewebu.ow.csqlc.ast;

import java.util.Arrays;
import java.util.List;

/**
 *<p>
 * SQL AST node : a table name with correlation syntax non-terminal as defined by the SQL grammar.<br>
 * &lt;table name&gt[[AS] &lt;correlation name&gt;];
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
public class OwCorrelatedTableName implements OwTableReference
{
    public static final String AS = "AS";

    private String m_tableName;

    private OwColumnQualifier m_correlationQualifier;

    //    public OwCorrelatedTableName(String tableName_p, String typeName_p)
    //    {
    //        this(tableName_p, new OwColumnQualifier(tableName_p, typeName_p));
    //    }

    /**
     * Constructor
     * @param tableName_p the &lt;table name&gt; terminal String representation 
     */
    public OwCorrelatedTableName(String tableName_p, OwColumnQualifier qualifier_p)
    {
        m_tableName = tableName_p;
        m_correlationQualifier = qualifier_p;
    }

    public StringBuilder createTableReferenceSQLString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(m_tableName);
        if (m_correlationQualifier.getQualifierString() != null)
        {
            builder.append(" ");
            builder.append(AS);
            builder.append(" ");
            builder.append(m_correlationQualifier.getQualifierString());
        }
        return builder;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return Arrays.asList(new OwColumnQualifier[] { m_correlationQualifier });
    }

    public OwColumnQualifier getMainTableQualifier()
    {
        return m_correlationQualifier;
    }

}
