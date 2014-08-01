package com.wewebu.ow.server.ui.preview;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Renderer in case no preview is available.
 * Implements the OwPreviewRenderer interface by writing
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
public class OwNoPreviewRenderer implements OwPreviewRenderer
{
    public static final OwNoPreviewRenderer INSTANCE = new OwNoPreviewRenderer();

    /** instance of the MIME manager used to open the objects     */
    protected OwMimeManager m_MimeManager = new OwMimeManager();

    @Override
    public void render(Writer out, Map<String, String> configuration, OwPreviewContext prevContext) throws OwException, IOException
    {
        m_MimeManager.reset();
        OwMainAppContext context = (OwMainAppContext) prevContext.getAppContext();

        try
        {
            m_MimeManager.attach(context, null);
            m_MimeManager.reset();
            String div = "<div class=\"message_download\">" + prevContext.getAppContext().localize("OwNoPreviewRenderer.text", "This document cannot be previewed.") + "<br>";
            out.write(div);
            if (prevContext.getObject().hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                m_MimeManager.insertHtmlLink(out, prevContext.getAppContext().localize("OwNoPreviewRenderer.textdownload", "Click here to download document."), prevContext.getObject(), null);
            }
            out.write("</div>");

        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Error rendering no-preview.", e);
        }

    }

}
