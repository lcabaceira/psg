package com.wewebu.expression.language;

/**
 *<p>
 * Primary language expressions are made from a prefix construction which can be 
 * a literal, a bracketed expression (egg. <code>(a-b)</code>) or an identifier followed
 * by any number of suffixes (zero or more). <br/>
 * Suffixes can be function arguments (<code>someFunction(1,2,'aaa')</code>), array 
 * selectors (<code>someProperty[2][4]</code>) or scope selectors(<code>employee.name.length</code>)).<br/>
 * Suffixes are custom implemented and represent prefixes for their subsequent suffixes. <br/>
 * Symbols that represent the primary expression prefixes are regressed after suffixes are parsed to 
 * their corresponding semantics (function,property or scope).
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
public interface OwExprPrimaryPrefix
{
    /**
     * Forces this primary prefix symbol to a function.
     * @return the function symbol of this expression 
     * @throws OwExprTypeMissmatchException if a type mismatch is detected during function symbol creation
     */
    OwExprFunctionSymbol regressToFunction() throws OwExprTypeMissmatchException;

    /**
     * Forces this primary prefix symbol to a property.
     * @return the property symbol of this expression 
     * @throws OwExprTypeMissmatchException if a type mismatch is detected during property symbol creation
     */
    OwExprPropertySymbol regressToPorperty() throws OwExprTypeMissmatchException;

    /**
     * Forces this primary prefix to a scope symbol.
     * @return the scope symbol of this expression 
     * @throws OwExprTypeMissmatchException if a type mismatch is detected during scope  symbol creation
     */
    OwExprSymbolScope regressToScope() throws OwExprTypeMissmatchException;

    /**
     * 
     * @return the expression represented by this primary prefix
     */
    OwExprExpression expression();
}
