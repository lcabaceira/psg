package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardFieldManager;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwUnresolvedReference;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.util.OwStandardXMLUtil;

/**
 *<p>
 * Field Control to display Attachments in the BPM Workitems using a standard  object list.<br/>
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
 * @see OwObjectListView
 */
public class OwBPMResultlistAttachmentFieldControl extends OwFieldManagerControl
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMResultlistAttachmentFieldControl.class);

    /** general ID in the plugin ID to be configure the plugin IDs for attachments */
    public static final String PLUGIN_CONFIG_ID_ATTACHMENT_FUNCTION = "AttachmentDocumentFunctions";

    /**
     * Inline attachment removal document function.<br>
     * This document function removes an attachment from an {@link OwBPMResultlistAttachmentFieldControl}
     * handled attachments list.
     *
     */
    public class OwDeleteAttachmentFunction extends OwDocumentFunction
    {
        private static final String PLUGIN_ID_PREFIX = "delete.attachment";
        private String m_fieldID;
        private String m_pluginID;

        /**
         * Constructor
         * @param fieldID_p field ID as provided by the field manager control
         */
        public OwDeleteAttachmentFunction(String fieldID_p)
        {
            super();
            m_fieldID = fieldID_p;
            Random random = new Random(System.currentTimeMillis());
            int randomSuffix = random.nextInt(Integer.MAX_VALUE);
            m_pluginID = PLUGIN_ID_PREFIX + "." + randomSuffix;
        }

        /** get the URL to the icon of the dialog / function
         */
        public String getIcon() throws Exception
        {
            return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdelref/remove.png");
        }

        /** get the URL to the icon of the dialog / function
         */
        public String getBigIcon() throws Exception
        {
            return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdelref/remove_24.png");
        }

        public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
        {
            List oneObjectList = new ArrayList();
            oneObjectList.add(object_p);
            onMultiselectClickEvent(oneObjectList, parent_p, refreshCtx_p);
        }

        public void onMultiselectClickEvent(Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
        {
            OwField field = getFieldManager().getField(m_fieldID);
            OwFieldDefinition fieldDefinition = field.getFieldDefinition();
            if (fieldDefinition.isArray())
            {
                Object[] values = (Object[]) field.getValue();
                List newValuesList = new ArrayList();
                for (int i = 0; i < values.length; i++)
                {
                    if (!objects_p.contains(values[i]))
                    {
                        newValuesList.add(values[i]);
                    }
                }
                Object[] newValues = newValuesList.toArray(new Object[newValuesList.size()]);
                field.setValue(newValues);
            }
            else
            {
                if (objects_p.contains(field.getValue()))
                {
                    field.setValue(null);
                }
            }

        }

        protected Set getSupportedObjectTypesFromDescriptor(String strKey_p) throws OwConfigurationException
        {
            Integer[] supportedObjects = new Integer[] { new Integer(OwObjectReference.OBJECT_TYPE_DOCUMENT), new Integer(OwObjectReference.OBJECT_TYPE_FOLDER) };
            return new HashSet(Arrays.asList(supportedObjects));
        }

        public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
        {
            return true;
        }

        public boolean getContextMenu()
        {
            return true;
        }

        public boolean getMultiselect()
        {
            return true;
        }

        public boolean getObjectInstance()
        {
            return true;
        }

        public String getPluginTitle()
        {
            return getContext().localize("owbpm.OwBPMResultlistAttachmentFieldControl.removeattachment", "Remove Attachment");
        }

        public String getTooltip() throws Exception
        {
            return getPluginTitle();
        }

        public String getPluginID()
        {
            return m_pluginID;
        }
    }

    /**Editable list controls mapped by field control ID*/
    private Map m_editableListViews = new HashMap();
    /**Editable list controls mapped by field class names */
    private Map m_readonlyListViews = new HashMap();

    /**Editable list controls mapped by field class names */
    private List m_enabledAttachmentFunctionIDs;

    /**
     * Constructor<br>
     * All document functions will be enabled.<br>
     * In order to enable only certain document functions use {@link #OwBPMResultlistAttachmentFieldControl(List)}.
     */
    public OwBPMResultlistAttachmentFieldControl()
    {
        this.m_enabledAttachmentFunctionIDs = null;
    }

    /**
     * Constructor
     * @param attachmentFunctionIDs_p a {@link List} of String document function IDs to enable for this
     *                                field control. If <code>null</code> NO DOCUMENT FUNCTION will be enabled.<br>
     *                                To enable all document functions use  {@link #OwBPMResultlistAttachmentFieldControl()}.
     */
    public OwBPMResultlistAttachmentFieldControl(List attachmentFunctionIDs_p)
    {
        if (attachmentFunctionIDs_p != null)
        {
            this.m_enabledAttachmentFunctionIDs = attachmentFunctionIDs_p;
        }
        else
        {
            this.m_enabledAttachmentFunctionIDs = new ArrayList();
        }
    }

    /**
     * Applies a hard coded/default configuration to the give list view.
     * The default configuration enables document plugins and adds the object name to the
     * list columns.
     * @param listView_p
     */
    protected void applyConfiguration(OwObjectListView listView_p)
    {
        listView_p.setViewMask(OwObjectListView.VIEW_MASK_USE_DOCUMENT_PLUGINS | OwObjectListView.VIEW_MASK_MULTI_SELECTION | OwObjectListView.VIEW_MASK_INSTANCE_PLUGINS);

        List columninfo = new ArrayList();

        columninfo.add(new OwStandardFieldColumnInfo(OwResource.m_ObjectNamePropertyClass, OwFieldColumnInfo.ALIGNMENT_DEFAULT));
        listView_p.setColumnInfo(columninfo);

    }

    /**
     * Set the enabled document functions list for the given {@link OwObjectListView} object.
     * @param listView_p - the list view object.
     * @throws Exception
     */
    private void enableFunctions(OwObjectListView listView_p) throws Exception
    {
        if (m_enabledAttachmentFunctionIDs != null)
        {
            // List configuredDocumentFunctions = listView_p.getDocumentFunctionPluginList();
            List enabledDocumentFunctions = new LinkedList();
            for (Iterator i = m_enabledAttachmentFunctionIDs.iterator(); i.hasNext();)
            {
                String id = (String) i.next();
                // only add to array if it is an allowed function
                if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                {
                    OwFunction function = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                    enabledDocumentFunctions.add(function);
                }
            }
            listView_p.setDocumentFunctionPluginList(enabledDocumentFunctions);
        }
    }

    /**
     * Returns the editable object list view corresponding to the given field control ID.
     * If no list control was created before for the given ID a new one is created and
     * mapped to the given control ID.
     * @param strID_p the field control ID
     * @return an {@link OwObjectListView}
     * @throws Exception
     */
    private OwObjectListView editableListViewFormControlId(String strID_p) throws Exception
    {
        OwObjectListView listView = (OwObjectListView) m_editableListViews.get(strID_p);
        if (listView == null)
        {

            listView = new OwObjectListViewRow();
            listView.setExternalFormTarget(getFormTarget());
            applyConfiguration(listView);
            listView.attach(getContext(), null);
            enableFunctions(listView);

            OwDeleteAttachmentFunction m_delAttachmentFunction = new OwDeleteAttachmentFunction(strID_p);
            m_delAttachmentFunction.init(new OwStandardXMLUtil(), (OwMainAppContext) getContext());
            List documentFunctions = new ArrayList(listView.getDocumentFunctionPluginList());
            documentFunctions.add(m_delAttachmentFunction);
            listView.setDocumentFunctionPluginList(documentFunctions);
            listView.setStickyFooterInUse(false);
            m_editableListViews.put(strID_p, listView);
        }
        return listView;

    }

    /**
     * Returns the editable object list view corresponding to the given list ID.
     * If no list control was created before for the given ID a new one is created and
     * mapped to the given list ID.
     * @param listID_p the field control ID
     * @return an {@link OwObjectListView}
     * @throws Exception
     */
    private OwObjectListView readonlyListViewFormControlId(String listID_p) throws Exception
    {
        OwObjectListView listView = (OwObjectListView) m_readonlyListViews.get(listID_p);
        if (listView == null)
        {
            listView = new OwObjectListViewRow();
            listView.setExternalFormTarget(getFormTarget());
            applyConfiguration(listView);
            listView.attach(getContext(), null);
            enableFunctions(listView);
            m_readonlyListViews.put(listID_p, listView);
        }
        return listView;

    }

    public void detach()
    {
        Set viewsEntries = m_editableListViews.entrySet();
        for (Iterator i = viewsEntries.iterator(); i.hasNext();)
        {
            Entry viewEntry = (Entry) i.next();
            OwObjectListView view = (OwObjectListView) viewEntry.getValue();
            view.detach();
        }
        Set readOnlyViewsEntries = m_readonlyListViews.entrySet();
        for (Iterator i = readOnlyViewsEntries.iterator(); i.hasNext();)
        {
            Entry viewEntry = (Entry) i.next();
            OwObjectListView view = (OwObjectListView) viewEntry.getValue();
            view.detach();
        }
        super.detach();
    }

    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
    }

    /**
     * Converts a field value to a list of objects to be used with  {@link OwObjectListView#setObjectList(com.wewebu.ow.server.ecm.OwObjectCollection, OwObject)}.
     * {@link OwUnresolvedReference} are ignored and logged.
     * @param value_p
     * @param fieldDef_p
     * @return an array of objects to be used as list control values through {@link OwObjectListView#setObjectList(com.wewebu.ow.server.ecm.OwObjectCollection, OwObject)}
     * @throws Exception
     */
    private Object[] createListValues(Object value_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        Object[] listValues = null;
        Object fieldValue = value_p;

        if (fieldValue == null)
        {
            listValues = new Object[0];
        }
        else if (fieldDef_p.isArray())
        {
            if (fieldValue != null)
            {
                listValues = (Object[]) fieldValue;
            }
        }
        else
        {
            if (fieldValue != null)
            {
                listValues = new Object[] { fieldValue };
            }
        }

        List listValuesList = new ArrayList();
        for (int i = 0; i < listValues.length; i++)
        {
            if (listValues[i] instanceof OwObject)
            {
                listValuesList.add(listValues[i]);
            }
            else if (listValues[i] instanceof OwObjectReference)
            {
                OwObjectReference reference = (OwObjectReference) listValues[i];
                if (reference instanceof OwUnresolvedReference)
                {
                    OwUnresolvedReference unresolvedReference = (OwUnresolvedReference) reference;
                    LOG.warn("OwBPMResultlistAttachmentFieldControl.createListValues : unresolved reference ignored in list control - " + unresolvedReference.getUnresolvedReason());
                }
                else
                {
                    listValuesList.add(reference.getInstance());
                }
            }
        }
        return listValuesList.toArray(new Object[listValuesList.size()]);
    }

    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        Object[] listValues = createListValues(field_p.getValue(), fieldDef_p);
        renderEditableList(w_p, strID_p, listValues);
        w_p.write("<div style='float:left;clear:left'>");
        // true when single-value property, and attachment list contains already one element
        boolean disabledPaste_ListFull = !fieldDef_p.isArray() && listValues.length > 0;
        // fieldDef_p - OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
        renderPasteLink(w_p, "PasteObjects", strID_p, disabledPaste_ListFull, fieldDef_p);
        w_p.write("</div>");
    }

    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        Object[] listValues = createListValues(value_p, fieldDef_p);
        String className = fieldDef_p.getClassName();
        renderReadonlyList(w_p, className, listValues);
    }

    /**
     * Fills the given list with the set of provided values and renders it on the provided Writer.
     * @param w_p
     * @param listView_p
     * @param values_p
     * @throws Exception
     */
    private void renderList(Writer w_p, OwObjectListView listView_p, Object[] values_p) throws Exception
    {
        w_p.write("<div style='floaf:left;'>");
        OwStandardObjectCollection objectList = new OwStandardObjectCollection();
        if (values_p != null)
        {
            objectList.addAll(Arrays.asList(values_p));
        }
        listView_p.setObjectList(objectList, null);
        if (objectList.size() > 0)
        {
            listView_p.render(w_p);
        }
        w_p.write("</div>");
    }

    /**
     * Renders an editable list view by field control ID.
     * @param w_p
     * @param strID_p
     * @param values_p
     * @throws Exception
     */
    private void renderEditableList(Writer w_p, String strID_p, Object[] values_p) throws Exception
    {
        OwObjectListView listView = editableListViewFormControlId(strID_p);
        renderList(w_p, listView, values_p);
    }

    /**
     * Renders an editable list view by its list ID.
     * @param w_p
     * @param listId_p
     * @param values_p
     * @throws Exception
     */
    private void renderReadonlyList(Writer w_p, String listId_p, Object[] values_p) throws Exception
    {
        OwObjectListView listView = readonlyListViewFormControlId(listId_p);
        renderList(w_p, listView, values_p);
    }

    /**
     * Renders the paste link associated with the attachments field.
     * @param w_p Writer
     * @param callbackfunction_p String name of the callback handler
     * @param strID_p ID of item
     * @param disabledPaste_ListFull true when single-value property, and attachment list contains already one element 
     * @param fieldDef_p OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
     * 
     * @throws Exception
     */
    private void renderPasteLink(Writer w_p, String callbackfunction_p, String strID_p, boolean disabledPaste_ListFull, OwFieldDefinition fieldDef_p) throws Exception
    {
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
        String pasteClipboardObjectTooltip = "";

        if (disabledPaste_ListFull) // true when single-value property, and attachment list contains already one element
        {
            //do not display any icon, display only when the attachment list is empty
            //w_p.write("<img title=\"");
            //w_p.write(pasteClipboardObjectTooltip);
            //w_p.write("\" alt=\"");
            //w_p.write(pasteClipboardObjectTooltip);
            //w_p.write("\" src=\"");
            //w_p.write(getContext().getDesignURL());
            //w_p.write("/images/plug/owbpm/add_paste_disabled.png\" class=\"OwMimeIcon\"/>");
        }
        else
        {
            if ((clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT) && (fieldDef_p.isArray() || (!fieldDef_p.isArray() && (clipboard.getContent().size() == 1))))
            {
                pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.pasteobject", "Paste an object from the clipboard.");

                w_p.write("<a class=\"OwMimeItem\" title=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\" href=\"");
                w_p.write(getFormEventURL(callbackfunction_p, OwStandardFieldManager.FIELD_ID_KEY + "=" + strID_p));
                w_p.write("\">");

                w_p.write("<img style=\"vertical-align:middle;margin:3px 0px;\" src=\"");
                w_p.write(getContext().getDesignURL());
                w_p.write("/images/plug/owbpm/add_paste.png\" class=\"OwMimeIcon\"");
                w_p.write(" alt=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\" title=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\"/></a>");
            }
            else
            {
                if (!fieldDef_p.isArray() && (clipboard.getContent().size() > 1))
                {
                    pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.howtopasteobject.onlyone", "Please copy only one object to the clipboard first. Then you can paste it using the paste icon.");
                }
                else
                {
                    pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.howtopasteobject", "Please copy an object to the clipboard first. Then you can paste it using the paste icon.");
                }

                w_p.write("<img title=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\" alt=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\" src=\"");
                w_p.write(getContext().getDesignURL());
                w_p.write("/images/plug/owbpm/add_paste_disabled.png\"  class=\"OwMimeIcon\"/>");

            }
        }
    }

    /** Called when user clicks to paste objects from clipboard */
    public void onPasteObjects(HttpServletRequest request_p) throws Exception
    {
        // render paste button if something is in the clipboard
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        if (clipboard.getContentType() != OwClipboard.CONTENT_TYPE_OW_OBJECT)
        {
            return;
        }

        // === copy the values into a new array and add one item
        OwField field = getFieldManager().getField(request_p.getParameter(OwStandardFieldManager.FIELD_ID_KEY));
        OwFieldDefinition definition = field.getFieldDefinition();
        if (definition.isArray())
        {
            // === scalar type
            Object[] values = (Object[]) field.getValue();

            Object[] newValues = null;

            if (null == values)
            {
                // === no previous values
                newValues = new Object[clipboard.getContent().size()];
            }
            else
            {
                newValues = new Object[values.length + clipboard.getContent().size()];

                // copy values
                for (int i = 0; i < values.length; i++)
                {
                    newValues[i] = values[i];
                }
            }

            // add an item from clipboard
            for (int i = 0; i < clipboard.getContent().size(); i++)
            {
                newValues[newValues.length - clipboard.getContent().size() + i] = ((OwClipboardContentOwObject) clipboard.getContent().get(i)).getObject();
            }

            // set new value
            field.setValue(newValues);
        }
        else if (clipboard.getContent().size() == 1)
        {
            field.setValue(((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject());
        }
    }

    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return value_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertLabel(java.io.Writer, boolean, java.lang.String, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String, boolean)
     */
    @Override
    public void insertLabel(Writer w_p, String suffix_p, OwField field, String strID_p, boolean writeLabel_p) throws Exception
    {
        OwFieldDefinition fieldDef_p = field.getFieldDefinition();
        w_p.write("<span>");
        w_p.write(fieldDef_p.getDisplayName(getContext().getLocale()));
        if (suffix_p != null)
        {
            w_p.write(suffix_p);
        }
        w_p.write("</span>");
    }
}
