package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * An {@link OwObjectCollection} that relates on an {@link OwObjectsQuery} for the 
 * default object contents. <br/>
 * The contents is refreshed when the collection is sorted using the collection query.
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
 *@since 2.5.3.1
 */
public class OwQueriedCollection extends OwObjectCollectionDelegator
{
    /**the object collection query*/
    private OwObjectsQuery m_query;

    /**
     * 
     * @param decoratedCollection_p
     * @param query_p 
     */
    public OwQueriedCollection(OwObjectCollection decoratedCollection_p, OwObjectsQuery query_p)
    {
        super(decoratedCollection_p);
        this.m_query = query_p;
    }

    /**
     * Constructor<br>
     * Triggers a reload on this collection (see {@link #reload()} using the given sort object.
     * @param query_p
     * @param sort_p
     * @throws OwException
     */
    public OwQueriedCollection(OwObjectsQuery query_p, OwSort sort_p) throws OwException
    {
        this(null, query_p);
        reload(sort_p);
    }

    /**
     * Reloads this collection with no sort object
     * @throws OwException
     */
    public void reload() throws OwException
    {
        reload(null);
    }

    /**
     * Executes the underlying query using the given sort criteria and replaces the delegate with the result.
     * @param sort_p
     * @throws OwException
     */
    public void reload(OwSort sort_p) throws OwException
    {
        OwObjectCollection newCollection = m_query.execute(sort_p);
        replaceDelegatee(newCollection);
    }

    /**
     * Executes the underlying query using the given sort criteria and replaces the delegate with the result.
     * @param sortCriteria_p
     * @throws Exception
     */
    public void sort(OwSort sortCriteria_p) throws Exception
    {
        reload(sortCriteria_p);
    }

}