package com.wewebu.ow.server.ecmimpl.opencmis;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ObjectData;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.app.id.viid.OwVIIdType;
import com.wewebu.ow.server.collections.OwIterableObjectCollectionConverter;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwManagedSemiVirtualRecordConfiguration;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSemiVirtualRecordClass;
import com.wewebu.ow.server.ecmimpl.opencmis.collections.OwCMISQueryIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISQueryResultConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.role.OwRoleOptionAttributeBag;
import com.wewebu.ow.server.util.OwObjectIDCodeUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of OwCMISNativeSession interface.
 * This implementation is based on the <code>org.apache.chemistry.opencmis.client.api.Session</code>
 * and uses additional helper and factory classes.
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
@SuppressWarnings("unchecked")
public class OwCMISRepositorySession implements OwCMISNativeSession
{
    private static Logger LOG = OwLog.getLogger(OwCMISRepositorySession.class);

    private Session session;
    private OwCMISDMSIDDecoder dmsidDecoder;
    private OwCMISRepositoryResource resource;
    private Map<String, ?> parameters;

    private OwCMISObjectClassFactory objectClassFactory;
    private OwCMISNativePropertyClassFactory propertyClassFactory;
    private TimeZone timeZone;

    /** selective ID's delegate for selective configuration via OwXMLUtilFilter */
    private OwRoleOptionAttributeBag m_roleoptionIDs = new OwRoleOptionAttributeBag();

    private OwCMISNetwork network;

    public OwCMISRepositorySession(OwCMISRepositoryResource resource_p, Map<String, ?> parameters_p, OwCMISNetwork network) throws OwException
    {
        this(resource_p.getRepository().createSession(), resource_p, parameters_p, network);
    }

    public OwCMISRepositorySession(Session session_p, OwCMISRepositoryResource resource_p, Map<String, ?> parameters_p, OwCMISNetwork network) throws OwException
    {
        super();
        this.parameters = parameters_p;
        this.network = network;
        this.session = session_p;
        this.resource = resource_p;
        this.dmsidDecoder = createSessionProduct(OwCMISDMSIDDecoder.class, OwCMISSessionParameter.DMSID_DECODER_CLASS, OwCMISSimpleDMSIDDecoder.class);
        this.propertyClassFactory = createSessionProduct(OwCMISNativePropertyClassFactory.class, OwCMISSessionParameter.PROPERTY_CLASS_FACTORY_CLASS, OwCMISSimplePropertyClassFactory.class);
        this.objectClassFactory = createSessionProduct(OwCMISObjectClassFactory.class, OwCMISSessionParameter.OBJECT_CLASS_FACTORY_CLASS, OwCMISSimpleObjectClassFactory.class);

        String timeZoneId = (String) this.parameters.get(OwCMISSessionParameter.TIME_ZONE_ID);
        if (timeZoneId == null)
        {
            this.timeZone = TimeZone.getDefault();
        }
        else
        {
            this.timeZone = TimeZone.getTimeZone(timeZoneId);
        }

    }

    private <P> P createSessionProduct(Class<P> class_p, String classParameter_p, Class<? extends P> defaultClass_p) throws OwException
    {
        try
        {
            String classname = (String) parameters.get(classParameter_p);

            Class<? extends P> factoryClass;
            if (classname == null)
            {
                factoryClass = defaultClass_p;
            }
            else
            {
                factoryClass = (Class<? extends P>) Class.forName(classname);
                if (!(class_p.isAssignableFrom(factoryClass)))
                {
                    throw new OwInvalidOperationException("Invalid property class factory java class : " + factoryClass + ". A " + class_p.getName() + " based class expected.");
                }
            }

            try
            {
                Constructor<? extends P> constructor = factoryClass.getConstructor(OwCMISNativeSession.class);
                P product = constructor.newInstance(this);

                return product;
            }
            catch (NoSuchMethodException en)
            {
                try
                {
                    Constructor<? extends P> constructor = factoryClass.getConstructor(OwCMISSession.class);
                    P product = constructor.newInstance(this);

                    return product;
                }
                catch (NoSuchMethodException e)
                {
                    Constructor<? extends P> constructor = factoryClass.getConstructor();
                    P product = constructor.newInstance();

                    return product;
                }
            }

        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Unable to create object factory: " + e, e);
        }
    }

    @Override
    public OwCMISResourceInfo getResourceInfo()
    {
        return new OwCMISRepositoryResourceInfo(session.getRepositoryInfo());
    }

    @Override
    public OwCMISObject getObject(String dmsId_p, boolean refresh_p) throws OwException
    {
        Session natSes = getOpenCMISSession();
        OwCMISDMSID dmsid = getDMSIDDecoder().createDMSID(dmsId_p);
        if (LOG.isInfoEnabled())
        {
            LOG.info("Retrival of object by Id = " + dmsid.getCMISID());
        }
        OperationContext opCtx = natSes.createOperationContext(null, false, true, false, IncludeRelationships.NONE, null, false, null, !refresh_p, 1);
        CmisObject object = natSes.getObject(dmsid.getCMISID(), opCtx);
        return from(object.getTransientObject(), null);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession#getObjectByPath(java.lang.String, boolean)
     */
    @Override
    public OwCMISObject getObjectByPath(String path, boolean refresh) throws OwException
    {
        OperationContext opCtx = getOpenCMISSession().createOperationContext(getDefaultFilter(), false, true, false, IncludeRelationships.NONE, null, false, null, !refresh, 1);
        CmisObject cmisNativeObject = this.session.getObjectByPath(path, opCtx);

        return from(cmisNativeObject.getTransientObject(), null);
    }

    @Override
    public OwCMISNativeObjectClass<ObjectType, TransientCmisObject> getNativeObjectClass(String objectClassName_p) throws OwException
    {
        ObjectType typeDefinition = this.objectClassFactory.retrieveObjectType(objectClassName_p, getOpenCMISSession());//session.getTypeDefinition(objectClassName_p);
        //explicit cast required for Java 1.6 compiler bug fix 
        return (OwCMISNativeObjectClass<ObjectType, TransientCmisObject>) from(typeDefinition);
    }

    @Override
    public OwCMISNativeObject<TransientCmisObject> getNativeObject(String objectNativeId, Collection<String> propertyNames, Map<String, ?> conversionParameters) throws OwException
    {
        OperationContext opCtx = this.session.createOperationContext();
        if (null != propertyNames)
        {
            Set<String> propertyfilter = new HashSet<String>(propertyNames);
            propertyfilter.addAll(getDefaultFilter());
            opCtx.setFilter(propertyfilter);
        }

        CmisObject cmisNativeObject = this.session.getObject(objectNativeId, opCtx);
        TransientCmisObject transientCmisObject = cmisNativeObject.getTransientObject();

        return from(transientCmisObject, conversionParameters);
    }

    public OwCMISNativeObjectClass<ObjectType, TransientCmisObject> getObjectClass(String objectClassName_p) throws OwException
    {
        return getNativeObjectClass(objectClassName_p);
    }

    @Override
    public String createObject(boolean promote_p, Object mode_p, String objectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwCMISObject parent_p, String mimeType_p,
            String mimeParameter_p, boolean keepCheckedOut_p) throws OwException
    {
        //request the object type for creation
        OwCMISObjectClass clazz = getObjectClass(objectClassName_p);
        String nativeId = clazz.createNewObject(promote_p, mode_p, properties_p, permissions_p, content_p, parent_p, mimeType_p, mimeParameter_p, keepCheckedOut_p);

        String dmsId = OwCMISSimpleDMSID.createDMSID(getDMSIDDecoder().getDMSIDPrefix(), resource.getID(), nativeId);
        return dmsId;
    }

    @Override
    public OwCMISNativePropertyClassFactory getNativePropertyClassFactory()
    {
        return propertyClassFactory;
    }

    @Override
    public Object getParameterValue(String name_p)
    {
        return parameters.get(name_p);
    }

    @Override
    public Locale getLocale()
    {
        return session.getLocale();
    }

    @Override
    public OwCMISDMSIDDecoder getDMSIDDecoder()
    {
        return dmsidDecoder;
    }

    @Override
    public TimeZone getTimeZone()
    {
        return timeZone;
    }

    @Override
    public OwCMISQueryIterable query(OwQueryStatement statement, OwLoadContext loadContext) throws OwException
    {
        //TODO: make query methods coherent - remove duplicated search when context gets extension or extra CMIS parameters
        LOG.debug("session-query-iterable: " + statement);
        return new OwCMISQueryIterable(this, statement, loadContext);
    }

    private OwCMISQueryIterable query(OwQueryStatement statement, OwLoadContext loadContext, final boolean includeAllowableActions, final IncludeRelationships includeRelationships, final String renditionFilter) throws OwException
    {
        //TODO: make query methods coherent - remove duplicated search when context gets extension or extra CMIS parameters
        LOG.debug("legacy-session-query-iterable: " + statement);
        return new OwCMISQueryIterable(this, statement, loadContext) {
            @Override
            protected OperationContext toOperationContext(OwLoadContext context)
            {
                OperationContext operationContext = super.toOperationContext(context);
                operationContext.setIncludeRelationships(includeRelationships);
                operationContext.setIncludeAllowableActions(includeAllowableActions);
                operationContext.setRenditionFilterString(renditionFilter);
                return operationContext;
            }
        };
    }

    @Override
    public OwObjectCollection query(OwQueryStatement statement, boolean searchAllVersions, boolean includeAllowableActions, IncludeRelationships includeRelationships, String renditionFilter, BigInteger maxItems, BigInteger skipCount)
            throws OwException
    {
        StopWatch stopWatch = new Log4JStopWatch();

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setPageSize(maxItems.intValue());
        loadContext.setVersionSelection(searchAllVersions ? OwSearchTemplate.VERSION_SELECT_ALL : OwSearchTemplate.VERSION_SELECT_DEFAULT);

        OwCMISQueryIterable iterable = query(statement, loadContext, includeAllowableActions, includeRelationships, renditionFilter);

        stopWatch.lap("OwCMISRepositorySession.legacy-query", "query");

        OwIterableObjectCollectionConverter objectCollection = new OwIterableObjectCollectionConverter(iterable, skipCount.longValue(), maxItems.intValue());

        stopWatch.stop("OwCMISRepositorySession.legacy-query", "createOwObjects");
        return objectCollection;

        //TODO: make query methods coherent - remove duplicated search when context gets extension or extra CMIS parameters
        //      the bellow code is the old query implementation - to be left in place until iterable implementation is finished
        /*
                String searchOp = statement.createSQLString().toString();

                OperationContext opContext = this.session.createOperationContext();
                opContext.setMaxItemsPerPage(maxItems.intValue());
                opContext.setIncludeRelationships(includeRelationships);

                LOG.debug("query: " + searchOp);

                StopWatch stopWatch = new Log4JStopWatch();
                ItemIterable<QueryResult> searchResult = this.session.query(searchOp, searchAllVersions, opContext);
                searchResult = searchResult.skipTo(skipCount.intValue()).getPage();
                searchResult.iterator().hasNext();//need to ask the iterator, otherwise the search is not fired!

                stopWatch.lap("OwCMISRepositorySession.query", "query");

                OwObjectCollection result = new OwStandardObjectCollection();

                for (QueryResult queryResult : searchResult)
                {
                    //TODO: extract context creation
                    OperationContext copy = this.session.createOperationContext();
                    copy.setMaxItemsPerPage(maxItems.intValue());
                    copy.setIncludeRelationships(includeRelationships);
                    List<PropertyData<?>> properties = queryResult.getProperties();
                    Set<String> propertyIds = new HashSet<String>();
                    for (PropertyData<?> propertyData : properties)
                    {
                        propertyIds.add(propertyData.getId());
                    }
                    copy.setFilter(propertyIds);
                    result.add(this.createCMISObject(statement, queryResult, copy));
                }
                stopWatch.stop("OwCMISRepositorySession.query", "createOwObjects");
                return result;
        */
    }

    public OwCMISObject createCMISObject(OwQueryStatement statement, QueryResult queryResult, OperationContext operationContext) throws OwException
    {
        Map<String, Object> localConversionParameters = new HashMap<String, Object>();
        localConversionParameters.put(OwCMISConversionParameters.CREATION_CONTEXT, operationContext);

        Map<String, ?> conversionParameters = defaultConversionParameters(localConversionParameters);

        PropertyData<String> mainTypeId = queryResult.getPropertyById(PropertyIds.OBJECT_TYPE_ID);
        OwCMISNativeObjectClass<ObjectType, TransientCmisObject> mainObjectClass = getNativeObjectClass(mainTypeId.getFirstValue());
        OwCMISQueryResultConverter<TransientCmisObject> converter = mainObjectClass.getQueryResultConverter(queryResult, statement, operationContext);
        TransientCmisObject cmisObject = converter.toCmisObject(queryResult, statement, operationContext);

        OwCMISNativeObjectClass<?, TransientCmisObject> queryResultClass = objectClassFactory.createObjectClassOf(cmisObject);
        OwCMISNativeObject<?> object = queryResultClass.from(cmisObject, conversionParameters);

        return checkObject(object, null);

    }

    protected Map<String, ?> defaultConversionParameters(Map<String, ?> conversionParameters)
    {
        conversionParameters = (conversionParameters == null ? OwCMISConversionParameters.NO_PARAMETERS : conversionParameters);

        Map<String, Object> conversionParametersCopy = new HashMap<String, Object>(conversionParameters);

        if (!conversionParametersCopy.containsKey(OwCMISConversionParameters.PRESERVE_VERSION))
        {
            conversionParametersCopy.put(OwCMISConversionParameters.PRESERVE_VERSION, getNetwork().getNetworkConfiguration().isPreservedVersion());
        }

        if (!conversionParametersCopy.containsKey(OwCMISConversionParameters.CREATION_CONTEXT))
        {

            conversionParametersCopy.put(OwCMISConversionParameters.CREATION_CONTEXT, getDefaultOperationContext());
        }

        return conversionParametersCopy;
    }

    @Override
    public <O extends TransientCmisObject> OwCMISNativeObject<O> from(O cmisObject, Map<String, ?> conversionParameters) throws OwException
    {

        OwCMISNativeObjectClass<?, O> objClass = classOf(cmisObject);
        Map<String, ?> conversionParametersCopy = defaultConversionParameters(conversionParameters);
        return checkObject(objClass.from(cmisObject, conversionParametersCopy), conversionParametersCopy);
    }

    /**
     * Check the new OwObject if additional changes/modifications needed.
     * By default used to attach semi-virtual structures.
     * @param wrapedObj OwCMISNativeObject recently native object wrapper
     * @param conversionParameters Map of defined conversion Parameters
     * @return OwCMISNativeObject
     * @throws OwException
     */
    protected <O extends TransientCmisObject> OwCMISNativeObject<O> checkObject(OwCMISNativeObject<O> wrapedObj, Map<String, ?> conversionParameters) throws OwException
    {
        OwManagedSemiVirtualRecordConfiguration semiVfHandler = getNetwork().getNetworkConfiguration().getSemiVirtualConfiguration();
        OwCMISNetwork myNetwork = getNetwork();
        OwSemiVirtualRecordClass semiDef = semiVfHandler.semiVirtualFolderForObjectClass(wrapedObj.getObjectClass().getClassName(), myNetwork.getRoleManager(), myNetwork.getNetworkConfiguration().getConfigNode());
        if (semiDef != null)
        {
            return new OwCMISSemiVirtualFolderObject<O>(wrapedObj, semiDef, getNetwork());
        }
        else
        {
            return wrapedObj;
        }
    }

    @Override
    public OwCMISObject getRootFolder() throws OwException
    {
        Folder root = session.getRootFolder();
        return from(root.getTransientFolder(), null);
    }

    @Override
    public <T extends ObjectType> OwCMISNativeObjectClass<T, ?> from(T type_p) throws OwException
    {
        return objectClassFactory.createObjectClass(type_p);
    }

    @Override
    public <O extends TransientCmisObject> OwCMISNativeObjectClass<?, O> classOf(O object_p) throws OwException
    {
        return objectClassFactory.createObjectClassOf(object_p);
    }

    @Override
    public Session getOpenCMISSession()
    {
        return session;
    }

    @Override
    public OwCMISResource getResource()
    {
        return resource;
    }

    protected OwCMISNativeObjectClass<?, ?>[] createDefaultClassContext() throws OwException
    {
        OwCMISNativeObjectClass<?, ?> document = getNativeObjectClass(BaseTypeId.CMIS_DOCUMENT.value());
        OwCMISNativeObjectClass<?, ?> folder = getNativeObjectClass(BaseTypeId.CMIS_FOLDER.value());
        OwCMISNativeObjectClass<?, ?> policy = getNativeObjectClass(BaseTypeId.CMIS_POLICY.value());
        OwCMISNativeObjectClass<?, ?> relationship = getNativeObjectClass(BaseTypeId.CMIS_RELATIONSHIP.value());

        return new OwCMISNativeObjectClass[] { document, folder, policy, relationship };
    }

    protected OperationContext getDefaultOperationContext()
    {
        Set<String> filter = getDefaultFilter();
        OperationContext operationContext = this.session.getDefaultContext();
        if (operationContext == null)
        {
            operationContext = this.session.createOperationContext();
        }
        if (operationContext.getFilter() != null)
        {
            filter.addAll(operationContext.getFilter());
        }
        operationContext.setFilter(filter);

        return operationContext;
    }

    protected Set<String> getDefaultFilter()
    {
        //TODO :customize for each base type ? ??!?

        Set<String> filter = new HashSet<String>();
        filter.add(PropertyIds.OBJECT_ID);
        filter.add(PropertyIds.OBJECT_TYPE_ID);
        filter.add(PropertyIds.BASE_TYPE_ID);
        filter.add(PropertyIds.NAME);
        filter.add(PropertyIds.CHANGE_TOKEN);

        //optimizing versions view
        filter.add(PropertyIds.CREATION_DATE);
        filter.add(PropertyIds.LAST_MODIFICATION_DATE);
        filter.add(PropertyIds.LAST_MODIFIED_BY);

        //-----------------------------------
        filter.add(PropertyIds.VERSION_SERIES_ID);
        filter.add(PropertyIds.IS_LATEST_MAJOR_VERSION);
        filter.add(PropertyIds.IS_LATEST_VERSION);
        filter.add(PropertyIds.IS_MAJOR_VERSION);
        filter.add(PropertyIds.IS_VERSION_SERIES_CHECKED_OUT);
        filter.add(PropertyIds.VERSION_SERIES_CHECKED_OUT_BY);
        filter.add(PropertyIds.VERSION_LABEL);
        filter.add(PropertyIds.CONTENT_STREAM_LENGTH);
        filter.add(PropertyIds.CONTENT_STREAM_FILE_NAME);
        filter.add(PropertyIds.CONTENT_STREAM_ID);
        filter.add(PropertyIds.CONTENT_STREAM_MIME_TYPE);
        filter.add(PropertyIds.IS_IMMUTABLE);

        return filter;
    }

    @Override
    public OperationContext createOperationContext(Collection<String> filterPropertyNames_p, int maxItemsPerPage_p, OwCMISNativeObjectClass<?, ?>... classContext_p)
    {
        return createOperationContext(filterPropertyNames_p, null, maxItemsPerPage_p, classContext_p);
    }

    @Override
    public OperationContext createOperationContext(Collection<String> filterPropertyNames_p, OwSort sorting_p, int maxItemsPerPage_p, OwCMISNativeObjectClass<?, ?>... classContext_p)
    {
        Set<String> filter = getDefaultFilter();

        if (classContext_p == null)
        {
            try
            {
                classContext_p = createDefaultClassContext();
            }
            catch (OwException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (filterPropertyNames_p != null && classContext_p != null)
        {
            for (String propertyName : filterPropertyNames_p)
            {
                OwCMISPropertyClass<?> propertyClass = findPropertyDefinition(propertyName, classContext_p);
                if (propertyClass == null || propertyClass.getQueryName() == null)
                {
                    LOG.debug("Missing property class required for operation context : " + propertyName);
                }
                else
                {
                    String queryName = propertyClass.getQueryName();
                    filter.add(queryName);
                }
            }
        }

        boolean includeAcls = true;
        boolean includeAllowableActions = true;
        boolean includePolicies = true;
        IncludeRelationships includeRelationships = IncludeRelationships.NONE;
        Set<String> renditionFilter = null;
        boolean includePathSegments = true;
        String orderBy = getSortString(sorting_p, classContext_p);
        boolean cacheEnabled = true;
        int maxItemsPerPage = maxItemsPerPage_p;

        return session.createOperationContext(filter, includeAcls, includeAllowableActions, includePolicies, includeRelationships, renditionFilter, includePathSegments, orderBy, cacheEnabled, maxItemsPerPage);
    }

    @Override
    public Set<OwCMISObjectClass> getObjectClasses(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {
        Set<OwCMISObjectClass> classes = new HashSet<OwCMISObjectClass>();

        int[] types = iTypes_p;

        if (types == null)
        {
            types = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER, OwObjectReference.OBJECT_TYPE_LINK };
        }

        for (int type : types)
        {
            switch (type)
            {
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                {
                    OwCMISObjectClass objClass = getObjectClass(ObjectType.DOCUMENT_BASETYPE_ID);
                    classes.addAll(this.classesfromRootClass(objClass, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p));
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                {
                    OwCMISObjectClass objClass = getObjectClass(ObjectType.FOLDER_BASETYPE_ID);
                    classes.addAll(this.classesfromRootClass(objClass, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p));
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_LINK:
                {
                    try
                    {
                        OwCMISObjectClass objClass = getObjectClass(ObjectType.RELATIONSHIP_BASETYPE_ID);
                        classes.addAll(this.classesfromRootClass(objClass, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p));
                    }
                    catch (OwException ex)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Could not retrieve relationship, maybe not supported", ex);
                        }
                    }

                }
                    break;
                default:
                    ;//nothing should be returned
            }
        }

        return classes;
    }

    private Collection<? extends OwCMISObjectClass> classesfromRootClass(OwCMISObjectClass objClass, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {
        Set<OwCMISObjectClass> result = new HashSet<OwCMISObjectClass>();
        result.add(objClass);
        if (!fRootOnly_p)
        {
            List<OwCMISObjectClass> children = objClass.getChilds(null, fExcludeHiddenAndNonInstantiable_p);
            for (OwCMISObjectClass owCMISObjectClass : children)
            {
                result.addAll(classesfromRootClass(owCMISObjectClass, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p));
            }
        }
        return result;
    }

    @Override
    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwNetwork network) throws OwException
    {
        try
        {
            OwCMISObjectClass clazz;
            if (objectclass_p instanceof OwCMISObjectClass)
            {
                clazz = (OwCMISObjectClass) objectclass_p;
            }
            else
            {
                clazz = getObjectClass(objectclass_p.getClassName());
            }
            OwXMLUtil initValues = getNetwork().getNetworkConfiguration().getCreationInitialValuesConfig(clazz.getClassName());
            OwObjectSkeleton skeleton = clazz.createSkeletonObject(network, this.resource, this, initValues);

            String className = clazz.getPropertyClass(PropertyIds.OBJECT_TYPE_ID).getFullQualifiedName().toString();
            skeleton.setField(className, objectclass_p.getClassName());
            return skeleton;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            LOG.error("OwCMISRepositorySession.createObjectSkeleton(): Problem instantiation of Skeleton object!", ex);
            throw new OwServerException(new OwString("opencmis.OwCMISRepositorySession.err.skeleton.object", "An error occurred while creating object!"), ex);
        }
    }

    public OwCMISNetwork getNetwork()
    {
        return this.network;
    }

    @Override
    public OwCMISObject getObject(OwVIId viid) throws OwException
    {
        CmisObject nativeObject;
        Session natSes = getOpenCMISSession();
        OperationContext opCtx = natSes.createOperationContext(null, false, true, false, IncludeRelationships.NONE, null, false, null, false, 1);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Retrieval object by VIID = " + viid.getViidAsString());
        }
        String objId = OwObjectIDCodeUtil.decode(viid.getObjectId());
        if (OwVIIdType.DOCUMENT.equals(viid.getType()))
        {

            ObjectData data = natSes.getBinding().getVersioningService().getObjectOfLatestVersion(viid.getResourceId(), objId, objId, null, null, Boolean.TRUE, IncludeRelationships.NONE, "", Boolean.FALSE, Boolean.FALSE, null);
            nativeObject = natSes.getObjectFactory().convertObject(data, opCtx);
        }
        else
        {
            nativeObject = natSes.getObject(objId, opCtx);
        }

        return from(nativeObject.getTransientObject(), null);
    }

    /**
     * Create a string representation for sorting, based on provided definition.
     * @param sortingOrder_p OwSort (can be null)
     * @param classContext_p OwCMISNativeObjectClass array of classes to check for property definition
     * @return String with CMIS sort definition, or null if sortingOrder could not be transformed into CMIS representation
     * @since 4.1.1.1
     */
    protected String getSortString(OwSort sortingOrder_p, OwCMISNativeObjectClass<?, ?>... classContext_p)
    {
        boolean supportOrderBy = true;//TODO CMIS 1.1 has specific OrderBy check 
        if (supportOrderBy && sortingOrder_p != null && !sortingOrder_p.getCriteriaCollection().isEmpty())
        {
            StringBuilder sortStatements = new StringBuilder();
            Iterator<OwSortCriteria> itSortCrit = sortingOrder_p.getPrioritizedIterator();
            while (itSortCrit.hasNext())
            {
                OwSortCriteria crit = itSortCrit.next();
                OwCMISPropertyClass<?> prop = findPropertyDefinition(crit.getPropertyName(), classContext_p);
                if (prop != null)
                {
                    if (prop.isOrderable())
                    {
                        StringBuilder sortOrder = new StringBuilder(prop.getQueryName());
                        sortOrder.append(" ");
                        sortOrder.append(crit.getAscFlag() ? "ASC" : "DESC");
                        if (sortStatements.length() > 0)
                        {
                            sortStatements.append(",");
                        }

                        sortStatements.append(sortOrder);
                    }
                    else
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("OwCMISRepositorySession.getSortString(OwSort): The property " + crit.getPropertyName() + " cannot be used for sorting.");
                        }
                    }

                }
                else
                {
                    LOG.warn("OwCMISRepositorySession.getSortString(OwSort): The property definition = " + crit.getPropertyName() + " cannot be found.");
                }
            }

            if (LOG.isInfoEnabled())
            {
                LOG.info("OwCMISRepositorySession.getSortString: Sorting will be: " + sortStatements.toString());
            }
            return sortStatements.length() > 0 ? sortStatements.toString() : null;
        }
        else
        {
            return null;
        }
    }

    /**
     * Helper to identify OwPropertyClass from String id. 
     * @param propertyName_p String property definition id
     * @param classContext_p OwCMISNativeObjectClass
     * @return OwCMISPropertyClass if found, else null is returned
     * @since 4.1.1.1
     */
    protected OwCMISPropertyClass<?> findPropertyDefinition(String propertyName_p, OwCMISNativeObjectClass<?, ?>... classContext_p)
    {
        OwCMISQualifiedName qName = new OwCMISQualifiedName(propertyName_p);
        String objectClassName = qName.getNamespace();
        OwCMISPropertyClass<?> propertyClass = null;

        if (objectClassName != null)
        {
            try
            {
                OwCMISNativeObjectClass<?, ?> anObjectClass = getObjectClass(objectClassName);
                propertyClass = anObjectClass.getPropertyClass(propertyName_p);
            }
            catch (OwException e)
            {
                LOG.debug("Missing property class required for operation context : " + propertyName_p);
            }
        }
        else
        {
            if (OwResource.m_ObjectNamePropertyClass.getClassName().equals(propertyName_p))
            {
                for (OwCMISNativeObjectClass<?, ?> aClass : classContext_p)
                {
                    String name = null;
                    try
                    {
                        name = aClass.getNamePropertyName();
                        propertyClass = aClass.findPropertyClass(name);
                        if (propertyClass != null)
                        {
                            break;
                        }
                    }
                    catch (OwException e)
                    {
                        //ignore
                    }
                }
            }
            else
            {
                for (OwCMISNativeObjectClass<?, ?> aClass : classContext_p)
                {
                    propertyClass = aClass.findPropertyClass(propertyName_p);
                    if (propertyClass != null)
                    {
                        break;
                    }
                }
            }
        }
        return propertyClass;
    }
}
