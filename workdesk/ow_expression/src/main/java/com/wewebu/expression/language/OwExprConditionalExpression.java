package com.wewebu.expression.language;

/**
 *<p>
 * The short conditional expression parsing result :<br/>
 * <code>1+b>a?'ok':'nok'</code><br/>
 * <code>1+b>a</code> is the test expression<br/>
 * <code>'ok'</code> is the true expression<br/>
 * <code>'nok'</code> is the false expression<br/>
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
public class OwExprConditionalExpression extends OwExprExpression
{
    /**The test expression*/
    private OwExprExpression m_test;

    /**The true branch expression - will be evaluated if {@link #m_test} is TRUE*/
    private OwExprExpression m_trueExpression;

    /**The false branch expression - will be evaluated if {@link #m_test} is FALSE*/
    private OwExprExpression m_falseExpression;

    /**
     * Constructor
     * @param symbolTable_p the global symbol table
     * @param errorTable_p the global error table
     * @param test_p the test expression
     * @param trueExpression_p the true branch expression
     * @param falseExpression_p the false branch expression
     */
    public OwExprConditionalExpression(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, OwExprExpression test_p, OwExprExpression trueExpression_p, OwExprExpression falseExpression_p)
    {
        super(symbolTable_p, errorTable_p);
        this.m_test = test_p;
        this.m_trueExpression = trueExpression_p;
        this.m_falseExpression = falseExpression_p;
        initType();
    }

    /**
     * Evaluates this expression :<br> 
     * If the {@link #m_test} expression results in a {@link OwExprBooleanValue} equal to {@link OwExprBooleanValue#TRUE} than the 
     * {@link #m_trueExpression} is evaluated end the result returned otherwise the {@link #m_falseExpression} expression is evaluated 
     * and the result returned.
     * @param scope_p 
     * @return the short conditional evaluation result
     * @throws OwExprEvaluationException
     */
    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        OwExprValue value = m_test.evaluate(scope_p);
        if (OwExprBooleanValue.value(true).equals(value))
        {
            return m_trueExpression.evaluate(scope_p);
        }
        else
        {
            return m_falseExpression.evaluate(scope_p);
        }
    }

    /**
    * Type check method.
    * If type errors are encountered during type computation the {@link #m_typeError} should
    * be set to <code>true</code> .
    * The type check is also forced on all dependent expressions {@link #m_test},{@link #m_trueExpression} and {@link #m_falseExpression}.
    * @return the expression type for this expression
    * @throws OwExprTypeMissmatchException if type errors are encountered during type computation  
    */
    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        boolean nofFullyRegressed = true;
        OwExprExpressionType testType = null;
        OwExprExpressionType trueType = null;
        OwExprExpressionType falseType = null;
        while (nofFullyRegressed)
        {
            testType = m_test.type();
            trueType = m_trueExpression.type();
            falseType = m_falseExpression.type();

            nofFullyRegressed = testType.regressTo(OwExprType.BOOLEAN);
            nofFullyRegressed |= trueType.regressTo(falseType.getInferringTypes());
            nofFullyRegressed |= falseType.regressTo(trueType.getInferringTypes());
        }
        return new OwExprConditionalExpressionType(testType, trueType, falseType);

    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(m_test.toString());
        sb.append("?");
        sb.append(m_trueExpression.toString());
        sb.append(":");
        sb.append(m_falseExpression.toString());

        return sb.toString();
    }
}
