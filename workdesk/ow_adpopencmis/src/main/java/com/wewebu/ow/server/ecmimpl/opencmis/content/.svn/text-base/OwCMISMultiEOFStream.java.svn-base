package com.wewebu.ow.server.ecmimpl.opencmis.content;

import java.io.IOException;
import java.io.InputStream;

import org.apache.chemistry.opencmis.commons.impl.Base64;

/**
 *<p>
 * {@link InputStream} wrapper that enables encoding via {@link Base64} of 
 * all {@link InputStream} implementations.
 * It is a workaround for usages of 
 * Mimepull's  DataHead.ReadMultiStream streams that fail in the case of a 
 * second past end of stream read call. This affects cross scenarios since 
 * input streams are based on ECM/OPENCMIS streams.       
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public class OwCMISMultiEOFStream extends InputStream
{
    private InputStream delegate;
    private boolean eofReached = false;

    public OwCMISMultiEOFStream(InputStream delegate)
    {
        super();
        this.delegate = delegate;
    }

    public int read(byte[] b) throws IOException
    {
        return delegate.read(b);
    }

    public boolean equals(Object obj)
    {
        return delegate.equals(obj);
    }

    public synchronized int read(byte[] b, int off, int len) throws IOException
    {
        if (eofReached)
        {
            return -1;
        }

        int readCount = delegate.read(b, off, len);

        if (readCount < 0)
        {
            this.eofReached = true;
        }

        return readCount;
    }

    public long skip(long n) throws IOException
    {
        return delegate.skip(n);
    }

    public int available() throws IOException
    {
        return delegate.available();
    }

    public void close() throws IOException
    {
        delegate.close();
    }

    public void mark(int readlimit)
    {
        delegate.mark(readlimit);
    }

    public void reset() throws IOException
    {
        delegate.reset();
    }

    public boolean markSupported()
    {
        return delegate.markSupported();
    }

    @Override
    public synchronized int read() throws IOException
    {
        if (eofReached)
        {
            return -1;
        }

        int readCount = delegate.read();

        if (readCount < 0)
        {
            this.eofReached = true;
        }

        return readCount;
    }

    public int hashCode()
    {
        return this.delegate.hashCode();
    }
}
