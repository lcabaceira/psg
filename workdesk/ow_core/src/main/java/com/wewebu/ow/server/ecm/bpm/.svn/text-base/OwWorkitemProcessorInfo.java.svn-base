package com.wewebu.ow.server.ecm.bpm;

import java.util.Locale;

/**
 *<p>
 * Base interface for step processor information.<br/><br/>
 * To be implemented with the specific BPM system.
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
public interface OwWorkitemProcessorInfo
{
    // === processor types
    /** step processor info type, used in getType */
    public static final int STEPPROCESSOR_TYPE_UNKNOWN = 0;
    /** step processor info type, used in getType */
    public static final int STEPPROCESSOR_TYPE_JSP_FORM = 1;
    /** step processor info type, used in getType */
    public static final int STEPPROCESSOR_TYPE_URL = 3;
    /** step processor info type, used in getType */
    public static final int STEPPROCESSOR_TYPE_JAVASCRIPT = 4;
    /** step processor info type, used in getType */
    public static final int STEPPROCESSOR_TYPE_JAVASCRIPT_AND_URL = 5;

    /** step processor info type, user defined definitions start here */
    public static final int STEPPROCESSOR_TYPE_USER_START = 0x1000;

    /** get the name of a JSP form to use
     * @return path to a JSP form page, or null if no form is associated
     */
    public abstract String getJspFormPage();

    /** get the type of the step processor as defined in STEPPROCESSOR_TYPE_...*/
    public abstract int getType();

    // === context types
    /** step processor info context, used in getContextType */
    public static final int STEPPROCESSOR_CONTEXT_UNKNOWN = 0;
    /** step processor info context, used in getContextType */
    public static final int STEPPROCESSOR_CONTEXT_STEP = 1;
    /** step processor info context, used in getContextType */
    public static final int STEPPROCESSOR_CONTEXT_LAUNCH = 2;

    /** step processor info context, user defined definitions start here */
    public static final int STEPPROCESSOR_CONTEXT_USER_START = 0x1000;

    /** get the URL to the processor to use
     * @return URL, or null if no URL is associated
     */
    public abstract String getURL() throws Exception;

    /** get the script to the processor to use
     * @return script command, or null if no script is associated
     */
    public abstract String getScript() throws Exception;

    /** get the ID  of the step processor, or null if not defined */
    public abstract String getID();

    /** get the name  of the step processor, or null if not defined */
    public abstract String getDisplayName(Locale locale_p);

    /** get the context of the step processor as defined in STEPPROCESSOR_CONTEXT_...*/
    public abstract int getContextType();

    /** get the native processor object, or null if not available */
    public abstract Object getNativeProcessor();

    /** get the width of the processor, or 0 if not defined */
    public abstract int getWidth();

    /** get the height of the processor, or 0 if not defined */
    public abstract int getHeight();
}