package com.wewebu.ow.server.ecmimpl.opencmis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

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
import com.wewebu.ow.server.ao.OwFileAOSupport;
import com.wewebu.ow.server.ao.OwSearchTemplateFactory;
import com.wewebu.ow.server.ao.OwSearchTemplatesManager;
import com.wewebu.ow.server.ao.OwVirtualFolderFactory;
import com.wewebu.ow.server.ao.OwVirtualFoldersManager;
import com.wewebu.ow.server.ao.OwXMLAOManager;
import com.wewebu.ow.server.app.id.OwIdDecoder;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.app.id.viid.OwVIIdFactory;
import com.wewebu.ow.server.app.id.viid.OwVIIdResolver;
import com.wewebu.ow.server.auth.OwAuthenticationContext;
import com.wewebu.ow.server.auth.OwLocalAuthenticationContext;
import com.wewebu.ow.server.collections.OwAggregateIterable;
import com.wewebu.ow.server.collections.OwEmptyIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwAttributeBagsSupport;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwTransientBagsSupport;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecm.eaop.OwBooleanCollector;
import com.wewebu.ow.server.ecm.eaop.OwJoinPoint;
import com.wewebu.ow.server.ecm.eaop.OwNativeSearchAdvice;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.ecmimpl.OwBackwardsCompatibilityAOProvider;
import com.wewebu.ow.server.ecmimpl.OwBackwardsCompatibilityAOProvider.OwLegacyAONetwork;
import com.wewebu.ow.server.ecmimpl.OwCredentialsConstants;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISAlfrescoAuthenticationProvider;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISAuthenticationInterceptor;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentials;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISDefaultAuthenticationProvider;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISLDAPAuthenticationProvider;
import com.wewebu.ow.server.ecmimpl.opencmis.collections.OwCMISQueryIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISResourceDomainFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISCSQLCProcessor;
import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISLikeWildCardDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISQueryResult;
import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISSearchResult;
import com.wewebu.ow.server.ecmimpl.opencmis.ui.OwCMISAccessRightsModule;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISResourceCache;
import com.wewebu.ow.server.ecmimpl.opencmis.viewer.info.OwCMISInfoProvider;
import com.wewebu.ow.server.event.OwEventManager;
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
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.viewer.OwInfoProvider;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Apache Chemistry (OpenCMIS) based OwNetwork implementation.
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
 *@see "http://chemistry.apache.org/java/developing/index.html"
 */
@SuppressWarnings("rawtypes")
public class OwCMISNetwork implements OwCMISAuthenticatedNetwork, OwSearchTemplateFactory, OwVirtualFolderFactory, OwVIIdResolver, OwLegacyAONetwork
{
    private static Logger LOG = OwLog.getLogger(OwCMISNetwork.class);

    /**optional configuration node name containing the full qualified java class name*/
    public static final String CONF_NODE_CSQLCPROCESSOR = "CSQLCProcessorClass";

    private OwCMISNetworkCfg config;
    private OwNetworkContext context;

    private OwEventManager eventManager;

    private OwCMISResourceCache resourceCache;

    private Map<String, ?> parameters;

    private OwRoleManager roleManager;

    private OwAOManagerRegistry aoManagerRegistry;

    private OwVirtualFolderFactory virtualFolderFactory;

    private OwCMISLikeWildCardDefinitions likeWildCards;

    private OwCMISExternalEntitiesResolver externalEntitiesResolver;

    private OwCMISResourceDomainFolder domainRoot;

    private String defaultRepositoryNameOrId;

    private OwCMISAuthenticationInterceptor authenticationInterceptor;

    private OwCMISDefaultAuthenticationProvider authenticationProvider;

    private OwAuthenticationContext localAuthenticationContext;

    private OwWorkitemRepository m_WorkitemRepository;

    public OwCMISNetwork()
    {
        this(null, null);
    }

    public OwCMISNetwork(OwCMISExternalEntitiesResolver externalEntitiesResolver_p)
    {
        this(externalEntitiesResolver_p, null);
    }

    public OwCMISNetwork(OwCMISExternalEntitiesResolver externalEntitiesResolver_p, OwVirtualFolderFactory virtualFolderFactory_p)
    {
        super();
        localAuthenticationContext = new OwLocalAuthenticationContext();
        this.externalEntitiesResolver = externalEntitiesResolver_p;
        this.virtualFolderFactory = virtualFolderFactory_p;
    }

    public boolean canBatch()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int iContext_p) throws OwException
    {
        //if resource_p == null, default resource will be used
        if (parent_p != null)
        {
            switch (parent_p.getType())
            {
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                case OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER:
                case OwObjectReference.OBJECT_TYPE_DYNAMIC_VIRTUAL_FOLDER:
                case OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER:
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:// for OwObjectLink handling
                    return true;

                default:
                    return false;
            }

        }
        else
        {
            //Unfilled handling will be done by createNewObject(...)
            return Boolean.TRUE.booleanValue();
        }
    }

    public boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int iContext_p) throws OwException
    {
        if (parent_p instanceof OwCMISObject)
        {//check if internal resource
            try
            {
                getResource(parent_p.getResourceID());
                return true;
            }
            catch (Exception e)
            {
                String msg = "Could not retrieve parent resource id";
                if (LOG.isDebugEnabled())
                {
                    LOG.debug(msg, e);
                }
                else
                {
                    LOG.info("OwCMISNetwork.canCreateObjectCopy: " + msg);
                }
            }
        }
        return false;
    }

    public boolean canDo(OwObject obj_p, int iFunctionCode_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canEditAccessRights(OwObject object_p) throws OwException
    {
        try
        {
            return object_p instanceof OwObjectSkeleton || (object_p.canGetPermissions() && object_p.canSetPermissions());
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not process Acl capabilities", e);
            throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.err.canAclCheck", "Problem during check of Permission-capabilities"), e);
        }
    }

    public boolean canRefreshStaticClassdescriptions() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canUserSelect() throws OwException
    {
        return this.getAuthenticationProvider().canUserSelect();
    }

    public void closeBatch(OwBatch batch_p) throws OwException
    {
        // TODO Auto-generated method stub
    }

    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        return createNewObject(fPromote_p, mode_p, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, Boolean.FALSE.booleanValue());
    }

    public String createNewObject(OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p) throws OwException
    {
        return createNewObject(true, null, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, false);
    }

    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        try
        {
            OwCMISSession session = null;
            OwCMISObject cmisParent = prepareParentObject(parent_p, properties_p);

            if (cmisParent != null)
            {
                session = getSession(cmisParent.getResource().getID());
            }

            if (session == null)
            {
                session = resource_p == null ? getSession(null) : getSession(resource_p.getID());
            }

            OwCMISCapabilities capabilities = session.getResourceInfo().getCapabilities();
            if (null == cmisParent && !capabilities.isCapabilityUnfiling())
            {
                // We must select a default parent
                cmisParent = session.getObjectByPath("/", false);
            }

            return session.createObject(fPromote_p, mode_p, strObjectClassName_p, properties_p, permissions_p, content_p, cmisParent, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (CmisBaseException cmise)
        {
            throw cmise;
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred while creating object.", ex);
            throw new OwServerException(new OwString("opencmis.OwCMISNetwork.err.createNewObject", "An error occurred while creating object."), ex);
        }
    }

    /**
     * Identify parent object from provided OwObject, if virtual it will be searched for semi-virtual path restriction.
     * Can return null if provided parent is null or no semi-virtual path restriction was found. 
     * @param parent_p OwObject (can be null)
     * @param properties_p OwPropertyCollection which will be used during create.
     * @return OwCMISObject or null
     * @throws OwException
     */
    protected OwCMISObject prepareParentObject(OwObject parent_p, OwPropertyCollection properties_p) throws OwException
    {
        if (parent_p == null)
        {
            return null;
        }
        OwCMISObject parent = null;

        if (parent_p.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
        {
            OwSearchNode specialNode = null;
            try
            {
                specialNode = parent_p.getSearchTemplate().getSearch(false).findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
            }
            catch (OwException ex)
            {
                throw ex;
            }
            catch (Exception e)
            {
                LOG.error("Could not get/find special node for path identification", e);
                throw new OwServerException(new OwString("opencmis.OwCMISNetwork.err.prepareParent", "Could not get/find special node for path identification."), e);
            }

            if (specialNode != null)
            {
                Iterator<?> it = specialNode.getChilds().iterator();
                OwSearchNode vfolderPathNode = null;
                while (it.hasNext())
                {
                    OwSearchNode specialChild = (OwSearchNode) it.next();
                    OwSearchCriteria specialCriteria = specialChild.getCriteria();
                    String criteriaUName = specialCriteria.getUniqueName();
                    String criteriaClassName = specialCriteria.getClassName();
                    if (OwSemiVirtualFolderAdapter.VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY.equals(criteriaUName) && OwSearchPathField.CLASS_NAME.equals(criteriaClassName))
                    {
                        vfolderPathNode = specialChild;
                        break;
                    }
                }
                if (vfolderPathNode != null)
                {
                    OwSearchCriteria virtualFolderPathCriteria = vfolderPathNode.getCriteria();
                    OwSearchPath virtualFolderPath = (OwSearchPath) virtualFolderPathCriteria.getValue();

                    String path = virtualFolderPath.getPathName();
                    parent = (OwCMISNativeObject<?>) getObjectFromPath(path, true);
                }
            }
        }
        else
        {
            if (parent_p instanceof OwCMISNativeObject<?>)
            {
                parent = (OwCMISNativeObject<?>) parent_p;
            }
            else
            {
                if (parent_p.getType() == OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER)
                {
                    OwCMISSession session;
                    try
                    {
                        session = getSession(parent_p.getResourceID());
                    }
                    catch (OwException owex)
                    {
                        throw owex;
                    }
                    catch (Exception e)
                    {
                        LOG.error("Could not identify repository for root folder retrieval", e);
                        throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.err.prepareParent.rootFolder", "Could not retrieve resource id from object"), e);
                    }
                    parent = session.getRootFolder();
                }
                else
                {
                    LOG.error("OwCMISNetwork.prepareParent: Unsupported parent type " + parent_p.getClass());
                    throw new OwInvalidOperationException(getContext().localize("opencmis.OwCMISNetwork.err.invalidParent", "Provided/Defined object is not valid/supported as parent"));
                }
            }
        }

        return parent;
    }

    public String createObjectCopy(OwObject obj_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwObject parent_p, int[] childTypes_p) throws OwException
    {
        boolean sameRepository = false;
        try
        {
            String objectResourceId = obj_p.getResourceID();
            String parendResourceId = parent_p.getResourceID();
            sameRepository = objectResourceId.equals(parendResourceId);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not compare resource id for copy process.", e);
            throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.err.createObjectCopy.idCompare", "Could not compare resource id for copy process."), e);
        }

        if (sameRepository && obj_p instanceof OwCMISObject && parent_p instanceof OwCMISObject)
        {
            OwCMISObject cmisObject = (OwCMISObject) obj_p;
            OwCMISObject cmisParent = (OwCMISObject) parent_p;
            OwCMISObject copy = cmisObject.createCopy(cmisParent, properties_p, permissions_p, childTypes_p);
            if (childTypes_p != null && childTypes_p.length > 0)
            {
                OwObjectCollection col = cmisObject.getChilds(childTypes_p, null, null, 100, 0, null);
                if (col != null && !col.isEmpty())
                {
                    Iterator it = col.iterator();
                    List<Integer> lstTypes = new LinkedList<Integer>();
                    for (int i = 0; i < childTypes_p.length; i++)
                    {
                        lstTypes.add(Integer.valueOf(childTypes_p[i]));
                    }
                    while (it.hasNext())
                    {
                        OwObject structObj = (OwObject) it.next();
                        if (lstTypes.contains(Integer.valueOf(structObj.getType())))
                        {
                            createObjectCopy(structObj, null, null, copy, childTypes_p);//recursive copy of structure
                        }
                    }
                }
            }
            try
            {
                return copy.getDMSID();
            }
            catch (Exception e)
            {
                LOG.error("Could not retrieve object copy DMSID", e);
                throw new OwInvalidOperationException(getContext().localize("opencmis.OwCMISNetwork.err.createObjectCopy.getDmsid", "Could not retrieve object copy DMSID"), e);
            }
        }
        else if (parent_p instanceof OwCMISObject)
        {
            try
            {
                String className = obj_p.getClassName();
                OwPropertyCollection copiedPorperties = obj_p.getProperties(null);
                OwPropertyCollection properties = new OwStandardPropertyCollection();

                properties.putAll(copiedPorperties);
                OwCMISPropertyClass<?> objectTypeId = (OwCMISPropertyClass<?>) getFieldDefinition(PropertyIds.OBJECT_TYPE_ID, parent_p.getResourceID());
                properties.remove(objectTypeId.getFullQualifiedName().toString());
                properties.remove(objectTypeId.getNonQualifiedName());

                if (properties_p != null)
                {
                    properties.putAll(properties_p);
                }
                boolean promote = true;
                if (obj_p.hasVersionSeries())
                {
                    promote = obj_p.getVersion().isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
                String copyDMSID = createNewObject(promote, null, parent_p.getResource(), className, properties, permissions_p, obj_p.getContentCollection(), parent_p, obj_p.getMIMEType(), obj_p.getMIMEParameter());

                if (childTypes_p != null && childTypes_p.length > 0)
                {//recursive copy of structure
                    OwObject copy = getObjectFromDMSID(copyDMSID, true);
                    OwObjectCollection col = obj_p.getChilds(childTypes_p, null, null, 100, 0, null);
                    if (col != null)
                    {
                        Iterator it = col.iterator();
                        while (it.hasNext())
                        {
                            OwObject structObj = (OwObject) it.next();
                            createObjectCopy(structObj, null, null, copy, childTypes_p);
                        }
                    }
                }
                return copyDMSID;
            }
            catch (Exception e)
            {
                LOG.error("Could not perform non-CMIS object copy!", e);
                throw new OwInvalidOperationException(getContext().localize("opencmis.OwCMISNetwork.err.createObjectCopy", "Could not copy object."), e);
            }
        }
        else
        {
            LOG.error("OwCMISNetwork.createObjectCopy: Copy can be performed only when parent is a CMIS object!");
            throw new OwInvalidOperationException(getContext().localize("opencmis.OwCMISNetwork.err.createObjectCopy.invalidParent", "Cannot copy object for provided parent object."));
        }
    }

    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwResource resource_p) throws OwException
    {
        try
        {
            OwCMISResource res = resource_p == null ? getDefaultResource() : getResource(resource_p.getID());
            OwCMISSession session = getSession(res.getID());
            OwObjectSkeleton skeleton = session.createObjectSkeleton(objectclass_p, this);
            return skeleton;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            LOG.error("OwCMISNetwork.createObjectSkeleton: Problem instantiation of Skeleton object!", ex);
            throw new OwServerException(new OwString("opencmis.OwCMISNetwork.err.createObjectSkeleton", "An error occurred while creating object!"), ex);
        }
    }

    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws OwException
    {
        if (iMaxSize_p < 0)
        {
            throw new OwInvalidOperationException("Invalid maximum parameter defined, value is = " + (Integer.toString(iMaxSize_p)));
        }
        OwCMISSearchResult result = doCMISSearch(searchCriteria_p, sortCriteria_p, propertyNames_p, iMaxSize_p, iVersionSelection_p);
        return result.getCmisSearchResult();
    }

    @SuppressWarnings("unchecked")
    public OwCMISSearchResult doCMISSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int maxSize_p, int versionSelection_p) throws OwException
    {
        /*Repositories MUST support the escaping of characters using a backslash (\) in the query statement.  The 
        backslash character (\) will be used to escape characters within quoted strings in the query as follows: 
          1. \" will represent a double-quote (") character
          2. \’ will represent a single-quote(‘) character
          3. \ \ will represent a backslash (\) character
          4. Within a LIKE string, \% and \_ will represent the literal characters % and _, respectively.
          5. All other instances of a \ are errors*/

        BigInteger max = new BigInteger(Integer.toString(2 + maxSize_p));

        //results mapped on their repository IDs
        Map<String, List<OwCMISQueryResult>> searchResults = new HashMap<String, List<OwCMISQueryResult>>();
        OwCMISCSQLCProcessor processor = factorySearchNodeSQLProcessor();

        /*Execute the search query, against the defined repository*/

        OwExternal<List<OwQueryStatement>> statementsEx = processor.createSQLStatements(searchCriteria_p, propertyNames_p, sortCriteria_p);
        List<OwQueryStatement> statements = statementsEx.getInternal();

        for (OwQueryStatement statement : statements)
        {
            //FIXME move all of this code inside the OwCMISSession
            OwBooleanCollector adviceResult = OwBooleanCollector.newAND(true);
            OwNativeSearchAdvice sqlSearchAdvice = OwJoinPoint.joinPoint(OwNativeSearchAdvice.class, adviceResult);
            sqlSearchAdvice.adviceNativeSearch(statement);
            if (adviceResult.getResult())
            {
                final String repositoryID = statement.getTargetRepositoryID();
                OwCMISRepositoryResource resource = (OwCMISRepositoryResource) getResource(repositoryID);
                RepositoryCapabilities capabilities = resource.getRepository().getCapabilities();
                boolean searchVersionAll = versionSelection_p == OwSearchTemplate.VERSION_SELECT_ALL;
                if (searchVersionAll)
                {
                    //check version selection is supported by repository
                    searchVersionAll = capabilities.isAllVersionsSearchableSupported();
                    if (!searchVersionAll)
                    {
                        //TODO may be throw an Invalid Exception, if not supported by repository
                        LOG.warn("OwCMISNetwork.doCMISSearch: The repository does not support search in all versions! Search will be excuted for current versions only!");
                    }
                }

                LOG.debug("OwCMISNetwork.doCMISSearch: searchVersionAll = " + searchVersionAll);
                BigInteger skip = BigInteger.ZERO;
                OwCMISSession session = getSession(repositoryID);
                do
                {
                    //delegate Search to CMIS-service
                    OwObjectCollection searchResultLst = session.query(statement, searchVersionAll, true, IncludeRelationships.NONE, "", max, skip);
                    if (searchResultLst.size() > 0)
                    {
                        skip = skip.add(new BigInteger(Integer.toString(searchResultLst.size())));
                        List<OwCMISQueryResult> repositoryResults = searchResults.get(repositoryID);
                        if (repositoryResults == null)
                        {
                            repositoryResults = new LinkedList<OwCMISQueryResult>();
                            searchResults.put(repositoryID, repositoryResults);
                        }
                        repositoryResults.add(new OwCMISQueryResult(statement, searchResultLst));
                    }
                    else
                    {
                        break;
                    }

                } while (skip.compareTo(max) < 0);
            }
        }

        OwObjectCollection cmisSearchResult = createSearchResult(searchResults, versionSelection_p, maxSize_p);
        return new OwCMISSearchResult(statementsEx, cmisSearchResult);

    }

    @SuppressWarnings("unchecked")
    /**
     * Wraps native search results into a framework defined search collection.
     * @param searchResults_p results mapped by their repository IDs 
     * @param versionSelection_p the value of the calling <code>doSearch</code> parameter   
     * @param maxSize_p the value of the calling <code>doSearch</code> parameter
     * @return an {@link OwObjectCollection} containing Alfresco Workdesk objects corresponding to all objects in the given collections
     */
    private OwObjectCollection createSearchResult(Map<String, List<OwCMISQueryResult>> searchResults_p, int versionSelection_p, int maxSize_p) throws OwException
    {
        try
        {
            OwStandardObjectCollection result = new OwStandardObjectCollection();
            result.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.TRUE);
            for (Map.Entry<String, List<OwCMISQueryResult>> repositoryResult : searchResults_p.entrySet())
            {
                List<OwCMISQueryResult> resultObjects = repositoryResult.getValue();

                String repositoryID = repositoryResult.getKey();
                OwCMISRepositoryResource resource = (OwCMISRepositoryResource) getResource(repositoryID);

                RepositoryCapabilities capabilities = resource.getRepository().getCapabilities();

                final boolean pwcSearchable = capabilities.isPwcSearchableSupported();
                final boolean searchCheckedOut = versionSelection_p == OwSearchTemplate.VERSION_SELECT_CHECKED_OUT;
                if (searchCheckedOut && !pwcSearchable)
                {
                    //TODO may be throw an Invalid Exception, if not supported by repository
                    LOG.warn("OwCMISNetwork.createSearchResult: The repository does not support search for checked-out documents!");
                }

                boolean searchVersionAll = versionSelection_p == OwSearchTemplate.VERSION_SELECT_ALL;
                if (searchVersionAll)
                {
                    //check version selection is supported by repository
                    searchVersionAll = capabilities.isAllVersionsSearchableSupported();
                    if (!searchVersionAll)
                    {
                        //TODO may be throw an Invalid Exception, if not supported by repository
                        LOG.warn("OwCMISNetwork.createSearchResult: The repository does not support search in all versions! Search will be excuted for current versions only!");
                    }
                }

                for (OwCMISQueryResult qResult : resultObjects)
                {
                    OwObjectCollection objects = qResult.getObjectList();
                    if (result.size() > maxSize_p && objects.size() > 0)
                    {
                        result.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
                        break;
                    }
                    if (objects != null)
                    {
                        //                        Map<String, OwCMISObject> candidates = new HashMap<String, OwCMISObject>();
                        for (Iterator<OwCMISObject> i = objects.iterator(); i.hasNext();)
                        {
                            OwCMISObject cmisObject = i.next();
                            result.add(cmisObject);
                            if (result.size() >= maxSize_p && i.hasNext())
                            {
                                result.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
                                break;
                            }
                        }
                    }
                }
                if (result.size() >= maxSize_p)
                {
                    break;
                }
            }
            result.setAttribute(OwObjectCollection.ATTRIBUTE_SIZE, Integer.valueOf(result.size()));
            return result;

        }
        catch (OwException e)
        {
            LOG.error("Search result could not be created!", e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Search result could not be created!", e);
            throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.err.createSearchResult", "Error transforming search result!"), e);
        }
    }

    /**(overridable) 
     * Factory which creates the SQLProcessor instance to 
     * be used for parsing the OwSearchNode tree in the doSearch-method.
     * @return OwCMISSearchNodeSQLProcessor
     * @throws OwException if initialization of SQLOperator class failed
     * @see #doSearch(OwSearchNode, OwSort, Collection, int, int)
     */
    @SuppressWarnings("unchecked")
    protected OwCMISCSQLCProcessor factorySearchNodeSQLProcessor() throws OwException
    {
        OwSQLEntitiesResolver resolver = createSQLEntitiesResolver();
        String pClass = getNetworkConfiguration().getConfigNode().getSafeTextValue(CONF_NODE_CSQLCPROCESSOR, null);
        if (pClass != null && !"".equals(pClass))
        {
            try
            {
                Class clazz = Class.forName(pClass);
                Constructor constructor = clazz.getConstructor(OwSQLEntitiesResolver.class);

                return (OwCMISCSQLCProcessor) constructor.newInstance(resolver);
            }
            catch (SecurityException e)
            { //permission denied to access the defined class/constructor
                LOG.error("SecurityManager restrict the instantiation of type " + pClass + "!", e);
                throw new OwServerException(getContext().localize1("opencmis.OwCMISNetwork.factorySQLProc.securityEx", "Instantiation of type %1 is restricted by security.", pClass), e);
            }
            catch (InstantiationException e)
            { //cannot instantiate defined class
                LOG.error("Cannot Instantiate the defined class " + pClass, e);
                throw new OwConfigurationException(getContext().localize1("opencmis.OwCMISNetwork.factorySQLProc.instanceEx", "The defined class %1 is not allowed to be instantiated.", pClass), e);
            }
            catch (IllegalAccessException e)
            { //default constructor is not accessible
                LOG.fatal("Cannot Instantiate defined class " + pClass + " because it is not accessible.", e);
                throw new OwServerException(getContext().localize1("opencmis.OwCMISNetwork.factorySQLProc.illegalAccessEx", "The default constructor of class %1 is not accessible or does not exist.", pClass), e);
            }
            catch (ClassNotFoundException e)
            { //defined class could not be found
                LOG.error("The class " + pClass + " could not be found by current ClassLoader.", e);
                throw new OwConfigurationException(getContext().localize1("opencmis.OwCMISNetwork.factorySQLProc.classNotFoundEx", "The defined class %1 could not be found.", pClass), e);
            }
            catch (NoSuchMethodException e)
            {
                LOG.error("The class " + pClass + " does not contain a Constructor(OwSQLEntitiesResolver.class).", e);
                throw new OwConfigurationException(getContext().localize1("opencmis.OwCMISNetwork.factorySQLProc.missingConstructor", "The defined class %1 miss a Constructor(OwSQLEntitiesResolver.class).", pClass), e);
            }
            catch (IllegalArgumentException e)
            {
                LOG.fatal("Provided entieties resolver is not implementing com.wewebu.ow.csqlc.OwSQLEntitiesResolver", e);
                throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.factorySQLProc.invalidResolver", "The defined the provided resolver does not implement OwSQLEntitiesResolver.class)."), e);
            }
            catch (InvocationTargetException e)
            {
                LOG.error("Creation of CSQLCProcessor (" + pClass + ") failed.", e);
                throw new OwConfigurationException(getContext().localize1("opencmis.OwCMISNetwork.factorySQLProc.instanceFailed", "The defined SQLProcessor %1 could not be instantiated.", pClass), e);
            }
        }
        else
        {
            return new OwCMISCSQLCProcessor(resolver);
        }
    }

    /**(overridable)
     * Create an entities resolver which will be used by the OwCMISCSQLCProcessor.
     * @return OwSQLEntitiesResolver
     * @throws OwException
     * @since 4.1.0.0
     */
    protected OwSQLEntitiesResolver createSQLEntitiesResolver() throws OwException
    {
        return new OwCMISSQLStandardEntitiesResolver(this, externalEntitiesResolver);
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
        return getAOProvider().getApplicationObject(OwAOConstants.fromType(type_p), strName_p, Arrays.asList(param_p), forceUserSpecificObject_p, createIfNotExist_p);
    }

    public Object getApplicationObject(int type_p, String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getAOProvider().getApplicationObject(OwAOConstants.fromType(type_p), strName_p, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public Collection<?> getApplicationObjects(int type_p, String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {
        return getAOProvider().getApplicationObjects(OwAOConstants.fromType(type_p), strName_p, forceUserSpecificObject_p);
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
        OwCMISNetworkCfg configuration = getNetworkConfiguration();
        OwAOSupport fileAOSupport = new OwFileAOSupport(this, configuration, "WEB-INF/cmis");
        OwDefaultRegistry stdRegistry = new OwDefaultRegistry();
        //preferences configuration
        final String defaultPreferencesFolder = "owpreferences";
        String preferencesFolder = configuration.getConfigNode().getSafeTextValue("UserDefinedPreferencesFolder", defaultPreferencesFolder);
        OwAttributeBagsSupport bagsSupport = null;
        try
        {
            bagsSupport = OwDBAttributeBagsSupport.createAndCheckDBSupport(this.getContext());
        }
        catch (OwNotSupportedException e)
        {
            LOG.warn("OwCMISNetwork.createAOManagerRegistry: DB bags are not supported - transient attribute bags will be used!");
            bagsSupport = new OwTransientBagsSupport();
        }
        stdRegistry.registerManager(new OwAttributeBagsManager(this.getContext(), OwAOConstants.AO_ATTRIBUTE_BAG_WRITABLE, false, bagsSupport));
        stdRegistry.registerManager(new OwAttributeBagsManager(this.getContext(), OwAOConstants.AO_INVERTED_ATTRIBUTE_BAG, true, bagsSupport));
        stdRegistry.registerManager(new OwSearchTemplatesManager(fileAOSupport, "", this, "owsearchtemplates", ""));
        stdRegistry.registerManager(new OwVirtualFoldersManager(fileAOSupport, virtualFolderFactory, roleManager, configuration.getVirtualFoldersContainer(null)));
        stdRegistry.registerManager(new OwDefaultAOManager(OwAOConstants.AO_PREFERENCES, fileAOSupport, preferencesFolder));
        stdRegistry.registerManager(new OwXMLAOManager(fileAOSupport));
        return stdRegistry;
    }

    public OwNetworkContext getContext()
    {
        return this.context;
    }

    public OwCMISCredentials getCredentials() throws OwException
    {
        return (OwCMISCredentials) this.getAuthenticationProvider().getCredentials();
    }

    public String getDMSPrefix()
    {
        return getNetworkConfiguration().getDMSIDDecoder().getDMSIDPrefix();
    }

    public OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws OwException
    {
        if (canEditAccessRights(object_p))
        {
            OwCMISAccessRightsModule accessRightsModul = new OwCMISAccessRightsModule(object_p);
            try
            {
                accessRightsModul.init(this);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Could not initialize AccessRightsModule", e);
                throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.err.AccesRightsInit", "Could not initialize AccessRightsModule"), e);
            }
            return accessRightsModul;
        }
        else
        {
            return null;
        }
    }

    public OwEventManager getEventManager()
    {
        return this.eventManager;
    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String resourceIdOrName_p) throws OwException, OwObjectNotFoundException
    {
        OwCMISQualifiedName qName = new OwCMISQualifiedName(strFieldDefinitionName_p);
        OwCMISResource resource = getResource(resourceIdOrName_p);
        if (qName.getNamespace() != null)
        {
            OwCMISObjectClass objectClass = getObjectClass(qName.getNamespace(), resource);
            return objectClass.getPropertyClass(strFieldDefinitionName_p);
        }
        else
        {
            //This might be a virtual property/field
            OwCMISObjectClass objClass = getObjectClass("cmis:document", resource);
            OwFieldDefinition field = null;
            try
            {
                field = objClass.getPropertyClass(strFieldDefinitionName_p);
            }
            catch (OwObjectNotFoundException ex)
            {
                try
                {
                    objClass = getObjectClass("cmis:relationship", resource);
                    field = objClass.getPropertyClass(strFieldDefinitionName_p);
                }
                catch (OwObjectNotFoundException e)
                {
                    //do nothing, a correct exception will be thrown
                }
            }

            if (null == field)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwCMISNetwork.getFieldDefinition: No field definition found for name = " + strFieldDefinitionName_p);
                }
                throw new OwObjectNotFoundException(getContext().localize1("opencmis.OwCMISNetwork.getFieldDefinition.err.not.found", "Could not find a field definition for name = %1!", strFieldDefinitionName_p));
            }
            return field;
        }
    }

    public synchronized Object getInterface(String strInterfaceName_p, Object oObject_p) throws OwException
    {
        if (!this.hasInterface(strInterfaceName_p))
        {
            String msg = getContext().localize1("ecmimpl.opencmis.error.interface.notSupported", "The interface %1 is not supported in this configuration.", strInterfaceName_p);
            throw new OwInvalidOperationException(msg);
        }

        if (OwVIIdResolver.class.getCanonicalName().equals(strInterfaceName_p))
        {
            return this;
        }
        else
        {
            try
            {
                Class interfaceClass = Class.forName(strInterfaceName_p);

                if (OwWorkitemRepository.class.isAssignableFrom(interfaceClass))
                {
                    if (null == this.m_WorkitemRepository)
                    {
                        this.m_WorkitemRepository = createWorkItemRepository();
                    }
                    return this.m_WorkitemRepository;
                }
                if (OwInfoProvider.class.isAssignableFrom(interfaceClass))
                {
                    return new OwCMISInfoProvider(this);
                }
                if (OwRestletAuthenticationHandler.class.isAssignableFrom(interfaceClass))
                {
                    return getAuthInterceptor().createRestletAuthenticationHandler(getCredentials());
                }
            }
            catch (ClassNotFoundException e)
            {
                String message = "The requested interface was not found on classpath: " + strInterfaceName_p;
                LOG.error(message, e);
                throw new OwInvalidOperationException(message, e);
            }
        }

        String message = "Requested interface is not supported by this network, interface = " + strInterfaceName_p;
        LOG.error(message);
        throw new OwNotSupportedException(message);
    }

    /**
     * @return An instance of {@link OwWorkitemRepository} as specified in the owbootstrap.xml configuration file.
     * @throws OwConfigurationException Thrown if we were unable to create the BPM instance or the owbootstrap.xml file does not have a BPM configured. 
     */
    @SuppressWarnings({ "unchecked" })
    private OwWorkitemRepository createWorkItemRepository() throws OwConfigurationException
    {
        OwXMLUtil bpmConfigNode = getNetworkConfiguration().getBpmNode();
        String implementationClassName = bpmConfigNode.getSafeTextValue("ClassName", "");

        if (0 == implementationClassName.length())
        {
            String message = "No BPM configured for this instance!";
            LOG.error(message);
            throw new OwConfigurationException(message);
        }

        try
        {
            Class implementationClass = Class.forName(implementationClassName);
            Constructor constructor = implementationClass.getConstructor(OwNetwork.class, OwXMLUtil.class);
            OwWorkitemRepository instance = (OwWorkitemRepository) constructor.newInstance(this, bpmConfigNode);
            return instance;
        }
        catch (ClassNotFoundException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
        catch (SecurityException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
        catch (NoSuchMethodException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
        catch (IllegalArgumentException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
        catch (InstantiationException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
        catch (IllegalAccessException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
        catch (InvocationTargetException e)
        {
            String message = "Could not create instance.";
            LOG.error(message, e);
            throw new OwConfigurationException(message, e);
        }
    }

    public Locale getLocale()
    {
        return getContext().getLocale();
    }

    public OwUILoginModul getLoginSubModul() throws OwException
    {
        return this.getAuthenticationProvider().getLoginSubModul();
    }

    /**
     * Get the Network configuration object,
     * which provides special configurations as utility object.
     * @return OwCMISNetworkCfg
     */
    public OwCMISNetworkCfg getNetworkConfiguration()
    {
        return this.config;
    }

    public OwCMISObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws OwException
    {
        OwResource res = resource_p == null ? getResource(null) : resource_p;
        String resId;
        try
        {
            resId = res.getID();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e1)
        {
            LOG.error("Request of repository id failed", e1);
            throw new OwServerException("Request of repository id failed", e1);
        }

        try
        {
            return getSession(resId).getObjectClass(strClassName_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Requested objectclass " + strClassName_p + " could not be found in repository = " + resId + " (" + res.getDisplayName(getLocale()) + ")");
            }
            throw new OwObjectNotFoundException(getContext().localize1("opencmis.OwCMISNetwork.err.objclassNotFound", "Requested object class (%1) not found", strClassName_p), e);
        }

    }

    public Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws OwException
    {
        OwCMISResource cmisResource = (OwCMISResource) resource_p;

        if (null == cmisResource)
        {
            cmisResource = getDefaultResource();
        }
        String resId = cmisResource.getID();
        OwCMISSession session = getSession(resId);
        Set<OwCMISObjectClass> classes = session.getObjectClasses(iTypes_p, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p);
        Map<String, String> classNames = new HashMap<String, String>();
        for (OwCMISObjectClass owCMISObjectClass : classes)
        {
            classNames.put(owCMISObjectClass.getClassName(), owCMISObjectClass.getDisplayName(getLocale()));
        }
        return classNames;
    }

    @Override
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        if (hasInterface(OwWorkitemRepository.class.getName()))
        {
            OwWorkitemRepository bpmRepository = (OwWorkitemRepository) this.getInterface(OwWorkitemRepository.class.getName(), null);
            if (strDMSID_p.startsWith(bpmRepository.getDMSPrefix()))
            {
                return bpmRepository.getObjectFromDMSID(strDMSID_p, true);
            }
        }

        if (strDMSID_p.startsWith(OwVIId.VIID_PREFIX))
        {
            OwVIIdFactory factory = getContext().getRegisteredInterface(OwVIIdFactory.class);
            OwIdDecoder<OwVIId> decoder = factory.createViidDecoder();

            return getObject(decoder.decode(strDMSID_p));
        }
        else
        {
            OwCMISDMSID dmsid = getNetworkConfiguration().getDMSIDDecoder().createDMSID(strDMSID_p);
            if (domainRoot.getName().equals(dmsid.getResourceID()))
            {
                return domainRoot;//TODO: check for CrossScenario
            }
            else
            {
                OwCMISSession ses = getSession(dmsid.getResourceID());
                return ses.getObject(dmsid.getDMSIDString(), fRefresh_p);
            }
        }
    }

    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws OwException
    {
        if (strPath_p.equals(OwObject.STANDARD_PATH_DELIMITER))
        {
            return domainRoot;
        }
        else
        {
            int idx = strPath_p.indexOf(OwObject.STANDARD_PATH_DELIMITER, 1);
            if (idx > 0)
            {
                OwCMISSession ses;
                String resourceName = null;
                String path = null;
                if (strPath_p.startsWith(OwObject.STANDARD_PATH_DELIMITER))
                {
                    resourceName = strPath_p.substring(1, idx);
                }
                else
                {
                    resourceName = strPath_p.substring(0, idx);
                }

                if ("".equals(resourceName))
                {//workaround for virtual Domain, which does not have an id/resource
                    idx = strPath_p.indexOf(OwObject.STANDARD_PATH_DELIMITER, 2);
                    resourceName = strPath_p.substring(2, idx);
                }

                ses = getResourceCache().getSession(resourceName);
                if (ses == null)
                {
                    LOG.info("Path does not have a valid resource name, using default resource.");
                    ses = getSession(null);
                    path = strPath_p;
                }
                else
                {
                    path = strPath_p.substring(idx);
                }

                return ses.getObjectByPath(path, fRefresh_p);
            }
            throw new OwObjectNotFoundException(getContext().localize1("opencmis.OwCMISNetwork.err.path", "Invalid or unsupported path definition %1", strPath_p));
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized OwCMISResourceCache getResourceCache() throws OwException
    {
        if (resourceCache == null)
        {
            this.resourceCache = new OwCMISResourceCache();

            SessionFactoryImpl sessionFactory = SessionFactoryImpl.newInstance();
            Map<String, String> opencmisParameters = (Map<String, String>) parameters.get(OwCMISSessionParameter.OPENCMIS_SESSION_PARAMETERS);
            List<Repository> repositories;
            if (getAuthInterceptor() != null)
            {
                AuthenticationProvider authProv = getCredentials().getAuthenticationProvider();
                repositories = sessionFactory.getRepositories(opencmisParameters, null, authProv, null);
            }
            else
            {
                repositories = sessionFactory.getRepositories(opencmisParameters);
            }
            LOG.debug("OwCMISNetwork.getResourceCache: initializing cache");
            for (Repository repository : repositories)
            {
                OwCMISRepositoryResource repositoryResource = new OwCMISRepositoryResource(repository, parameters, this);
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Cache entry: " + repository.getName() + " id = " + repository.getId());
                }
                this.resourceCache.add(repositoryResource);
                if (defaultRepositoryNameOrId == null)
                {
                    this.defaultRepositoryNameOrId = repository.getId();
                }
            }
        }

        return this.resourceCache;

    }

    public OwCMISResource getDefaultResource() throws OwException
    {
        OwCMISResource defaultResource = getResourceCache().getResource(defaultRepositoryNameOrId);
        if (defaultResource == null)
        {
            LOG.error("OwCMISNetwork.getDefaultResource: Invalid DefaultObjectStore, current definition is " + defaultRepositoryNameOrId);
            throw new OwConfigurationException(getContext().localize("opencmis.OwCMISNetwork.err.repDef", "Invalid or incorrect default repository definition"));
        }

        return defaultResource;
    }

    public OwCMISSession getDefaultSession() throws OwException
    {
        if (defaultRepositoryNameOrId == null)
        {
            LOG.error("OwCMISNetwork.getDefaultSession: Invalid network state or invalid DefaultObjectStore.");
            throw new OwConfigurationException(getContext().localize("opencmis.OwCMISNetwork.err.repDef", "Invalid or incorrect default repository definition"));
        }

        OwCMISSession defaultSession = getSession(defaultRepositoryNameOrId);
        if (defaultSession == null)
        {
            LOG.error("OwCMISNetwork.getDefaultSession: Invalid DefaultObjectStore.");
            throw new OwConfigurationException(getContext().localize("opencmis.OwCMISNetwork.err.repDef", "Invalid or incorrect default repository definition"));
        }

        return defaultSession;
    }

    public OwCMISSession getSession(String resourceIdOrName_p) throws OwException
    {
        OwCMISSession session = null;

        if (resourceIdOrName_p == null)
        {
            session = getDefaultSession();
        }
        else
        {
            session = getResourceCache().getSession(resourceIdOrName_p);
        }

        if (session == null)
        {
            LOG.warn("OwCMISNetwork.getSession: Could not find resource by Id = " + resourceIdOrName_p);
            throw new OwObjectNotFoundException(getContext().localize1("opencmis.OwCMISNetwork.resourceNotFoundById", "Could not find resource by Id = %1", resourceIdOrName_p));
        }
        else
        {
            return session;
        }

    }

    public OwCMISResource getResource(String resourceIdOrName_p) throws OwException
    {
        OwCMISResource resource = null;

        if (resourceIdOrName_p == null)
        {
            resource = getDefaultResource();
        }
        else
        {
            resource = getResourceCache().getResource(resourceIdOrName_p);
        }

        if (resource == null)
        {
            LOG.warn("OwCMISNetwork.getResource: Could not retrive resource by Id = " + resourceIdOrName_p);
            throw new OwObjectNotFoundException(getContext().localize1("opencmis.OwCMISNetwork.resourceNotFoundById", "Could not find resource by Id = %1", resourceIdOrName_p));
        }
        else
        {
            return resource;
        }
    }

    public Iterator<String> getResourceIDs() throws OwException
    {
        return getResourceCache().getResourceIDs();
    }

    public String getRoleDisplayName(String strRoleName_p) throws OwException
    {
        return this.getAuthenticationProvider().getRoleDisplayName(strRoleName_p);
    }

    public OwUserInfo getUserFromID(String strID_p) throws OwException
    {
        return this.getAuthenticationProvider().getUserFromID(strID_p);
    }

    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws OwException
    {
        return this.getAuthenticationProvider().getUserSelectSubModul(strID_p, types_p);
    }

    public Collection<OwWildCardDefinition> getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws OwException
    {
        switch (iOp_p)
        {
            case OwSearchOperator.CRIT_OP_LIKE:
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
                if (this.likeWildCards == null)
                {
                    this.likeWildCards = new OwCMISLikeWildCardDefinitions(getContext());
                }
                return this.likeWildCards.getWildCardDefinitions();
        }
        return null;
    }

    public boolean hasInterface(String strInterfaceName_p)
    {
        if ("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository".equals(strInterfaceName_p))
        {
            try
            {
                return getNetworkConfiguration() != null && getNetworkConfiguration().getBpmNode() != null;
            }
            catch (OwConfigurationException ex)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Support for Workflow not available, caused by missing configuration");
                }
                return false;
            }
        }
        if (OwInfoProvider.class.getCanonicalName().equals(strInterfaceName_p))
        {
            return true;
        }
        if (OwVIIdResolver.class.getCanonicalName().equals(strInterfaceName_p))
        {
            return true;
        }
        if (OwRestletAuthenticationHandler.class.getCanonicalName().equals(strInterfaceName_p))
        {
            return true;
        }
        return false;
    }

    public void init(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws OwException
    {
        context = context_p;
        config = createConfiguration(networkSettings_p);
        if (this.virtualFolderFactory == null)
        {
            virtualFolderFactory = new OwCMISVirtualFolderFactory(this);
        }
        authenticationInterceptor = getNetworkConfiguration().getAuthenticationInterceptor(this);

        this.setAuthenticationProvider(createAuthProvider());
    }

    protected OwCMISNetworkCfg createConfiguration(OwXMLUtil networkSettings_p)
    {
        return new OwCMISNetworkCfg(networkSettings_p);
    }

    /**(overridable)
     * Create an instance of OwCMISDefaultAuthenticationProvider, which should be used.<br />
     * Throws an configuration exception if the current authentication mode is null, or unsupported.
     * @return OwCMISDefaultAuthenticationProvider
     * @throws OwException
     */
    protected OwCMISDefaultAuthenticationProvider createAuthProvider() throws OwException
    {
        OwAuthenticationConfiguration authConf = getNetworkConfiguration().getAuthenticationConfiguration();

        if (OwAuthenticationConfiguration.LDAP.equals(authConf.getMode()))
        {
            return new OwCMISLDAPAuthenticationProvider(this);
        }
        else if (OwAuthenticationConfiguration.NONE.equals(authConf.getMode()))
        {
            return new OwCMISDefaultAuthenticationProvider(this);
        }
        else if ("ALFRESCO".equals(authConf.getMode()))
        {
            return new OwCMISAlfrescoAuthenticationProvider(this);
        }
        else
        {
            String msg = "Invalid CMIS authentication configuration! Invalid <Authentication> @mode : " + authConf.getMode();
            LOG.error("OwCMISNetwork.createAuthProvider: " + msg);
            throw new OwConfigurationException(getContext().localize("opencmis.OwCMISNetwork.err.invalid.authentication.configuration", "Invalid CMIS authentication configuration!"));

        }
    }

    public void loginDefault(String strUser_p, String strPassword_p) throws OwException, CmisBaseException
    {
        try
        {
            this.getAuthenticationProvider().loginDefault(strUser_p, strPassword_p);
        }
        catch (Exception e)
        {
            logout();
            if (e instanceof CmisBaseException)
            {
                throw (CmisBaseException) e;
            }
            else
            {
                throw new OwServerException("Unknonw error during authentication, verify configuration", e);
            }
        }

        defaultRepositoryNameOrId = getNetworkConfiguration().getDefaultRepository();

        //from here on there should already exist credentials
        parameters = prepareParameters();

        try
        {
            domainRoot = new OwCMISResourceDomainFolder("DOMAIN", getResourceCache().getSessions());
            getContext().onLogin(this.getAuthenticationProvider().getCredentials().getUserInfo());
        }
        catch (OwException owex)
        {
            logout();
            throw owex;
        }
        catch (Exception e)
        {
            LOG.error("Unhandled error was thrown.", e);
            logout();
            throw new OwServerException("Unhandled error", e);
        }
    }

    public void logout() throws OwException
    {
        this.getAuthenticationProvider().logout();

        if (this.defaultRepositoryNameOrId != null)
        {
            this.defaultRepositoryNameOrId = null;
        }

        if (this.resourceCache != null)
        {
            this.resourceCache.clear();
            this.resourceCache = null;
        }

        getAuthenticationContext().setAuthentication(null);
    }

    public OwBatch openBatch() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void refreshStaticClassdescriptions() throws OwException
    {
        // TODO Auto-generated method stub

    }

    public void releaseResources() throws OwException
    {
        if (this.config != null)
        {
            this.config = null;
        }
        if (null != this.m_WorkitemRepository)
        {
            try
            {
                this.m_WorkitemRepository.releaseResources();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setEventManager(OwEventManager eventManager_p)
    {
        this.eventManager = eventManager_p;
    }

    public void setRoleManager(OwRoleManager roleManager_p)
    {
        roleManager = roleManager_p;
    }

    /**
     * Getter for role manager instance.
     * @return OwRoleManager or null if not set yet
     */
    public OwRoleManager getRoleManager()
    {
        return this.roleManager;
    }

    /**
     * Method called to prepare a configuration which will be used to connect to CMIS back-end. 
     * @return Map with connection parameters
     * @throws OwException in case of configuration problems or missing information
     */
    protected Map<String, ?> prepareParameters() throws OwException
    {
        Map<String, ?> opencmisParameters = buildOpenCmisParameters();

        Map<String, String> sessionParametersConfig = getNetworkConfiguration().getSessionParametersConfig();
        Map<String, Object> parameters = new HashMap<String, Object>(sessionParametersConfig);

        parameters.put(OwCMISSessionParameter.OPENCMIS_SESSION_PARAMETERS, opencmisParameters);

        TimeZone clientTimeZone = getContext().getClientTimeZone();
        parameters.put(OwCMISSessionParameter.TIME_ZONE_ID, clientTimeZone.getID());

        parameters.put(OwCMISSessionParameter.CURRENT_USER, getCredentials().getAuthInfo(OwCredentialsConstants.LOGIN_USR));

        //        parameters.put(OwCMISSessionParameter.OBJECT_CLASS_FACTORY_CLASS, OwCMISAlfrescoObjectClassFactory.class.getName());

        return parameters;
    }

    public Map<String, String> buildOpenCmisParameters() throws OwException
    {
        Map<String, String> opencmisParameters = getNetworkConfiguration().getBindingConfig();

        //        opencmisParameters.put(SessionParameter.OBJECT_FACTORY_CLASS, AlfrescoObjectFactoryImpl.class.getName());
        opencmisParameters.put(SessionParameter.USER, getCredentials().getAuthInfo(OwCredentialsConstants.LOGIN_USR));
        opencmisParameters.put(SessionParameter.PASSWORD, getCredentials().getAuthInfo(OwCredentialsConstants.LOGIN_PWD));

        if (!"debugmode".equals(getLocale().toString()))
        {
            opencmisParameters.put(SessionParameter.LOCALE_ISO3166_COUNTRY, getLocale().getCountry());
            opencmisParameters.put(SessionParameter.LOCALE_ISO639_LANGUAGE, getLocale().getLanguage());
            opencmisParameters.put(SessionParameter.LOCALE_VARIANT, getLocale().getVariant());
        }

        return opencmisParameters;
    }

    @Override
    public OwSearchTemplate createSearchTemplate(OwObject obj_p) throws OwException
    {
        try
        {
            return new OwStandardSearchTemplate(getContext(), obj_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not instantiate search template: " + e.getMessage(), e);
            throw new OwServerException(getContext().localize("opencmis.OwCMISNetwork.search.template.error", "Search template error!"), e);
        }
    }

    /**
     * Getter for authentication intercepter, used in LoginUI
     * and Session creation of OpenCMIS framework.
     * @return OwCMISAuthenticationInterceptor or null
     * @throws OwException
     */
    public OwCMISAuthenticationInterceptor getAuthInterceptor() throws OwException
    {
        return authenticationInterceptor;
    }

    /**
     * Get current active authentication context
     * @return OwAuthenticationContext
     */
    public OwAuthenticationContext getAuthenticationContext()
    {
        OwNetworkContext context = getContext();
        if (context instanceof OwAuthenticationContext)
        {
            return (OwAuthenticationContext) context;
        }
        else
        {
            return localAuthenticationContext;
        }
    }

    @Override
    public OwCMISObject getObject(OwVIId objViid) throws OwException
    {
        OwCMISSession ses = getSession(objViid.getResourceId());
        return ses.getObject(objViid);
    }

    /**
     * Getter for current authentication provider.
     * @return OwCMISDefaultAuthenticationProvider
     * @since 4.1.1.1
     */
    public OwCMISDefaultAuthenticationProvider getAuthenticationProvider()
    {
        return authenticationProvider;
    }

    /**
     * Setter of authentication provider.
     * @param authProvider OwCMISDefaultAuthenticationProvider
     * @since 4.1.1.1
     */
    protected void setAuthenticationProvider(OwCMISDefaultAuthenticationProvider authProvider)
    {
        this.authenticationProvider = authProvider;
    }

    @Override
    public OwIterable<OwCMISObject> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        OwCMISCSQLCProcessor processor = factorySearchNodeSQLProcessor();

        OwExternal<List<OwQueryStatement>> statementsEx = processor.createSQLStatements(searchClause, loadContext.getPropertyNames(), loadContext.getSorting());
        List<OwQueryStatement> statements = statementsEx.getInternal();

        if (statements != null)
        {
            List<OwIterable<OwCMISObject>> results = new LinkedList<OwIterable<OwCMISObject>>();
            for (OwQueryStatement statement : statements)
            {
                OwCMISQueryIterable iterable = getSession(statement.getTargetRepositoryID()).query(statement, loadContext);
                results.add(iterable);
            }

            return OwAggregateIterable.forList(results);
        }

        return OwEmptyIterable.instance();

    }

    @Override
    public boolean canPageSearch()
    {
        return true;
    }

    @Override
    public OwObject createVirtualFolder(Node xmlVirtualFolderDescriptionNode_p, String strName_p, String strDmsIDPart_p) throws OwException
    {
        return this.virtualFolderFactory.createVirtualFolder(xmlVirtualFolderDescriptionNode_p, strName_p, strDmsIDPart_p);
    }

}