package com.wewebu.ow.csqlc.ast;

import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * Repository merge policy type enumeration.
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
public enum OwMergeType
{
    NONE("NONE"), UNION("UNION"), INTERSECT("INTERSECT");

    public static OwMergeType fromCriteriaOperator(int operator_p)
    {
        if (OwSearchOperator.MERGE_UNION == operator_p)
        {
            return UNION;
        }
        else if (OwSearchOperator.MERGE_INTERSECT == operator_p)
        {
            return INTERSECT;
        }
        else
        {
            return NONE;
        }
    }

    private String mergeTypeName;

    private OwMergeType(String mergeTypeName_p)
    {
        this.mergeTypeName = mergeTypeName_p;
    }

    @Override
    public String toString()
    {
        return mergeTypeName;
    }
}
