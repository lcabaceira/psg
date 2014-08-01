package com.wewebu.ow.server.ecmimpl.opencmis.content;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwMimeTypes;
import com.wewebu.ow.server.util.OwStreamUtil;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * CMIS content element implementation.
 * Wrapping of native <code>org.apache.chemistry.opencmis.commons.data.ContentStream</code> interface.
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
public class OwCMISContentElement implements OwContentElement
{
    private static final Logger LOG = OwLog.getLogger(OwCMISContentElement.class);
    private ContentStream stream;
    private int idx;

    public OwCMISContentElement(ContentStream stream)
    {
        this(stream, 0);
    }

    public OwCMISContentElement(ContentStream stream, int pageNr)
    {
        this.stream = stream;
        this.idx = pageNr;
    }

    @Override
    public String getContentFilePath() throws OwException
    {
        return null;
    }

    @Override
    public String getContentURL() throws OwException
    {
        return "";
    }

    @Override
    public Object getContentObject() throws OwException
    {
        return null;
    }

    @Override
    public int getContentRepresentation() throws OwException
    {
        return OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    @Override
    public boolean isInputStreamNative()
    {
        return true;
    }

    @Override
    public InputStream getContentStream(OutputStream out_p) throws OwException
    {
        try
        {
            if (out_p == null)
            {
                InputStream theStream = stream.getStream();
                return new OwCMISMultiEOFStream(theStream);
            }
            else
            {
                InputStream source = stream.getStream();
                OwStreamUtil.upload(source, out_p, false);
                out_p.flush();

                source.close();
                return null;
            }
        }
        catch (Exception e)
        {
            LOG.error("Could not redirect stream to given output stream", e);
            throw new OwServerException(new OwString("opencmis.OwCMISContentElement.getContentStream.err.redirect", "Could not redirect stream to given output stream."), e);
        }
    }

    @Override
    public int getPageNumber(int lPageIndex_p) throws OwException
    {
        return this.idx;
    }

    @Override
    public String getMIMEType() throws OwException
    {
        String mime = getNativeObject().getMimeType();
        if (mime != null)
        {
            return mime;
        }
        else
        {
            if (getNativeObject().getFileName() != null)
            {
                int idx = getNativeObject().getFileName().lastIndexOf('.');
                if (idx > 0)
                {
                    mime = OwMimeTypes.getMimeTypeFromExtension(getNativeObject().getFileName().substring(idx + 1));
                    if (mime != null)
                    {
                        return mime;
                    }
                }
            }
            return "application/octet-stream";
        }
    }

    @Override
    public String getMIMEParameter() throws OwException
    {
        String name = getNativeObject().getFileName();
        if (name != null)
        {
            return "filename=\"" + name + "\"";
        }
        else
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwCMISContentElement.getMIMEParameter: No file name defined, returning empty string.");
            }
            return "";
        }
    }

    @Override
    public void releaseResources()
    {
        this.stream = null;
    }

    /**
     * Return the native object,
     * which is of type ContentStream
     * @return ContentStream
     */
    protected ContentStream getNativeObject()
    {
        return this.stream;
    }

}
