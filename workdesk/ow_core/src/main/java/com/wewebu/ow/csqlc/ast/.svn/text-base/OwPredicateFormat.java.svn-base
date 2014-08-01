package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * SQL standard form predicate (prefix leftOPerand operator rightOperand suffix) node configuration.
 * Defines configuration elements for the normal and negated predicate.  
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
public class OwPredicateFormat
{
    private final String prefix;
    private final String negatedPrefix;
    private final String suffix;
    private final String negatedSuffix;
    private final String operator;
    private final String negatedOperator;

    public OwPredicateFormat(String prefix_p, String negatedPrefix_p, String operator_p, String negatedOperator_p, String suffix_p, String negatedSuffix_p)
    {
        super();
        this.prefix = prefix_p;
        this.negatedPrefix = negatedPrefix_p;
        this.operator = operator_p;
        this.negatedOperator = negatedOperator_p;
        this.suffix = suffix_p;
        this.negatedSuffix = negatedSuffix_p;
    }

    public String prefix(boolean negated_p)
    {
        return negated_p ? negatedPrefix : prefix;
    }

    public String operator(boolean negated_p)
    {
        return negated_p ? negatedOperator : operator;
    }

    public String suffix(boolean negated_p)
    {
        return negated_p ? negatedSuffix : suffix;
    }
}
