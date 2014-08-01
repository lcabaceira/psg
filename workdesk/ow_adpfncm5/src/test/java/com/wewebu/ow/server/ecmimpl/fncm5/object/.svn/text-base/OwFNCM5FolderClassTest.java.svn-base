package com.wewebu.ow.server.ecmimpl.fncm5.object;

import org.junit.Assert;
import org.junit.Test;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Folder;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5IntegrationTest;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5FolderClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5FolderClassTest.
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
public class OwFNCM5FolderClassTest extends OwFNCM5IntegrationTest
{
    private static final String SCRAP_FOLDERS = "scrap_folders";
    private static final String TEST_RECORD_CLASS_NAME = "TestRecord1";
    private static final String RECORDTEMPLATES = "recordtemplates";

    private OwFNCM5Object<?> recordtemplatesFolder;
    private OwFNCM5Object<?> testRecord1Folder;
    private OwFNCM5Object<?> scrapFolder;
    private String owAppRootPath;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        owAppRootPath = "/" + this.network.getResource(null).getName() + "/ow_app/";

        if (!createManager.isFolderAvailable(owAppRootPath, RECORDTEMPLATES))
        {
            String recordtemplatesDMSID = createManager.createFolder(owAppRootPath, RECORDTEMPLATES);
            this.recordtemplatesFolder = network.getObjectFromDMSID(recordtemplatesDMSID, true);
        }
        else
        {
            this.recordtemplatesFolder = (OwFNCM5Object<?>) network.getObjectFromPath(owAppRootPath + RECORDTEMPLATES, true);
        }

        createTestRecordFolder();

        String scrapPath = SCRAP_FOLDERS;
        if (createManager.isFolderAvailable(scrapPath))
        {
            createManager.deleteFolderByPath(scrapPath);
        }
        String scrapDMSID = createManager.createFolder(SCRAP_FOLDERS);
        this.scrapFolder = network.getObjectFromDMSID(scrapDMSID, true);
    }

    /**
     * @throws Exception
     * @throws OwException
     */
    private void createTestRecordFolder() throws Exception, OwException
    {
        String testRecord1Path = RECORDTEMPLATES + "/" + TEST_RECORD_CLASS_NAME;
        if (createManager.isFolderAvailable(owAppRootPath, testRecord1Path))
        {
            OwObject testRecordFolder = network.getObjectFromPath(owAppRootPath + testRecord1Path, true);
            createManager.deleteFolder(testRecordFolder);
        }
        String testRecord1DMSID = createManager.createFolder(this.recordtemplatesFolder, TEST_RECORD_CLASS_NAME);
        this.testRecord1Folder = network.getObjectFromDMSID(testRecord1DMSID, true);

        String folderADMSID = this.createManager.createFolder(this.testRecord1Folder, "A");
        OwFNCM5Object<?> folderA = this.network.getObjectFromDMSID(folderADMSID, true);

        String folderBDMSID = this.createManager.createFolder(this.testRecord1Folder, "B");
        OwFNCM5Object<?> folderB = this.network.getObjectFromDMSID(folderADMSID, true);

        String folderAADMSID = this.createManager.createFolder(folderA, "AA");
        OwFNCM5Object<?> folderAA = this.network.getObjectFromDMSID(folderAADMSID, true);
    }

    @Override
    public void tearDown() throws Exception
    {
        this.createManager.deleteFolder(this.testRecord1Folder);
        this.createManager.deleteFolder(this.scrapFolder);
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateRecordClassBasedFolder() throws Exception
    {
        OwFNCM5FolderClass fncm5FolderClass = (OwFNCM5FolderClass) this.network.getObjectClass(TEST_RECORD_CLASS_NAME, this.network.getResource(null));
        OwFNCM5ObjectFactory factory_p = new OwFNCM5DefaultObjectFactory();
        OwPropertyCollection properties = new OwStandardPropertyCollection();

        OwFNCM5PropertyClass propClass = fncm5FolderClass.getPropertyClass(PropertyNames.FOLDER_NAME);
        OwStandardProperty nameProp = new OwStandardProperty("foobar", propClass);
        properties.put(propClass.getClassName(), nameProp);

        OwFNCM5Object<? extends Folder> testFolder = fncm5FolderClass.newObject(true, null, this.network.getResource(null), properties, null, null, this.scrapFolder, null, null, false, factory_p);
        Assert.assertNotNull(testFolder);

        OwObjectCollection childrenLeve0 = testFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, 10, 0, null);
        Assert.assertEquals(2, childrenLeve0.size());

        OwObject folderA = this.network.getObjectFromPath(testFolder.getPath() + "/" + "A", true);
        Assert.assertNotNull(folderA);
        OwObjectCollection folderAChildren = folderA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, 10, 0, null);
        Assert.assertEquals(1, folderAChildren.size());

        OwObject folderAA = (OwObject) folderAChildren.get(0);
        Assert.assertEquals("AA", folderAA.getName());
    }
}
