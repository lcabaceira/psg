package com.wewebu.ow.server.ecmimpl.fncm5.stress;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.security.auth.Subject;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.util.UserContext;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ContainmentName;

/**
 *<p>
 * This is a command line tool to prepare a test fixture for stress testing.
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
public class StressTestFixture
{
    private static final String CONNECTION_URI = "http://abs-fncm52.alfresco.com:9080/wsi/FNCEWS40MTOM/";
    private static final String USER_NAME = "P8Admin";
    private static final String PASSWORD = "IBMFileNetP8";
    private static final String JAAS_CONTEXT = "FileNetP8WSI";
    private static final String STORE_NAME = "P8ConfigObjectStore";

    private static final String DOCUMENT_CLASS = "Email";//ClassNames.DOCUMENT;
    private static final int ERR_RETRY_SLEEP = 30 * 1000;
    private static final int ERR_RETRIES = 3;

    private static final String ID_PREFIX = "I";
    private static final String NAME_PREFIX = "N";
    public static final String APPLICATION_ID = "com.wewebu.ow.StressDataLoader";

    private String password;
    private String user;
    private Map<String, ObjectStore> objectStoreCache = null;

    private Connection connection;

    public StressTestFixture()
    {
        this(USER_NAME, PASSWORD);
    }

    public StressTestFixture(String user, String password)
    {
        super();
        this.user = user;
        this.password = password;
    }

    @SuppressWarnings("unchecked")
    private synchronized Map<String, ObjectStore> getObjectStoreCache()
    {
        if (objectStoreCache == null)
        {
            try
            {
                objectStoreCache = new HashMap<String, ObjectStore>();

                Domain domain = getDomain();
                ObjectStoreSet objectStores = domain.get_ObjectStores();

                Iterator<ObjectStore> osIt = objectStores.iterator();
                while (osIt.hasNext())
                {
                    ObjectStore os = osIt.next();
                    objectStoreCache.put(NAME_PREFIX + os.get_Name(), os);
                    objectStoreCache.put(ID_PREFIX + os.get_Id(), os);
                }
            }
            catch (RuntimeException e)
            {
                objectStoreCache = null;
                throw e;
            }
        }

        return objectStoreCache;
    }

    public Domain getDomain()
    {
        Domain domain = com.filenet.api.core.Factory.Domain.fetchInstance(this.connection, null, null);
        return domain;
    }

    public ObjectStore fromObjectStoreId(String id)
    {
        Map<String, ObjectStore> cache = getObjectStoreCache();
        return cache.get(ID_PREFIX + id);
    }

    public ObjectStore fromObjectStoreName(String name)
    {
        Map<String, ObjectStore> cache = getObjectStoreCache();
        return cache.get(NAME_PREFIX + name);
    }

    public Folder fromPath(String objectStoreName, String path)
    {
        ObjectStore os = fromObjectStoreName(objectStoreName);
        return fromPath(os, path);
    }

    public Folder fromPath(ObjectStore objectStore, String path)
    {
        try
        {
            Folder object = Factory.Folder.fetchInstance(objectStore, path, null);
            object.get_Name();
            return object;
        }
        catch (EngineRuntimeException erx)
        {
            if (erx.getExceptionCode().equals(ExceptionCode.E_OBJECT_NOT_FOUND))
            {
                return null;
            }
            throw erx;
        }
    }

    public Folder newFolder(String name, String objectStoreName, String parentPath)
    {
        ObjectStore os = fromObjectStoreName(objectStoreName);
        return newFolder(name, os, parentPath);

    }

    public Folder newFolder(String name, ObjectStore objectStore, String partentPath)
    {
        Folder parent = fromPath(objectStore, partentPath);
        return newFolder(name, parent);
    }

    public Folder newFolder(String name, Folder parent)
    {
        final int retries = ERR_RETRIES;
        int retry = retries;
        while (true)
        {
            try
            {
                Folder subfolder = Factory.Folder.createInstance(parent.getObjectStore(), ClassNames.FOLDER);
                subfolder.set_FolderName(name);
                subfolder.set_Parent(parent);
                subfolder.save(RefreshMode.REFRESH);

                return subfolder;
            }
            catch (RuntimeException e)
            {
                if (retry > 0)
                {
                    retry--;
                    System.err.println("Document creation failed : " + e.getMessage() + ". Retry #" + (retries - retry));
                    try
                    {
                        Thread.sleep(ERR_RETRY_SLEEP);
                    }
                    catch (InterruptedException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                else
                {
                    throw e;
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    public Document newDocument(ObjectStore objectStore, String name, Folder parent, InputStream contentStream, String fileName, String mimeType)
    {
        final int retries = ERR_RETRIES;
        int retry = retries;
        while (true)
        {
            try
            {
                Document newDocument = Factory.Document.createInstance(objectStore, DOCUMENT_CLASS);

                Properties properties = newDocument.getProperties();
                properties.putValue("DocumentTitle", name);

                properties.putValue("From", "yoda@planetzorg.foo");

                StringList toValues = Factory.StringList.createList();
                toValues.add("vader@vademecum.bar");
                properties.putObjectValue("To", toValues);

                StringList ccValues = Factory.StringList.createList();
                ccValues.add("trash@trash.ro");
                properties.putObjectValue("CarbonCopy", ccValues);

                properties.putValue("EmailSubject", "Instant Intergalactic noise " + name);
                properties.putObjectValue("SentOn", new Date());
                properties.putObjectValue("ReceivedOn", new Date());

                newDocument.save(RefreshMode.REFRESH);

                OwFNCM5ContainmentName containmentName = new OwFNCM5ContainmentName(name);
                ReferentialContainmentRelationship ref = parent.file(newDocument, AutoUniqueName.AUTO_UNIQUE, containmentName.toString(), null);
                ref.save(RefreshMode.REFRESH);

                if (contentStream != null)
                {
                    ContentElementList contentList = Factory.ContentElement.createList();
                    ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
                    ctObject.setCaptureSource(contentStream);
                    ctObject.set_ContentType(mimeType);
                    ctObject.set_RetrievalName(fileName);
                    // Add ContentTransfer object to list.
                    contentList.add(ctObject);

                    newDocument.set_ContentElements(contentList);
                }

                newDocument.save(RefreshMode.REFRESH);
                newDocument.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

                newDocument.save(RefreshMode.REFRESH);

                return newDocument;
            }
            catch (RuntimeException e)
            {
                if (retry > 0)
                {
                    retry--;
                    System.err.println("Document creation failed : " + e.getMessage() + ". Retry #" + (retries - retry));
                    try
                    {
                        Thread.sleep(ERR_RETRY_SLEEP);
                    }
                    catch (InterruptedException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                else
                {
                    throw e;
                }
            }
        }
    }

    public void login() throws IOException
    {
        this.connection = Factory.Connection.getConnection(CONNECTION_URI);
        Subject subject = UserContext.createSubject(connection, user, password, JAAS_CONTEXT);
        UserContext.get().pushSubject(subject);
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length > 0)
        {

            int restartSubFolders = 0;
            int restartDocuments = 0;

            if (args.length >= 2)
            {
                restartSubFolders = Integer.parseInt(args[1]);
                restartDocuments = Integer.parseInt(args[2]);
            }

            if ("tiny".equals(args[0]))
            {
                createDS("TinyDS", 10, 3, restartSubFolders, restartDocuments);
                return;
            }
            else if ("small".equals(args[0]))
            {
                //Small DS (10*100=1000 Folders of 10 Documents each)
                createDS("SmallDS", 1000, 10, restartSubFolders, restartDocuments);
                return;
            }
            else if ("medium".equals(args[0]))
            {
                //Medium DS (50*100=5000 Folders of 50 Documents each)
                createDS("MediumDS", 5000, 50, restartSubFolders, restartDocuments);
                return;
            }
            else if ("large".equals(args[0]))
            {
                //Large DS (100*100=10000 Folders of 100 Documents each)
                createDS("LargeDS", 10000, 100, restartSubFolders, restartDocuments);
                return;
            }
            else if ("OWD-3638".equals(args[0]))
            {
                createDS("OWD-3638", 1, 100, restartSubFolders, restartDocuments);
                createDS("OWD-3638", 2, 300, 1 + restartSubFolders, restartDocuments);
                createDS("OWD-3638", 3, 600, 2 + restartSubFolders, restartDocuments);
                createDS("OWD-3638", 4, 1000, 3 + restartSubFolders, restartDocuments);
                return;
            }
            else
            {
                usage("Invalid stress data type " + args[0]);
            }

        }
        else
        {
            usage("");
        }

    }

    /**
     * @param errMsg
     */
    private static void usage(String errMsg)
    {
        System.err.println(errMsg);
        System.err.println("Usage: \n " + StressTestFixture.class.getName() + " <tiny/small/medium/large> [<restartSubFoldersIndex>] [<restartDocumentsIndex>]");
    }

    public static void createDS(String dsFolderPath, int subFoldersCount, int documentCount, int restartSubfolderIndex, int restartDocumentIndex) throws Exception
    {
        int lastSubfolderIndex = 0;
        int lastDocumentIndex = 0;
        final long startTime = System.currentTimeMillis();
        try
        {
            System.out.println("Proceeding with stress data folder " + dsFolderPath + " of " + subFoldersCount + " sub folders of " + documentCount + " each and re-start 0 based indexes " + restartSubfolderIndex + ", " + restartDocumentIndex);

            StressTestFixture loader = new StressTestFixture();
            loader.login();
            ObjectStore os = loader.fromObjectStoreName(STORE_NAME);

            Folder dsFolder = loader.fromPath(os, "/" + dsFolderPath);
            if (null == dsFolder)
            {
                dsFolder = loader.newFolder(dsFolderPath, os, "/");
            }

            String fileName = "text_content.txt";
            String mimeType = "text/plain";

            int docStartIndex = restartDocumentIndex;

            for (int subfolderIndex = restartSubfolderIndex; subfolderIndex < subFoldersCount; subfolderIndex++)
            {
                lastSubfolderIndex = subfolderIndex;

                String subfolderName = "test" + subfolderIndex;
                Folder subfolder = loader.fromPath(os, "/" + dsFolderPath + "/" + subfolderName);
                if (subfolder == null)
                {
                    subfolder = loader.newFolder(subfolderName, os, "/" + dsFolderPath);
                }

                for (int j = docStartIndex; j < documentCount; j++)
                {
                    lastDocumentIndex = j;

                    String documentName = "foo" + j;
                    InputStream contentStream = StressTestFixture.class.getResourceAsStream(fileName);
                    try
                    {
                        loader.newDocument(os, documentName, subfolder, contentStream, fileName, mimeType);
                        lastDocumentIndex = j;
                    }
                    finally
                    {
                        contentStream.close();
                    }
                    System.out.print(".");
                }

                System.err.println(subfolderIndex + "\t\t");

                docStartIndex = 0;

                if (0 == ((subfolderIndex + 1) % 50))
                {
                    long now = System.currentTimeMillis();
                    long time = now - startTime;
                    int done = subfolderIndex - restartSubfolderIndex;
                    double subfolderTime = (double) time / (double) done;
                    int toDo = subFoldersCount - (subfolderIndex + 1);
                    double etc = toDo * subfolderTime;
                    double etcH = etc / (60.0 * 60.0 * 1000.0);
                    long etcHH = (long) Math.floor(etcH);
                    long etcMM = (long) ((etcH - etcHH) * 60.0);
                    System.out.println();
                    System.out.println("Sub folder progress : " + (subfolderIndex + 1) + " sub fodlers out of " + subFoldersCount + " so far. ETC " + etcHH + "h " + etcMM + "m");
                    System.out.println();
                    //                    Thread.sleep(3 * 60 * 1000);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Restart parameters are " + lastSubfolderIndex + " " + lastDocumentIndex);
            System.exit(1);
        }
    }
}
