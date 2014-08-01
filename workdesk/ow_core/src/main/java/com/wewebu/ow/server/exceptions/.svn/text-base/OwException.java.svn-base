package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Base class for all specialized Exception in OECM Framework.
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
public abstract class OwException extends Exception
{
    private static final long serialVersionUID = -125326514684507332L;

    private OwString m_localizeablemessage;

    /** construct a exception with a message */
    public OwException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and cause*/
    public OwException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message */
    public OwException(OwString strMessage_p)
    {
        super(strMessage_p.getDefaultDisplayName());
        m_localizeablemessage = strMessage_p;
    }

    /** construct a exception with a message and cause*/
    public OwException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p.getDefaultDisplayName(), cause_p);
        m_localizeablemessage = strMessage_p;
    }

    public String getMessage(java.util.Locale locale_p)
    {
        if (m_localizeablemessage != null)
        {
            return m_localizeablemessage.getString(locale_p);
        }
        else
        {
            return getLocalizedMessage();
        }
    }

    /**
     * @return the m_localizeablemessage
     */
    public OwString getLocalizeablemessage()
    {
        return m_localizeablemessage;
    }

    /** get the name of the module / manager / plugin where the exception occurred */
    public abstract String getModulName();
}