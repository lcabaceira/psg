package com.wewebu.ow.server.ecmimpl.fncm5.ui;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PermissionSource;
import com.filenet.api.security.AccessPermission;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwComboboxRendererFactory;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwMenu;

/**
 *<p>
 * P8 5.x AccessRightsModul.<br/>
 * Implementation for the access rights sub module of FileNet P8 5.x.
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
public class OwFNCM5UIAccessRightsModul extends OwUIAccessRightsModul implements OwDialog.OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNCM5UIAccessRightsModul.class);

    /** query key for the selected permission */
    protected static final String PERMISSION_INDEX_KEY = "pindex";
    /** query key for the selected access right within a permission */
    protected static final String ACE_INDEX_KEY = "aindex";
    /** query key for the inheritance combobox/choicelist */
    protected static final String ID_CMB_KEY = "cmbId";
    /** query key for the combobox/choicelist selection value*/
    protected static final String VAL_CMB_KEY = "cmbVal";

    /** permissions to edit */
    protected OwFNCM5Permissions perms;

    /** optional object with permissions */
    protected OwObject obj;

    /** make the access rights view read-only
     * true = user can only view the access rights, 
     * false = user can edit the access rights (default) **/
    protected boolean readOnly;

    /** the network */
    protected OwFNCM5Network network;

    /** menu */
    protected OwSubMenuView m_menu;

    /** menu index of the save button */
    protected int m_iSaveMenuBtnIndex;
    /** menu index of the reset button */
    protected int m_iResetMenuBtnIndex;

    private OwFNCM5UIAccessRightsModel model;

    /** construct a access rights module for OwFNCMObject 
     *
     * @param network Network
     * @param obj the OwFNCMObject which permissions to be read
     */
    public OwFNCM5UIAccessRightsModul(OwFNCM5Network network, OwObject obj) throws OwInvalidOperationException
    {
        this.network = network;
        this.obj = obj;
        try
        {
            perms = (OwFNCM5Permissions) obj.getPermissions();
        }
        catch (ClassCastException ex)
        {
            String msg = "Invalid OwPermissionCollection class, must be of type OwFNCM5Permissions!";
            LOG.error(msg, ex);
            throw new OwInvalidOperationException(msg, ex);
        }
        catch (Exception e)
        {
            String msg = "Error retriving OwPermissionCollection from object, needed for the OwFNCM5UIAccessRightsModul...";
            LOG.error(msg, e);
            throw new OwInvalidOperationException(network.getContext().localize("OwFNCM5UIAccessRightsModul.error.init", "Could not retrieve permissions from object."), e);
        }
    }

    /**
     * Constructor for access module with specific model 
     * @param network OwFNCM5Network
     * @param obj OwObject
     * @param model OwFNCM5UIAccessRightsModel
     * @throws OwInvalidOperationException
     */
    public OwFNCM5UIAccessRightsModul(OwFNCM5Network network, OwObject obj, OwFNCM5UIAccessRightsModel model) throws OwInvalidOperationException
    {
        this(network, obj);
        setModel(model);
    }

    /**
     * Set the model which should be used by this module.
     * <p>Attention: If this method is called after {@link #init()}
     * method, the model must be initialized by it's own.</p> 
     * @param model OwFNCM5UIAccessRightsModel
     */
    public void setModel(OwFNCM5UIAccessRightsModel model)
    {
        this.model = model;
    }

    /**
     * Get the current used model for this access module.
     * @return OwFNCM5UIAccessRightsModel or null
     */
    public OwFNCM5UIAccessRightsModel getModel()
    {
        return this.model;
    }

    /** get the menu of the access rights module
     * 
     * @return OwMenu or null if not defined
     */
    public OwMenu getMenu()
    {
        return this.m_menu;
    }

    /** Initialization the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_menu = new OwSubMenuView();
        addView(m_menu, null);

        // save button
        m_iSaveMenuBtnIndex = m_menu.addMenuItem(this, getContext().localize("OwFNCM5UIAccessRightsModul.btn.save", "Save"), "Save", null);
        m_menu.enable(m_iSaveMenuBtnIndex, false);
        // reset button
        m_iResetMenuBtnIndex = m_menu.addMenuItem(this, getContext().localize("OwFNCM5UIAccessRightsModul.btn.reset", "Reset"), "Reset", null);
        m_menu.enable(m_iResetMenuBtnIndex, false);

        // new button
        m_menu.addMenuItem(this, getContext().localize("OwFNCM5UIAccessRightsModul.btn.new", "New"), "New", null);

        if (getModel() != null)
        {
            this.model.init(getContext());
        }
        else
        {
            OwFNCM5UIAccessRightsModel mod = new OwFNCM5UIAccessRightsModel(this.obj.getType());
            mod.init(getContext());
            setModel(mod);
        }

    }

    /**
     * Called when permissions were modified
     * @param modified_p boolean
     */
    private void setModified(boolean modified_p)
    {
        // enable when access right was changed
        m_menu.enable(m_iSaveMenuBtnIndex, modified_p);
        m_menu.enable(m_iResetMenuBtnIndex, modified_p);
    }

    /** make the access rights view read-only
     * @param fReadOnly_p true = user can only view the access rights, false = user can edit the access rights (default)
     */
    public void setReadOnly(boolean fReadOnly_p)
    {
        this.readOnly = fReadOnly_p;
    }

    public void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"OwAccessRights\">");
        w_p.write("<table class=\"OwGeneralList_table\"><caption>");
        w_p.write(getContext().localize("OwFNCM5UIAccessRightsModul.captionTable", "This table contain access rights list"));
        // === Header
        w_p.write("</caption><thead><tr class=\"OwGeneralList_header\">");
        // Access rights header
        for (OwFNCM5ColumnHeader header : getModel().getHeaders())
        {
            w_p.write("<th class=\"OwGeneralList_sort\">");
            w_p.write(getModel().getHeaderDisplayName(header));
            w_p.write("</th>");
        }
        w_p.write("</tr></thead>");

        // === Permission Entries
        w_p.write("<tbody>");
        Iterator<?> itPerms = perms.getNativeObject().iterator();
        for (int iPermissionIndex = 0; itPerms.hasNext(); iPermissionIndex++)
        {
            AccessPermission p = (AccessPermission) itPerms.next();
            if (readOnly)
            {
                renderPermissionRow(w_p, p, iPermissionIndex, true);
            }
            else
            {
                PermissionSource ps = p.get_PermissionSource();
                renderPermissionRow(w_p, p, iPermissionIndex, !(ps.getValue() == PermissionSource.SOURCE_DEFAULT_AS_INT ^ ps.getValue() == PermissionSource.SOURCE_DIRECT_AS_INT));
            }
        }
        w_p.write("</tbody>");
        w_p.write("</table>");

        if (getModel().getHeaders().contains(OwFNCM5ColumnHeader.DEPTH))
        {
            StringBuilder function = new StringBuilder("<script type=\"text/javascript\">\nfunction cmbSelection(cmb, record, index){\n");
            String url = getEventURL("ModifyInheritanceRight", null);
            function.append("var strUrl =\"").append(url).append("\";\n");
            function.append("strUrl = strUrl + \"&").append(ID_CMB_KEY);
            function.append("=\" + cmb.hiddenId +\"&").append(VAL_CMB_KEY).append("=\" + record.data.value;\n");
            function.append("window.location.href=strUrl;");
            function.append("}\n</script>");
            w_p.write(function.toString());
        }
        // === render menu
        if (!readOnly)
        {
            w_p.write("<div class=\"OwButtonBar\">");
            m_menu.render(w_p);
            w_p.write("</div>");
        }

        w_p.write("</div>");
    }

    /**
     * Render a row which represents the access rights, of given permission.
     * The row can be controlled in which manner it is rendered, read-only or not.
     * @param w_p Writer to use for representation
     * @param perm_p Permission to be rendered
     * @param iPermIdx_p int index of the permission
     * @param readOnly_p boolean flag notifying if the row should be read-only or not
     * @throws Exception if cannot write to writer or retrieve additional information
     */
    protected void renderPermissionRow(Writer w_p, AccessPermission perm_p, int iPermIdx_p, boolean readOnly_p) throws Exception
    {
        String strRowClassName = ((iPermIdx_p % 2) != 0) ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd";
        // === start row
        w_p.write("<tr class=\"");
        w_p.write(strRowClassName);
        w_p.write("\">");

        for (OwFNCM5ColumnHeader header : getModel().getHeaders())
        {
            w_p.write("<td>");
            switch (header.getId())
            {
                case OwFNCM5ColumnHeader.PRINCIPAL_ICON_INT:
                    renderPermissionIcon(w_p, perm_p, iPermIdx_p, readOnly_p);
                    break;//Grantee type icon
                case OwFNCM5ColumnHeader.PRINCIPAL_NAME_INT:
                    renderPermissionName(w_p, perm_p, iPermIdx_p, readOnly_p);
                    break;//Grantee name
                case OwFNCM5ColumnHeader.DELETE_BTN_INT:
                    renderDeleteButton(w_p, perm_p, iPermIdx_p, readOnly_p);
                    break;//insert delete button if available
                case OwFNCM5ColumnHeader.DEPTH_INT:
                    renderInheritanceDepth(w_p, perm_p, iPermIdx_p, readOnly_p);
                    break;//Inheritable depth

                default:
                    renderPermissionState(w_p, perm_p, iPermIdx_p, readOnly_p, header);
            }
            w_p.write("</td>");
        }
        w_p.write("</tr>");
    }

    /**(overridable)
     * Rendering an icon depending on the given permission object. 
     * @param w_p Writer to write icon/image
     * @param perm_p AccessPermission for which an icon/image should be rendered
     * @param permIdx_p int index of the permission in the permission collection
     * @param readOnly_p boolean is current read only mode
     * @throws Exception if problem with Writer or retrieve information from permission
     */
    protected void renderPermissionIcon(Writer w_p, AccessPermission perm_p, int permIdx_p, boolean readOnly_p) throws Exception
    {
        String iconUrl = getModel().getPrincipalIcon(perm_p);
        String toolTip = getModel().getPrincipalIconTooltip(perm_p);

        if (iconUrl != null && iconUrl.length() > 0)
        {
            if (toolTip == null)
            {
                toolTip = iconUrl;
            }
            w_p.write("<img src=\"");
            w_p.write(iconUrl);
            w_p.write("\" alt=\"");
            w_p.write(toolTip);
            w_p.write("\" title=\"");
            w_p.write(toolTip);
            w_p.write("\" />");
        }
        else
        {
            w_p.write("?");
        }
    }

    /**(overridable)
     * Method to render the name column of the given permission object.
     * Called by {@link #renderPermissionRow(Writer, AccessPermission, int, boolean)} when the
     * name of the permission should be rendered.
     * @param w_p Writer to use for rendering
     * @param perm_p AccessPermission where to retrieve the name
     * @param permIdx_p int index of permission object in the collection
     * @param readOnly_p boolean if current permission is rendered in read-only mode
     * @throws Exception
     */
    protected void renderPermissionName(Writer w_p, AccessPermission perm_p, int permIdx_p, boolean readOnly_p) throws Exception
    {
        w_p.write(perm_p.get_GranteeName());
        if (perm_p.get_PermissionSource() == PermissionSource.SOURCE_TEMPLATE)
        {
            String securityPolicyImageTooltip = getContext().localize("OwFNCM5UIAccessRightsModul.icon.template.desc", "Defined by security template");

            w_p.write("<img style=\"vertical-align:middle;\" src=\"");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/shield_yellow.png\" alt=\"");
            w_p.write(securityPolicyImageTooltip);
            w_p.write("\" title=\"");
            w_p.write(securityPolicyImageTooltip);
            w_p.write("\" />");
        }
    }

    /**(overridable)
     * Render a button if the row is not read-only.
     * @param w_p Writer to render
     * @param perm_p AccessPermission to work on
     * @param permIdx_p int index of permission in collection
     * @param readOnly_p boolean is row read-only
     * @throws OwException
     */
    protected void renderDeleteButton(Writer w_p, AccessPermission perm_p, int permIdx_p, boolean readOnly_p) throws OwException
    {
        if (!readOnly_p)
        {// === render delete button only if not read-only mode
            String deletePermissionTooltip = getContext().localize1("OwFNCM5UIAccessRightsModul.delete.desc", "Delete %1", perm_p.get_GranteeName());
            try
            {
                w_p.write("<a title=\"");
                w_p.write(deletePermissionTooltip);
                w_p.write("\" href=\"");
                w_p.write(getEventURL("DeletePermission", PERMISSION_INDEX_KEY + "=" + String.valueOf(permIdx_p)));
                w_p.write("\">");

                w_p.write("<img style=\"border:0px none;\" src=\"");
                w_p.write(getContext().getDesignURL());
                w_p.write("/images/deletebtn.png\"");
                w_p.write(" alt=\"");
                w_p.write(deletePermissionTooltip);
                w_p.write("\" title=\"");
                w_p.write(deletePermissionTooltip);
                w_p.write("\" /></a>");
            }
            catch (IOException e)
            {
                LOG.fatal("Cannot render component using Writer", e);
                throw new OwServerException("Cannot render component", e);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                String msg = "Cannot get the design directory for rendering";
                LOG.error(msg, e);
                throw new OwServerException(msg, e);
            }
        }
    }

    /**
     * Render inheritance depth of current given permission.
     * P8 Permission Inheritance is defined as follows (extract from P8 API description):
     *  &gt; 1 : 2 means this object, its immediate children and grandchildren only can inherit the permission<br/>
     *  = 1 : this object and immediate children only<br/>
     *  = 0 : this object only no inheritance<br/>
     *  = -1: this object included and all sub children (children, grandchildren, grand-grandchildren ...)<br/>
     *  = -2: All children (infinite levels deep) but not the object itself<br/>
     *  = -3: only immediate children, but without the current object<br/>
     *  &lt; -3: -4 means only immediate children and grandchildren of the object inherit the permission, but not the object itself<br/>
     * @param w_p Writer for providing the rendering
     * @param perm_p AccessPermission which is currently handled
     * @param permIdx_p int index of permission in the permission collection
     * @param readOnly_p boolean can this permission be edited
     * @throws IOException
     * @throws OwException
     */
    protected void renderInheritanceDepth(Writer w_p, AccessPermission perm_p, int permIdx_p, boolean readOnly_p) throws IOException, OwException
    {
        String value = perm_p.get_InheritableDepth().toString();

        OwComboboxRendererFactory cmbFactory = getContext().getRegisteredInterface(OwComboboxRendererFactory.class);

        OwComboboxRenderer cmb = cmbFactory.createComboboxRenderer();
        String[] values = new String[10];
        String[] displays = new String[10];

        for (int i = 0; i < 5; i++)
        {
            values[i] = String.valueOf(i - 3);
            displays[i] = getInheritanceLable("OwFNCM5UIAccessRightsModul.inheritance.depth" + String.valueOf(i - 3), i);
        }

        OwDefaultComboModel cmbModel = new OwDefaultComboModel(value, values, displays);
        cmb.setModel(cmbModel);
        cmb.setFieldId("cmb_" + permIdx_p);
        cmb.setEnabled(!readOnly);
        cmb.addEvent("change", "cmbSelection");
        try
        {
            cmb.renderCombo(w_p);
        }
        catch (Exception e)
        {
            String msg = "Could not render combo-box for element";
            LOG.error(msg, e);
            throw new OwServerException(msg, e);
        }
    }

    protected String getInheritanceLable(String localizeKey, int idx)
    {
        return getContext().localize(localizeKey, Integer.toString(idx));
    }

    protected void renderPermissionState(Writer w_p, AccessPermission perm_p, int permIdx_p, boolean readOnly_p, OwFNCM5ColumnHeader header_p) throws Exception
    {
        // === Access rights
        if (!readOnly_p)
        {
            w_p.write("<a title=\"");
            w_p.write(getContext().localize("OwFNCM5UIAccessRightsModul.modifyrighttip", "Change access right"));
            w_p.write("\" href=\"");
            w_p.write(getEventURL("ModifyAccessRight", new StringBuffer(ACE_INDEX_KEY).append("=").append(String.valueOf(header_p.getId())).append("&").append(PERMISSION_INDEX_KEY).append("=").append(permIdx_p).toString()));
            w_p.write("\">");
        }

        boolean enabled = isPermissionSelected(perm_p, header_p);

        String accessRightImageTooltip;
        if (enabled)
        {
            accessRightImageTooltip = !readOnly_p ? getContext().localize1("OwFNCM5UIAccessRightsModul.modify.access.right.disable", "Disable access right for %1 ", perm_p.get_GranteeName()) : getContext().localize(
                    "OwFNCM5UIAccessRightsModul.access.right.status.enabled", "Access allowed");
        }
        else
        {
            accessRightImageTooltip = !readOnly_p ? getContext().localize1("OwFNCM5UIAccessRightsModul.modify.access.right.enable", "Enable access right for %1 ", perm_p.get_GranteeName()) : getContext().localize(
                    "OwFNCM5UIAccessRightsModul.access.right.status.disabled", "Access denied");
        }

        w_p.write("<img style=\"border:0px none;\" src=\"");
        w_p.write(getModel().getAclIcon(perm_p.get_AccessType() == AccessType.ALLOW, enabled));
        w_p.write("\" alt=\"");
        w_p.write(accessRightImageTooltip);
        w_p.write("\" title=\"");
        w_p.write(accessRightImageTooltip);
        w_p.write("\" />");

        if (!readOnly_p)
        {
            w_p.write("</a>");
        }

    }

    /**
     * Method called to get the selection state of a specific permission state.
     * <p>Check the current AccessMask with the header specific mask 
     * if the conjunction is not empty (!= 0).</p>
     * @param perm AccessPermission
     * @param header OwFNCM5ColumnHeader
     * @return true if active/selected, else false
     */
    protected boolean isPermissionSelected(AccessPermission perm, OwFNCM5ColumnHeader header)
    {
        return (perm.get_AccessMask() & header.getAccessMask()) != 0;
    }

    /**(overridable)
     * Called when user clicked save to save the modified permissions
     * @param request HttpServletRequest which was triggered
     * @param reason Object
     * @throws OwException
     * @throws IOException
     */
    public void onSave(HttpServletRequest request, Object reason) throws OwException, IOException
    {
        try
        {
            this.obj.setPermissions(perms);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String msg = "An unknown error occured on server while saving.";
            LOG.debug(msg, e);
            throw new OwServerException(getContext().localize("OwFNCM5UIAccessRightsModul.error.save", "Unknown error occurred while saving."), e);
        }
        // set modified when access right was changed
        setModified(false);
    }

    /**(overridable)
     * Called when reset button was clicked to reset the modified permissions
     * @param request HttpServletRequest
     * @param reason
     * @throws Exception
     */
    public void onReset(HttpServletRequest request, Object reason) throws Exception
    {
        perms = (OwFNCM5Permissions) obj.getPermissions();
        // set modified false
        setModified(false);
    }

    /**(overridable)
     * Called when new button was clicked to add a new permission.
     * Will open a Dialog where first a user or group can be selected.
     * @param request HttpServletRequest
     * @param reason Object
     * @throws Exception
     */
    public void onNew(HttpServletRequest request, Object reason) throws Exception
    {
        // === select user
        // open user select dialog
        OwUserSelectDialog userSelectDlg = new OwUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_USER, OwUIUserSelectModul.TYPE_GROUP }, true);
        getContext().openDialog(userSelectDlg, this);
    }

    /* 
     * OwDialogListener implementation for User select dialog.
     * Called if the Dialog that was opened by this view 
     * is closed.
     * @param dialogView_p the Dialog that closed.
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        List<?> users = ((OwUserSelectDialog) dialogView_p).getSelectedUsers();
        if (null != users && !users.isEmpty())
        {
            Iterator<?> it = users.iterator();
            while (it.hasNext())
            {
                OwBaseUserInfo userinfo = (OwBaseUserInfo) it.next();
                perms.createNewEntry(userinfo);
            }
            setModified(true);
        }
    }

    /**(overridable)
     * Called when delete button was clicked, to remove one of the permission entries.
     * @param request_p HttpServletRequest
     * @throws Exception
     */
    public void onDeletePermission(HttpServletRequest request_p) throws Exception
    {
        int permIdx = Integer.parseInt(request_p.getParameter(PERMISSION_INDEX_KEY));

        perms.getNativeObject().remove(permIdx);

        // set modified when access right was changed
        setModified(true);
    }

    /**
     * Called when user clicked on an inheritance to modify it. 
     * @param request_p
     * @throws Exception
     */
    public void onModifyInheritanceRight(HttpServletRequest request_p) throws Exception
    {
        String id = request_p.getParameter(ID_CMB_KEY);
        String strVal = request_p.getParameter(VAL_CMB_KEY);
        if (id != null && strVal != null)
        {
            int idx = Integer.valueOf(id.substring(4));
            int value = Integer.valueOf(strVal);
            ((AccessPermission) perms.getNativeObject().get(idx)).set_InheritableDepth(Integer.valueOf(value));
            setModified(true);
        }
    }

    /**(overridable)
     * Called when user clicked on an access right to modify it.
     * @param req HttpServletRequest which triggered that call
     * @throws Exception
     */
    public void onModifyAccessRight(HttpServletRequest req) throws Exception
    {
        int permIdx = Integer.parseInt(req.getParameter(PERMISSION_INDEX_KEY));//row
        int aclIdx = Integer.parseInt(req.getParameter(ACE_INDEX_KEY));//column

        for (OwFNCM5ColumnHeader header : getModel().getHeaders())
        {
            if (header.getId() == aclIdx)
            {
                AccessPermission perm = (AccessPermission) perms.getNativeObject().get(permIdx);
                int current = perm.get_AccessMask();
                if ((current & header.getAccessMask()) != 0)
                {
                    if ((current & header.getAccessMask()) == header.getAccessMask())
                    {//Permission already exist, this must be a reset click
                        perm.set_AccessMask(current & ~header.getAccessMask());
                    }
                    else
                    {//Permission is mixed with other access mask, add difference
                        perm.set_AccessMask(current | header.getAccessMask());
                    }
                }
                else
                {//Permission not set, now add new permission
                    perm.set_AccessMask(current | header.getAccessMask());
                }
                // set modified when access right was changed
                setModified(true);
                break;
            }
        }
    }
}