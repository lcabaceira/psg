package com.wewebu.expression.language;

/**
 *<p>
 * Function symbols represent symbol table recordings of function pointing symbols in the 
 * expressions.<br/>
 * <table class="jd">
 *     <tr>
 *         <td><code>fun(1,2,3)</code></td>
 *         <td>Creates function symbol for the <b>fun</b> identifier </td>
 *     </tr>
 * </table>
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
public class OwExprFunctionSymbol extends OwExprSymbol
{
    private OwExprExpressionType[] m_argumentTypes;
    private OwExprSymbolScope m_symbolScope;

    public OwExprFunctionSymbol(OwExprSymbol parent_p, String name_p, OwExprType[] types_p)
    {
        super(parent_p, name_p, types_p);
    }

    /**
     * 
     * @param argumentTypes_p the argument expressions types that this function should use  
     *        to discover the function binding  in the scope at evaluation time   
     */
    public final void setArgumentTypes(OwExprExpressionType[] argumentTypes_p)
    {
        this.m_argumentTypes = argumentTypes_p;
    }

    /**
     * Returns a value produced by the invocation of the function represented by this symbol.
     * @param scope_p the top level scope on which this function is evaluated.
     * @param arguments_p the value of the arguments used to invoke the function
     * @return the value resulted from the functions invocation 
     * @throws OwExprEvaluationException
     */
    public final OwExprValue getValue(OwExprScope scope_p, OwExprValue[] arguments_p) throws OwExprEvaluationException
    {
        OwExprScope evaluationScope = scope_p;
        if (m_symbolScope != null)
        {
            evaluationScope = m_symbolScope.solveScope(scope_p);
        }
        OwExprFunction function = evaluationScope.function(getName(), m_argumentTypes);
        OwExprType functionType = function.type();
        OwExprExpressionType symbolType = getType();
        if (!symbolType.canInfer(functionType))
        {
            throw new OwExprEvaluationException("Invalid function return type " + functionType + " for symbol " + this.getSymbolFQN() + " expected " + symbolType);
        }
        return function.value(arguments_p);
    }

    /**
     * Pretty-print utility
     * @param sb_p
     * @param tabs_p
     */
    protected void toPrettyPrintString(StringBuffer sb_p, StringBuffer tabs_p)
    {
        sb_p.append("function");
        sb_p.append(OwExprExpressionType.printTypes(m_argumentTypes));
        super.toPrettyPrintString(sb_p, tabs_p);
    }

    /**
     * 
     * @param symbolScope_p the functions parent scope setter 
     */
    public final void setSymbolScope(OwExprSymbolScope symbolScope_p)
    {
        this.m_symbolScope = symbolScope_p;
    }

    public boolean matches(OwExprScope scope_p)
    {
        return true;
    }

}
