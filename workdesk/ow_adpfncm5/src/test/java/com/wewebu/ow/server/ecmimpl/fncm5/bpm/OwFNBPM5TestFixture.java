package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.filenet.api.admin.DocumentClassDefinition;
import com.filenet.api.admin.EventClassDefinition;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.EventActionSet;
import com.filenet.api.collection.SubscribedEventList;
import com.filenet.api.collection.SubscriptionSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.GuidConstants;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.WorkflowDefinition;
import com.filenet.api.events.ClassWorkflowSubscription;
import com.filenet.api.events.EventAction;
import com.filenet.api.events.SubscribedEvent;
import com.filenet.api.events.Subscription;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.wewebu.ow.server.app.OwUserOperationEvent;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5NetworkManager;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ContainmentName;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NetworkCreateManager;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.exceptions.OwException;

import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWQueueQuery;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWWorkObject;

/**
 *<p>
 * Utility base class, used by all tests that need 
 * to have a clean fixture set up and teared down at each run. 
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
public abstract class OwFNBPM5TestFixture
{
    private static final Logger LOG = Logger.getLogger(OwFNBPM5TestFixture.class);

    protected static final String INBOX = "Inbox";
    protected static final String QUEUE_A = "QueueA";
    protected static final String DEFAULT_ROSTER = "DefaultRoster";

    protected static final String NAME_PROPERTY = "F_Subject";

    private static final String WORKFLOWS_RESINDING_FOLDER = "/ow_app/workflows";
    private static final String TEST_OBJECTS_RESINDING_FOLDER = "BPM_REP_JUNIT_OBJECTS";

    protected static final String WORKFLOWDEFINITION_1_XML = "workflowdefinition_1.xml";
    protected static final String WORKFLOWDEFINITION_2_XML = "workflowdefinition_2.xml";
    protected static final String WORKFLOWDEFINITION_1_NAME = "WorkflowDefinition_JUNIT_1";
    protected static final String WORKFLOWDEFINITION_2_NAME = "WorkflowDefinition_JUNIT_2";
    protected static final String WF1_TG1_SUBSCRIPTION_NAME = "WorkflowDefinition_JUNIT_1_Email_Subscription";
    protected static final String WF2_TG1_SUBSCRIPTION_NAME = "WorkflowDefinition_JUNIT_2_Form_Data_Subscription";
    protected static final String WF2_TG2_SUBSCRIPTION_NAME = "WorkflowDefinition_JUNIT_2_Email_Subscription";

    protected static final String WF1_DOCUMENT_SUBSCRIPTION_NAME = "WorkflowDefinition_JUNIT_1_Document_Subscription";

    protected static final String TEST_CLASS_1 = "Email";
    protected static final String TEST_CLASS_2 = "FormData";
    protected static final String TEST_CLASS_UNSUBSCRIBED = "FormPolicy";
    protected static final String TEST_CLASS_DOCUMENT = ClassNames.DOCUMENT;

    protected static final String[] TEST_CLASSES = new String[] { TEST_CLASS_1, TEST_CLASS_2 };

    protected static final String WORKFLOW_1_TARGET_CLASS_1 = TEST_CLASS_1;

    protected static final String WORKFLOW_2_TARGET_CLASS_1 = TEST_CLASS_2;
    protected static final String WORKFLOW_2_TARGET_CLASS_2 = TEST_CLASS_1;

    protected static final String[] WORKFLOWDEFINITIONS_XML = new String[] { WORKFLOWDEFINITION_1_XML, WORKFLOWDEFINITION_2_XML };
    protected static final String[] WORKFLOWDEFINITIONS_NAMES = new String[] { WORKFLOWDEFINITION_1_NAME, WORKFLOWDEFINITION_2_NAME };
    protected static final String[][] SUBSCRIPTIONS_TARGET_CLASSES = new String[][] { { WORKFLOW_1_TARGET_CLASS_1 }, { WORKFLOW_2_TARGET_CLASS_1, WORKFLOW_2_TARGET_CLASS_2 } };
    protected static final String[][] SUBSCRIPTIONS_NAMES = new String[][] { { WF1_TG1_SUBSCRIPTION_NAME }, { WF2_TG1_SUBSCRIPTION_NAME, WF2_TG2_SUBSCRIPTION_NAME } };

    protected static final String TEST_STEP_SUBJECT1 = "JUNIT_TEST_Launch_TestClass1";
    protected static final String TEST_STEP_SUBJECT2 = "JUNIT_TEST_Launch_TestClass2";

    protected OwFNCM5NetworkCreateManager m_createManager = null;

    protected OwObject[] m_testObjects;
    protected OwObject m_unsubscribedTestObject;

    protected OwFNCM5Network m_fncmNetwork;

    protected WorkflowDefinition[] m_testWorkflowDefinitions;

    @SuppressWarnings("rawtypes")
    protected OwFNCM5Object[] m_allDefinedDescriptions;
    @SuppressWarnings("rawtypes")
    protected OwFNCM5Object[][] m_classSubscriptions = null;
    //    protected OwFNCMSubscribedDescription m_documentSubscribedDescription;
    protected OwFNCM5Object<WorkflowDefinition>[][] m_wfPerSubscription;

    protected OwFNCM5Object<ClassWorkflowSubscription> m_documentSubscription;

    @SuppressWarnings("rawtypes")
    protected void deleteExistingTestWorkflows(ObjectStore objectStore_p)
    {
        for (int i = 0; i < WORKFLOWDEFINITIONS_NAMES.length; i++)
        {
            PropertyFilter filter = new PropertyFilter();
            filter.addIncludeProperty(new FilterElement(null, null, null, PropertyNames.CONTAINED_DOCUMENTS, null));
            Folder workflowsFolder = Factory.Folder.fetchInstance(objectStore_p, WORKFLOWS_RESINDING_FOLDER, filter);

            DocumentSet containess = workflowsFolder.get_ContainedDocuments();
            for (Iterator it = containess.iterator(); it.hasNext();)
            {
                Document containee = (Document) it.next();
                if (WORKFLOWDEFINITIONS_NAMES[i].equals(containee.get_Name()))
                {
                    assertTrue("Existing " + WORKFLOWDEFINITIONS_NAMES[i] + " is not a Document!", containee instanceof WorkflowDefinition);
                    LOG.debug("About to delete workflow defnition " + WORKFLOWDEFINITIONS_NAMES[i]);
                    WorkflowDefinition definition = (WorkflowDefinition) containee;
                    SubscriptionSet subscriptions = definition.get_WorkflowSourceSubscriptions();
                    for (Iterator j = subscriptions.iterator(); j.hasNext();)
                    {
                        Subscription subscription = (Subscription) j.next();
                        LOG.debug("About to delete the " + subscription.get_Name() + " subscription!");
                        subscription.delete();
                        subscription.save(RefreshMode.NO_REFRESH);
                    }
                    definition.delete();
                    definition.save(RefreshMode.NO_REFRESH);
                }
            }
        }
    }

    //    @SuppressWarnings("rawtypes")
    //    protected ClassDescription findClassDescription(ObjectStore objectStore_p, String symbolicName_p)
    //    {
    //        ClassDescriptionSet classDescriptons = objectStore_p.get_ClassDescriptions();
    //        ClassDescription targetClass = null;
    //        for (Iterator it = classDescriptons.iterator(); it.hasNext();)
    //        {
    //            ClassDescription description = (ClassDescription) it.next();
    //            if (symbolicName_p.equals(description.get_SymbolicName()))
    //            {
    //                targetClass = description;
    //                break;
    //            }
    //        }
    //        return targetClass;
    //    }

    @SuppressWarnings("rawtypes")
    protected OwWorkflowDescription findLaunchableWFDescription(Collection objectCollection_p, String descriptionName_p) throws Exception
    {
        final Collection wfForClass2 = getBPMRepository().getLaunchableWorkflowDescriptions(objectCollection_p);

        for (Iterator i = wfForClass2.iterator(); i.hasNext();)
        {
            OwWorkflowDescription description = (OwWorkflowDescription) i.next();
            String name = description.getName();
            if (descriptionName_p.equals(name))
            {
                return description;
            }
        }
        return null;
    }

    protected OwWorkitem findWorkItemInQueue(String queueName_p, String itemName_p) throws Exception
    {
        return findWorkItemInQueue(queueName_p, itemName_p, 5, 0);
    }

    @SuppressWarnings("unchecked")
    private void setUpClassSubscriptions(ObjectStore objectStore_p) throws Exception
    {
        m_classSubscriptions = new OwFNCM5Object<?>[m_testWorkflowDefinitions.length][];
        m_wfPerSubscription = new OwFNCM5Object[m_testWorkflowDefinitions.length][];
        for (int i = 0; i < m_testWorkflowDefinitions.length; i++)
        {
            m_classSubscriptions[i] = new OwFNCM5Object<?>[SUBSCRIPTIONS_NAMES[i].length];
            m_wfPerSubscription[i] = new OwFNCM5Object[SUBSCRIPTIONS_NAMES[i].length];
            for (int j = 0; j < SUBSCRIPTIONS_NAMES[i].length; j++)
            {
                // Create a class subscription.
                String targetClassSymbolicName = SUBSCRIPTIONS_TARGET_CLASSES[i][j];
                ClassWorkflowSubscription wfClassSubscriptionObj = createWfClassSubscription(objectStore_p, m_testWorkflowDefinitions[i], SUBSCRIPTIONS_NAMES[i][j], targetClassSymbolicName);

                //                subscriptionObjects[i][j] = new OwFNCMSubscribedDescription(m_fncmNetwork, wfClassSubscriptionObj);
                //                m_wfPerSubscription[i][j] = new OwFNCMDefinedDescription(m_fncmNetwork, testWorkflowDefinitions_p[i]);
                m_classSubscriptions[i][j] = m_fncmNetwork.fromNativeObject(wfClassSubscriptionObj);
                m_wfPerSubscription[i][j] = m_fncmNetwork.fromNativeObject(m_testWorkflowDefinitions[i]);

                LOG.debug("Added test subscription " + SUBSCRIPTIONS_NAMES[i][j] + "->" + targetClassSymbolicName + " for workflow " + m_testWorkflowDefinitions[i].get_Name());
            }
        }

        ClassWorkflowSubscription wfClassSubscriptionForDocument = createWfClassSubscription(objectStore_p, m_testWorkflowDefinitions[0], WF1_DOCUMENT_SUBSCRIPTION_NAME, TEST_CLASS_DOCUMENT);
        m_documentSubscription = m_fncmNetwork.fromNativeObject(wfClassSubscriptionForDocument);
    }

    @SuppressWarnings("unchecked")
    private ClassWorkflowSubscription createWfClassSubscription(ObjectStore objectStore_p, WorkflowDefinition workflowDefinition_p, String subscriptionName_p, String targetClassSymbolicName_p) throws Exception
    {

        DocumentClassDefinition targetClass = Factory.DocumentClassDefinition.fetchInstance(objectStore_p, targetClassSymbolicName_p, null);

        assertNotNull("Test class " + targetClassSymbolicName_p + " not found in " + objectStore_p.get_Name(), targetClass);

        ClassWorkflowSubscription wfClassSubscriptionObj = Factory.ClassWorkflowSubscription.createInstance(objectStore_p, ClassNames.CLASS_WORKFLOW_SUBSCRIPTION);

        // Add the required properties.
        wfClassSubscriptionObj.set_DisplayName(subscriptionName_p);
        wfClassSubscriptionObj.set_DescriptiveText(subscriptionName_p);
        wfClassSubscriptionObj.set_IsEnabled(true);
        wfClassSubscriptionObj.set_EnableManualLaunch(true);
        wfClassSubscriptionObj.set_IncludeSubclassesRequested(true);
        wfClassSubscriptionObj.set_SubscriptionTarget(targetClass);
        wfClassSubscriptionObj.set_WorkflowDefinition(workflowDefinition_p);

        //        prop = ObjectFactory.getProperty(Property.PRIORITY);
        //        prop.setValue(50);
        //        props.add(prop);

        //        EventAction action = Factory.EventAction.createInstance(os, classId)
        //          (EventAction) objectStore_p.getEventActions().findByProperty(Property.ID, ReadableMetadataObjects.IS_EQUAL, WORKFLOW_EVENT_ACTION_CLASS_ID);
        //        prop.setValue(PropertyNames.EVENT_ACTION, action);
        //        props.putValue(prop);

        EventActionSet eventActions = objectStore_p.get_EventActions();
        EventAction wfAction = null;
        Iterator<EventAction> eventActionIt = eventActions.iterator();
        while (eventActionIt.hasNext())
        {
            EventAction action = eventActionIt.next();
            if (ClassNames.WORKFLOW_EVENT_ACTION.equals(action.get_ClassDescription().get_SymbolicName()))
            {
                wfAction = action;
                break;
            }
        }
        assertNotNull(wfAction);

        wfClassSubscriptionObj.set_EventAction(wfAction);

        EventClassDefinition promoteVersionEventClassDescription = Factory.EventClassDefinition.getInstance(objectStore_p, GuidConstants.Class_PromoteVersionEvent);
        SubscribedEvent oSE1 = Factory.SubscribedEvent.createInstance();
        oSE1.set_EventClass(promoteVersionEventClassDescription);

        EventClassDefinition checkoutEventEventClassDescription = Factory.EventClassDefinition.getInstance(objectStore_p, GuidConstants.Class_CheckoutEvent);
        SubscribedEvent oSE2 = Factory.SubscribedEvent.createInstance();
        oSE2.set_EventClass(checkoutEventEventClassDescription);

        SubscribedEventList subEventList = Factory.SubscribedEvent.createList();
        subEventList.add(oSE1);
        subEventList.add(oSE2);

        wfClassSubscriptionObj.set_SubscribedEvents(subEventList);

        wfClassSubscriptionObj.set_IsolatedRegionNumber(0);

        // VWVersion will be available after the WorkflowDefinition is transfered to the Process Engine
        wfClassSubscriptionObj.set_VWVersion(workflowDefinition_p.get_VWVersion());

        wfClassSubscriptionObj.save(RefreshMode.REFRESH);
        return wfClassSubscriptionObj;
    }

    protected WorkflowDefinition[] setUpWorkflowDefinitions(ObjectStore objectStore_p) throws Exception
    {

        deleteExistingTestWorkflows(objectStore_p);
        WorkflowDefinition[] definitions = new WorkflowDefinition[WORKFLOWDEFINITIONS_NAMES.length];
        m_allDefinedDescriptions = new OwFNCM5Object[WORKFLOWDEFINITIONS_NAMES.length];
        for (int i = 0; i < definitions.length; i++)
        {
            String wfName = WORKFLOWDEFINITIONS_NAMES[i];
            String wfDefXML = WORKFLOWDEFINITIONS_XML[i];

            WorkflowDefinition newWflDef = createWfDefinition(objectStore_p, wfName, wfDefXML);

            definitions[i] = newWflDef;
            m_allDefinedDescriptions[i] = m_fncmNetwork.fromNativeObject(definitions[i]);
        }

        return definitions;
    }

    /**
     * @param objectStore_p
     * @param wfName
     * @param wfDefXML
     * @return WorkflowDefinition
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    protected WorkflowDefinition createWfDefinition(ObjectStore objectStore_p, String wfName, String wfDefXML)
    {
        WorkflowDefinition newWflDef = Factory.WorkflowDefinition.createInstance(objectStore_p, ClassNames.WORKFLOW_DEFINITION);
        Properties workflowProperties = newWflDef.getProperties();
        workflowProperties.putValue("DocumentTitle", wfName);

        InputStream wdContent = OwFNBPM5RepositoryTest.class.getResourceAsStream(wfDefXML);
        ContentElementList contentList = Factory.ContentTransfer.createList();

        ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
        ctObject.setCaptureSource(wdContent);
        ctObject.set_ContentType("application/x-filenet-workflowdefinition");
        ctObject.set_RetrievalName(wfName);
        // Add ContentTransfer object to list.
        contentList.add(ctObject);

        //            wdContent.setFilename(WORKFLOWDEFINITIONS_NAMES[i]);
        //            wdContent.setMimeType("application/x-filenet-workflowdefinition");
        newWflDef.set_ContentElements(contentList);
        newWflDef.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        newWflDef.set_VWVersion("12345678");
        newWflDef.save(RefreshMode.REFRESH);

        Folder workflowsFolder = (Folder) objectStore_p.getObject(ClassNames.FOLDER, WORKFLOWS_RESINDING_FOLDER);
        OwFNCM5ContainmentName containmentName = new OwFNCM5ContainmentName(wfName);
        ReferentialContainmentRelationship rcr = workflowsFolder.file(newWflDef, AutoUniqueName.AUTO_UNIQUE, containmentName.toString(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        //            newWflDef.file(workflowsFolder, true);
        rcr.save(RefreshMode.REFRESH);
        return newWflDef;
    }

    protected void setUpDocuments() throws Exception
    {
        if (m_createManager.isFolderAvailable(TEST_OBJECTS_RESINDING_FOLDER))
        {
            LOG.debug("Delete Folder (if exists) by FolderName [" + TEST_OBJECTS_RESINDING_FOLDER + "]");
            m_createManager.deleteFolderByPath(TEST_OBJECTS_RESINDING_FOLDER, true);
        }

        LOG.debug("Create Folder [" + TEST_OBJECTS_RESINDING_FOLDER + "]");
        m_createManager.createFolder(TEST_OBJECTS_RESINDING_FOLDER);
        m_testObjects = new OwObject[TEST_CLASSES.length];
        for (int i = 0; i < TEST_CLASSES.length; i++)
        {
            final String objName = TEST_CLASSES[i] + "_junit_obj";
            LOG.debug("Create Document [" + objName + "] inside Folder of class " + TEST_CLASSES[i]);
            String dmsid = m_createManager.createDocument(TEST_CLASSES[i], true, TEST_OBJECTS_RESINDING_FOLDER, objName);
            m_testObjects[i] = m_fncmNetwork.getObjectFromDMSID(dmsid, true);
        }
        final String unsubscribedObjName = TEST_CLASS_UNSUBSCRIBED + "_junit_obj";
        LOG.debug("Create Document [" + unsubscribedObjName + "] inside Folder of class " + TEST_CLASS_UNSUBSCRIBED);
        String dmsid = m_createManager.createDocument(TEST_CLASS_UNSUBSCRIBED, true, TEST_OBJECTS_RESINDING_FOLDER, unsubscribedObjName);
        m_unsubscribedTestObject = m_fncmNetwork.getObjectFromDMSID(dmsid, true);
    }

    public void removeTestItems() throws Exception
    {
        //        final String[] locations = new String[] { INBOX, QUEUE_A };
        //        for (int i = 0; i < locations.length; i++)
        //        {
        //            OwWorkitem queueTestItem1 = findWorkItemInQueue(locations[i], TEST_STEP_SUBJECT1);
        //            if (queueTestItem1 != null)
        //            {
        //                queueTestItem1.delete();
        //                LOG.debug("FOUND intem " + TEST_STEP_SUBJECT1 + " found in " + locations[i] + " - deleted the item!");
        //            }
        //            OwWorkitem queueTestItem2 = findWorkItemInQueue(locations[i], TEST_STEP_SUBJECT2);
        //            if (queueTestItem2 != null)
        //            {
        //                queueTestItem2.delete();
        //                LOG.debug("FOUND intem " + TEST_STEP_SUBJECT2 + " found in " + locations[i] + " - deleted the item!");
        //            }
        //        }
    }

    @After
    public void tearDown() throws Exception
    {
        LOG.debug("---------------------------------- TearDown: OwFNBPMRepositoryTest ----------------------------------");
        LOG.debug("Removing test workflow definition!");
        assertTrue("No FNCM network set up !", m_fncmNetwork instanceof OwFNCM5Network);
        ObjectStore objectStore = m_fncmNetwork.getResource(null).getNativeObjectStore();
        deleteExistingTestWorkflows(objectStore);
        removeTestItems();

        m_fncmNetwork.operationPerformed(new OwUserOperationEvent(null, OwUserOperationEvent.OwUserOperationType.STOP, null));
    }

    @Before
    public void setUp() throws Exception
    {
        LOG.debug("---------------------------------- SetUp: OwFNBPMRepositoryTest ----------------------------------");
        removeTestItems();
        LOG.debug("Recreating test workflow definition!");

        this.m_fncmNetwork = new OwFNCM5NetworkManager().createLoggedInNetwork();
        m_fncmNetwork.operationPerformed(new OwUserOperationEvent(null, OwUserOperationEvent.OwUserOperationType.START, null));

        this.m_createManager = new OwFNCM5NetworkCreateManager(this.m_fncmNetwork);

        OwFNCM5ObjectStoreResource defaultResource = m_fncmNetwork.getResource(null);
        ObjectStore objectStore = defaultResource.getNativeObjectStore();
        m_testWorkflowDefinitions = setUpWorkflowDefinitions(objectStore);
        setUpClassSubscriptions(objectStore);
        setUpDocuments();
    }

    protected OwWorkitem findWorkItemInQueueById(String queueName_p, String itemId_p) throws Exception
    {
        OwWorkitem foundItem = null;
        OwWorkitemContainer queue = getBPMRepository().getWorkitemContainer(queueName_p, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        VWQueue nativeQueue = (VWQueue) queue.getNativeObject();
        int queryFlags = VWQueue.QUERY_MIN_VALUES_INCLUSIVE + VWQueue.QUERY_MAX_VALUES_INCLUSIVE + VWQueue.QUERY_READ_LOCKED;
        VWQueueQuery query = nativeQueue.createQuery("F_WobNum", new Object[] { itemId_p }, new Object[] { itemId_p }, queryFlags, null, null, VWFetchType.FETCH_TYPE_QUEUE_ELEMENT);
        query.setBufferSize(1);
        if (query.hasNext())
        {
            VWQueueElement nativeFoundItem = (VWQueueElement) query.next();
            foundItem = new OwFNBPM5QueueWorkItem((OwFNBPM5BaseContainer) queue, nativeFoundItem);
        }

        return foundItem;
    }

    /**
     * Search in Roster
     * @param itemId_p
     * @return null if not found
     * @throws Exception
     */
    protected OwWorkitem findWorkItemById(String itemId_p) throws Exception
    {
        OwWorkitem foundItem = null;
        OwWorkitemContainer queue = getBPMRepository().getWorkitemContainer(OwFNBPM5TestFixture.DEFAULT_ROSTER, OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER);
        VWRoster roster = (VWRoster) queue.getNativeObject();
        int queryFlags = VWQueue.QUERY_MIN_VALUES_INCLUSIVE + VWQueue.QUERY_MAX_VALUES_INCLUSIVE + +VWQueue.QUERY_READ_LOCKED;
        VWRosterQuery query = roster.createQuery("F_WobNum", new Object[] { itemId_p }, new Object[] { itemId_p }, queryFlags, null, null, VWFetchType.FETCH_TYPE_WORKOBJECT);
        query.setBufferSize(1);
        if (query.hasNext())
        {
            VWWorkObject nativeFoundItem = (VWWorkObject) query.next();
            foundItem = new OwFNBPM5RosterWorkItem((OwFNBPM5BaseContainer) queue, nativeFoundItem);
        }

        return foundItem;
    }

    @SuppressWarnings("rawtypes")
    OwWorkitem findWorkItemInQueue(String queueName_p, String itemName_p, int retrys_p, int retrySleepMs_p) throws Exception
    {
        OwWorkitem foundItem = null;
        final int retryCount = retrys_p < 1 ? 2 : retrys_p + 1;
        int retry = 1;
        while (foundItem == null & retry < retryCount)
        {
            if (retry > 1)
            {
                LOG.debug("Search " + itemName_p + " in " + queueName_p + " retry #" + retry);
                Thread.sleep(retrySleepMs_p);
            }
            retry++;

            OwWorkitemContainer queue = getBPMRepository().getWorkitemContainer(queueName_p, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
            OwObjectCollection queueChildren = queue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
            LOG.debug("Queue " + queueName_p + " size :  " + queueChildren.size());
            for (Iterator i = queueChildren.iterator(); i.hasNext();)
            {
                OwWorkitem item = (OwWorkitem) i.next();
                LOG.debug("SI> " + item.getName());
                if (itemName_p.equals(item.getName()))
                {
                    foundItem = item;
                    break;
                }
            }
        }
        if (foundItem == null)
        {
            LOG.debug("No item named " + itemName_p + " in queue " + queueName_p + " !");
        }
        return foundItem;
    }

    protected OwWorkitemContainer getQueueA() throws Exception
    {
        return getBPMRepository().getWorkitemContainer(QUEUE_A, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
    }

    protected OwWorkitemContainer getInbox() throws Exception
    {
        return getBPMRepository().getWorkitemContainer(INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected OwWorkitem createTestWorkItem(String subject_p) throws Exception
    {
        Collection attachmentObjects = Arrays.asList(new OwObject[] { m_testObjects[0] });
        OwWorkflowDescription testWFDescription = findLaunchableWFDescription(attachmentObjects, WF1_TG1_SUBSCRIPTION_NAME);
        OwWorkitem workitem = getBPMRepository().createLaunchableItem(testWFDescription, attachmentObjects);

        OwProperty subjectProperty = workitem.getProperty(NAME_PROPERTY);
        subjectProperty.setValue(subject_p);

        OwPropertyCollection properties = new OwStandardPropertyCollection();
        properties.put(NAME_PROPERTY, subjectProperty);

        workitem.setProperties(properties);

        workitem.dispatch();

        //Refresh
        workitem = findWorkItemInQueue(INBOX, workitem.getName(), 5, 20000);
        return workitem;
    }

    protected OwFNBPM5Repository getBPMRepository() throws OwException
    {
        return (OwFNBPM5Repository) this.m_fncmNetwork.getInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository", null);
    }
}