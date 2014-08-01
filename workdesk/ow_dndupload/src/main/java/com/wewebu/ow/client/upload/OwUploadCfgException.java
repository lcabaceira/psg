package com.wewebu.ow.client.upload;

/**
 *<p>
 * Signals a general error in the properties file used for configuration.
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
 *@since 3.2.0.0
 */
@SuppressWarnings("serial")
public class OwUploadCfgException extends OwDNDException
{

    public OwUploadCfgException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    public OwUploadCfgException(String message_p)
    {
        super(message_p);
    }
}
