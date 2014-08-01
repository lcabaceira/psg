package com.wewebu.ow.server.ecmimpl.opencmis;

import java.util.Collection;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Abstraction to OpenCMIS-Session object, contains additionally simplified helper methods. 
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
public interface OwCMISNativeSession extends OwCMISSession
{

    /**
     * 
     * @return the corresponding Open CMIS {@link Session}
     */
    Session getOpenCMISSession();

    /**
     * 
     * @param filterPropertyNames
     * @param maxItemsPerPage
     * @param classContext
     * @return an Open CMIS  {@link OperationContext} with the filter set to the give properties and max items to the given value.
     *         Property IDs resolution is done in the given AWD class context.
     */
    OperationContext createOperationContext(Collection<String> filterPropertyNames, int maxItemsPerPage, OwCMISNativeObjectClass<?, ?>... classContext);

    /**
     * Create an OperationContext with specific sorting
     * @param filterPropertyNames Collection of property names
     * @param sorting OwSort specific sorting
     * @param maxItemsPerPage int page size
     * @param classContext OwCMISNativeObjectClass root type representation
     * @return OperationContext with specific definitions
     * @since 4.1.1.1
     */
    OperationContext createOperationContext(Collection<String> filterPropertyNames, OwSort sorting, int maxItemsPerPage, OwCMISNativeObjectClass<?, ?>... classContext);

    /**
     * Get a property class factory, for creation of OwPropertyClass instances.
     * @return OwCMISNativePropertyClassFactory
     */
    OwCMISNativePropertyClassFactory getNativePropertyClassFactory();

    /**
     * Performs {@link ObjectType} to {@link OwCMISNativeObjectClass} conversion.
     * @param type
     * @return the {@link OwCMISNativeObjectClass} correspondent of the given type.
     * @throws OwException
     */
    <T extends ObjectType> OwCMISNativeObjectClass<T, ?> from(T type) throws OwException;

    /**
     * 
     * @param object
     * @return the {@link OwCMISNativeObjectClass} of the given {@link TransientCmisObject}
     * @throws OwException
     */
    <O extends TransientCmisObject> OwCMISNativeObjectClass<?, O> classOf(O object) throws OwException;

    /**
     * 
     * @param transientCmisObject
     * @param conversionParameters
     * @return the {@link OwCMISNativeObject} o
     * @throws OwException
     */
    <O extends TransientCmisObject> OwCMISNativeObject<O> from(O transientCmisObject, Map<String, ?> conversionParameters) throws OwException;

    /**
     * 
     * @param className 
     * @return the native object class with the given class name.  
     * @throws OwException
     */
    OwCMISNativeObjectClass<?, ?> getNativeObjectClass(String className) throws OwException;

    /**
     * 
     * @param objectNativeId the object ID of the document
     * @param propertyNames a list of properties to be loaded for this object. 
     *          If null then the set of properties that are loaded depends on the particular content repository implementation.
     * @return a native object loaded from the content repository
     * @throws OwException
     */
    OwCMISNativeObject<TransientCmisObject> getNativeObject(String objectNativeId, Collection<String> propertyNames, Map<String, ?> conversionParameters) throws OwException;
}
