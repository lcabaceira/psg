package com.wewebu.ow.server.ecmimpl.fncm5.search;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;

/**
 *<p>
 * P8 5.0 SQL column reference implementation.
 * See the FileNet P8 5.0 SQL Syntax Reference.
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
public class OwFNCM5ColumnReference extends OwColumnReference
{

    public OwFNCM5ColumnReference(OwColumnQualifier qualifier_p, String columnName_p)
    {
        super(qualifier_p, columnName_p);
    }

    @Override
    protected String createColumnName()
    {
        return "[" + super.createColumnName() + "]";
    }

}
