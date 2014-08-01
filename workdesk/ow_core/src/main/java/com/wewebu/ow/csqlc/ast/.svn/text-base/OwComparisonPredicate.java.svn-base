package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;comparison predicate&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
public class OwComparisonPredicate extends OwPredicate
{

    private OwValueExpression m_valueExpression;
    private OwComparisonOperator m_operator;
    private OwLiteral m_literal;

    /**
     * 
     * @param valueExpression_p the <b>value expression</b> of this comparison terminal (left side operand)
     * @param operator_p the operator of this predicate
     * @param literal_p the <b>literal</b> terminal of this comparison prdicate
     */
    public OwComparisonPredicate(OwValueExpression valueExpression_p, OwComparisonOperator operator_p, OwLiteral literal_p)
    {
        super();
        this.m_valueExpression = valueExpression_p;
        this.m_operator = operator_p;
        this.m_literal = literal_p;
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        StringBuilder builder = new StringBuilder();
        if (isValid())
        {
            builder.append(m_valueExpression.createValueExpressionSQLString());
            builder.append(m_operator.getSQLString());
            builder.append(m_literal.createLiteralSQLString());
        }
        return builder;

    }

    /**
     * 
     * @return <code>false</code> if the literal terminal of this predicate is null-valued
     *         <code>true</code> otherwise
     *         
     * @see OwLiteral#isNull() 
     */
    public boolean isValid()
    {
        return !m_literal.isNull();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (isValid())
        {
            return m_valueExpression.getColumnQualifiers();
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

}
