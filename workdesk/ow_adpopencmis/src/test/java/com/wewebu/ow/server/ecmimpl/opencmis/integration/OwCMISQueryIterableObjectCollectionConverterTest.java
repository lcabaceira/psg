package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Iterator;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwIterableObjectCollectionConverter;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;

public class OwCMISQueryIterableObjectCollectionConverterTest extends OwCMISBigDataTest implements OwCMISBigDataTestResources
{

    public OwCMISQueryIterableObjectCollectionConverterTest()
    {
        super("IterableObjectCollectionConverterTest");
    }

    public void testConversion() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_SEARCH1_XML);

        OwIterableObjectCollectionConverter converter = new OwIterableObjectCollectionConverter(result, 0);

        assertEquals(getBigDataDocumentCount(D_CM_THUMBNAIL), converter.size());
        assertEquals((int) getBigDataDocumentCount(D_CM_THUMBNAIL), converter.getAttribute(OwObjectCollection.ATTRIBUTE_SIZE));
        assertEquals(true, converter.getAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE));
        assertTrue(converter.isComplete());

        for (Iterator iterator = converter.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            assertNotNull(object);
            assertTrue(object instanceof OwCMISObject);
        }
    }

}
