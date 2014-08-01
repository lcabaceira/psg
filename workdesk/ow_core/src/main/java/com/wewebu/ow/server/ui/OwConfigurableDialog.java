package com.wewebu.ow.server.ui;

import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Configurable dialog interface.
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
public interface OwConfigurableDialog
{
    /**
     * Get the associated {@link OwXMLUtil} object. Can be <code>null</code>.
     * @return  the associated {@link OwXMLUtil} object. Can be <code>null</code>.
     */
    OwXMLUtil getConfigNode();

    /**
     * Set the associated {@link OwXMLUtil} object. Can be <code>null</code>.
     */
    void setConfigNode(OwXMLUtil configNode_p);
}