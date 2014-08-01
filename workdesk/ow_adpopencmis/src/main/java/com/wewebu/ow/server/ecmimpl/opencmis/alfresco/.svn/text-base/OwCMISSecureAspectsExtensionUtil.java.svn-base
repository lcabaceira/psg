package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.List;

import org.alfresco.cmis.client.impl.AlfrescoUtils;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.CmisExtensionElementImpl;

/**
 *<p>
 * OwCMISSecureAspectsExtensionUtil.
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
public class OwCMISSecureAspectsExtensionUtil
{
    public static final String MUST_SECURE_ASPECTS_NAME = "mustSecureAspects";
    public static final CmisExtensionElement MUST_SECURE_ASPECTS = new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, MUST_SECURE_ASPECTS_NAME, null, (String) null);

    public static boolean containsMustSecureAspects(List<CmisExtensionElement> extensions)
    {
        boolean mustSecureAspects = false;

        if (extensions != null)
        {
            for (CmisExtensionElement objectExtension : extensions)
            {
                if (OwCMISSecureAspectsExtensionUtil.MUST_SECURE_ASPECTS_NAME.equals(objectExtension.getName()))
                {
                    mustSecureAspects = true;
                    break;
                }
            }
        }

        return mustSecureAspects;
    }
}
