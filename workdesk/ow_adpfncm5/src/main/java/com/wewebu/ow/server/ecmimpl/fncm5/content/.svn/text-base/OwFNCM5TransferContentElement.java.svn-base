package com.wewebu.ow.server.ecmimpl.fncm5.content;

import java.io.InputStream;
import java.io.OutputStream;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.property.Properties;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwStreamUtil;

/**
 *<p>
 * Base implementation for standard stream based content handling.
 * This class will work with the native API to retrieve
 * content element information and also the stream itself.
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
public class OwFNCM5TransferContentElement extends OwFNCM5ContentElement<ContentTransfer>
{

    public OwFNCM5TransferContentElement(ContentTransfer contentElement)
    {
        super(contentElement);
    }

    public int getContentRepresentation() throws OwException
    {
        return OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    public boolean isInputStreamNative()
    {
        return true;
    }

    public InputStream getContentStream(OutputStream out_p) throws OwException
    {
        if (out_p == null)
        {
            return getNativeObject().accessContentStream();
        }
        else
        {
            InputStream stream = getNativeObject().accessContentStream();
            try
            {
                OwStreamUtil.upload(stream, out_p, true, 1024);
            }
            catch (Exception e)
            {
                throw new OwServerException("Could not redirect input- to output-stream", e);
            }
            return null;
        }
    }

    @Override
    public String getMIMEParameter() throws OwException
    {
        ContentTransfer contentTrasfer = getNativeObject();
        Properties properties = contentTrasfer.getProperties();
        String name = null;

        if (properties.isPropertyPresent(PropertyNames.RETRIEVAL_NAME))
        {
            name = contentTrasfer.get_RetrievalName();
        }
        if (name != null)
        {
            return FILE_NAME_MIME_PARAMETER + "=\"" + name + "\"";
        }
        else
        {
            return super.getMIMEParameter();
        }
    }

}
