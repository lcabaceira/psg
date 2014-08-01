package com.wewebu.ow.server.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.paramcodec.OwAlphabetCoder;

public class OwAlphabetCoderTest extends TestCase
{
    private static final Logger LOG = Logger.getLogger(OwAlphabetCoderTest.class);

    private OwAlphabetCoder m_alphabetCoder;

    protected void setUp() throws Exception
    {
        super.setUp();
        m_alphabetCoder = new OwAlphabetCoder();
    }

    public void testCodeRandom() throws Exception
    {
        assertEquals("a", m_alphabetCoder.encode(0));
        int range = 1000;
        Random random = new Random(System.currentTimeMillis());
        long randomLong = Math.abs(random.nextLong());
        List values = new ArrayList();
        for (long l = randomLong; l < Long.MAX_VALUE && l < range + randomLong; l++)
        {
            String encodedL = m_alphabetCoder.encode(l);
            int index = values.indexOf(encodedL);
            if (index != -1)
            {
                fail("Non unique encoded long : " + l + " was generated before for " + (l - (values.size() - index)));
            }
        }
    }

    public void testCodeNegative()
    {
        try
        {
            m_alphabetCoder.encode(-100);
            fail("Shouold not be able to encode negative values!");
        }
        catch (OwInvalidOperationException e)
        {
            LOG.debug("OwAlphabetCoderTest.testCodeNegative():caught expected zero value exception !", e);
        }
    }
}
