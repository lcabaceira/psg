package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

public class RenditionsTest extends AbstractNativeTest
{
    private Folder testFolder;
    private Document newDocument;
    private LinkedList<CmisObject> oldObjects;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.testFolder = (Folder) getSession().getObjectByPath("/JUnitTest/");

        ItemIterable<CmisObject> testChildren = testFolder.getChildren();
        Iterator<CmisObject> childrenIterator = testChildren.iterator();
        oldObjects = new LinkedList<CmisObject>();
        while (childrenIterator.hasNext())
        {
            oldObjects.add(childrenIterator.next());
        }
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:owd:hrdocument");
        properties.put(PropertyIds.NAME, "RenditionDocument");

        this.newDocument = testFolder.createDocument(properties, null, VersioningState.MAJOR);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.nativeapi.AbstractNativeTest#tearDown()
     */
    @Override
    protected void tearDown() throws Exception
    {
        this.newDocument.delete();
        super.tearDown();
    }

    public void testRenditions() throws Exception
    {
        List<Rendition> renditions = this.newDocument.getRenditions();

        trace(renditions);

        for (CmisObject oldObject : oldObjects)
        {
            trace(oldObject.getRenditions());
        }
    }

    private void trace(List<Rendition> renditions)
    {
        if (renditions == null || renditions.isEmpty())
        {
            System.out.println("NO RENDITIONS");
        }
        else
        {
            for (Rendition rendition : renditions)
            {
                System.out.println(rendition.getTitle() + " = " + rendition.getMimeType());
            }
        }
    }
}
