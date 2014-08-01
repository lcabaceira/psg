package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.InputRepresentation;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessImageResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Implementation of link {@link OwContentElement} to be used with alfresco BPM work items.
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
 *@since 4.2.0.0
 */
public class OwAlfrescoBPMContentElement implements OwContentElement
{
    private File file;
    private MediaType mediaType;

    public OwAlfrescoBPMContentElement(ProcessImageResource resource) throws IOException, OwException
    {
        this.file = File.createTempFile("alfContent", ".png");
        this.file.deleteOnExit();

        new RestCallTemplate<ProcessImageResource, Void>() {

            @Override
            protected Void execWith(ProcessImageResource resource) throws OwException
            {
                try
                {
                    InputRepresentation obj = resource.getImage();

                    OwAlfrescoBPMContentElement.this.mediaType = obj.getMediaType();
                    OutputStream outputStream = new FileOutputStream(file);
                    try
                    {
                        obj.write(outputStream);
                    }
                    finally
                    {
                        outputStream.close();
                    }

                    return null;
                }
                catch (IOException ex)
                {
                    throw new OwServerException("Could not make local copy of workflow image.", ex);
                }
            }
        }.doCall(resource);

    }

    @Override
    public String getContentFilePath() throws Exception
    {
        return this.file.getAbsolutePath();
    }

    @Override
    public String getContentURL() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getContentObject() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getContentRepresentation() throws Exception
    {
        return OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    @Override
    public boolean isInputStreamNative()
    {

        return true;
    }

    @Override
    public InputStream getContentStream(OutputStream out_p) throws Exception
    {
        if (out_p == null)
        {
            return new FileInputStream(this.file);
        }
        else
        {
            FileInputStream in = new FileInputStream(this.file);
            IOUtils.copy(in, out_p);

        }
        return null;

    }

    @Override
    public int getPageNumber(int lPageIndex_p) throws Exception
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getMIMEType() throws Exception
    {

        return this.mediaType.toString();
    }

    @Override
    public String getMIMEParameter() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void releaseResources()
    {
        this.file.delete();

    }

}
