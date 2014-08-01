package com.wewebu.ow.client.upload;

/**
 *<p>
 * Use this Exception to signal any errors that appear during the upload process.
 * This is a kind of umbrella exception. Feel free to add more fine grained exceptions, 
 * by extending this one, if needed.
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
public class OwDNDException extends Exception
{
    public OwDNDException(String message_p)
    {
        super(message_p);
    }

    public OwDNDException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }
}
