package com.wewebu.ow.csqlc.ast;

import static com.wewebu.ow.csqlc.ast.OwComparisonOperator.GEQ;
import static com.wewebu.ow.csqlc.ast.OwComparisonOperator.GT;
import static com.wewebu.ow.csqlc.ast.OwComparisonOperator.LEQ;
import static com.wewebu.ow.csqlc.ast.OwComparisonOperator.LT;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : between predicate implementation.<br/>
 * An aggregated predicate : a greater than and less than search condition composition.<br/>
 * Example : <br/>   
 * <b>(value>2123 AND value<7789)</b><br/>
 * <b>(value<'foo' OR value>'bar')</b> for the negated form (not between). <br/> 
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
public class OwBetweenPredicate extends OwPredicate
{
    private OwLiteral m_value1;
    private OwLiteral m_value2;
    private OwColumnReference m_columnReference;
    private boolean m_negate;

    /**
     * 
     * @param columnReference_p the left column reference operand, must not be null
     * @param value1_p first value right operand literal , can be null
     * @param value2_p second value right operand literal , can be null
     */
    public OwBetweenPredicate(OwColumnReference columnReference_p, OwLiteral value1_p, OwLiteral value2_p)
    {
        this(columnReference_p, value1_p, value2_p, false);
    }

    public OwBetweenPredicate(OwColumnReference columnReference_p, OwLiteral value1_p, OwLiteral value2_p, boolean negate_p)
    {
        super();
        this.m_value1 = value1_p;
        this.m_value2 = value2_p;
        this.m_columnReference = columnReference_p;
        this.m_negate = negate_p;
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        if (isValid())
        {
            OwComparisonOperator v1Operator = m_negate ? LT : GEQ;
            OwComparisonOperator v2Operator = m_negate ? GT : LEQ;

            OwComparisonPredicate v1ComparisonPredicate = null;
            OwComparisonPredicate v2ComparisonPredicate = null;
            OwComparisonPredicate uniqueComparisonPredicate = null;

            if (m_value1 != null && !m_value1.isNull())
            {
                v1ComparisonPredicate = new OwComparisonPredicate(m_columnReference, v1Operator, m_value1);
            }
            if (m_value2 != null && !m_value2.isNull())
            {
                v2ComparisonPredicate = new OwComparisonPredicate(m_columnReference, v2Operator, m_value2);
            }

            if (v2ComparisonPredicate == null)
            {
                uniqueComparisonPredicate = v1ComparisonPredicate;
            }
            else if (v1ComparisonPredicate == null)
            {
                uniqueComparisonPredicate = v2ComparisonPredicate;
            }

            if (uniqueComparisonPredicate != null)
            {
                return uniqueComparisonPredicate.createPredicateSQLString();
            }
            else
            {
                OwSearchCondition betweenInnerTerm;

                if (m_negate)
                {
                    betweenInnerTerm = new OwORSearchCondition(v1ComparisonPredicate, v2ComparisonPredicate);
                }
                else
                {
                    betweenInnerTerm = new OwANDBooleanTerm(v1ComparisonPredicate, v2ComparisonPredicate);
                }

                OwSearchConditionBooleanTest betweenTest = new OwSearchConditionBooleanTest(betweenInnerTerm);

                return betweenTest.createBooleanTestSQLString();
            }
        }
        else
        {
            return new StringBuilder();
        }
    }

    /**
     * 
     * @return <code>true</code> if both right operand values are not null and not null-valued<br>
     *         <code>false</code> otherwise 
     * @see OwLiteral#isNull()
     */
    public boolean isValid()
    {
        return (m_value1 != null && !m_value1.isNull()) || (m_value2 != null && !m_value2.isNull());
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

}
