package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;

import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * Privilege addition dialog. Used in {@link OwPrivilegesView}.
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
public class OwAddPrivilegeDialog extends OwStandardDialog
{

    public OwAddPrivilegeDialog(OwPrivilegesView privilegesView_p)
    {
        super();
        OwPrivilegesDocument document = privilegesView_p.getDocument();
        setDocument(document);
    }

    @Override
    public OwPrivilegesDocument getDocument()
    {
        return (OwPrivilegesDocument) super.getDocument();
    }

    @Override
    protected void init() throws Exception
    {
        super.init();

        setTitle(getContext().localize("app.OwAddPrivilegeDialog.title", "Add privileges"));

        OwPrivilegeSetEditor editorView = createPrivilegeSetEditor();
        addView(editorView, MAIN_REGION, null);
    }

    protected OwPrivilegeSetEditor createPrivilegeSetEditor()
    {
        return new OwPrivilegeSetEditor(getDocument(), this);
    }

    @Override
    public void closeDialog() throws Exception
    {
        super.closeDialog();

        OwPrivilegeSetEditor editorView = (OwPrivilegeSetEditor) getViewRegion(MAIN_REGION);
        OwPrivilegeSetEditorDocument editorDocument = editorView.getDocument();

        Collection<OwPrivilege> addedPrivileges = editorDocument.getAddedPrivileges();
        OwUserInfo currentRole = editorDocument.getCurrentRole();

        if (addedPrivileges != null)
        {
            OwPrivilegesDocument document = getDocument();
            document.addPrivilegeSet(currentRole, addedPrivileges, false, 0);
        }
    }
}
