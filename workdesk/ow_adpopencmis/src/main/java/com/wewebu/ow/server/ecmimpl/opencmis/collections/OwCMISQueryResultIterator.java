package com.wewebu.ow.server.ecmimpl.opencmis.collections;

import java.util.Iterator;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISRepositorySession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * {@link Iterator} implementation for the {@link OwCMISQueryIterable} using an underlying {@link QueryResult}.
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
 *@since 4.2.0.0
 */
public class OwCMISQueryResultIterator implements Iterator<OwCMISObject>
{

    private Iterator<QueryResult> iterator;
    private OwCMISRepositorySession session;
    private OwQueryStatement queryStatement;
    private OperationContext context;

    public OwCMISQueryResultIterator(Iterator<QueryResult> iterator, OwCMISRepositorySession session, OwQueryStatement queryStatement, OperationContext context)
    {
        this.iterator = iterator;
        this.session = session;
        this.queryStatement = queryStatement;
        this.context = context;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public OwCMISObject next()
    {
        QueryResult child = this.iterator.next();
        return from(child);
    }

    private OwCMISObject from(QueryResult child)
    {
        try
        {
            OwCMISObject childObject = session.createCMISObject(queryStatement, child, context);
            return childObject;
        }
        catch (OwException owe)
        {
            throw new RuntimeException(owe);
        }
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove()
    {
        throw new RuntimeException("Not supported yet!");
    }
}
