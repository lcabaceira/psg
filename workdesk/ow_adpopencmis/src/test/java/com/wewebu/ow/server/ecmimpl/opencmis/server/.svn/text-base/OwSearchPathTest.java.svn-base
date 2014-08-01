package com.wewebu.ow.server.ecmimpl.opencmis.server;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

public class OwSearchPathTest extends TestCase
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwSearchPathTest.class);

    public void testCreate() throws Exception
    {
        try
        {
            new OwSearchPath(null);
            fail("Should not be able to create object store reference path with no object store!");
        }
        catch (OwInvalidOperationException e)
        {
            LOG.debug("Caught expected create exception.", e);
        }

        try
        {
            new OwSearchPath(null, null, false, null);
            fail("Should not be able to create path with no id and no path name!");
        }
        catch (OwInvalidOperationException e)
        {
            LOG.debug("Caught expected create exception.", e);
        }
    }

    public void testNormalize() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("oid1", "osn1");
        OwSearchPath p1 = new OwSearchPath("Id1", "\\pn1\\pn2", false, os1);
        assertEquals("/pn1/pn2", p1.getPathName());

    }

    public void testEquals() throws Exception
    {
        OwSearchObjectStore os1 = new OwSearchObjectStore("oid1", "osn1");
        OwSearchObjectStore os2 = new OwSearchObjectStore("oid1", "osn1");
        OwSearchObjectStore os3 = new OwSearchObjectStore("oid1", "osn3");
        OwSearchPath p1 = new OwSearchPath("Id1", "Pn1", false, os1);
        OwSearchPath p2 = new OwSearchPath("Id1", "Pn1", false, os2);
        OwSearchPath p3 = new OwSearchPath("Id1", "Pn1", false, os3);
        OwSearchPath p4 = new OwSearchPath("Id1", "Pn1", true, os3);
        OwSearchPath p5 = new OwSearchPath("Id1", "Pn2", false, os3);
        OwSearchPath p6 = new OwSearchPath("Id2", "Pn1", false, os3);
        OwSearchPath p7 = new OwSearchPath("Id1", null, false, os1);
        OwSearchPath p8 = new OwSearchPath("Id1", null, false, os1);
        OwSearchPath p9 = new OwSearchPath(null, "Pn1", false, os3);
        OwSearchPath p10 = new OwSearchPath(null, "Pn1", false, os3);

        assertEquals(p1, p2);
        assertFalse(p1.equals(p3));
        assertFalse(p3.equals(p4));
        assertFalse(p3.equals(p5));
        assertFalse(p3.equals(p6));
        assertEquals(p7, p8);
        assertEquals(p9, p10);
    }
}
