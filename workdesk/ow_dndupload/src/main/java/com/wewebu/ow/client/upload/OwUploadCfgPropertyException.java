package com.wewebu.ow.client.upload;

/**
 *<p>
 * Signals that one of the configuration properties is not correctly configured.
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
public class OwUploadCfgPropertyException extends OwUploadCfgException
{

    private String propertyName;

    /**
     * @param propertyName_p The name of the configuration parameter/property which is not correctly configured.
     * @param message_p the message of the exception.
     */
    public OwUploadCfgPropertyException(String propertyName_p, String message_p)
    {
        super(message_p);
        this.propertyName = propertyName_p;
    }

    /**
     * @return The name of the configuration parameter/property which is not correctly set.
     */
    public String getPropertyName()
    {
        return propertyName;
    }
}
