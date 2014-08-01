package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardUnresolvedReference;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSIDDecoder;

public class OwCMISIdObjectConverterTest extends TestCase
{
    private Map<String, OwObjectReference> defaulResolvedObjects;
    private OwObjectReference reference1;
    private OwObjectReference reference2;
    private String dmsid1;
    private String dmsid2;

    protected OwObjectReference mockObjectReference(final String dmsid)
    {
        return new OwObjectReference() {

            @Override
            public boolean hasContent(int iContext_p) throws Exception
            {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public int getType()
            {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getResourceID() throws Exception
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getPageCount() throws Exception
            {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getName()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getMIMEType() throws Exception
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getMIMEParameter() throws Exception
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public OwObject getInstance() throws Exception
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getID()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getDMSID() throws Exception
            {
                return dmsid;
            }
        };
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        defaulResolvedObjects = new HashMap<String, OwObjectReference>();
        dmsid1 = "ocmis,RID1,ID1";
        dmsid2 = "ocmis,RID1,ID2";
        reference1 = mockObjectReference(dmsid1);
        reference2 = mockObjectReference(dmsid2);

        defaulResolvedObjects.put(dmsid1, reference1);
        defaulResolvedObjects.put(dmsid2, reference2);
    }

    protected OwCMISIdObjectConverter newTestConverter()
    {
        return newTestConverter(defaulResolvedObjects);
    }

    protected OwCMISIdObjectConverter newTestConverter(final Map<String, OwObjectReference> resolvedObjects)
    {
        OwCMISIdObjectConverter converter = new OwCMISIdObjectConverter(null, new OwCMISIDDMSIDConverter(new OwCMISSimpleDMSIDDecoder(), "RID1")) {
            protected OwObjectReference toObjectReference(String dmsid, boolean refresh)
            {
                OwObjectReference reference = resolvedObjects.get(dmsid);
                if (reference == null)
                {
                    reference = new OwStandardUnresolvedReference(null, "Not found", dmsid, null, null, 0);
                }

                return reference;
            };
        };

        return converter;
    }

    public void testFromTo() throws Exception
    {

        OwCMISIdObjectConverter converter = newTestConverter();
        List<String> nativeValues = converter.fromValue(reference1);

        OwObjectReference owdValue = converter.toValue(nativeValues);
        assertSame("Conversion from Value -> Native Value -> Value should yield the same value.", owdValue, reference1);

    }

    public void testArrayFromTo() throws Exception
    {
        OwCMISIdObjectConverter converter = newTestConverter();
        List<String> nativeValues = converter.fromArrayValue(new Object[] { reference1, null, reference2 });

        assertEquals("ID1", nativeValues.get(0));
        assertNull(nativeValues.get(1));
        assertEquals("ID2", nativeValues.get(2));

        OwObjectReference[] owdValues = converter.toArrayValue(nativeValues);
        String message = "Conversion from Value -> Native Value -> Value should yield the same value.";
        assertEquals(message, reference1, owdValues[0]);
        assertEquals(message, null, owdValues[1]);
        assertEquals(message, reference2, owdValues[2]);

    }
}
