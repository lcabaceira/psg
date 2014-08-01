package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * &lt;in folder&gt; syntax non-terminal function format(e.g. INFOLDER('/MyDocuments'))<br/>
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
public class OwFunctionFolderFormat extends OwFolderPredicateFormat
{

    public OwFunctionFolderFormat(String inFolderOperator_p, String inTreeOperator_p)
    {
        super(inFolderOperator_p, inTreeOperator_p);
    }

    public StringBuilder format(String folderId_p, String qualifier_p, boolean inTree_p)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(folderId_p);

        if (qualifier_p != null && qualifier_p.length() > 0)
        {
            builder.insert(0, ",");
            builder.insert(0, qualifier_p);
        }

        builder.insert(0, "(");
        builder.insert(0, operator(inTree_p));
        builder.append(")");

        return builder;
    }

}
