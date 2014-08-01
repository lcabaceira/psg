package com.wewebu.ow.server.ui.preview;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface for internal renderer of Integrated preview handler. 
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
public interface OwPreviewRenderer
{
    /**
     * Called if this instance is matching the rendering requirements. 
     * @param out Writer
     * @param config Map of String's defining configuration
     * @param prevContext OwPreviewContext current preview context
     * @throws OwException if configuration or preview context does not match
     * @throws IOException if cannot use writer for rendering
     */
    void render(Writer out, Map<String, String> config, OwPreviewContext prevContext) throws OwException, IOException;

}
