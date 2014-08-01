package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 *<p>
 * OwCMISStringTransparentTextFormat.
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
public class OwCMISStringTransparentTextFormat extends Format
{
    private static final long serialVersionUID = -7287631312110017918L;

    @Override
    public StringBuffer format(Object obj_p, StringBuffer toAppendTo_p, FieldPosition pos_p)
    {
        return toAppendTo_p.append(obj_p.toString());
    }

    @Override
    public Object parseObject(String source_p, ParsePosition pos_p)
    {
        pos_p.setIndex(source_p.length());
        return source_p;
    }

}