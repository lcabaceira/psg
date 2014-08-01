package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import junit.framework.Assert;

import org.apache.log4j.Logger;

import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwFNCM5NetworkCreateManager.
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
public class OwFNCM5NetworkCreateManager
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5NetworkCreateManager.class);

    private static final String PROPERTY_NAME_DOCUMENT_TITLE = "DocumentTitle";

    private OwNetwork m_network;
    private String rootPath;

    public OwFNCM5NetworkCreateManager(OwNetwork network_p) throws Exception
    {
        this.m_network = network_p;
        OwFNCM5ObjectStoreResource resource = (OwFNCM5ObjectStoreResource) m_network.getResource(null);

        this.rootPath = "/" + resource.getName() + "/Test/";
    }

    public String getRootPath()
    {
        return rootPath;
    }

    /**
     * Ckech if a folder is available
     * 
     * @param folderName_p
     * @return boolean
     */
    public boolean isFolderAvailable(String folderName_p)
    {
        return this.isFolderAvailable(this.rootPath, folderName_p);
    }

    public boolean isFolderAvailable(String rootPath, String folderName_p)
    {
        boolean flag = true;
        try
        {
            m_network.getObjectFromPath(rootPath + folderName_p, true);
        }
        catch (Exception e)
        {
            Assert.assertTrue("Exception ist not from type OwExceptionm is from type: " + e.getClass(), e instanceof OwException);
            flag = false;
        }
        return flag;
    }

    public void deleteFolder(OwObject folder) throws Exception
    {
        String folderPath = folder.getPath();

        OwObjectCollection subFolders = folder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, 1000, 0, null);
        for (Object object : subFolders)
        {
            OwObject owObject = (OwObject) object;
            if (0 != (owObject.getType() & OwObjectReference.OBJECT_TYPE_FOLDER))
            {
                deleteFolder(owObject);
            }
        }

        deleteDocumentsOfFolder(folder);
        folder.delete();

        // RE-READ Folder to see if delete works fine
        OwObject owObject = null;
        try
        {
            owObject = m_network.getObjectFromPath(folderPath, true);
        }
        catch (Exception e)
        {
            Assert.assertTrue((e instanceof OwObjectNotFoundException));
            LOG.debug("Retry read deleted folder: ...was not found or was successfully deleted");
        }
        Assert.assertNull(owObject);
    }

    public void deleteFolderByPath(String folderName_p) throws Exception
    {
        OwObject owObject = null;
        try
        {
            owObject = m_network.getObjectFromPath(this.rootPath + folderName_p, true);
        }
        catch (Exception e)
        {
            if (e instanceof OwObjectNotFoundException)
            {
                LOG.debug("Folder [" + folderName_p + "] was not found");
            }
            else
            {
                Assert.fail("Unkown exception when getting folder by foldername...");
            }
        }
        Assert.assertNotNull("Cannot get Object by Path", owObject);
        deleteFolder(owObject);
    }

    /**
     * Delete a folder and his content 
     * The folder is on first level under ObjectStore/
     * @param folderName_p 
     * @param deleteDocumentsFirst_p if true, first delete all documents of this folder....   
     * @throws Exception 
     */
    public void deleteFolderByPath(String folderName_p, boolean deleteDocumentsFirst_p) throws Exception
    {
        // DELETE Folder
        OwObject owObject = null;
        try
        {
            owObject = m_network.getObjectFromPath(this.rootPath + folderName_p, true);
        }
        catch (Exception e)
        {
            if (e instanceof OwObjectNotFoundException)
            {
                LOG.debug("Folder [" + folderName_p + "] was not found");
            }
            else
            {
                Assert.fail("Unkown exception when getting folder by foldername...");
            }
        }
        Assert.assertNotNull("Cannot get Object by Path", owObject);

        try
        {
            if (deleteDocumentsFirst_p)
            {
                this.deleteDocumentsOfFolder(owObject);
            }
        }
        catch (Exception e)
        {
            LOG.error("Cannot delete the documents of the folder first...", e);
            Assert.fail("Cannot delete the documents of the folder first...");
        }

        try
        {
            owObject.delete();
            LOG.debug("Folder [" + folderName_p + "] was deleted...");
        }
        catch (Exception e)
        {
            //com.filenet.wcm.api.CannotDeleteObjectException: Content Engine COM API error. 
            //IDispatch error #44 The object cannot be deleted because it is referenced by other objects. [Code=8004022c]  ... 
            //Object Reference [FOLDER:2] ID "/junit_Folder Name_1" in ObjectStore "{997129AA-93EB-430B-A853-1C8259C08525}"; 
            //OMFC/Folder/Delete/Server.Unknown.8004022c
            LOG.debug("Error:" + e);
            Assert.assertTrue("Cannot delete Folder by path, the Exception is not from type OwException, is from type: " + e.getClass(), e instanceof OwException);
        }

        // RE-READ Folder to see if delete works fine
        owObject = null;
        try
        {
            owObject = m_network.getObjectFromPath(this.rootPath + folderName_p, true);
        }
        catch (Exception e)
        {
            Assert.assertTrue((e instanceof OwObjectNotFoundException));
            LOG.debug("Retry read deleted folder: ...was not found or was successfully deleted");
        }
        Assert.assertNull(owObject);
    }

    /**
     * Deletes all Documents of a folder (OwObject) 
     * @param owFolder_p 
     * @return String
     * @throws Exception 
     */
    public String deleteDocumentsOfFolder(OwObject owFolder_p) throws Exception
    {
        if (owFolder_p != null)
        {
            OwObjectCollection objectCollection = owFolder_p.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS }, null, new OwSort(), 20000, 0, null);
            if (objectCollection != null && objectCollection.size() > 0)
            {
                for (int i = 0; i < objectCollection.size(); i++)
                {
                    OwObject owObject = (OwObject) objectCollection.get(i);
                    owObject.delete();
                }
            }
        }
        return null;
    }

    /**
     * Create a Folder, try to Re-Read the Folder -Test
     * @param folderName_p 
     * @return String
     * @throws Exception 
     */
    public String createFolder(String folderName_p) throws Exception
    {
        return this.createFolder(this.rootPath, folderName_p);
    }

    public String createFolder(String rootPath, String folderName_p) throws Exception
    {
        Assert.assertNotNull("Initialize Network first. Use the initNetwork(OwNetwork network) first.", m_network);

        OwObject parent = m_network.getObjectFromPath(rootPath, true);

        return createFolder(parent, folderName_p);
    }

    /**
     * @param parent
     * @param folderName_p
     * @return String
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String createFolder(OwObject parent, String folderName_p) throws Exception
    {
        // CREATE Folder
        OwPropertyCollection props = new OwStandardPropertyCollection();
        OwPropertyClass propclass = m_network.getObjectClass(ClassNames.FOLDER, null).getPropertyClass(PropertyNames.FOLDER_NAME);
        OwProperty prop = new OwStandardProperty(folderName_p, propclass);
        props.put(prop.getPropertyClass().getClassName(), prop);
        String dmsID = null;

        dmsID = m_network.createNewObject(null, ClassNames.FOLDER, props, null, null, parent, "", "");

        Assert.assertNotNull("Cannot create folder " + folderName_p + "...", dmsID);
        Assert.assertNotSame("Cannot create folder " + folderName_p + "...", "", dmsID);

        // RE-READ Folder
        OwObject owObject = null;
        try
        {
            owObject = m_network.getObjectFromDMSID(dmsID, true);
        }
        catch (Exception e)
        {
            Assert.assertTrue("Cannot read the folder, the Exception is not from type OwException, is from type: " + e.getClass(), e instanceof OwException);
        }
        Assert.assertNotNull("Retry to read the created folder Assert.failed", owObject);
        Assert.assertEquals("Retry to read the created folder Assert.failed", dmsID, owObject.getDMSID());

        return owObject.getDMSID();
    }

    /**
     * Create a Document, try to Re-Read the Document
     * @param fPromote_p
     * @param folderName_p
     * @param documentName_p
     * @return String
     * @throws Exception
     */
    public String createDocument(String objectType_p, boolean fPromote_p, String folderName_p, String documentName_p) throws Exception
    {

        return createDocument(objectType_p, fPromote_p, folderName_p, documentName_p, false);
    }

    public String createDocument(String objectType_p, boolean fPromote_p, OwObject folder, String documentName_p) throws Exception
    {
        return createDocument(objectType_p, fPromote_p, folder, documentName_p, false);
    }

    public String createDocument(String objectType_p, boolean fPromote_p, String folderName_p, String documentName_p, boolean keepCheckedout_p) throws Exception
    {
        OwObject folder = m_network.getObjectFromPath(this.rootPath + folderName_p, keepCheckedout_p);

        return createDocument(objectType_p, fPromote_p, folder, documentName_p, keepCheckedout_p);

    }

    /**
     * @param objectType_p
     * @param fPromote_p
     * @param folder
     * @param documentName_p
     * @return String
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String createDocument(String objectType_p, boolean fPromote_p, OwObject folder, String documentName_p, boolean keepCheckedout_p) throws Exception
    {
        // CREATE Resource
        OwPropertyCollection props = new OwStandardPropertyCollection();
        OwPropertyClass propclass = m_network.getObjectClass(objectType_p, null).getPropertyClass(PROPERTY_NAME_DOCUMENT_TITLE);
        OwProperty prop = new OwStandardProperty(documentName_p, propclass);
        props.put(prop.getPropertyClass().getClassName(), prop);
        // === create object with createNewObject method
        String dmsID = m_network.createNewObject(fPromote_p, null, null, objectType_p, props, null, null, folder, "", "", keepCheckedout_p);

        Assert.assertNotNull("Cannot create document " + documentName_p + "...", dmsID);
        Assert.assertNotSame("Cannot create document " + documentName_p + "...", "", dmsID);

        // RE-READ Document
        OwObject owObject = null;
        try
        {
            owObject = m_network.getObjectFromDMSID(dmsID, true);
        }
        catch (Exception e)
        {
            if (e instanceof OwObjectNotFoundException)
            {
                LOG.debug("New created document [" + documentName_p + "] was not found");
            }
            else
            {
                Assert.fail("Unkown exception when getting new created document [" + documentName_p + "] by DMSID...");
            }
        }
        Assert.assertNotNull("Retry to read the created document Assert.failed", owObject);

        return owObject.getDMSID();
    }

    public String createCustomObject(String objectType_p, OwObject folder, String objectName_p) throws Exception
    {
        // CREATE Resource
        OwPropertyCollection props = new OwStandardPropertyCollection();
        // === create object with createNewObject method
        String dmsID = m_network.createNewObject(false, null, null, objectType_p, props, null, null, folder, "", "");

        Assert.assertNotNull("Cannot create custom object " + objectName_p + "...", dmsID);
        Assert.assertNotSame("Cannot create custom object " + objectName_p + "...", "", dmsID);

        // RE-READ Document
        OwObject owObject = null;
        try
        {
            owObject = m_network.getObjectFromDMSID(dmsID, true);
        }
        catch (Exception e)
        {
            if (e instanceof OwObjectNotFoundException)
            {
                LOG.debug("New created document [" + objectName_p + "] was not found");
            }
            else
            {
                Assert.fail("Unkown exception when getting new created document [" + objectName_p + "] by DMSID...");
            }
        }
        Assert.assertNotNull("Retry to read the created document Assert.failed", owObject);

        return owObject.getDMSID();
    }
}
