package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Displays and edits the privileges of a given {@link OwPermissionCollection}.
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
public class OwPrivilegesView extends OwView implements OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwPrivilegesView.class);

    public static final String PRIVILEGES_PARAM = "pv";

    private boolean m_readonly = false;
    private String message;
    private boolean liveUpdate;

    public OwPrivilegesView(OwUIGenericAccessRightsModul accessRightsView_p, boolean forceLiveUpdate_p)
    {
        this(accessRightsView_p, Collections.EMPTY_MAP, forceLiveUpdate_p);
    }

    public OwPrivilegesView(OwUIGenericAccessRightsModul accessRightsView_p, Map<String, String> privilegeDisplayNames_p, boolean forceLiveUpdate_p)
    {
        super();
        OwPermissionsDocument permissionsDocument = accessRightsView_p.getDocument();
        OwPrivilegesDocument doc = createPrivilegesDocument(permissionsDocument, privilegeDisplayNames_p);
        if (doc != null)
        {
            setDocument(doc);
        }
        this.liveUpdate = forceLiveUpdate_p;
    }

    /**(overridable)
     * Factory method to create the document which will be used by the current instance.
     * 
     * @param permissionsDocument_p 
     * @param privilegeDisplayNames_p 
     * @return OwPrivilegesDocument
     */
    protected OwPrivilegesDocument createPrivilegesDocument(OwPermissionsDocument permissionsDocument_p, Map<String, String> privilegeDisplayNames_p)
    {
        return new OwPrivilegesDocument(permissionsDocument_p, privilegeDisplayNames_p);
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        //        setExternalFormTarget(this);
    }

    @Override
    public OwPrivilegesDocument getDocument()
    {
        return (OwPrivilegesDocument) super.getDocument();
    }

    /**
     * OnSave {@link OwUIGenericAccessRightsModul} delegate for non live updating privileges views.
     * @param request_p
     * @param reason_p
     * @throws OwException
     * @since 3.1.0.0
     */
    public void onSave(HttpServletRequest request_p, Object reason_p) throws OwException
    {
        // void
    }

    /**
     * @param readOnly_p true if the privileges view should be read-only, false otherwise.
     * @since 3.1.0.0
     */
    public final void setReadOnly(boolean readOnly_p)
    {
        this.m_readonly = readOnly_p;
    }

    /**
     * @return the read only flag
     * @since 3.1.0.0
     */
    public boolean isReadOnly()
    {
        return this.m_readonly;
    }

    /**
     * Set the message to be displayed in the message region.
     * @param message_p - the message (can be null - nothing is displayed).
     * @since 3.1.0.0
     */
    public void setMessage(String message_p)
    {
        this.message = message_p;
    }

    /**
     * Get the message which should be displayed. 
     * @return String message, or null if not set
     * @since 3.1.0.3
     */
    public String getMessage()
    {
        return this.message;
    }

    protected void onRender(Writer w_p) throws Exception
    {
        super.onRender(w_p);
        serverSideDesignInclude("OwPrivilegesView.jsp", w_p);
    }

    public void onRemovePrivileges(HttpServletRequest request_p) throws Exception
    {

        String[] removedPrivilegesParams = request_p.getParameterValues(PRIVILEGES_PARAM);

        if (removedPrivilegesParams != null)
        {
            OwPrivilegesDocument document = getDocument();

            Collection<OwPrivilegeSet> appliedPrivilegesSets = document.getAppliedPrivilegeSets();

            List<OwPrivilegeSet> removedPrivilegeSets = new LinkedList<OwPrivilegeSet>();

            for (String privilegeParam : removedPrivilegesParams)
            {
                int splitIndex = privilegeParam.indexOf("_P");
                String guids = privilegeParam.substring(0, splitIndex);
                String principal = privilegeParam.substring(splitIndex + 2);
                String[] guidArray = guids.split("_");
                List<String> removedPrivileges = new LinkedList<String>();
                for (String guid : guidArray)
                {
                    removedPrivileges.add(OwPrivilegesDocument.guidToPrivilege(guid));
                }

                for (OwPrivilegeSet appliedSet : appliedPrivilegesSets)
                {
                    OwUserInfo appliedPrincipal = appliedSet.getPrincipal();

                    if (appliedPrincipal.getUserName().equals(principal))
                    {
                        Collection<OwPrivilege> appliedPrivileges = appliedSet.getPrivileges();
                        List<String> appliedNames = new LinkedList<String>();
                        for (OwPrivilege privilege : appliedPrivileges)
                        {
                            appliedNames.add(privilege.getName());
                        }

                        if (appliedNames.size() == removedPrivileges.size() && appliedNames.containsAll(removedPrivileges))
                        {
                            removedPrivilegeSets.add(appliedSet);
                        }

                    }
                }
            }

            for (OwPrivilegeSet removedSet : removedPrivilegeSets)
            {
                document.removePrivilegeSet(removedSet);
            }

        }

    }

    /** 
     * Handler to open add privilege dialog, called from JSP.  
     * @param request_p a HttpServletRequest object
     * @throws OwException if could not create or open dialog
     * @since 3.2.0.0
     */
    public void onOpenAddDialog(HttpServletRequest request_p) throws OwException
    {
        OwAddPrivilegeDialog addPrivilegeDialog = createAddPrivilegeDialog();
        try
        {
            getContext().openDialog(addPrivilegeDialog, this);
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (Exception e)
        {
            LOG.error("Could not open privilege dialog.", e);
            throw new OwServerException(getContext().localize("OwPrivilegesView.openAddDialog.error", "Could not open add privilege dialog."), e);
        }
    }

    /**(overridable)
     * Create a dialog which provide the possibility to create
     * new privilege entry. 
     * @return OwAddPrivilegeDialog
     * @throws OwException
     * @since 3.2.0.0
     */
    protected OwAddPrivilegeDialog createAddPrivilegeDialog() throws OwException
    {
        return new OwAddPrivilegeDialog(this);
    }

    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {

    }

    /**
     * Return if live update is
     * enabled or not. 
     * @return boolean
     * @since 3.2.0.0
     */
    public boolean liveUpdate()
    {
        return liveUpdate;
    }
}
