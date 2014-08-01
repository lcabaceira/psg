package com.wewebu.ow.server.ecmimpl.fncm5.search;

import java.util.Arrays;
import java.util.List;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwPredicate;
import com.wewebu.ow.server.ecm.OwClass;

/**
 *<p>
 * P8 5.0 SQL subclasses search predicate condition implementation.
 * Adds ISOFCLASS or ISCLASS where predicate conditions for defined subclasses.
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
 *@since 3.2.0.3
 */
public class OwFNCM5SubclassesSearchContion extends OwPredicate
{

    private static final String OR = " OR ";
    private static final String IS_OF_CLASS = "ISOFCLASS";
    private static final String IS_CLASS = "ISCLASS";
    private List<OwClass> subclasses;
    private OwColumnQualifier qualifier = new OwColumnQualifier();

    public OwFNCM5SubclassesSearchContion(List<OwClass> subclasses_p)
    {
        super();
        this.subclasses = subclasses_p;
    }

    public boolean isValid()
    {
        boolean valid = subclasses != null && !subclasses.isEmpty();

        return valid;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return Arrays.asList(new OwColumnQualifier[] { qualifier });
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        StringBuilder builder = new StringBuilder();
        if (isValid())
        {
            for (OwClass subclass : subclasses)
            {
                if (subclass.isEnabled())
                {
                    String function = IS_CLASS;

                    if (subclass.isIncludeSubclasses())
                    {
                        function = IS_OF_CLASS;
                    }
                    if (builder.length() > 0)
                    {
                        builder.append(OR);
                    }
                    builder.append(function);
                    builder.append("(");
                    String qualifierString = qualifier.getQualifierString();
                    if (qualifierString != null)
                    {
                        builder.append(qualifierString);
                        builder.append(",");
                    }
                    builder.append("[");
                    builder.append(subclass.getClassName());
                    builder.append("]");
                    builder.append(")");
                }
            }
        }
        return builder;
    }

}
