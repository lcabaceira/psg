package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import com.wewebu.ow.server.exceptions.OwRuntimeException;

/**
 *<p>
 * Signals an exception in the REST layer.
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
 *@since 4.0.0.0
 */
public class OwRestException extends OwRuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     * @param cause
     */
    public OwRestException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message
     */
    public OwRestException(String message)
    {
        super(message);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.exceptions.OwRuntimeException#getModulName()
     */
    @Override
    public String getModulName()
    {
        return "ow.adp.alfrescobpm.rest";
    }
}
