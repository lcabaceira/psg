package com.wewebu.ow.server.ecmimpl.opencmis.cross;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.csqlc.ast.OwExternal;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ao.OwAOSupport;
import com.wewebu.ow.server.ao.OwFileAOSupport;
import com.wewebu.ow.server.ao.OwVirtualFolderFactory;
import com.wewebu.ow.server.ao.OwVirtualFoldersManager;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwCrossMappings;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardCrossMappings;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVirtualFolderObjectFactory;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISAuthenticatedNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISExternalEntitiesResolver;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISVirtualFolderFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISAuthenticationInterceptor;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISSearchResult;
import com.wewebu.ow.server.ecmimpl.opencmis.ui.OwCMISUILoginModule;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * The implementation of the <code>{@link OwNetwork}</code> interface 
 * for the CMIS cross adaptor scenarios. 
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
 *@since 4.1.1.0
 */
public class OwCMISCrossNetwork implements OwCMISAuthenticatedNetwork, OwCMISExternalEntitiesResolver, OwVirtualFolderFactory
{
    private static final Logger LOG = OwLog.getLogger(OwCMISCrossNetwork.class);

    private OwCMISNetwork internalNetwork;
    private OwNetwork externalNetwork;

    private List<String> internalResourceIDs = new LinkedList<String>();
    private List<String> externalResourceIDs = new LinkedList<String>();
    private List<String> crossResourceIDs = new LinkedList<String>();

    private OwXMLUtil externalConfiguration;

    private OwCMISNetworkCfg crossConfiguration;

    private OwRoleManager roleManager;

    private OwStandardCrossMappings xMapping;

    private OwNetworkContext context;

    private OwObject domainFolder;

    private OwVirtualFoldersManager vfManager;

    public OwNetwork getNetwork(OwResource resource_p) throws Exception
    {
        if (resource_p == null)
        {
            return this.internalNetwork;
        }
        String rID = resource_p.getID();
        return getNetwork(rID);
    }

    private String removeResourcePrefix(OwNetwork network_p, String id_p)
    {
        if (id_p == null)
        {
            return null;
        }
        else
        {
            String prefix = network_p.getDMSPrefix() + "/";
            if (id_p.startsWith(prefix))
            {
                id_p = id_p.substring(prefix.length());
            }

            return id_p;
        }
    }

    public OwNetwork getNetwork(String resourceID_p) throws OwException
    {

        if (resourceID_p == null)
        {
            return this.internalNetwork;
        }

        if (this.internalResourceIDs.contains(resourceID_p))
        {
            return this.internalNetwork;
        }
        else if (this.externalNetwork != null && this.externalResourceIDs.contains(resourceID_p))
        {
            return this.externalNetwork;
        }
        else
        {
            OwResource resource = null;
            try
            {
                resource = this.internalNetwork.getResource(resourceID_p);
                if (resource != null)
                {
                    return this.internalNetwork;
                }
            }
            catch (OwObjectNotFoundException internalException)
            {
                try
                {
                    resourceID_p = removeResourcePrefix(this.externalNetwork, resourceID_p);

                    resource = this.externalNetwork.getResource(resourceID_p);
                    if (resource != null)
                    {
                        return this.externalNetwork;
                    }
                }
                catch (Exception externalException)
                {
                    LOG.error("Unknown internal resource " + resourceID_p, internalException);
                    LOG.error("Unknown external resource " + resourceID_p, externalException);
                    throw new OwObjectNotFoundException("Unknown resource " + resourceID_p, internalException);
                }
            }

            LOG.error("OwCMISCrossNetwork.getNetwork(): Unknown resource " + resourceID_p);
            throw new OwObjectNotFoundException("Unknown resource " + resourceID_p);
        }
    }

    public boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int context_p) throws Exception
    {
        return getNetwork(resource_p).canCreateNewObject(resource_p, parent_p, context_p);
    }

    public boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int context_p) throws Exception
    {
        if (parent_p.getResource() == null)
        {
            return true;
        }
        else
        {
            String rID = parent_p.getResourceID();
            return getNetwork(rID).canCreateObjectCopy(parent_p, childTypes_p, context_p);
        }
    }

    public boolean canDo(OwObject obj_p, int functionCode_p, int context_p) throws Exception
    {
        String rID = obj_p.getResourceID();
        return getNetwork(rID).canDo(obj_p, functionCode_p, context_p);
    }

    public boolean canEditAccessRights(OwObject object_p) throws Exception
    {
        String rID = object_p.getResourceID();
        return getNetwork(rID).canEditAccessRights(object_p);
    }

    public String createNewObject(final OwResource resource_p, final String strObjectClassName_p, final OwPropertyCollection properties_p, final OwPermissionCollection permissions_p, final OwContentCollection content_p, final OwObject parent_p,
            final String strMimeType_p, final String strMimeParameter_p) throws Exception
    {
        OwNetwork network = getNetwork(resource_p);
        OwResource defaultResource = getResource(null);
        if (resource_p.getID().equals(defaultResource.getID()))
        {
            return network.createNewObject(resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p);
        }
        else
        {
            final OwNetwork fnetwork = OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), network, resource_p.getID());
            return (String) OwCMISCrossInvocationHandler.call(new OwCMISCrossInvocationHandler.CrossCall() {

                public Object call() throws Exception
                {

                    return fnetwork.createNewObject(resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p);
                }
            }, defaultResource.getID());

        }
    }

    public String createNewObject(final boolean fPromote_p, final Object mode_p, final OwResource resource_p, final String strObjectClassName_p, final OwPropertyCollection properties_p, final OwPermissionCollection permissions_p,
            final OwContentCollection content_p, final OwObject parent_p, final String strMimeType_p, final String strMimeParameter_p) throws Exception
    {
        OwNetwork network = getNetwork(resource_p);
        OwResource defaultResource = getResource(null);
        if (resource_p.getID().equals(defaultResource.getID()))
        {
            return network.createNewObject(fPromote_p, mode_p, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p);
        }
        else
        {
            final OwNetwork fnetwork = OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), network, resource_p.getID());
            return (String) OwCMISCrossInvocationHandler.call(new OwCMISCrossInvocationHandler.CrossCall() {

                public Object call() throws Exception
                {

                    return fnetwork.createNewObject(fPromote_p, mode_p, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p);
                }
            }, defaultResource.getID());

        }
    }

    public String createNewObject(boolean promote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean keepCheckedOut_p) throws Exception
    {
        OwNetwork network = getNetwork(resource_p);
        return network.createNewObject(promote_p, mode_p, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, keepCheckedOut_p);
    }

    public String createObjectCopy(OwObject obj_p, final OwPropertyCollection properties_p, final OwPermissionCollection permissions_p, final OwObject parent_p, final int[] childTypes_p) throws Exception
    {
        OwNetwork network = getNetwork(parent_p.getResourceID());

        if (!obj_p.getResourceID().equals(parent_p.getResourceID()))
        {
            final OwObject fobj = OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), obj_p, obj_p.getResourceID());
            final OwNetwork fnetwork = OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), network, parent_p.getResourceID());
            return (String) OwCMISCrossInvocationHandler.call(new OwCMISCrossInvocationHandler.CrossCall() {

                public Object call() throws Exception
                {

                    return fnetwork.createObjectCopy(fobj, properties_p, permissions_p, parent_p, childTypes_p);
                }
            }, obj_p.getResourceID());

        }
        else
        {
            return network.createObjectCopy(obj_p, properties_p, permissions_p, parent_p, childTypes_p);
        }

    }

    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwResource resource_p) throws Exception
    {
        OwNetwork network = getNetwork(resource_p);
        return network.createObjectSkeleton(objectclass_p, resource_p);
    }

    public Object getApplicationObject(int typ_p, String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws Exception
    {
        if (typ_p == APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER)
        {
            return getVFManager().getApplicationObject(strName_p, param_p, forceUserSpecificObject_p, createIfNotExist_p);
        }
        else
        {
            return this.internalNetwork.getApplicationObject(typ_p, strName_p, param_p, forceUserSpecificObject_p, createIfNotExist_p);
        }
    }

    public Object getApplicationObject(int typ_p, String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws Exception
    {
        if (typ_p == APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER)
        {
            return getVFManager().getApplicationObject(strName_p, forceUserSpecificObject_p, createIfNotExist_p);
        }
        else
        {
            return this.internalNetwork.getApplicationObject(typ_p, strName_p, forceUserSpecificObject_p, createIfNotExist_p);
        }
    }

    public Collection getApplicationObjects(int typ_p, String strName_p, boolean forceUserSpecificObject_p) throws Exception
    {
        return this.internalNetwork.getApplicationObjects(typ_p, strName_p, forceUserSpecificObject_p);
    }

    public OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws Exception
    {
        String rID = object_p.getResourceID();
        return getNetwork(rID).getEditAccessRightsSubModul(object_p);
    }

    public Object getInterface(String strInterfaceName_p, Object object_p) throws Exception
    {
        return this.internalNetwork.getInterface(strInterfaceName_p, object_p);
    }

    public OwCMISNetwork getInternalNetwork()
    {
        return internalNetwork;
    }

    public OwNetwork getExternalNetwork()
    {
        return externalNetwork;
    }

    public Locale getLocale()
    {
        return this.internalNetwork.getLocale();
    }

    public boolean hasInterface(String strInterfaceName_p)
    {
        return this.internalNetwork.hasInterface(strInterfaceName_p);
    }

    public void init(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws Exception
    {
        this.crossConfiguration = new OwCMISNetworkCfg(networkSettings_p);
        this.context = context_p;

        this.internalNetwork = new OwCMISNetwork(this, new OwCMISVirtualFolderFactory(this));
        this.internalNetwork.init(context_p, networkSettings_p);

        OwXMLUtil externalConfiguration = networkSettings_p.getSubUtil("ExternalCrossAdapter");

        if (externalConfiguration != null)
        {
            externalInit(context_p, externalConfiguration);
        }
    }

    protected void externalInit(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws OwException
    {
        this.externalConfiguration = networkSettings_p;
        OwXMLUtil adapterConfiguration = null;
        try
        {
            adapterConfiguration = networkSettings_p.getSubUtil("XEcmAdapter");
        }
        catch (Exception e)
        {
            LOG.error("OwCMISCrossNetwork.externalInit(): The external ECM Adapter could not be loaded.", e);
            throw new OwConfigurationException(new OwString("opencmis.cross.OwCMISCrossNetwork.err.read.xecmadapter", "Could not read CMIS cross configuration!"), e);
        }

        if (adapterConfiguration == null)
        {
            LOG.error("OwCMISCrossNetwork.externalInit(): The external ECM Adapter configuration could not be found.");
            throw new OwConfigurationException(new OwString("opencmis.cross.OwCMISCrossNetwork.err.invalid.conf", "Missing CMIS cross configuration!"));
        }
        // === create single instance for network object (DMS adaptor)
        String strNetworkClassName = "";
        try
        {
            strNetworkClassName = adapterConfiguration.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_CLASSNAME, null);
            Class NetworkClass = Class.forName(strNetworkClassName);
            this.externalNetwork = (OwNetwork) NetworkClass.newInstance();
            this.externalNetwork.init(context_p, adapterConfiguration);

            this.externalNetwork.setEventManager(this.getEventManager());
            this.externalNetwork.setRoleManager(this.roleManager);
        }
        catch (Exception e)
        {
            LOG.error("OwCMISCrossNetwork.externalInit(): The external ECM Adapter could not be loaded: " + strNetworkClassName, e);
            throw new OwConfigurationException(new OwString("opencmis.cross.OwCMISCrossNetwork.err.externalClass", "Invalid CMIS NetworkClass configuration!"), e);
        }
    }

    public void setEventManager(OwEventManager eventManager_p)
    {
        this.internalNetwork.setEventManager(eventManager_p);
        if (externalNetwork != null)
        {
            this.externalNetwork.setEventManager(eventManager_p);
        }
    }

    public void setRoleManager(OwRoleManager roleManager_p)
    {
        this.roleManager = roleManager_p;
        this.internalNetwork.setRoleManager(roleManager_p);
        if (this.externalNetwork != null)
        {
            this.externalNetwork.setRoleManager(roleManager_p);
        }
    }

    public boolean canBatch()
    {
        return this.internalNetwork.canBatch();
    }

    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return this.internalNetwork.canRefreshStaticClassdescriptions();
    }

    public void closeBatch(OwBatch batch_p) throws OwException
    {
        this.internalNetwork.closeBatch(batch_p);
    }

    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int maxSize_p, int versionSelection_p) throws Exception
    {
        OwCMISSearchResult result = this.internalNetwork.doCMISSearch(searchCriteria_p, sortCriteria_p, propertyNames_p, maxSize_p, versionSelection_p);
        OwExternal<List<OwQueryStatement>> exStatements = result.getExternalStatements();
        if (exStatements.hasExternalObjectStores())
        {
            OwObjectCollection crossCollection = new OwStandardObjectCollection();

            List<String[]> externalObjectStores = exStatements.getExternalObjectStores();
            for (String[] xStore : externalObjectStores)
            {
                OwObjectCollection externalResults = doExternalRepositorySearch(xStore[0], searchCriteria_p, sortCriteria_p, propertyNames_p, maxSize_p, versionSelection_p);
                crossCollection.addAll(result.getCmisSearchResult());
                crossCollection.addAll(externalResults);
            }

            return crossCollection;
        }
        else
        {
            return result.getCmisSearchResult();
        }
    }

    /**
     * Delegation method to execute the search on secondary network (external).
     * @param xStore_p OwSearchNode search to execute
     * @param sort_p OwSort which type of sorting should be provided
     * @param propertyNames_p Collection of property names to retrieve
     * @param maxSize_p int maximum size of object to retrieve
     * @param versionSelection_p int which type of version selection should be executed
     * @return OwObjectCollection with corresponding results
     * @throws Exception if CrossMapping fails or external search cannot be executed
     */
    protected OwObjectCollection doExternalRepositorySearch(String xStore_p, OwSearchNode searchNode_p, OwSort sort_p, Collection propertyNames_p, int maxSize_p, int versionSelection_p) throws Exception
    {
        OwCrossMappings xMapping = getXMapping();
        OwSearchNode xSearchNode = xMapping.getXSearch(searchNode_p, xStore_p, this.externalNetwork);
        OwSort xSort = xMapping.getXSort(sort_p);
        Collection xProperties = xMapping.getXProperties(propertyNames_p);

        OwObjectCollection result = this.externalNetwork.doSearch(xSearchNode, xSort, xProperties, maxSize_p, versionSelection_p);

        // wrap the returned objects so properties can be mapped
        //        xMapping.mapXObjectCollection(result);

        for (int i = 0; i < result.size(); i++)
        {
            OwObject obj = (OwObject) result.get(i);

            OwObject crossObject = OwCMISCrossInvocationHandler.createCrossNetworkObject(this, xMapping, obj, obj.getResourceID());

            result.set(i, crossObject);
        }

        return result;
    }

    public synchronized OwCrossMappings getXMapping() throws OwException
    {
        if (null == this.xMapping)
        {
            OwXMLUtil xconfig = null;

            try
            {
                xconfig = externalConfiguration.getSubUtil("XMapping");
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwConfigurationException(getContext().localize("opencmis.cross.OwCMISCrossNetwork.err.read.xmapping", "Could not read XMapping."), e);
            }
            if (xconfig == null)
            {
                LOG.error("OwCMISCrossNetwork.getXMapping: The external ECM Adapter could not be loaded (is null), please define XMapping.");
                throw new OwConfigurationException(getContext().localize("opencmis.cross.OwCMISCrossNetwork.err.no.xmapping", "The external ECM adapter could not be loaded, please define XMapping."));
            }

            this.xMapping = new OwStandardCrossMappings(xconfig);
        }

        return this.xMapping;
    }

    public String getDMSPrefix()
    {
        return this.internalNetwork.getDMSPrefix();
    }

    public OwEventManager getEventManager()
    {
        return this.internalNetwork.getEventManager();
    }

    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        OwNetwork network = getNetwork(resource_p);
        OwObjectClass clazz = network.getObjectClass(strClassName_p, resource_p);
        return OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), clazz, resource_p.getID());
    }

    public Map getObjectClassNames(int[] types_p, boolean excludeHiddenAndNonInstantiable_p, boolean rootOnly_p, OwResource resource_p) throws Exception
    {
        return getNetwork(resource_p).getObjectClassNames(types_p, excludeHiddenAndNonInstantiable_p, rootOnly_p, resource_p);
    }

    public OwObject getObjectFromDMSID(String strDMSID_p, boolean refresh_p) throws Exception
    {
        OwCMISDMSIDDecoder decoder = this.internalNetwork.getNetworkConfiguration().getDMSIDDecoder();
        OwException cmisDecodeException = null;
        OwCMISDMSID dmsid = null;
        try
        {
            dmsid = decoder.createDMSID(strDMSID_p);
            String rID = dmsid.getResourceID();
            if (this.internalResourceIDs.contains(rID))
            {
                OwObject iObject = this.internalNetwork.getObjectFromDMSID(strDMSID_p, refresh_p);
                return OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), iObject, rID);
            }
        }
        catch (OwException e)
        {
            if (dmsid != null)
            {
                throw e;
            }
            else
            {
                cmisDecodeException = e;
            }
        }

        if (this.externalNetwork != null)
        {
            OwObject exObject = this.externalNetwork.getObjectFromDMSID(strDMSID_p, refresh_p);
            return OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), exObject, exObject.getResourceID());
        }
        else
        {
            if (cmisDecodeException != null)
            {
                throw cmisDecodeException;
            }
            else
            {
                LOG.error("OwCMISCrossNetwork.getObjectFromDMSID() : Invalid DMSID " + strDMSID_p);
                throw new OwInvalidOperationException(new OwString1("opencmis.cross.OwCMISCrossNetwork.err.invalid.dmsid", "Invalid DMSID %1 .", strDMSID_p));
            }
        }
    }

    public OwObject getObjectFromPath(String strPath_p, boolean refresh_p) throws Exception
    {
        if (strPath_p.equals(OwObject.STANDARD_PATH_DELIMITER))
        {
            return this.domainFolder;
        }
        else
        {
            OwObject iObject = this.internalNetwork.getObjectFromPath(strPath_p, refresh_p);
            return OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), iObject, iObject.getResourceID());
        }
    }

    public OwResource getResource(String strID_p) throws Exception
    {

        OwNetwork network = getNetwork(strID_p);
        return network.getResource(removeResourcePrefix(network, strID_p));
    }

    public Iterator getResourceIDs() throws Exception
    {
        return crossResourceIDs.iterator();
    }

    public OwBatch openBatch() throws OwException
    {
        return this.internalNetwork.openBatch();
    }

    public void refreshStaticClassdescriptions() throws Exception
    {
        this.internalNetwork.refreshStaticClassdescriptions();
    }

    public void releaseResources() throws Exception
    {
        this.internalNetwork.releaseResources();
    }

    private String toExternalField(String strFieldDefinitionName_p) throws OwException
    {
        OwCrossMappings mapping = getXMapping();
        return mapping.getXProperty(strFieldDefinitionName_p);
    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        OwNetwork network = getNetwork(strResourceName_p);
        //        if (network == externalNetwork)
        //        {
        //            strFieldDefinitionName_p = toExternalField(strFieldDefinitionName_p);
        //        }
        OwFieldDefinition definition = network.getFieldDefinition(strFieldDefinitionName_p, removeResourcePrefix(network, strResourceName_p));
        return definition;
        //        return OwCMISCrossInvocationHandler.createCrossNetworkObject(this, getXMapping(), definition,strResourceName_p);
    }

    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int op_p) throws Exception
    {
        OwNetwork network = getNetwork(strResourceName_p);
        if (network == externalNetwork)
        {
            strFieldDefinitionName_p = toExternalField(strFieldDefinitionName_p);
        }
        return network.getWildCardDefinitions(strFieldDefinitionName_p, removeResourcePrefix(network, strResourceName_p), op_p);
    }

    public boolean canUserSelect() throws Exception
    {
        return this.internalNetwork.canUserSelect();
    }

    public OwCredentials getCredentials() throws Exception
    {
        return this.internalNetwork.getCredentials();
    }

    public OwUILoginModul<?> getLoginSubModul() throws Exception
    {
        OwCMISUILoginModule<OwCMISCrossNetwork> loginSubModul = new OwCMISUILoginModule<OwCMISCrossNetwork>();
        loginSubModul.init(this);

        return loginSubModul;
    }

    public String getRoleDisplayName(String strRoleName_p) throws Exception
    {
        return this.internalNetwork.getRoleDisplayName(strRoleName_p);
    }

    public OwUserInfo getUserFromID(String strID_p) throws Exception
    {
        return this.internalNetwork.getUserFromID(strID_p);
    }

    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws Exception
    {
        return this.internalNetwork.getUserSelectSubModul(strID_p, types_p);
    }

    public void loginDefault(String user_p, String password_p) throws Exception
    {
        this.internalNetwork.loginDefault(user_p, password_p);

        if (this.externalNetwork != null)
        {
            // also log into the external network
            try
            {
                getXMapping().doXLogin(this.externalNetwork, this, user_p, password_p);
            }
            catch (Exception e)
            {
                String msg = "Your X Login information is invalid or the X Server is not available. Please make sure that name and password are correct and the X Server is running.";
                LOG.debug(msg, e);
                throw new OwAuthenticationException(msg, e);
            }

            Iterator xResources = this.externalNetwork.getResourceIDs();
            while (xResources.hasNext())
            {
                String xResourceID = (String) xResources.next();
                this.externalResourceIDs.add(xResourceID);
                OwResource xResource = this.externalNetwork.getResource(xResourceID);
            }

        }

        Iterator<String> iResources = this.internalNetwork.getResourceIDs();
        while (iResources.hasNext())
        {
            String iResource = iResources.next();
            this.internalResourceIDs.add(iResource);
        }
        this.crossResourceIDs.addAll(this.internalResourceIDs);
        this.crossResourceIDs.addAll(this.externalResourceIDs);

        this.domainFolder = createDomainFolder();
    }

    protected OwObject createDomainFolder() throws OwException
    {
        // create domain root
        if (this.externalNetwork != null)
        {
            return this.domainFolder = new OwCMISCrossDomainFolder(this);
        }
        else
        {
            return this.internalNetwork.getObjectFromPath("/", true);
        }
    }

    public void logout() throws Exception
    {
        this.internalNetwork.logout();
        if (this.externalNetwork != null)
        {
            this.externalNetwork.logout();
        }

    }

    public String resolveRepositoryID(String repositoryName_p) throws OwException
    {
        OwNetwork network = getNetwork(repositoryName_p);
        try
        {
            OwResource resource = network.getResource(removeResourcePrefix(network, repositoryName_p));
            return resource.getID();
        }
        catch (Exception e)
        {
            LOG.error("OwCMISCrossNetwork.resolveRepositoryID():");
            throw new OwObjectNotFoundException(getContext().localize1("opencmis.cross.OwCMISCrossNetwork.err.resolveRepositoryID", "Could not find resource %1 !", repositoryName_p), e);
        }
    }

    public OwNetworkContext getContext()
    {
        return this.context;
    }

    /**
     * Implementation of  {@link OwVirtualFolderFactory} to create the OwObject representation.
     * @param xmlVirtualFolderDescriptionNode_p
     * @param strName_p
     * @param strDmsIDPart_p
     * @return a virtual folder object
     * @throws OwException
     * @since 4.0.0.0
     */
    public OwObject createVirtualFolder(Node xmlVirtualFolderDescriptionNode_p, String strName_p, String strDmsIDPart_p) throws OwException
    {
        // read the classname to instantiate from XML, default is OwStandardVirtualFolderObject
        OwXMLUtil description;
        try
        {
            description = new OwStandardXMLUtil(xmlVirtualFolderDescriptionNode_p);
            String strVirtualFolderClassName = description.getSafeTextValue(OwVirtualFolderObjectFactory.CLASSNAME_TAG_NAME, "com.wewebu.ow.server.ecmimpl.opencmis.OwCMISVirtualFolderFactory");
            Class<?> virtualFolderClass = Class.forName(strVirtualFolderClassName);

            OwVirtualFolderObjectFactory retObject = (OwVirtualFolderObjectFactory) virtualFolderClass.newInstance();

            Node rootNode = description.getSubNode(OwVirtualFolderObjectFactory.ROOT_NODE_TAG_NAME);
            //            OwCMISDMSIDDecoder dmisdDecoder = getDMSIDDecoder();
            //            String dmisdPrefix = dmisdDecoder.getDMSIDPrefix();
            final String virtualFolderPrefix = "vf";
            retObject.init(getContext(), this, getInternalNetwork().getDMSPrefix() + "," + virtualFolderPrefix + "," + strName_p, strName_p, rootNode);

            return retObject.getInstance(strDmsIDPart_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISCrossNetwork.createVirtualFolder():Could not create virtual folder named " + strName_p + " and dmisd part " + strDmsIDPart_p);
            throw new OwInvalidOperationException(getContext().localize("opencmis.cross.OwCMISCrossNetwork.err.createVirtualFolder", "Could not create virtual folder!"), e);
        }
    }

    /**
     * Get the virtual folder manager, which will be created if not existent.
     * @return OwVirtualFoldersManager
     * @throws OwException
     * @since 4.0.0.0 
     */
    protected OwVirtualFoldersManager getVFManager() throws OwException
    {
        if (this.vfManager == null)
        {
            OwCMISNetwork internalNetowrk = getInternalNetwork();
            OwCMISNetworkCfg configuration = internalNetowrk.getNetworkConfiguration();

            OwAOSupport fileAOSupport = new OwFileAOSupport(this, configuration, "WEB-INF/cmis");

            this.vfManager = new OwVirtualFoldersManager(fileAOSupport, this, this.roleManager, configuration.getVirtualFoldersContainer(null));
        }
        return this.vfManager;
    }

    @Override
    public OwCMISAuthenticationInterceptor getAuthInterceptor() throws OwException
    {
        return this.internalNetwork.getAuthInterceptor();
    }

    @Override
    public OwRoleManager getRoleManager()
    {

        return this.roleManager;
    }

    @Override
    public OwIterable<OwCMISObject> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : Cross Open CMIS page search
        throw new OwNotSupportedException("The Open CMIS cross-adapter does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }

}
