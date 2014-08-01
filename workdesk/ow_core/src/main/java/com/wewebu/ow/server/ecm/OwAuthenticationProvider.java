package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;

/**
 *<p>
 * Base interface for the authentication of users.<br/><br/>
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
public interface OwAuthenticationProvider
{
    /** get an instance of the login UI submodule for user authentication 
     * Login is very specific to the provider and can not be handled generically
     * @return OwUILoginModul OwView derived module 
     */
    public abstract OwUILoginModul getLoginSubModul() throws Exception;

    /** get an instance of the user select UI submodule for selecting a user or group
     * User selection is very specific to the ECM System and can not be handled generically
     *
     * @param strID_p the ID of the currently set user or null if no user is selected
     * @param types_p array of type identifiers as defined in OwUIUserSelectModul
     *
     * @return OwUIUserSelectModul OwView derived module 
     */
    public abstract OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws Exception;

    /** check if the user select module is supported i.e. getUserSelectSubModul is implemented
     *
     * @return true if access rights can be edited
     */
    public abstract boolean canUserSelect() throws Exception;

    /** get the credentials of the logged in user 
     *
     * @return the valid credentials of the logged in user, or null if not logged on
     */
    public abstract OwCredentials getCredentials() throws Exception;

    /** get the user information form a User ID
     *
     * @param strID_p the ID of the searched user 
     * @return the user information object of a user
     */
    public abstract OwUserInfo getUserFromID(String strID_p) throws Exception;

    /** get the display name for a role name
     *
     * @param strRoleName_p to retrieve the display name for
     * @return the display name for the role
     */
    public abstract String getRoleDisplayName(String strRoleName_p) throws Exception;

    /** log off and reset credentials */
    public abstract void logout() throws Exception;

    /** log on to the provider with default configuration 
     *
     *  NOTE:   The behavior of the function depends on the configuration of the ECM adapter.
     *          For a determined login use the OwUILoginModul Screen (getLoginSubModul())
     *
     * @param strUser_p String user name or name of function user or null to use a default login if available
     * @param strPassword_p String the password for the user or null if not required
     */
    public abstract void loginDefault(String strUser_p, String strPassword_p) throws Exception;
}