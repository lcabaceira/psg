package com.wewebu.expression.language;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *<p>
 * A value is an expression evaluation result.<br/>
 * All values are scopes.
 * Values are ''visitable'' by binary operator objects (see Visitor pattern) in a 2 
 * stage visitor pattern implementation (first left side operand is visited than 
 * right side operand is visited thus selecting a typed operation evaluation in the operator class) 
 * 
 * Values are ''visitable'' by binary operator objects (see Visitor pattern) in a 1 stage 
 * visitor implementation.
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
public abstract class OwExprValue implements OwExprScope
{
    /**No properties constant to be used with {@link #getValuePropeties()}*/
    private static final Map NOPROPERTIES = new HashMap();

    /**
     * Static expression language value factory based on a java object.
     * 
     * <table>   
     * <tr>
     * <td>Expression Type</td> <td>Java Peer Type</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprNumericValue}</td> <td>java.lang.Number</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprStringValue}</td> <td>java.lang.String</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprBooleanValue}</td> <td>java.lang.Boolean</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprTimeValue}</td> <td> {@link OwExprTime} </td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprDateValue}</td> <td>java.util.Calendar</td>
     * </tr>
     * <tr>   
     * <td> {@link OwExprNullValue} </td> <td>Java's null  literal type</td>
     * </tr>
     * </table>
     *
     * If javaValue_p's type does not match any of the previous table entries a  {@link OwExprScopeValue} will 
     * be factored.
     * 
     * @param javaValue_p an object to be converted into its expression language value peer
     * @return an expression language value based on  javaValue_p
     */
    public static final OwExprValue fromJavaValue(Object javaValue_p) throws OwExprEvaluationException
    {
        return fromJavaValue(javaValue_p, javaValue_p == null ? null : javaValue_p.getClass());
    }

    /**
     * Static expression language value factory based on a java object.
     * 
     * <table>   
     * <tr>
     * <td>Expression Type</td> <td>Java Peer Type</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprNumericValue}</td> <td>java.lang.Number</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprStringValue}</td> <td>java.lang.String</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprBooleanValue}</td> <td>java.lang.Boolean</td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprTimeValue}</td> <td> {@link OwExprTime} </td>
     * </tr>
     * <tr>   
     * <td>{@link OwExprDateValue}</td> <td>java.util.Calendar</td>
     * </tr>
     * <tr>   
     * <td> {@link OwExprNullValue} </td> <td>Java's null  literal type</td>
     * </tr>
     * </table>
     *
     * If javaValue_p's type does not match any of the previous table entries a  {@link OwExprScopeValue} will 
     * be factored.
     * 
     * @param javaValue_p an object to be converted into its expression language value peer
     * @param javaType_p original java type 
     * @return an expression language value based on  javaValue_p
     * @since 1.3.0 and AWD 3.1.0
     */
    public static final OwExprValue fromJavaValue(Object javaValue_p, Class<?> javaType_p) throws OwExprEvaluationException
    {
        if (javaValue_p == null)
        {
            return OwExprNullValue.INSTANCE;

        }
        else if (javaValue_p instanceof Number)
        {
            return new OwExprNumericValue((Number) javaValue_p, javaType_p);
        }
        else if (javaValue_p instanceof String)
        {
            return new OwExprStringValue((String) javaValue_p, javaType_p);
        }
        else if (javaValue_p instanceof Character)
        {
            return new OwExprStringValue(javaValue_p.toString(), javaType_p);
        }
        else if (javaValue_p instanceof Calendar)
        {
            return new OwExprDateValue((Calendar) javaValue_p, javaType_p);
        }
        else if (javaValue_p instanceof OwExprTime)
        {
            return new OwExprTimeValue((OwExprTime) javaValue_p, javaType_p);
        }
        else if (javaValue_p instanceof Date)
        {
            Date date = (Date) javaValue_p;
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return new OwExprDateValue(c, javaType_p);
        }
        else if (javaValue_p instanceof Boolean)
        {
            return OwExprBooleanValue.value((Boolean) javaValue_p);
        }
        else if (javaValue_p instanceof OwExprScope)
        {
            return new OwExprScopeValue((OwExprScope) javaValue_p);
        }
        else
        {
            return new OwExprScopeValue(new OwExprReflectiveScope("V$", javaValue_p));
        }
    }

    private OwExprReflectiveScope reflectiveScope;
    private Class<?> javaType;

    /**
     * 
     * @param javaType_p original java type
     * @since 1.3.0 and AWD 3.1.0
     */
    public OwExprValue(Class javaType_p)
    {
        this.javaType = javaType_p;
    }

    private OwExprReflectiveScope getReflectiveScope()
    {
        if (this.reflectiveScope == null)
        {
            this.reflectiveScope = new OwExprReflectiveScope(this.toString(), this);
        }
        return this.reflectiveScope;
    }

    /**
     * Java class conversion method.
     * 
     * @param javaSuperType_p the requested java super type 
     *                        The returned type should be a subclass of this type.
     *                        This is necessary for values having more than one possible java peers 
     *                        such as {@link OwExprNumericValue}'s Integer and Double support.  
     * @return the java class peer of this value's expression language type
     * @throws OwExprEvaluationException
     */
    public abstract Object toJavaObject(Class javaSuperType_p) throws OwExprEvaluationException;

    /**
     * Stage 1 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v2_p right side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + this + " and " + v2_p);
    }

    /**
     * Stage 2 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v1_p lef side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprBooleanValue v1_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + v1_p + " and " + this);
    }

    /**
     * Stage 2 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v1_p lef side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprNumericValue v1_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + v1_p + " and " + this);
    }

    /**
     * Stage 2 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v1_p lef side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprStringValue v1_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + v1_p + " and " + this);
    }

    /**
     * Stage 2 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v1_p lef side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprDateValue v1_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + v1_p + " and " + this);
    }

    /**
     * Stage 2 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v1_p lef side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprTimeValue v1_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + v1_p + " and " + this);
    }

    /**
     * Stage 2 binary operator visitor acceptance method
     * @param binaryOperator_p binary operator visitor
     * @param v1_p lef side operator value
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprBinaryOperator binaryOperator_p, OwExprScopeValue v1_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + binaryOperator_p + " for values " + v1_p + " and " + this);
    }

    /**
     * Unary operator visitor acceptance method
     * @param unaryOperator_p binary operator visitor
     * @return the operator visit processed {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue accept(OwExprUnaryOperator unaryOperator_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Invalid operation " + unaryOperator_p + " for values " + this);
    }

    /**
     * Scope {@link OwExprScope#function(String, OwExprExpressionType[])} default implementation.
     * By default value-scopes have no functions defined
     * @param functionName_p the requested function name
     * @return the {@link OwExprProperty} designated by propertyName_p and found in this value's scope 
     * @throws OwExprEvaluationException if the requested property is not found in this scope or 
     *                                   the creation of the corresponding {@link OwExprProperty} has failed
     */
    public OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTypes_p) throws OwExprEvaluationException
    {
        OwExprReflectiveScope myReflectiveScope = getReflectiveScope();
        return myReflectiveScope.function(functionName_p, argunmentTypes_p);
        //        throw new OwExprEvaluationException("No such function " + functionName_p + " for value scope " + this);
    }

    /**
     * Indexed scope access method
     * 
     * @param index_p <code>int</code> index of the requested property
     * 
     * @return the method always throws an exception 
     * 
     * @throws OwExprEvaluationException always as no indexed access is supported on value scopes  
     */
    public OwExprProperty at(int index_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Array access on non array value : " + this);
    }

    /**
     * Scope {@link OwExprScope#length()} helper default value implementation (returns 0).
     * 
     * @return always 0
     * @throws OwExprEvaluationException 
     */
    public int length() throws OwExprEvaluationException
    {
        return 0;
    }

    /**
     * Overridable default value properties factory method 
     * @return a {@link Map} of {@link OwExprProperty} for this value
     */
    protected Map getValuePropeties()
    {
        return NOPROPERTIES;
    }

    /**
     * Scope {@link OwExprScope#property(String)} default implementation.
     * A default value properties mechanism is implemented. 
     * Concrete values must create properties via {@link #getValuePropeties()}.
     * @param propertyName_p the requested property name
     * @return the {@link OwExprProperty} designated by propertyName_p and found in this value's scope 
     * @throws OwExprEvaluationException if the requested property is not found in this scope or 
     *                                   the creation of the corresponding {@link OwExprProperty} has failed
     */
    public OwExprProperty property(String propertyName_p) throws OwExprEvaluationException
    {
        OwExprProperty property = (OwExprProperty) getValuePropeties().get(propertyName_p);
        if (property != null)
        {
            return property;
        }
        else
        {
            throw new OwExprEvaluationException("No such property <" + propertyName_p + "> for dynamic scope " + this);
        }
    }

    /**
     * Scope {@link OwExprScope#hasProperty(String)} default implementation.
     * A default value properties mechanism is implemented. 
     * Concrete values must create properties via {@link #getValuePropeties()}.
     * @param propertyName_p the requested property name
     * @return <code>true</code> if this scope can perform property access for the requested property 
     *         - an access via {@link #property(String)} will NOT fail on missing property grounds, 
     *         <code>false</code> otherwise
     *  
     * @throws OwExprEvaluationException if the property validity check has failed
     */
    public boolean hasProperty(String propertyName_p) throws OwExprEvaluationException
    {
        return getValuePropeties().containsKey(propertyName_p);
    }

    /**
     * 
     * @return the original java type
     * @since 1.3.0 and AWD 3.1.0
     */
    public final Class<?> getJavaType()
    {
        return this.javaType;
    }

}
