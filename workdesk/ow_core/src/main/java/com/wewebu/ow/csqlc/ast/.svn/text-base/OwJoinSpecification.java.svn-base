package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;join specification&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwJoinSpecification
{
    public static final String DEFAULT = "";
    public static final String INNER = "INNER ";
    public static final String LEFT = "LEFT ";
    public static final String LEFT_OUTER = "LEFT OUTER ";
    public static final String RIGHT = "RIGHT ";
    public static final String RIGHT_OUTER = "RIGHT OUTER ";
    public static final String FULL = "FULL ";
    public static final String FULL_OUTER = "FULL OUTER ";

    private static final String ON = "ON";

    private OwColumnReference m_columnReference1;
    private OwColumnReference m_columnReference2;
    private String m_type;

    /**
     * Constructor
     * @param columnReference1_p
     * @param columnReference2_p
     */
    public OwJoinSpecification(OwColumnReference columnReference1_p, OwColumnReference columnReference2_p, String type_p)
    {
        super();
        m_columnReference1 = columnReference1_p;
        m_columnReference2 = columnReference2_p;
        m_type = type_p;
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the SQL String representation of this join specification
     */
    public StringBuilder createJoinSpectificationSQLString()
    {
        StringBuilder builder = m_columnReference1.createValueExpressionSQLString();
        builder.insert(0, " ");
        builder.insert(0, ON);
        builder.append("=");
        builder.append(m_columnReference2.createValueExpressionSQLString());
        return builder;
    }

    public String getType()
    {
        return m_type;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>(m_columnReference1.getColumnQualifiers());
        qualifiers.addAll(m_columnReference1.getColumnQualifiers());

        return qualifiers;
    }
}
