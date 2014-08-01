package com.wewebu.expression.language;

import java.util.Collection;
import java.util.Set;

/**
 *<p>
 * Expression type of all binary expressions.<br/>
 * Applies custom-binary regression rules.    
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
public class OwExprBinaryExpressionType extends OwExprExpressionType
{
    private OwExprExpressionType m_type1;
    private OwExprBinaryOperator m_binaryOperator;
    private OwExprExpressionType m_type2;

    /**
     * Constructor
     * @param binaryOperator_p the binary operator the holding expression of this type is based on 
     * @param type1_p left side operand expression type
     * @param type2_p right side operand expression type
     * @param inferringTypes_p the initial inferred types
     */
    public OwExprBinaryExpressionType(OwExprBinaryOperator binaryOperator_p, OwExprExpressionType type1_p, OwExprExpressionType type2_p, Collection inferringTypes_p)
    {
        super(inferringTypes_p);
        this.m_binaryOperator = binaryOperator_p;
        this.m_type1 = type1_p;
        this.m_type2 = type2_p;
    }

    /**
     * Constructor
     * @param binaryOperator_p the binary operator the holding expression of this type is based on 
     * @param type1_p left side operand expression type
     * @param type2_p right side operand expression type
     * @param type_p the initial inferred type
     */

    public OwExprBinaryExpressionType(OwExprBinaryOperator binaryOperator_p, OwExprExpressionType type1_p, OwExprExpressionType type2_p, OwExprType type_p)
    {
        super(type_p);
        this.m_binaryOperator = binaryOperator_p;
        this.m_type1 = type1_p;
        this.m_type2 = type2_p;

    }

    /**
     * Constructor
     * @param binaryOperator_p the binary operator the holding expression of this type is based on 
     * @param type1_p left side operand expression type
     * @param type2_p right side operand expression type
     * @param inferringTypes_p the initial inferred types
     */
    public OwExprBinaryExpressionType(OwExprBinaryOperator binaryOperator_p, OwExprExpressionType type1_p, OwExprExpressionType type2_p, OwExprType[] inferringTypes_p)
    {
        super(inferringTypes_p);
        this.m_binaryOperator = binaryOperator_p;
        this.m_type1 = type1_p;
        this.m_type2 = type2_p;
    }

    /**
     * Custom binary regression rules are applied here.
     * The regression is propagated according to the rules defined by the operator's conversion table to the 
     * left and right operand also.
     * @param regressedTypes_p
     * @return <code>true</code> if the current inferred types were changed in any way , <code>false</code> otherwise. 
     *         Also , the expression is considered regressed if either of the operand types is regressed. 
     * @throws OwExprTypeMissmatchException
     */
    boolean regressTo(Set regressedTypes_p) throws OwExprTypeMissmatchException
    {
        boolean regressed = false;
        try
        {
            regressed |= super.regressTo(regressedTypes_p);
        }
        catch (OwExprTypeMissmatchException e)
        {
            throw new OwExprTypeMissmatchException(e.getMessage() + " " + m_binaryOperator.toString() + " " + m_type2);
        }

        Set regressingTypes1 = m_binaryOperator.getRegressingTypes1(regressedTypes_p);
        Set regressingTypes2 = m_binaryOperator.getRegressingTypes2(regressedTypes_p);
        regressed |= m_type2.regressTo(regressingTypes2);
        regressed |= m_type1.regressTo(regressingTypes1);
        return regressed;

    }

}
