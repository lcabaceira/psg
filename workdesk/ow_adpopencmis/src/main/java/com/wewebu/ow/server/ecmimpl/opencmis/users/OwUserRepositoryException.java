package com.wewebu.ow.server.ecmimpl.opencmis.users;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Signals an exception in carring out a {@link OwUserRepositoryException} operation.
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
 *@since 4.1.1.0
 */
@SuppressWarnings("serial")
public class OwUserRepositoryException extends OwException
{

    /**
     * @param strMessage_p
     * @param cause_p
     */
    public OwUserRepositoryException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param strMessage_p
     * @param cause_p
     */
    public OwUserRepositoryException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /**
     * @param th
     */
    public OwUserRepositoryException(Throwable th)
    {
        super(th.getMessage(), th);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.exceptions.OwException#getModulName()
     */
    @Override
    public String getModulName()
    {
        return "UserRepository";
    }

}
