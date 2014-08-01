package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.cmis.client.AlfrescoAspects;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwCMISAbstractAlfrescoObjectClass.
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
public abstract class OwCMISAbstractAlfrescoObjectClass<T extends ObjectType, O extends TransientCmisObject> extends OwCMISAbstractAlfrescoClass<T, O>
{
    private static Logger LOG = OwLog.getLogger(OwCMISAbstractAlfrescoObjectClass.class);
    private OwCMISNativeObject<O> singleton;
    private StackTraceElement[] singletonStackTrace;

    public OwCMISAbstractAlfrescoObjectClass(OwCMISNativeObjectClass<T, O> nativeObjectClass)
    {
        super(nativeObjectClass);

    }

    private Collection<ObjectType> getAspects(boolean secure) throws OwException
    {

        //return alfrescoObjectType.getAspects();

        OwCMISAlfrescoTransientObject<O> transientObject = (OwCMISAlfrescoTransientObject<O>) singleton.getTransientObject();

        AlfrescoAspects singletonNative = null;

        if (secure)
        {
            singletonNative = (AlfrescoAspects) transientObject.secureAspects();
        }
        else
        {
            singletonNative = (AlfrescoAspects) transientObject.getTransientCmisObject();
        }

        return singletonNative.getAspects();
    }

    @Override
    protected boolean areAspectsSecured()
    {
        OwCMISAlfrescoTransientObject<O> transientObject = (OwCMISAlfrescoTransientObject<O>) singleton.getTransientObject();
        return !transientObject.mustSecureAspects();
    }

    @Override
    protected Collection<OwCMISNativeObjectClass<?, ?>> getAspectsClasses(boolean secure) throws OwException
    {
        List<OwCMISNativeObjectClass<?, ?>> aspectClasses = new LinkedList<OwCMISNativeObjectClass<?, ?>>();
        Collection<ObjectType> aspectes = getAspects(secure);
        OwCMISNativeSession mySession = getSession();
        for (ObjectType objectType : aspectes)
        {
            OwCMISNativeObjectClass<ObjectType, ?> aspectClass = mySession.from(objectType);
            aspectClasses.add(aspectClass);
        }
        return aspectClasses;
    }

    protected void setSingleton(OwCMISNativeObject<O> singleton) throws OwException
    {
        if (this.singleton != null)
        {
            if (LOG.isDebugEnabled())
            {
                if (singletonStackTrace != null)
                {
                    LOG.error("The singleton previous set stack trace " + Arrays.toString(singletonStackTrace));
                }
            }
            throw new OwInvalidOperationException("OwCMISAbstractAlfrescoObjectClass' support single instances olny!");
        }

        this.singleton = singleton;

        if (LOG.isDebugEnabled())
        {
            this.singletonStackTrace = Thread.currentThread().getStackTrace();
        }
    }

}
