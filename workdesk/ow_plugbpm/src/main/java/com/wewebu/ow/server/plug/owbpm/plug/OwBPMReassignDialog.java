package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;

/**
 *<p>
 * Dialog for reassigning a work item to an inbox.
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
public class OwBPMReassignDialog extends OwBPMInsertNoteDialog implements OwDialog.OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMReassignDialog.class);

    public static final String DESTINATION_WEB_KEY = "destination";
    public static final String PUBLIC_CONTAINER_NAME_WEB_KEY = "publiccontname";
    public static final String DEFAULT_USER_SELECTION_WEB_KEY = "defaultuser";

    /** destination value for own inbox */
    public static final int DESTINATION_OWN_INBOX_VALUE = 1;
    /** destination value for inbox of another user  */
    public static final int DESTINATION_OTHER_INBOX_VALUE = 2;
    /** destination value for inbox of another public box  */
    public static final int DESTINATION_OTHER_PUBLICBOX_VALUE = 3;

    /** region */
    public static final int OWN_INBOX_REGION = INSERT_NOTE_DIALOG_REGION_MAX + 1;
    /** region */
    public static final int OTHER_INBOX_REGION = OWN_INBOX_REGION + 1;
    /** region */
    public static final int OTHER_PUBLICBOX_REGION = OTHER_INBOX_REGION + 1;

    /** currently selected receiver user name */
    private OwUserInfo m_receiverUser = null;

    /** currently selected destination */
    private int m_selectedDestination = DESTINATION_OWN_INBOX_VALUE;

    /** refresh context ref */
    private OwClientRefreshContext m_refreshCtx;

    /** parent container */
    private OwWorkitemContainer m_oParentContainer;

    private Map m_defaultUsersSelectMap = new HashMap();

    /**
     * create a new instance of the dialog
     * @throws OwException 
     */
    public OwBPMReassignDialog(List workitems_p, OwWorkitemContainer oParent_p, OwClientRefreshContext refreshCtx_p, String noteProperty_p) throws OwException
    {
        super(workitems_p, noteProperty_p, false);
        m_refreshCtx = refreshCtx_p;

        m_oParentContainer = oParent_p;
    }

    /**
     * create a new instance of the dialog
     * @param noteProperty_p 
     * @throws OwException 
     */
    public OwBPMReassignDialog(OwWorkitem workitem_p, OwWorkitemContainer oParent_p, OwClientRefreshContext refreshCtx_p, String noteProperty_p) throws OwException
    {
        super(workitem_p, noteProperty_p, false);
        m_refreshCtx = refreshCtx_p;

        m_oParentContainer = oParent_p;
    }

    /**
     * Updates the selected user from the default user select box if its value is found on the given request.
     * @param request_p
     * @throws Exception
     */
    private void updateDefaultUser(HttpServletRequest request_p) throws Exception
    {
        String defaultUserSelection = request_p.getParameter(DEFAULT_USER_SELECTION_WEB_KEY);
        if (defaultUserSelection != null)
        {
            OwUserInfo defaultUserInfo = (OwUserInfo) m_defaultUsersSelectMap.get(defaultUserSelection);
            m_receiverUser = defaultUserInfo;
        }
    }

    /** 
     * event called when user clicked the ok button  should be overloaded by specific Dialog
     *  @param request_p a {@link HttpServletRequest}
     *  @param reason_p
     */
    public void onOkDo(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        updateDefaultUser(request_p);

        // fetch the HTML parameters
        int selection = Integer.parseInt(request_p.getParameter(DESTINATION_WEB_KEY));

        // set note
        setNote(request_p);

        switch (selection)
        {
            case DESTINATION_OWN_INBOX_VALUE:
            {
                OwUserInfo userInfo = ((OwMainAppContext) getContext()).getCredentials().getUserInfo();
                doReassignToUserBox(userInfo.getUserID());
                break;
            }

            case DESTINATION_OTHER_INBOX_VALUE:
            {
                doReassignToUserBox(m_receiverUser.getUserID());
                break;
            }

            case DESTINATION_OTHER_PUBLICBOX_VALUE:
            {
                String sPublicContainerName = request_p.getParameter(PUBLIC_CONTAINER_NAME_WEB_KEY);
                doReassignToPublicBox(sPublicContainerName);
                break;
            }
        }
        getDocument().update(this, OwUpdateCodes.OBJECT_FORWARD, m_currentItem);
        closeDialog();

        // do a refresh of the changed queues
        if (null != m_refreshCtx)
        {
            m_refreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
        }
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case MAIN_REGION:
                this.serverSideDesignInclude("owbpm/OwReassign.jsp", w_p);
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    public boolean isRegion(int iRegion_p)
    {
        try
        {
            switch (iRegion_p)
            {
                case OTHER_INBOX_REGION:
                case OWN_INBOX_REGION:
                {
                    Iterator it = m_workitems.iterator();
                    while (it.hasNext())
                    {
                        OwWorkitem item = (OwWorkitem) it.next();
                        if (!item.canReassignToUserContainer(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            return false;
                        }
                    }

                    return true;
                }

                case OTHER_PUBLICBOX_REGION:
                {
                    Iterator it = m_workitems.iterator();
                    while (it.hasNext())
                    {
                        OwWorkitem item = (OwWorkitem) it.next();
                        if (!item.canReassignToPublicContainer(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            return false;
                        }
                    }

                    return true;
                }

                default:
                    return super.isRegion(iRegion_p);
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** get a display name for the given ID 
     * 
     * @param sID_p
     * @return String
     */
    public String getPublicDestinationDisplayName(String sID_p)
    {
        return m_oParentContainer.getPublicReassignContainerDisplayName(sID_p);
    }

    /** get a collection of public destination IDs 
     * @throws Exception */
    public Collection getPublicDestinations() throws Exception
    {
        return m_oParentContainer.getPublicReassignContainerNames();
    }

    /**
     * called from JSP to get role from role dialogue
     * 
     * @param request_p
     *            a HttpServletRequest object
     */
    public void onOpenUserDialog(HttpServletRequest request_p) throws Exception
    {
        // save radio button selection
        m_selectedDestination = Integer.parseInt(request_p.getParameter(DESTINATION_WEB_KEY));
        OwUserSelectDialog dlgUser = new OwUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_USER }, false);
        getContext().openDialog(dlgUser, this);
    }

    /** invoked when user select dialog is closed
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        // assuming there is just one user selected so take the first of the list
        List users = ((OwUserSelectDialog) dialogView_p).getSelectedUsers();
        if (users != null)
        {
            OwUserInfo user = (OwUserInfo) users.get(0);
            if (user != null)
            {
                m_receiverUser = user;
            }
        }

    }

    /**
     * @param radioButtonValue_p the value of the given radio button
     * @return "checked" String if the given radio button was selected
     */
    public String getCheckedString(int radioButtonValue_p)
    {
        if (m_selectedDestination == radioButtonValue_p)
        {
            return "checked";
        }
        else
        {
            return "";
        }
    }

    /**
     * do the reassign for all workitems
     * @param destination_p
     * @throws Exception 
     */
    public void doReassignToPublicBox(String destination_p) throws Exception
    {
        OwWorkitem item = null;

        for (Iterator iter = m_workitems.iterator(); iter.hasNext();)
        {
            item = (OwWorkitem) iter.next();

            try
            {

                // do the reassign
                item.reassignToPublicContainer(destination_p, false);

            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException(getContext().localize1("plug.owtaskp8.plugins.OwMoveDialog.destination.error", "Work item (%1) could not be forwarded.", item.getName()), e);
            }
        }
    }

    /**
     * do the reassign for all workitems
     * @param destination_p
     * @throws Exception 
     */
    @SuppressWarnings("rawtypes")
    public void doReassignToUserBox(String destination_p) throws Exception
    {
        OwWorkitem item = null;

        for (Iterator iter = m_workitems.iterator(); iter.hasNext();)
        {
            item = (OwWorkitem) iter.next();

            try
            {

                // do the reassign
                item.reassignToUserContainer(destination_p, false);

            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException(getContext().localize1("plug.owtaskp8.plugins.OwMoveDialog.destination.error", "Der Vorgang ( %1 ) konnte nicht weitergeleitet werden!", item.getName()), e);
            }
        }
    }

    /**
     * getter method for receiverUserName
     * 
     * @return Returns the receiverUserName.
     */
    public String getReceiverUserName()
    {
        try
        {
            if (m_receiverUser != null)
            {
                return m_receiverUser.getUserLongName();
            }
            else
            {
                return "";
            }

        }
        catch (Exception e)
        {
            LOG.error("OwBPMReassignDialog.getReceiverUserName: could not retrieve user name!", e);
            return "";
        }
    }

    /**
     * getter method for receiverUserName
     * 
     * @return Returns the receiverUserName.
     */
    public OwUserInfo getReceiverUser()
    {
        return m_receiverUser;
    }

    /**
     * unlock the workitems
     * 
     * @throws Exception
     */
    public void unlockAll() throws Exception
    {
        for (Iterator iter = this.m_workitems.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            object.setLock(false);
        }
    }

    /**
     * 
     * @return <code>true</code> if the default user selection is enabled - user can choose from a list of default users<br>
     *         <code>false</code> otherwise
     */
    public boolean isUserDefaultEnabled()
    {
        return m_oParentContainer.getDefaultUsers() != null;
    }

    /**
     *Returns a list of users to select from. If a user is selected using the {@link OwUserSelectDialog} 
     *this user will be included too.
     * 
     *@return a {@link List} of users to select from if default user selection is enabled<br>
     *        <code>null</code> if default user selection is not enabled
     */
    public List getDefaultUsers()
    {
        m_defaultUsersSelectMap.clear();

        Collection defaultUsers = m_oParentContainer.getDefaultUsers();
        if (defaultUsers != null)
        {
            List defaultUsersList = new ArrayList(defaultUsers);
            int usersCount = defaultUsersList.size();

            for (int i = 0; i < usersCount; i++)
            {
                OwUserInfo info = (OwUserInfo) defaultUsersList.get(i);
                m_defaultUsersSelectMap.put(info.getUserID(), info);
            }

            if (m_receiverUser != null && !m_defaultUsersSelectMap.containsKey(m_receiverUser.getUserID()))
            {
                defaultUsersList.add(m_receiverUser);
                m_defaultUsersSelectMap.put(m_receiverUser.getUserID(), m_receiverUser);
            }
            return defaultUsersList;
        }
        else
        {
            return null;
        }
    }

}