package com.wewebu.expression.language;

import java.util.HashMap;
import java.util.Map;

/**
 *<p>
 * External scopes are property and function domains Java API interfaces.<br>
 * External scopes can be used on their own as collections of properties and functions.
 * <b>This version only supports adding properties!</b><br>
 * By extending this class custom scopes can be made available during expressions evaluation.   
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
public class OwExprExternalScope implements OwExprScope
{

    private Map m_functions = new HashMap();

    private Map m_properties = new HashMap();

    private String m_name;

    /**
     * Constructor
     * @param name_p the name of this scope. The scope will be referenced by this name in the evaluated expressions.
     */
    public OwExprExternalScope(String name_p)
    {
        super();
        this.m_name = name_p;
    }

    /**
     * Adds a sub scope to this scope.<br>
     * Scopes are stored as properties of this scope.
     * @param externalScope_p the sub scope to add
     */
    public final void addScope(final OwExprExternalScope externalScope_p)
    {
        m_properties.put(externalScope_p.getName(), new OwExprScopedProperty() {

            public OwExprType type() throws OwExprEvaluationException
            {
                return OwExprType.SCOPE;
            }

            public OwExprValue value() throws OwExprEvaluationException
            {

                return new OwExprScopeValue(externalScope_p);
            }

            public Class<?> javaType() throws OwExprEvaluationException
            {
                return OwExprExternalScope.class;
            }

        });
    }

    /**
     * 
     * @param functionName_p the name of the requested function
     * @param argunmentTypes_p the argument types of the requested function
     * @return the function defined by this scope that matches the requested name and argument types
     * @throws OwExprEvaluationException
     */
    public final OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTypes_p) throws OwExprEvaluationException
    {
        OwExprFunction function = prelevateFunction(functionName_p, argunmentTypes_p);
        if (function == null)
        {
            StringBuffer sb = new StringBuffer();
            sb.append("(");
            if (argunmentTypes_p.length > 0)
            {
                sb.append(argunmentTypes_p[0].toString());
            }
            for (int i = 1; i < argunmentTypes_p.length; i++)
            {
                sb.append(",");
                sb.append(argunmentTypes_p[i].toString());
            }
            sb.append(")");
            throw new OwExprEvaluationException("No such function : " + functionName_p + sb.toString());
        }
        else
        {
            return function;
        }
    }

    /**
     * 
     * @param propertyName_p
     * @return the function defined by this scope that matches the requested name 
     * @throws OwExprEvaluationException
     */
    public final OwExprProperty property(String propertyName_p) throws OwExprEvaluationException
    {
        OwExprProperty property = prelevateProperty(propertyName_p);
        if (property == null)
        {
            throw new OwExprEvaluationException("No such property : " + propertyName_p);
        }
        else
        {
            return property;
        }
    }

    /**
     * Function search hook for {@link #function(String, OwExprExpressionType[])}.<br>
     * Children of this class should override this method to perform custom 
     * 
     * @param functionName_p
     * @param argumentTypes_p
     * @return always <code>null</code>
     * @throws OwExprEvaluationException
     */
    protected OwExprFunction prelevateFunction(String functionName_p, OwExprExpressionType[] argumentTypes_p) throws OwExprEvaluationException
    {
        return null;
    }

    /**
     * Property search hook for {@link #property(String)}.<br>
     *  
     * @param propertyName_p
     * @return a property that was previously added using {@link #addScope(OwExprExternalScope)} or <code>null</code> if no such property was added 
     * @throws OwExprEvaluationException
     */
    protected OwExprProperty prelevateProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return (OwExprProperty) m_properties.get(propertyName_p);
    }

    public final String getName()
    {
        return m_name;
    }

    public String toString()
    {
        return "$" + m_name;
    }

    public OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Array access on non array scope!");
    }

    public int length() throws OwExprEvaluationException
    {
        return 0;
    }

    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return m_properties.containsKey(propertyName_p);
    }
}
