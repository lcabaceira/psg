package com.wewebu.ow.server.ecmimpl.opencmis.collections;

import java.util.Iterator;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwIterableAttributeBag;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISRepositorySession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchTemplate;

/**
 *<p>
 * {@link OwIterable} implementation over a CMIS {@link OwQueryStatement}. 
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
public class OwCMISQueryIterable extends OwIterableAttributeBag<OwCMISObject>
{
    private static Logger LOG = OwLog.getLogger(OwCMISQueryIterable.class);

    private ItemIterable<QueryResult> iterable = null;
    private OwCMISRepositorySession session;
    private OwQueryStatement queryStatement;
    private OwLoadContext context;
    private OwCMISNativeObjectClass<ObjectType, TransientCmisObject> mainObjectClass;
    private long skipCount = 0;

    public OwCMISQueryIterable(OwCMISRepositorySession session, OwQueryStatement queryStatement, OwLoadContext context) throws OwException
    {
        this.session = session;
        this.queryStatement = queryStatement;
        this.context = context;
        findMainClass();
    }

    private OwCMISQueryIterable(ItemIterable<QueryResult> iterable, OwCMISRepositorySession session, OwQueryStatement queryStatement, OwLoadContext context, OwCMISNativeObjectClass<ObjectType, TransientCmisObject> mainObjectClass)
    {

        this.iterable = iterable;
        this.session = session;
        this.queryStatement = queryStatement;
        this.context = context;
        this.mainObjectClass = mainObjectClass;
    }

    private void findMainClass() throws OwException
    {
        String mainObjectClassId = queryStatement.getMainTableQualifier().getTargetObjectType();
        mainObjectClass = session.getObjectClass(mainObjectClassId);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#skipTo(long)
     */
    @Override
    public OwCMISQueryIterable skipTo(long position)
    {
        OwCMISQueryIterable skippedIterable = new OwCMISQueryIterable(this.getIterable().skipTo(position), this.session, queryStatement, context, mainObjectClass);
        skippedIterable.skipCount = position;
        return skippedIterable;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage(int)
     */
    @Override
    public OwCMISQueryIterable getPage(int maxNumItems)
    {
        OwCMISQueryIterable page = new OwCMISQueryIterable(this.getIterable().getPage(maxNumItems), this.session, queryStatement, context, mainObjectClass);
        page.isPage = true;
        return page;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage()
     */
    @Override
    public OwCMISQueryIterable getPage()
    {
        OwCMISQueryIterable page = new OwCMISQueryIterable(this.getIterable().getPage(), this.session, queryStatement, context, mainObjectClass);
        page.isPage = true;
        return page;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#iterator()
     */
    @Override
    public Iterator<OwCMISObject> iterator()
    {
        if (isPage)
        {
            return new OwCMISQueryResultIterator(this.getIterable().getPage().iterator(), this.session, queryStatement, toOperationContext(context));
        }
        else
        {
            return new OwCMISQueryResultIterator(this.getIterable().skipTo(skipCount).iterator(), this.session, queryStatement, toOperationContext(context));
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPageNumItems()
     */
    @Override
    public long getPageNumItems()
    {
        return this.getIterable().getPageNumItems();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getHasMoreItems()
     */
    @Override
    public boolean getHasMoreItems()
    {
        return this.getIterable().getHasMoreItems();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getTotalNumItems()
     */
    @Override
    public long getTotalNumItems()
    {
        return this.getIterable().getTotalNumItems();
    }

    protected OperationContext toOperationContext(OwLoadContext context)
    {

        OperationContext operationContext = session.createOperationContext(context.getPropertyNames(), context.getSorting(), context.getPageSize(), mainObjectClass);
        operationContext.setIncludeRelationships(IncludeRelationships.NONE);
        return operationContext;
    }

    private ItemIterable<QueryResult> getIterable()
    {
        if (null == iterable)
        {
            String sqlStatement = queryStatement.createSQLString().toString();
            LOG.debug("iterable-query: " + sqlStatement);
            this.iterable = this.session.getOpenCMISSession().query(sqlStatement, context.getVersionSelection() == OwSearchTemplate.VERSION_SELECT_ALL, toOperationContext(context));
        }
        return this.iterable;
    }
}
