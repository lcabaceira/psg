package com.wewebu.ow.client.zidilauncher.winregistry;

/**
 *<p>
 * RegistryNotAvailableException.
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
public class RegistryNotAvailableException extends Exception
{

    private static final long serialVersionUID = -8422376689167348981L;

    public RegistryNotAvailableException()
    {
        super();
    }

    public RegistryNotAvailableException(String message)
    {
        super(message);
    }

    public RegistryNotAvailableException(Throwable cause)
    {
        super(cause);
    }

    public RegistryNotAvailableException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
