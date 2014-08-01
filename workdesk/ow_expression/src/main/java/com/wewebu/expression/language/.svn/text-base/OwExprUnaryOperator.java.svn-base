package com.wewebu.expression.language;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *<p>
 * Unary operator class (egg. <code>!boolPropertz</code> , <code>-232.2</code> ).
 * A single operand operation implementation. <br>
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
public abstract class OwExprUnaryOperator extends OwExprOperator
{

    /**Conversion table of this operator*/
    private OwExprUnaryConversionTable m_conversionTable;

    /**
     * Constructor
     * @param image_p literal image (egg. <code>"!"</code> , <code>"-"</code>)
     * @param conversionTable_p conversion table to use 
     */
    public OwExprUnaryOperator(String image_p, OwExprUnaryConversionTable conversionTable_p)
    {
        super(image_p);
        this.m_conversionTable = conversionTable_p;

    }

    /**
     * Unary evaluation method to be called at evaluation time.
     * This is the entry point in the operator evaluation visitor sequence.
     * @param scope_p the scope this operation is evaluated on
     * @param expression_p single operand 
     * @return the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public final OwExprValue evaluate(OwExprScope scope_p, OwExprExpression expression_p) throws OwExprEvaluationException
    {
        OwExprValue value = expression_p.evaluate(scope_p);
        return value.accept(this);
    }

    /**
     * Evaluation by visiting with concrete single operand type.   
     * @param booleanValue_p 
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprBooleanValue booleanValue_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this + " for operand " + booleanValue_p);
    }

    /**
     * Evaluation by visiting with concrete single operand type.   
     * @param numericValue_p 
     * @return  the resulted value as {@link OwExprValue}
     * @throws OwExprEvaluationException if the operation fails for any reason (unsupported operand types ,
     *                                   incompatible operands, unimplemented operation or operation failure at evaluation time) 
     */
    public OwExprValue evaluate(OwExprNumericValue numericValue_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Unsupported operator " + this + " for operand " + numericValue_p);
    }

    /**
     * Type computation method.
     * Delegates type computation to inner conversion table : {@link #m_conversionTable} 
     * @param type_p single operand type
     * @return the type of the value resulted if two operands of type1 and type2 were combined in this operation
     * @throws OwExprTypeMissmatchException
     */
    public final OwExprType computeType(OwExprType type_p) throws OwExprTypeMissmatchException
    {
        return m_conversionTable.convert(type_p);
    }

    /**
     * Returns a set of compatible single operand types ( {@link OwExprType}s ) for a set of given operation result 
     * types set.
     * The returned type set is a subset of all left side operand types that can result invalid types
     *(different from {@link OwExprType#NOTYPE} ) for this operator. 
     * @param regressedTypes_p the operation result types set  
     * @return  a set of compatible left side operand  {@link OwExprType}s
     */
    public final Set getRegressingTypes(Set regressedTypes_p)
    {
        return m_conversionTable.getConversionsType(regressedTypes_p);
    }

    /**
     * Computes the resulted expression type based on given operand and regresses 
     * sub-expression types accordingly
     * 
     * @param operand_p single {@link OwExprExpression} operand
     * @return the resulted expression type as {@link OwExprUnaryExpressionType}
     * @throws OwExprTypeMissmatchException if the operator evaluation cannot not be performed due to type incompatibility 
     *      
     */
    public final OwExprUnaryExpressionType computeExpressionType(OwExprExpression operand_p) throws OwExprTypeMissmatchException
    {
        boolean notFullyRegressed = true;
        OwExprExpressionType type = null;
        Set computedTypes = null;
        while (notFullyRegressed)
        {
            type = operand_p.type();

            Set types = type.getInferringTypes();
            Set typesConvertible = new HashSet();
            computedTypes = new HashSet();

            for (Iterator i = types.iterator(); i.hasNext();)
            {
                OwExprType theType = (OwExprType) i.next();
                OwExprType computedType = computeType(theType);
                if (!computedType.equals(OwExprType.NOTYPE))
                {
                    computedTypes.add(computedType);
                    typesConvertible.add(theType);

                }

            }

            if (typesConvertible.isEmpty())
            {
                throw new OwExprTypeMissmatchException("Incompatible operand type " + type + " for operator " + this.toString());
            }

            notFullyRegressed = type.regressTo(typesConvertible);
        }
        return new OwExprUnaryExpressionType(this, type, computedTypes);
    }

}
