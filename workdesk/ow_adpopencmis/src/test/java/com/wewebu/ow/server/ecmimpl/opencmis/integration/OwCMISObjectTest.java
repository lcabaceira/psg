package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;

public abstract class OwCMISObjectTest extends OwCMISIntegrationTest
{

    /**
     * 
     */
    static final Logger LOG = Logger.getLogger(OwCMISIntegrationTest.class);

    public OwCMISObjectTest(String name_p)
    {
        super(name_p);
    }

    public OwCMISObjectTest(String sessionName, String name)
    {
        super(sessionName, name);
    }

    @Override
    protected OwCMISTestFixture createFixture()
    {
        return new OwCMISIntegrationFixture(this);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    protected void assertSameStream(InputStream expected, InputStream actual) throws IOException
    {
        final int bufferLength = 64;
        byte[] expectedBuffer = new byte[bufferLength];
        byte[] actualBuffer = new byte[bufferLength];
        int expectedRead;
        while ((expectedRead = expected.read(expectedBuffer)) != -1)
        {

            int actualCumulatedRead = 0;
            int actualRead = 0;
            int off = 0;
            int toRead = expectedRead;

            while ((actualRead = actual.read(actualBuffer, off, toRead)) != -1)
            {
                actualCumulatedRead += actualRead;
                if (actualRead < toRead)
                {
                    off = actualRead;
                    toRead = toRead - actualRead;
                }
                else
                {
                    break;
                }
            }

            if (actualCumulatedRead != expectedRead)
            {
                fail("Diffrenet stream content. Actual has less bytes that expected.");
            }
            else
            {
                if (!Arrays.equals(expectedBuffer, actualBuffer))
                {
                    fail("Diffrenet stream content.");
                }

            }

        }

        if (actual.read() != -1)
        {
            fail("Diffrenet stream content. Expected has less bytes that actual.");
        }
    }

}
