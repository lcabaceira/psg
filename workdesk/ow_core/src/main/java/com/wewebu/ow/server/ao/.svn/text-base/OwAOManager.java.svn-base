package com.wewebu.ow.server.ao;

import java.util.Collection;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Alfresco Workdesk application objects manager interface. Used to provide different application 
 * object retrieval implementations. One application objects manager provides objects 
 * of one type.
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
public interface OwAOManager
{

    /**
     * 
     * @return the managed objects type integer code
     * @deprecated will be replaced by and {@link OwAOType} accessor      
     */
    int getManagedType();

    //    will be introduced together when AOW managers refactoring     
    //    /**
    //     * 
    //     * @return the managed objects type
    //     * @since 4.2.0.0  
    //     */
    //    OwAOType<?> getType();

    /**
     * Returns all application objects that are matched by the given name.
     * @param strName_p the 
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user. 
     * @return a collection of Alfresco Workdesk application objects
     * @throws OwException
     */
    Collection<?> getApplicationObjects(String strName_p, boolean fForceUserSpecificObject_p) throws OwException;

    /**
     * Returns the application object that is matched by the given name.
     * @param strName_p name of the object to retrieve e.g. "userprefs"
     * @param param_p optional Object, can be null
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     * @param fCreateIfNotExist_p 
     * @return an Alfresco Workdesk application object
     * @throws OwException if the given object could not be retrieved
     */
    Object getApplicationObject(String strName_p, Object param_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws OwException;

    /**
     * Returns the application object that is matched by the given name.
     * @param strName_p name of the object to retrieve e.g. "userprefs"
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     * @param fCreateIfNotExist_p
     * @return an Alfresco Workdesk application object
     * @throws OwException if the given object could not be retrieved
     */
    Object getApplicationObject(String strName_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws OwException;
}
