package com.wewebu.ow.server.plug.owlink;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksDocument;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 *Document object links view. 
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
 *@since 4.1.1.0
 */
public class OwDocumentFunctionShowLinks extends OwDocumentFunction implements OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDocumentFunctionShowLinks.class);
    protected static final String SETTINGS_COLUMN_INFO = "columninfo";

    private List<String> linkClassNames = Collections.emptyList();
    private OwObjectLinkRelation split = OwObjectLinkRelation.BOTH;
    private boolean displayTypedList = true;
    private List<String> documentFunctionIds = Collections.emptyList();

    @SuppressWarnings("unchecked")
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        if (node_p != null)
        {
            displayTypedList = node_p.getSafeBooleanValue("typeList", true);
            String direction = node_p.getSafeTextValue("direction", "BOTH");
            try
            {
                split = OwObjectLinkRelation.valueOf(direction);
            }
            catch (IllegalArgumentException e)
            {
                LOG.error("Invalid direction element value in confiuration of document function with ID=" + getPluginID(), e);
            }
            linkClassNames = node_p.getSafeStringList("linkclasses");
            documentFunctionIds = node_p.getSafeStringList("DocumentFunctionPlugins");
        }
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owlink/show_links.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owlink/show_links_24.png");
    }

    /** event called when user clicked the label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            LOG.debug("OwDocumentFunctionShowLinks.onClickEvent(): the edit function is not enabled for the given object !");
            throw new OwInvalidOperationException(new OwString("owdocprops.show.links.function.invalid.invalid.object", "Can not display links for this item."));
        }

        List<OwObject> objects = new LinkedList<OwObject>();

        objects.add(oObject_p);

        onMultiselectClickEvent(objects, oParent_p, refreshCtx_p);
    }

    protected OwShowLinksDialog createShowLinksDialog(List<OwObject> enabledObjects, boolean typedList, Collection<String> linkClassNames, OwObjectLinkRelation relation, List<String> documentFunctionIds)
    {
        OwObjectLinksDocument document = new OwObjectLinksDocument(relation, linkClassNames, documentFunctionIds);
        OwObjectLinksView view = typedList ? new OwReferencedObjectTypedLinksView(document) : new OwReferencedObjectAllLinksView(document);
        return new OwShowLinksDialog(enabledObjects, view);
    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List<OwObject> enabledObjects = new LinkedList<OwObject>();

        for (Iterator i = objects_p.iterator(); i.hasNext();)
        {
            OwObject objectToEdit = (OwObject) i.next();
            if (isEnabled(objectToEdit, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledObjects.add(objectToEdit);
            }
        }
        if (enabledObjects.isEmpty())
        {
            LOG.error("OwDocumentFunctionShowLinks.onMultiselectClickEvent(): Invalid object collection!The enabled objects collection is empty!");
            throw new OwInvalidOperationException(new OwString("owdocprops.show.links.function.invalid.object.collection", "Invalid object collection!"));
        }

        OwShowLinksDialog dlg = createShowLinksDialog(enabledObjects, displayTypedList, linkClassNames, split, documentFunctionIds);
        dlg.setTitle(getPluginTitle());
        dlg.setColumnNames(getConfiguredColumns());
        dlg.setInfoIcon(getBigIcon());

        getContext().openDialog(dlg, this);
    }

    @SuppressWarnings("unchecked")
    private List<String> getConfiguredColumns()
    {
        List<String> columns = (List<String>) getSafeSetting(SETTINGS_COLUMN_INFO, null);
        if (columns == null)
        {
            columns = new LinkedList<String>();
            columns.add(OwResource.m_ObjectNamePropertyClass.getClassName());
        }
        return columns;
    }

    @Override
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

}