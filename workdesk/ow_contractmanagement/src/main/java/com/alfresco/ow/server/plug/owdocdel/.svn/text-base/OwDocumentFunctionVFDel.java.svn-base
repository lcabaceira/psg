package com.alfresco.ow.server.plug.owdocdel;

import java.util.Collection;

import com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver;
import com.alfresco.ow.server.plug.owlink.OwReferencedObject;
import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwDocumentFunctionVFDel.
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
public class OwDocumentFunctionVFDel extends OwDocumentFunctionDel
{

    public static final String PLUGIN_PARAM_USED_PARENT_FOLDER = "ParentFolder";

    private OwParentFolderResolver rootFolderResolver;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#init(com.wewebu.ow.server.util.OwXMLUtil, com.wewebu.ow.server.app.OwMainAppContext)
     */
    @Override
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        rootFolderResolver = new OwParentFolderResolver();
        rootFolderResolver.init(node_p.getSafeTextValue(PLUGIN_PARAM_USED_PARENT_FOLDER, ""), context_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
        return super.isEnabled(oObject_p, resolvedParent, iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#isEnabled(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean isEnabled(Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        return super.isEnabled(objects_p, resolvedParent, iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel#onClickEvent(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        //OwReferencedObject can't be deleted with default delete. OwDocumentFunctionVFDeleteLinkObject should be used instead. Provide this information in exception
        if (oObject_p instanceof OwReferencedObject)
        {
            throw new OwInvalidOperationException(getContext().localize("owdocdel.OwDocumentFunctionDel.invalidobject", "Item cannot be deleted."), new OwNotSupportedException(
                    "OwReferencedObject.delete: Referenced objects are part of an link object and do not support delete(). Use OwDocumentFunctionVFDeleteLinkObject to delete the link."));
        }
        OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
        super.onClickEvent(oObject_p, resolvedParent, refreshCtx_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel#onMultiselectClickEvent(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        super.onMultiselectClickEvent(objects_p, resolvedParent, refreshCtx_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#getNeedParent()
     */
    @Override
    public boolean getNeedParent()
    {
        if (rootFolderResolver.isExplicitDefinedRootFolder())
        {
            return false;
        }

        return super.getNeedParent();
    }
}
