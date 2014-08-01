package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Definitions for the context used for status functions like can... or is... <br/>
 * Some of the status functions are slow and can not be used in a time critical context such as objects lists.
 * On the other hand  querying the status of just a single object is not so time critical.<br/> 
 * The context definitions can distinguish between those time critical contexts and provide 
 * a more comfortable user interface. <br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwStatusContextDefinitions
{
    /** the context is time critical return the true status only if it can be provided fast, otherwise return an enabled status */
    public static final int STATUS_CONTEXT_TIME_CRITICAL = 1;

    /** the context is not time critical, but we need the correct status */
    public static final int STATUS_CONTEXT_CORRECT_STATUS = 2;

}