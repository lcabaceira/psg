package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import com.wewebu.ow.server.ecm.OwRole;
import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * Dummy Implementation for user information, used by credentials.
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
public class OwDummyUserInfo implements OwUserInfo
{
    /** long descriptive name of logged in user */
    private String m_strLongUserName;

    /** user login name*/
    private String m_userName;

    /** the user's email address*/
    private String m_emailAddress;

    /** the user's ID */
    private String m_userId;

    /** is the user info a group*/
    private boolean m_isGroup;

    /** role names of the roles the user is member of*/
    private Collection m_roleNames;

    public OwDummyUserInfo(String userName_p)
    {
        m_userName = userName_p;
        m_userId = "OW-USERID-" + userName_p;
        m_strLongUserName = userName_p + "@owdummy.de";
        m_emailAddress = userName_p + "@mail.owdummy.de";
    }

    /**
     * @param emailAddress_p The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress_p)
    {
        m_emailAddress = emailAddress_p;
    }

    /**
     * @param isGroup_p The isGroup to set.
     */
    public void setGroup(boolean isGroup_p)
    {
        m_isGroup = isGroup_p;
    }

    /**
     * @param strLongUserName_p The strLongUserName to set.
     */
    public void setStrLongUserName(String strLongUserName_p)
    {
        m_strLongUserName = strLongUserName_p;
    }

    /**
     * @param userId_p The userId to set.
     */
    public void setUserId(String userId_p)
    {
        m_userId = userId_p;
    }

    /**
     * @param userName_p The userName to set.
     */
    public void setUserName(String userName_p)
    {
        m_userName = userName_p;
    }

    /** get name of logged in user in the long descriptive form 
    * @return String user name
    */
    public String getUserLongName() throws Exception
    {
        return m_strLongUserName;
    }

    /** get the user login
     * @return String user login
     */
    public String getUserName() throws Exception
    {
        return m_userName;
    }

    /** get the user eMail Address
     * @return String user eMail
     */
    public String getUserEmailAdress() throws Exception
    {
        return m_emailAddress;
    }

    /** get the user unique persistent ID, which does not change even when name changes
     * @return String unique persistent ID
     */
    public String getUserID()
    {
        return m_userId;
    }

    /** get a string list of role names for this user
     * @return java.util.List of string role names
     */
    public java.util.Collection getRoleNames() throws Exception
    {

        return m_roleNames;
    }

    /** get a list with group info the user is assigned to
     * @return java.util.List of OwUserInfo
     */
    public java.util.Collection getGroups() throws Exception
    {
        OwDummyUserInfo group = new OwDummyUserInfo("DummyGroup");
        group.setGroup(true);

        Collection ret = new Vector();

        ret.add(group);

        return ret;
    }

    /**
     * @param roleNames_p The roleNames to set.
     */
    public void setRoleNames(Collection roleNames_p)
    {
        m_roleNames = new LinkedList(roleNames_p);
        m_roleNames.add(OwRole.OW_AUTHENTICATED);
    }

    /** flag indication, that user info actually designates a group rather than a single user
     * @return true = group otherwise user
     */
    public boolean isGroup() throws Exception
    {
        return m_isGroup;
    }

    public String getUserDisplayName() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUserShortName() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}