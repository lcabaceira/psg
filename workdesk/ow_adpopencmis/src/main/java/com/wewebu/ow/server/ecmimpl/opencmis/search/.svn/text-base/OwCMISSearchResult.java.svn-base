package com.wewebu.ow.server.ecmimpl.opencmis.search;

import java.util.List;

import com.wewebu.ow.csqlc.ast.OwExternal;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecm.OwObjectCollection;

/**
 *<p>
 * Search information (search statements and search results) wrapper class.
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
 *@since 4.1.0.0
 */
public class OwCMISSearchResult
{
    private OwExternal<List<OwQueryStatement>> externalStatements;
    private OwObjectCollection cmisSearchResult;

    public OwCMISSearchResult(OwExternal<List<OwQueryStatement>> externalStatements_p, OwObjectCollection cmisSearchResult_p)
    {
        super();
        this.externalStatements = externalStatements_p;
        this.cmisSearchResult = cmisSearchResult_p;
    }

    public OwExternal<List<OwQueryStatement>> getExternalStatements()
    {
        return externalStatements;
    }

    public OwObjectCollection getCmisSearchResult()
    {
        return cmisSearchResult;
    }
}
