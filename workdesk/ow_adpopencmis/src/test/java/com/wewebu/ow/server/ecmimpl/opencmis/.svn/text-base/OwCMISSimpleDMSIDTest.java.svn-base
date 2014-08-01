package com.wewebu.ow.server.ecmimpl.opencmis;

import junit.framework.TestCase;

public class OwCMISSimpleDMSIDTest extends TestCase
{

    public void testEscaping()
    {
        String toBeEscaped = "1234/4567/8888";
        OwCMISSimpleDMSID dmsid = new OwCMISSimpleDMSID("none", "repo", toBeEscaped);

        assertEquals("repo", dmsid.getResourceID());

        assertEquals("none,repo,1234|4567|8888", dmsid.getDMSIDString());

        assertEquals(toBeEscaped, dmsid.getCMISID());
    }

    public void testHashCode()
    {
        assertTrue(new OwCMISSimpleDMSID("none", "repo", "0000").hashCode() > 31);
        //null values are handled for hashCode
        assertTrue(new OwCMISSimpleDMSID(null, "repo", "0000").hashCode() > 31);
        assertTrue(new OwCMISSimpleDMSID(null, null, "0000").hashCode() > 31);
    }

    public void testEquals()
    {
        OwCMISSimpleDMSID dmsid = new OwCMISSimpleDMSID("none", "repo", "1234/4567/8888");

        assertTrue(dmsid.equals(dmsid));
        assertTrue(dmsid.equals(new OwCMISSimpleDMSID("none", "repo", "1234/4567/8888")));

        assertFalse(dmsid.equals(null));
        assertFalse(dmsid.equals(new OwCMISSimpleDMSID("otherPrefix", "repo", "1234/4567/8888")));
        assertFalse(dmsid.equals(new OwCMISSimpleDMSID("none", "otherRepo", "1234/4567/8888")));
        assertFalse(dmsid.equals(new OwCMISSimpleDMSID("none", "repo", "8888/7654/4321")));
        assertFalse(dmsid.equals(new OwCMISSimpleDMSID("otherPrefix", null, "1234/4567/8888")));
        assertFalse(dmsid.equals(new OwCMISSimpleDMSID(null, "repo", "1234/4567/8888")));
        assertFalse(new OwCMISSimpleDMSID(null, "repo", "1234/4567/8888").equals(dmsid));
        assertFalse(new OwCMISSimpleDMSID(null, null, "1234/4567/8888").equals(dmsid));
    }

    public void testExceptionNullpointer()
    {
        try
        {
            new OwCMISSimpleDMSID("none", "repo", null);
            fail("Null value not allowed for object Id");
        }
        catch (NullPointerException nullEx)
        {

        }
    }
}
