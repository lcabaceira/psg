package com.wewebu.expression.language;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *<p>
 * Binary operator class (egg. <code>1+2</code> , <code>'aaaa'+'b'</code> , <code>1<2</code>).
 * A two operands operation implementation (left and right). <br>
 * Index present in the parameters names usually denote left (index 1) or right (index 2) correlation.
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
public abstract class OwExprBinaryOperator extends OwExprOperator
{

    /** Conversion table used by this operation for type compatibility verification 
     *  at compile time. 
     */
    private OwExprBinaryConversionTable m_conversionTable;

    /**
     * Constructor
     * @param image_p String image of this operator (egg. "+","-" or "<=")
     * @param conversionTable_p the conversion table {@link #m_conversionTable}
     */
    public OwExprBinaryOperator(String image_p, OwExprBinaryConversionTable conversionTable_p)
    {
        super(image_p);
        this.m_conversionTable = conversionTable_p;
    }

    /**
     * Binary evaluation method to be called at evaluation time.
     * This is the entry point in the operator evaluation visitor sequence (stage 1 accept method is called
     * on the left side operand) 
     * @param scope_p the scope this operation is evaluated on
     * @param v1_p left side operand (1)
     * @param v2_p right side operand (2)
     * @return the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    protected final OwExprValue binaryEvaluation(OwExprScope scope_p, OwExprValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v1_p.accept(this, v2_p);
    }

    /**
     * Stage 2 evaluation by visiting entry point with concrete left operand type.   
     * @param v1_p 
     * @param v2_p
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v2_p.accept(this, v1_p);
    }

    /**
     * Stage 2 evaluation by visiting entry point with concrete left operand type.   
     * @param v1_p 
     * @param v2_p
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v2_p.accept(this, v1_p);
    }

    /**
     * Stage 2 evaluation by visiting entry point with concrete left operand type.   
     * @param v1_p 
     * @param v2_p
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v2_p.accept(this, v1_p);
    }

    /**
     * Stage 2 evaluation by visiting entry point with concrete left operand type.   
     * @param v1_p 
     * @param v2_p
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v2_p.accept(this, v1_p);
    }

    /**
     * Stage 2 evaluation by visiting entry point with concrete left operand type.   
     * @param v1_p 
     * @param v2_p
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprScopeValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        return v2_p.accept(this, v1_p);
    }

    /**
     * Stage 2 evaluation by visiting entry point with concrete left operand type.   
     * @param v1_p 
     * @param v2_p
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprNullValue v1_p, OwExprValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * STRING operator NULL operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * NUMERIC operator NULL operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * BOOLEAN operator NULL operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * DATE operator NULL operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * TIME operator NULL operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * BOOLEAN operator BOOLEAN operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprBooleanValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * BOOLEAN operator STRING operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprBooleanValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * NUMERIC operator NUMERIC operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * NUMERIC operator STRING operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprNumericValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * STRING operator NUMERIC operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprNumericValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * STRING operator STRING operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * STRING operator BOOLEAN operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprBooleanValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * STRING operator DATE operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * STRING operator TIME operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprStringValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * DATE operator DATE operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * DATE operator TIME operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * DATE operator STRING operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprDateValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * TIME operator TIME operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprTimeValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * TIME operator DATE operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprDateValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * TIME operator STRING operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprTimeValue v1_p, OwExprStringValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * SCOPE operator NULL operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprScopeValue v1_p, OwExprNullValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * SCOPE operator SCOPE operation evaluation
     * @param v1_p
     * @param v2_p
     * @return the resulted value as {@link OwExprValue} according to the conversion rules 
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time)
     * @since version 1.1.0 and AWD 3.1.0.0 
     */
    public OwExprValue evaluate(OwExprScopeValue v1_p, OwExprScopeValue v2_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this.toString() + " for operands " + v1_p.toString() + " and " + v2_p.toString());
    }

    /**
     * Type computation method.
     * Delegates type computation to inner conversion table : {@link #m_conversionTable} 
     * 
     * @param type1_p left operand type
     * @param type2_p right operand type
     * @return the type of the value resulted if two operands of type1 and type2 were combined in this operation
     * @throws OwExprTypeMissmatchException
     */
    public final OwExprType computeType(OwExprType type1_p, OwExprType type2_p) throws OwExprTypeMissmatchException
    {
        return m_conversionTable.convert(type1_p, type2_p);
    }

    /**
     * Computes the resulted expression type based on given operands and regresses 
     * sub-expression types accordingly
     * 
     * @param operand1_p the left side {@link OwExprExpression} operand
     * @param operand2_p the right side {@link OwExprExpression} operand
     * @return the resulted expression type as {@link OwExprBinaryExpressionType}
     * @throws OwExprTypeMissmatchException if the operator evaluation cannot not be performed due to type incompatibility 
     *                                      (type incompatibility resulted from the operator itself or due to the regression process) 
     */
    public final OwExprBinaryExpressionType computeExpressionType(OwExprExpression operand1_p, OwExprExpression operand2_p) throws OwExprTypeMissmatchException
    {

        boolean notFullyRegressed = true;
        OwExprExpressionType type1 = null;
        OwExprExpressionType type2 = null;
        Set computedTypes = null;
        while (notFullyRegressed)
        {
            type1 = operand1_p.type();
            type2 = operand2_p.type();
            Set types1 = type1.getInferringTypes();
            Set types2 = type2.getInferringTypes();
            Set types1Convertible = new HashSet();
            Set types2Convertible = new HashSet();
            computedTypes = new HashSet();

            for (Iterator i = types1.iterator(); i.hasNext();)
            {
                OwExprType theType1 = (OwExprType) i.next();

                for (Iterator j = types2.iterator(); j.hasNext();)
                {

                    OwExprType theType2 = (OwExprType) j.next();
                    OwExprType computedType = computeType(theType1, theType2);
                    if (!computedType.equals(OwExprType.NOTYPE))
                    {
                        computedTypes.add(computedType);
                        types1Convertible.add(theType1);
                        types2Convertible.add(theType2);
                    }
                }
            }

            if (types1Convertible.isEmpty())
            {
                throw new OwExprTypeMissmatchException("Incompatible operand types " + type1 + " " + this.toString() + " " + type2 + " for operands " + operand1_p.toString() + " and " + operand2_p);
            }

            notFullyRegressed = type1.regressTo(types1Convertible);
            notFullyRegressed |= type2.regressTo(types2Convertible);
        }
        return new OwExprBinaryExpressionType(this, type1, type2, computedTypes);
    }

    /**
     * Returns a set of compatible left side operand types ( {@link OwExprType}s ) for a set of given operation result 
     * types set.
     * The returned type set is a subset of all left side operand types that can result invalid types
     * (different from {@link OwExprType#NOTYPE} ) for this operator. 
     * @param regressedTypes_p the operation result types set  
     * @return  a set of compatible left side operand  {@link OwExprType}s
     */
    public final Set getRegressingTypes1(Set regressedTypes_p)
    {
        return m_conversionTable.getConversionsType1(regressedTypes_p);
    }

    /**
     * Returns a set of compatible right side operand types ( {@link OwExprType}s ) for a set of given operation result 
     * types set.
     * The returned type set is a subset of all right side operand types that can result invalid types
     * (different from {@link OwExprType#NOTYPE} ) for this operator. 
     * @param regressedTypes_p the operation result types set  
     * @return  a set of compatible right side operand  {@link OwExprType}s
     */
    public final Set getRegressingTypes2(Set regressedTypes_p)
    {
        return m_conversionTable.getConversionsType2(regressedTypes_p);
    }

}
