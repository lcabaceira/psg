package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstraction of inner {@link OwCMISNativeObject} transient states.
 * Implementors relay on {@link OperationContext}s and {@link TransientCmisObject}s to provide a 
 * centralized cache for CMIS property values.
 * Transient objects are used to provide a so-called secured {@link TransientCmisObject} :
 * the {@link TransientCmisObject} MWD object state with certain properties cached.     
 * Using secured {@link TransientCmisObject} guarantees that their method calls will not fail 
 * because of missing properties if the secured object was obtained through securing OwCMISTransientObject methods.  
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
public interface OwCMISTransientObject<N extends TransientCmisObject>
{
    /**
     * 
     * @return the transient state creation context 
     */
    OperationContext getTransientContext();

    /**
     * 
     * @return the {@link TransientCmisObject} state data
     */
    N getTransientCmisObject();

    /**
     * 
     * @param propertyNames CMIS property IDs that need to be secured 
     * @return the {@link TransientCmisObject} state data with the given properties secured
     * @throws OwException
     */
    N secureObject(String... propertyNames) throws OwException;

    /**
     * 
     * @param propertyNames CMIS property IDs that need to be secured 
     * @return the {@link TransientCmisObject} state data with the given properties secured
     * @throws OwException
     */
    N secureObject(Collection<String> propertyNames) throws OwException;

    /**
     * 
     * @param propertyNames CMIS property IDs that need to be secured 
     * @return a Map of the requested IDs mapped to property values. The underlying 
     *         {@link TransientCmisObject} will also have the requested properties secured. 
     * @throws OwException
     */
    Map<String, Property<?>> secureProperties(Collection<String> propertyNames) throws OwException;

    /**
     * 
     * @param propertyName CMIS property ID that needs to be secured 
     * @return the property value of the requested ID 
     * @throws OwException
     */
    Property<?> secureProperty(String propertyName) throws OwException;

    /**
     * 
     * @param renditions string filter of renditions to secure
     * @param refresh true to retrieve again available renditions
     * @return objects renditions that include the filter specified renditions if 
     *         the object has such renditions
     * @since 4.2.0.0
     */
    List<Rendition> secureRenditions(Set<String> renditions, boolean refresh) throws OwException;

    /**
     * Persists to the CMIS-ECM and sets the mapped property values to the underlying {@link TransientCmisObject}.   
     * @param propertyValues
     */
    void setProperties(Map<String, Object> propertyValues);

    /**
     * Refreshes the requested cached CMIS properties. 
     * @param propertyNames
     */
    void refresh(Collection<String> propertyNames);

    /**
     * Detaches this transient object form the ECM CMIS repository. 
     * No CMIS server round-trip call will be performed on its caching or setting method calls.
     */
    void detach();

    boolean isDetached();

}
