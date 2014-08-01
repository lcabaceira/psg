package com.wewebu.ow.server.ecmimpl.fncm5.content;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.ContentElement;
import com.filenet.api.property.Properties;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Base class for P8 5.x API handling.
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
public abstract class OwFNCM5ContentElement<C extends ContentElement> implements OwContentElement
{
    public static final String FILE_NAME_MIME_PARAMETER = "filename";

    private C elem;

    public OwFNCM5ContentElement(C contentElement)
    {
        elem = contentElement;
    }

    public Object getContentObject() throws OwException
    {
        return null;
    }

    public int getPageNumber(int lPageIndex_p) throws OwException
    {
        return elem.get_ElementSequenceNumber().intValue();
    }

    public String getMIMEType() throws OwException
    {
        Properties properties = elem.getProperties();
        if (properties.isPropertyPresent(PropertyNames.CONTENT_TYPE))
        {
            return elem.get_ContentType() != null ? elem.get_ContentType() : "";
        }
        else
        {
            return "";
        }
    }

    public String getMIMEParameter() throws OwException
    {
        return "";
    }

    public void releaseResources()
    {
        // TODO Auto-generated method stub
    }

    protected C getNativeObject()
    {
        return elem;
    }

    public String getContentFilePath() throws OwException
    {
        return null;
    }

    public String getContentURL() throws OwException
    {
        return null;
    }
}
