package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.lang.reflect.Array;
import java.text.Format;
import java.text.ParseException;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.fieldctrlimpl.OwRelativeDate;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * OwCMISNativeFormat.
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
public class OwCMISJavaTextFormat implements OwCMISFormat
{
    private static final Logger LOG = OwLog.getLogger(OwCMISJavaTextFormat.class);

    private static final String DEFAULT_ARRAY_SEPARATOR_REGEXP = ",";
    private Format defaultTextFormat;
    private Class<?> valueClass;
    private String arraySeparatorRegexp = DEFAULT_ARRAY_SEPARATOR_REGEXP;

    public OwCMISJavaTextFormat(Format textFormat_p, Class<?> valueClass_p)
    {
        this(textFormat_p, valueClass_p, DEFAULT_ARRAY_SEPARATOR_REGEXP);
    }

    public OwCMISJavaTextFormat(Format textFormat_p, Class<?> valueClass_p, String arraySeparatorRegexp_p)
    {
        super();
        this.defaultTextFormat = textFormat_p;
        this.valueClass = valueClass_p;
        this.arraySeparatorRegexp = arraySeparatorRegexp_p;
    }

    public boolean canValidate()
    {
        return false;
    }

    public Format getTextFormat(int fieldProviderType_p)
    {
        return this.defaultTextFormat;
    }

    public boolean ignoreDate()
    {
        return false;
    }

    public boolean ignoreTime()
    {
        return false;
    }

    public String validate(int fieldProviderType_p, Object object_p, Locale locale_p) throws OwInvalidOperationException
    {
        return null;
    }

    protected String[] splitArray(String arrayText_p) throws OwException
    {
        if (arrayText_p != null)
        {
            String[] elements = arrayText_p.split(this.arraySeparatorRegexp);
            return elements;
        }
        else
        {
            return null;
        }
    }

    protected Object parseSingleString(String valueString_p) throws OwInvalidOperationException
    {
        try
        {
            return this.defaultTextFormat.parseObject(valueString_p);
        }
        catch (ParseException e)
        {
            OwRelativeDate relativeDate = OwRelativeDate.fromString(valueString_p);
            if (relativeDate != null)
            {
                return relativeDate;
            }
            else
            {
                //re-throw now LOG needed
                LOG.error("Invalid string format: " + valueString_p, e);
                throw new OwInvalidOperationException(new OwString1("opencmis.OwCMISJavaTextFormat.err.parseSingleString", "Invalid string format %1", valueString_p), e);
            }
        }
    }

    public Object parse(String text_p, boolean asArray_p) throws OwException
    {
        if (asArray_p)
        {
            if (text_p != null && text_p.length() == 0)
            {
                return Array.newInstance(valueClass, 0);
            }

            String[] arrayElements = splitArray(text_p);
            if (arrayElements == null || arrayElements.length == 0)
            {
                return null;
            }
            else
            {
                Object valuesArray = Array.newInstance(valueClass, arrayElements.length);
                int index = 0;
                for (String element : arrayElements)
                {
                    Object elementValue = parseSingleString(element);
                    Array.set(valuesArray, index, elementValue);
                    index++;
                }
                return valuesArray;
            }
        }
        else
        {
            return parseSingleString(text_p);
        }
    }

}
