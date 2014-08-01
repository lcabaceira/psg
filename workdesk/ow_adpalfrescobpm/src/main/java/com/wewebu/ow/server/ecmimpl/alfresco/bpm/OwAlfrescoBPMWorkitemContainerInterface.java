package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;

/**
 *<p>
 * Alfresco BPM extensions to the {@link OwWorkitemContainer} interface.
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
 *@since 4.0.0.0 known as IfOwAlfrescoWporkitemContainer
 *@since 4.2.0.0 renamed to OwAlfrescoWorkitemContainerInterface
 */
public interface OwAlfrescoBPMWorkitemContainerInterface extends OwWorkitemContainer, OwPageableObject<OwObject>
{
    public String createContaineeMIMEType(OwAlfrescoBPMWorkItem item) throws Exception;

    public String createContaineeMIMEParameter(OwAlfrescoBPMWorkItem item);

    public OwNetwork getNetwork();
}
