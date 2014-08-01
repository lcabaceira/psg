package com.wewebu.ow.server.field;

import java.util.List;

import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprObjectProperty;
import com.wewebu.expression.language.OwExprProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;

/**
 *<p>
 * Scope of complex OwProperty occurrences for Expression Language expressions.<br/>
 * Rules Engine for Highlighting in Hit List.
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
 *@since 3.1.0
 */
public class OwComplexPropertyChildScope extends OwExprExternalScope
{
    private Object[] values;
    private OwPropertyClass complexPropertyClass;

    public OwComplexPropertyChildScope(String name_p, Object[] values_p, OwPropertyClass complexPropertyClass_p)
    {
        super(name_p);
        this.values = values_p;
        this.complexPropertyClass = complexPropertyClass_p;
    }

    private int getComplexPropertyIndex(String propertyName_p) throws OwExprEvaluationException
    {
        try
        {
            List children = this.complexPropertyClass.getComplexChildClasses();
            int childrenSize = children.size();
            for (int i = 0; i < childrenSize; i++)
            {
                OwFieldDefinition child = (OwFieldDefinition) children.get(i);
                String className = child.getClassName();
                if (className.equals(propertyName_p))
                {
                    return i;
                }
            }
            return -1;
        }
        catch (Exception e)
        {
            //avoid excessive logging
            throw new OwExprEvaluationException("Could not get property " + propertyName_p + " from complex property sciope" + this.toString(), e);
        }
    }

    @Override
    protected OwExprProperty prelevateProperty(String propertyName_p) throws OwExprEvaluationException
    {
        try
        {
            int index = getComplexPropertyIndex(propertyName_p);
            if (index > -1)
            {
                List<OwPropertyClass> childClasses = this.complexPropertyClass.getComplexChildClasses();
                OwPropertyClass childClass = childClasses.get(index);

                String javaClass = childClass.getJavaClassName();

                return new OwExprObjectProperty(this.values[index], Class.forName(javaClass));
            }
        }
        catch (Exception e)
        {
            //avoid excessive logging
            throw new OwExprEvaluationException("Could not get property " + propertyName_p + " from complex property scope" + this.toString(), e);
        }
        throw new OwExprEvaluationException("Could not get property " + propertyName_p + " from complex property scope" + this.toString());
    }

    @Override
    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return getComplexPropertyIndex(propertyName_p) > -1;
    }

    @Override
    public int length() throws OwExprEvaluationException
    {
        return super.length();
    }

    public String toString()
    {
        String className = "<noPropertyClassName>";
        try
        {
            className = this.complexPropertyClass.getClassName();
        }
        catch (Exception e)
        {
            //avoid excessive logging
            className += ":" + e.getMessage();
        }
        return super.toString() + " aka complex OwProperty " + className;
    }

}
