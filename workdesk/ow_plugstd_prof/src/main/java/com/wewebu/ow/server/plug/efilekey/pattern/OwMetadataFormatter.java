package com.wewebu.ow.server.plug.efilekey.pattern;

/**
 *<p>
 * Interface for metadata formatter pattern.<br/>
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
 *@since 3.1.0.0
 */
public interface OwMetadataFormatter
{
    /**
     * Get the formatter {@link String} object.
     * @return - the {@link String} object used to format the metadata.
     */
    String getFormatterValue();

}
