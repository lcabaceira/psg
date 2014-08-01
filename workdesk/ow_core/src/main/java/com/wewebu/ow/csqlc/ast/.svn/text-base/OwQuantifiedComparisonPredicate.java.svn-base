package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;quantified comparison predicate&gt; based on syntax non-terminal 
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
public class OwQuantifiedComparisonPredicate extends OwFormatPredicate
{
    private OwColumnReference m_columnReference;
    private OwLiteral m_comparisonLiteral;

    /**
     * Constructor
     * @param comparisonLiteral_p the comparison literal 
     * @param columnReference_p the multi value column reference operand, must not be null
     * @param negated_p <code>true</code> for a negated operand (ie. NOT literal = ANY multi-valued-column-reference)
     *                  <code>false</code> otherwise 
     */
    public OwQuantifiedComparisonPredicate(OwLiteral comparisonLiteral_p, OwColumnReference columnReference_p, boolean negated_p, OwPredicateFormat format_p)
    {
        super(format_p, negated_p);
        m_columnReference = columnReference_p;
        m_comparisonLiteral = comparisonLiteral_p;

    }

    //    @Override
    //    public StringBuilder createPredicateSQLString()
    //    {
    //        if (isValid())
    //        {
    //            StringBuilder builder = m_columnReference.createValueExpressionSQLString();
    //            builder.insert(0, " ");
    //            //            builder.insert(0, ANY);
    //            //            builder.insert(0, " ");
    //            //            builder.insert(0, EQUAL);
    //            builder.insert(0, m_operator);
    //            builder.insert(0, " ");
    //            builder.insert(0, m_comparisonLiteral.createLiteralSQLString());
    //            if (m_negate)
    //            {
    //                builder.insert(0, " ");
    //                builder.insert(0, NOT);
    //            }
    //            return builder;
    //        }
    //        else
    //        {
    //            return new StringBuilder();
    //        }
    //    }

    /**
     * 
     * @return <code>true</code> if literal operand and column reference is valid
     *         <code>false</code> otherwise
     * @see OwInValueList#isValid()
     */
    public boolean isValid()
    {
        return m_comparisonLiteral != null && m_columnReference != null && !m_comparisonLiteral.isNull();
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
        return m_comparisonLiteral.createLiteralSQLString();
    }

    @Override
    protected StringBuilder createRightOperand()
    {
        return m_columnReference.createValueExpressionSQLString();
    }

}