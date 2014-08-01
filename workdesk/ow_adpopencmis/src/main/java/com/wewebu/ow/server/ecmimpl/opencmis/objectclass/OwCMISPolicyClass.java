package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.PolicyType;
import org.apache.chemistry.opencmis.client.api.TransientPolicy;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISPolicy;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Object class for the {@link TransientPolicy} - {@link PolicyType} OpenCMIS object model.
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
public interface OwCMISPolicyClass extends OwCMISNativeObjectClass<PolicyType, TransientPolicy>
{
    @Override
    OwCMISPolicy from(TransientPolicy object, Map<String, ?> conversionParameters) throws OwException;
}
