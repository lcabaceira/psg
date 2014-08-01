package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;quantified in predicate&gt; based on syntax non-terminal 
 * defined by the SQL grammar.<br/>
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
public class OwQuantifiedInPredicate extends OwFormatPredicate
{
    private OwColumnReference m_columnReference;
    private OwInValueList m_inValueList;

    /**
     * Constructor 
     * @param columnReference_p the column reference operand, must not be null
     * @param inValueList_p the in list values literal operand , must not be null
     * @param negated_p <code>true</code> for a negated operand (ie. NOT IN)
     *                  <code>false</code> otherwise (ie. IN)
     * @see OwInValueList#isValid()
     */
    public OwQuantifiedInPredicate(OwColumnReference columnReference_p, OwInValueList inValueList_p, boolean negated_p, OwPredicateFormat format_p)
    {
        super(format_p, negated_p);
        m_columnReference = columnReference_p;
        m_inValueList = inValueList_p;
    }

    /**
     * 
     * @return <code>true</code> if character in value list literal operand is valid
     *         <code>false</code> otherwise
     * @see OwInValueList#isValid()
     */
    public boolean isValid()
    {
        return m_inValueList.isValid();
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
        return m_inValueList.createInValueListSQLString();
    }

}