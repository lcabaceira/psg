package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Caches {@link CmisObject} properties to minimize the number of DMS repository accesses.
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
public abstract class OwCMISAbstractTransientObject<N extends TransientCmisObject> implements OwCMISTransientObject<N>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractTransientObject.class);

    protected Session session;
    protected OwCMISContextBoundObject<N> contextBoundObject;
    private boolean detached = false;

    public OwCMISAbstractTransientObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        this.contextBoundObject = new OwCMISContextBoundObject<N>(transientCmisObject, creationContext);
        this.session = session;
    }

    @Override
    public synchronized void detach()
    {
        this.detached = true;
    }

    @Override
    public boolean isDetached()
    {
        return detached;
    }

    protected Session getSession()
    {
        return session;
    }

    public OperationContext getTransientContext()
    {
        return this.contextBoundObject.context;
    }

    public N getTransientCmisObject()
    {
        return this.contextBoundObject.object;
    }

    protected abstract OwCMISContextBoundObject<N> retrieveProperties(Set<String> nativePropertyFilter) throws OwException;

    protected abstract OwCMISContextBoundObject<N> retrieveRenditions(Set<String> nativeRenditionsFilter) throws OwException;

    protected OperationContext addPropertyFilter(OperationContext context, Set<String> nativePropertyFilter, Set<String> nativeRenditionFilter)
    {
        boolean includeAcls = context.isIncludeAcls();
        boolean includeAllowableActions = context.isIncludeAllowableActions();
        boolean includePolicies = context.isIncludePolicies();
        IncludeRelationships includeRelationships = context.getIncludeRelationships();

        Set<String> oldRenditionFilter = context.getRenditionFilter();
        Set<String> renditionFilter = oldRenditionFilter;
        if (nativeRenditionFilter != null)
        {
            renditionFilter = (renditionFilter == null) ? new HashSet<String>() : new HashSet<String>(oldRenditionFilter);
            renditionFilter.addAll(nativeRenditionFilter);
            if (renditionFilter.size() > 1)
            {
                renditionFilter.remove("cmis:none");
            }
        }

        boolean includePathSegments = context.isIncludePathSegments();
        String orderBy = context.getOrderBy();
        boolean cacheEnabled = context.isCacheEnabled();
        int maxItemsPerPage = context.getMaxItemsPerPage();

        Set<String> oldFilter = context.getFilter();
        Set<String> filter = oldFilter;

        if (nativePropertyFilter != null)
        {
            filter = oldFilter == null ? new HashSet<String>() : new HashSet<String>(oldFilter);

            if (oldFilter == null || !oldFilter.containsAll(nativePropertyFilter))
            {
                //TODO : keep old caching? 
                cacheEnabled = false;
            }

            filter.addAll(nativePropertyFilter);
        }

        OperationContext newContext = this.session.createOperationContext(filter, includeAcls, includeAllowableActions, includePolicies, includeRelationships, renditionFilter, includePathSegments, orderBy, cacheEnabled, maxItemsPerPage);

        return newContext;
    }

    public synchronized N secureObject(String... propertyNames) throws OwException
    {
        return secureObject(Arrays.asList(propertyNames));
    }

    /**
     * Make sure we have these properties cached.
     * @param propertyNames
     * @throws OwException 
     */
    public synchronized N secureObject(Collection<String> propertyNames) throws OwException
    {
        secureProperties(propertyNames);
        return this.contextBoundObject.object;
    }

    /**
     * 
     * @param propertyNames Collection of String (native property names)
     * @return a map of (Native Name -> Native Property) values.
     * @throws OwException
     */
    public synchronized Map<String, Property<?>> secureProperties(Collection<String> propertyNames) throws OwException
    {
        Map<String, Property<?>> requestedProperties = new HashMap<String, Property<?>>();

        HashMap<String, String> unfetchedPropertyNames = new HashMap<String, String>();
        for (String nativePropertyName : propertyNames)
        {
            Property<?> nativeProperty = contextBoundObject.object.getProperty(nativePropertyName);
            if (null == nativeProperty)
            {
                PropertyDefinition<?> propDef = getUnfetchedPropertyDefinition(nativePropertyName);
                if (propDef != null)
                {
                    unfetchedPropertyNames.put(nativePropertyName, propDef.getQueryName());
                }
                else
                {
                    LOG.debug("OwCMISAbstractTransientObject.secureProperties: Could not get query name, trying to retrieve property by id = " + nativePropertyName);
                    unfetchedPropertyNames.put(nativePropertyName, nativePropertyName);
                }
            }
            else
            {
                requestedProperties.put(nativePropertyName, nativeProperty);
            }
        }

        if (!unfetchedPropertyNames.isEmpty())
        {
            this.contextBoundObject = this.retrieveProperties(new HashSet<String>(unfetchedPropertyNames.values()));

            for (String propertyName : unfetchedPropertyNames.keySet())
            {
                Property<?> nativeProperty = contextBoundObject.object.getProperty(propertyName);
                if (null == nativeProperty)
                {
                    if (LOG.isDebugEnabled())
                    {
                        String objectId = contextBoundObject.object.getId();
                        String repositoryId = getSession().getRepositoryInfo().getId();
                        ObjectType objectType = contextBoundObject.object.getType();
                        LOG.debug("Fetch fault : RID=" + repositoryId + " OID=" + objectId + " objectType=" + objectType.getId() + " propertyId=" + propertyName);
                    }

                    // ATTENTION : 
                    // The com.wewebu.owd.opencmis.fetch.safety system property should be used only on 
                    // faulty CMIS implementations that do not return all filter requested properties (such as 
                    // Old-Alfresco-CMIS implementation or File Net P8 CMIS implementation).    
                    // When set to false only a DEBUG log trace will be issued - no exception thrown and    
                    // the object container will be inconsistent - some behavior based on the non retrieved 
                    // properties might raise an exception ( most probably NullPoinetrExceptions).   

                    if (Boolean.getBoolean("com.wewebu.owd.opencmis.fetch.safety"))
                    {
                        throw new OwServerException("Could not fetch property " + propertyName + " !");
                    }
                }
                requestedProperties.put(propertyName, nativeProperty);
            }
        }

        return requestedProperties;
    }

    /**(overridable)
     * Called by {@link #secureProperties(Collection)} if a property is requested, which is not cached.
     * Returned PropertyDefinition will be used to transform id to corresponding queryName representation which will be used for fetching.
     * @param nativePropertyName String native (non-qualified) property Id
     * @return PropertyDefinition
     * @since 4.1.1.1
     */
    protected PropertyDefinition<?> getUnfetchedPropertyDefinition(String nativePropertyName)
    {
        return contextBoundObject.object.getType().getPropertyDefinitions().get(nativePropertyName);
    }

    /**
     * The property is first looked up in the cache, if not found it is loaded from the DMS repository.
     * 
     * @param propertyName native property name (not qualified)
     * @return the property with the given name
     * @throws OwException If the property is not defined for this object or it could not be retrieved from the DMS repository.
     */
    public synchronized Property<?> secureProperty(String propertyName) throws OwException
    {
        secureProperties(Arrays.asList(propertyName));
        return contextBoundObject.object.getProperty(propertyName);
    }

    @Override
    public void refresh(Collection<String> propertyNames)
    {
        if (propertyNames != null && !contextBoundObject.context.getFilter().containsAll(propertyNames))
        {
            HashSet<String> propSet = new HashSet<String>(contextBoundObject.context.getFilter());
            propSet.addAll(propertyNames);
            contextBoundObject.context.setFilter(propSet);
        }

        contextBoundObject.object.refreshAndReset();
    }

    @Override
    public void setProperties(Map<String, Object> propertyValues)
    {
        Set<Entry<String, Object>> propertyEntries = propertyValues.entrySet();
        for (Entry<String, Object> entry : propertyEntries)
        {
            if (entry.getValue() != null)
            {
                contextBoundObject.object.setPropertyValue(entry.getKey(), entry.getValue());
            }
            else
            {
                //Do nothing (OWD-4839) see workaround in com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISAbstractNativeObject.setProperties(OwPropertyCollection, Object)
            }
        }
    }

    @Override
    public List<Rendition> secureRenditions(Set<String> renditions, boolean refresh) throws OwException
    {
        Set<String> renditionFilter = contextBoundObject.context.getRenditionFilter();
        if (!renditionFilter.containsAll(renditions) || refresh)
        {
            Set<String> diff = new HashSet<String>(renditions);
            diff.removeAll(renditionFilter);
            this.contextBoundObject = retrieveRenditions(diff);
        }

        return contextBoundObject.object.getRenditions();
    }
}
