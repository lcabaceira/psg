package com.wewebu.ow.server.log;

import org.apache.log4j.Logger;

/**
 *<p>
 * Alfresco Workdesk Logger.<br/>
 * usage:<br/>
 * <code>private static final Logger LOG = OwLogCore.getLogger(Class class_p);</code><br/>
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
public class OwLogCore
{
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
            return Logger.getLogger("owd.core");
        }
    }
}