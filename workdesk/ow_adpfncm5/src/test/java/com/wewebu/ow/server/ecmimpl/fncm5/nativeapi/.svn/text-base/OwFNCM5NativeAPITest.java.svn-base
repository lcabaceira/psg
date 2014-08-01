package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.DocumentClassDefinition;
import com.filenet.api.admin.EventClassDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.PropertyTemplateSet;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.collection.SubscribedEventList;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.GuidConstants;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Containable;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.WorkflowDefinition;
import com.filenet.api.events.ClassWorkflowSubscription;
import com.filenet.api.events.SubscribedEvent;
import com.filenet.api.events.WorkflowEventAction;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyEngineObject;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

/**
 *<p>
 * OwFNCM5NativeAPITest.
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
@Ignore
public class OwFNCM5NativeAPITest extends OwFNCM5LoggedInNativeTest
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5NativeAPITest.class);

    @Test
    public void testDomainAndObjectStore() throws Exception
    {
        trace(defaultDomain);
        trace(defaultObjectStore);

        ObjectStore fetchedOS = (ObjectStore) defaultDomain.fetchObject("ObjectStore", defaultObjectStore.get_Id(), null);

        assertEquals(defaultObjectStore.get_Id(), fetchedOS.get_Id());
    }

    @Test
    public void testRootFolder() throws Exception
    {
        trace(defaultObjectStore);
        ClassDescription cd = defaultObjectStore.get_ClassDescription();
        List<PropertyDescription> pd = cd.get_PropertyDescriptions();
        for (PropertyDescription desc : pd)
        {
            System.out.println(desc.get_SymbolicName());
        }

        FolderSet topFolders = defaultObjectStore.get_TopFolders();
        Iterator fi = topFolders.iterator();
        while (fi.hasNext())
        {
            Folder f = (Folder) fi.next();
            trace(f);
        }

        PropertyEngineObject rootFolderProperty = (PropertyEngineObject) defaultObjectStore.getProperties().get(PropertyNames.ROOT_FOLDER);
        Folder root = defaultObjectStore.get_RootFolder();
        assertNotNull(root);
        root = (Folder) rootFolderProperty.getObjectValue();
        assertNotNull(root);
        trace(root);

    }

    public void testSearch() throws Exception
    {
        //        {
        //            SearchSQL sql = new SearchSQL();
        //            sql.setMaxRecords(150);
        //            sql.setSelectList("This");
        //            sql.setFromClauseInitialValue("document", "d", true);
        //            sql.setWhereClause("d.DocumentTitle LIKE '%a%'");
        //
        //            SearchScope searchScope = new SearchScope(defaultObjecStore);
        //            RepositoryRowSet rowSet = searchScope.fetchRows(sql, 10, null, new Boolean(true));
        //            trace(rowSet);
        //        }

        {
            SearchSQL sql = new SearchSQL();
            //            sql.setQueryString("SELECT This FROM document WHERE ((DocumentTitle LIKE '%ref%' AND (DateCreated>2000-10-17T03:00:00.000+03:00 AND DateCreated<2012-10-17T10:35:22.345+03:00)))");
            //            sql.setQueryString("SELECT This FROM Email WHERE ('in'= ANY To OR NOT 'notin'= ANY To OR To IN ('isin1','isin2','isin3') OR To NOT IN ('isnotin1','isnotin2','isnotin3'))");
            //            sql.setQueryString("SELECT This FROM Email WHERE (CarbonCopy IN ('isin1','isin2','isin3') OR NOT (CarbonCopy IN ('isnotin1','isnotin2','isnotin3')))");

            sql.setQueryString("SELECT a.This FROM Document AS a INNER JOIN ContentSearch AS b ON a.This=b.QueriedObject WHERE (CONTAINS(a.content,'bar'))");

            SearchScope searchScope = new SearchScope(defaultObjectStore);
            // Define property name array
            String propNames[] = { "DocumentTitle" };

            // Define filter element
            FilterElement fe = new FilterElement(null, null, null, propNames[0], null);

            // Define property filter
            PropertyFilter pf = new PropertyFilter();
            pf.addIncludeProperty(fe);
            pf.setMaxRecursion(1);

            //            RepositoryRowSet rowSet = searchScope.fetchRows(sql, 10, pf, new Boolean(true));
            //            trace(rowSet);
            IndependentObjectSet objects = searchScope.fetchObjects(sql, 10, pf, true);
            trace(objects);
        }

    }

    @Test
    public void testClassDescription()
    {
        Iterator osIter = objectStores.iterator();

        while (osIter.hasNext())
        {
            ObjectStore store = (ObjectStore) osIter.next();
            Iterator descIt = store.get_ClassDescriptions().iterator();
            trace(store.get_ClassDescriptions(), true);
        }
    }

    @Test
    public void testPorperties() throws Exception
    {

        PropertyDefinitionList l = Factory.PropertyDefinition.createList();
        System.out.println(l.size());
        //        IndependentObject dc = defaultObjecStore.fetchObject("PropertyDefinition", "DateCreated", null);

        //        assertNotNull(dc);

        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);
        PropertyTemplateSet propertyTemplates = defaultObjectStore.get_PropertyTemplates();

        Iterator ti = propertyTemplates.iterator();
        while (ti.hasNext())
        {
            PropertyTemplate template = (PropertyTemplate) ti.next();

            System.out.println(template.get_DisplayName());
        }

        final String className = "Email";

        ClassDescription classDescription = Factory.ClassDescription.fetchInstance(defaultObjectStore, className, pf);
        ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(defaultObjectStore, className, pf);

        PropertyDescriptionList pd = classDescription.get_PropertyDescriptions();
        PropertyDescription desc = (PropertyDescription) pd.iterator().next();

        ClassDescription pcd = desc.get_ClassDescription();
        System.out.println("PCD = " + pcd.get_SymbolicName());

        Properties properties = desc.getProperties();
        Iterator pi = properties.iterator();
        while (pi.hasNext())
        {
            Property p = (Property) pi.next();
            System.out.println("P> " + p.getPropertyName());
        }

        //        PropertyDescriptionList propertyDescriptions = classDescription.get_PropertyDescriptions();
        //        PropertyDefinitionList propertyDefinition = classDefinition.get_PropertyDefinitions();

        System.out.println(classDescription.get_SymbolicName());
        System.out.println(classDefinition.get_SymbolicName());
        System.out.println(classDescription.get_Name());
        System.out.println(classDefinition.get_Name());

        System.out.println(classDefinition.fetchProperty("NamePropertyIndex", null));

        //        PropertyDefinition pd=Factory.PropertyDefinition.

    }

    @Test
    public void testObjectFromPath() throws Exception
    {
        try
        {
            IndependentObject f = defaultObjectStore.fetchObject("Document", "/ow_app/owsearchtemplates", null);
        }
        catch (EngineRuntimeException e)
        {

        }

        IndependentObject fromPath1 = defaultObjectStore.fetchObject("Folder", "/root", null);
        assertNotNull(fromPath1);
        IndependentObject fromPath2 = defaultObjectStore.fetchObject("Folder", "/Test", null);
        assertNotNull(fromPath2);

        try
        {
            defaultObjectStore.fetchObject("Folder", "/", null);
            fail("fetchObject with path / should not work!");
        }
        catch (EngineRuntimeException e)
        {
            LOG.debug(e);
        }

        try
        {
            defaultObjectStore.fetchObject("Folder", "", null);
            fail("fetchObject with empty path should not work!");
        }
        catch (EngineRuntimeException e)
        {
            LOG.debug(e);
        }

    }

    @Test
    public void testObjectStoreClass() throws Exception
    {
        ClassDescription osClassDescription = defaultObjectStore.get_ClassDescription();
        assertNotNull(osClassDescription);
        assertEquals("ObjectStore", osClassDescription.get_SymbolicName());

        try
        {
            Factory.ClassDescription.fetchInstance(defaultObjectStore, osClassDescription.get_SymbolicName(), null);
            fail("fetchInstance of ObjectStore class should not work!");
        }
        catch (EngineRuntimeException e)
        {
            LOG.debug(e);
        }

        Iterator osIt = this.objectStores.iterator();
        while (osIt.hasNext())
        {
            ObjectStore os = (ObjectStore) osIt.next();
            ClassDescription cd = os.get_ClassDescription();
            if (os != this.defaultObjectStore)
            {
                assertNotSame(defaultObjectStore.get_ClassDescription(), cd);
            }
        }
    }

    @Test
    public void testScratch() throws Exception
    {
        Folder f = (Folder) defaultObjectStore.fetchObject("Folder", "/Test", null);

        ReferentialContainmentRelationshipSet containees = f.get_Containees();
        Iterator ci = containees.iterator();
        while (ci.hasNext())
        {

            ReferentialContainmentRelationship r = (ReferentialContainmentRelationship) ci.next();
            System.out.println("--------------");
            System.out.println("\tReferentialContainmentRelationship.ContainmentName = " + r.get_ContainmentName());
            System.out.println("\tReferentialContainmentRelationship.Name = " + r.get_Name());
            System.out.println("\tReferentialContainmentRelationship.Class = " + r.get_ClassDescription().get_SymbolicName());
            System.out.println(">");
            Containable head = (Containable) r.get_Head();
            System.out.println("\tHead.Name= " + head.get_Name());
            System.out.println("--------------");

        }
    }

    @Test
    public void testLinks() throws Exception
    {
        final String id = "{6336CD49-E58E-4282-9179-E0070E5D55E4}";

        SearchSQL sql = new SearchSQL();
        //            sql.setQueryString("SELECT This FROM document WHERE ((DocumentTitle LIKE '%ref%' AND (DateCreated>2000-10-17T03:00:00.000+03:00 AND DateCreated<2012-10-17T10:35:22.345+03:00)))");
        //            sql.setQueryString("SELECT This FROM Email WHERE ('in'= ANY To OR NOT 'notin'= ANY To OR To IN ('isin1','isin2','isin3') OR To NOT IN ('isnotin1','isnotin2','isnotin3'))");
        //            sql.setQueryString("SELECT This FROM Email WHERE (CarbonCopy IN ('isin1','isin2','isin3') OR NOT (CarbonCopy IN ('isnotin1','isnotin2','isnotin3')))");

        sql.setQueryString("SELECT Id, Tail, Name FROM Link WHERE (Head = Object('" + id + "') OR Tail = Object('" + id + "'))");

        SearchScope searchScope = new SearchScope(defaultObjectStore);

        IndependentObjectSet objects = searchScope.fetchObjects(sql, 10, null, true);
        trace(objects);
    }

    @Test
    public void testSubscriptions() throws Exception
    {
        ObjectStore os = defaultObjectStore;

        // Create a class workflow subscription.
        ClassWorkflowSubscription classWfSubscription = Factory.ClassWorkflowSubscription.createInstance(os, ClassNames.CLASS_WORKFLOW_SUBSCRIPTION);

        // Get the target class, the workflow definition document, and the event action.
        DocumentClassDefinition targetClass = Factory.DocumentClassDefinition.getInstance(os, new Id("{95D9616A-1800-4499-BE69-8F6B64B82D91}"));
        WorkflowDefinition workflowDefDocument = Factory.WorkflowDefinition.fetchInstance(os, "{08BC27A5-D60D-4854-8B03-F6D63F4108A4}", null);
        //        WorkflowDefinition workflowDefDocument = getWorkFlowDefinitionDocument(os, conn_point, ceURI, workflowDefinitionId);
        WorkflowEventAction eventAction = Factory.WorkflowEventAction.getInstance(os, ClassNames.WORKFLOW_EVENT_ACTION, new Id("{9A542A15-3D20-43D3-BCD7-A66BD158B73F}"));

        // Create a list of subscribed events.
        Id subscribedEventId = GuidConstants.Class_CheckoutEvent;
        EventClassDefinition evDef = Factory.EventClassDefinition.getInstance(os, subscribedEventId);
        SubscribedEvent subEvent = Factory.SubscribedEvent.createInstance();
        subEvent.set_EventClass(evDef);
        SubscribedEventList subEventList = Factory.SubscribedEvent.createList();
        subEventList.add(subEvent);

        // Set subscription properties.
        classWfSubscription.set_SubscribedEvents(subEventList);
        classWfSubscription.set_SubscriptionTarget(targetClass);
        classWfSubscription.set_WorkflowDefinition(workflowDefDocument);
        classWfSubscription.set_EventAction(eventAction);

        classWfSubscription.set_IsolatedRegionNumber(0);
        classWfSubscription.set_VWVersion("1234");
        classWfSubscription.set_DisplayName("ClassWorkflowSubscriptionJava");
        //        classWfSubscription.set_VWVersion(workflowDefDocument.get_VWVersion());
        //        PEConnectionPoint peConnPoint = Factory.PEConnectionPoint.fetchInstance(domain, conn_point, null);
        //        classWfSubscription.set_IsolatedRegionNumber(peConnPoint.get_IsolatedRegion().get_IsolatedRegionNumber());

        // Save the subscription.
        classWfSubscription.save(RefreshMode.REFRESH);

    }

    @Test
    public void testConcurrentAccess() throws Exception
    {
        PropertyFilter intitalFetchPropertyFilter = new PropertyFilter();
        intitalFetchPropertyFilter.addIncludeProperty(0, null, null, PropertyNames.ID, null);

        final String path = "/Test/getByPath/anEmail";
        Document f1 = (Document) defaultObjectStore.fetchObject(ClassNames.DOCUMENT, path, intitalFetchPropertyFilter);
        Document f2 = (Document) defaultObjectStore.fetchObject(ClassNames.DOCUMENT, path, intitalFetchPropertyFilter);

        assertNotSame(f1, f2);

        Properties f1Properties = f1.getProperties();
        Properties f2Properties = f2.getProperties();

        final String concurrentProperty = "EmailSubject";

        assertEquals(true, f1Properties.isPropertyPresent(PropertyNames.ID));
        assertEquals(false, f1Properties.isPropertyPresent(concurrentProperty));

        assertEquals(true, f2Properties.isPropertyPresent(PropertyNames.ID));
        assertEquals(false, f2Properties.isPropertyPresent(concurrentProperty));

        PropertyFilter titleFetchPropertyFilter = new PropertyFilter();
        titleFetchPropertyFilter.addIncludeProperty(0, null, null, concurrentProperty, null);

        f1.fetchProperties(titleFetchPropertyFilter);

        assertEquals(true, f1Properties.isPropertyPresent(concurrentProperty));
        f1Properties.putValue(concurrentProperty, "newTitle");

        f1.save(RefreshMode.NO_REFRESH);

        try
        {
            f2.fetchProperties(titleFetchPropertyFilter);
        }
        catch (EngineRuntimeException e)
        {
            if (ExceptionCode.API_FETCH_MERGE_PROPERTY_ERROR.equals(e.getExceptionCode()))
            {
                f2Properties = f2.getProperties();

                assertEquals(false, f2Properties.isPropertyPresent(concurrentProperty));

                f2.refresh();

                f2Properties = f2.getProperties();

                //unexpected property update ?!?@?!
                assertEquals(true, f2Properties.isPropertyPresent(concurrentProperty));

                f2.fetchProperties(titleFetchPropertyFilter);

                assertEquals(true, f2Properties.isPropertyPresent(concurrentProperty));

            }
            else
            {
                throw e;
            }
        }
    }
}
