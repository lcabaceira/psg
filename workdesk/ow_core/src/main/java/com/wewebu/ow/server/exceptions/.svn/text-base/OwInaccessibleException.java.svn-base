package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Special exception for runtime exceptions.
 * Should be thrown only if the requested resource, data, content etc...
 * is not accessible, or access method is throwing exception. 
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
public class OwInaccessibleException extends OwRuntimeException
{
    /**generated serial Version UID*/
    private static final long serialVersionUID = -1230492636787751173L;

    private String modulName;

    public OwInaccessibleException(OwString message)
    {
        super(message);
    }

    public OwInaccessibleException(OwString message, String moduleName)
    {
        super(message);
        this.modulName = moduleName;
    }

    public OwInaccessibleException(OwString message, Throwable throwable)
    {
        super(message, throwable);
    }

    public OwInaccessibleException(OwString message, Throwable throwable, String moduleName)
    {
        super(message, throwable);
        this.modulName = moduleName;
    }

    public OwInaccessibleException(String message)
    {
        super(message);
    }

    public OwInaccessibleException(String message, String moduleName)
    {
        super(message);
        this.modulName = moduleName;
    }

    public OwInaccessibleException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    public OwInaccessibleException(String message, Throwable throwable, String moduleName)
    {
        super(message, throwable);
        this.modulName = moduleName;
    }

    @Override
    public String getModulName()
    {
        return modulName;
    }

    /**
     * Set the module name, where Exception was thrown. 
     * @param moduleName String
     */
    public void setModulName(String moduleName)
    {
        this.modulName = moduleName;
    }
}
