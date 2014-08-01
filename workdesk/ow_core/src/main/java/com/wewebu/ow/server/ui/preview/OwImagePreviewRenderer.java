package com.wewebu.ow.server.ui.preview;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInaccessibleException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Preview Renderer for image MIME types.
 * Will simply render the object as HTML <code><b>img</b> tag</code>. 
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
public class OwImagePreviewRenderer implements OwPreviewRenderer
{

    @Override
    public void render(Writer writer, Map<String, String> configuration, OwPreviewContext prevContext) throws OwException, IOException
    {
        String src = configuration.get("url");
        src = OwString.replaceAll(src, "{baseurl}", prevContext.getAppContext().getBaseURL());
        try
        {
            src = OwString.replaceAll(src, "{dmsid}", OwAppContext.encodeURL(prevContext.getObject().getDMSID()));
        }
        catch (Exception e)
        {
            throw new OwInaccessibleException(prevContext.getAppContext().localize("OwImagePreviewRenderer.render.err.dmsid", "Could not access DMSID."), e);
        }

        writer.write("<img src=\"");
        writer.write(src);
        writer.write("\" alt=\"");
        writer.write(prevContext.getObject().getName());
        writer.write("\" >");

    }

}
