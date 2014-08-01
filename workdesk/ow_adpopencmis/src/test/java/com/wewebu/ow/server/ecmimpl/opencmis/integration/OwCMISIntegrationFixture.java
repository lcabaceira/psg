package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Iterator;

import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwIntegrationTest;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.unittest.util.OwTestContext;

/**
 *<p>
 * OwCMISIntegrationFixture.
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
public class OwCMISIntegrationFixture implements OwCMISTestFixture
{

    private static final Logger LOG = Logger.getLogger(OwCMISIntegrationFixture.class);

    public static final String J_UNIT_TEST = "/JUnitTest/";

    public static final String MAIN_REPOSITORY = "Main Repository";

    public static final String MAIN_REPOSITORY_J_UNIT_TEST = "/" + MAIN_REPOSITORY + J_UNIT_TEST;

    public static final String TEST_FOLDER_NAME = "OWCMISObjectTests";
    public static final String TEST_FOLDER = MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME + "/";
    public static final String ROOT_FOLDER = "/" + MAIN_REPOSITORY + "/";

    protected OwObject rootFolder;
    protected OwObject testFolder;

    protected OwIntegrationTest<? extends OwCMISNetwork> integrationTest;

    public OwCMISIntegrationFixture(OwIntegrationTest<? extends OwCMISNetwork> integrationTest)
    {
        super();
        this.integrationTest = integrationTest;
    }

    public OwObject getRootFolder()
    {
        return rootFolder;
    }

    public OwObject getTestFolder()
    {
        return testFolder;
    }

    public String getTestFolderPath()
    {
        return TEST_FOLDER;
    }

    @Override
    public void setUp() throws Exception
    {

        integrationTest.loginAdmin();
        ((OwTestContext) integrationTest.getNetwork().getContext()).loginInit();
        this.rootFolder = integrationTest.getNetwork().getObjectFromPath(ROOT_FOLDER, true);
        try
        {

            this.testFolder = integrationTest.getNetwork().getObjectFromPath(TEST_FOLDER, true);
        }
        catch (CmisObjectNotFoundException exception)
        {
            String testFolderID = createTestObject(BaseTypeId.CMIS_FOLDER.value(), TEST_FOLDER_NAME, null, rootFolder);
            this.testFolder = integrationTest.getNetwork().getObjectFromDMSID(testFolderID, true);
        }

        deleteFolderContents(testFolder, new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER });

    }

    @Override
    public void tearDown() throws Exception
    {
        deleteFolderContents(testFolder, new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER });

    }

    public String createTestObject(String className, String objectName, Object[][] properties, OwObject parent) throws Exception
    {
        int newLength = properties == null ? 1 : properties.length + 1;
        Object[][] newProperties = new Object[newLength][];

        if (properties != null && properties.length > 0)
        {
            System.arraycopy(properties, 0, newProperties, 0, properties.length);
        }

        OwCMISObjectClass clazz = integrationTest.getNetwork().getObjectClass(className, parent.getResource());

        int newIndex = newLength - 1;
        newProperties[newIndex] = new Object[2];
        newProperties[newIndex][0] = clazz.getNamePropertyName();
        newProperties[newIndex][1] = objectName;

        return createTestObject(className, newProperties, parent);
    }

    @SuppressWarnings("rawtypes")
    protected void deleteFolderContents(OwObject folder, int[] types) throws Exception
    {
        OwObjectCollection children = folder.getChilds(types, null, null, 10000, 0, null);
        LOG.info("About to delete " + children.size() + " test objects.");
        for (Iterator i = children.iterator(); i.hasNext();)
        {
            OwObject object = (OwObject) i.next();
            if (object.hasVersionSeries() && object.getVersion().canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                object.getVersion().cancelcheckout();
            }
        }
        children = folder.getChilds(types, null, null, 10000, 0, null);
        for (Iterator i = children.iterator(); i.hasNext();)
        {
            OwObject object = (OwObject) i.next();
            if (object.canDelete(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                object.delete();
            }
        }

    }

    @SuppressWarnings("unchecked")
    protected OwPropertyCollection createProperties(Object[][] properties_p, OwCMISObjectClass clazz) throws OwException
    {
        OwPropertyCollection testProperties = new OwStandardPropertyCollection();

        for (int i = 0; i < properties_p.length; i++)
        {
            OwCMISPropertyClass<?> propertyClass = clazz.getPropertyClass(properties_p[i][0].toString());
            OwProperty property = new OwStandardProperty(properties_p[i][1], propertyClass);

            testProperties.put(propertyClass.getClassName(), property);
        }
        return testProperties;
    }

    protected String createTestObject(String className_p, Object[][] properties_p, OwObject parent_p) throws Exception
    {
        OwCMISObjectClass clazz = integrationTest.getNetwork().getObjectClass(className_p, parent_p.getResource());
        OwPropertyCollection testProperties = createProperties(properties_p, clazz);

        return integrationTest.getNetwork().createNewObject(true, null, parent_p.getResource(), clazz.getClassName(), testProperties, null, null, parent_p, null, null, false);

    }

    public OwPropertyCollection createProperties(Object[][] properties_p, String className) throws OwException
    {
        return createProperties(properties_p, integrationTest.getNetwork().getObjectClass(className, null));
    }

}
