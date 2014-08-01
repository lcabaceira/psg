package com.wewebu.expression.language;

/**
 *<p>
 * OwExprScopedProperty. 
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
public abstract class OwExprScopedProperty implements OwExprProperty
{

    public final OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        return value().at(index_p);
    }

    public final OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTypes_p) throws OwExprEvaluationException
    {
        return value().function(functionName_p, argunmentTypes_p);
    }

    public final int length() throws OwExprEvaluationException
    {
        return value().length();
    }

    public final OwExprProperty property(String propertyName_p) throws OwExprEvaluationException
    {
        return value().property(propertyName_p);
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return value().hasProperty(propertyName_p);
    }

}
