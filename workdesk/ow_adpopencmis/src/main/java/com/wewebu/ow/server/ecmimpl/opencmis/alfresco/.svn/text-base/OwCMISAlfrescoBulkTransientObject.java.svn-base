package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.alfresco.cmis.client.AlfrescoAspects;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.ExtensionLevel;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISBulkTransientObject;

/**
 *<p>
 * Implements bulk Alfresco-aspect property extension data tweaking during caching operations.
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
public class OwCMISAlfrescoBulkTransientObject<N extends TransientCmisObject> extends OwCMISBulkTransientObject<N> implements OwCMISAlfrescoTransientObject<N>
{

    public OwCMISAlfrescoBulkTransientObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        super(transientCmisObject, creationContext, session);
    }

    @Override
    public synchronized N secureAspects()
    {
        if (mustSecureAspects())
        {
            this.contextBoundObject = this.retrieveProperties(Collections.EMPTY_SET);
        }

        return getTransientCmisObject();
    }

    @Override
    public synchronized boolean mustSecureAspects()
    {
        List<CmisExtensionElement> propertiesExtensions = getTransientCmisObject().getInputExtensions(ExtensionLevel.PROPERTIES);
        return OwCMISSecureAspectsExtensionUtil.containsMustSecureAspects(propertiesExtensions);
    }

    @Override
    protected PropertyDefinition<?> getUnfetchedPropertyDefinition(String nativePropertyName)
    {
        PropertyDefinition<?> basePropDef = super.getUnfetchedPropertyDefinition(nativePropertyName);
        if (basePropDef == null)
        {
            Collection<ObjectType> lst = ((AlfrescoAspects) this.contextBoundObject.object).getAspects();
            if (lst != null)
            {
                for (ObjectType type : lst)
                {
                    basePropDef = type.getPropertyDefinitions().get(nativePropertyName);
                    if (basePropDef != null)
                    {
                        break;
                    }
                }
            }
        }
        return basePropDef;
    }

}
