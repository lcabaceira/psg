package com.wewebu.ow.server.ui;

import com.wewebu.ow.server.app.OwJspFormConfigurator;

/**
 *<p>
 * Interface to implement if supporting JSP forms configuration.
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
 *@since 3.1.0.0
 */
public interface OwJspConfigurable
{
    /**
     * Get the associated {@link OwJspFormConfigurator} object. Can be <code>null</code>.
     * @return  the associated {@link OwJspFormConfigurator} object or <code>null</code>.
     */
    OwJspFormConfigurator getJspConfigurator();

    /**
     * Set the associated {@link OwJspFormConfigurator} object. Can be <code>null</code>.
     */
    void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p);
}
