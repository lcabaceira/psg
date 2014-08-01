package com.wewebu.ow.server.ecmimpl.opencmis.search;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecm.OwObjectCollection;

/**
 *<p>
 * Packs a query result its SQL statement.
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
public class OwCMISQueryResult
{
    private OwQueryStatement statement;
    private OwObjectCollection objectList;

    /**
     * Constructor
     * @param statement_p an SQL statement structure 
     * @param searchResultLst the statements CMIS query based result
     */
    public OwCMISQueryResult(OwQueryStatement statement_p, OwObjectCollection searchResultLst)
    {
        super();
        this.statement = statement_p;
        this.objectList = searchResultLst;
    }

    public OwQueryStatement getStatement()
    {
        return statement;
    }

    public OwObjectCollection getObjectList()
    {
        return objectList;
    }

}
