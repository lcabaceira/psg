package com.wewebu.ow.server.ecmimpl.fncm5;

import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ResourceAccessor;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Builds {@link OwFNCM5ContentObjectModel}s for given object store resource considering 
 * the current network and configuration settings.  
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
public interface OwFNCM5ObjectModelBuilder
{
    /**
     * Builds a {@link OwFNCM5ContentObjectModel} considering current network and configuration settings.
     * 
     * @param network_p the current {@link OwFNCM5Network}
     * @param resourceAccessor_p accessor of the object-store resource that defines thre resulted object model  
     * @param modelConfiguration_p the &lt;ContentObjectModel&gt; model configuration element utility-wrapper 
     * @return a {@link OwFNCM5ContentObjectModel} for the give object-store resource 
     * @throws OwException 
     */
    OwFNCM5ContentObjectModel build(OwFNCM5Network network_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p, OwXMLUtil modelConfiguration_p) throws OwException;
}
