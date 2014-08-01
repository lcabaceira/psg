package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * The P8 5.0 AWD content object interface extension.
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
public interface OwFNCM5Object<N> extends OwObject
{
    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getNativeObject()
     */
    N getNativeObject() throws OwException;

    OwFNCM5Network getNetwork() throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getResource()
     */
    OwFNCM5Resource getResource() throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getDMSID()
     */
    String getDMSID() throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getProperty(java.lang.String)
     */
    OwProperty getProperty(String strPropertyName_p) throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getProperties(java.util.Collection)
     */
    OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getClonedProperties(java.util.Collection)
     */
    OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws OwException;

    /**
     * Can this object be target for a workflow ?
     * @return true if there is at least one workflow that can have this content object as target <br/> 
     *         false otherwise 
     */
    boolean hasWorkflowDescriptions();

    /**
     * 
     * @return a {@link Set} of {@link OwFNCM5Object} representations of workflows  
     *         that can have this content object as target 
     * @throws OwException
     */
    Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#delete()
     */
    void delete() throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getVersion()
     */
    OwFNCM5Version<?> getVersion() throws OwException;
}
