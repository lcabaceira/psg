package com.wewebu.expression.language;

/**
 *<p>
 * An array selector suffix of a primary expression.<br/>
 * In the expression 
 * <code>property<b>[2]</b></code>
 * the <code><b>[2]</b></code> arguments will be parsed into an {@link OwExprArraySelectorSuffix}.
 * At evaluation time the <code>property</code> property symbol will be used to perform an indexed
 * property evaluation with the designated index.  
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
public class OwExprArraySelectorSuffix extends OwExprExpression implements OwExprPrimaryPrefix
{

    /**Anonymous symbol name used for index symbols*/
    private static final String ARRAY_SNONYMOUS_SYMBOL = "0$A";

    /**The prefixed scope symbol*/
    private OwExprSymbolScope m_symbolScope;

    /**Array selecting expression*/
    private OwExprExpression m_arraySelector;

    /**The prefix expression*/
    private OwExprPrimaryPrefix m_prefix;

    /**The prefix property symbol*/
    private OwExprPropertySymbol m_propertySymbol;

    /**
     * Constructor
     * @param symbolTable_p global symbol table 
     * @param errorTable_p global error table
     * @param prefix_p the primary prefix
     * @param arraySelector_p  array selector expression
     */
    public OwExprArraySelectorSuffix(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, OwExprPrimaryPrefix prefix_p, OwExprExpression arraySelector_p)
    {
        super(symbolTable_p, errorTable_p);
        this.m_prefix = prefix_p;
        this.m_arraySelector = arraySelector_p;

    }

    /**
     * Evaluates this expression on the {@link OwExprScope} (scope) provided as argument.
     * <br>
     * First the prefix is solbed as an scope .
     * <br>
     * Than the selector expression is evaluated to a numeric value.
     * <br>
     * Than the indexed value is obtained using {@link OwExprScope#at(int)} indexed access method.
     * @param scope_p scope to be used during evaluation
     * @return the value resulted from the evaluation of this function
     * @throws OwExprEvaluationException
     */
    public final OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        OwExprScope evaluationScope = m_symbolScope.solveScope(scope_p);
        OwExprNumericValue selectorValue = (OwExprNumericValue) m_arraySelector.evaluate(scope_p);

        return evaluationScope.at(selectorValue.toInt()).value();
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        if (!m_typeError)
        {
            if (m_propertySymbol != null)
            {
                boolean notFullyRegressed = true;
                while (notFullyRegressed)
                {
                    try
                    {
                        OwExprExpressionType selectorType = m_arraySelector.type();
                        notFullyRegressed = selectorType.regressTo(OwExprType.NUMERIC);

                    }
                    catch (OwExprTypeMissmatchException e)
                    {
                        throw new OwExprTypeMissmatchException(e.getMessage() + " in array selector. Numeric is required.", e);
                    }

                }
                return m_propertySymbol.getType();

            }
            else
            {
                throw new OwExprTypeMissmatchException("Invalid array access expression : " + this.toString());
            }
        }
        else
        {
            return OwExprExpressionType.NOTYPE;
        }
    }

    public OwExprExpression expression()
    {
        if (!m_typeError)
        {
            try
            {
                regressToPorperty();
            }
            catch (OwExprTypeMissmatchException e)
            {
                m_errorTable.add(e);
                m_typeError = true;
            }
            initType();
        }

        return this;
    }

    public OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Function arrays are not supported!");
    }

    public OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException
    {
        if (m_propertySymbol == null)
        {
            if (m_prefix != null)
            {
                m_symbolScope = m_prefix.regressToScope();
                OwExprSymbol prefixSymbol = m_symbolScope.getSymbol();
                m_propertySymbol = prefixSymbol.addPropertySymbol(ARRAY_SNONYMOUS_SYMBOL, OwExprType.ALL_TYPE_TYPES);
                m_propertySymbol.forceSymbolMatchInScope();
            }
            else
            {
                throw new OwExprTypeMissmatchException("Invalid function call!");
            }
        }
        return m_propertySymbol;
    }

    public OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException
    {
        if (m_prefix != null)
        {
            OwExprExpression expression = this.expression();
            OwExprSymbol prefixSymbol = m_symbolScope.getSymbol();
            OwExprSymbol symbol = prefixSymbol.addAnonymousSymbol(ARRAY_SNONYMOUS_SYMBOL, expression.type());

            return new OwExprSymbolScope(symbol, expression);
        }
        else
        {
            throw new OwExprTypeMissmatchException("Invalid array access !");
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(m_arraySelector.toString());
        sb.append("]");
        return m_prefix.toString() + sb.toString();
    }

}
