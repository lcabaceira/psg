package com.wewebu.ow.server.ecmimpl.fncm5.log;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCoreBase;

/**
 *<p>
 * Alfresco Workdesk Logger for P8 5.0.<br/>
 * usage:<br/>
 * <code>private static final Logger LOG = OwLog.getLogger(Class class_p);</code><br/>
 * Depending on the settings, returns the module/package logger or the class logger.<br/>
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
public class OwLog
{
    private OwLog()
    {

    }

    /** get the Logger for the module/package
     * 
     * @return org.apache.log4j.Logger;
     */
    public static Logger getLogger(Class class_p)
    {
        if (OwLogCoreBase.isUseClassLogger())
        {
            return Logger.getLogger(class_p);
        }
        else
        {
            return Logger.getLogger("owd.adp.fncm5");
        }
    }
}