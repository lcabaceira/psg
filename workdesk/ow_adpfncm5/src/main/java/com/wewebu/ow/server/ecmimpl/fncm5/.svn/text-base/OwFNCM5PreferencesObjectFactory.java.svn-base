package com.wewebu.ow.server.ecmimpl.fncm5;

import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5DefaultObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Document;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5PreferencesDocument;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Creates special AO of type  {@link OwFNCM5PreferencesDocument}.
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
 *@since 3.2.0.2
 */
public class OwFNCM5PreferencesObjectFactory extends OwFNCM5DefaultObjectFactory
{
    @SuppressWarnings("unchecked")
    public <N, O extends OwFNCM5Object<N>, R extends OwFNCM5Resource> O create(Class<O> objectClass_p, Class<?>[] parameterTypes_p, Object[] parameters_p) throws OwException
    {
        if (objectClass_p.isAssignableFrom(OwFNCM5Document.class))
        {
            return (O) super.create(OwFNCM5PreferencesDocument.class, parameterTypes_p, parameters_p);
        }
        else
        {
            return super.create(objectClass_p, parameterTypes_p, parameters_p);
        }
    }
}
