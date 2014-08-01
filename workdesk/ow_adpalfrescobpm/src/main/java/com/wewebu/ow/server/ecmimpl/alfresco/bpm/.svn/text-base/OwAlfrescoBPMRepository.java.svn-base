package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwCollectionIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ProcessDefinitionsPageFetcher;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstanceEntry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactoryRest;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.util.WorkflowDescriptionNameComparator;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of a BPM Repository based on the Alfresco ProcessREST RESTful API.
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
 *@since 3.2.0.0
 */
@SuppressWarnings("rawtypes")
public class OwAlfrescoBPMRepository implements OwWorkitemRepository<OwAlfrescoBPMWorkItem>
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMRepository.class);
    private static final String BASE_URL = "BaseURL";

    private OwNetwork network;
    private OwXMLUtil config;
    private String baseURL;

    private AlfrescoRESTFulFactory restFulFactory;
    private NativeValueConverterFactory valueConverterFactory;

    // type -> List<OwAlfrescoBPMBaseContainer>
    private Map<Integer, List<OwAlfrescoBPMBaseContainer>> containersByType = new HashMap<Integer, List<OwAlfrescoBPMBaseContainer>>();

    /**
     * This constructor is called through reflection.
     * 
     * @param network_p the initiating {@link OwNetwork} instance.
     * @param bpmConfig_p the BPM configuration XML snippet.
     * @throws OwConfigurationException thrown if there is was configuration exception 
     * preventing us from creating a fully configured instance of this repository. 
     */
    public OwAlfrescoBPMRepository(OwNetwork network_p, OwXMLUtil bpmConfig_p) throws OwConfigurationException
    {
        this.network = network_p;
        this.config = bpmConfig_p;
        this.init();
    }

    private void init() throws OwConfigurationException
    {
        this.baseURL = this.config.getSafeTextValue(BASE_URL, null);
        if (null == this.baseURL)
        {
            throw new OwConfigurationException("Missing " + BASE_URL + " configuration.");
        }
        OwRestletAuthenticationHandler authHandler;
        try
        {
            authHandler = (OwRestletAuthenticationHandler) this.network.getInterface(OwRestletAuthenticationHandler.class.getCanonicalName(), null);
        }
        catch (Exception e1)
        {
            throw new OwConfigurationException("Problem configuring OwAlfrescoBPMRepository", e1);
        }
        if (authHandler == null)
        {
            throw new OwConfigurationException("Unable to retrieve OwRestletAuthenticationHandler for BPM");
        }

        try
        {
            TimeZone clientTimeZone = getNetwork().getContext().getClientTimeZone();

            this.valueConverterFactory = new NativeValueConverterFactoryRest(clientTimeZone);
            this.restFulFactory = new AlfrescoRESTFulFactory(this.baseURL, authHandler, this.network.getContext().getLocale(), this.valueConverterFactory);
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not create a RESTful factory instance.", e);
        }

        initUserQueues();
    }

    private void initUserQueues()
    {

        ArrayList<OwAlfrescoBPMBaseContainer> userQueues = new ArrayList<OwAlfrescoBPMBaseContainer>();
        userQueues.add(new OwAlfrescoBPMInboxContainer(this.network, this));
        userQueues.add(new OwAlfrescoBPMUnassignedContainer(this.network, this));
        this.containersByType.put(OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER, userQueues);

        ArrayList<OwAlfrescoBPMBaseContainer> groupQueues = new ArrayList<OwAlfrescoBPMBaseContainer>();
        this.containersByType.put(OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER, groupQueues);
        try
        {
            Collection myGroups = this.network.getCredentials().getUserInfo().getGroups();
            for (Object object : myGroups)
            {
                OwUserInfo groupInfo = (OwUserInfo) object;
                //String groupName = groupInfo.getUserName();
                groupQueues.add(new OwAlfrescoBPMGroupQueueContainer(this.network, this, groupInfo));
            }
        }
        catch (Exception e)
        {
            // just log it and go on
            LOG.error("Could not initialise Group Containers.", e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#refreshStaticClassdescriptions()
     */
    public void refreshStaticClassdescriptions() throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canRefreshStaticClassdescriptions()
     */
    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#doSearch(com.wewebu.ow.server.field.OwSearchNode, com.wewebu.ow.server.field.OwSort, java.util.Collection, int, int)
     */
    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        // TODO Auto-generated method stub
        throw new OwNotSupportedException("The Alfresco BPM repository does not suppport PageSearch.");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectFromDMSID(java.lang.String, boolean)
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        OwAlfrescoBPMDMSID alfrescoBPMID = new OwAlfrescoBPMDMSID(strDMSID_p);
        String nativeId = alfrescoBPMID.getId();
        TaskInstanceResource res = this.getRestFulFactory().taskInstanceResource(nativeId, null);
        OwAlfrescoBPMWorkItem workItem = new RestCallTemplate<TaskInstanceResource, OwAlfrescoBPMWorkItem>() {

            @Override
            protected OwAlfrescoBPMWorkItem execWith(TaskInstanceResource resource) throws OwException
            {
                TaskInstanceEntry task = resource.get();
                OwAlfrescoBPMWorkItem result = new OwAlfrescoBPMWorkItem(task.getEntry(), null, OwAlfrescoBPMRepository.this);
                return result;
            }
        }.doCall(res);

        return workItem;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectFromPath(java.lang.String, boolean)
     */
    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectClass(java.lang.String, com.wewebu.ow.server.ecm.OwResource)
     */
    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectClassNames(int[], boolean, boolean, com.wewebu.ow.server.ecm.OwResource)
     */
    public Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getResource(java.lang.String)
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getResourceIDs()
     */
    public Iterator getResourceIDs() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getEventManager()
     */
    public OwEventManager getEventManager()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getDMSPrefix()
     */
    public String getDMSPrefix()
    {
        return OwAlfrescoBPMDMSID.DMSID_PREFIX;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#releaseResources()
     */
    public void releaseResources() throws Exception
    {
        getRestFulFactory().release();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canBatch()
     */
    public boolean canBatch()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#closeBatch(com.wewebu.ow.server.ecm.OwBatch)
     */
    public void closeBatch(OwBatch batch_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldDefinitionProvider#getFieldDefinition(java.lang.String, java.lang.String)
     */
    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldDefinitionProvider#getWildCardDefinitions(java.lang.String, java.lang.String, int)
     */
    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#getWorkitemContainerIDs(boolean, int)
     */
    @SuppressWarnings("rawtypes")
    public Collection getWorkitemContainerIDs(boolean fRefresh_p, int iType_p) throws Exception
    {
        Collection<String> ids = new ArrayList<String>();
        List<OwAlfrescoBPMBaseContainer> containers = this.containersByType.get(iType_p);
        if (null != containers)
        {
            for (OwAlfrescoBPMBaseContainer container : containers)
            {
                ids.add(container.getID());
            }
        }
        else
        {
            LOG.warn("Unknown type definition provided. Will return empty Collection, unknown value = " + iType_p);
        }

        return ids;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#getWorkitemContainer(java.lang.String, int)
     */
    public OwWorkitemContainer getWorkitemContainer(String sID_p, int iType_p) throws Exception
    {
        List<OwAlfrescoBPMBaseContainer> containers = this.containersByType.get(iType_p);
        for (OwAlfrescoBPMBaseContainer container : containers)
        {
            if (sID_p.equals(container.getID()))
            {
                return container;
            }
        }

        String msg = "Object not found, Type = " + iType_p + ", ID = " + sID_p;
        LOG.error(msg);
        throw new OwObjectNotFoundException(msg);
    }

    public String getWorkitemContainerName(String sID_p, int iType_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasContainer(int iType_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public OwProxyInfo createProxy() throws Exception, OwNotSupportedException
    {
        throw new OwNotSupportedException("Proxying is not supported with the Alfresco BPM adapter.");
    }

    @SuppressWarnings("rawtypes")
    public void setProxies(Collection proxies_p, String absentpersonID_p) throws Exception, OwNotSupportedException
    {
        throw new OwNotSupportedException("Proxying is not supported with the Alfresco BPM adapter.");
    }

    @SuppressWarnings("rawtypes")
    public Collection getProxies(String absentpersonID_p) throws Exception, OwNotSupportedException
    {
        throw new OwNotSupportedException("Proxying is not supported with the Alfresco BPM adapter.");
    }

    public boolean canProxy()
    {
        return false;
    }

    @SuppressWarnings({ "rawtypes" })
    public Collection getLaunchableWorkflowDescriptions(Collection attachmentobjects_p) throws Exception
    {
        //There are no Subscriptions in Alfresco BPM, so we always return all workflow descriptions
        OwCollectionIterable<OwAlfrescoBPMWorkflowDescription> definitions = getDefaultWorkflowDescriptions();

        // Only keep the latest version and remove any duplicates.
        Map<String, OwAlfrescoBPMWorkflowDescription> definitionsByKey = new HashMap<String, OwAlfrescoBPMWorkflowDescription>();
        for (OwAlfrescoBPMWorkflowDescription definition : definitions)
        {
            String key = definition.getKey();
            OwAlfrescoBPMWorkflowDescription existingDef = definitionsByKey.get(key);
            if (null == existingDef)
            {
                definitionsByKey.put(key, definition);
            }
            else if (existingDef.getVersion() < definition.getVersion())
            {
                // We only keep the newest version
                definitionsByKey.put(key, definition);
            }
        }

        List<OwAlfrescoBPMWorkflowDescription> result = new ArrayList<OwAlfrescoBPMWorkflowDescription>(definitionsByKey.values());
        java.util.Collections.sort(result, new WorkflowDescriptionNameComparator());
        return result;
    }

    private OwCollectionIterable<OwAlfrescoBPMWorkflowDescription> getDefaultWorkflowDescriptions() throws OwException
    {
        OwAbstractPageFetcher<OwAlfrescoBPMWorkflowDescription> fetcher = new ProcessDefinitionsPageFetcher(this);
        OwCollectionIterable<OwAlfrescoBPMWorkflowDescription> result = new OwCollectionIterable<OwAlfrescoBPMWorkflowDescription>(fetcher);
        return result;
    }

    @SuppressWarnings("rawtypes")
    public OwWorkitem createLaunchableItem(OwWorkflowDescription workflowDescription_p, Collection attachmentobjects_p) throws Exception
    {
        OwAlfrescoBPMLaunchableWorkitemContainer workitemContainer = new OwAlfrescoBPMLaunchableWorkitemContainer(this.network, this);
        OwAlfrescoBPMWorkflowDescription alfrescoWFDescription = (OwAlfrescoBPMWorkflowDescription) workflowDescription_p;
        //        alfrescoWFDescription.load();
        return workitemContainer.createLaunchableWorkItem(alfrescoWFDescription, attachmentobjects_p);
    }

    public boolean canLaunch()
    {
        return true;
    }

    /**
     * @return The base URL of the server we are connecting to.
     */
    public String getBaseURL()
    {
        return this.baseURL;
    }

    public OwNetworkContext getContext()
    {
        return this.network.getContext();
    }

    public AlfrescoRESTFulFactory getRestFulFactory()
    {
        return restFulFactory;
    }

    public OwNetwork getNetwork()
    {
        return network;
    }

    public NativeValueConverterFactory getValueConverterFactory()
    {
        return valueConverterFactory;
    }

    @Override
    public OwIterable<OwAlfrescoBPMWorkItem> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : Alfresco BPM repository page search
        throw new OwNotSupportedException("The Alfresco BPM repository does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }
}
