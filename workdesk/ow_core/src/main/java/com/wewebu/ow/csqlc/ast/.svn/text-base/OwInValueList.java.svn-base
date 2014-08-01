package com.wewebu.ow.csqlc.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;in value list&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwInValueList
{
    private List<OwLiteral> m_literals = new ArrayList<OwLiteral>();

    /**
     * Adds the given literal to this list of literals
     * @param literal_p literal to add, must not be null 
     */
    public void addLiteral(OwLiteral literal_p)
    {
        m_literals.add(literal_p);
    }

    /**
     * 
     * @return <code>true</code> if this list of literals is not empty and at least one of the literas is not null-valued
     *         <code>false</code> otherwise
     * @see OwLiteral#isNull() 
     */
    public boolean isValid()
    {
        if (m_literals.isEmpty())
        {
            return false;
        }
        else
        {
            for (OwLiteral literal : m_literals)
            {
                if (!literal.isNull())
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 
     * @return a {@link StringBuilder} filled with the String representation of this in value list;
     *         null-valued literals are omitted 
     *@see OwLiteral#isNull()                    
     */
    public StringBuilder createInValueListSQLString()
    {
        if (isValid())
        {

            OwLiteral literal0 = m_literals.get(0);
            StringBuilder builder = new StringBuilder(literal0.createLiteralSQLString());
            builder.insert(0, "(");
            for (int i = 1; i < m_literals.size(); i++)
            {
                OwLiteral literal = m_literals.get(i);
                if (!literal.isNull())
                {
                    builder.append(",");
                    builder.append(literal.createLiteralSQLString());
                }
            }
            builder.append(")");
            return builder;

        }
        else
        {
            throw new NullPointerException("Can not create in value lists of invalid values!");
        }
    }

}
