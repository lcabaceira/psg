package com.wewebu.expression.language;

import java.util.Set;

/**
 *<p>
 * Expression type of all short-conditional expressions.<br>
 * Regresses the two possible resulting branches.  
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
public class OwExprConditionalExpressionType extends OwExprExpressionType
{
    private OwExprExpressionType m_testType;
    private OwExprExpressionType m_trueType;
    private OwExprExpressionType m_falseType;

    public OwExprConditionalExpressionType(OwExprExpressionType testType_p, OwExprExpressionType trueType_p, OwExprExpressionType falseType_p)
    {
        super(trueType_p.m_inferringTypes);
        this.m_trueType = trueType_p;
        this.m_falseType = falseType_p;
        this.m_testType = testType_p;

    }

    boolean regressTo(Set regressedTypes_p) throws OwExprTypeMissmatchException
    {
        boolean regressed;
        regressed = super.regressTo(regressedTypes_p);
        regressed |= m_trueType.regressTo(regressedTypes_p);
        regressed |= m_falseType.regressTo(regressedTypes_p);
        return regressed;
    }
}
