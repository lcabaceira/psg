package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;like predicate&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwLikePredicate extends OwFormatPredicate
{
    public static final String LIKE = "LIKE";
    public static final String NOT_LIKE = "NOT LIKE";
    public static final String NOT = "NOT";

    private OwColumnReference m_columnReference;
    private OwCharacterStringLiteral m_characterStringLiteral;

    /**
     * Constructor 
     * @param columnReference_p the column reference operand, must not be null
     * @param characterStringLiteral_p the literal operand , must not be null , can be null-valued 
     * @see OwLiteral#isNull()
     */
    public OwLikePredicate(OwColumnReference columnReference_p, OwCharacterStringLiteral characterStringLiteral_p, OwPredicateFormat format_p)
    {
        this(columnReference_p, characterStringLiteral_p, false, format_p);
    }

    /**
     * Constructor
     * @param columnReference_p  the column reference operand, must not be null
     * @param characterStringLiteral_p the literal operand , must not be null , can be null-valued
     * @param negated_p <code>true</code> for a negated operand (eg. NOT LIKE)
     *                  <code>false</code> otherwise (eg. LIKE)
     */
    public OwLikePredicate(OwColumnReference columnReference_p, OwCharacterStringLiteral characterStringLiteral_p, boolean negated_p, OwPredicateFormat format_p)
    {
        super(format_p, negated_p);
        m_columnReference = columnReference_p;
        m_characterStringLiteral = characterStringLiteral_p;
        if (!m_characterStringLiteral.isNull())
        {
            if (!m_characterStringLiteral.contains('%', false))
            {
                m_characterStringLiteral = m_characterStringLiteral.insert(0, "%").append("%");
            }
        }
    }

    /**
     * 
     * @return <code>true</code> if character srting literal operand is null-valued
     * @see OwLiteral#isNull()
     */
    public boolean isValid()
    {
        return !m_characterStringLiteral.isNull();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (isValid())
        {
            return m_columnReference.getColumnQualifiers();
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

    @Override
    protected StringBuilder createLeftOperand()
    {
        return m_columnReference.createValueExpressionSQLString();
    }

    @Override
    protected StringBuilder createRightOperand()
    {
        return m_characterStringLiteral.createLiteralSQLString();
    }

}
