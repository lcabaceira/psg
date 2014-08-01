package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import java.util.Iterator;

import javax.security.auth.Subject;

import junit.framework.TestCase;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Containable;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

/**
 *<p>
 * PageTest.
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
public class PageTest extends TestCase
{

    public void testPaging()
    {
        // Set connection parameters; substitute for the placeholders.
        String uri = "http://abs-fncm52.alfresco.com:9080/wsi/FNCEWS40MTOM/";
        String username = "user1";
        String password = "wewebu2011";

        // Make connection.
        Connection conn = Factory.Connection.getConnection(uri);
        Subject subject = UserContext.createSubject(conn, username, password, "FileNetP8WSI");
        //        System.out.println(subject);
        UserContext.get().pushSubject(subject);

        try
        {
            // Get default domain.
            Domain domain = Factory.Domain.fetchInstance(conn, null, null);
            System.out.println("Domain: " + domain.get_Name());

            // Get object stores for domain.
            ObjectStoreSet osSet = domain.get_ObjectStores();
            ObjectStore store;
            Iterator osIter = osSet.iterator();

            while (osIter.hasNext())
            {
                store = (ObjectStore) osIter.next();
                System.out.println("Object store: " + store.get_Name() + " " + store.get_DisplayName() + " " + store.get_Id());
                iterateFolders(store.get_RootFolder(), 50, 25);

            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Connection to Content Engine successful");
        }
        finally
        {
            UserContext.get().popSubject();
        }

    }

    // Get folders for iteration.
    // Folder to iterate.
    // Prefetch page size. Example: 1
    // Fetch page size. Example: 1
    private static void iterateFolders(Folder mainFolder, int pageSizePrefetch, int pageSizeFetch)
    {
        // Show attributes.
        //        System.out.println("Parameter pageSizePrefetch: " + pageSizePrefetch);
        //        System.out.println("Parameter pageSizeFetch: " + pageSizeFetch);
        //        System.out.println("");

        //        // Get sub folders collection (and set prefetch page size).
        //        Property prop = mainFolder.fetchProperty("SubFolders", null, new Integer(pageSizePrefetch));
        //        IndependentObjectSet objectSet = prop.getIndependentObjectSetValue();

        // Initialize page iterator (and set fetch page size).
        Thread a = new Thread(new RunTree(mainFolder, pageSizeFetch, UserContext.get().getSubject()));
        a.start();
        //                PageIterator pageIter = mainFolder.get_SubFolders().pageIterator();//objectSet.pageIterator();
        //                pageIter.setPageSize(pageSizeFetch);
        //
        //                // Iterate through subfolders.
        //                iteratePageElements(pageIter);
    }

    // Iterate through page elements.
    public static void iteratePageElements(PageIterator pageIter)
    {
        // Cycle through pages.
        int pageCount = 0;
        while (pageIter.nextPage() == true)
        {
            // Get counts.
            pageCount++;
            int elementCount = pageIter.getElementCount();

            // Display number of objects on this page.
            System.out.println("Page: " + pageCount + "  Element count: " + elementCount);

            // Get elements on page.
            Object[] pageObjects = pageIter.getCurrentPage();
            for (int index = 0; index < pageObjects.length; index++)
            {
                // Get subobject.
                Object elementObject = pageObjects[index];

                // Check if it is a document.
                if (elementObject instanceof Document)
                {
                    Document elementDoc = (Document) elementObject;
                    elementDoc.refresh();
                    com.filenet.api.property.Properties props = elementDoc.getProperties();
                    System.out.println(" -" + props.getStringValue("DocumentTitle") + " (" + elementDoc.get_ClassDescription().get_SymbolicName() + ")");
                }
                // Check if it is any other type.
                else
                {
                    Containable conObject = (Containable) elementObject;
                    System.out.print(" -" + conObject.get_Name());
                    if (conObject instanceof Folder)
                    {
                        System.out.print(" (" + ((Folder) conObject).get_ClassDescription().get_SymbolicName() + ")");
                        iterateFolders((Folder) conObject, 25, 50);
                    }
                    System.out.println();

                }
            }
        }
    }

    static class RunTree implements Runnable
    {
        Folder folder;
        int pageSize;
        Subject sub;

        public RunTree(Folder f, int pageSize, Subject sub)
        {
            this.folder = f;
            this.pageSize = pageSize;
            this.sub = sub;
        }

        public void run()
        {
            if (UserContext.get().getSubject() == null)
            {
                UserContext.get().pushSubject(sub);
            }
            PageIterator pageIter = folder.get_SubFolders().pageIterator();//objectSet.pageIterator();
            pageIter.setPageSize(pageSize);
            // Iterate through subfolders.
            iteratePageElements(pageIter);

        }

    }

}
