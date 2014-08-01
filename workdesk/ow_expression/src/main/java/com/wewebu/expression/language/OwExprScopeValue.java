package com.wewebu.expression.language;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 *<p>
 * Scope values represent values obtained during evaluation that need to be queried for 
 * properties or functions.<br>
 * Example:<br>
 * <code>employee.birthDate.year</code>
 * <br>
 * The <code>employee.birthDate</code> sub-expression will be evaluated to a {@link OwExprScopeValue}
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
public class OwExprScopeValue extends OwExprValue
{
    private OwExprScope m_scope;

    /**
     * The scope <code>length</code> property inner class used for indexed scope access.<br>
     * Example:<br> 
     * <code>person.children.length</code>
     * @see OwExprScope#length() 
     */
    private class LengthProperty extends OwExprScopedProperty
    {

        public OwExprType type() throws OwExprEvaluationException
        {
            return OwExprType.NUMERIC;
        }

        /**
         * 
         * @return a numeric value of this scopes indexed length  (see. OwExprScope{@link #length()} )
         * @throws OwExprEvaluationException
         */
        public OwExprValue value() throws OwExprEvaluationException
        {
            return new OwExprNumericValue(OwExprScopeValue.this.m_scope.length());
        }

        public Class<?> javaType() throws OwExprEvaluationException
        {
            return Integer.class;
        }

    }

    private Map m_scopeProperties = new HashMap();

    /**
     * Constructor
     * @param scope_p the scope represented by this value
     */
    public OwExprScopeValue(OwExprScope scope_p)
    {
        super(OwExprScope.class);
        this.m_scope = scope_p;
        m_scopeProperties.put("length", new LengthProperty());
    }

    protected Map getValuePropeties()
    {
        return m_scopeProperties;
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(this, v2_p);
    }

    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprScopeValue v1_p) throws OwExprEvaluationException
    {
        return binaryOperator_p.evaluate(v1_p, this);
    }

    public Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException
    {
        if (javaSuperType_p.isArray() || javaSuperType_p.equals(Object.class))
        {
            Class targetType = javaSuperType_p.isArray() ? javaSuperType_p.getComponentType() : Object.class;
            int len = length();
            Object[] array = (Object[]) Array.newInstance(targetType, len);
            for (int i = 0; i < len; i++)
            {
                OwExprProperty property = at(i);
                OwExprValue value = property.value();
                Object javaObject = value.toJavaObject(targetType);
                array[i] = javaObject;
            }

            return array;
        }
        else
        {
            throw new OwExprEvaluationException("Non-array scope conversions are not allowed!");
        }
    }

    public final OwExprScope solveScope(String name_p) throws OwExprEvaluationException
    {
        return m_scope.property(name_p);
    }

    public final OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTypes_p) throws OwExprEvaluationException
    {
        return m_scope.function(functionName_p, argunmentTypes_p);
    }

    public final OwExprProperty property(String propertyName_p) throws OwExprEvaluationException
    {
        try
        {
            return m_scope.property(propertyName_p);

        }
        catch (OwExprEvaluationException e)
        {
            //TODO: no error. add err behavior or extract
            return super.property(propertyName_p);
        }
    }

    /**
     * Indexed access delegate.<br>
     * Delegates to {@link #m_scope}.
     * 
     * @param index_p 
     * @return the {@link OwExprProperty} property found at the requested index  
     * @throws OwExprEvaluationException
     */
    public OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        return m_scope.at(index_p);
    }

    /**
     * Indexed access delegate.<br>
     * Delegates to {@link #m_scope}.
     * 
     * @return the maximum index range  for which indexed access is possible in this scope
     * @throws OwExprEvaluationException
     */
    public int length() throws OwExprEvaluationException
    {
        return m_scope.length();
    }

    public final OwExprScope getScope()
    {
        return m_scope;
    }

    /**
     * 
     * @param propertyName_p
     * @return true if the property is defined by this value or by the represented scope
     * @throws OwExprEvaluationException
     */
    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        if (super.hasProperty(propertyName_p))
        {
            return true;
        }
        else
        {
            return m_scope.hasProperty(propertyName_p);
        }

    }

    public String toString()
    {
        return "V$" + m_scope.toString();
    }
}
