package com.wewebu.expression.language;

/**
 *<p>
 * Binary expression implementation (a left-operand-operator-right-operand expression).   
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
 */
public class OwExprBinaryExpression extends OwExprExpression
{
    /**Left side opernd*/
    private OwExprExpression m_operand1;
    /**Operator*/
    private OwExprBinaryOperator m_operator;
    /**Right side operand*/
    private OwExprExpression m_operand2;

    /**
     * Constructor
     * @param symTable_p global symbol table
     * @param errorTable_p global error table
     * @param operand1_p left side operand
     * @param operator_p operator
     * @param operand2_p right side operand
     */
    public OwExprBinaryExpression(OwExprSymbolTable symTable_p, OwExprErrorTable errorTable_p, OwExprExpression operand1_p, OwExprBinaryOperator operator_p, OwExprExpression operand2_p)
    {
        super(symTable_p, errorTable_p);
        this.m_operand1 = operand1_p;
        this.m_operator = operator_p;
        this.m_operand2 = operand2_p;

        initType();
    }

    /**
     * Evaluates this expression on the {@link OwExprScope} (scope) provided as argument.
     * @param scope_p external scope to be used during evaluation
     * @return the value resulted from the evaluation of this expression 
     * @throws OwExprEvaluationException in case of evaluation failure
     */
    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        OwExprValue v1 = m_operand1.evaluate(scope_p);
        OwExprValue v2 = m_operand2.evaluate(scope_p);

        return m_operator.binaryEvaluation(scope_p, v1, v2);
    }

    /**
     * Type check method.
     * If type errors are encountered during type computation the {@link #m_typeError} should
     * be set to <code>true</code> 
     * @return the expression type for this expression
     * @throws OwExprTypeMissmatchException if type errors are ecountrede during type computation  
     */
    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        if (m_typeError)
        {
            return OwExprExpressionType.NOTYPE;
        }
        else
        {
            return m_operator.computeExpressionType(m_operand1, m_operand2);
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(m_operand1.toString());
        sb.append(m_operator.toString());
        sb.append(m_operand2.toString());
        return sb.toString();
    }

}
