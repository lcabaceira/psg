package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;
import org.apache.chemistry.opencmis.commons.data.AllowableActions;
import org.apache.chemistry.opencmis.commons.enums.AclPropagation;
import org.apache.chemistry.opencmis.commons.enums.Action;
import org.apache.chemistry.opencmis.commons.enums.RelationshipDirection;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.server.collections.OwAggregateIterable;
import com.wewebu.ow.server.collections.OwEmptyIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISRepositorySession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.collections.OwCMISObjectIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollectionImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISNativeProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwObjectIDCodeUtil;

/**
 *<p>
 * Abstraction for native based object handling.
 * Derived instances will be concrete/specific type implementation.
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
public abstract class OwCMISAbstractNativeObject<N extends TransientCmisObject, T extends ObjectType, C extends OwCMISNativeObjectClass<T, N>> extends OwCMISAbstractSessionObject<OwCMISNativeSession, C> implements OwCMISNativeObject<N>,
        OwPageableObject<OwCMISObject>
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractNativeObject.class);
    private static final LinkedList<String> REQUIRED_RELATIONSHIP_PROPERTIES = new LinkedList<String>();
    static
    {
        REQUIRED_RELATIONSHIP_PROPERTIES.add(PropertyIds.BASE_TYPE_ID);
        REQUIRED_RELATIONSHIP_PROPERTIES.add(PropertyIds.OBJECT_TYPE_ID);
        REQUIRED_RELATIONSHIP_PROPERTIES.add(PropertyIds.OBJECT_ID);
        REQUIRED_RELATIONSHIP_PROPERTIES.add(PropertyIds.SOURCE_ID);
        REQUIRED_RELATIONSHIP_PROPERTIES.add(PropertyIds.TARGET_ID);
    }

    protected OwCMISTransientObject<N> owTransientObject;

    public OwCMISAbstractNativeObject(OwCMISNativeSession session_p, N nativeObject_p, OperationContext creationContext, C class_p) throws OwException
    {
        super(session_p, class_p);
        replaceNativeObject(nativeObject_p, creationContext);
    }

    public N getNativeObject()
    {
        return this.owTransientObject.getTransientCmisObject();
    }

    @Override
    public OwCMISTransientObject<N> getTransientObject()
    {
        return owTransientObject;
    }

    public boolean canDelete(int context_p) throws OwException
    {
        AllowableActions allowableActions = getNativeObject().getAllowableActions();
        return allowableActions.getAllowableActions().contains(Action.CAN_DELETE_OBJECT);
    }

    @Override
    public String getID()
    {
        return OwObjectIDCodeUtil.encode(getNativeObject().getId());
    }

    @Override
    public String getNativeID()
    {
        String nativeId = getNativeObject().getId();
        //get rid of the version from the end of the native ID
        String[] split = nativeId.split(";");
        return split[0];
    }

    @Override
    public String getDMSID()
    {
        OwCMISDMSIDDecoder dmsidDecoder = getSession().getDMSIDDecoder();
        //TODO:native DMSID should relay on native information (egg. .repositoryId and objectId)
        return OwCMISSimpleDMSID.createDMSID(dmsidDecoder.getDMSIDPrefix(), getResourceID(), getNativeObject().getId());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        OwPropertyCollection allProperties = new OwStandardPropertyCollection();

        C objectClass = getObjectClass();
        Collection<String> requestedPropertyNames = propertyNames_p;
        if (requestedPropertyNames == null)
        {
            Map<String, OwCMISNativePropertyClass<?, ?, ?>> nativePropertyClasses = objectClass.getNativePropertyClasses(false);
            requestedPropertyNames = nativePropertyClasses.keySet();
        }

        //PropName -> NativeName
        Map<String, String> nativeShortNamesMap = new LinkedHashMap<String, String>();
        Set<String> nonNativeProperties = new HashSet<String>();
        for (String propertyName : requestedPropertyNames)
        {
            OwCMISNativePropertyClass nativePropertyClass = objectClass.getNativePropertyClass(propertyName);
            if (nativePropertyClass != null)
            {
                String nativeShortName = nativePropertyClass.getNonQualifiedName();
                nativeShortNamesMap.put(propertyName, nativeShortName);
            }
            else
            {
                nonNativeProperties.add(propertyName);
            }
        }

        Map<String, Property<?>> nativeProperties = this.owTransientObject.secureProperties(nativeShortNamesMap.values());
        for (Entry<String, String> propNameRef : nativeShortNamesMap.entrySet())
        {
            OwCMISNativePropertyClass nativePropertyClass = objectClass.getNativePropertyClass(propNameRef.getKey());
            Property<?> nativeProperty = nativeProperties.get(propNameRef.getValue());
            OwCMISNativeProperty property = nativePropertyClass.from(nativeProperty);
            allProperties.put(nativePropertyClass.getClassName(), property);
        }

        if (propertyNames_p == null && nonNativeProperties.isEmpty())
        {
            allProperties.putAll(super.getProperties(null));//all properties are requested
        }
        else
        {
            allProperties.putAll(super.getProperties(nonNativeProperties));
        }

        checkPropertyCollection(requestedPropertyNames, allProperties);

        return allProperties;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public OwCMISProperty<?> getProperty(String propertyName_p) throws OwException
    {
        OwCMISNativePropertyClass nativePropertyClass = getObjectClass().getNativePropertyClass(propertyName_p);
        if (nativePropertyClass == null)
        {
            // maybe a virtual property
            return super.getProperty(propertyName_p);
        }

        String propertyNativeName = nativePropertyClass.getNonQualifiedName();
        Property<?> nativeProperty = owTransientObject.secureProperty(propertyNativeName);

        return nativePropertyClass.from(nativeProperty);
    }

    @Override
    public boolean canSetProperties(int iContext_p)
    {
        AllowableActions allowableActions = getNativeObject().getAllowableActions();
        return allowableActions.getAllowableActions().contains(Action.CAN_UPDATE_PROPERTIES);
    }

    @Override
    public String getName()
    {
        try
        {
            return owTransientObject.secureObject(PropertyIds.NAME).getName();
        }
        catch (OwException e)
        {
            LOG.error("Invalid objec state.", e);
            return "N/A";
        }
    }

    @Override
    public OwCMISPermissionCollection getPermissions() throws OwException
    {
        OwCMISPermissionCollection perm = new OwCMISPermissionCollectionImpl(getNativeObject().getCmisObject(), getSession().getOpenCMISSession());
        return perm;
    }

    @Override
    public void setPermissions(OwPermissionCollection permissions_p) throws OwException
    {
        if (!(permissions_p instanceof OwCMISPermissionCollection))
        {
            LOG.error("OwCMISAbstractNativeObject.setPermissions: Invalid permissions type/class, class = " + permissions_p.getClass());
            throw new OwInvalidOperationException("The provided permission object is not valid for setPermission call.");
        }
        OwCMISPermissionCollection perms = (OwCMISPermissionCollection) permissions_p;
        List<Ace> added = perms.getDiff().getAdded();
        List<Ace> deleted = perms.getDiff().getDeleted();
        if (LOG.isDebugEnabled())
        {
            if (added != null)
            {
                LOG.debug("=============== Added ACE ========================");
                for (Ace ace : added)
                {
                    LOG.debug("+ User = " + ace.getPrincipalId());
                    for (String perm : ace.getPermissions())
                    {
                        LOG.debug(perm);
                    }
                }
            }
            if (deleted != null)
            {
                LOG.debug("=============== Deleted ACE ========================");
                for (Ace ace : deleted)
                {
                    LOG.debug("- User = " + ace.getPrincipalId());
                    for (String perm : ace.getPermissions())
                    {
                        LOG.debug(perm);
                    }
                }
            }
        }
        //        AclPropagation propagation = getSession().getOpenCMISSession().getRepositoryInfo().getAclCapabilities().getAclPropagation();

        Acl lst = getNativeObject().getCmisObject().applyAcl(added, deleted, AclPropagation.REPOSITORYDETERMINED);

        if (LOG.isDebugEnabled())
        {
            LOG.debug("============ Answer ==============");
            if (lst != null)
            {
                for (Ace ace : lst.getAces())
                {
                    LOG.debug(" User = " + ace.getPrincipalId());
                    for (String perm : ace.getPermissions())
                    {
                        LOG.debug(perm);
                    }
                }
            }
        }
    }

    @Override
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        Map<String, Object> convertedProperties = getObjectClass().convertToNativeProperties(properties_p);

        if (!convertedProperties.isEmpty())
        {
            owTransientObject.setProperties(convertedProperties);
            N nativeObject = owTransientObject.getTransientCmisObject();
            //Bug in TransientObject handling for update, go back to CmisObject
            CmisObject cmisObj = nativeObject.getCmisObject();
            //            ObjectId updatedObjectId = nativeObject.save();
            ObjectId updatedObjectId = cmisObj.updateProperties(convertedProperties, true);//do update using non-transient object

            afterPropertiesSet(updatedObjectId);
        }

    }

    protected void afterPropertiesSet(ObjectId updatedObjectId)
    {
        OwCMISNativeSession mySession = getSession();
        N nativeObject = getNativeObject();

        if (!updatedObjectId.equals(nativeObject))
        {
            OperationContext creationContext = owTransientObject.getTransientContext();
            CmisObject versionUpdatedObject = mySession.getOpenCMISSession().getObject(updatedObjectId, creationContext);
            replaceNativeObject((N) versionUpdatedObject.getTransientObject(), creationContext);
        }
        else
        {
            nativeObject.refreshAndReset();
        }
    }

    protected void reloadNativeObject()
    {
        if (!owTransientObject.isDetached())
        {
            Session nativeSession = getSession().getOpenCMISSession();

            N object = getNativeObject();
            nativeSession.removeObjectFromCache(object.getId());
            OperationContext context = owTransientObject.getTransientContext();

            CmisObject reloadedObject = nativeSession.getObject(getNativeObject(), context);
            replaceNativeObject((N) reloadedObject.getTransientObject(), context);
        }
    }

    protected void replaceNativeObject(N object, OperationContext creationContext)
    {
        C myObjectClass = getObjectClass();
        this.owTransientObject = myObjectClass.newTransientObject(object, creationContext);
    }

    protected boolean replaceNativeObject(String id)
    {
        return replaceNativeObject(id, this.owTransientObject.getTransientContext());
    }

    protected boolean replaceNativeObject(String id, OperationContext creationContext)
    {
        N transientObject = owTransientObject.getTransientCmisObject();
        if (!id.equals(transientObject.getId()))
        {
            Session nativeSession = getSession().getOpenCMISSession();

            CmisObject newObject = nativeSession.getObject(id, creationContext);
            replaceNativeObject((N) newObject.getTransientObject(), creationContext);

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void detach()
    {
        owTransientObject.detach();
    }

    @Override
    public void delete() throws OwException
    {
        N natvieObject = getNativeObject();
        natvieObject.delete(true);
        natvieObject.save();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void refreshProperties(Collection propertyClassNames_p) throws OwException
    {
        //TODO: fix property names
        this.owTransientObject.refresh(propertyClassNames_p);
    }

    @Override
    public void refreshProperties() throws OwException
    {
        reloadNativeObject();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public OwObjectCollection getChilds(int[] objectTypes, Collection propertyNames, OwSort sort, int maxSize, int versionSelection, OwSearchNode filterCriteria) throws OwException
    {
        boolean getRelationships = false;
        for (int type : objectTypes)
        {
            getRelationships = type == OwObjectReference.OBJECT_TYPE_LINK;
            if (getRelationships)
            {
                break;
            }
        }

        if (getRelationships)
        {
            OwCMISNativeSession mySession = getSession();
            RelationshipDirection direction = getRelationDirection(filterCriteria);

            List<String> relationTypes = getRelationTypeFilter(filterCriteria);
            if (relationTypes == null)
            {
                relationTypes = new LinkedList<String>();
                relationTypes.add(null);
            }

            OwStandardObjectCollection relations = new OwStandardObjectCollection();
            StopWatch stopWatch = new Log4JStopWatch("OwCMISObject.getChilds", "getRelationships(Id, true, direction, null, context)");
            for (Iterator<String> it = relationTypes.iterator(); it.hasNext() && relations.size() <= maxSize;)
            {
                int page;
                if (relations.size() == 0 && maxSize == Integer.MAX_VALUE)
                {
                    page = maxSize;
                }
                else
                {
                    page = (maxSize - relations.size()) + 1;
                }
                String type = it.next();
                OperationContext ctx = mySession.createOperationContext(propertyNames == null ? REQUIRED_RELATIONSHIP_PROPERTIES : propertyNames, page, getSession().getNativeObjectClass(ObjectType.RELATIONSHIP_BASETYPE_ID));
                ItemIterable<Relationship> relationIterator;
                if (type != null)
                {//retrieve only specific relationships
                    ObjectType nativeType = mySession.getOpenCMISSession().getTypeDefinition(type);
                    relationIterator = mySession.getOpenCMISSession().getRelationships(getNativeObject(), false, direction, nativeType, ctx);
                }
                else
                {//retrieve all relation types
                    relationIterator = mySession.getOpenCMISSession().getRelationships(getNativeObject(), true, direction, null, ctx);
                }

                boolean finished = false;
                int count = 0;
                while (!finished)
                {
                    ItemIterable<Relationship> currentPage = relationIterator.skipTo(count).getPage();
                    for (CmisObject child : currentPage)
                    {
                        TransientCmisObject transinetChild = child.getTransientObject();
                        OwCMISNativeObject<TransientCmisObject> childObject = mySession.from(transinetChild, null);
                        relations.add(childObject);
                        count++;
                    }
                    if (!currentPage.getHasMoreItems() || relations.size() > maxSize)
                    {
                        finished = true;
                    }
                }
            }

            OwStandardObjectCollection result = null;
            if (relations.size() > maxSize)
            {
                result = new OwStandardObjectCollection();
                result.addAll(relations.subList(0, maxSize));
                result.setComplete(false);
                try
                {
                    result.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
                    result.setAttribute(OwObjectCollection.ATTRIBUTE_SIZE, result.size());
                }
                catch (Exception e)
                {
                    throw new OwServerException("Could not set " + OwObjectCollection.ATTRIBUTE_IS_COMPLETE + " attribute.", e);
                }
            }
            else
            {
                result = relations;
            }

            stopWatch.stop();
            return relations;
        }
        else
        {
            return super.getChilds(objectTypes, propertyNames, sort, maxSize, versionSelection, filterCriteria);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwCMISObject> getChildren(OwLoadContext loadContext) throws OwException
    {
        int[] objectTypes = loadContext.getObjectTypes();
        OwSearchNode filterCriteria = loadContext.getFilter();

        //Only handle relationships
        boolean getRelationships = false;
        for (int type : objectTypes)
        {
            getRelationships = type == OwObjectReference.OBJECT_TYPE_LINK;
            if (getRelationships)
            {
                break;
            }
        }

        if (getRelationships)
        {
            RelationshipDirection direction = getRelationDirection(filterCriteria);
            OwCMISRepositorySession mySession = (OwCMISRepositorySession) getSession();
            OperationContext context = mySession.createOperationContext(loadContext.getPropertyNames(), loadContext.getSorting(), loadContext.getPageSize(), getSession().getNativeObjectClass(ObjectType.FOLDER_BASETYPE_ID), getSession()
                    .getNativeObjectClass(ObjectType.DOCUMENT_BASETYPE_ID));

            List<String> relationTypes = getRelationTypeFilter(filterCriteria);
            if (null == relationTypes)
            {
                //retrieve all relation types
                ItemIterable<Relationship> relationIterator = mySession.getOpenCMISSession().getRelationships(getNativeObject(), true, direction, null, context);
                return new OwCMISObjectIterable(relationIterator, mySession);
            }
            else
            {
                List<OwIterable<OwCMISObject>> iterables = new LinkedList<OwIterable<OwCMISObject>>();
                for (String type : relationTypes)
                {
                    //retrieve only specific relationships
                    ObjectType nativeType = mySession.getOpenCMISSession().getTypeDefinition(type);
                    ItemIterable<Relationship> relationIterator = mySession.getOpenCMISSession().getRelationships(getNativeObject(), false, direction, nativeType, context);
                    iterables.add(new OwCMISObjectIterable(relationIterator, mySession));
                }

                return OwAggregateIterable.forList(iterables);
            }
        }
        else
        {
            return OwEmptyIterable.instance();
        }
    }

    @Override
    public boolean hasChilds(int[] objectTypes, int context) throws OwException
    {
        for (int type : objectTypes)
        {
            if (type == OwObjectReference.OBJECT_TYPE_LINK)
            {
                Set<?> objSet = getSession().getObjectClasses(new int[] { type }, false, true);
                if (objSet == null || objSet.isEmpty())
                {
                    //relation ship not supported
                    break;
                }
                else
                {
                    OperationContext ctx = getSession().createOperationContext(null, 1, getSession().getNativeObjectClass(ObjectType.RELATIONSHIP_BASETYPE_ID));
                    ItemIterable<Relationship> relationIterator = getSession().getOpenCMISSession().getRelationships(getNativeObject(), true, RelationshipDirection.EITHER, null, ctx);
                    boolean hasItem = false;
                    try
                    {
                        hasItem = relationIterator.getHasMoreItems();
                    }
                    catch (CmisInvalidArgumentException e)
                    {
                        LOG.info(e.getMessage());
                    }
                    return hasItem;
                }
            }
        }
        return super.hasChilds(objectTypes, context);
    }

    /**
     * Return a list of type-Id's (String), which should be retrieved.
     * <p>Can return null if provide filter criteria is null, or no type restriction in criteria found.</p>
     * @param filterCriteria OwSearchNode (can be null)
     * @return List of Id's (can return null)
     */
    protected List<String> getRelationTypeFilter(OwSearchNode filterCriteria)
    {
        if (filterCriteria != null)
        {
            List<?> crits = filterCriteria.getCriteriaList(OwSearchNode.FILTER_NONE);
            Iterator<?> it = crits.iterator();
            while (it.hasNext())
            {
                OwSearchCriteria crit = (OwSearchCriteria) it.next();
                if (OwObjectLink.OW_LINK_TYPE_FILTER.equals(crit.getClassName()))
                {
                    Object[] values = (Object[]) crit.getValue();
                    if (values != null)
                    {
                        return Arrays.asList((String[]) values);
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * This method is used by getChilds implementation for resolving of the property {@link OwObjectLink#OW_LINK_RELATION} to corresponding native value.
     * <p>
     * Will return RelationshipDirection.EITHER by default, if provided search node is null or no definition of direction can be found.
     * </p>
     * @param filterCriteria OwSearchNode (can be null)
     * @return RelationshipDirection
     */
    protected RelationshipDirection getRelationDirection(OwSearchNode filterCriteria)
    {
        if (filterCriteria != null)
        {
            List<?> crits = filterCriteria.getCriteriaList(OwSearchNode.FILTER_NONE);
            Iterator<?> it = crits.iterator();
            while (it.hasNext())
            {
                OwSearchCriteria crit = (OwSearchCriteria) it.next();
                if (OwObjectLink.OW_LINK_RELATION.equals(crit.getClassName()))
                {
                    OwObjectLinkRelation direction = OwObjectLinkRelation.valueOf(crit.getValue().toString());
                    switch (direction)
                    {
                        case INBOUND:
                            return RelationshipDirection.TARGET;
                        case OUTBOUND:
                            return RelationshipDirection.SOURCE;
                        case BOTH:
                        default:
                            return RelationshipDirection.EITHER;
                    }
                }
            }
        }
        return RelationshipDirection.EITHER;
    }

    @Override
    public List<OwCMISRendition> retrieveRenditions(Set<String> filter, boolean refresh) throws OwException
    {
        List<Rendition> renditions = owTransientObject.secureRenditions(filter, refresh);
        List<OwCMISRendition> owRenditions = new LinkedList<OwCMISRendition>();
        for (Rendition rendition : renditions)
        {
            owRenditions.add(new OwCMISNativeRenditionImpl(rendition));
        }

        return owRenditions;

    }
}
