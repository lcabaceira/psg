package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.List;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSIDDecoder;

public class OwCMISIDConverterTest extends TestCase
{
    public void testFromTo() throws Exception
    {

        OwCMISIDDMSIDConverter converter = new OwCMISIDDMSIDConverter(new OwCMISSimpleDMSIDDecoder(), "RID1");
        String owdDMSID = "ocmis,RID1,ID";
        List<String> nativeValues = converter.fromValue(owdDMSID);

        String owdValue = converter.toValue(nativeValues);
        assertEquals("Conversion from Value -> Native Value -> Value should yield the same value.", owdDMSID, owdValue);

    }

    public void testArrayFromTo() throws Exception
    {
        String owdDMSID1 = "ocmis,RID1,ID1";
        String owdDMSID2 = "ocmis,RID1,ID2";

        OwCMISIDDMSIDConverter converter = new OwCMISIDDMSIDConverter(new OwCMISSimpleDMSIDDecoder(), "RID1");
        List<String> nativeValues = converter.fromArrayValue(new Object[] { owdDMSID1, null, owdDMSID2 });

        assertEquals("ID1", nativeValues.get(0));
        assertNull(nativeValues.get(1));
        assertEquals("ID2", nativeValues.get(2));

        String[] owdValues = converter.toArrayValue(nativeValues);
        String message = "Conversion from Value -> Native Value -> Value should yield the same value.";
        assertEquals(message, owdDMSID1, owdValues[0]);
        assertEquals(message, null, owdValues[1]);
        assertEquals(message, owdDMSID2, owdValues[2]);

    }
}
