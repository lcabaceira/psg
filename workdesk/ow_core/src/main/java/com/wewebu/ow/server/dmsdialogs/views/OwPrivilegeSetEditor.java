package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * {@link OwPrivilegeSet} editor view for set-user and privilege selection to
 * be used with {@link OwPrivilegesView}.   
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
 *@since 3.2.0.0
 */
public class OwPrivilegeSetEditor extends OwView implements OwDialogListener
{
    /** size of recent list */
    private static final int RECENT_LIST_MAX_SIZE = 25;
    public static final String PRIVILEGES_PARAMETER = "sp";

    /** recent list of role names */
    private List<String> recentRoleNames = new ArrayList<String>();

    private OwUserSelectDialog dlgRole;
    private OwDialog dialog;

    public OwPrivilegeSetEditor(OwPrivilegesDocument privilegesDocument_p, OwDialog dialog_p)
    {
        super();
        OwPrivilegeSetEditorDocument document = new OwPrivilegeSetEditorDocument(privilegesDocument_p);
        setDocument(document);
        this.dialog = dialog_p;
    }

    @Override
    public OwPrivilegeSetEditorDocument getDocument()
    {
        return (OwPrivilegeSetEditorDocument) super.getDocument();
    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwPrivilegeSetEditor.jsp", w_p);
    }

    public List<String> getRecentRoleNames()
    {
        return recentRoleNames;
    }

    /** called from JSP to change to a recent role
     * @param request_p a HttpServletRequest object
     * @throws Exception 
     */
    public void onChangeToRecentRole(HttpServletRequest request_p) throws Exception
    {
        int recentId = -1;
        try
        {
            recentId = Integer.parseInt(request_p.getParameter("recentRoleId"));
        }
        catch (Exception e)
        {
            // invalid parameter. do nothing and return
            return;
        }
        if ((recentId >= 0) && (recentId < recentRoleNames.size()))
        {
            // get recent role name from recent list
            String recentRoleName = recentRoleNames.get(recentId);
            // remove all previous occurrences in the recent list
            recentRoleNames.remove(recentRoleName);
            // add to the end of the recent list
            recentRoleNames.add(recentRoleName);
            // crop the recent list to its maximum size
            while (recentRoleNames.size() > RECENT_LIST_MAX_SIZE)
            {
                recentRoleNames.remove(0);
            }
            // set new current role name
            OwPrivilegeSetEditorDocument document = getDocument();
            document.setCurrentRole(recentRoleName);
            //((OwConfigurationDocument) getDocument()).setRoleName(recentRoleName);
        }
    }

    /** called from JSP to get role from role dialogue
     * @param request_p a HttpServletRequest object
     * @throws Exception
     */
    public void onOpenRoleDialog(HttpServletRequest request_p) throws Exception
    {
        dlgRole = createOwUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_ROLE }, false);
        getContext().openDialog(dlgRole, this);
    }

    /**(overridable)
     * Factory methods for user select dialog, can be overwritten if needed.
     * This will be called by {@link #onOpenRoleDialog(HttpServletRequest)}
     * if a selection was triggered.
     * @param filter int array of OwUIUserSelectModul.TYPE_...
     * @param multiSelect boolean allow multi selection
     * @return OwUserSelectDialog
     * @throws OwException if could not instantiate dialog
     */
    protected OwUserSelectDialog createOwUserSelectDialog(int[] filter, boolean multiSelect) throws OwException
    {
        return new OwUserSelectDialog(filter, multiSelect);
    }

    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        List roles = ((OwUserSelectDialog) dialogView_p).getSelectedRoles();
        if (roles != null)
        {
            String selRole = (String) roles.get(0);
            if (selRole != null)
            {
                // remove all previous occurrences in the recent list
                recentRoleNames.remove(selRole);
                // add to the end of the recent list
                recentRoleNames.add(selRole);
                // crop the recent list to its maximum size
                while (recentRoleNames.size() > RECENT_LIST_MAX_SIZE)
                {
                    recentRoleNames.remove(0);
                }
                // set new current role name
                //((OwConfigurationDocument) getDocument()).setRoleName(selRole);
                OwPrivilegeSetEditorDocument document = getDocument();
                document.setCurrentRole(selRole);

            }
        }
        dlgRole = null;
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";//render new form
    }

    public void onAddPrivilege(HttpServletRequest request_p) throws Exception
    {
        String[] guids = request_p.getParameterValues(PRIVILEGES_PARAMETER);
        if (guids != null)
        {
            OwPrivilegeSetEditorDocument document = getDocument();
            String[] privileges = new String[guids.length];
            for (int i = 0; i < guids.length; i++)
            {
                privileges[i] = OwPrivilegesDocument.guidToPrivilege(guids[i]);
            }
            document.setAddedPrivileges(privileges);
        }
        if (this.dialog != null)
        {
            this.dialog.closeDialog();
        }
    }
}
