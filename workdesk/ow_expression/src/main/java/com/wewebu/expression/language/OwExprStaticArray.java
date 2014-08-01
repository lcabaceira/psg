package com.wewebu.expression.language;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * An expression representing a static array (inlined 
 * array value like {1,2,3} ).
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
 *@since version 1.1.0 and AWD 3.1.0.0
 *   
 */
public class OwExprStaticArray extends OwExprExpression implements OwExprPrimaryPrefix, OwExprScope
{
    private List m_expressions = new LinkedList();
    private List m_values = new LinkedList();

    public OwExprStaticArray(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super(symbolTable_p, errorTable_p);
    }

    /**
     * Parse time construction method.
     * @param expression_p array item expression
     */
    public void add(OwExprExpression expression_p)
    {
        m_expressions.add(expression_p);
    }

    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        for (Iterator i = m_expressions.iterator(); i.hasNext();)
        {
            OwExprExpression expression = (OwExprExpression) i.next();
            OwExprValue value = expression.evaluate(scope_p);
            m_values.add(value);

        }
        return new OwExprScopeValue(this);
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {

        return new OwExprExpressionType(OwExprType.SCOPE);
    }

    public OwExprExpression expression()
    {
        return this;
    }

    public OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Invalid function call on literal " + this);
    }

    public OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException
    {
        throw new OwExprTypeMissmatchException("Invalid property access on static array " + this);
    }

    public OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException
    {
        OwExprSymbolTable symTable = getSymbolTable();

        OwExprSymbol symbol = symTable.addAnonymousSymbol(null, m_symbolTable.issueRuntimeSymbolName(), type());
        return new OwExprSymbolScope(symbol, this);
    }

    public OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        final OwExprValue value = (OwExprValue) m_values.get(index_p);

        return new OwExprScopedProperty() {

            public OwExprType type() throws OwExprEvaluationException
            {
                return OwExprType.SCOPE;
            }

            public OwExprValue value() throws OwExprEvaluationException
            {

                return value;
            }

            public Class<?> javaType() throws OwExprEvaluationException
            {
                return OwExprValue.class;
            }
        };
    }

    public OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTyes_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("No such function " + functionName_p + " for static array scope " + this);
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("No such property " + propertyName_p + " for static array scope " + this);
    }

    public int length() throws OwExprEvaluationException
    {
        return m_expressions.size();
    }

    public OwExprProperty property(String propertyName_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("No such property " + propertyName_p + " for static array scope " + this);
    }

    public String toString()
    {
        return "{" + m_expressions + "}";
    }

}
