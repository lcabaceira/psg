package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * Search condition wrapping predicate.
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
public class OwSearchConditionPredicate extends OwPredicate
{
    private OwSearchCondition m_condition;

    public OwSearchConditionPredicate(OwSearchCondition condition_p)
    {
        super();
        m_condition = condition_p;
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        if (isValid())
        {
            StringBuilder builder = m_condition.createSearchConditionSQLString();
            builder.insert(0, " ( ");
            builder.append(" ) ");
            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (isValid())
        {
            return m_condition.getColumnQualifiers();
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

    public boolean isValid()
    {
        return m_condition.isValid();
    }

}
