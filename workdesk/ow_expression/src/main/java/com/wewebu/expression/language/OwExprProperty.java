package com.wewebu.expression.language;

/**
 *<p>
 * Properties are value bearing identifiers applied to scopes.<br>
 * Properties can define sub properties through a self-defined scope, hence properties 
 * are scopes.
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
public interface OwExprProperty extends OwExprScope
{
    /**
     * 
     * @return the value of this property
     * @throws OwExprEvaluationException
     */
    OwExprValue value() throws OwExprEvaluationException;

    /**
     * 
     * @return the type of this property
     * @throws OwExprEvaluationException
     */
    OwExprType type() throws OwExprEvaluationException;

    /**
     * 
     * @return the original java type of this property
     * @throws OwExprEvaluationException
     * @since 1.3.0 and AWD 3.1.0
     */
    Class<?> javaType() throws OwExprEvaluationException;
}
