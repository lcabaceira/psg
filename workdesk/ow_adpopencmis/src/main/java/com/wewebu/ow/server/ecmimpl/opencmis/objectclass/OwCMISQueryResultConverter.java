package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Implementors convert {@link QueryResult} objects to corresponding {@link TransientCmisObject} instances.
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
public interface OwCMISQueryResultConverter<O extends TransientCmisObject>
{
    /**
     * Converts a given CMIS query result object to an OPEN CMIS transient object
     * based on query result properties , SQL statement that produced the result and 
     * the context in which it was ran.  
     * 
     * @param queryResult a {@link QueryResult}
     * @param statement {@link OwQueryStatement} that produced the {@link QueryResult} 
     * @param operationContext the operation context in which the statement was ran 
     * @return a {@link TransientCmisObject} corresponding to the given {@link QueryResult} 
     *         of the given CMIS {@link OwQueryStatement} in the given {@link OperationContext}  
     *            
     * @throws OwException
     */
    O toCmisObject(QueryResult queryResult, OwQueryStatement statement, OperationContext operationContext) throws OwException;
}
