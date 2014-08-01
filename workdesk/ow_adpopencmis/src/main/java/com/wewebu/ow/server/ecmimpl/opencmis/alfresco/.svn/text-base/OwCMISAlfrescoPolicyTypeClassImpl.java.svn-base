package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Map;

import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.PolicyType;
import org.apache.chemistry.opencmis.client.api.TransientPolicy;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISPolicy;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISPolicyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISAlfrescoPolicyTypeClassImpl.
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
public class OwCMISAlfrescoPolicyTypeClassImpl extends OwCMISAlfrescoTypeClassImpl<PolicyType, TransientPolicy> implements OwCMISPolicyClass
{

    public OwCMISAlfrescoPolicyTypeClassImpl(OwCMISPolicyClass nativeObjectClass, AlfrescoType alfrescoType)
    {
        super(nativeObjectClass, alfrescoType);
    }

    @Override
    protected OwCMISPolicyClass getNativeObjectClass()
    {
        return (OwCMISPolicyClass) super.getNativeObjectClass();
    }

    @Override
    public OwCMISPolicy from(TransientPolicy object, Map<String, ?> conversionParameters) throws OwException
    {
        return getNativeObjectClass().from(object, addClassParameter(conversionParameters));
    }

}
