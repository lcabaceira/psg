package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for Object versions to identify a single version of a document in a versioning system.<br/><br/>
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
public interface OwVersion
{
    /** get the version number as an array of integer.
     *
     * @return int[] with version number, first index has highest priority.
     */
    public abstract int[] getVersionNumber() throws Exception;

    /** get the version number as a String
     *
     * @return String with version number, or placeholder [latest | released]
     */
    public abstract String getVersionInfo() throws Exception;

    /** check if this version is the released version 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean isReleased(int context_p) throws Exception;

    /** check if this version is the latest version 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean isLatest(int context_p) throws Exception;

    /** check if this version is a major version, e.g. was released once 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean isMajor(int context_p) throws Exception;

    /** true if object is locked due to check out 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean isCheckedOut(int context_p) throws Exception;

    /** true if object is locked by the current user due to check out 
     * @param context_p OwStatusContextDefinitions
     * @since 2.5.2.0
     */
    public abstract boolean isMyCheckedOut(int context_p) throws Exception;

    /** get the user who checked out the object
    * @param iContext_p OwStatusContextDefinitions
    * @return the User ID of the user who checked out the item, or null if it is not checked out
    * @since 2.5.2.0
    */
    public abstract String getCheckedOutUserID(int iContext_p) throws Exception;

    /** check if this version is equal to the given version 
     * @param version_p OwVersion to compare to
     */
    public abstract boolean equals(OwVersion version_p) throws Exception;

    /** checkout 
     *
     * @param mode_p Object access mode for checked out object, see getCheckoutModes
     */
    public abstract void checkout(Object mode_p) throws Exception;

    /** check if checkout function is allowed
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean canCheckout(int context_p) throws Exception;

    /** checkin 
     * 
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param fOverwriteContent_p boolean true = content_p overwrites existing content, even if null, false = existing content is kept
     * @param strMimeType_p String MIME Types of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     *
     */
    public abstract void checkin(boolean fPromote_p, Object mode_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, boolean fOverwriteContent_p,
            String strMimeType_p, String strMimeParameter_p) throws Exception;

    /** check if checkin function is allowed
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean canCheckin(int context_p) throws Exception;

    /** cancel checkout */
    public abstract void cancelcheckout() throws Exception;

    /** check if cancel checkout function is allowed 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean canCancelcheckout(int context_p) throws Exception;

    /** promote version */
    public abstract void promote() throws Exception;

    /** check if promote version function is allowed 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean canPromote(int context_p) throws Exception;

    /** demote version */
    public abstract void demote() throws Exception;

    /** check if demote version function is allowed 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean canDemote(int context_p) throws Exception;

    /** set the content to the checked out object
     *
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param strMimeType_p String MIME Types of the new object content
     * @param strMimeParameter_p extra info to the MIME type
    */
    public abstract void save(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws Exception;

    /** check if a reservation object is available, which can be saved to 
     * @param context_p OwStatusContextDefinitions
     */
    public abstract boolean canSave(int context_p) throws Exception;

}