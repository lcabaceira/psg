package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.collection.WorkflowDefinitionSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.MergeMode;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.EngineObject;
import com.filenet.api.core.EntireNetwork;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.WorkflowDefinition;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyEngineObject;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.User;
import com.filenet.api.util.UserContext;
import com.wewebu.ow.csqlc.OwCSQLCProcessor;
import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.csqlc.ast.OwExternal;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ao.OwAOManager;
import com.wewebu.ow.server.ao.OwAOManagerRegistry;
import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ao.OwAOSupport;
import com.wewebu.ow.server.ao.OwAOType;
import com.wewebu.ow.server.ao.OwAttributeBagsManager;
import com.wewebu.ow.server.ao.OwDBAttributeBagsSupport;
import com.wewebu.ow.server.ao.OwDefaultAOManager;
import com.wewebu.ow.server.ao.OwDefaultRegistry;
import com.wewebu.ow.server.ao.OwRepositoryAOSupport;
import com.wewebu.ow.server.ao.OwSearchTemplateFactory;
import com.wewebu.ow.server.ao.OwSearchTemplatesManager;
import com.wewebu.ow.server.ao.OwVirtualFolderFactory;
import com.wewebu.ow.server.ao.OwVirtualFoldersManager;
import com.wewebu.ow.server.ao.OwXMLAOManager;
import com.wewebu.ow.server.app.OwUserOperationEvent;
import com.wewebu.ow.server.app.OwUserOperationEvent.OwUserOperationType;
import com.wewebu.ow.server.app.OwUserOperationListener;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwAttributeBagsSupport;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwManagedSemiVirtualRecordConfiguration;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwTransientBagsSupport;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.ecm.OwVirtualFolderObjectFactory;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;
import com.wewebu.ow.server.ecmimpl.OwBackwardsCompatibilityAOProvider;
import com.wewebu.ow.server.ecmimpl.OwBackwardsCompatibilityAOProvider.OwLegacyAONetwork;
import com.wewebu.ow.server.ecmimpl.fncm5.aspects.Historized;
import com.wewebu.ow.server.ecmimpl.fncm5.aspects.ObjectAccess;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5Repository;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.fncm5.exceptions.OwFNCM5EngineException;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5Cache;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5SimpleCache;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5DefaultObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Domain;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5VirtualFolderObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Constructor;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DomainClass;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5MutableAccessor;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5SkeletonFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EngineBinding;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice.OwFNCM5EnumCacheHandler;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice.OwFNCM5SimpleEnumCacheHandler;
import com.wewebu.ow.server.ecmimpl.fncm5.search.OwFNCM5CSQLCProcessor;
import com.wewebu.ow.server.ecmimpl.fncm5.search.OwFNCM5SearchTemplate;
import com.wewebu.ow.server.ecmimpl.fncm5.ui.OwFNCM5LoginUISubModul;
import com.wewebu.ow.server.ecmimpl.fncm5.ui.OwFNCM5UIAccessRightsModul;
import com.wewebu.ow.server.ecmimpl.fncm5.ui.OwFNCM5UIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.fncm5.viewer.info.OwFNCM5AnnotationInfoProvider;
import com.wewebu.ow.server.ecmimpl.fncm5.viewer.info.OwFNCM5InfoProvider;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.viewer.OwAnnotationInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwInfoProvider;
import com.wewebu.ow.server.util.OwNetworkConfiguration;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Network for P8 5.x systems.
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwFNCM5Network implements OwNetwork<OwFNCM5Object<?>>, OwSearchTemplateFactory, OwVirtualFolderFactory, OwSQLEntitiesResolver, OwUserOperationListener, OwLegacyAONetwork
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Network.class);

    /**
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#SEARCHTEMPLATE_BPM}
     */
    public static final int APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE_BPM = -3;

    /**Placeholder for cache since null values are not allowed
     * used by {@link #getInterface(String, Object)} to check whether
     * an instance needed to be created or not*/
    protected static final Object PLACE_HOLDER = new Object();

    private static final ThreadLocal<OwFNCM5Network> localNetwork = new ThreadLocal<OwFNCM5Network>();

    public static final String CONF_NODE_URL = "ConnectionURL";
    public static final String CONF_NODE_DEF_STORE = "DefaultObjectStore";

    public static final String DMS_PREFIX = "fnce5";

    public static final String PROPERTY_TYPE_USER = "user";

    public static final String PROPERTY_TYPE_SYSTEM = "system";

    /** prefix for DMSID to identify virtual folders  */
    public static final String VIRTUAL_FOLDER_PREFIX = "owvf";

    private OwEventManager eventManager;

    private OwNetworkConfiguration networkConfig;

    private OwFNCM5DomainResource domainResource;
    private OwFNCM5Domain domainObject;

    private OwFNCM5ObjectStoreResource defaultResource;

    private OwFNCM5Credentials credentials;

    private OwNetworkContext context;

    private OwFNCM5DMSIDDecoder decoder;

    protected OwAttributeBagsSupport bagsSupport;

    private OwAOManagerRegistry aoManagerRegistry;

    private OwFNCM5Cache networkCache;

    private OwRoleManager roleManager;

    private OwManagedSemiVirtualRecordConfiguration managedVirtualConfiguration;

    private Map<String, String> preferredPropertyTypeMap;
    private OwFNBPM5Repository m_bpm_repository;

    private Set<String> m_RecordClassNameSet;

    private List<OwWildCardDefinition> likeWildCards;

    public static OwFNCM5Network localNetwork()
    {
        return localNetwork.get();
    }

    public boolean canBatch()
    {
        return false;
    }

    public boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int iContext_p) throws OwException
    {
        return true;
    }

    public boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canDo(OwObject obj_p, int iFunctionCode_p, int iContext_p) throws OwException
    {
        switch (iFunctionCode_p)
        {
            case OwNetwork.CAN_DO_FUNCTIONCODE_CREATE_ANNOTATION:
            case OwNetwork.CAN_DO_FUNCTIONCODE_DELETE_ANNOTATION:
            case OwNetwork.CAN_DO_FUNCTIONCODE_EDIT_ANNOTATION:
                return true;
            default:
                return false;
        }
    }

    public boolean canEditAccessRights(OwObject object_p) throws OwException
    {
        if (object_p instanceof OwFNCM5Object)
        {
            OwFNCM5Object obj = ((OwFNCM5Object) object_p);
            try
            {
                return obj.canGetPermissions() && obj.canSetPermissions();
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Could not retrieve permissions capabilities", e);
                throw new OwServerException("Permission capabilities request problem", e);
            }
        }
        return false;
    }

    public boolean canRefreshStaticClassdescriptions() throws OwException
    {
        return false;
    }

    public boolean canUserSelect() throws OwException
    {
        return true;
    }

    public void closeBatch(OwBatch batch_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    private void refresh(IndependentObject object_p, String[] properties_p)
    {
        Properties properties = object_p.getProperties();
        List<String> missingProperties = new LinkedList<String>();
        for (int i = 0; i < properties_p.length; i++)
        {
            if (!properties.isPropertyPresent(properties_p[i]))
            {
                missingProperties.add(properties_p[i]);
            }
        }

        if (!missingProperties.isEmpty())
        {
            String[] missingPropertyNames = missingProperties.toArray(new String[missingProperties.size()]);
            object_p.refresh(missingPropertyNames);
        }
    }

    public <I extends IndependentObject> OwFNCM5Object<I> fromNativeObject(I nativeObject_p) throws OwException
    {
        ObjectReference reference = nativeObject_p.getObjectReference();
        String resourceID = reference.getObjectStoreIdentity();
        return fromNativeObject(nativeObject_p, resourceID);

    }

    public <N extends EngineObject> OwFNCM5Object<N> fromNativeObject(N nativeObject_p, String resourceID_p) throws OwException
    {
        OwFNCM5Resource resource = getResource(resourceID_p);
        return fromNativeObject(nativeObject_p, resource);
    }

    public <N extends EngineObject> OwFNCM5Object<N> fromNativeObject(N nativeObject_p, OwFNCM5Resource resource_p) throws OwException
    {
        if (nativeObject_p instanceof IndependentObject)
        {
            final String[] requiredPropertyNames = new String[] { PropertyNames.ID, PropertyNames.CLASS_DESCRIPTION };
            refresh((IndependentObject) nativeObject_p, requiredPropertyNames);
        }

        ClassDescription classDescription = nativeObject_p.get_ClassDescription();

        OwFNCM5Class<N, ?> objectClass = (OwFNCM5Class<N, ?>) getObjectClass(classDescription.get_SymbolicName(), resource_p);
        return objectClass.from(nativeObject_p, OwFNCM5DefaultObjectFactory.INSTANCE);
    }

    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        return createNewObject(fPromote_p, mode_p, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, false);
    }

    @Historized(id = OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT, type = OwEventManager.HISTORY_EVENT_TYPE_OBJECT)
    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        OwFNCM5Class<?, ?> objectClass = getObjectClass(strObjectClassName_p, resource_p);
        if (objectClass.canCreateNewObject())
        {
            OwObject parent = prepareParentObject(parent_p, resource_p, objectClass, properties_p);
            OwFNCM5Constructor<?, ?> constructor = objectClass.getConstructor();
            OwFNCM5Object<?> newObject = constructor.newObject(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p, OwFNCM5DefaultObjectFactory.INSTANCE);
            return newObject.getDMSID();
        }
        else
        {
            throw new OwInvalidOperationException("Creating new objects for object class " + objectClass.getClassName() + " is not supported.");
        }
    }

    public String createNewObject(OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p) throws OwException
    {
        return createNewObject(false, null, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, false);
    }

    /**(overridable)
     * Prepare the parent object before creation of object is processed.<br />
     * Specially for virtual structures a specific path definition of the search 
     * will be used as parent object. 
     * @param parent_p OwObject parent where it should be filed (can be null)
     * @param resource_p OwResource
     * @param objectClass_p OwObjectClass representing the type of the new object
     * @param properties_p OwPropertiesCollection properties assigned to new created object
     * @return OwObject representing the parent to be used for file operation (null if unfiled).
     * @throws OwException
     */
    protected OwObject prepareParentObject(OwObject parent_p, OwResource resource_p, OwObjectClass objectClass_p, OwPropertyCollection properties_p) throws OwException
    {
        OwObject parent = parent_p;
        if (parent != null)
        {
            if (parent.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
            {
                // not a OwFNCMObject. Perhaps a OwVirtualFolderObject ?
                OwVirtualFolderObject vfo = (OwVirtualFolderObject) parent_p;
                OwSearchNode specialNode = null;
                // set the required properties
                try
                {
                    vfo.setFiledObjectProperties(objectClass_p, properties_p);
                    //try to retrieve the search path ...
                    OwSearchTemplate template = vfo.getSearchTemplate();
                    OwSearchNode theSearchNode = template.getSearch(false);

                    specialNode = theSearchNode.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
                }
                catch (OwException owe)
                {
                    LOG.warn("Could not prepare parent for virtual structure filling", owe);
                    throw owe;
                }
                catch (Exception e)
                {
                    LOG.warn("Failed to prepare parent object for creation process", e);
                    throw new OwServerException(getContext().localize("OwFNCM5Network.prepareParentFolder.ex", "Filling into virutal structure failed during prepare"), e);
                }

                if (specialNode != null)
                {
                    List specialChildren = specialNode.getChilds();
                    OwSearchNode virtualFolderPathNode = null;
                    for (Iterator k = specialChildren.iterator(); k.hasNext();)
                    {
                        OwSearchNode specialChild = (OwSearchNode) k.next();
                        OwSearchCriteria specialCriteria = specialChild.getCriteria();
                        String criteriaUName = specialCriteria.getUniqueName();
                        String criteriaClassName = specialCriteria.getClassName();
                        if (OwSemiVirtualFolderAdapter.VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY.equals(criteriaUName) && OwSearchPathField.CLASS_NAME.equals(criteriaClassName))
                        {
                            virtualFolderPathNode = specialChild;
                            break;
                        }
                    }
                    if (virtualFolderPathNode != null)
                    {
                        OwSearchCriteria virtualFolderPathCriteria = virtualFolderPathNode.getCriteria();
                        OwSearchPath virtualFolderPath = (OwSearchPath) virtualFolderPathCriteria.getValue();
                        OwSearchObjectStore virtualFolderSearchStore = virtualFolderPath.getObjectStore();

                        String osIdentification = virtualFolderSearchStore.getId();
                        if (osIdentification == null)
                        {
                            osIdentification = virtualFolderSearchStore.getName();
                        }
                        OwFNCM5ObjectStoreResource virtualFolderStore = getResource(osIdentification);
                        parent = virtualFolderStore.getObjectStore().getObjectFromId(virtualFolderPath.getPathName());
                    }
                }
            }
            else
            {
                if (parent instanceof OwFNCM5ObjectStore)
                {
                    OwFNCM5ObjectStore store = (OwFNCM5ObjectStore) parent_p;
                    Folder f = store.getNativeObject().get_RootFolder();
                    OwFNCM5ContentObjectModel objectModel = store.getResource().getObjectModel();
                    OwFNCM5NativeObjHelper.ensure(f, new String[] { PropertyNames.NAME, PropertyNames.CLASS_DESCRIPTION });

                    OwFNCM5Class clazz = objectModel.objectClassForName(f.get_ClassDescription().get_SymbolicName());
                    parent = clazz.from(f, OwFNCM5DefaultObjectFactory.INSTANCE);
                }
            }
        }
        return parent;
    }

    public String createObjectCopy(OwObject obj_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwObject parent_p, int[] childTypes_p) throws OwException
    {
        if (obj_p == null)
        {
            throw new OwInvalidOperationException("OwFNCM5Network.createObjectCopy: The passed OwObject is null! Cannot create a copy of a null object...");
        }
        try
        {
            OwPropertyCollection newPropertyCollection = properties_p;
            if (properties_p == null)
            {
                //different approach:
                OwResource resource = obj_p.getResource();

                OwObjectSkeleton skeletonObject = createObjectSkeleton(obj_p.getObjectClass(), resource);
                skeletonObject.refreshProperties();
                OwPropertyCollection allProps = skeletonObject.getProperties(null);
                OwPropertyCollection clonnedProps = obj_p.getClonedProperties(null);

                newPropertyCollection = new OwStandardPropertyCollection();

                Iterator it = allProps.values().iterator();
                while (it.hasNext())
                {
                    OwProperty prop = (OwProperty) it.next();
                    if (prop.isReadOnly(OwPropertyClass.CONTEXT_ON_CREATE) || prop.getPropertyClass().isSystemProperty() || prop.isHidden(OwPropertyClass.CONTEXT_ON_CREATE))
                    {
                        continue;
                    }
                    OwProperty original = (OwProperty) clonnedProps.get(prop.getPropertyClass().getClassName());
                    prop.setValue(original.getValue());
                    newPropertyCollection.put(prop.getPropertyClass().getClassName(), prop);
                }
            }
            boolean isMajorVersion = obj_p.getVersion().isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
            return createNewObject(isMajorVersion, null, obj_p.getResource(), obj_p.getClassName(), newPropertyCollection, permissions_p, obj_p.getContentCollection(), parent_p, obj_p.getMIMEType(), obj_p.getMIMEParameter());
        }
        catch (OwFNCM5EngineException e)
        {
            if (e.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE)
            {
                LOG.debug("A uniqueness requirement has been violated. Possible causes are: the document name already exists, a multivalue property has duplicated values...", e);
                throw new OwInvalidOperationException(getContext().localize("OwFNCM5Network.UniquenessException",
                        "A uniqueness requirement has been violated. Possible causes: The document name already exists, a multivalue property has duplicated values..."), e);
            }
            else if (e.getExceptionCode() == ExceptionCode.SECURITY_INVALID_CREDENTIALS)
            {
                LOG.debug("User does not have the necessary access authorization to create this object.", e);
                throw new OwAccessDeniedException(getContext().localize("OwFNCM5Network.createaccessdenied", "You do not have the necessary authorization to create this object."), e);
            }
            else
            {
                throw e;
            }
        }
        catch (OwException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            String msg = "Could not create object copy.";
            LOG.error(msg, e);
            throw new OwServerException(msg, e);
        }
    }

    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectClass_p, OwResource resource_p) throws OwException
    {
        if (objectClass_p instanceof OwFNCM5SkeletonFactory)
        {
            try
            {
                OwFNCM5SkeletonFactory factory = (OwFNCM5SkeletonFactory) objectClass_p;
                return factory.createSkeleton(this, objectClass_p);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Could not create object skeleton.", e);
                throw new OwInvalidOperationException("Could not create object skeleton.", e);
            }
        }
        else
        {
            LOG.error("OwFNCM5Network.createObjectSkeleton: No available skeleton factory found for requested class.");
            throw new OwServerException(getContext().localize("OwFNCM5Network.createObjectSkeleton.noFactory", "No available skeleton factory found for requested class."));
        }
    }

    private Map<String, List<ObjectStore>> createFNCMOptimizedStatements(List<OwQueryStatement> statements_p) throws OwException
    {
        Map<String, List<ObjectStore>> optimizedStatements = new HashMap<String, List<ObjectStore>>();

        for (OwQueryStatement statement : statements_p)
        {
            String targetId = statement.getTargetRepositoryID();
            OwFNCM5ObjectStoreResource resource = getResource(targetId);

            OwFNCM5ObjectStoreResource storeResource = resource;
            OwFNCM5ObjectStore storeObject = storeResource.getObjectStore();
            ObjectStore objectStore = storeObject.getNativeObject();

            StringBuilder sqlBuilder = statement.createSQLString();
            String sql = sqlBuilder.toString();

            List<ObjectStore> sqlStores = optimizedStatements.get(sql);
            if (sqlStores == null)
            {
                sqlStores = new LinkedList<ObjectStore>();
            }
            sqlStores.add(objectStore);

            optimizedStatements.put(sql, sqlStores);
        }

        return optimizedStatements;
    }

    @ObjectAccess(mask = OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW, name = " view this object ")
    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws OwException
    {
        OwCSQLCProcessor processor = createCSQLProcessor();
        processor.setMaxRows(Integer.valueOf(iMaxSize_p));

        OwExternal<List<OwQueryStatement>> statements = processor.createSQLStatements(searchCriteria_p, propertyNames_p, sortCriteria_p, iVersionSelection_p);
        List<OwQueryStatement> internalStatements = statements.getInternal();

        Map<String, List<ObjectStore>> optimizedStatements = createFNCMOptimizedStatements(internalStatements);

        OwObjectCollection result = new OwStandardObjectCollection();

        Set<Entry<String, List<ObjectStore>>> optimizedEntries = optimizedStatements.entrySet();
        for (Entry<String, List<ObjectStore>> entry : optimizedEntries)
        {
            String queryString = entry.getKey();
            List<ObjectStore> objectStoreList = entry.getValue();
            ObjectStore[] objectStores = objectStoreList.toArray(new ObjectStore[objectStoreList.size()]);
            //TODO: merge mode handling
            SearchScope searchScope = new SearchScope(objectStores, MergeMode.UNION);
            SearchSQL sql = new SearchSQL(queryString);

            if (LOG.isDebugEnabled())
            {
                StringBuilder osNames = new StringBuilder();
                if (objectStores.length > 0)
                {
                    osNames.append(objectStores[0].get_Id());
                    osNames.append(" : ");
                    osNames.append(objectStores[0].get_Name());
                }
                for (int i = 1; i < objectStores.length; i++)
                {
                    osNames.append(" , ");
                    osNames.append(objectStores[i].get_Id());
                    osNames.append(" : ");
                    osNames.append(objectStores[i].get_Name());
                }
                LOG.debug("Running query " + queryString + " on " + objectStores.length + " objectstores (" + osNames + ")");
            }

            StopWatch fetchRowsStopWatch = new Log4JStopWatch("fetchRows");
            RepositoryRowSet rowSet = searchScope.fetchRows(sql, iMaxSize_p, null, Boolean.FALSE);
            fetchRowsStopWatch.stop();

            if (LOG.isDebugEnabled())
            {
                LOG.debug("Query produced " + (rowSet.isEmpty() ? "empty" : "non-empty") + " row set.");
            }

            StopWatch processRowsStopWatch = new Log4JStopWatch("processRows");
            Iterator it = rowSet.iterator();

            while (result.size() < iMaxSize_p && it.hasNext())
            {
                RepositoryRow row = (RepositoryRow) it.next();
                Properties properties = row.getProperties();
                PropertyEngineObject engineObject = (PropertyEngineObject) properties.get("This");
                IndependentObject independentObject = engineObject.fetchIndependentObject(null);

                //                ObjectReference objectReference = engineObject.getObjectReference();
                //                String objectStoreId = objectReference.getObjectStoreIdentity();
                //                ClassDescription classDescription = independentObject.get_ClassDescription();
                //                OwFNCM5Resource resource = getResource(objectStoreId);
                //                OwFNCM5ContentObjectModel om = resource.getObjectModel();
                //                OwFNCM5ObjectClass objectClass = om.getObjectClass(classDescription.get_SymbolicName());
                //                OwFNCM5Object<IndependentObject> object = objectClass.fromNativeObject(independentObject);

                OwFNCM5Object<IndependentObject> object = fromNativeObject(independentObject);
                result.add(object);
            }
            processRowsStopWatch.stop();
        }
        return result;
    }

    protected OwCSQLCProcessor createCSQLProcessor()
    {
        OwNetworkContext currentContext = getContext();
        TimeZone clientTimeZone = currentContext.getClientTimeZone();

        OwCSQLCProcessor processor = new OwFNCM5CSQLCProcessor(this, clientTimeZone);
        return processor;
    }

    public OwNetworkContext getContext()
    {
        return this.context;
    }

    public OwFNCM5Credentials getCredentials()
    {
        return this.credentials;
    }

    public String getDMSPrefix()
    {
        return OwFNCM5Network.DMS_PREFIX;
    }

    public OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws OwException
    {
        try
        {
            OwFNCM5UIAccessRightsModul mod = new OwFNCM5UIAccessRightsModul(this, object_p);
            mod.init(this);
            return mod;
        }
        catch (OwException ex)
        {
            throw ex;
        }
        catch (Exception e)
        {
            LOG.error("Could not initialize OwUIAccessRightsModul", e);
            throw new OwServerException("Initialization of OwUIAccessRightsModul failed", e);
        }
    }

    public OwEventManager getEventManager()
    {
        return this.eventManager;
    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws OwException
    {
        OwFNCM5Resource resource = getResource(strResourceName_p);
        if (resource == null)
        {
            LOG.error("OwFNCM5Network.getFieldDefinition(): Could not find a requested field definition! Resource could not be found, or incorrect resource ID/name (" + strResourceName_p + ")");
            throw new OwObjectNotFoundException(getContext().localize("OwFNCM5Network.field.definition.not.found.error", "Could not find a requested field definition!"));
        }

        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();
        return objectModel.propertyClassForName(strFieldDefinitionName_p);
    }

    public Locale getLocale()
    {
        OwNetworkContext currentContext = getContext();
        return currentContext.getLocale();
    }

    public OwUILoginModul getLoginSubModul() throws OwException
    {
        try
        {
            return new OwFNCM5LoginUISubModul(this);
        }
        catch (Exception e)
        {
            throw new OwServerException("Cannot instantiate Login module!", e);
        }
    }

    public OwFNCM5Class<?, ?> getObjectClass(String strClassName_p, OwResource resource_p) throws OwException
    {
        OwFNCM5ObjectStoreResource resource = this.defaultResource;
        String resourceId = "<null>";
        if (resource_p != null)
        {
            try
            {
                resourceId = resource_p.getID();
                resource = getResource(resourceId);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("OwFNCM5Network.getObjectClass: error ", e);
            }
        }

        if (resource != null)
        {
            OwFNCM5ContentObjectModel om = resource.getObjectModel();
            return om.objectClassForName(strClassName_p);
        }
        else
        {
            throw new OwObjectNotFoundException("OwFNCM5Network.getObjectClass: unknown resource with id " + resourceId);
        }
    }

    public Map<String, String> getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws OwException
    {
        OwFNCM5Resource cmisResource = (OwFNCM5Resource) resource_p;

        if (null == cmisResource)
        {
            cmisResource = getResource(null);
        }
        OwFNCM5ContentObjectModel objectModel = cmisResource.getObjectModel();

        return objectModel.objectClassNamesWith(iTypes_p, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p);
    }

    @ObjectAccess(mask = OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW, name = " view this object ")
    public OwFNCM5Object<?> getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws OwException
    {
        OwFNCM5DMSID id = getDmsIdDeconder().getDmsidObject(strDMSID_p);
        OwFNCM5ObjectStoreResource resource = getResource(id.getResourceID());
        if (resource == null)
        {
            throw new OwInvalidOperationException("The provided DMSID does not contains an accessible resource, resource ID = " + id.getResourceID());
        }
        OwFNCM5ObjectStore store = resource.getObjectStore();
        return store.getObjectFromId(id.getObjectID());
    }

    @ObjectAccess(mask = OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW, name = " view this object ")
    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws OwException
    {
        if (strPath_p != null && strPath_p.length() > 1)
        {
            int idx = strPath_p.indexOf('/', 1);

            if (idx == -1)
            {
                idx = strPath_p.length();
            }

            String resourceName = strPath_p.substring(1, idx);

            OwFNCM5ObjectStoreResource resource = getResource(resourceName);

            if (resource == null)
            {
                resource = getResource(null);
            }

            OwFNCM5ObjectStore objectStore = resource.getObjectStore();
            String resourcePath = strPath_p.substring(idx);
            return objectStore.getObjectFromPath(resourcePath);
        }
        else if ("/".equals(strPath_p))
        {
            return this.domainObject;
        }
        throw new OwInvalidOperationException("Unsupported or Invalid path definition! Provided path = " + strPath_p);
    }

    @Deprecated
    /**
     * 
     * @param nameOrId_p
     * @return OwFNCM5ObjectStoreResource
     * @throws OwException
     * @deprecated since 3.2.0.3 use interface method {@link #getResource(String)}  
     */
    protected OwFNCM5ObjectStoreResource getInternalResource(String nameOrId_p) throws OwException
    {
        if (nameOrId_p == null)
        {
            return this.defaultResource;
        }
        else
        {
            OwFNCM5ObjectStore os = this.domainObject.getObjectStore(nameOrId_p);
            if (os == null)
            {
                throw new OwObjectNotFoundException(getContext().localize1("OwFNCM5Network.getResource.notfound", "Object Store \"%1\" not found", nameOrId_p));
            }
            return os.getResource();
        }
    }

    public OwFNCM5ObjectStoreResource getResource(String nameOrId_p) throws OwException
    {
        if (nameOrId_p == null)
        {
            return this.defaultResource;
        }
        else
        {
            OwFNCM5ObjectStore os = this.domainObject.getObjectStore(nameOrId_p);
            if (os == null)
            {
                throw new OwObjectNotFoundException(getContext().localize1("OwFNCM5Network.getResource.notfound", "Object Store \"%1\" not found", nameOrId_p));
            }
            return os.getResource();
        }
    }

    public Iterator getResourceIDs() throws OwException
    {
        return this.domainObject.getObjectStoreIds();
    }

    public String getRoleDisplayName(String strRoleName_p) throws OwException
    {
        return strRoleName_p;
    }

    public OwUserInfo getUserFromID(String strID_p) throws OwException
    {
        try
        {
            OwFNCM5Credentials myCredentials = getCredentials();
            Connection connection = myCredentials.getConnection();
            User user = com.filenet.api.core.Factory.User.fetchInstance(connection, strID_p, null);

            OwUserInfo info = myCredentials.createUserInfo(user);
            info.getUserName();
            return info;
        }
        catch (Exception e)
        {
            String msg = "Exception getting the OwUserInfo (getUserFromID), User Id = " + strID_p;
            LOG.debug(msg, e);
            throw new OwObjectNotFoundException(msg, e);
        }
    }

    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws OwException
    {
        OwFNCM5Credentials credentials = getCredentials();
        EntireNetwork network = com.filenet.api.core.Factory.EntireNetwork.getInstance(credentials.getConnection());
        network.fetchProperties(new String[] { PropertyNames.ALL_REALMS, PropertyNames.MY_REALM });
        try
        {
            OwMandator mandator = context.getMandator();
            OwFNCM5UIUserSelectModul module = new OwFNCM5UIUserSelectModul(network.get_AllRealms(), types_p, credentials);
            module.init(this);
            return module;
        }
        catch (Exception e)
        {
            LOG.error("Cannot initialize the user selection module", e);
            throw new OwServerException("Problem durring initialization of user selection module", e);
        }
    }

    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws OwException
    {
        switch (iOp_p)
        {
            case OwSearchOperator.CRIT_OP_LIKE:
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
                if (likeWildCards == null)
                {
                    List<OwWildCardDefinition> lst = new LinkedList<OwWildCardDefinition>();
                    lst.add(new OwStandardWildCardDefinition("%", getContext().getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1(
                            "ecmimpl.OwFNCM5Network.WILD_CARD_TYPE_MULTI_CHAR", "(%1) replaces any characters")));
                    lst.add(new OwStandardWildCardDefinition("_", getContext().getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1(
                            "ecmimpl.OwFNCM5Network.WILD_CARD_TYPE_SINGLE_CHAR", "(%1) replaces any character")));

                    likeWildCards = Collections.unmodifiableList(lst);
                }
                return this.likeWildCards;
            default:
                return null;
        }

    }

    public Object getInterface(String strInterfaceName_p, Object oObject_p) throws OwException
    {
        try
        {
            Class reqClass = Class.forName(strInterfaceName_p);
            Object cached = networkCache.getCachedObject(reqClass);
            if (cached == null || cached == PLACE_HOLDER)
            {
                cached = createInstanceOf(reqClass);
                if (cached == null)
                {
                    LOG.error("OwFNCM5Network.getInterface: Requested class not supported by implementation, class = " + strInterfaceName_p);
                    String msg = getContext().localize1("OwFNCM5Network.getInterface.unsupported", "Requested class not supported by implementation, class =  %1", strInterfaceName_p);
                    throw new OwNotSupportedException(msg);
                }
                networkCache.add(reqClass, cached);
            }
            return cached;
        }
        catch (ClassNotFoundException e)
        {
            LOG.error("Could not find requested Class: " + strInterfaceName_p, e);
            throw new OwInvalidOperationException(getContext().localize1("OwFNCM5Network.getInterface.unknownClass", "The requested class %1 could not be found.", strInterfaceName_p), e);
        }
    }

    /**
     * Method is called by {@link #getInterface(String, Object)}
     * to create a corresponding instance.
     * @param reqClass Class to create an instance of
     * @return Object or null if class is unknown
     * @throws OwConfigurationException 
     */
    protected Object createInstanceOf(Class<?> reqClass) throws OwConfigurationException
    {
        if (OwInfoProvider.class.equals(reqClass))
        {
            return new OwFNCM5InfoProvider(this);
        }
        if (OwAnnotationInfoProvider.class.equals(reqClass))
        {
            return new OwFNCM5AnnotationInfoProvider(this);
        }
        if (OwWorkitemRepository.class.equals(reqClass))
        {
            return getBpmRepository();
        }
        return null;
    }

    public boolean hasInterface(String strInterfaceName_p)
    {
        try
        {
            Class reqClass = Class.forName(strInterfaceName_p);
            return networkCache.contains(reqClass);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error("Could not find requested Class: " + strInterfaceName_p, e);
        }
        return false;
    }

    public void init(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws OwException
    {
        networkCache = createCache();
        networkCache.addCacheHandler(OwFNCM5EnumCacheHandler.class, createEnumCacheHandler());
        networkCache.add(OwInfoProvider.class, PLACE_HOLDER);
        networkCache.add(OwAnnotationInfoProvider.class, PLACE_HOLDER);

        this.networkConfig = new OwNetworkConfiguration(networkSettings_p);
        this.context = context_p;
        this.bagsSupport = new OwTransientBagsSupport();
        context_p.addUserOperationListener(this);

        try
        {
            if (m_bpm_repository == null && getConfiguration().getSubNode("BPM") != null)
            {
                networkCache.add(OwWorkitemRepository.class, PLACE_HOLDER);
            }
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not check for BPM configuration.", e);
        }
    }

    /**(overridable)
     * Creates credentials for given name and password with respect to 
     * the &lt;Authentication&gt; bootstrap configuration.  
     * @param strUser_p
     * @param strPassword_p
     * @return an {@link OwFNCM5Credentials} 
     * @throws OwException
     */
    protected OwFNCM5Credentials createCredentials(String strUser_p, String strPassword_p, Connection connection_p) throws OwException
    {

        OwXMLUtil configuration = getConfiguration();

        OwXMLUtil authenticationConfiguration = null;

        try
        {
            authenticationConfiguration = configuration.getSubUtil(OwFNCM5Credentials.CONF_NODE_AUTHENTICATION);
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Error reading authentication configuration.", e);
        }

        if (authenticationConfiguration == null)
        {
            throw new OwConfigurationException("Missing authentication configuration.");
        }

        String authenticationMode = authenticationConfiguration.getSafeStringAttributeValue(OwFNCM5Credentials.CONF_ATTRIBUTE_MODE, "");

        OwNetworkContext context = getContext();
        OwMandator mandator = context.getMandator();
        OwFNCM5DefaultUserInfoFactory userInfoFactory = new OwFNCM5DefaultUserInfoFactory(mandator, false);

        if (OwFNCM5LDAPCredentials.AUTHENTICATION_MODE_LDAP.equals(authenticationMode))
        {
            if (strPassword_p != null)
            {
                return createLDAPCredentials(connection_p, strUser_p, strPassword_p, configuration, userInfoFactory);
            }
            else
            {
                throw new OwConfigurationException("Invalid <Athentication> @mode: LDAP not working in SSO environment, please check bootstrap config.");
            }
        }
        else if (OwFNCM5EngineCredentials.AUTHENTICATION_MODE_NONE.equals(authenticationMode))
        {

            return createEngineCredentials(connection_p, strUser_p, strPassword_p, configuration, userInfoFactory);
        }
        else
        {
            String msg = "Invalid authentication configuration! Invalid <Athentication> @mode : " + authenticationMode;
            LOG.error("OwFNCM5Network.createCredentials():" + msg);
            throw new OwServerException(getContext().localize("fncm5.OwFNCM5Network.invalid.authentication.configuration.error", "Invalid authentication configuration!"));
        }

    }

    public void loginDefault(String strUser_p, String strPassword_p) throws OwException
    {
        Connection connection;
        try
        {
            connection = Factory.Connection.getConnection(getConfiguration().getSafeTextValue(CONF_NODE_URL, ""));
        }
        catch (Exception ex)
        {
            OwXMLUtil errConfig = getConfiguration();
            String configURL = "N/A";
            if (errConfig != null)
            {
                configURL = errConfig.getSafeTextValue(CONF_NODE_URL);
            }
            logout();
            throw new OwConfigurationException("Cannot create connection to server, URL = " + configURL, ex);
        }

        this.credentials = createCredentials(strUser_p, strPassword_p, connection);

        postLogin();
    }

    public void loginJAAS() throws OwException
    {
        loginDefault(null, null);
    }

    /**
     * Post processing login handling.
     * Loading of domain and object store information, which will be cached.
     * @throws OwException
     * @since 3.2.0.2
     */
    protected void postLogin() throws OwException
    {
        try
        {
            Domain domain = Factory.Domain.fetchInstance(getCredentials().getConnection(), null, null);
            OwFNCM5MutableAccessor<OwFNCM5DomainResource> domainResourceAccessor = new OwFNCM5MutableAccessor<OwFNCM5DomainResource>();
            OwFNCM5DomainClass domainClass = new OwFNCM5DomainClass(domain, domainResourceAccessor);
            this.domainObject = domainClass.from(domain, OwFNCM5DefaultObjectFactory.INSTANCE);
            this.domainResource = new OwFNCM5DomainResource(this.domainObject, this);
            domainResourceAccessor.set(this.domainResource);

            String defaultStore = getConfiguration().getSafeTextValue(CONF_NODE_DEF_STORE, "");
            if ("".equals(defaultStore))
            {
                throw new OwConfigurationException("Missing information about default object store please check the configuration, node " + CONF_NODE_DEF_STORE + " is empty");
            }

            this.defaultResource = getResource(defaultStore);
            if (this.defaultResource == null)
            {
                throw new OwConfigurationException("Invalid default object store [" + defaultStore + "]");
            }
            this.context.onLogin(this.credentials.getUserInfo());
        }
        catch (EngineRuntimeException p8ex)
        {
            logout();
            if (p8ex.getExceptionCode().equals(ExceptionCode.E_NOT_AUTHENTICATED))
            {
                throw new OwAuthenticationException(getContext().localize("OwFNCM5Network.login.error.auth", "Could not login cause of wrong credentials"), p8ex);
            }
            else
            {
                throw new OwServerException("Unhandled error occured during login", p8ex);
            }
        }
        catch (OwException owEx)
        {
            logout();
            throw owEx;
        }
        catch (Exception ex)
        {
            logout();
            throw new OwServerException("Unknown error Occured", ex);
        }
    }

    public void logout() throws OwException
    {
        OwFNCM5Credentials credentials = getCredentials();
        if (credentials != null)
        {
            credentials.invalidate();
        }

        this.credentials = null;
        if (domainObject != null)
        {
            domainObject.clearOSCache();
        }
        domainObject = null;
        this.defaultResource = null;
    }

    public OwBatch openBatch() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void refreshStaticClassdescriptions() throws OwException
    {
        getNetworkCache().getCacheHandler(OwFNCM5EnumCacheHandler.class).clearCache();
    }

    public void releaseResources() throws OwException
    {
        localNetwork.remove();
        if (m_bpm_repository != null)
        {
            try
            {
                m_bpm_repository.releaseResources();
            }
            catch (RuntimeException re)
            {
                throw re;
            }
            catch (Exception e)
            {
                throw new OwServerException("Could not release resources !", e);
            }
        }
    }

    public void setEventManager(OwEventManager eventManager_p)
    {
        this.eventManager = eventManager_p;
    }

    public void setRoleManager(OwRoleManager roleManager_p)
    {
        this.roleManager = roleManager_p;
    }

    public OwRoleManager getRoleManager()
    {
        return this.roleManager;
    }

    public OwXMLUtil getConfiguration()
    {
        return getNetworkConfig().getConfigNode();
    }

    public OwFNCM5LDAPCredentials createLDAPCredentials(Connection connection_p, String userName_p, String password_p, OwXMLUtil configuration_p, OwFNCM5DefaultUserInfoFactory userInfoFactory_p) throws OwAuthenticationException,
            OwConfigurationException
    {
        OwFNCM5EngineCredentials engineCredentials = createEngineCredentials(connection_p, userName_p, password_p, configuration_p, userInfoFactory_p);
        return new OwFNCM5LDAPCredentials(userName_p, password_p, configuration_p, engineCredentials);
    }

    public OwFNCM5EngineCredentials createEngineCredentials(Connection con, String username, String password, OwXMLUtil config, OwFNCM5DefaultUserInfoFactory userInfoFactory) throws OwAuthenticationException, OwConfigurationException
    {
        try
        {
            if (username != null && password != null)
            {
                return new OwFNCM5EngineCredentials(con, username, password, config, userInfoFactory);
            }
            else
            {
                return new OwFNCM5EngineCredentials(con, config, userInfoFactory);
            }
        }
        catch (EngineRuntimeException ex)
        {
            if (ExceptionCode.PASSWORD_IS_EMPTY_ERROR.equals(ex.getExceptionCode()))
            {
                throw new OwAuthenticationException(getContext().localize("OwFNCM5Network.auth.emptyPwd", "The provided password is empty or could not be read"), ex);
            }
            else
            {
                throw new OwAuthenticationException(getContext().localize("OwFNCM5Network.auth.unknownException", "An unknown problem occured during login"), ex);
            }
        }
    }

    public OwSearchTemplate createSearchTemplate(OwObject obj_p) throws OwException
    {
        try
        {
            return new OwFNCM5SearchTemplate(getContext(), obj_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not create search template", e);
        }

    }

    public OwFNCM5DMSIDDecoder getDmsIdDeconder()
    {
        if (decoder == null)
        {
            decoder = createDMSIDDecoder();
        }
        return decoder;
    }

    public OwFNCM5DMSIDDecoder createDMSIDDecoder()
    {
        return new OwFNCM5SimpleDMSIDDecoder();
    }

    private OwAOProvider getAOProvider() throws OwException
    {
        OwNetworkContext myContext = getContext();
        if (myContext instanceof OwAppContext)
        {
            return ((OwAppContext) myContext).getAOProvider();
        }
        else
        {
            return null;
        }
    }

    /**
     * @deprecated used for backwards configuration compatibility
     */
    public final OwAOProvider getSafeAOProvider() throws OwException
    {
        OwAOProvider provider = getAOProvider();
        if (provider != null && !(provider instanceof OwBackwardsCompatibilityAOProvider))
        {
            return provider;
        }
        else
        {
            return new OwAOProvider() {

                @Override
                public <T> List<T> getApplicationObjects(OwAOType<T> type, String name, boolean forceSpecificObj) throws OwException
                {
                    OwAOManager typedManager = getAOManagerForType(type.getType());
                    Object appObject = typedManager.getApplicationObjects(name, forceSpecificObj);
                    return (List<T>) appObject;
                }

                @Override
                public <T> T getApplicationObject(OwAOType<T> type, String name, List<Object> params, boolean forceSpecificObj, boolean createNonExisting) throws OwException
                {
                    OwAOManager typedManager = getAOManagerForType(type.getType());
                    Object appObject = typedManager.getApplicationObject(name, params.get(0), forceSpecificObj, createNonExisting);
                    return (T) appObject;
                }

                @Override
                public <T> T getApplicationObject(OwAOType<T> type, String name, boolean forceSpecificObj, boolean createNonExisting) throws OwException
                {
                    OwAOManager typedManager = getAOManagerForType(type.getType());
                    Object appObject = typedManager.getApplicationObject(name, forceSpecificObj, createNonExisting);
                    return (T) appObject;
                }
            };
        }
    }

    public Object getApplicationObject(int type_p, String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getSafeAOProvider().getApplicationObject(OwAOConstants.fromType(type_p), strName_p, Arrays.asList(param_p), forceUserSpecificObject_p, createIfNotExist_p);
    }

    public Object getApplicationObject(int type_p, String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getSafeAOProvider().getApplicationObject(OwAOConstants.fromType(type_p), strName_p, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public Collection<?> getApplicationObjects(int type_p, String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {
        return getSafeAOProvider().getApplicationObjects(OwAOConstants.fromType(type_p), strName_p, forceUserSpecificObject_p);
    }

    protected synchronized OwAOManager getAOManagerForType(int type_p) throws OwException
    {
        if (aoManagerRegistry == null)
        {
            aoManagerRegistry = createAOManagerRegistry();
        }

        OwAOManager typedManager = aoManagerRegistry.getManager(type_p);
        return typedManager;
    }

    protected OwAOManagerRegistry createAOManagerRegistry() throws OwException
    {
        //        OwAOSupport aoSupport = new OwFileAOSupport(this, config);
        OwXMLUtil configuration = getConfiguration();

        OwFNCM5Resource defaultAOResource = getResource(null);
        String defaultAOResourceID = defaultAOResource.getID();
        final String defaultAORepositoryPath = "/" + defaultAOResourceID;

        String aoRepositoryPath = defaultAORepositoryPath;

        final String defaultAOSubPath = "/ow_app/";

        String aoPath = aoRepositoryPath + defaultAOSubPath;

        final String defaultPreferencesFolder = "owpreferences";
        String preferencesFolder = configuration.getSafeTextValue("UserDefinedPreferencesFolder", defaultPreferencesFolder);

        OwAOSupport aoSupport = new OwRepositoryAOSupport(this, aoPath, new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, OwSearchTemplate.VERSION_SELECT_CURRENT, ClassNames.DOCUMENT);
        LOG.debug("Using OwRepositoryAOSupport with AO path " + aoPath + ", preferences folder " + preferencesFolder);

        OwAOSupport aoPreferencesSupport = new OwFNCM5AOSupport(this, aoPath + preferencesFolder, new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, OwSearchTemplate.VERSION_SELECT_CURRENT, ClassNames.DOCUMENT);
        LOG.debug("Using OwRepositoryAOSupport with AO preferences path " + aoPath + ", preferences folder " + preferencesFolder);

        OwDefaultRegistry stdRegistry = new OwDefaultRegistry();
        OwAttributeBagsSupport bagsSupport = null;
        try
        {
            bagsSupport = OwDBAttributeBagsSupport.createAndCheckDBSupport(this.getContext());
        }
        catch (OwNotSupportedException e)
        {
            LOG.warn("OwFNCM5Network.createAOManagerRegistry: DB bags are not supported - transient attribute bags will be used!");
            bagsSupport = new OwTransientBagsSupport();
        }
        stdRegistry.registerManager(new OwAttributeBagsManager(this.getContext(), OwAOConstants.AO_ATTRIBUTE_BAG_WRITABLE, false, bagsSupport));
        stdRegistry.registerManager(new OwAttributeBagsManager(this.getContext(), OwAOConstants.AO_INVERTED_ATTRIBUTE_BAG, true, bagsSupport));
        stdRegistry.registerManager(new OwSearchTemplatesManager(aoSupport, "", this, "other", ""));
        //HACK this is the big one
        stdRegistry.registerManager(new OwFNBPM5SearchTemplatesManager(this.getContext(), aoSupport, "", "other", ""));
        OwNetworkConfiguration nwConfiguration = getNetworkConfig();
        stdRegistry.registerManager(new OwVirtualFoldersManager(aoSupport, this, this.roleManager, nwConfiguration.getVirtualFoldersContainer(null)));
        stdRegistry.registerManager(new OwDefaultAOManager(OwAOConstants.AO_PREFERENCES, aoPreferencesSupport, null));
        stdRegistry.registerManager(new OwXMLAOManager(aoSupport));
        return stdRegistry;
    }

    public OwObject createVirtualFolder(Node xmlVirtualFolderDescriptionNode_p, String strName_p, String strDmsIDPart_p) throws OwException
    {
        try
        {
            //  read the classname to instantiate from XML, default is OwStandardVirtualFolderObject
            OwXMLUtil description = new OwStandardXMLUtil(xmlVirtualFolderDescriptionNode_p);
            String strVirtualFolderClassName = description.getSafeTextValue(OwVirtualFolderObjectFactory.CLASSNAME_TAG_NAME, OwFNCM5VirtualFolderObjectFactory.class.getName());
            Class virtualFolderClass = Class.forName(strVirtualFolderClassName);

            OwVirtualFolderObjectFactory retObject = (OwVirtualFolderObjectFactory) virtualFolderClass.newInstance();

            Node rootNode = description.getSubNode(OwVirtualFolderObjectFactory.ROOT_NODE_TAG_NAME);
            if (rootNode == null)
            {
                throw new OwInvalidOperationException("Provided node is not a virtual structure, strName = " + strName_p);
            }
            //            retObject.init(this.context, this, DMS_PREFIX + "," + VIRTUAL_FOLDER_PREFIX + "," + strName_p, rootNode);
            retObject.init(this.context, this, DMS_PREFIX + "," + VIRTUAL_FOLDER_PREFIX + "," + strName_p, strName_p, rootNode);

            return retObject.getInstance(strDmsIDPart_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not create virtual folder.", e);
        }
    }

    //SQL resolver 

    public boolean isVersionable(String tableName_p, String repositoryID_p) throws OwException
    {
        OwFNCM5Resource resource = getResource(repositoryID_p);
        OwFNCM5Class<?, ?> clazz = getObjectClass(tableName_p, resource);

        return clazz.hasVersionSeries();
    }

    public boolean canOrderBy(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        OwFNCM5PropertyClass columnProperty = resolveColumnClass(tableName_p, columnName_p, repositoryID_p);
        OwFNCM5EngineBinding<?, ?, ?> engineBinding = columnProperty.getEngineBinding();
        if (engineBinding != null)
        {
            return engineBinding.isOrderable();
        }
        else
        {
            return false;
        }
    }

    private OwFNCM5PropertyClass resolveColumnClass(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        OwFNCM5Resource resource = getResource(repositoryID_p);
        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();
        OwFNCM5Class<?, ?> objectClass = null;

        if (tableName_p != null)
        {
            objectClass = objectModel.objectClassForName(tableName_p);
        }

        OwFNCM5PropertyClass columnProperty = null;

        if (objectClass != null)
        {
            try
            {
                columnProperty = objectClass.getPropertyClass(columnName_p);
            }
            catch (OwObjectNotFoundException e)
            {
                columnProperty = objectModel.propertyClassForName(columnName_p);
            }
        }
        else
        {
            columnProperty = objectModel.propertyClassForName(columnName_p);
        }

        return columnProperty;
    }

    public String resolveQueryFolderId(String resourceID_p, String path_p) throws OwException
    {
        return path_p;
    }

    public Set<String> resolveQueryableColumnNames(String tableName_p, String repositoryID_p) throws OwException
    {
        //        OwFNCM5Resource resource = getResource(repositoryID_p);
        //        OwFNCM5ContentObjectModel om = resource.getObjectModel();
        //        OwFNCM5ObjectClass clazz = om.getObjectClass(tableName_p);
        //        return clazz.getPropertyClassNames();
        return Collections.singleton("This");
    }

    public String resolveQueryTableName(String tableName_p, String repositoryID_p) throws OwException
    {

        return tableName_p;
    }

    public String resolveDefaultRepositoryID() throws OwException
    {
        return this.defaultResource.getID();
    }

    public String resolveRepositoryID(String repositoryName_p) throws OwException
    {
        OwFNCM5Resource resource = getResource(repositoryName_p);
        return resource.getID();
    }

    public boolean isInternalRepositoryID(String repositoryID_p) throws OwException
    {
        try
        {
            OwFNCM5Resource resource = getResource(repositoryID_p);
            return resource != null;
        }
        catch (OwObjectNotFoundException ex)
        {
            return false;
        }
    }

    public boolean isSubtable(String parentTable_p, String childTable_p, String repositoryID_p) throws OwException
    {
        if (parentTable_p == null)
        {
            return true;
        }
        else
        {
            //TODO: check for sub-classing
            return false;
        }
    }

    /**
     * Threaded network interaction start event.
     * A call to this method signals the beginning of a sequence of network calls on this thread.
     * The default implementation sets up the P8 5.0 thread context {@link Subject} and locale. 
     */
    protected void startThreadOperation()
    {
        if (localNetwork.get() != null)
        {
            localNetwork.remove();
        }
        localNetwork.set(this);
        OwFNCM5Credentials thisCredentials = getCredentials();
        if (thisCredentials != null)
        {
            Subject subject = thisCredentials.getLoginSubject();
            if (subject != null)
            {
                if (!subject.equals(UserContext.get().getSubject()))
                {
                    UserContext.get().pushSubject(subject);
                }
            }
            else
            {
                LOG.debug("OwFNCM5Network.startThreadOperation: current subject is null.");
            }
        }
        if (getLocale() != null && !"debugmode".equals(getLocale().toString()))
        {
            UserContext.get().setLocale(getLocale());
        }
        else
        {
            UserContext.get().setLocale(null);
        }
    }

    /**
     * Threaded network interaction stop event.
     * A call to this method signals the end of a sequence of network calls on this thread.
     * The default implementation cleans up the P8 5.0 thread context {@link Subject} and locale. 
     */
    protected void endThreadedOperation()
    {
        OwFNCM5Credentials thisCredentials = getCredentials();
        if (thisCredentials != null)
        {
            UserContext.get().popSubject();
        }
        UserContext.get().setLocale(null);
        localNetwork.remove();
    }

    public void operationPerformed(OwUserOperationEvent event_p) throws OwException
    {
        OwUserOperationType type = event_p.getType();

        if (OwUserOperationType.START.equals(type))
        {
            startThreadOperation();
        }
        else if (OwUserOperationType.STOP.equals(type))
        {
            endThreadedOperation();
        }
    }

    /**(overridable)
     * Create a Cache instance which will
     * be used by the Network instance.
     * @return OwFNCM5Cache
     */
    protected OwFNCM5Cache createCache()
    {
        return new OwFNCM5SimpleCache();
    }

    /**(overridable)
     * Create a EnumCacheHandler for the 
     * ChoiceList to OwEnumCollection handling.
     * @return OwFNCM5EnumCacheHandler
     */
    protected OwFNCM5EnumCacheHandler createEnumCacheHandler()
    {
        return new OwFNCM5SimpleEnumCacheHandler();
    }

    /**
     * Return the current used NetworkCache
     * @return OwFNCM5Cache
     */
    public OwFNCM5Cache getNetworkCache()
    {
        return this.networkCache;
    }

    public OwFNCM5Class<?, ?> from(ClassDescription classDescription) throws OwException
    {
        String className = classDescription.get_SymbolicName();
        ObjectReference classReference = classDescription.getObjectReference();
        String storeId = classReference.getObjectStoreIdentity();
        OwFNCM5Resource resource = getResource(storeId);
        if (resource == null)
        {
            throw new OwObjectNotFoundException("Could not retrieve object store resource with id=" + storeId + " of class description " + className);
        }

        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();
        return objectModel.objectClassForName(className);
    }

    public OwManagedSemiVirtualRecordConfiguration getManagedVirtualConfiguration() throws OwConfigurationException
    {
        if (null == managedVirtualConfiguration)
        {
            managedVirtualConfiguration = new OwManagedSemiVirtualRecordConfiguration();
        }

        return managedVirtualConfiguration;
    }

    /**
     * get a list of property names which represents the preferred order (sequence) of the properties
     * @return a {@link List} of property names
     * @throws Exception
     */
    public List getPreferedPropertyOrder() throws Exception
    {
        OwXMLUtil configuration = getConfiguration();
        Node preferedOrderNode = configuration.getSubNode("PreferedPropertyOrder");
        if (preferedOrderNode == null)
        {
            return new LinkedList();
        }
        return (new OwStandardXMLUtil(preferedOrderNode)).getSafeStringList();
    }

    public synchronized String getPreferredPropertyType(String propertyName_p)
    {
        if (preferredPropertyTypeMap == null)
        {
            preferredPropertyTypeMap = new HashMap();
            OwXMLUtil configuration = getConfiguration();
            List preferredPropertyTypes = configuration.getSafeNodeList("PreferredPropertyType");
            Iterator propertyElementsIt = preferredPropertyTypes.iterator();
            while (propertyElementsIt.hasNext())
            {
                Element propDefElem = (Element) propertyElementsIt.next();
                String propType = OwXMLDOMUtil.getSafeStringAttributeValue(propDefElem, "type", null);
                String propName = OwXMLDOMUtil.getElementText(propDefElem);
                if ((propName != null) && (propType != null) && (propType.equalsIgnoreCase(PROPERTY_TYPE_SYSTEM) || propType.equalsIgnoreCase(PROPERTY_TYPE_USER)))
                {
                    preferredPropertyTypeMap.put(propName, propType);
                }
            }
        }

        return preferredPropertyTypeMap.get(propertyName_p);
    }

    public OwFNBPM5Repository getBpmRepository() throws OwConfigurationException
    {
        try
        {
            if (m_bpm_repository == null && getConfiguration().getSubNode("BPM") != null)
            {
                OwXMLUtil bpmnode = new OwStandardXMLUtil(getConfiguration().getSubNode("BPM"));

                m_bpm_repository = new OwFNBPM5Repository(this, bpmnode);
            }
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not create BPM Repository instance!", e);
        }

        return m_bpm_repository;
    }

    /**
    *
    * @return a {@link Set} of descriptions for all {@link WorkflowDefinition}s found in the underlying
    *         object stores
    * @throws OwException
    */
    public Set<OwFNCM5Object<WorkflowDefinition>> getWorkflowDescriptions() throws OwException
    {
        Set<OwFNCM5Object<WorkflowDefinition>> descriptions = new HashSet<OwFNCM5Object<WorkflowDefinition>>();
        Iterator resourcesIt = getResourceIDs();
        while (resourcesIt.hasNext())
        {
            String rid = (String) resourcesIt.next();
            OwFNCM5ObjectStore owdObjectStore = getResource(rid).getObjectStore();
            ObjectStore objectStore = owdObjectStore.getNativeObject();
            try
            {
                WorkflowDefinitionSet objectStoreWorkflowDefinitions = objectStore.get_WorkflowDefinitions();
                for (Iterator i = objectStoreWorkflowDefinitions.iterator(); i.hasNext();)
                {
                    WorkflowDefinition definition = (WorkflowDefinition) i.next();
                    descriptions.add(fromNativeObject(definition));
                }
            }
            catch (EngineRuntimeException p8Ex)
            {
                if (!ExceptionCode.E_ACCESS_DENIED.equals(p8Ex.getExceptionCode()))
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Problem retrieving Workflow Defintion from os = " + objectStore.get_Name(), p8Ex);
                    }
                    String msg = getContext().localize("OwFNCM5Network.getWorkflowDescriptions.err", "Problem retrieving workflow definitions.");
                    throw new OwServerException(msg, p8Ex);
                }
            }
        }
        return descriptions;
    }

    public String getConnectionURL()
    {
        return getConfiguration().getSafeTextValue(CONF_NODE_URL, "");
    }

    /**
     * @return a Set with all the RecordClass names.
     */
    public Set<String> getRecordClassNames()
    {
        if (this.m_RecordClassNameSet == null)
        {
            m_RecordClassNameSet = new HashSet<String>();

            List recordClassNames = getConfiguration().getSafeNodeList("RecordClasses");

            Iterator it = recordClassNames.iterator();
            while (it.hasNext())
            {
                Node n = ((Node) it.next()).getFirstChild();
                if (n != null)
                {
                    m_RecordClassNameSet.add(n.getNodeValue());
                }
            }
        }

        return m_RecordClassNameSet;
    }

    /**
     * @return the networkConfig
     */
    public OwNetworkConfiguration getNetworkConfig()
    {
        return networkConfig;
    }

    public OwNetworkContext getNetworkContext()
    {
        return getContext();
    }

    public String resovleQueryColumnName(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        OwFNCM5PropertyClass columnProperty = resolveColumnClass(tableName_p, columnName_p, repositoryID_p);
        OwFNCM5EngineBinding<?, ?, ?> engineBinding = columnProperty.getEngineBinding();
        if (engineBinding != null)
        {
            return engineBinding.getSymbolicName();
        }
        else
        {
            return null;
        }
    }

    @Override
    public OwIterable<OwFNCM5Object<?>> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : FNCM5 page search
        throw new OwNotSupportedException("The FNCM5 adapter does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }

}