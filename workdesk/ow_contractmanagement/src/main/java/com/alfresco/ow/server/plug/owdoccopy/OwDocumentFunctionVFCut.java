package com.alfresco.ow.server.plug.owdoccopy;

import java.util.Collection;

import com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver;
import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 * <p>
 * Implementation of the Document cut plugin.
 * With extension for use with (semi) virtual folders.
 * </p>
 * In {@code owplugins.xml} a new tag was introduced: {@code <SemiVirtualRootFolder>}.<br>
 * Define the physical folder object from which the documents in virtual folder should be cut.<br>
 * If the parent of a document is a virtual folder, the defined physical folder is used as parent for cut operation.<br>
 * If the parent is an physical folder, the defined parameter will not be applied;  instead the real physical parent will be used.<br>
 * Can be one of the following:
 * <ul>
 * <li>PATH      e.g.:          /DevStore1/      (root of OS "DevStore1")</li>
 * <li>PATH      e.g.:          /DevStore1/MyFolder/ (some subfolder within OS "DevStore1")</li>
 * <li>DMSID     e.g.:          dmsid=100005</li>
 * <li>UseSingleRoot :          Use the common single root folder of all cut out objects.</li>
 * <li>VirtualFolderSearchPath: If type of object is OBJECT_TYPE_VIRTUAL_FOLDER. The virtual folder search path will be used, if defined.
 * </ul>
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
public class OwDocumentFunctionVFCut extends OwDocumentFunctionCut
{
    public static final String PLUGIN_PARAM_USED_PARENT_FOLDER = "ParentFolder";

    private OwParentFolderResolver rootFolderResolver;

    /** set the plugin description node 
     * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
     * @param context_p OwMainAppContext
     */
    @Override
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        rootFolderResolver = new OwParentFolderResolver();
        rootFolderResolver.init(node_p.getSafeTextValue(PLUGIN_PARAM_USED_PARENT_FOLDER, ""), context_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut#getNeedParent()
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

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut#isEnabled(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean isEnabled(Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        return super.isEnabled(objects_p, resolvedParent, iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
        return super.isEnabled(oObject_p, resolvedParent, iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionAddToClipboard#onMultiselectClickEvent(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        super.onMultiselectClickEvent(objects_p, resolvedParent, refreshCtx_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionAddToClipboard#onClickEvent(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
        super.onClickEvent(oObject_p, resolvedParent, refreshCtx_p);
    }
}
