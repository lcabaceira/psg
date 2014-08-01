package org.alfresco.wd.ui.conf;

/**
 *<p>
 * Interface defining a standard way for configuration.<br />
 * Example
 * <pre>
 *  &lt;param name="myName"&gt;someValue&lt;/param&gt;
 *
 *  &lt;paramList name="listParam"&gt;
 *   &lt;value&gt;value1&lt;/value&gt;
 *   &lt;value&gt;value2&lt;/value&gt;
 *   &lt;value&gt;value3&lt;/value&gt;
 *  &lt;/paramList&gt;
 * </pre>
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
 *@since 4.2.0.0
 */
public interface OwUniformParamConfiguration
{
    public static final String ATT_NAME = "name";

    public static final String ELEM_PARAM = "param";

    public static final String ELEM_PARAM_LIST = "paramList";

}
