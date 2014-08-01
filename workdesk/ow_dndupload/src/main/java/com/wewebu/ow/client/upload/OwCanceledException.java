package com.wewebu.ow.client.upload;

/**
 *<p>
 * Use this exception to signal a canceled task.
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
public class OwCanceledException extends RuntimeException
{

    public OwCanceledException(String message_p)
    {
        super(message_p);
    }

}
