package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * &lt;in folder&gt; syntax non-terminal format.
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
public abstract class OwFolderPredicateFormat
{
    private String inFolderOperator;
    private String inTreeOperator;

    public OwFolderPredicateFormat(String inFolderOperator_p, String inTreeOperator_p)
    {
        super();
        this.inFolderOperator = inFolderOperator_p;
        this.inTreeOperator = inTreeOperator_p;
    }

    protected String operator(boolean inTree_p)
    {
        return inTree_p ? inTreeOperator : inFolderOperator;
    }

    public abstract StringBuilder format(String folderId_p, String qualifier_p, boolean inTree_p);

}
