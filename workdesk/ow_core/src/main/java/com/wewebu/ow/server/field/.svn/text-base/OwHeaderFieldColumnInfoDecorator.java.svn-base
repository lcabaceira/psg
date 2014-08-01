package com.wewebu.ow.server.field;

import java.util.Locale;

/**
 *<p>
 * Header ID decorator of standard {@link OwFieldColumnInfo}s.
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
 *@since 3.2.0.0
 */
public class OwHeaderFieldColumnInfoDecorator implements OwHeaderFieldColumnInfo
{
    private String headerID = null;
    private OwFieldColumnInfo fieldInfo;

    public OwHeaderFieldColumnInfoDecorator(OwFieldColumnInfo fieldInfo_p, String headerID_p)
    {
        this.fieldInfo = fieldInfo_p;
        this.headerID = headerID_p;
    }

    public String getDisplayName(Locale locale_p)
    {
        return fieldInfo.getDisplayName(locale_p);
    }

    public String getPropertyName()
    {
        return fieldInfo.getPropertyName();
    }

    public int getAlignment()
    {
        return fieldInfo.getAlignment();
    }

    public int getWidth()
    {
        return fieldInfo.getWidth();
    }

    public boolean isSortable()
    {
        return fieldInfo.isSortable();
    }

    public String getHeaderID()
    {
        return headerID;
    }

}
