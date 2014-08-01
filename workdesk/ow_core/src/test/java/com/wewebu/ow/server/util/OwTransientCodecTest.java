package com.wewebu.ow.server.util;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.paramcodec.OwCookieStoreCodec;
import com.wewebu.ow.server.util.paramcodec.OwParameterMap;
import com.wewebu.ow.server.util.paramcodec.OwTransientCodec;

public class OwTransientCodecTest extends TestCase
{
    private static final Logger LOG = Logger.getLogger(OwTransientCodecTest.class);

    private OwTransientCodec m_codec;

    protected final void setUp() throws Exception
    {
        super.setUp();
        m_codec = new OwTransientCodec(OwCookieStoreCodec.DEFAULT_URL_PARAMETER_NAME, 2000);
    }

    public void testEncodeDecode() throws Exception
    {
        OwParameterMap aMap1 = new OwParameterMap();
        aMap1.setParameter("P1", "P1V1");
        aMap1.setParameter("P1", "P1V2");
        aMap1.setParameter("P2", "P1V1");

        OwParameterMap aMap2 = new OwParameterMap();
        aMap2.setParameter("a", "a1");
        aMap2.setParameter("b", "b1");
        aMap2.setParameter("ccc", "ccc1");

        OwParameterMap encodedMap1 = m_codec.encode(aMap1);
        OwParameterMap encodedMap2 = m_codec.encode(aMap2);

        LOG.debug("OwTransientCodecTest.testEncodeDecode(): encoded query string #1 = \"" + encodedMap1.toRequestQueryString() + "\"");
        LOG.debug("OwTransientCodecTest.testEncodeDecode(): encoded query string #2 = \"" + encodedMap2.toRequestQueryString() + "\"");

        OwParameterMap decodedMap1 = m_codec.decode(encodedMap1, false);
        OwParameterMap decodedMap2 = m_codec.decode(encodedMap2, true);

        assertEquals(aMap1, decodedMap1);
        assertNotSame(aMap2, decodedMap2);

        aMap1.setParameter("P1", "P1Ex");

        assertNotSame(aMap1, decodedMap1);
    }

    public void testExpiredCookie() throws Exception
    {
        OwParameterMap aMap1 = new OwParameterMap();
        aMap1.setParameter("P1", "P1V1");

        OwParameterMap encodedMap1 = m_codec.encode(aMap1);
        try
        {
            Thread.sleep(2100);
        }
        catch (InterruptedException e)
        {
            LOG.debug("OwTransientCodecTest.testExpiredCookie(): sleep interrup. Test skipped! ", e);
        }

        try
        {
            m_codec.decode(encodedMap1, true);
            fail("Should not reach this point! The encoded map cookie shoukd have expired by now");
        }
        catch (OwException e)
        {
            LOG.debug("OwTransientCodecTest.testExpiredCookie(): caught expired cookie exception ", e);
        }
    }
}
