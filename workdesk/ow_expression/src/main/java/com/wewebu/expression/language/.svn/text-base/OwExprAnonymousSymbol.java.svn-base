package com.wewebu.expression.language;

/**
 *<p>
 * Anonymous symbols represent evaluation time symbols used to check expression return    
 * type consistency and array element type consistency.<br/>
 * Examples : <br/>
 * <table class="jd">
 *     <tr>
 *         <td><code>(a-b).days</code></td>
 *         <td>Creates an anonymous symbol for expression <code>a-b</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>arrayObject[1][2]</code></td>
 *         <td>Creates 2 anonymous symbols for expressions <code>arrayObject[1]</code> and
 *             <code>arrayObject[1][2]</code>.<br>
 *             Further references to array first and second array dimensions will be type 
 *             checked against this symbols.
 *         </td>
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
public class OwExprAnonymousSymbol extends OwExprSymbol
{

    /**
     * Constructor
     */
    public OwExprAnonymousSymbol(OwExprSymbol parent_p, String name_p, OwExprExpressionType type_p)
    {
        super(parent_p, name_p, type_p);
    }

    /**
     * Constructor
     */
    public OwExprAnonymousSymbol(OwExprSymbol parent_p, String name_p, OwExprType type_p)
    {
        super(parent_p, name_p, type_p);
    }

    /**
     * Constructor
     */
    public OwExprAnonymousSymbol(OwExprSymbol parent_p, String name_p, OwExprType[] types_p)
    {
        super(parent_p, name_p, types_p);
    }

    /**
     * A match method used to check the validity of symbols for certain scopes.<br>
     * Can be used to discover symbol and related expressions validity before evaluation.
     * @param scope_p a scope to search the symbol on
     * @return always <code>true</code>  (No pre-evaluation match is implemented for anonymous symbols).
     */
    public boolean matches(OwExprScope scope_p)
    {
        return true;
    }

}
