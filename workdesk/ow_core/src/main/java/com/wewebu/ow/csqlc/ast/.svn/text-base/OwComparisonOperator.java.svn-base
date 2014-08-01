package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * The SQL comparison operator enumeration.
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
public enum OwComparisonOperator
{
    /**The <b>=</b> operator */
    EQ("="),
    /**The <b>&lt;&gt;</b> operator */
    NEQ("<>"),
    /**The <b>&lt;</b> operator */
    LT("<"),
    /**The <b>&gt;</b> operator */
    GT(">"),
    /**The <b>&lt;=</b> operator */
    LEQ("<="),
    /**The <b>&gt;=</b> operator */
    GEQ(">=");

    private String m_opSQLString;

    OwComparisonOperator(String opSQLString_p)
    {
        m_opSQLString = opSQLString_p;
    }

    /**
     * 
     * @return the SQL String representation of this operator 
     */
    public String getSQLString()
    {
        return m_opSQLString;
    }
}
