package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface for batch operations.<br/>
 * Each operation is stored as pending and committed to the ECM system upon method call commit.<br/>
 * To retrieve a batch see {@link OwRepository#openBatch()}
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
public interface OwBatch
{
    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     * @param mode_p mode to use or null to use default mode, @see {@link OwObjectClass#getModes(int)}
     */
    public void setProperties(OwObject batchobject_p, OwPropertyCollection properties_p, Object mode_p) throws Exception;

    /** checkout 
    *
    * @param mode_p Object access mode for checked out object, see getCheckoutModes
    */
    public abstract void checkout(OwObject batchobject_p, Object mode_p) throws Exception;

    /** checkin 
     * 
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param fOverwriteContent_p boolean true = content_p overwrites existing content, even if null, false = existing content is kept
     * @param strMimeType_p String MIME type of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     *
     */
    public abstract void checkin(OwObject batchobject_p, boolean fPromote_p, Object mode_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p,
            boolean fOverwriteContent_p, String strMimeType_p, String strMimeParameter_p) throws Exception;

    /** promote version */
    public abstract void promote(OwObject batchobject_p) throws Exception;

    /** demote version */
    public abstract void demote(OwObject batchobject_p) throws Exception;

    /** set the content to the checked out object
     *
     * @param batchobject_p OwObject to perform batch operation
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param strMimeType_p String MIME type of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     */
    public abstract void save(OwObject batchobject_p, OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws Exception;

    /** set the permissions object
    *
    * @param permissions_p OwPermissionCollection to set
    */
    public abstract void setPermissions(OwObject batchobject_p, OwPermissionCollection permissions_p) throws Exception;

    /** creates a new object on the ECM System using the given parameters
     * has additional promote and checkin mode parameters for versionable objects
     *
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param resource_p OwResource to add to
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param strMimeType_p String MIME type of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     *
     * @return String the ECM ID of the new created object
     */
    public abstract String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p,
            OwObject parent_p, String strMimeType_p, String strMimeParameter_p) throws Exception;

    /** 
     * Commits all pending operations
     */
    public void commit() throws OwException;

    /**
     *  Deletes  an object and all its references from DB.
     *  
     *  @param batchobject_p the object to delete
     *  @throws Exception if the deletion fails
     *  @since 2.5.2.0
     */
    public void delete(OwObject batchobject_p) throws Exception;;

    /** 
    * Moves an object reference to a new parent/destination object (folder).
    *
    * @param object_p OwObject reference to add to the new parent/destination object (folder) 
    * @param destination_p OwObject (folder) to move the object to
    * @param oldParent_p OwObject old parent / old folder to remove the object from, can be <code>null</code>
    * @since 2.5.2.0
    */
    void move(OwObject object_p, OwObject destination_p, OwObject oldParent_p) throws Exception;
}