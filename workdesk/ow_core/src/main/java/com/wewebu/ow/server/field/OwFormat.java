package com.wewebu.ow.server.field;

import java.text.Format;
import java.util.Locale;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Base interface for format information for fields.
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
public interface OwFormat
{
    /** check if the format should ignore time part of date values */
    public boolean ignoreTime();

    /** check if the format should ignore date part of date values */
    public boolean ignoreDate();

    /** get the optional formatter object for string representation, 
     *  according to the given context type 
     * 
     * @param iFieldProviderType_p int as defined in OwFieldProvider.TYPE_...
     * @return OwFormat, can be null to use the default format
     */
    public abstract Format getTextFormat(int iFieldProviderType_p);

    /** check if validation is implemented 
     * 
     * @return a boolean
     */
    public abstract boolean canValidate();

    /** validate the given value
     * 
     * @param iFieldProviderType_p int as defined in OwFieldProvider.TYPE_...
     * @param object_p the value to validate
     * @param locale_p Locale for String localize
     * @return null if valid, a String user message if invalid
     * @throws OwInvalidOperationException 
     */
    public abstract String validate(int iFieldProviderType_p, Object object_p, Locale locale_p) throws OwInvalidOperationException;
}