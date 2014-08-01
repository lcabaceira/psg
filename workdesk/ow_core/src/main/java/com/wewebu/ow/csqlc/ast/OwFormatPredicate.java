package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * SQL standard form predicate (prefix leftOPerand operator rightOperand suffix) AST node.
 * @see OwPredicateFormat
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
public abstract class OwFormatPredicate extends OwPredicate
{

    private OwPredicateFormat format;

    private boolean negated;

    public OwFormatPredicate(OwPredicateFormat format_p, boolean negated_p)
    {
        super();
        this.format = format_p;
        this.negated = negated_p;
    }

    protected abstract StringBuilder createLeftOperand();

    protected abstract StringBuilder createRightOperand();

    @Override
    public final StringBuilder createPredicateSQLString()
    {
        if (isValid())
        {
            StringBuilder builder = createLeftOperand();
            String prefix = this.format.prefix(negated);
            if (prefix != null)
            {
                builder.insert(0, " ");
                builder.insert(0, this.format.prefix(negated));
            }
            builder.append(" ");
            builder.append(this.format.operator(negated));
            builder.append(" ");
            builder.append(createRightOperand());
            String suffix = this.format.suffix(negated);
            if (suffix != null)
            {
                builder.append(" ");
                builder.append(suffix);
            }

            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }
}
