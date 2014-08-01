package com.wewebu.ow.server.ecmimpl.fncm5.content;

import java.io.InputStream;
import java.io.OutputStream;

import com.filenet.api.core.ContentReference;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Specific class for referenced content handling.
 * This class is only returning the content URL,
 * will <b>not</b> retrieve the content from specific location. 
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
public class OwFNCM5ReferenceContentElement extends OwFNCM5ContentElement<ContentReference>
{

    public OwFNCM5ReferenceContentElement(ContentReference contentElement)
    {
        super(contentElement);
    }

    public String getContentURL() throws OwException
    {
        return getNativeObject().get_ContentLocation();
    }

    public int getContentRepresentation() throws OwException
    {
        return OwContentCollection.CONTENT_REPRESENTATION_TYPE_URL;
    }

    public boolean isInputStreamNative()
    {
        return false;
    }

    public InputStream getContentStream(OutputStream out_p) throws OwException
    {
        throw new OwInvalidOperationException("Cannot retrieve content since it is an referential content element");
    }

}
