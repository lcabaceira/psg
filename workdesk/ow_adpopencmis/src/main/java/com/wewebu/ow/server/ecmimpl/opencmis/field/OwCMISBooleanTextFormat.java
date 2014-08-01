package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwCMISBooleanTextFormat.
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
public class OwCMISBooleanTextFormat extends Format
{
    private static final Logger LOG = OwLog.getLogger(OwCMISBooleanTextFormat.class);

    /**
     * 
     */
    private static final long serialVersionUID = -6411421911069058729L;

    /**
     * A string representing the boolean value true.
     */
    private String trueString;

    /**
     * A string representing the boolean value false.
     */
    private String falseString;

    /**
     * Creates a BooleanFormat with default true/false formatting.
     */
    public OwCMISBooleanTextFormat()
    {
        this.trueString = "true";
        this.falseString = "false";
    }

    /**
     * Creates a BooleanFormat.
     *
     * @param trueString_p A string representing true.
     * @param falseString_p A string representing false.
     */
    public OwCMISBooleanTextFormat(String trueString_p, String falseString_p) throws OwInvalidOperationException
    {
        if (trueString_p == null || falseString_p == null)
        {
            LOG.error("OwCMISBooleanTextFormat.OwCMISBooleanTextFormat(): both the true and false string should be specified!");
            throw new OwInvalidOperationException("Invalid format boolean strings!");
        }
        this.trueString = trueString_p;
        this.falseString = falseString_p;
    }

    /**
     * Constructs a boolean format from a pattern. The pattern must contain two
     * strings separated by colon, for example: "true:false".
     *
     * @param pattern_p The pattern from which to construct.
     */
    public OwCMISBooleanTextFormat(String pattern_p) throws OwInvalidOperationException
    {
        String[] valuePatterns = pattern_p.split(":");
        if (valuePatterns.length != 2)
        {
            LOG.error("OwCMISBooleanTextFormat.OwCMISBooleanTextFormat():Cannot construct a boolean format " + "from " + pattern_p + ". The pattern must contain a single ':' " + "character");
            throw new OwInvalidOperationException("Cannot construct a boolean format " + "from " + pattern_p + ". The pattern must contain a single ':' " + "character");
        }
        this.trueString = valuePatterns[0];
        this.falseString = valuePatterns[1];
    }

    /**
     * Formats a Boolean and appends the result to a StringBuffer.
     *
     * @param obj_p The object to format.
     * @param appendTo_p The StringBuffer to which the formatted string will be appended.
     * @param pos_p A FieldPosition param (not used in this class).
     *
     * @return A StringBuffer with the formatted string for this object.
     */
    @Override
    public StringBuffer format(Object obj_p, StringBuffer appendTo_p, FieldPosition pos_p)
    {
        if ((null != obj_p) && !(obj_p instanceof Boolean))
        {
            LOG.error("OwCMISBooleanTextFormat.format():A non null Boolean object was expected!");
            throw new IllegalArgumentException("A non null Boolean object was expected!");
        }
        Boolean val = (Boolean) obj_p;
        if (val == null)
        {
            // nothing to append.
            pos_p.setBeginIndex(0);
            pos_p.setEndIndex(0);
        }
        else if (val)
        {
            appendTo_p.append(this.trueString);
            pos_p.setBeginIndex(0);
            pos_p.setEndIndex(this.trueString.length() - 1);
        }
        else
        {
            // val == false
            appendTo_p.append(this.falseString);
            pos_p.setBeginIndex(0);
            pos_p.setEndIndex(this.falseString.length() - 1);
        }
        return appendTo_p;
    }

    /**
     * Parses a string into a {@code Boolean}. A string can be either a trueString
     * or a falseString (non-case sensitive).
     *
     * @param source_p The string from which to parse.
     * @param pos_p Marks the end of the parsing, or 0 if the parsing failed.
     *
     * @return A {@code Boolean} for the parsed string.
     */
    @Override
    public Boolean parseObject(String source_p, ParsePosition pos_p)
    {
        if (source_p == null)
        {
            LOG.error("OwCMISBooleanTextFormat.parseObject():The source is null!");
            throw new NullPointerException("The source is null!");
        }
        Boolean value = null;
        if (this.trueString.equalsIgnoreCase(source_p.trim()))
        {
            value = Boolean.TRUE;
            pos_p.setIndex(trueString.length());
        }
        else if (this.falseString.equalsIgnoreCase(source_p.trim()))
        {
            value = Boolean.FALSE;
            pos_p.setIndex(this.falseString.length());
        }
        if (null == value)
        {
            pos_p.setErrorIndex(0);
        }
        return value;
    }

    /**
     * Parses text from the beginning of the given string to produce a boolean.
     * The method may not use the entire text of the given string.
     *
     * @param text_p A String that should be parsed from it's start.
     *
     * @return A {@code Boolean} parsed from the string.
     *
     * @exception ParseException If the string cannot be parsed.
     */
    public Boolean parse(String text_p) throws ParseException
    {
        ParsePosition parsePosition = new ParsePosition(0);
        Boolean result = parseObject(text_p, parsePosition);
        if (parsePosition.getIndex() == 0)
        {
            LOG.debug("OwCMISBooleanTextFormat.parse():Unparseable boolean: \"" + text_p + "\" at possition " + parsePosition.getErrorIndex());
            throw new ParseException("Unparseable boolean: \"" + text_p + '"', parsePosition.getErrorIndex());
        }
        return result;
    }
}