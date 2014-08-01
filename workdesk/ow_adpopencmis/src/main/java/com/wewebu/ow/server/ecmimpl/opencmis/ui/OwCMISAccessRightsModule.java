package com.wewebu.ow.server.ecmimpl.opencmis.ui;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.ui.acl.OwCMISPrivilegeEditorDocument;
import com.wewebu.ow.server.ecmimpl.opencmis.ui.acl.OwCMISPrivilegeSetEditor;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwMenu;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwCMISAccessRightsModule which will provide depending 
 * on the configuration what selection will be available. 
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
public class OwCMISAccessRightsModule extends OwUIAccessRightsModul<OwCMISNetwork> implements OwDialogListener
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAccessRightsModule.class);

    public static final String HEADER_PRINCIPAL = "Principal";
    public static final String HEADER_ACE = "Ace(s)";
    public static final String HEADER_ISDIRECT = "isDirect";

    /**Default list of Headers which will be rendered*/
    protected static final List<String> DEFAULT_HEADERS = new LinkedList<String>();
    static
    {
        DEFAULT_HEADERS.add(HEADER_PRINCIPAL);
        DEFAULT_HEADERS.add(HEADER_ACE);
        DEFAULT_HEADERS.add(HEADER_ISDIRECT);
    }

    /**Name of property for selected rows*/
    public static final String EDIT_ROW = "row";

    private boolean readOnly;
    private OwObject permObj;
    private OwCMISPermissionCollection permissions;
    private OwMenu menu;
    private boolean addDialog;
    private List<String> previousSelectedPrincipals;

    public OwCMISAccessRightsModule(OwObject object) throws OwException
    {
        super();
        permObj = object;
    }

    @Override
    public void init() throws Exception
    {
        super.init();
        menu = new OwSubMenuView();
        addView(menu, null);
        createActions(menu);
        permissions = (OwCMISPermissionCollection) this.permObj.getPermissions();
    }

    @Override
    public void setReadOnly(boolean readOnly_p)
    {
        this.readOnly = readOnly_p;
    }

    public boolean isReadOnly()
    {
        return this.readOnly;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"OwCMISAccessRightsModule OwUIAccessRightsModul\">");
        w_p.write("<table class=\"OwGeneralList_table\"><thead><tr>");
        List<String> headers = getHeaderList();
        OwNetworkContext nctx = getNetwork().getContext();

        w_p.write("<th></th>");//first selection, afterwards the data columns
        for (String header : headers)
        {
            w_p.write("<th>");
            w_p.write(nctx.localizeLabel(header));
            w_p.write("</th>");
        }
        w_p.write("</tr></thead>\n<tbody>");

        Iterator<OwPrivilegeSet> itSets = getPermissions().getAppliedPrivilegeSets().iterator();
        for (int rowIdx = 0; itSets.hasNext(); rowIdx += 1)
        {
            OwPrivilegeSet set = itSets.next();
            w_p.write("<tr class=\"");
            w_p.write(rowIdx % 2 == 0 ? "EvenRow" : "OddRow");
            w_p.write("\">");
            w_p.write("<td>");
            renderSelectionColumn(w_p, set, rowIdx);
            w_p.write("</td>");
            for (int colIdx = 0; colIdx < headers.size(); colIdx += 1)
            {
                w_p.write("<td>");
                renderRowValue(w_p, set, headers.get(colIdx), colIdx, rowIdx);
                w_p.write("</td>");
            }
            w_p.write("</tr>\n");
        }
        w_p.write("</tbody></table>");
        w_p.write("<div class=\"OwInlineMenu\" >");
        super.onRender(w_p);
        w_p.write("</div></div>");
    }

    protected void renderSelectionColumn(Writer w_p, OwPrivilegeSet set, int rowIdx) throws IOException
    {
        if (!isReadOnly() && set.getInheritanceDepth() == OwPrivilegeSet.INHERITANCE_DEPTH_NO_INHERITANCE)//direct
        {
            w_p.write("<input type=\"checkbox\" id=\"");
            w_p.write(EDIT_ROW);
            w_p.write("_");
            w_p.write(Integer.toString(rowIdx));
            w_p.write("\" name=\"");
            w_p.write(EDIT_ROW);
            w_p.write("\" value=\"");
            w_p.write(Integer.toString(rowIdx));
            w_p.write("\" />");
        }
    }

    protected void renderRowValue(Writer w_p, OwPrivilegeSet set, String header, int colIdx, int rowIdx) throws IOException
    {
        if (HEADER_ISDIRECT.equals(header))
        {
            w_p.write("<input type=\"checkbox\" id=\"");
            w_p.write(new StringBuilder(header).append("_").append(rowIdx).append("_").append(colIdx).toString());
            w_p.write("\" ");
            if (set.getInheritanceDepth() == OwPrivilegeSet.INHERITANCE_DEPTH_NO_INHERITANCE)//direct
            {
                w_p.write("checked");
            }
            w_p.write(" disabled />");
        }
        else
        {
            if (HEADER_PRINCIPAL.equals(header))
            {
                try
                {
                    w_p.write(set.getPrincipal().getUserDisplayName());
                }
                catch (IOException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    LOG.warn("Could not get Information of Principal object", e);
                    w_p.write("Access to Principal object failed.");
                }
            }
            else if (HEADER_ACE.equals(header))
            {
                Iterator<?> it = set.getPrivileges().iterator();
                while (it.hasNext())
                {
                    OwPrivilege priv = (OwPrivilege) it.next();
                    w_p.write(getContext().localize(OwString.LABEL_PREFIX + priv.getName(), priv.getName()));
                    w_p.write("<br />");
                }
            }
        }
    }

    protected void createActions(OwMenu menu) throws Exception
    {
        if (!isReadOnly())
        {
            menu.addFormMenuItem(this, getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.name.add", "Add"), "Add", getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.tooltip.add", "Add new entry"));
            menu.addFormMenuItem(this, getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.name.modify", "Modify"), "Modify", getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.tooltip.Modify", "Modify selected entry"));
            menu.addFormMenuItem(this, getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.name.delete", "Delete"), "Delete", getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.tooltip.delete", "Delete selected"));
            if (getObject().canSetPermissions())
            {
                menu.addFormMenuItem(this, getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.name.save", "Save"), "Save", getContext().localize("opencmis.ui.OwCMISAccessRightsModule.btn.tooltip.save", "Save changes"));
            }
        }
    }

    @Override
    public OwMenu getMenu()
    {
        return menu;
    }

    /**
     * Get the permission collection which is handled currently.
     * May return null if view was not initialized.
     * @return OwPermissionCollection (or null)
     * @see #getObject()
     */
    protected OwCMISPermissionCollection getPermissions()
    {
        return this.permissions;
    }

    /**
     * Get the Object where permission will be modified/viewed.
     * @return OwObject
     */
    protected OwObject getObject()
    {
        return this.permObj;
    }

    protected String usesFormWithAttributes()
    {
        return "";//render new form for this View
    }

    /**
     * List of header names/id's, defining also order of the values.
     * <p>Will return {@link #DEFAULT_HEADERS} list by default.</p>
     * @return List of header which should be rendered elements.
     */
    protected List<String> getHeaderList()
    {
        return new LinkedList<String>(DEFAULT_HEADERS);
    }

    /**
     * Handling method for Save events
     * @param req HttpServletRequest of save event
     * @param reason Object (optional parameter can be null)
     * @throws OwException
     */
    public void onSave(HttpServletRequest req, Object reason) throws OwException
    {
        try
        {
            this.permObj.setPermissions(getPermissions());
            this.permissions = (OwCMISPermissionCollection) this.permObj.getPermissions();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not save permission collection", e);
            throw new OwServerException(getContext().localize("opencmis.ui.OwCMISAccessRightsModule.err.save", "Could not save permissions in object"), e);
        }
    }

    /**
     * Handling method for Add/Create events
     * @param req HttpServletRequest of add/create event
     * @param reason Object (optional parameter can be null)
     * @throws OwException
     */
    public void onAdd(HttpServletRequest req, Object reason) throws OwException
    {
        try
        {
            OwCMISPrivilegeSetEditor editorDlg = createEditor();
            OwCMISPrivilegeEditorDocument doc = createEditorDocument(getPermissions(), null);
            if (getPreviousSelectedPrincipals() != null)
            {
                doc.setPrincipalSelection(getPreviousSelectedPrincipals());
            }
            editorDlg.setDocument(doc);
            getContext().openDialog(editorDlg, this);
            addDialog = true;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Cannot open Dialog", e);
            throw new OwServerException(getContext().localize("opencmis.ui.OwCMISAccessRightsModule.err.onAdd", "Could not open editor dialog"), e);
        }

    }

    /**
     * Modify handler method
     * @param req HttpServletRequest of modify event
     * @param reason Object (optional parameter can be null)
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    public void onModify(HttpServletRequest req, Object reason) throws OwException
    {
        String[] rows = req.getParameterValues(EDIT_ROW);
        if (rows != null && rows.length > 0)
        {
            List<String> selectedRows = Arrays.asList(rows);
            List<OwPrivilegeSet> sets = new LinkedList<OwPrivilegeSet>();
            Iterator<OwPrivilegeSet> itSets = getPermissions().getAppliedPrivilegeSets().iterator();
            for (int rowIdx = 0; itSets.hasNext(); rowIdx++)
            {
                OwPrivilegeSet set = itSets.next();
                if (selectedRows.contains(Integer.toString(rowIdx)))
                {
                    sets.add(set);
                }
            }
            try
            {
                OwCMISPrivilegeSetEditor editorDlg = createEditor();
                editorDlg.setDocument(createEditorDocument(getPermissions(), sets));
                getContext().openDialog(editorDlg, this);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Cannot open Dialog", e);
                throw new OwServerException(getContext().localize("opencmis.ui.OwCMISAccessRightsModule.err.onAdd", "Could not open editor dialog"), e);
            }
        }
    }

    /**
     * Handling method for PrivilegeSet deletion event.
     * @param req HttpServletRequest of delete event
     * @param reason Object (optional parameter can be null)
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    public void onDelete(HttpServletRequest req, Object reason) throws OwException
    {
        String[] rows = req.getParameterValues(EDIT_ROW);
        if (rows != null && rows.length > 0)
        {
            List<String> deletedRows = Arrays.asList(rows);
            Iterator<OwPrivilegeSet> itSets = permissions.getAppliedPrivilegeSets().iterator();
            for (int rowIdx = 0; itSets.hasNext(); rowIdx += 1)
            {
                OwPrivilegeSet set = itSets.next();
                if (deletedRows.contains(Integer.toString(rowIdx)))
                {
                    this.permissions.removePrivilegeSet(set);
                }
            }
        }
    }

    /**(overridable)
     * Create an editor which will be used for adding or modifying PrivilegeSet's.
     * @return OwCMISPrivilegeSetEditor instance
     */
    protected OwCMISPrivilegeSetEditor createEditor()
    {
        return new OwCMISPrivilegeSetEditor();
    }

    /**(overridable)
     * Create document for editor instance.
     * @param perms OwCMISPermissionCollection current permission collection
     * @param modifySet List of PrivilegeSet-objects (can be null)
     * @return OwCMISPrivilegeEditorDocument
     */
    protected OwCMISPrivilegeEditorDocument createEditorDocument(OwCMISPermissionCollection perms, List<OwPrivilegeSet> modifySet) throws Exception
    {
        return new OwCMISPrivilegeEditorDocument(perms, modifySet);
    }

    @Override
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        if (addDialog)
        {
            this.previousSelectedPrincipals = ((OwCMISPrivilegeEditorDocument) dialogView_p.getDocument()).getPrincipalSelection();
            this.addDialog = false;//closed dialog
        }
    }

    /**
     * Get the principals which where previously selected. 
     * @return List of Strings or null (if none is provided)
     */
    protected List<String> getPreviousSelectedPrincipals()
    {
        return previousSelectedPrincipals;
    }
}
