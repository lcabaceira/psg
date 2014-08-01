package com.wewebu.ow.server.ao;

import java.util.List;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface to be implemented for Application object provider.
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
 *@since 4.0.0.0
 */
public interface OwAOProvider
{

    /**
     * get a list of Objects for the application to work, like search templates, preferences...
     * @param type OwAOType type which define class and integer representation of requested application object.
     * @param name String Name of the object to retrieve e.g. "userprefs"
     * @param forceSpecificObj if true, the object must be specific to the logged in user, otherwise the OwAOProvider determines if it is common to a site or specific to a group or a user.
     *
     * @return Collection, which elements need to be cast to the appropriate type according to aoType
     */
    <T> List<T> getApplicationObjects(OwAOType<T> type, String name, boolean forceSpecificObj) throws OwException;

    /** 
     * get a Objects for the application to work, like search templates, preferences...
     * 
     * @param type OwAOType type which define class and integer representation of requested application object. 
     * @param name String name/location of the object to retrieve e.g. "userprefs"
     * @param forceSpecificObj if true, the object must be specific to the logged in user, otherwise the OwAOProvider determines if it is common to a site or specific to a group or a user.
     * @param createNonExisting boolean true = create if not exist
     *
     * @return Object, which needs to be cast to the appropriate type according to iTyp_p
     */
    <T> T getApplicationObject(OwAOType<T> type, String name, boolean forceSpecificObj, boolean createNonExisting) throws OwException;

    /** 
     * get an application object with specific parameters, like search templates, preferences...
     * 
     * @param aoType OwAOType type which define class and integer representation of requested application object
     * @param name String Name/Location of the object to retrieve e.g. "userprefs"
     * @param params List of objects (optional can be null)
     * @param forceSpecificObj boolean if true, the object must be specific to the logged in user, otherwise the OwAOProvider determines if it is common to a site or specific to a group or a user.
     * @param createNonExisting boolean true = create if not exist
     * 
     * @return Object, which needs to be cast to the appropriate type according to iTyp_p
     */
    <T> T getApplicationObject(OwAOType<T> aoType, String name, List<Object> params, boolean forceSpecificObj, boolean createNonExisting) throws OwException;

}
