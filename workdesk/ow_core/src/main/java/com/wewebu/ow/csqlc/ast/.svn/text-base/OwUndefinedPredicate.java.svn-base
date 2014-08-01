package com.wewebu.ow.csqlc.ast;

import java.util.Arrays;
import java.util.List;

/**
 *<p>
 * SQL AST node : undefined predicate implementation.<br/>
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
public class OwUndefinedPredicate extends OwPredicate
{

    @Override
    public StringBuilder createPredicateSQLString()
    {
        return new StringBuilder();
    }

    /**
     * 
     * @return always <code>true</code>
     */
    public boolean isValid()
    {
        return false;
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        return Arrays.asList(new OwColumnQualifier[] {});
    }

}
