package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * &lt;in folder&gt; syntax non-terminal operand based format(e.g. MyDocument INFOLDER '/MyDocuments')<br/>
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
public class OwOperandFolderFormat extends OwFolderPredicateFormat
{
    private String leftOperand;

    public OwOperandFolderFormat(String inFolderOperator_p, String inTreeOperator_p, String leftOperand_p)
    {
        super(inFolderOperator_p, inTreeOperator_p);
        this.leftOperand = leftOperand_p;
    }

    public StringBuilder format(String folderId_p, String qualifier_p, boolean inTree_p)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(leftOperand);

        if (qualifier_p != null && qualifier_p.length() > 0)
        {
            builder.insert(0, ".");
            builder.insert(0, qualifier_p);
        }

        builder.append(" ");
        builder.append(operator(inTree_p));
        builder.append(" ");

        builder.append(folderId_p);

        return builder;
    }

}
