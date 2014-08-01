package com.wewebu.ow.server.conf;

/**
 *<p>
 * Base interface for user and group information.
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
public interface OwBaseUserInfo
{

    /** get name of logged in user in the long descriptive form 
     * @return String user name
     */
    public abstract String getUserLongName() throws Exception;

    /** get the user login
     * @return String user login
     */
    public abstract String getUserName() throws Exception;

    /** get the user display name.
     * @return {@link String} the user display name or <code>null</code> if the display name can not be obtained. 
     * @since 2.5.3.0
     */
    public String getUserDisplayName() throws Exception;

    /** get the user short name.
     * @return {@link String} the user short name or <code>null</code> if the short name can not be obtained. 
     * @since 2.5.3.0
     */
    public String getUserShortName() throws Exception;

    /** get the user eMail Address
     * @return String user eMail
     */
    public abstract String getUserEmailAdress() throws Exception;

    /** get the user unique persistent ID, which does not change even when name changes
     * @return String unique persistent ID
     */
    public abstract String getUserID();

    /** get a string list of role names for this user
     * @return java.util.List of string role names
     */
    public abstract java.util.Collection getRoleNames() throws Exception;

    /** get a list with group information the user is assigned to
     * @return java.util.List of OwUserInfo
     */
    public abstract java.util.Collection getGroups() throws Exception;

    /** flag indication, that user info actually designates a group rather than a single user
     * @return true = group otherwise user
     */
    public abstract boolean isGroup() throws Exception;

}
