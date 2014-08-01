package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationTest;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISNetworkTestCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;

public class OwCMISAlfrescoDocumentObjectTest extends OwCMISIntegrationTest
{

    private static final String D_OWD_HRDOCUMENT = "D:owd:hrdocument";
    private static final String OWD_DISPATCHDATE = "owd:DispatchDate";
    private static final String VERSIONING_TEST_DOC_1 = "alfrescoVersioningTestDoc1";
    private static String versioningTestDocument1DMSID;

    public OwCMISAlfrescoDocumentObjectTest(String name)
    {
        super("DocumentObjectTest", name);
    }

    @Override
    protected OwCMISTestFixture createFixture()
    {
        return new OwCMISAlfrescoIntegrationFixture(this);
    }

    @Override
    protected String getConfigBootstrapName()
    {
        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML;
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwCMISAlfrescoIntegrationFixture integrationFixture = (OwCMISAlfrescoIntegrationFixture) fixture;
        versioningTestDocument1DMSID = integrationFixture.createTestObject(D_OWD_HRDOCUMENT, VERSIONING_TEST_DOC_1, new Object[][] { { OWD_DISPATCHDATE, new Date() } }, integrationFixture.getTestFolder());
    }

    public void testCheckInAspects() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);

        OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();
        OwVersion version = versioningTestDocument1.getVersion();

        assertFalse("Object should not be checked-out", version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        version.checkout(null);

        assertTrue("Verification of check-out failed", version.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse("Verification of version check-in failed", version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion reservation = versionSeries.getReservation();

        assertTrue("Reservation object is not checked-out", reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue("Cannot check-in reservation object, verify AccessRights for JUnit user", reservation.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        final Date checkedinDate = new Date();

        OwCMISAlfrescoIntegrationFixture integrationFixture = (OwCMISAlfrescoIntegrationFixture) fixture;

        OwPropertyCollection properties = integrationFixture.createProperties(new Object[][] { { OWD_DISPATCHDATE, checkedinDate } }, "P:owd:AspectDate");
        reservation.checkin(true, null, D_OWD_HRDOCUMENT, properties, null, null, false, null, null);

        assertFalse("Reservation object still in check-out state", reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion checkedInVersion = versionSeries.getLatest();
        OwObject latestObject = versionSeries.getObject(checkedInVersion);

        assertFalse("Latest version still checked-out", checkedInVersion.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue("Latest version is not Major", checkedInVersion.isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwProperty checkedinDateProperty = latestObject.getProperty(OWD_DISPATCHDATE);
        assertEquals(checkedinDate, checkedinDateProperty.getValue());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testPreferedPropertyOrder() throws Exception
    {
        List<String> testPreferedOrder = new ArrayList<String>();
        testPreferedOrder.add("cmis:document.cmis:name");
        testPreferedOrder.add("owd:DispatchDate");
        testPreferedOrder.add("cmis:document.cmis:objectId");
        testPreferedOrder.add("cmis:document.cmis:createdBy");

        OwCMISNetworkTestCfg cfg = (OwCMISNetworkTestCfg) getNetwork().getNetworkConfiguration();
        cfg.setTestPreferedOrder(testPreferedOrder);

        OwCMISAlfrescoIntegrationFixture integrationFixture = (OwCMISAlfrescoIntegrationFixture) fixture;
        String documentName = "testNocontent_" + System.currentTimeMillis();
        String newId = integrationFixture.createTestObject("D:owd:hrdocument", documentName, new Object[][] { { "owd:DispatchDate", new Date() } }, integrationFixture.getTestFolder());
        //        this.createdObjectsDMSIDs.add(newId);
        OwObject createdDocument = getNetwork().getObjectFromDMSID(newId, true);

        OwPropertyCollection allProperties = createdDocument.getProperties(null);
        List<OwCMISProperty> orderedProps = new ArrayList<OwCMISProperty>(allProperties.values());

        assertEquals("cmis:document.cmis:name", orderedProps.get(0).getPropertyClass().getClassName());
        assertEquals("owd:DispatchDate", ((OwCMISPropertyClass) orderedProps.get(1).getPropertyClass()).getNonQualifiedName());
        assertEquals("cmis:document.cmis:objectId", orderedProps.get(2).getPropertyClass().getClassName());
        assertEquals("cmis:document.cmis:createdBy", orderedProps.get(3).getPropertyClass().getClassName());
    }
}
