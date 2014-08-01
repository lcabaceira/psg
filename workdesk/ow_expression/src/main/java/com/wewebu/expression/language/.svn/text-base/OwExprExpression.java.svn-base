package com.wewebu.expression.language;

/**
 *<p>
 * An expression language compiled expression.
 * Expressions can be obtained from literal representations of expression language expressions.
 * An expression can be evaluated on different scopes.
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
public abstract class OwExprExpression
{
    /** No external scopes array */
    private static final OwExprExternalScope[] NO_EX_SCOPES = new OwExprExternalScope[] {};

    /** Global symbol table reference*/
    protected OwExprSymbolTable m_symbolTable;

    /** Global error table reference*/
    protected OwExprErrorTable m_errorTable;

    /** Type error indicating flag - a type error was detected in this expression */
    protected boolean m_typeError = false;

    /**
     * 
     * @param symbolTable_p global symbol table reference 
     * @param errorTable_p global error table reference
     */
    public OwExprExpression(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p)
    {
        super();
        this.m_symbolTable = symbolTable_p;
        this.m_errorTable = errorTable_p;

    }

    /**
     * Standard expression type initialization method.
     * If an error is detected during type checking the type error flag is set ({@link #m_typeError}) 
     * and the type error is added to the error table ({@link #m_errorTable}). 
     */
    protected final void initType()
    {
        try
        {
            type();
        }
        catch (OwExprTypeMissmatchException e)
        {
            m_errorTable.add(e);
            m_typeError = true;
        }
    }

    /**
     * A no external scopes evaluation routine.
     * This method delegates to {@link #evaluate(OwExprExternalScope[])}
     * @return the value resulted from the scopeless evaluation
     * @throws OwExprEvaluationException in case of evaluation failure
     */
    public OwExprValue evaluate() throws OwExprEvaluationException
    {
        return evaluate(NO_EX_SCOPES);
    }

    /**
     * Evaluates this expression.
     * The {@link OwExprSystem}  scope (the default top level scope) is added by default 
     * (it is not necessary to be amongs t the exte).
     * 
     * @param externalScopes_p the external scopes this expression is evaluated against
     * @return the value resulted from the evaluation of this expression 
     * @throws OwExprEvaluationException in case of evaluation failure
     */
    public OwExprValue evaluate(OwExprExternalScope[] externalScopes_p) throws OwExprEvaluationException
    {
        OwExprSystem system = new OwExprSystem();
        for (int i = 0; i < externalScopes_p.length; i++)
        {
            system.addScope(externalScopes_p[i]);
        }
        return evaluate(system);
    }

    /**
     * Evaluates this expression on the {@link OwExprScope} (scope) provided as argument.
     * @param scope_p external scope to be used during evaluation
     * @return the value resulted from the evaluation of this expression 
     * @throws OwExprEvaluationException in case of evaluation failure
     */
    public abstract OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException;

    /**
     * Type check method.
     * If type errors are encountered during type computation the {@link #m_typeError} should
     * be set to <code>true</code> 
     * @return the expression type for this expression
     * @throws OwExprTypeMissmatchException if type errors are encountered during type computation  
     */
    public abstract OwExprExpressionType type() throws OwExprTypeMissmatchException;

    /**
     * {@link #m_symbolTable} getter
     * @return the global symbol table
     */
    public final OwExprSymbolTable getSymbolTable()
    {
        return m_symbolTable;
    }

    /**
     * Pre-evaluation optional symbol visibility check method.
     * This method should be used on non erroneous expressions to check whether all symbols are 
     * visible in certain scopes.
     * Not all symbols are verifiable though (TODO)
     * @param externalScopes_p
     * @return <code>true</code> if are verifiable symbols are found in the given scopes <code>false</code> otherwise 
     * @throws OwExprEvaluationException if symbols visibility check fails
     */
    public final boolean symbolsVisibleInScopes(OwExprExternalScope[] externalScopes_p) throws OwExprEvaluationException
    {
        OwExprSystem system = new OwExprSystem();
        for (int i = 0; i < externalScopes_p.length; i++)
        {
            system.addScope(externalScopes_p[i]);
        }
        return m_symbolTable.matchesScope(system);
    }

    /**
     * Global error table accessor
     * @return the global error table
     */
    public final OwExprErrorTable getErrorTable()
    {
        return m_errorTable;
    }

    /**
     * Compile time errors indicator
     * @return <code>true</code> if the expression that resulted in this object had compilation errors 
     * (either semantic - type errors or lexical errors), <code>false</code> otherwise 
     */
    public final boolean hasErrors()
    {
        return m_errorTable.hasErrors();
    }

    /**
     * Number of errors discovered at compile time
     * @return <code>int</code> number of errors found at compile time
     */
    public final int errCount()
    {
        return m_errorTable.errCount();
    }

    /**
     * Symbol accessor.
     * @param symbolName_p requested symbol's name
     * @return the top level symbol identified with the given name 
     */
    public final OwExprSymbol getSymbol(String symbolName_p)
    {
        return m_symbolTable.getSymbol(symbolName_p);
    }
}
