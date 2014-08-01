package com.wewebu.ow.server.app;

import java.util.List;

import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Base Class for the login sub module to be created in the network (DMS) Adaptor. Submodules are used to delegate
 * DMS specific User Interactions to the DMS Adaptor, which can not be generically solved. 
 * e.g.: Login or Access rights Dialog. <br/><br/>
 * To be implemented with the specific DMS system.
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
public class OwUserSelectDialog extends OwStandardDialog implements OwUIUserSelectModul.OwEventListner
{
    /** array of types to be displayed / filtered as defined in OwUIUserSelectModul */
    private int[] m_filterType;

    /** the selected users OwUserInfo to be returned */
    private List m_selectedUsers;
    /** the selected roles String to be returned */
    private List m_SelectedRoles;

    /** determine if multiple selection is allowed */
    private boolean m_fMultiselect = false;

    /** create a user select dialog
     * @param filterType_p array of types to be displayed / filtered as defined in OwUIUserSelectModul
     * @param fMultiselect_p true = allow multiple selection of users / groups / roles, false = allow only one selection
     */
    public OwUserSelectDialog(int[] filterType_p, boolean fMultiselect_p)
    {
        m_filterType = filterType_p;
        m_fMultiselect = fMultiselect_p;
    }

    public void init() throws Exception
    {
        super.init();

        if (((OwMainAppContext) getContext()).getNetwork().canUserSelect())
        {
            // create and add the user select module 
            OwUIUserSelectModul userView = ((OwMainAppContext) getContext()).getNetwork().getUserSelectSubModul(null, m_filterType);
            userView.setEventListner(this);
            userView.setMultiselect(m_fMultiselect);

            addView(userView, MAIN_REGION, null);

            StringBuffer strTitle = new StringBuffer();
            for (int i = 0; i < m_filterType.length; i++)
            {
                if (m_filterType[i] == OwUIUserSelectModul.TYPE_GROUP)
                {
                    strTitle.append(getContext().localize("app.OwUserSelectDialog.group", "Group"));
                }

                if (m_filterType[i] == OwUIUserSelectModul.TYPE_USER)
                {
                    strTitle.append(getContext().localize("app.OwUserSelectDialog.user", "User"));
                }

                if (m_filterType[i] == OwUIUserSelectModul.TYPE_ROLE)
                {
                    strTitle.append(getContext().localize("app.OwUserSelectDialog.role", "Role"));
                }

                if (i < (m_filterType.length - 1))
                {
                    strTitle.append(" / ");
                }
            }

            strTitle.append(" ");
            strTitle.append(getContext().localize("app.OwUserSelectDialog.title", "Selection"));
            setTitle(strTitle.toString());
        }
        else
        {
            throw new OwInvalidOperationException(getContext().localize("app.OwUserSelectDialog.userselectmodulnotavailable", "Users / roles cannot be selected."));
        }
    }

    /** get the selected user
     *
     * @return List of OwUserInfo or null if no user was selected
     */
    public List getSelectedUsers()
    {
        return m_selectedUsers;
    }

    /** get the selected user
     *
     * @return List of String rolename or null if no role was selected
     */
    public List getSelectedRoles()
    {
        return m_SelectedRoles;
    }

    /** called when user selected a role 
     * @param roleNames_p list of String
     */
    public void onSelectRoles(List roleNames_p) throws Exception
    {
        m_SelectedRoles = roleNames_p;
        closeDialog();
    }

    /** called when user selected a group or user 
     * @param users_p List of OwUserInfo
     */
    public void onSelectUsers(List users_p) throws Exception
    {
        m_selectedUsers = users_p;
        closeDialog();
    }

}