package com.wewebu.ow.server.ecmimpl.opencmis.ui.acl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Editor document which contains common work objects/data.
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
public class OwCMISPrivilegeEditorDocument extends OwDocument
{
    private OwCMISPermissionCollection permissions;
    private List<OwPrivilegeSet> modifySets;
    private String selectedPrincipal;
    private List<String> principalSelection;

    private List<OwPrivilege> renderList;
    private List<OwPrivilege> selectedList;

    public OwCMISPrivilegeEditorDocument(OwCMISPermissionCollection permCol, List<OwPrivilegeSet> modifySets) throws Exception
    {
        this.permissions = permCol;
        this.modifySets = modifySets;
        principalSelection = new LinkedList<String>();
        if (modifySets != null)
        {
            for (OwPrivilegeSet set : modifySets)
            {
                principalSelection.add(set.getPrincipal().getUserName());
                if (selectedPrincipal == null)
                {
                    selectedPrincipal = set.getPrincipal().getUserName();
                }
            }
        }
    }

    /**
     * Dependent permission collection which is currently modified. 
     * @return OwCMISPermissionCollection
     */
    public OwCMISPermissionCollection getPermissions()
    {
        return this.permissions;
    }

    /**
     * Get List of privilege set's which should be modified.
     * @return List of OwPrivilegeSet or null
     */
    public List<OwPrivilegeSet> getModifySets()
    {
        return this.modifySets;
    }

    public String getSelectedPrincipal()
    {
        return selectedPrincipal;
    }

    public void setSelectedPrincipal(String principalId)
    {
        if (modifySets == null && selectedPrincipal != null)
        {
            getPrincipalSelection().add(selectedPrincipal);
        }
        if (principalId != null && !getPrincipalSelection().contains(principalId))
        {
            getPrincipalSelection().add(principalId);
        }

        this.selectedPrincipal = principalId;
    }

    /**
     * Get a list of selectable principals.
     * @return List of principal-Id
     */
    public List<String> getPrincipalSelection()
    {
        return this.principalSelection;
    }

    /**
     * Set a list of previous available Selections 
     * @param previousSelected List of principal Ids
     */
    public void setPrincipalSelection(List<String> previousSelected)
    {
        if (previousSelected != null)
        {
            this.principalSelection = previousSelected;
        }
    }

    /**
     * List of privileges to be rendered.
     * @return List of OwPrivilege objects
     * @see #setRenderList(List)
     */
    public List<OwPrivilege> getRenderList()
    {
        if (renderList == null)
        {
            return Collections.emptyList();
        }
        else
        {
            return this.renderList;
        }
    }

    /**
     * List of already selected privileges.
     * @return List of OwPrivilege
     * @see #setSelectedList(List)
     */
    public List<OwPrivilege> getSelectedList()
    {
        if (selectedList == null)
        {
            return Collections.emptyList();
        }
        return this.selectedList;
    }

    /**
     * Set the list of privileges which should be rendered.
     * @param renderLst List of OwPrivilege objects
     */
    public void setRenderList(List<OwPrivilege> renderLst)
    {
        this.renderList = renderLst;
    }

    /**
     * Set the list of selected/defined privileges.
     * @param selected List of OwPrivilege objects
     */
    public void setSelectedList(List<OwPrivilege> selected)
    {
        this.selectedList = selected;
    }
}
