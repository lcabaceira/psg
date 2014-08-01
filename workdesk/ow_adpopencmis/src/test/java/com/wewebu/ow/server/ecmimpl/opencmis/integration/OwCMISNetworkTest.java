package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResourceInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.mock.OwMockObject;
import com.wewebu.ow.unittest.pojo.OwObjectAdaptor;
import com.wewebu.ow.unittest.pojo.OwResourceAdaptor;

public class OwCMISNetworkTest extends OwCMISIntegrationTest
{
    private Set<String> createdObjectsDMSIDs;

    public OwCMISNetworkTest(String name_p)
    {
        super("DirectNetworkTest", name_p);
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();
        this.createdObjectsDMSIDs = new HashSet<String>();
    }

    protected OwCMISTestFixture createFixture()
    {
        return new OwCMISIntegrationFixture(this);
    }

    public void testWildCard() throws OwException
    {
        Collection<OwWildCardDefinition> def = this.getNetwork().getWildCardDefinitions("cmis:document.cmis:name", null, OwSearchOperator.CRIT_OP_LIKE);
        assertNotNull(def);
        assertEquals(2, def.size());
        def = this.getNetwork().getWildCardDefinitions("cmis:document.cmis:name", null, OwSearchOperator.CRIT_OP_NOT_LIKE);
        assertNotNull(def);
        assertEquals(2, def.size());
        def = this.getNetwork().getWildCardDefinitions("cmis:document.cmis:name", null, OwSearchOperator.CRIT_OP_EQUAL);
        assertNull(def);
    }

    public void testCanBatch() throws OwException
    {
        assertFalse(this.getNetwork().canBatch());
    }

    public void testCanUserSelect() throws OwException
    {
        assertFalse(this.getNetwork().canUserSelect());
    }

    public void testCanMethod() throws OwException
    {
        assertFalse(this.getNetwork().canDo(null, 0, 0));
        assertFalse(this.getNetwork().canDo(null, OwNetwork.CAN_DO_FUNCTIONCODE_ACL_TO_MODIFY_ANNOTATION, 0));
        assertFalse(this.getNetwork().canDo(null, OwNetwork.CAN_DO_FUNCTIONCODE_CREATE_ANNOTATION, 0));
        assertFalse(this.getNetwork().canDo(null, OwNetwork.CAN_DO_FUNCTIONCODE_DELETE_ANNOTATION, 0));
        assertFalse(this.getNetwork().canDo(null, OwNetwork.CAN_DO_FUNCTIONCODE_EDIT_ANNOTATION, 0));
        assertFalse(this.getNetwork().canDo(null, OwNetwork.CAN_DO_FUNCTIONCODE_PRINT, 0));
        assertFalse(this.getNetwork().canDo(null, OwNetwork.CAN_DO_FUNCTIONCODE_SAVE_CONTENT_TO_DISK, 0));
    }

    public void testReleaseResources() throws OwException
    {
        assertFalse(this.getNetwork().canRefreshStaticClassdescriptions());
        this.getNetwork().releaseResources();
    }

    public void testCanCreateNewObject() throws OwException
    {
        assertTrue(this.getNetwork().canCreateNewObject(null, null, 0));

        OwObjectAdaptor sample = new OwObjectAdaptor();
        sample.setType(OwObjectReference.OBJECT_TYPE_FOLDER);
        assertTrue(this.getNetwork().canCreateNewObject(null, sample, 0));
        sample.setType(OwObjectReference.OBJECT_TYPE_DYNAMIC_VIRTUAL_FOLDER);
        assertTrue(this.getNetwork().canCreateNewObject(null, sample, 0));
        sample.setType(OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER);
        assertTrue(this.getNetwork().canCreateNewObject(null, sample, 0));
        sample.setType(OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER);
        assertTrue(this.getNetwork().canCreateNewObject(null, sample, 0));

        sample.setType(OwObjectReference.OBJECT_TYPE_DOCUMENT);
        assertTrue(this.getNetwork().canCreateNewObject(null, sample, 0));//support of OwObjectLink (relationship) creation

        sample.setType(OwObjectReference.OBJECT_TYPE_LINK);
        assertFalse(getNetwork().canCreateNewObject(null, sample, 0));

        assertTrue(this.getNetwork().canCreateNewObject(null, null, 0));
    }

    public void testCanCreateCopy() throws OwException
    {
        assertFalse(this.getNetwork().canCreateObjectCopy(null, new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, 0));

        StringBuilder path = new StringBuilder("/");
        path.append(this.getNetwork().getResource(null).getID());
        path.append("/JUnitTest/");

        OwObject folder = this.getNetwork().getObjectFromPath(path.toString(), true);
        assertTrue(this.getNetwork().canCreateObjectCopy(folder, new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, 0));
    }

    public void testGetContext() throws OwException
    {
        assertNotNull(this.getNetwork().getContext());
    }

    public void testGetCredentials() throws OwException
    {
        assertNotNull(this.getNetwork().getCredentials());
    }

    public void testGetDmsPrefix() throws OwException
    {
        assertEquals("ocmis", this.getNetwork().getDMSPrefix());
    }

    public void testGetResource() throws OwException
    {
        OwCMISResource defRes = this.getNetwork().getResource(null);
        assertNotNull(defRes);
        defRes = this.getNetwork().getResource(defRes.getID());
        assertNotNull(defRes);
    }

    public void testHasInterface() throws OwException
    {
        assertFalse(this.getNetwork().hasInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository"));
        assertTrue(this.getNetwork().hasInterface("com.wewebu.ow.server.ui.viewer.OwInfoProvider"));
        assertTrue(this.getNetwork().hasInterface("com.wewebu.ow.server.app.id.viid.OwVIIdResolver"));
        assertTrue(this.getNetwork().hasInterface("org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler"));
    }

    public void testGetInterface() throws OwException
    {
        try
        {
            this.getNetwork().getInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository", null);
            fail("An OwInvalidOperationException should have been thrown!");
        }
        catch (OwInvalidOperationException invEx)
        {
            // OK
        }

        Object o = this.getNetwork().getInterface("com.wewebu.ow.server.app.id.viid.OwVIIdResolver", null);
        assertTrue(this.getNetwork() == o);

        o = this.getNetwork().getInterface("com.wewebu.ow.server.ui.viewer.OwInfoProvider", null);
        assertTrue(o instanceof com.wewebu.ow.server.ui.viewer.OwInfoProvider);

        o = this.getNetwork().getInterface("org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler", null);
        assertNotNull(o);
        assertTrue(o instanceof org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler);
    }

    public void testGetRoleDisplayName() throws OwException
    {
        assertNotNull(this.getNetwork().getRoleDisplayName(OwCMISIntegrationFixture.MAIN_REPOSITORY));
    }

    public void testGetUserFromId() throws OwException
    {
        try
        {
            assertFalse("No UserRepository available by default", this.getNetwork().canUserSelect());
            this.getNetwork().getUserFromID(OwCMISIntegrationFixture.MAIN_REPOSITORY);
            fail("This should only work without UserRepository LDAP");
        }
        catch (OwException se)
        {
            //OK
        }
    }

    public void testCanEditAccessRights() throws OwException
    {
        try
        {
            this.getNetwork().canEditAccessRights(null);
            fail("Null value was not checked before");
        }
        catch (OwServerException e)
        {

        }

        OwObjectSkeleton skeleton = this.getNetwork().createObjectSkeleton(this.getNetwork().getObjectClass("cmis:document", null), null);
        assertTrue(this.getNetwork().canEditAccessRights(skeleton));

    }

    public void testGetAccessRightsView() throws OwException
    {
        try
        {
            this.getNetwork().getEditAccessRightsSubModul(null);
            fail("Null value was not checked before");
        }
        catch (OwServerException e)
        {

        }

        OwUIAccessRightsModul aclUI = this.getNetwork().getEditAccessRightsSubModul(((OwCMISIntegrationFixture) fixture).getTestFolder());
        assertTrue("No Access RightsModule returned", aclUI != null);
        OwMockObject obj = new OwMockObject("abc");
        aclUI = getNetwork().getEditAccessRightsSubModul(obj);
        assertNull("Access rigths module available, for non-modifiable ACL", aclUI);
    }

    public void testGetEventManager() throws OwException
    {
        assertNotNull(this.getNetwork().getEventManager());
    }

    public void testGetLoginUI() throws OwException
    {
        assertNotNull(this.getNetwork().getLoginSubModul());
    }

    public void testResourceIterator() throws OwException
    {
        assertNotNull(this.getNetwork().getResourceIDs());
    }

    public void testGetUserSelectionUI() throws OwException
    {
        try
        {
            this.getNetwork().getUserSelectSubModul("nonNullId", new int[0]);
            fail("An OwInvalidOperationException  should have been thrown!");
        }
        catch (OwInvalidOperationException ivoe)
        {

        }
    }

    public void testOwUserInfoObject() throws Exception
    {
        OwCredentials credentials = this.getNetwork().getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();

        assertNotNull(userInfo);
        assertNotNull(userInfo.getUserName());
        assertNotNull(userInfo.getUserID());
        assertNotNull(userInfo.getUserLongName());
        assertNotNull(userInfo.getGroups());
        assertNotNull(userInfo.getRoleNames());
        assertFalse(userInfo.isGroup());
        assertTrue(userInfo.equals(credentials.getUserInfo()));
    }

    @SuppressWarnings("unchecked")
    public void testCreateDocumentNoContent() throws Exception
    {
        OwResource resource = getNetwork().getResource(OwCMISIntegrationFixture.MAIN_REPOSITORY);
        OwObjectClass objectclass = getNetwork().getObjectClass("cmis:document", resource);

        OwObjectSkeleton skeleton = getNetwork().createObjectSkeleton(objectclass, resource);
        OwPropertyCollection docStandardPropertiesMap = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String documentName = "testNocontent_" + System.currentTimeMillis();
        String namePropertyName = objectclass.getNamePropertyName();
        OwPropertyClass namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        OwProperty nameProperty = new OwStandardProperty(documentName, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        OwContentCollection noContent = null;
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        String newId = getNetwork().createNewObject(true, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, integrationFixture.getTestFolder(), null, null, false);

        OwObject createdDocument = getNetwork().getObjectFromDMSID(newId, true);
        assertNotNull(createdDocument);
        OwContentCollection contentcollection = createdDocument.getContentCollection();
        assertNotNull(contentcollection);
        assertTrue(0 == contentcollection.getPageCount());
    }

    @SuppressWarnings("unchecked")
    public void testCreateDocumentMinorMajorVersion() throws Exception
    {
        OwResource resource = getNetwork().getResource(OwCMISIntegrationFixture.MAIN_REPOSITORY);
        OwObjectClass objectclass = getNetwork().getObjectClass("cmis:document", resource);

        //Major
        OwObjectSkeleton skeletonMajor = getNetwork().createObjectSkeleton(objectclass, resource);
        OwPropertyCollection docStandardPropertiesMap = skeletonMajor.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String documentNameMajor = "testNocontent_Major_" + System.currentTimeMillis();
        String namePropertyName = objectclass.getNamePropertyName();
        OwPropertyClass namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        OwProperty nameProperty = new OwStandardProperty(documentNameMajor, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        OwContentCollection noContent = null;
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        String newIdMajor = getNetwork().createNewObject(true, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, integrationFixture.getTestFolder(), null, null, false);

        //Minor
        OwObjectSkeleton skeletonMinor = getNetwork().createObjectSkeleton(objectclass, resource);
        docStandardPropertiesMap = skeletonMinor.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String documentNameMinor = "testNocontent_Minor_" + System.currentTimeMillis();
        namePropertyName = objectclass.getNamePropertyName();
        namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        nameProperty = new OwStandardProperty(documentNameMinor, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        String newIdMinor = getNetwork().createNewObject(false, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, integrationFixture.getTestFolder(), null, null, false);
        this.createdObjectsDMSIDs.add(newIdMinor);

        OwObject majorDocument = getNetwork().getObjectFromDMSID(newIdMajor, true);
        assertNotNull(majorDocument);
        OwObject minorDocument = getNetwork().getObjectFromDMSID(newIdMinor, true);
        assertNotNull(minorDocument);

        assertTrue(majorDocument.getVersion().isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(minorDocument.getVersion().isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
    }

    //Only for CMIS over IBM P8 5 CMIS
    @SuppressWarnings("unchecked")
    public void testCreateDocumentNoParent() throws Exception
    {
        OwResource resource = getNetwork().getDefaultResource();
        OwCMISSession session = getNetwork().getSession(resource.getID());
        OwCMISResourceInfo resourceInfo = session.getResourceInfo();
        if (!resourceInfo.getCapabilities().isCapabilityUnfiling())
        {
            // skip this test
            return;
        }

        OwObjectClass objectclass = getNetwork().getObjectClass("cmis:document", resource);

        OwObjectSkeleton skeleton = getNetwork().createObjectSkeleton(objectclass, resource);
        OwPropertyCollection docStandardPropertiesMap = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String documentName = "testNocontent_" + System.currentTimeMillis();
        String namePropertyName = objectclass.getNamePropertyName();
        OwPropertyClass namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        OwProperty nameProperty = new OwStandardProperty(documentName, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        OwContentCollection noContent = null;
        String newId = getNetwork().createNewObject(true, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, null, null, null, false);
        this.createdObjectsDMSIDs.add(newId);

        OwObject createdDocument = getNetwork().getObjectFromDMSID(newId, true);
        assertNotNull(createdDocument);
        OwObjectCollection parents = createdDocument.getParents();
        assertNotNull(parents);
        assertTrue(parents.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testCreateDocumentKeepCheckedOut() throws Exception
    {
        OwResource resource = getNetwork().getResource(OwCMISIntegrationFixture.MAIN_REPOSITORY);
        OwObjectClass objectclass = getNetwork().getObjectClass("cmis:document", resource);

        //Checked out
        OwObjectSkeleton skeleton = getNetwork().createObjectSkeleton(objectclass, resource);
        OwPropertyCollection docStandardPropertiesMap = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String documentName = "testNocontent_Major_" + System.currentTimeMillis();
        String namePropertyName = objectclass.getNamePropertyName();
        OwPropertyClass namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        OwProperty nameProperty = new OwStandardProperty(documentName, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        OwContentCollection noContent = null;
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        String pwcId = getNetwork().createNewObject(true, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, integrationFixture.getTestFolder(), null, null, true);

        OwObject document = getNetwork().getObjectFromDMSID(pwcId, true);
        assertNotNull(document);
        OwVersionSeries versions = document.getVersionSeries();
        assertNotNull(versions.getReservation());
        assertTrue("The ID of the PWC should be returned as result.", document.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
    }

    @SuppressWarnings("unchecked")
    public void testCreateNewDossierFolder() throws Exception
    {
        OwResource resource = getNetwork().getResource(OwCMISIntegrationFixture.MAIN_REPOSITORY);
        OwObjectClass objectclass = getNetwork().getObjectClass("F:owd:dossier", resource);

        //Checked out
        OwObjectSkeleton skeleton = getNetwork().createObjectSkeleton(objectclass, resource);
        OwPropertyCollection docStandardPropertiesMap = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String documentName = "testNewFolder_" + System.currentTimeMillis();
        String namePropertyName = objectclass.getNamePropertyName();
        OwPropertyClass namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        OwProperty nameProperty = new OwStandardProperty(documentName, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        OwPropertyClass owdKnowledgePropertyClass = objectclass.getPropertyClass("owd:knowledge");
        OwProperty owdKnowledgeProperty = new OwStandardProperty(new String[] { "Administration", "Java" }, owdKnowledgePropertyClass);
        docStandardPropertiesMap.put(owdKnowledgeProperty.getPropertyClass().getClassName(), owdKnowledgeProperty);

        OwContentCollection noContent = null;
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        String newId = getNetwork().createNewObject(false, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, integrationFixture.getTestFolder(), null, null, false);

        OwObject folder = getNetwork().getObjectFromDMSID(newId, true);
        assertNotNull(folder);
        assertTrue(OwObjectReference.OBJECT_TYPE_FOLDER == folder.getObjectClass().getType());
        assertEquals("F:owd:dossier", folder.getObjectClass().getClassName());

        owdKnowledgeProperty = folder.getProperty("owd:knowledge");
        assertTrue(owdKnowledgeProperty.getValue() instanceof String[]);
        String[] value = (String[]) owdKnowledgeProperty.getValue();
        assertEquals(2, value.length);
        List<String> valueList = Arrays.asList(value);
        assertTrue(valueList.contains("Administration"));
        assertTrue(valueList.contains("Java"));
    }

    public void testGetFieldDefinition() throws Exception
    {
        OwFieldDefinition cmisNameDef = this.getNetwork().getFieldDefinition("cmis:document.cmis:name", null);
        assertNotNull(cmisNameDef);
        assertEquals("cmis:document.cmis:name", cmisNameDef.getClassName());

        OwFieldDefinition owObjectName = this.getNetwork().getFieldDefinition("OW_ObjectName", null);
        assertNotNull(owObjectName);
        //        assertEquals("OW_ObjectName", owObjectName.getClassName());

        OwFieldDefinition owObjectPath = this.getNetwork().getFieldDefinition("OW_ObjectPath", null);
        assertNotNull(owObjectPath);
        //        assertEquals("OW_ObjectPath", owObjectPath.getClassName());

        OwFieldDefinition owClassDescription = this.getNetwork().getFieldDefinition("OW_ClassDescription", null);
        assertNotNull(owClassDescription);
        //        assertEquals("OW_ClassDescription", owClassDescription.getClassName());

        OwFieldDefinition owVersionSeries = this.getNetwork().getFieldDefinition("OW_VersionSeries", null);
        assertNotNull(owVersionSeries);
        //        assertEquals("OW_VersionSeries", owVersionSeries.getClassName());

        //        OwFieldDefinition owResource = this.network.getFieldDefinition("OW_Resource", null);
        //        assertNotNull(owResource);
        //        assertEquals("OW_Resource", owResource.getClassName());
    }

    public void testGetNetworkConfiguration() throws Exception
    {
        assertNotNull("Configuration should not be null", getNetwork().getNetworkConfiguration());
    }

    public void testGetNetworkCfgOpenCMIS() throws Exception
    {
        OwCMISNetworkCfg cfg = getNetwork().getNetworkConfiguration();
        assertNotNull("Configuration should not be null", cfg);
        Map<String, String> cmisParams = cfg.getBindingConfig();
        assertNotNull(cmisParams);
        assertEquals("Test", cmisParams.get("junit"));
    }

    public void testGetObjectClass() throws Exception
    {
        getNetwork().getObjectClass("cmis:document", null);

        OwResourceAdaptor resource = new OwResourceAdaptor();
        resource.id = "invalidId";
        try
        {
            getNetwork().getObjectClass("cmis:document", resource);
            fail("Invalid Resource Id was provided, should throw exception");
        }
        catch (OwObjectNotFoundException ex)
        {

        }

        try
        {
            getNetwork().getObjectClass("Hello World", null);
            fail("Invalid ObjectClass Id was provided, should throw exception");
        }
        catch (OwObjectNotFoundException ex)
        {

        }
    }

    @SuppressWarnings("rawtypes")
    public void testGetObjectClassNames() throws Exception
    {
        int[] types = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER };
        Map names = getNetwork().getObjectClassNames(types, true, true, getNetwork().getResource(null));

        assertNotNull(names);
        assertFalse(names.isEmpty());

        assertEquals(2, names.size());
        //that may fail with different CMIS system
        String name = (String) names.get("cmis:document");
        assertTrue(name.contains("Document"));
        name = (String) names.get("cmis:folder");
        assertTrue(name.contains("Folder"));

        name = (String) names.get("cmis:relationship");
        assertNull(name);
        name = (String) names.get("cmis:policy");
        assertNull(name);
    }

    public void testGetObjectFromPath() throws Exception
    {
        OwObject o = getNetwork().getObjectFromPath("/", false);
        assertNotNull(o);
        assertEquals("ow_root/cmis_domain", o.getMIMEType());

        OwObject rootPath = getNetwork().getObjectFromPath("//Main Repository/", false);
        assertNotNull(rootPath);
        assertEquals("ow_folder/cmis:folder", rootPath.getMIMEType());

        o = getNetwork().getObjectFromPath("/JUnitTest/", true);
        assertEquals("JUnitTest", o.getName());

        try
        {
            getNetwork().getObjectFromPath("///", false);
            fail("Invalid path send to Repository");
        }
        catch (OwServerException ex)
        {
            //OK
        }

        try
        {
            getNetwork().getObjectFromPath("/anIncompletePath", false);
            fail("Path is incomplete and invalid.");
        }
        catch (OwObjectNotFoundException ex)
        {
            //OK
        }
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        if (this.getNetwork() != null)
        {
            //Cleanup
            for (String documentId : this.createdObjectsDMSIDs)
            {
                try
                {
                    OwObject document = getNetwork().getObjectFromDMSID(documentId, false);
                    document.delete();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            getNetwork().logout();
        }
    }
}