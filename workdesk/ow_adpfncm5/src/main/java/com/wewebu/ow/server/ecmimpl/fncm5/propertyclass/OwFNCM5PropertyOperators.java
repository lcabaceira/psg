package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Arrays;
import java.util.List;

import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * OwFNCM5PropertyOperators.
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
public enum OwFNCM5PropertyOperators
{
    DATE_OPERATORS(new Integer[] { OwSearchOperator.CRIT_OP_NOT_BETWEEN, OwSearchOperator.CRIT_OP_GREATER, OwSearchOperator.CRIT_OP_GREATER_EQUAL, OwSearchOperator.CRIT_OP_LESS, OwSearchOperator.CRIT_OP_LESS_EQUAL }), BOOLEAN_OPERATORS(
            new Integer[] { OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL }), NUMERIC_OPERATORS(new Integer[] { OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL, OwSearchOperator.CRIT_OP_BETWEEN,
            OwSearchOperator.CRIT_OP_NOT_BETWEEN, OwSearchOperator.CRIT_OP_GREATER, OwSearchOperator.CRIT_OP_GREATER_EQUAL, OwSearchOperator.CRIT_OP_LESS, OwSearchOperator.CRIT_OP_LESS_EQUAL }), STRING_OPERATORS(new Integer[] {
            OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL, OwSearchOperator.CRIT_OP_LIKE, OwSearchOperator.CRIT_OP_NOT_LIKE, OwSearchOperator.CRIT_OP_IS_IN, OwSearchOperator.CRIT_OP_IS_NOT_IN, OwSearchOperator.CRIT_OP_IS_NULL,
            OwSearchOperator.CRIT_OP_IS_NOT_NULL }), ID_OPERATORS(new Integer[] { OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL });

    public final List<Integer> list;

    private OwFNCM5PropertyOperators(Integer[] operators_p)
    {
        this.list = Arrays.asList(operators_p);
    }

}
