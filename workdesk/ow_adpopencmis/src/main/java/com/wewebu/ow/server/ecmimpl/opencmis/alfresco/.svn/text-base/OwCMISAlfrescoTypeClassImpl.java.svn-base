package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.data.ExtensionsData;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyIdDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISAlfrescoTypeClassImpl.
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
public abstract class OwCMISAlfrescoTypeClassImpl<T extends ObjectType, O extends TransientCmisObject> extends OwCMISAbstractAlfrescoClass<T, O>
{

    private AlfrescoType alfrescoType;

    public OwCMISAlfrescoTypeClassImpl(OwCMISNativeObjectClass<T, O> nativeObjectClass, AlfrescoType alfrescoType)
    {
        super(nativeObjectClass);
        this.alfrescoType = alfrescoType;
    }

    @Override
    protected Collection<OwCMISNativeObjectClass<?, ?>> getAspectsClasses(boolean secure) throws OwException
    {
        List<OwCMISNativeObjectClass<?, ?>> aspectClasses = new LinkedList<OwCMISNativeObjectClass<?, ?>>();
        //handling of Aspects as extensions
        Set<String> mandatoryAspects = getMandatoryAspects();
        OwCMISNativeSession mySession = getSession();
        for (String madatoryAspect : mandatoryAspects)
        {
            OwCMISNativeObjectClass<?, ?> aspectClass = (OwCMISNativeObjectClass<?, ?>) mySession.getObjectClass(madatoryAspect);
            aspectClasses.add(aspectClass);
        }

        //handling of aspects as CMIS 1.1 Standard
        Map<String, PropertyDefinition<?>> props = getNativeObject().getPropertyDefinitions();
        if (props != null && props.containsKey(OwCMISAspectsPropertyDefinition.ID))
        {
            PropertyIdDefinition idDefs = (PropertyIdDefinition) props.get(OwCMISAspectsPropertyDefinition.ID);
            if (idDefs.getDefaultValue() != null)
            {
                //get OwObjectClass representations from defined Id's
                OwCMISNativeSession session = getSession();
                for (String aspectId : idDefs.getDefaultValue())
                {
                    OwCMISNativeObjectClass<?, ?> aspectClz = (OwCMISNativeObjectClass<?, ?>) session.getObjectClass(aspectId);
                    aspectClasses.add(aspectClz);
                }
            }
        }
        return aspectClasses;
    }

    @Override
    protected boolean areAspectsSecured()
    {
        return true;
    }

    private Set<String> getMandatoryAspects()
    {
        Set<String> madatoryAspects = null;

        /*BUG in alfresco extension : NullPoiter exception is raised 
                                      when extensions are null during
                                      a getMandatoryAspects call. */

        if (alfrescoType instanceof ExtensionsData && ((ExtensionsData) alfrescoType).getExtensions() == null)
        {
            madatoryAspects = Collections.EMPTY_SET;
        }
        else
        {
            madatoryAspects = new HashSet<String>(alfrescoType.getMandatoryAspects());
        }

        return madatoryAspects;
    }

}
