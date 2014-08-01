package com.wewebu.expression.language;

/**
 *<p>
 * The expression language function interface.
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
public interface OwExprFunction
{
    /**
     * Evaluation method for this function.
     * @param arguments_p
     * @return the value returned when this function is evaluated basted on the given arguments
     * @throws OwExprEvaluationException
     */
    OwExprValue value(OwExprValue[] arguments_p) throws OwExprEvaluationException;

    /**
     * 
     * @return the {@link OwExprType} that this function will return upon evaluation.
     * @throws OwExprEvaluationException
     */
    OwExprType type() throws OwExprEvaluationException;
}
