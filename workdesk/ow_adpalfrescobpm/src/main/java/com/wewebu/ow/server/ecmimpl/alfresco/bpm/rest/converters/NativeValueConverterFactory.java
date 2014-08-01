package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Factory for {@link NativeValueConverter}s.
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
public interface NativeValueConverterFactory
{
    public NativeValueConverter converterFor(OwFieldDefinition propertyClass) throws OwException;
}
