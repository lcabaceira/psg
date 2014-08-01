package com.wewebu.expression.language;

/**
 *<p>
 * Property symbols represent symbol table recordings of property pointing symbols in the 
 * expressions.<br>
 * <table class="jd">
 *    <tr>
 *         <td><code>object.pro</code></td>
 *         <td>Creates two recursive  property symbols for the <b>object</b> and <b>pro</b> identifiers</td>
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
public class OwExprPropertySymbol extends OwExprSymbol
{
    private OwExprSymbolScope m_symbolScope;
    private boolean m_forceMatch = false;

    public OwExprPropertySymbol(OwExprSymbol parent_p, String name_p, OwExprType[] types_p)
    {
        super(parent_p, name_p, types_p);
    }

    /**
     * @param scope_p the top level scope on which this function is evaluated.
     * @return the value of the property represented by this symbol as solved on the given scope
     * @throws OwExprEvaluationException
     */
    public final OwExprValue getValue(OwExprScope scope_p) throws OwExprEvaluationException
    {

        OwExprScope evaluationScope = scope_p;
        if (m_symbolScope != null)
        {
            evaluationScope = m_symbolScope.solveScope(scope_p);
        }
        OwExprProperty property = evaluationScope.property(getName());
        OwExprType type = property.type();
        OwExprExpressionType symbolType = getType();
        if (!symbolType.canInfer(type))
        {
            throw new OwExprEvaluationException("Invalid property type " + type + " for symbol " + this.getSymbolFQN() + " expected " + symbolType + " but found " + type);
        }
        return property.value();
    }

    /**
     * Pretty-print utility
     * @param sb_p
     * @param tabs_p
     */
    protected void toPrettyPrintString(StringBuffer sb_p, StringBuffer tabs_p)
    {
        sb_p.append("property ");
        super.toPrettyPrintString(sb_p, tabs_p);
    }

    /**
     * 
     * @param symbolScope_p parent symbol scope
     */
    public final void setSymbolScope(OwExprSymbolScope symbolScope_p)
    {
        this.m_symbolScope = symbolScope_p;
    }

    /**
     * Informs this symbol that it should be always matched by {@link #matches(OwExprScope)}
     */
    public void forceSymbolMatchInScope()
    {
        this.m_forceMatch = true;
    }

    /**
     * A match method used to check the validity of symbols for certain scopes.<br>
     * Can be used to discover symbol and related expressions validity before evaluation.
     * @param scope_p a scope to search the symbol on
     * @return <code>true</code> if this symbol is valid for the given scope
     * @throws OwExprEvaluationException
     */
    public boolean matches(OwExprScope scope_p) throws OwExprEvaluationException
    {
        if (m_forceMatch)
        {
            return true;
        }
        else
        {
            if (scope_p.hasProperty(getName()))
            {
                OwExprProperty property = scope_p.property(getName());
                OwExprSymbolTable symbolTable = getSymbolTable();
                return symbolTable.matchesScope(property);
            }
            else
            {
                return false;
            }
        }
    }
}
