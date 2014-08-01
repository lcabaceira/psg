package com.wewebu.ow.server.ecmimpl.opencmis.server;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

public class OwSearchObjectStoreTest extends TestCase
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwSearchObjectStoreTest.class);

    public void testUnify_1() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("ID", null);
        OwSearchObjectStore os2 = new OwSearchObjectStore(null, "NAME");

        OwSearchObjectStore uos = os1.unify(os2);

        assertEquals("ID", uos.getId());
        assertEquals("NAME", uos.getName());
    }

    public void testUnify_2() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore(null, "NAME");
        OwSearchObjectStore os2 = new OwSearchObjectStore("ID", null);

        OwSearchObjectStore uos = os1.unify(os2);

        assertEquals("ID", uos.getId());
        assertEquals("NAME", uos.getName());
    }

    public void testUnify_3() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("ID", null);
        OwSearchObjectStore os2 = new OwSearchObjectStore("ID", null);

        OwSearchObjectStore uos = os1.unify(os2);

        assertEquals("ID", uos.getId());
        assertNull(uos.getName());
    }

    public void testUnify_4() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore(null, "NAME");
        OwSearchObjectStore os2 = new OwSearchObjectStore(null, "NAME");

        os1.unify(os2);

        assertNull(os1.getId());
        assertEquals("NAME", os1.getName());
        assertNull(os2.getId());
        assertEquals("NAME", os2.getName());
    }

    public void testUnify_5() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("ID1", null);
        OwSearchObjectStore os2 = new OwSearchObjectStore("ID2", null);

        try
        {
            os1.unify(os2);
            fail("Should not be able to unify objectstores of different IDs!");
        }
        catch (OwInvalidOperationException e)
        {
            LOG.debug("Caught expected unifiy exception.", e);
        }
    }

    public void testUnify_6() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore(null, "NAME1");
        OwSearchObjectStore os2 = new OwSearchObjectStore(null, "NAME2");

        try
        {
            os1.unify(os2);
            fail("Should not be able to unify objectstores of different names!");
        }
        catch (OwInvalidOperationException e)
        {
            LOG.debug("Caught expected unifiy exception.", e);
        }
    }

    public void testUnify_7() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("ID1", "NAME1");
        OwSearchObjectStore os2 = new OwSearchObjectStore("ID1", "NAME2");

        try
        {
            os1.unify(os2);
            fail("Should not be able to unify objectstores of different names!");
        }
        catch (OwInvalidOperationException e)
        {
            LOG.debug("Caught expected unifiy exception.", e);
        }
    }

    public void testEquals() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("Id1", "Name1");
        OwSearchObjectStore os2 = new OwSearchObjectStore("Id1", null);
        OwSearchObjectStore os3 = new OwSearchObjectStore(null, "Name1");
        OwSearchObjectStore os4 = new OwSearchObjectStore(null, null);
        OwSearchObjectStore os5 = new OwSearchObjectStore("Id1", "Name2");
        OwSearchObjectStore os6 = new OwSearchObjectStore("Id2", "Name1");
        OwSearchObjectStore os7 = new OwSearchObjectStore("Id1", "Name1");
        OwSearchObjectStore os8 = new OwSearchObjectStore("Id1", null);
        OwSearchObjectStore os9 = new OwSearchObjectStore(null, "Name1");

        assertEquals(os1, os7);
        assertFalse(os1.equals(os2));
        assertFalse(os1.equals(os3));
        assertFalse(os1.equals(os4));
        assertFalse(os1.equals(os5));
        assertFalse(os1.equals(os6));
        assertEquals(os2, os8);
        assertEquals(os3, os9);
    }
}
