package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

/**
 *<p>
 * Installation/Creation of data for tests.
 * 
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
 *@since 4.2.0.0
 */
public class InstallTestData extends AbstractNativeTest
{
    protected static final String LARGE_DATA_ROOT = "/LargeData";

    public InstallTestData()
    {
        super(true, null, "http://10.249.240.62:8080/alfresco/cmisatom");
    }

    @Override
    protected void setUp() throws Exception
    {
        verifyFolderExist(LARGE_DATA_ROOT, JUNIT_TEST_PATH);
        super.setUp();
    }

    protected void verifyFolderExist(String... pathes)
    {
        for (String path : pathes)
        {
            try
            {
                getSession().getObjectByPath(path);
            }
            catch (CmisObjectNotFoundException e)
            {
                Map<String, String> properties = new HashMap<String, String>();
                //remove leading path separator
                properties.put("cmis:name", path.substring(1));
                properties.put("cmis:objectTypeId", "cmis:folder");
                getSession().getRootFolder().createFolder(properties);
            }
        }
    }

    public void testMassCreateFolder()
    {
        Folder obj = (Folder) getSession().getObjectByPath(LARGE_DATA_ROOT);
        int[] amount = { 100, 500, 1000 };
        for (int subFolder : amount)
        {
            Map<String, Object> prop = new HashMap<String, Object>();
            prop.put("cmis:name", subFolder + "sub");
            prop.put("cmis:objectTypeId", "cmis:folder");
            //create root folder
            Folder generate = obj.createFolder(prop);
            //generate sub folders
            for (int i = 0; i < subFolder; i++)
            {
                prop.put("cmis:name", String.format("%04d", i) + "sub" + subFolder);
                generate.createFolder(prop);
            }
        }
    }

    public void testMassCreateDocument()
    {
        Folder obj = (Folder) getSession().getObjectByPath(LARGE_DATA_ROOT);
        int[] amount = { 100, 500, 1000 };
        for (int part : amount)
        {
            Map<String, Object> prop = new HashMap<String, Object>();
            prop.put("cmis:name", part + "docs");
            prop.put("cmis:objectTypeId", "cmis:folder");
            //create folder for document
            Folder generate = obj.createFolder(prop);
            //begin document creation
            prop.put("cmis:objectTypeId", "cmis:document");
            for (int i = 0; i < part; i++)
            {
                prop.put("cmis:name", String.format("%04d", i) + "In" + generate.getName());
                generate.createDocument(prop, null, i % 2 == 0 ? VersioningState.MAJOR : VersioningState.MINOR);
            }
        }
    }

}
