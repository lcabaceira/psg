package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception thrown, when a error occurred in the configuration or setup of the framework.
 * Mainly in the bootstrap and plugins XML files.
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
public class OwConfigurationException extends OwException
{
    /**
     * 
     */
    private static final long serialVersionUID = -1331915657346710670L;

    /** construct a exception with a message */
    public OwConfigurationException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwConfigurationException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwConfigurationException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwConfigurationException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** get the name of the module / manager / plugin where the exception occurred */
    public String getModulName()
    {
        return "OECM";
    }
}