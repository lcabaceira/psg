package com.wewebu.ow.server.ecmimpl.fncm5.converter;

import java.util.Date;
import java.util.TimeZone;

import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5MetaConverterClass;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * Date converter class.
 * Using time zones to transform the values
 * between server and client representation. 
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
public class OwFNCM5DateValueConverterClass extends OwFNCM5MetaConverterClass<Date, Date>
{
    private TimeZone serverTimeZone;
    private TimeZone clientTimeZone;

    public OwFNCM5DateValueConverterClass(TimeZone serverTimeZone_p, TimeZone clientTimeZone_p)
    {
        super(Date.class, OwFNCM5EngineListFactory.DateTimeList);
        this.serverTimeZone = serverTimeZone_p;
        this.clientTimeZone = clientTimeZone_p;
    }

    public Date toNativeValue(Date owdValue_p)
    {
        return OwDateTimeUtil.convert(owdValue_p, clientTimeZone, serverTimeZone);
    }

    public Date convertNativeValue(Date nativeValue_p)
    {
        return OwDateTimeUtil.convert(nativeValue_p, serverTimeZone, clientTimeZone);
    }

}
