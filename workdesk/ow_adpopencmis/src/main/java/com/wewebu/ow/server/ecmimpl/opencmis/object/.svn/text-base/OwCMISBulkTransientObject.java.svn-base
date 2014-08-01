package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 *A transient object that refreshes cached properties using bulk fetch through {@link Session#getObject(String)}.
 *</p>
 *
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
public class OwCMISBulkTransientObject<N extends TransientCmisObject> extends OwCMISAbstractTransientObject<N>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISBulkTransientObject.class);

    public OwCMISBulkTransientObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        super(transientCmisObject, creationContext, session);
    }

    /**
     * Retrieve the native properties and add them to the cache.
     * @param nativePropertyFilter native properties' names (no qualifier)
     */
    @Override
    protected OwCMISContextBoundObject<N> retrieveProperties(Set<String> nativePropertyFilter)
    {
        LOG.info("Fetching bulk-object-properties from content repository!");
        if (LOG.isDebugEnabled())
        {
            LOG.debug(String.format("OwCMISCachedObject.fetchProperties: Fetched bulk-object-properties = %s", nativePropertyFilter.toString()));
        }

        return fetchObject(nativePropertyFilter);

    }

    protected OwCMISContextBoundObject<N> fetchObject(Set<String> nativePropertyFilter)
    {
        String nativeId = this.contextBoundObject.object.getId();
        //TODO : keep old caching?
        OperationContext newContext = addPropertyFilter(this.contextBoundObject.context, nativePropertyFilter, null);
        CmisObject newObject = session.getObject(nativeId, newContext);
        N newTransientObject = (N) newObject.getTransientObject();
        return new OwCMISContextBoundObject<N>(newTransientObject, newContext);
    }

    @Override
    protected OwCMISContextBoundObject<N> retrieveRenditions(Set<String> nativeRenditionsFilter) throws OwException
    {
        String nativeId = this.contextBoundObject.object.getId();
        //TODO : keep old caching?
        OperationContext newContext = addPropertyFilter(this.contextBoundObject.context, null, nativeRenditionsFilter);
        newContext.setCacheEnabled(false);
        CmisObject newObject = session.getObject(nativeId, newContext);
        N newTransientObject = (N) newObject.getTransientObject();
        return new OwCMISContextBoundObject<N>(newTransientObject, newContext);
    }

}
