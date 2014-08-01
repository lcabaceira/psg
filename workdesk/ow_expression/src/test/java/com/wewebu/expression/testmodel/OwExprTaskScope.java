package com.wewebu.expression.testmodel;

import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprObjectProperty;
import com.wewebu.expression.language.OwExprProperty;

/**
*<p>
* OwExprTaskScope. 
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
public class OwExprTaskScope extends OwExprExternalScope
{
    private OwExprTask m_task;

    public OwExprTaskScope(String name_p, OwExprTask task_p)
    {
        super(name_p);
        this.m_task = task_p;
    }

    protected OwExprProperty prelevateProperty(String propertyName_p) throws OwExprEvaluationException
    {
        Object value = m_task.getProperty(propertyName_p);

        if (value instanceof OwExprTask)
        {
            return new OwExprObjectProperty(new OwExprTaskScope(propertyName_p, (OwExprTask) value), OwExprTaskScope.class);
        }
        else
        {
            return new OwExprObjectProperty(value, value == null ? null : value.getClass());
        }
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return m_task.getProperty(propertyName_p) != null;
    }

}
