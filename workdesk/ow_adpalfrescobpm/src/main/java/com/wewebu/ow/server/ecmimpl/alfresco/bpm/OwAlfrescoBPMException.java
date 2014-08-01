package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.exceptions.OwRuntimeException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Runtime exception to be used in the Alfresco BPM adaptor.
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
@SuppressWarnings("serial")
public class OwAlfrescoBPMException extends OwRuntimeException
{

    public OwAlfrescoBPMException(OwString message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    public OwAlfrescoBPMException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.exceptions.OwRuntimeException#getModulName()
     */
    @Override
    public String getModulName()
    {
        return "ow.adp.alfrescobpm";
    }
}
