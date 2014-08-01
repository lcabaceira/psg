package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.AttributedCharacterIterator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 *<p>
 * OwCMISBigIntegerTextFormat.
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
public class OwCMISBigIntegerTextFormat extends Format
{

    private static final long serialVersionUID = 7124849733436615011L;

    private DecimalFormat decimalFormat;

    public OwCMISBigIntegerTextFormat()
    {
        this(new DecimalFormat());
    }

    public OwCMISBigIntegerTextFormat(String pattern_p)
    {
        this(new DecimalFormat(pattern_p));
    }

    public OwCMISBigIntegerTextFormat(String pattern_p, DecimalFormatSymbols symbols_p)
    {
        this(new DecimalFormat(pattern_p, symbols_p));
    }

    private OwCMISBigIntegerTextFormat(DecimalFormat decimalFormat_p)
    {
        super();
        this.decimalFormat = decimalFormat_p;
        this.decimalFormat.setParseBigDecimal(true);
        this.decimalFormat.setParseIntegerOnly(true);
    }

    public StringBuffer format(Object obj_p, StringBuffer toAppendTo_p, FieldPosition pos_p)
    {
        return decimalFormat.format(obj_p, toAppendTo_p, pos_p);
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj_p)
    {
        return decimalFormat.formatToCharacterIterator(obj_p);
    }

    public int hashCode()
    {
        return decimalFormat.hashCode();
    }

    public Object parseObject(String source_p, ParsePosition pos_p)
    {
        BigDecimal decimalObject = (BigDecimal) decimalFormat.parseObject(source_p, pos_p);
        return new BigInteger(decimalObject.toString());
    }

    public Object parseObject(String source_p) throws ParseException
    {
        BigDecimal decimalObject = (BigDecimal) decimalFormat.parseObject(source_p);
        return new BigInteger(decimalObject.toString());
    }

    public String toString()
    {
        return decimalFormat.toString();
    }

}