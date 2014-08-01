package com.wewebu.ow.server.ecm;

import java.util.Map;

import com.wewebu.ow.server.collections.OwPageableObject;

/**
 *<p>
 * Base Class for virtual folder objects.
 * Virtual folder objects are created through a OwVirtualFolderObjectFactory.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwVirtualFolderObject extends OwObject, OwPageableObject<OwObject>
{
    /** modify the properties of an object to meet the criteria list of this virtual folders search template
     * 
     *  This method is used to add documents to a virtual folder. Since the contents of a virtual folder is
     *  the result of a search template, a file in a virtual folder must meet all criteria of this search
     *  template.
     *  
     * @param objectClass_p OwObjectClass of the new document
     * @param properties_p OwPropertyCollection to be modified
     *  
     */
    public abstract void setFiledObjectProperties(OwObjectClass objectClass_p, OwPropertyCollection properties_p) throws Exception;

    /**
     * Return a map which represents external triggered value propagation.
     * Map is constructed of criteria name (String) and value (Object),
     * can also be null if no propagation map was set. 
     * @return Map if set, else null
     * @since 3.2.0.0
     * @see #setPropagationMap(Map)
     */
    Map getPropagationMap();

    /**
     * Set a map which defines an external triggered value propagation.
     * This map will be used to set the search corresponding its definition,
     * and will also be provided to subnodes of current virtual object.
     * <p>Can be set to null to avoid any external defined propagation.</p>
     * @param propagationMap Map (String criteria Name, Object value)
     * @since 3.2.0.0
     * @see #getPropagationMap()
     */
    void setPropagationMap(Map propagationMap);

    /**
     * Retrieve the name of the virtual folder template used for constructing this object 
     * as defined in the bootstrap SemiVirtualRecordClass configurations.  
     * 
     * @return the name of the virtual folder template used for constructing this object
     * @since 4.0.0.0
     */
    String getVirtualFolderName();

}
