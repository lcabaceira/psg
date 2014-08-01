package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Base class for all specialized unchecked Exception in OECM Framework.
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
public abstract class OwRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 6663085131670270565L;

    /** the message as localizable OwString or null, if message is only given as Strong */
    private OwString m_localizeableMessage;

    /** 
     * Create a new <code>OwRuntimeException</code> with a non-localizable message.
     * 
     * @param message_p the non-localizable message
     */
    public OwRuntimeException(String message_p)
    {
        super(message_p);
    }

    /** 
     * Create a new <code>OwRuntimeException</code> with a non-localizable message and a cause.
     * 
     * @param message_p the non-localizable message
     * @param cause_p the cause of this Exception
     */
    public OwRuntimeException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    /** 
     * Create a new <code>OwRuntimeException</code> with a localizable message.
     * 
     * @param message_p the localizable message
     */
    public OwRuntimeException(OwString message_p)
    {
        super(message_p.getDefaultDisplayName());
        m_localizeableMessage = message_p;
    }

    /** 
     * Create a new <code>OwRuntimeException</code> with a localizable message and a cause.
     * 
     * @param message_p the localizable message
     * @param cause_p the cause of this Exception
     */
    public OwRuntimeException(OwString message_p, Throwable cause_p)
    {
        super(message_p.getDefaultDisplayName(), cause_p);
        m_localizeableMessage = message_p;
    }

    /**
     * Get the localized message of this <code>OwRuntimeException</code>.
     * 
     * @param locale_p the Locale to localize this Exception for.
     *
     * @return the localized message if available or the unlocalized message if no localization is available.
     */
    public String getMessage(java.util.Locale locale_p)
    {
        if (m_localizeableMessage != null)
        {
            return m_localizeableMessage.getString(locale_p);
        }
        else
        {
            return getLocalizedMessage();
        }
    }

    /**
     * Get the name of the module / manager / plugin where the exception occured.
     * 
     * @return the name of the module / manager / plugin where the exception occured.
     */
    public abstract String getModulName();

}