package com.wewebu.ow.server.ecmimpl.opencmis.ui.acl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Special privilege set editor, based on CMIS 
 * available permission and user selection.
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
public class OwCMISPrivilegeSetEditor extends OwStandardDialog implements OwDialogListener
{
    private static final Logger LOG = OwLog.getLogger(OwCMISPrivilegeSetEditor.class);

    /**Key for choice selections events*/
    public static final String CHOICE = "owrolerecent";
    /**Name of parameter for selection events*/
    public static final String ROW_IDX = "rowIdx";

    @Override
    protected void init() throws Exception
    {
        super.init();
        setTitle(getContext().localize("opencmis.ui.acl.OwCMISPrivilegeSetEditor.title", "Privilege Editor"));
        if (getDocument().getSelectedPrincipal() != null)
        {
            processSelection();
        }
    }

    /**
     * Factory for OwUserSelectDialog which is used in Editor.
     * @param filter int array of {@link OwUIUserSelectModul}.TYPE_... values
     * @param multiSelect boolean allow multiple selection
     * @return OwUserSelectDialog
     * @throws OwException
     */
    protected OwUserSelectDialog createOwUserSelectDialog(int[] filter, boolean multiSelect) throws OwException
    {
        OwRoleManagerContext roleCtx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
        if (roleCtx != null)
        {
            OwNetwork network = roleCtx.getNetwork();
            try
            {
                if (network.canUserSelect())
                {
                    return new OwUserSelectDialog(filter, multiSelect);
                }
                else
                {
                    Session ses = getDocument().getPermissions().getSession();
                    return new OwCMISRestrictedUserSelectDialog(ses.getRepositoryInfo());
                }
            }
            catch (Exception e)
            {
                LOG.error("Could not evaluate user selection of network", e);
                throw new OwServerException(getContext().localize("opencmis.ui.acl.OwCMISPrivilegeSetEditor.createOwUserSelectDialog.error", "Evaluation of user selection failed."), e);
            }
        }
        else
        {
            throw new OwServerException("Editor was not initialised or missing reference to current context");
        }
    }

    @Override
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        if (iRegion_p != MAIN_REGION)
        {
            super.renderRegion(w_p, iRegion_p);
        }
        else
        {
            if (isRegion(MAIN_REGION))
            {
                super.renderRegion(w_p, iRegion_p);
            }
            else
            {
                renderMainRegion(w_p);
            }
        }
    }

    /**(overridable)<br />
     * Render main region for current state, this method is delegated from renderRegion(writer, MAIN_REGION).
     * @param w_p Writer
     * @throws Exception
     * @throws IOException
     */
    protected void renderMainRegion(Writer w_p) throws Exception, IOException
    {
        OwCMISPrivilegeEditorDocument doc = getDocument();
        w_p.write("<div class=\"OwCMISPrivilegeSetEditor\">");
        w_p.write("<div class=\"OwInlineMenu\"><script type=\"text/javascript\">");
        w_p.write(" function changePrincipal() {\n");
        w_p.write("  var value = \"");
        w_p.write(CHOICE);
        w_p.write("=\" + arguments[1].id;\n");
        w_p.write("  window.location.href=\"");
        w_p.write(getEventURL("PrincipalChanged", ""));
        w_p.write("\" + value;\n}</script>\n");

        OwComboModel recentRolesModel = new OwDefaultComboModel(false, false, doc.getSelectedPrincipal(), createComboItemList(doc.getPrincipalSelection()));
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(recentRolesModel, "owrolerecent", null, null, null);
        renderer.addEvent("onchange", "changePrincipal()");
        String changePrincipalDisplayName = getContext().localize("OwCMISPrivilegeSetEditor.changePrincipalRole", "Change Principal Role");
        OwInsertLabelHelper.insertLabelValue(w_p, changePrincipalDisplayName, "owrolerecent");
        renderer.renderCombo(w_p);
        if (getDocument().getModifySets() == null)
        {
            w_p.write("<div class=\"UserSelectionButton\">");
            w_p.write("<input type=\"button\" value=\"");
            w_p.write(getContext().localize("opencmis.ui.acl.OwCMISPrivilegeSetEditor.btn.userSelect", "Select Principal"));
            w_p.write("\" onclick=\"");
            w_p.write(getFormEventFunction("OpenUserSelection", null));
            w_p.write("\" /></div>");//UserSelectionButton
        }
        w_p.write("</div>\n");//OwInlineMenu div
        w_p.write("<div class=\"Privileges\">\n");
        renderMainContent(w_p, doc.getRenderList(), doc.getSelectedList());
        w_p.write("</div>");//Privileges div
        w_p.write("</div>");//OwCMISPrivilegeSetEditor div
    }

    /**(overridable)<br />
     * Render content for current selection.
     * @param w_p Writer
     * @param render List of Elements to be rendered.
     * @param selected
     * @throws Exception
     * @throws IOException
     */
    protected void renderMainContent(Writer w_p, List<OwPrivilege> render, List<OwPrivilege> selected) throws Exception, IOException
    {
        if (render != null && !render.isEmpty())
        {
            w_p.write("<table class=\"OwGeneralList_table\"><thead><tr><th></th><th width=\"99%\">");
            w_p.write(OwString.localizeLabel(getContext().getLocale(), "Ace(s)"));
            w_p.write("</th></thead><tbody>");
            Iterator<OwPrivilege> it = render.iterator();
            for (int i = 0; it.hasNext(); i += 1)
            {
                OwPrivilege row = it.next();
                w_p.write("<tr class=\"");
                w_p.write(i % 2 == 0 ? "EvenRow" : "OddRow");
                w_p.write("\"><td><input type=\"checkbox\" value=\"");
                w_p.write(Integer.toString(i));
                w_p.write("\" name=\"");
                w_p.write(ROW_IDX);
                w_p.write("\"");
                if (selected.contains(row))
                {
                    w_p.write(" checked ");
                }
                w_p.write("/></td><td>");
                w_p.write(OwString.localizeLabel(getContext().getLocale(), row.getName()));
                w_p.write("</td></tr>\n");
            }

            w_p.write("</tbody></table>");

            w_p.write("<div class=\"SaveBtn\">");
            w_p.write("<input type=\"button\" value=\"");
            w_p.write(getContext().localize("opencmis.ui.acl.OwCMISPrivilegeSetEditor.btn.save", "Save"));
            w_p.write("\" onclick=\"");
            w_p.write(getFormEventFunction("Save", null));
            w_p.write("\" /></div>");
        }
    }

    @Override
    public OwCMISPrivilegeEditorDocument getDocument()
    {
        return (OwCMISPrivilegeEditorDocument) super.getDocument();
    }

    /**(overridable)<br />
     * Process Principal selection and set the
     * available and selected list of selection.
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    protected void processSelection() throws OwException
    {
        OwCMISPrivilegeEditorDocument doc = getDocument();
        List<OwPrivilege> renderLst = new LinkedList<OwPrivilege>();
        List<OwPrivilege> selected = new LinkedList<OwPrivilege>();
        if (doc.getModifySets() == null)
        {
            if (doc.getSelectedPrincipal() != null)
            {
                OwUserInfo usr = createUserInfo(doc.getSelectedPrincipal());

                renderLst = new LinkedList<OwPrivilege>(doc.getPermissions().getAvailablePrivileges(usr));
            }
        }
        else
        {
            if (doc.getSelectedPrincipal() != null)
            {
                Iterator<?> it = doc.getPermissions().getAppliedPrivilegeSets().iterator();
                while (it.hasNext())
                {
                    OwPrivilegeSet set = (OwPrivilegeSet) it.next();
                    try
                    {
                        if (doc.getSelectedPrincipal().equals(set.getPrincipal().getUserName()))
                        {
                            if (set.getInheritanceDepth() == OwPrivilegeSet.INHERITANCE_DEPTH_NO_INHERITANCE)
                            {
                                selected.addAll(set.getPrivileges());
                            }
                        }
                    }
                    catch (OwException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        LOG.error("Could not retrieve user name from PrivilegeSet, class = " + set.getClass() + " principal = " + set.getPrincipal(), e);
                        throw new OwServerException("Cannot evaluate principal object", e);
                    }
                }
                renderLst = new LinkedList<OwPrivilege>(doc.getPermissions().getAvailablePrivileges(null));
            }
        }
        doc.setRenderList(renderLst);
        doc.setSelectedList(selected);
    }

    /**(overridable)<br />
     * Handler method for principal changed events.
     * @param req HttpServletRequest
     * @throws OwException
     */
    public void onPrincipalChanged(HttpServletRequest req) throws OwException
    {
        getDocument().setSelectedPrincipal(req.getParameter(CHOICE));
        processSelection();
    }

    /**(overridable)<br />
     * Handler method for open user selection events.
     * @param req HttpServletRequest
     * @throws OwException
     */
    public void onOpenUserSelection(HttpServletRequest req) throws OwException
    {
        OwUserSelectDialog usrDlg = createOwUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_GROUP, OwUIUserSelectModul.TYPE_USER }, false);
        try
        {
            getContext().openDialog(usrDlg, this);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Cannot open user selection dialog", e);
            throw new OwServerException("Failed to open User selection dialog.", e);
        }
    }

    /* Handler for user dialog close events */
    @Override
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        if (dialogView_p instanceof OwUserSelectDialog)
        {
            OwUserSelectDialog dlg = (OwUserSelectDialog) dialogView_p;
            if (dlg.getSelectedUsers() != null && !dlg.getSelectedUsers().isEmpty())
            {
                OwUserInfo usr = (OwUserInfo) dlg.getSelectedUsers().get(0);
                getDocument().setSelectedPrincipal(usr.getUserName());
                processSelection();
            }
            else if (dlg.getSelectedRoles() != null && !dlg.getSelectedRoles().isEmpty())
            {
                String role = (String) dlg.getSelectedRoles().get(0);
                getDocument().setSelectedPrincipal(role);
                processSelection();
            }
        }
    }

    /**
     * On Save event handler method. 
     * @param req HttpServletRequest event trigger
     * @throws OwException
     */
    public void onSave(HttpServletRequest req) throws OwException
    {
        String[] arr = req.getParameterValues(ROW_IDX);
        if (arr != null)
        {
            List<OwPrivilege> avlst = getDocument().getRenderList();
            List<OwPrivilege> currentSelected = getDocument().getSelectedList();
            List<OwPrivilege> newSelection = new LinkedList<OwPrivilege>();
            List<OwPrivilege> deleted = new LinkedList<OwPrivilege>(currentSelected);
            for (String idx : arr)
            {
                OwPrivilege a = avlst.get(Integer.valueOf(idx));
                deleted.remove(a);
                newSelection.add(a);
            }
            OwUserInfo usr = createUserInfo(getDocument().getSelectedPrincipal());
            if (getDocument().getModifySets() == null)
            {//Add handling
                getDocument().getPermissions().addPrivilegeSet(usr, newSelection, false, 0);

            }
            else
            {//modify privilege set
                Iterator<?> it = getDocument().getModifySets().iterator();
                OwPrivilegeSet set = null;
                while (it.hasNext())
                {//Find Privilege set
                    OwPrivilegeSet next = (OwPrivilegeSet) it.next();
                    try
                    {
                        if (usr.getUserName().equals(next.getPrincipal().getUserName()))
                        {
                            set = next;
                            it.remove();
                            break;
                        }
                    }
                    catch (OwException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        LOG.error("Cannot compare the user name", e);
                        throw new OwServerException("Faild to get current edited PrivilegeSet object", e);
                    }
                }
                if (set != null)
                {//process changes/modifications on privilege set
                    getDocument().getPermissions().removePrivilegeSet(set);
                    set = getDocument().getPermissions().addPrivilegeSet(usr, newSelection, false, 0);
                    getDocument().getModifySets().add(set);
                }
            }
            getDocument().setSelectedList(newSelection);
        }
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";
    }

    /**(overridable)<br />
     * Transform from list of Strings to List of OwComboItems
     * @param values List of Strings
     * @return List of OwComboItem
     */
    protected List<OwComboItem> createComboItemList(List<String> values)
    {
        List<OwComboItem> cmbItems = new LinkedList<OwComboItem>();
        if (values != null)
        {
            for (String value : values)
            {
                cmbItems.add(new OwDefaultComboItem(value, value));
            }
        }
        return cmbItems;
    }

    /**(overridable)<br />
     * Factory for OwUserInfo creation from provided principal Id.
     * @param principalId String principal Id
     * @throws OwException if unable to create OwUserInfo object
     * @return OwUserInfo
     */
    protected OwUserInfo createUserInfo(String principalId) throws OwException
    {
        return new OwCMISUserInfo(principalId, null);
    }
}
