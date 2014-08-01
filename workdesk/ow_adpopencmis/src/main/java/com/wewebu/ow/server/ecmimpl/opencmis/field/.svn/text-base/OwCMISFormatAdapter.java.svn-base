package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.text.Format;
import java.util.Locale;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * OwCMISFormatAdapter.
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
public class OwCMISFormatAdapter implements OwCMISFormat
{
    private OwFormat format;

    public OwCMISFormatAdapter(OwFormat format_p)
    {
        super();
        this.format = format_p;
    }

    public boolean ignoreTime()
    {
        return format.ignoreTime();
    }

    public boolean ignoreDate()
    {
        return format.ignoreDate();
    }

    public Format getTextFormat(int iFieldProviderType_p)
    {
        return format.getTextFormat(iFieldProviderType_p);
    }

    public boolean canValidate()
    {
        return format.canValidate();
    }

    public String validate(int iFieldProviderType_p, Object object_p, Locale locale_p) throws OwInvalidOperationException
    {
        return format.validate(iFieldProviderType_p, object_p, locale_p);
    }

    @Override
    public Object parse(String text_p, boolean asArray_p) throws OwException
    {
        throw new OwNotSupportedException("Parse not supported.");
    }

}
