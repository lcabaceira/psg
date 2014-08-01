package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.text.Format;
import java.text.ParseException;
import java.util.Locale;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * Simple {@link OwFormat} implementation.
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
public class OwAlfrescoBPMFormat implements OwFormat
{
    private Format textFormat;

    public OwAlfrescoBPMFormat(Format textForma)
    {
        this.textFormat = textForma;
    }

    public Object parse(String text) throws OwException
    {
        try
        {
            return this.textFormat.parseObject(text);
        }
        catch (ParseException e)
        {
            throw new OwServerException("Could not parse value: " + text, e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFormat#ignoreTime()
     */
    public boolean ignoreTime()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFormat#ignoreDate()
     */
    public boolean ignoreDate()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFormat#getTextFormat(int)
     */
    public Format getTextFormat(int iFieldProviderType_p)
    {
        return this.textFormat;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFormat#canValidate()
     */
    public boolean canValidate()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFormat#validate(int, java.lang.Object, java.util.Locale)
     */
    public String validate(int iFieldProviderType_p, Object object_p, Locale locale_p) throws OwInvalidOperationException
    {
        return null;
    }
}
