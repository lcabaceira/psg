package com.wewebu.expression.language;

import java.util.Iterator;
import java.util.List;

/**
 *<p>
 * An function arguments suffix of a primary expression.<br/>
 * In the expression 
 * <code>function<b>(2,3,'sss')</b></code>
 * the <code><b>(2,3,'sss')</b></code> arguments will be parsed into an {@link OwExprArgumentsSuffix}.
 * At evaluation time the <code>function</code> function symbol will be used to perform a function 
 * call with the designated arguments.   
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
public class OwExprArgumentsSuffix extends OwExprExpression implements OwExprPrimaryPrefix
{
    /**The prefixed function symbol*/
    private OwExprFunctionSymbol m_functionSymbol;
    /**The prefix expression*/
    private OwExprPrimaryPrefix m_prefix;
    /**A list of arguments as {@link OwExprExpression}s*/
    private List m_argumentsExpressions;

    /**
     * Constructor
     * @param symbolTable_p global symbol table 
     * @param errorTable_p global error table
     * @param prefix_p the primary prefix
     * @param argumentsExpressions_p  list of arguments as {@link OwExprExpression}s
     */
    public OwExprArgumentsSuffix(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, OwExprPrimaryPrefix prefix_p, List argumentsExpressions_p)
    {
        super(symbolTable_p, errorTable_p);
        this.m_argumentsExpressions = argumentsExpressions_p;
        this.m_prefix = prefix_p;

    }

    /**
     * Evaluates this expression on the {@link OwExprScope} (scope) provided as argument.
     * <br>
     * First the arguments are evaluated.
     * <br>
     * Than the function value is obtained using  {@link OwExprFunctionSymbol#getValue(OwExprScope, OwExprValue[])}
     * @param scope_p scope to be used during evaluation
     * @return the value resulted from the evaluation of this function
     * @throws OwExprEvaluationException
     */
    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        OwExprValue[] argumentValues = new OwExprValue[m_argumentsExpressions.size()];
        int i = 0;
        for (Iterator iterator = m_argumentsExpressions.iterator(); iterator.hasNext(); i++)
        {
            OwExprExpression argument = (OwExprExpression) iterator.next();
            argumentValues[i] = argument.evaluate(scope_p);
        }
        return m_functionSymbol.getValue(scope_p, argumentValues);
    }

    /**
     * Type check method.The type of the function symbol is considered - {@link #m_functionSymbol}.
     * If type errors are encountered during type computation the {@link #m_typeError} should
     * be set to <code>true</code> 
     * @return the expression type for this expression
     * @throws OwExprTypeMissmatchException if type errors are ecountrede during type computation  
     */
    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        if (!m_typeError)
        {
            OwExprExpressionType[] argumentTypes = new OwExprExpressionType[m_argumentsExpressions.size()];
            int i = 0;
            for (Iterator iterator = m_argumentsExpressions.iterator(); iterator.hasNext(); i++)
            {
                OwExprExpression argument = (OwExprExpression) iterator.next();
                argumentTypes[i] = argument.type();
            }
            m_functionSymbol.setArgumentTypes(argumentTypes);
            return m_functionSymbol.getType();
        }
        else
        {
            return OwExprExpressionType.NOTYPE;
        }
    }

    /**
     * The prefix of this expression is forced to function.
     * @return  the expression represented by this primary prefix
     */
    public OwExprExpression expression()
    {

        if (!m_typeError)
        {
            try
            {
                if (m_functionSymbol == null)
                {
                    m_functionSymbol = m_prefix.regressToFunction();
                }
                initType();
            }
            catch (OwExprTypeMissmatchException e)
            {
                m_errorTable.add(e);
                m_typeError = true;
            }
        }

        return this;
    }

    /**
     * Function on function call is not allowed.<br> (egg. <code>aFunction(1,2)(3,4)</code>).
     * @throws OwExprTypeMissmatchException always
     */
    public OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Invalid function on function call!");
    }

    /**
     * Function on property call is not allowed. <br> (egg. <code>aFunction.(1,2)</code>).
     * @throws OwExprTypeMissmatchException always
     */
    public OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Invalid function call !");
    }

    /**
     * Forces this primary prefix to a scope symbol.<br>
     * It also forces the {@link #m_prefix} primary prefix to a function call.
     * (egg. <code>aFunction(1,2).aProperty</code>)
     * @return the scope symbol of this function expression
     * @throws OwExprTypeMissmatchException
     */
    public OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException
    {
        if (m_prefix != null)
        {
            m_functionSymbol = m_prefix.regressToFunction();
            OwExprSymbolScope symbolScope = new OwExprSymbolScope(true, m_functionSymbol, this.expression());
            return symbolScope;
        }
        else
        {
            throw new OwExprTypeMissmatchException("Invalid function call!");
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        if (m_argumentsExpressions.size() > 0)
        {
            sb.append(m_argumentsExpressions.get(0).toString());
        }
        for (int i = 1; i < m_argumentsExpressions.size(); i++)
        {
            sb.append(",");
            sb.append(m_argumentsExpressions.get(i));
        }
        sb.append(")");
        return m_prefix.toString() + sb.toString();
    }

}
