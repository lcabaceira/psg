package com.wewebu.ow.csqlc.ast;

import java.util.List;

/**
 *<p>
 * SQL AST node : external class based boolean factor - used in multiple class search templates .<br/>
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
 *@since 3.2.0.0
 */
public class OwXBooleanFactor extends OwBooleanFactor
{
    private OwBooleanFactor factor;

    public OwXBooleanFactor(OwBooleanFactor factor_p)
    {
        super();
        this.factor = factor_p;
    }

    @Override
    public StringBuilder createBooleanFactorSQLString()
    {
        return this.factor.createBooleanFactorSQLString();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return this.factor.getColumnQualifiers();
    }

    public boolean isValid()
    {
        return this.factor.isValid();
    }

    public boolean isXCondition()
    {
        return true;
    }

    public OwBooleanTest asBooleanTest()
    {
        return new OwSearchConditionBooleanTest(this);
    }

}
