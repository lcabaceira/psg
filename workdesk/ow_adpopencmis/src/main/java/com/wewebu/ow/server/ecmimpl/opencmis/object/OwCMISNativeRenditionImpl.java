package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.io.InputStream;

import org.apache.chemistry.opencmis.client.api.Rendition;

/**
 *<p>
 * Open CMIS {@link Rendition} adaptation.  
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
public class OwCMISNativeRenditionImpl implements OwCMISRendition
{
    private Rendition rendition;

    public OwCMISNativeRenditionImpl(Rendition rendition)
    {
        super();
        this.rendition = rendition;
    }

    @Override
    public String getContentMIMEType()
    {
        return rendition.getContentStream().getMimeType();
    }

    @Override
    public InputStream getContentStream()
    {
        return rendition.getContentStream().getStream();
    }

    @Override
    public String getType()
    {
        return rendition.getKind();
    }

}
