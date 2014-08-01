package com.wewebu.ow.clientservices.exception;

import java.io.UnsupportedEncodingException;

/**
 *<p>
 * Signals a decoding error.
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
@SuppressWarnings("serial")
public class OwDecodeException extends Exception
{

    /**
     * @param reason_p
     */
    public OwDecodeException(String reason_p)
    {
        super(reason_p);
    }

    /**
     * @param cause_p
     */
    public OwDecodeException(UnsupportedEncodingException cause_p)
    {
        super(cause_p);
    }
}
