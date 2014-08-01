/**
 * 
 */
package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.core.WorkflowDefinition;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwRepositoryContext.OwConfigChangeEventListener;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectReference;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5IndependentObject;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWAttachmentType;
import filenet.vw.api.VWException;
import filenet.vw.api.VWFieldDefinition;
import filenet.vw.api.VWLibraryType;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWTransferResult;
import filenet.vw.api.VWWorkflowDefinition;

/**
 *<p>
 * FileNet P8 5.0 BPM Plugin. Repository for BPM Objects Wrapper.<br/>
 * A single workitem.
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
public class OwFNBPM5Repository implements OwWorkitemRepository<OwFNBPM5WorkItem>, OwConfigChangeEventListener
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5Repository.class);

    /** prefix for DMSID */
    public static final String DMS_PREFIX = "fnbpm5";

    /** the FileNet BPM session */
    private VWSession m_vwSession;

    /** a reference to the network */
    private OwFNCM5Network m_network;

    private String m_strConnectionPointName;

    /** map of all virtual views */
    protected Map<String, OwWorkitemContainer> m_viewqueues;

    /** set of properties that act as external attachments other than P8 */
    private Set m_externalattachmentproperties = new HashSet();
    private Set m_initialExtAttachmentProperties = new HashSet();

    /** configuration node */
    private OwXMLUtil m_confignode;

    /** container names that can be reassigned to */
    private Collection m_reassigncontainernames;

    /** set of ,items that have been locked by this session */
    private Set<VWStepElement> m_locked = new HashSet<VWStepElement>();

    /** Workflow definition cache */
    private HashMap<String, OwObjectClass> classHashMap = new HashMap<String, OwObjectClass>();

    /** the one and only proxy queue */
    protected OwFNBPM5ProxyContainer m_proxyqueue;

    /** the proxy store for proxy information */
    private OwFNBPM5ProxyStore m_proxystore;

    private static final String[] HIDDEN_QUEUE_NAMES = new String[] { "CE_Operations", "Conductor" };;

    public OwFNBPM5Repository(OwFNCM5Network network_p, OwXMLUtil confignode_p) throws Exception
    {
        m_confignode = confignode_p;
        m_network = network_p;

        m_strConnectionPointName = m_confignode.getSafeTextValue("ConnectionPointName", null);
        if (null == m_strConnectionPointName)
        {
            throw new OwInvalidOperationException(getContext().localize("ecmimpl.fncm.invalidConnectionPointName", "Invalid connection point name."));
        }

        // subscribe to configuration changes notification's
        getContext().addConfigChangeEventListener(this);

        // load external attachment configuration
        List properties = m_confignode.getSafeNodeList("ExternalAttachmentProperties");
        for (Iterator i = properties.iterator(); i.hasNext();)
        {
            Node propertyNode = (Node) i.next();
            OwXMLUtil propertyUtil = new OwStandardXMLUtil(propertyNode);
            if ("property".equals(propertyNode.getNodeName()))
            {
                String propertyName = propertyUtil.getSafeTextValue("");
                if (propertyName.length() > 0)
                {
                    m_externalattachmentproperties.add(propertyName);
                    boolean initial = propertyUtil.getSafeBooleanAttributeValue("initial", false);
                    if (initial)
                    {
                        m_initialExtAttachmentProperties.add(propertyName);
                    }

                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#refreshStaticClassdescriptions()
     */
    public void refreshStaticClassdescriptions() throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canRefreshStaticClassdescriptions()
     */
    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#doSearch(com.wewebu.ow.server.field.OwSearchNode, com.wewebu.ow.server.field.OwSort, java.util.Collection, int, int)
     */
    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectFromDMSID(java.lang.String, boolean)
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectFromPath(java.lang.String, boolean)
     */
    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwFNBPMRepository.getObjectFromPath: Path = " + strPath_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectClass(java.lang.String, com.wewebu.ow.server.ecm.OwResource)
     */
    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        OwFNBPM5WorkItemObjectClass retClass = (OwFNBPM5WorkItemObjectClass) classHashMap.get(strClassName_p);
        if (retClass == null)
        {
            VWWorkflowDefinition def = getVWSession().fetchWorkflowDefinition(-1, strClassName_p, false);

            retClass = new OwFNBPM5WorkItemObjectClass(def, this);

            classHashMap.put(def.getName(), retClass);
        }
        return retClass;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getObjectClassNames(int[], boolean, boolean, com.wewebu.ow.server.ecm.OwResource)
     */
    public Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getResource(java.lang.String)
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getResourceIDs()
     */
    public Iterator getResourceIDs() throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getEventManager()
     */
    public OwEventManager getEventManager()
    {
        return m_network.getEventManager();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#getDMSPrefix()
     */
    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#releaseResources()
     */
    public void releaseResources() throws Exception
    {
        if (m_locked.size() == 0)
        {
            return;
        }

        VWStepElement[] stepElements = m_locked.toArray(new VWStepElement[m_locked.size()]);
        m_locked.clear();

        try
        {
            VWException[] errors = VWStepElement.doUnlockMany(stepElements, false, false);
            for (int i = 0; i < errors.length; i++)
            {
                if (errors[i] != null)
                {
                    VWStepElement vwStepElement = stepElements[i];
                    LOG.error("OwFNBPMRepository.releaseResources(): unlocking StepElement failed for " + vwStepElement.getWorkflowName() + "." + vwStepElement.getStepName() + " Subject: " + vwStepElement.getSubject());
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Error release locked items.", e);
        }

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

    /** get the ECM specific ID of the Object.
     * 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     * 
     * @param wobid_p
     * @param resourceid_p
     * @return a <code>String</code>
     * @throws Exception
     */
    public static String getDMSID(String wobid_p, String resourceid_p) throws Exception
    {
        StringBuilder ret = new StringBuilder();

        ret.append(DMS_PREFIX);
        ret.append(",");
        ret.append(wobid_p);
        ret.append(",");
        ret.append(resourceid_p);

        return ret.toString();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepositoryContext.OwConfigChangeEventListener#onConfigChangeEventUpdateRoles()
     */
    public void onConfigChangeEventUpdateRoles() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public Collection<String> getWorkitemContainerIDs(boolean fRefresh_p, int iType_p) throws Exception
    {
        VWSession vwSession = getVWSession();

        // collect the queue names
        Collection<String> retNames = new ArrayList<String>();

        switch (iType_p)
        {
            case OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER:
            {
                //fetch roster boxes
                String[] queues = vwSession.fetchRosterNames(false);
                retNames.addAll(Arrays.asList(queues));
            }
                break;

            case OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER:
            {
                //fetch roster boxes
                String[] queues = vwSession.fetchQueueNames(VWSession.QUEUE_PROCESS);

                for (int i = 0; i < queues.length; i++)
                {
                    if (!isSystemQueue(queues[i]))
                    {
                        retNames.add(queues[i]);
                    }
                }
            }
                break;

            case OwObjectReference.OBJECT_TYPE_SYS_QUEUE_FOLDER:
            {
                //fetch roster boxes 
                String[] queues = vwSession.fetchQueueNames(VWSession.QUEUE_PROCESS);//QUEUE_SYSTEM

                for (int i = 0; i < queues.length; i++)
                {
                    if (isSystemQueue(queues[i]))
                    {
                        retNames.add(queues[i]);
                    }
                }
            }
                break;

            case OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER:
            {
                //          retNames.add("Inbox");
                //fetch user boxes
                String[] queues = vwSession.fetchQueueNames(VWSession.QUEUE_USER_CENTRIC_FOR_USER_ONLY);

                for (int i = 0; i < queues.length; i++)
                {
                    // Skip the Tracker user box and remove the "(x)" suffixes from all others
                    if (!queues[i].startsWith("Tracker"))
                    {
                        if (queues[i].endsWith(")"))
                        {
                            queues[i] = queues[i].substring(0, queues[i].indexOf("("));
                        }
                        retNames.add(queues[i]);
                    }
                }
            }
                break;

            case OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER:
            {
                retNames.add("Tracker");
            }
                break;

            case OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER:
            {
                retNames = getVirtualViewMap().keySet();
            }
                break;

            case OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER:
            {
                retNames.add(getProxyQueue().getID());
            }
                break;
            default:
                LOG.warn("OwFNBPM5Repository.getWorkitemContainerIDs: Unknown type definition provided will return empty Collection, unknown value = " + iType_p);
        }

        return retNames;
    }

    /** 
     * checks if the queue is system 
     */
    protected static boolean isSystemQueue(String strQueueName_p)
    {
        for (int i = 0; i < HIDDEN_QUEUE_NAMES.length; i++)
        {
            if (HIDDEN_QUEUE_NAMES[i].equals(strQueueName_p))
            {
                return true;
            }
        }

        return false;
    }

    public OwWorkitemContainer getWorkitemContainer(String sID_p, int iType_p) throws Exception
    {
        return createWorkitemContainer(sID_p, iType_p);
    }

    public String getWorkitemContainerName(String sID_p, int iType_p) throws Exception
    {
        switch (iType_p)
        {
            case OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER:
            {
                return getVirtualViewMap().get(sID_p).getName();
            }

            case OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER:
            {
                return getProxyQueue().getName();
            }

            default:
                return getContext().localizeLabel(sID_p);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#hasContainer(int)
     */
    public boolean hasContainer(int iType_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#createProxy()
     */
    public OwProxyInfo createProxy() throws Exception, OwNotSupportedException
    {
        // delegate to the proxy store
        return getProxyStore().createProxy();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#setProxies(java.util.Collection, java.lang.String)
     */
    public void setProxies(java.util.Collection proxies_p, String absentpersonID_p) throws Exception, OwNotSupportedException
    {
        // delegate to the proxy store
        getProxyStore().setProxies(proxies_p, absentpersonID_p);
    }

    /** 
     * get proxy info for given absent person
     * 
     * @param proxypersonID_p String ID of proxy person
     * @return Collection of OwFNBPMProxyStore.OwFNBPMProxyInfo
     * */
    public java.util.Collection getAbsents(String proxypersonID_p) throws Exception
    {
        // delegate to the proxy store
        return getProxyStore().getAbsents(proxypersonID_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#getProxies(java.lang.String)
     */
    public java.util.Collection getProxies(String absentpersonID_p) throws Exception, OwNotSupportedException
    {
        // delegate to the proxy store
        return getProxyStore().getProxies(absentpersonID_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#canProxy()
     */
    public boolean canProxy()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#getLaunchableWorkflowDescriptions(java.util.Collection)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Collection getLaunchableWorkflowDescriptions(Collection attachmentobjects_p) throws Exception
    {
        Collection attachmentObjects = attachmentobjects_p;
        if (attachmentobjects_p == null)
        {
            //set to NULL-object (empty collection)
            attachmentObjects = new ArrayList();
        }

        Set workflowDescriptionsSet = new HashSet();

        if (attachmentObjects.isEmpty())
        {
            workflowDescriptionsSet.addAll(getDefaultWorkflowDescriptions());
        }
        else
        {
            List<Set<OwFNCM5Object<?>>> listOfSetsOfWfDescriptions = new ArrayList<Set<OwFNCM5Object<?>>>();
            for (Iterator i = attachmentObjects.iterator(); i.hasNext();)
            {
                Object object = i.next();
                if (!(object instanceof OwObject))
                {
                    String message = "Invalid workflow operation! Attachment objects must be OwObjects!";
                    LOG.error("OwFNBPMRepository.getLaunchableWorkflowDescriptions: " + message);
                    throw new OwInvalidOperationException(getContext().localize("fncm.OwFNBPMRepository.invalidattachements", "Invalid workflow operation! Attachment objects must be OwObjects!"));
                }
                OwObject owObject = (OwObject) object;
                if (owObject instanceof OwFNCM5Object<?>)
                {
                    OwFNCM5Object<?> workflowSource = (OwFNCM5Object<?>) owObject;
                    if (!workflowSource.hasWorkflowDescriptions())
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.warn("OwFNBPMRepository.getLaunchableWorkflowDescriptions: object with DMSID=" + owObject.getDMSID() + " is not a workflow source. No workflow definitions are associated with it!");
                        }
                        return Collections.EMPTY_SET;
                    }
                    else
                    {
                        Set<OwFNCM5Object<?>> workflowDescriptions = workflowSource.getWorkflowDescriptions();
                        listOfSetsOfWfDescriptions.add(workflowDescriptions);
                    }
                }
                else
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.warn("OwFNBPMRepository.getLaunchableWorkflowDescriptions: object with DMSID=" + owObject.getDMSID() + " can't be a workflow target. Not an OwWorkflowSource, but an " + owObject.getClass() + " !");
                    }
                    listOfSetsOfWfDescriptions.add(getDefaultWorkflowDescriptions());
                }
            }

            if (listOfSetsOfWfDescriptions.isEmpty())
            {
                return Collections.EMPTY_SET;
            }

            //create the ''intersection'' of the definitions lists
            while (listOfSetsOfWfDescriptions.size() > 1)
            {
                Set<OwFNCM5Object<?>> setOfDescriptions1 = listOfSetsOfWfDescriptions.get(0);
                Set<OwFNCM5Object<?>> setOfDescriptions2 = listOfSetsOfWfDescriptions.get(1);
                Set commonIDs = new HashSet();
                for (OwFNCM5Object<?> wfDescription : setOfDescriptions1)
                {
                    if (setOfDescriptions2.contains(wfDescription))
                    {
                        commonIDs.add(wfDescription);
                    }
                }
                listOfSetsOfWfDescriptions.remove(0);
                listOfSetsOfWfDescriptions.remove(0);
                listOfSetsOfWfDescriptions.add(0, commonIDs);
            }

            workflowDescriptionsSet = listOfSetsOfWfDescriptions.get(0);

        }

        return workflowDescriptionsSet;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#createLaunchableItem(com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription, java.util.Collection)
     */
    public OwWorkitem createLaunchableItem(OwWorkflowDescription workflowDescription_p, Collection attachmentobjects_p) throws Exception
    {
        //inline null-object for attachment objects
        Collection objectsToAttach = attachmentobjects_p;
        if (objectsToAttach == null)
        {
            objectsToAttach = new ArrayList();
        }

        if (workflowDescription_p instanceof OwFNBPM5LaunchableWorkflowDescription)
        {
            OwFNBPM5LaunchableWorkflowDescription fnBpmDescription = (OwFNBPM5LaunchableWorkflowDescription) workflowDescription_p;
            return fnBpmDescription.createLaunchableItem(this, objectsToAttach);
        }
        else
        {
            LOG.error("OwFNBPMRepository.createLaunchableItem: Incompatible workflow definition " + workflowDescription_p.getName());
            throw new OwInvalidOperationException(getContext().localize("fncm.OwFNBPMRepository.incompatibleworkflowdefinition", "Incompatible workflow definition") + workflowDescription_p.getName());
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#canLaunch()
     */
    public boolean canLaunch()
    {
        return true;
    }

    private Set getDefaultWorkflowDescriptions() throws Exception
    {
        return m_network.getWorkflowDescriptions();
    }

    /** get the context
     */
    protected OwNetworkContext getContext()
    {
        return m_network.getContext();
    }

    /** reference to the configuration node */
    public OwXMLUtil getConfigNode()
    {
        return m_confignode;
    }

    /** get a reference to the network */
    public OwFNCM5Network getNetwork()
    {
        return m_network;
    }

    /**
     * Create a FileNet P8 BPM Session with the credentials password and user name.
     * First try to create a Connection by using the distinguished user name,
     * then only name and password.
     * @return filenet.vw.api.VWSession the session for given router
     * @throws Exception 
     */
    public VWSession createVWSession() throws Exception
    {
        OwFNCM5Credentials cred = getNetwork().getCredentials();
        OwUserInfo usr = cred.getUserInfo();

        VWSession vwSession = new VWSession();
        String connectionURL = m_network.getConnectionURL();
        vwSession.setBootstrapCEURI(connectionURL);

        try
        {
            LOG.debug("OwFNBPM5Repository.createVWSession: Using this distinguished username for vwsession: UserName = " + usr.getUserName());
            vwSession.logon(getConnectionPointName());
            //            vwSession.logon(usr.getUserName(), cred.getPassword(), getConnectionPointName());
        }
        catch (VWException e)
        {
            vwSession.logoff();
            try
            {
                LOG.debug("Login to PE with user short name = " + usr.getUserShortName());
                vwSession.logon(usr.getUserShortName(), cred.getPassword(), getConnectionPointName());
            }
            catch (VWException e1)
            {
                vwSession.logoff();
                LOG.debug("Login to PE with user long name = " + usr.getUserLongName());
                vwSession.logon(usr.getUserLongName(), cred.getPassword(), getConnectionPointName());
            }
        }
        // Make sure the web app ID is set
        vwSession.setDefaultWebApplication(filenet.vw.api.VWSession.WEBAPP_CUSTOM);

        return vwSession;
    }

    private String getConnectionPointName()
    {
        return this.m_strConnectionPointName;
    }

    /**
     * Get VWSession from Network
     *
     * @return VWSession object.
     */
    public VWSession getVWSession() throws Exception
    {
        if (null == m_vwSession)
        {
            m_vwSession = createVWSession();
        }

        return m_vwSession;
    }

    /** check if given property is a external string property attachment 
     * 
     * @param propname_p
     * @return a <code>boolean</code>
     */
    public boolean isExternalAttachmentProperty(String propname_p)
    {
        return m_externalattachmentproperties.contains(propname_p);
    }

    /**  (overridable) object factory which is called to create a object collection
     * 
     * @return OwStandardObjectCollection for search results
     */
    protected OwStandardObjectCollection createObjectCollection()
    {
        return new OwStandardObjectCollection();
    }

    /** get security token used for SSO between different applications accessing the same ECM system
     * 
     * @param resource_p OwResource of subsystem or null to use default
     * @return String a security token that can be used for authentication or an empty String if no token can be generated
     * @throws Exception 
     */
    public String getSecurityToken(OwResource resource_p) throws Exception
    {
        return m_network.getCredentials().getSecurityToken(resource_p);
    }

    /** save the lock state in a global array so we can track all the locked items
     * 
     * @param item_p {@link OwFNBPM5QueueWorkItem}
     * @param lock_p boolean lock state
     * @throws Exception 
     */
    public void saveLocked(OwFNBPM5QueueWorkItem item_p, boolean lock_p) throws Exception
    {
        if (lock_p)
        {
            m_locked.add(item_p.getStepElement());
        }
        else
        {
            m_locked.remove(item_p.getStepElement());
        }
    }

    private VWStepElement launch(String vwVersion_p, Collection attachmentObjects_p) throws Exception
    {
        VWStepElement launchStep = m_vwSession.createWorkflow(vwVersion_p);
        VWWorkflowDefinition vwDefinition = m_vwSession.fetchWorkflowDefinition(-1, vwVersion_p, false);
        String mainAttachementName = vwDefinition.getMainAttachmentName();
        if (mainAttachementName != null)
        {
            mainAttachementName = mainAttachementName.replaceAll("\"", "");
        }
        List attachments = new ArrayList();

        Map externalPropertyValues = new HashMap();

        for (Iterator i = attachmentObjects_p.iterator(); i.hasNext();)
        {
            OwObject object = (OwObject) i.next();
            if (object instanceof OwFNCM5Object)
            {
                if (mainAttachementName != null)
                {
                    VWAttachment attachment = new VWAttachment();
                    int objectType = object.getType();
                    int attachmentType = VWAttachmentType.ATTACHMENT_TYPE_UNDEFINED;
                    switch (objectType)
                    {
                        case OwObjectReference.OBJECT_TYPE_FOLDER:
                            attachmentType = VWAttachmentType.ATTACHMENT_TYPE_FOLDER;
                            break;
                        case OwObjectReference.OBJECT_TYPE_CUSTOM:
                        case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                            attachmentType = VWAttachmentType.ATTACHMENT_TYPE_DOCUMENT;
                            break;

                        default:
                            attachmentType = VWAttachmentType.ATTACHMENT_TYPE_UNDEFINED;
                            break;
                    }
                    attachment.setType(attachmentType);

                    attachment.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
                    attachment.setAttachmentName(object.getName());
                    attachment.setLibraryName(object.getResource().getID());
                    if (object.getVersionSeries() != null)
                    {
                        attachment.setId(object.getVersionSeries().getId());
                    }
                    else
                    {
                        OwFNCM5IndependentObject<?, ?> fncmObject = (OwFNCM5IndependentObject<?, ?>) object;
                        IndependentObject nativeObject = fncmObject.getNativeObject();
                        if (nativeObject != null)
                        {
                            attachment.setId(fncmObject.getID());
                        }
                        else
                        {
                            LOG.debug("OwFNBPMRepository.launch: Invalid nativ object, nativeObject == null");
                            attachment.setId("");
                        }
                    }
                    attachments.add(attachment);
                }
            }
            else
            {
                for (Iterator extI = m_initialExtAttachmentProperties.iterator(); extI.hasNext();)
                {
                    String initialProperty = (String) extI.next();
                    List propertyValues = (List) externalPropertyValues.get(initialProperty);
                    if (propertyValues == null)
                    {
                        propertyValues = new ArrayList();
                        externalPropertyValues.put(initialProperty, propertyValues);
                    }
                    propertyValues.add(OwStandardObjectReference.getCompleteReferenceString(object, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
                }
            }
        }

        if (mainAttachementName != null)
        {
            if (attachments.size() > 0)
            {
                VWFieldDefinition mainAttachementFiled = vwDefinition.getField(mainAttachementName);
                VWAttachment[] attachmentsArray = (VWAttachment[]) attachments.toArray(new VWAttachment[attachments.size()]);
                if (mainAttachementFiled.isArray())
                {
                    launchStep.setParameterValue(mainAttachementName, attachmentsArray, true);
                }
                else
                {
                    LOG.warn("OwFNBPMRepository.launch: multiple FNCM attachemnts requested on non array attachement for workflow class " + vwDefinition.getName() + " with attachemtn " + mainAttachementName + ". Using only the first attachement!");
                    launchStep.setParameterValue(mainAttachementName, attachmentsArray[0], true);
                }
            }
        }
        Set entries = externalPropertyValues.entrySet();
        for (Iterator i = entries.iterator(); i.hasNext();)
        {
            Entry externalEntry = (Entry) i.next();
            String property = (String) externalEntry.getKey();
            List values = (List) externalEntry.getValue();
            if (launchStep.hasParameterName(property) && values.size() > 0)
            {

                VWFieldDefinition externalAttachementFiled = vwDefinition.getField(property);
                String[] valuesArray = (String[]) values.toArray(new String[values.size()]);
                if (externalAttachementFiled.isArray())
                {
                    launchStep.setParameterValue(property, valuesArray, false);
                }
                else
                {
                    LOG.warn("OwFNBPMRepository.launch: multiple EXTERNAL attachemnts requested on non array attachement for workflow class " + vwDefinition.getName() + " with attachemtn " + property + ". Using only the first attachement!");
                    launchStep.setParameterValue(property, valuesArray[0], true);
                }
            }
        }
        return launchStep;
    }

    public String transfer(InputStream vwDefinitionStream_p, String uniqueId_p) throws OwServerException
    {
        //TODO: check theLinked parameter usage  !!!!!
        //TODO: check theMakeNewWorkSpace parameter usage  !!!!!
        try
        {
            VWWorkflowDefinition vwDefinition = VWWorkflowDefinition.read(vwDefinitionStream_p);

            LOG.warn("OwFNBPMRepository.transfer: Stream-read definition named = " + vwDefinition.getName());

            List vwExistingClassNames = Arrays.asList(m_vwSession.fetchWorkClassNames(true));

            if (vwExistingClassNames.contains(vwDefinition.getName()))
            {
                LOG.warn("OwFNBPMRepository.transfer: The engine-wf-definition name " + vwDefinition.getName() + " already exists!");
                //TODO : maybe use customizable name conflict resolution strategies ???  
            }

            VWTransferResult transferResult = m_vwSession.transfer(vwDefinition, uniqueId_p, true, true);
            if (!transferResult.success())
            {
                String[] transferErrors = transferResult.getErrors();
                if (transferErrors == null)
                {
                    transferErrors = new String[] { "Unknown error!" };
                }
                LOG.error("OwFNBPMRepository.transfer: Could not transfer workflow !. The following errors occurred : ");
                StringBuffer transferErrorsBuffer = new StringBuffer();
                if (transferErrors != null)
                {
                    for (int i = 0; i < transferErrors.length; i++)
                    {
                        LOG.error("\t Transfer error: " + transferErrors[i]);
                        transferErrorsBuffer.append(" [");
                        transferErrorsBuffer.append(transferErrors[i]);
                        transferErrorsBuffer.append("] ");
                    }
                }

                throw new OwServerException(getContext().localize("fncm.OwFNBPMRepository.workflowtransfererror", "Could not transfer workflow!") + " " + transferErrorsBuffer.toString());
            }

            return transferResult.getVersion();
        }
        catch (VWException e)
        {
            LOG.error("OwFNBPMRepository.transfer: Could not transfer workflow!", e);
            throw new OwServerException(getContext().localize("fncm.OwFNBPMRepository.workflowtransfererror", "Could not transfer workflow!"), e);
        }
    }

    /** get a OwWorkitemContainer wrapper for the given container name 
    *
    * @param sID_p String ID of container
    * @param iType_p <code>int</code> type of the requested container names as defined in OwObjectReference.OBJECT_TYPE_...
    * 
    * @return OwWorkitemContainer or throws OwObjectNotFoundException
    */
    private OwWorkitemContainer createWorkitemContainer(String sID_p, int iType_p) throws Exception
    {
        VWSession vwSession = getVWSession();

        switch (iType_p)
        {
            case OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER:
            {
                VWRoster roster = vwSession.getRoster(sID_p);
                return new OwFNBPM5RosterContainer(this, roster);
            }

            case OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER:
            {
                VWQueue queue = vwSession.getQueue(sID_p);
                return new OwFNBPM5PublicQueueContainer(this, queue);
            }

            case OwObjectReference.OBJECT_TYPE_SYS_QUEUE_FOLDER:
            {
                VWQueue queue = vwSession.getQueue(sID_p);
                return new OwFNBPM5SystemQueueContainer(this, queue);
            }

            case OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER:
            {
                VWQueue queue = vwSession.getQueue(sID_p);
                return new OwFNBPM5UserQueueContainer(this, queue);
            }

            case OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER:
            {
                VWQueue queue = vwSession.getQueue(sID_p);
                return new OwFNBPM5TrackerQueueContainer(this, queue);
            }

            case OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER:
            {
                return getVirtualViewMap().get(sID_p);
            }

            case OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER:
            {
                return getProxyQueue();
            }
        }

        String msg = "OwFNBPMRepository.createWorkitemContainer: Object not found, Type = " + iType_p + ", ID = " + sID_p;
        LOG.error(msg);
        throw new OwObjectNotFoundException(msg);
    }

    /** 
     * get the map of the virtual views, which maps ID's to views queues 
     * @return Map with natural order of virtual views
     */
    private Map<String, OwWorkitemContainer> getVirtualViewMap() throws Exception
    {
        if (null == m_viewqueues)
        {
            m_viewqueues = new LinkedHashMap<String, OwWorkitemContainer>();

            Collection searchtemplates = null;
            try
            {
                // get the view definition search templates from network
                searchtemplates = m_network.getApplicationObjects(OwAOTypesEnum.SEARCHTEMPLATE_BPM.type, "owbpmviewtemplates", false);
            }
            catch (OwObjectNotFoundException e)
            {
                // nothing defined
                return m_viewqueues;
            }

            m_reassigncontainernames = new ArrayList();

            // put into a map
            Iterator it = searchtemplates.iterator();
            while (it.hasNext())
            {
                OwFNBPM5SearchTemplate searchtemplate = (OwFNBPM5SearchTemplate) it.next();

                // collect reassign names independent of role
                m_reassigncontainernames.add(searchtemplate.getName());

                // add only allowed queues
                if (getRoleManager().isAllowed(OwRoleManager.ROLE_CATEGORY_VIRTUAL_QUEUE, searchtemplate.getName()))
                {
                    OwFNBPM5CrossQueueContainer queue = new OwFNBPM5CrossQueueContainer(this, searchtemplate);
                    m_viewqueues.put(queue.getID(), queue);
                }
            }
        }

        return m_viewqueues;
    }

    /** container names that can be reassigned to 
     * @return Collection of Strings
     * @throws Exception */
    public Collection getReassignContainerNames() throws Exception
    {
        // make sure map is loaded
        getVirtualViewMap();

        return m_reassigncontainernames;
    }

    protected OwRoleManager getRoleManager()
    {
        return m_network.getRoleManager();
    }

    /**
     * Utility method that prepares FileNet workflow definitions to be launched on 
     * {@link OwFNBPM5Repository} wrapped FileNet Process Engines.  
     * @param definition_p
     * @return the process engine <code>vwversion</code> of the prepared (i.e. transferred) workflow definition
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String prepareDefinition(WorkflowDefinition definition_p) throws Exception
    {
        VWSession vwSession = this.getVWSession();
        //TODO this should be replace with this (self)
        OwObject definitionObject = getNetwork().fromNativeObject(definition_p);

        OwProperty vwVersionProperty = definitionObject.getProperty(PropertyNames.VWVERSION);
        Object vwVersion = vwVersionProperty.getValue();
        boolean wfDefinitionTransferNeeded = (vwVersion == null || !vwSession.checkWorkflowIdentifier(vwVersion.toString()));

        if (wfDefinitionTransferNeeded)
        {
            OwVersion definitionObjectVersion = definitionObject.getVersion();
            String definitionObjectVersionInfo = definitionObjectVersion.getVersionInfo();
            LOG.warn("OwFNCMContentWorkflowDescription.prepareDefinition : Workflow definition " + definitionObject.getName() + " at version " + definitionObjectVersionInfo + " needs to be transferred!");

            OwContentCollection definitionContent = definitionObject.getContentCollection();
            if (definitionContent.getPageCount() < 1)
            {
                LOG.error("OwFNCMContentWorkflowDescription.prepareDefinition  : Invalid workflow definition " + definitionObject.getName());
                throw new OwInvalidOperationException(getNetwork().getContext().localize("fncm.OwFNCMContentWorkflowDescription.invalidworkflowdefinition", "Invalid workflow definition") + definitionObject.getName());
            }
            OwContentElement definitionContentElementNo1 = definitionContent.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
            InputStream definitionContentStream = definitionContentElementNo1.getContentStream(null);

            String uniqueId = definitionObject.getResourceID() + "/" + definitionObject.getID() + "/" + definitionObjectVersionInfo;
            String transferResultedVersion = this.transfer(definitionContentStream, uniqueId);

            vwVersionProperty.setValue(transferResultedVersion);
            OwPropertyCollection aPropertyCollection = new OwStandardPropertyCollection();
            OwPropertyClass vwVersionPropertyClass = vwVersionProperty.getPropertyClass();
            aPropertyCollection.put(vwVersionPropertyClass.getClassName(), vwVersionProperty);
            definitionObject.setProperties(aPropertyCollection);

            LOG.warn("OwFNCMContentWorkflowDescription.prepareDefinition  : Reset the " + PropertyNames.VWVERSION + " property to [" + transferResultedVersion + "]");
            vwVersion = transferResultedVersion;
        }
        else
        {
            LOG.debug("OwFNCMContentWorkflowDescription.prepareDefinition : Workflow definition " + definitionObject.getName() + " found on workflow engine !");
        }
        return vwVersion.toString();
    }

    /**
     * @param definition
     * @param attachmentobjects_p
     * @return OwWorkitem
     * @throws Exception 
     */
    public OwWorkitem launch(WorkflowDefinition definition, Collection attachmentobjects_p) throws Exception
    {
        String vwVersion = this.prepareDefinition(definition);
        VWStepElement launchStep = this.launch(vwVersion, attachmentobjects_p);

        LOG.debug("OwFNCMContentWorkflowDescription.launch : Creating launchable item from Engine-Workflow class-named " + launchStep.getWorkClassName());

        OwFNBPM5LaunchContainer launchContainer = new OwFNBPM5LaunchContainer(this, launchStep);
        return launchContainer.getLaunchWorkItem();
    }

    /**
     * get the object reference from a given native VW attachment
     *
     * @return OwObjectReference or OwStandardUnresolvedReference if not found or inaccessible
     *
     */
    public OwObjectReference getFNCMObject(VWAttachment att_p) throws Exception
    {
        OwObjectReference object = null; // the OW object

        //get ObjectStore
        OwFNCM5ObjectStoreResource resource = getNetwork().getResource(att_p.getLibraryName());
        ObjectStore obst = resource.getNativeObjectStore();

        switch (att_p.getType())
        {
            case VWAttachmentType.ATTACHMENT_TYPE_FOLDER:
                try
                {
                    IndependentObject doc = obst.getObject(ClassNames.FOLDER, att_p.getId());
                    object = getNetwork().fromNativeObject(doc);
                }
                catch (OwException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Could nod get attachment = " + att_p.getAttachmentName() + ", attachmenttype = folder", e);
                    }
                    object = new OwStandardUnresolvedAttachmentReference(e, this.getContext().localize1("ecmimpl.fncm.unresolvedobject", "Could not retreive object: (%1).", att_p.getAttachmentName()), att_p.getId(), null, att_p.getAttachmentName(),
                            OwObjectReference.OBJECT_TYPE_FOLDER, att_p);
                }
                break;

            case VWAttachmentType.ATTACHMENT_TYPE_CUSTOM_OBJECT:
                //has no content

            case VWAttachmentType.ATTACHMENT_TYPE_DOCUMENT:
                try
                {
                    VersionSeries versionSeries = Factory.VersionSeries.fetchInstance(obst, new Id(att_p.getId()), null);
                    //                    VersionSeries versionSeries = (VersionSeries) obst.getObject(ClassNames.VERSION_SERIES, att_p.getId());

                    Document doc;
                    if (att_p.getVersion() == null) // get the current version
                    {
                        doc = (Document) versionSeries.get_CurrentVersion();
                    }
                    else if (att_p.getVersion().equals("-1")) // is it the released version?
                    {
                        //TODO: maybe there is a better way to sort out the released version?
                        doc = (Document) versionSeries.get_ReleasedVersion();
                    }
                    else
                    {
                        //it is a specific version
                        doc = (Document) obst.getObject(ClassNames.DOCUMENT, att_p.getVersion());
                    }
                    doc.refresh((PropertyFilter) null);
                    object = getNetwork().fromNativeObject(doc);
                }
                catch (OwException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Could nod get attachment = " + att_p.getAttachmentName() + ", attachmenttype = document", e);
                    }
                    object = new OwStandardUnresolvedAttachmentReference(e, this.getContext().localize1("ecmimpl.fncm.unresolvedobjectnoaccess", "You do not have the required rights to open object: (%1).", att_p.getAttachmentName()), att_p.getId(),
                            null, att_p.getAttachmentName(), OwObjectReference.OBJECT_TYPE_DOCUMENT, att_p);
                }
                break;

            default:
            case VWAttachmentType.ATTACHMENT_TYPE_UNDEFINED:
                LOG.error("OwFNBPM5Repository.getFNCMObject: The object could not be found or does not exist, attachmentname = " + att_p.getAttachmentName() + ", attachmenttype = undefined");
                object = new OwStandardUnresolvedAttachmentReference(null, this.getContext().localize1("ecmimpl.fncm.unresolvedobjectnotfound", "The object (%1) can not be found or does not exist.", att_p.getAttachmentName()), att_p.getId(), null,
                        att_p.getAttachmentName(), OwObjectReference.OBJECT_TYPE_UNDEFINED, att_p);
        }

        return object;
    }

    /**
     * Return the proxy store used for current session.
     * @return OwFNBPM5ProxyStore
     * @throws Exception
     * @since 3.2.0.2
     */
    protected synchronized OwFNBPM5ProxyStore getProxyStore() throws Exception
    {
        if (m_proxystore == null)
        {
            m_proxystore = new OwFNBPM5ProxyStoreAttributeBag();

            m_proxystore.init(m_network);
        }
        return m_proxystore;
    }

    /**
     * Return the proxy queue of this session.
     * @return OwFNBPM5ProxyContainer
     * @throws Exception
     * @since 3.2.0.2
     */
    public synchronized OwFNBPM5ProxyContainer getProxyQueue() throws Exception
    {
        if (m_proxyqueue == null)
        {
            // create the one and only proxy queue
            m_proxyqueue = new OwFNBPM5ProxyContainer(this);
        }
        return m_proxyqueue;
    }

    @Override
    public OwIterable<OwFNBPM5WorkItem> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : FNBPM5 page search
        throw new OwNotSupportedException("The FNBPM5 repository does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }
}