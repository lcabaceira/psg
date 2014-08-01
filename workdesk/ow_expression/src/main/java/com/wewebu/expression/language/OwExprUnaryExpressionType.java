package com.wewebu.expression.language;

import java.util.Collection;
import java.util.Set;

/**
 *<p>
 * Expression type of all unary expressions.<br/>
 * Applies custom-unary regression rules.  
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
public class OwExprUnaryExpressionType extends OwExprExpressionType
{
    private OwExprUnaryOperator m_operator;
    private OwExprExpressionType m_type;

    public OwExprUnaryExpressionType(OwExprUnaryOperator operator_p, OwExprExpressionType generatorType_p, Collection inferringTypes_p)
    {
        super(inferringTypes_p);
        this.m_operator = operator_p;
        this.m_type = generatorType_p;
    }

    public OwExprUnaryExpressionType(OwExprUnaryOperator operator_p, OwExprExpressionType generatorType_p, OwExprType type_p)
    {
        super(type_p);
        this.m_operator = operator_p;
        this.m_type = generatorType_p;
    }

    public OwExprUnaryExpressionType(OwExprUnaryOperator operator_p, OwExprExpressionType generatorType_p, OwExprType[] inferringTypes_p)
    {
        super(inferringTypes_p);
        this.m_operator = operator_p;
        this.m_type = generatorType_p;
    }

    /**
     * Custom binary regression rules are applied here.
     * The regression is propagated according to the rules defined by the operator's conversion table to the 
     * left and right operand also.
     * @param regressedTypes_p
     * @return <code>true</code> if the current inferred types were changed in any way , <code>false</code> otherwise. 
     *         Also , the expression is considered regressed if the operand type is regressed. 
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
            throw new OwExprTypeMissmatchException(e.getMessage() + " and operator " + m_operator.toString());
        }
        Set regressingTypes = m_operator.getRegressingTypes(regressedTypes_p);
        regressed |= m_type.regressTo(regressingTypes);
        return regressed;
    }
}
