package com.wewebu.ow.csqlc.ast;

import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;search condition&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
 *@since 3.2.0.0
 */
public interface OwSearchCondition
{
    /**
     * 
     * @return <code>true</code> if this search condition can produce a valid string representation
     *         through {@link #createSearchConditionSQLString()}<br>
     *         <code>false</code> if this search condition can NOT produce a valid string representation
     *         through {@link #createSearchConditionSQLString()}
     */
    boolean isValid();

    /**
     * 
     *@return  <code>false</code> if this search condition refers other tables than the main FROM clause table
     *         <code>true</code> if this search condition refers the main FROM clause table 
     *@since 3.2.0.0
     */
    boolean isXCondition();

    /**
     * 
     * @return a {@link StringBuilder} filled with the string representation of this non-terminal
     */
    StringBuilder createSearchConditionSQLString();

    List<OwColumnQualifier> getColumnQualifiers();

    OwBooleanTest asBooleanTest();
}
