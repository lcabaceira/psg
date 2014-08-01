package com.wewebu.ow.server.ecmimpl.opencmis.util;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface for OwObjectClass creation.
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
public interface OwCMISObjectClassFactory
{
    /**
     * Handling native object retrieval for provided object type id.
     * @param id String specific object type id
     * @param session Session the current OpenCMIS session
     * @return ObjectType
     * @throws OwException
     */
    ObjectType retrieveObjectType(String id, Session session) throws OwException;

    /**
     * Create an {@link OwCMISNativeObjectClass} representation from the given native definition object.
     * @param objectType {@link ObjectType}
     * @return OwCMISObjectClass
     * @throws OwException
     */
    <T extends ObjectType> OwCMISNativeObjectClass<T, ?> createObjectClass(T objectType) throws OwException;

    /**
     * Create an {@link OwCMISNativeObjectClass} representation of the object type  of the given {@link TransientCmisObject} .
     * @param object
     * @return an {@link OwCMISNativeObjectClass}
     * @throws OwException
     */
    <O extends TransientCmisObject> OwCMISNativeObjectClass<?, O> createObjectClassOf(O object) throws OwException;

}
