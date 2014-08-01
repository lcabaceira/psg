package com.alfresco.ow.server.fieldctrlimpl.objectreferenceselect;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardFieldManager;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Field Control to select an object from a search template and store the object id as String.
 * The id's are displayed in field control as an object list with optional document functions.
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
public class OwObjectReferenceSelectFieldControl extends OwFieldManagerControl
{
    private static final String BTN_TITLE_DEFAULT = "Select Object";
    private static final String DLG_TITLE_DEFAULT = "Select object from list";

    private static final Logger LOG = OwLog.getLogger(OwObjectReferenceSelectFieldControl.class);

    public static final String SEARCHTEMPLATE_NAME_ATTRIBUTE = "searchtemplate";
    public static final String DOCUMENT_FUNCTION_PLUGINS_NODE = "DocumentFunctionPlugins";
    public static final String DOCUMENT_FUNCTION_PLUGIN_ID = "pluginid";

    /** Reference to configuration node */
    protected OwXMLUtil m_config;

    protected String m_selectedValue = null;

    /**Editable list controls mapped by field control ID*/
    protected Map m_editableListViews = new HashMap();
    /**Editable list controls mapped by field class names */
    protected Map m_readonlyListViews = new HashMap();

    /**Editable list controls mapped by field class names */
    protected List<String> m_enabledDocumentFunctionIDs;

    /** list of poperties shown in list view */
    protected List<String> m_columninfoProperties;

    public static final String SELECT_BUTTON_TITLE_NODE = "ButtonTitle";
    public static final String SELECT_DIALOG_TITLE_NODE = "DialogTitle";
    private static final String LOCALIZE_ID_ATT = "localizeId";

    protected String selectButtonTitleLocalizationKey;
    protected String selectButtonTitleDefaultText;

    protected String selectDialogTitleLocalizationKey;
    protected String selectDialogTitleDefaultText;

    /**
     * Initializes the field control from configuration node
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#init(com.wewebu.ow.server.app.OwFieldManager, org.w3c.dom.Node)
     */
    @Override
    public void init(final OwFieldManager fieldmanager_p, final Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);

        // get configuration
        m_config = new OwStandardXMLUtil(configNode_p);

        m_enabledDocumentFunctionIDs = new LinkedList();
        OwXMLUtil documentFunctionsNode = m_config.getSubUtil("EnabledDocumentFunctions");

        this.selectButtonTitleLocalizationKey = null;
        this.selectButtonTitleDefaultText = BTN_TITLE_DEFAULT;
        OwXMLUtil btnTitlexml = m_config.getSubUtil(SELECT_BUTTON_TITLE_NODE);
        if (null != btnTitlexml)
        {
            this.selectButtonTitleLocalizationKey = btnTitlexml.getSafeStringAttributeValue(LOCALIZE_ID_ATT, null);
            this.selectButtonTitleDefaultText = btnTitlexml.getSafeTextValue(BTN_TITLE_DEFAULT);
        }

        this.selectDialogTitleLocalizationKey = null;
        this.selectDialogTitleDefaultText = DLG_TITLE_DEFAULT;
        OwXMLUtil dlgTitlexml = m_config.getSubUtil(SELECT_DIALOG_TITLE_NODE);
        if (null != dlgTitlexml)
        {
            this.selectDialogTitleLocalizationKey = dlgTitlexml.getSafeStringAttributeValue(LOCALIZE_ID_ATT, null);
            this.selectDialogTitleDefaultText = dlgTitlexml.getSafeTextValue(DLG_TITLE_DEFAULT);
        }

        // documentFunctionsNode is not null and document functions are enabled
        // for this master plugin
        if (documentFunctionsNode != null && documentFunctionsNode.getSafeBooleanAttributeValue("enable", false))
        {
            // === filter document function plugins if filter list defined
            List docfunctionsList = documentFunctionsNode.getSafeStringList();
            // remove duplicated IDs
            Set docfunctions = new LinkedHashSet(docfunctionsList);
            if (docfunctions.size() != 0)
            {
                // === use only defined functions
                Iterator it = docfunctions.iterator();
                while (it.hasNext())
                {
                    String id = (String) it.next();
                    if (id != null && id.length() > 0)
                    {
                        m_enabledDocumentFunctionIDs.add(id);
                    }
                }
            }
        }

        m_columninfoProperties = new LinkedList<String>();
        OwXMLUtil columninfoNode = m_config.getSubUtil("columninfo");
        if (columninfoNode != null)
        {
            m_columninfoProperties = columninfoNode.getSafeStringList();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertReadOnlyField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object)
     */
    @Override
    public void insertReadOnlyField(final Writer w_p, final OwFieldDefinition fieldDef_p, final Object value_p) throws Exception
    {
        List<Object> listValues = createListValues(value_p, fieldDef_p);
        String className = fieldDef_p.getClassName();
        renderReadonlyList(w_p, className, listValues);
    }

    /**
     * Inserts a single edit field whose value can be set from user select dialog
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
     */
    @Override
    public void insertEditField(final Writer w_p, final OwFieldDefinition fieldDef_p, final OwField field_p, final String strID_p) throws Exception
    {
        List<Object> listValues = null;
        if (field_p == null)
        {
            listValues = new ArrayList<Object>();
        }
        else
        {
            listValues = createListValues(field_p.getValue(), fieldDef_p);
        }

        renderEditableList(w_p, strID_p, listValues);
        if (fieldDef_p.isArray() || field_p.getValue() == null)
        {
            w_p.write("<div style='float:left;clear:left'>");
            renderSelectObjectButton(w_p, "SelectObject", strID_p);
            w_p.write("</div>");
        }
    }

    /**
     * renders select button
     * @param w_p
     * @param callbackfunction_p
     * @param strID_p
     * @throws Exception
     */
    protected void renderSelectObjectButton(Writer w_p, String callbackfunction_p, String strID_p) throws Exception
    {
        // no validation for this form request!
        String selectObjectEventURL = getFormEventURL(callbackfunction_p, OwStandardFieldManager.FIELD_ID_KEY + "=" + strID_p, true);

        w_p.write("&nbsp; <input type=\"button\" name=\"Select a object\" onClick=\"");
        w_p.write(selectObjectEventURL);
        String btnTitle = (null != this.selectButtonTitleLocalizationKey) ? getContext().localize(this.selectButtonTitleLocalizationKey, this.selectButtonTitleDefaultText) : this.selectButtonTitleDefaultText;

        w_p.write("\" value=\"" + btnTitle + "\" />");
    }

    /**
     * Event handler called when object is selected
     * @param request_p
     * @throws OwException
     */
    public void onSelectObject(final HttpServletRequest request_p) throws OwException
    {

        try
        {
            final String fieldId = request_p.getParameter(OwStandardFieldManager.FIELD_ID_KEY);

            OwObjectReferenceSelectDialog selectDlg = new OwObjectReferenceSelectDialog(m_config);
            String dlgTitle = (null != this.selectDialogTitleLocalizationKey) ? getContext().localize(this.selectDialogTitleLocalizationKey, this.selectDialogTitleDefaultText) : this.selectDialogTitleDefaultText;
            selectDlg.setTitle(dlgTitle);
            getContext().openDialog(selectDlg, new OwDialog.OwDialogListener() {

                public void onDialogClose(OwDialog dialogView_p) throws Exception
                {
                    OwObjectReferenceSelectDialog dlg = (OwObjectReferenceSelectDialog) dialogView_p;
                    List<OwObject> objects = dlg.getSelection();
                    if (null != objects)
                    {
                        setFieldValue(fieldId, objects);

                        // SOL-33 call update for validation and error cleanup
                        //getFieldManager().update(dialogView_p.getContext().getHttpRequest(), null, null);
                    }
                }

                public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
                {
                    // do nothing

                }
            });
        }
        catch (Exception ex)
        {
            LOG.error("Problem with the select object dialog", ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     */
    @Override
    public Object updateField(final HttpServletRequest request_p, final OwFieldDefinition fieldDef_p, final Object value_p, final String strID_p) throws Exception
    {
        return value_p;
    }

    /**
     * Applies a hard coded/default configuration to the give list view.
     * The default configuration enables document plugins and adds the object name to the 
     * list columns. 
     * @param listView_p
     */
    protected void applyConfiguration(OwObjectListView listView_p)
    {
        //        listView_p.setViewMask(OwObjectListView.VIEW_MASK_USE_DOCUMENT_PLUGINS | OwObjectListView.VIEW_MASK_INSTANCE_PLUGINS);
        listView_p.setViewMask(OwObjectListView.VIEW_MASK_USE_DOCUMENT_PLUGINS | OwObjectListView.VIEW_MASK_INSTANCE_PLUGINS);

        List columninfo = new ArrayList();

        OwFieldDefinition fielddef = null;
        for (Iterator iterator = m_columninfoProperties.iterator(); iterator.hasNext();)
        {
            String propStr = (String) iterator.next();
            try
            {
                fielddef = ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(propStr, null);
                columninfo.add(new OwStandardFieldColumnInfo(fielddef, OwFieldColumnInfo.ALIGNMENT_DEFAULT));
            }
            catch (Exception e)
            {
                // === property not found
                // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                LOG.warn("Could not resolve property for contentlist: " + propStr, e);
            }
        }

        listView_p.setColumnInfo(columninfo);

    }

    /**
     * Set the enabled document functions list for the given {@link OwObjectListView} object.
     * @param listView_p - the list view object.
     * @throws Exception 
     */
    private void enableFunctions(OwObjectListView listView_p) throws Exception
    {
        if (m_enabledDocumentFunctionIDs != null)
        {
            // List configuredDocumentFunctions = listView_p.getDocumentFunctionPluginList();
            List enabledDocumentFunctions = new LinkedList();
            for (Iterator i = m_enabledDocumentFunctionIDs.iterator(); i.hasNext();)
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

            OwDeleteObjectReferenceFunction m_delAttachmentFunction = new OwDeleteObjectReferenceFunction(this.getFieldManager(), strID_p);
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

    /**
     * Detach
     */
    @Override
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

    /**
     * Converts a field value to a list of objects to be used with  {@link OwObjectListView#setObjectList(com.wewebu.ow.server.ecm.OwObjectCollection, OwObject)}.
     * {@link com.wewebu.ow.server.ecm.OwUnresolvedReference} are ignored and logged.
     * @param fieldValue_p
     * @param fieldDef_p
     * @return an array of objects to be used as list control values through {@link OwObjectListView#setObjectList(com.wewebu.ow.server.ecm.OwObjectCollection, OwObject)}
     * @throws Exception
     */
    private List<Object> createListValues(final Object fieldValue_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        List<Object> listValues = new ArrayList<Object>();

        if (fieldValue_p == null)
        {
            return listValues;
        }

        Object[] fieldValuesArray = null;

        if (fieldDef_p.isArray())
        {
            fieldValuesArray = (Object[]) fieldValue_p;
        }
        else
        {
            fieldValuesArray = new Object[] { fieldValue_p };
        }

        for (int i = 0; i < fieldValuesArray.length; i++)
        {
            OwObject obj = (OwObject) fieldValuesArray[i];
            listValues.add(obj);
        }

        return listValues;
    }

    /**
     * Fills the given list with the set of provided values and renders it on the provided Writer. 
     * @param w_p
     * @param listView_p
     * @param values_p
     * @throws Exception
     */
    private void renderList(Writer w_p, OwObjectListView listView_p, List<Object> values_p) throws Exception
    {
        w_p.write("<div style='floaf:left;'>");
        OwStandardObjectCollection objectList = new OwStandardObjectCollection();
        if (values_p != null)
        {
            objectList.addAll(values_p);
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
    private void renderEditableList(Writer w_p, String strID_p, List<Object> values_p) throws Exception
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
    private void renderReadonlyList(Writer w_p, String listId_p, List<Object> values_p) throws Exception
    {
        OwObjectListView listView = readonlyListViewFormControlId(listId_p);
        renderList(w_p, listView, values_p);
    }

    private void setFieldValue(String fieldId_p, List<OwObject> newObjects_p) throws Exception
    {
        if (newObjects_p == null || newObjects_p.size() == 0)
        {
            return;
        }

        // === copy the values into a new array and add one item
        OwField field = getFieldManager().getField(fieldId_p);
        OwFieldDefinition definition = field.getFieldDefinition();

        if (definition.isArray())
        {
            // === scalar type
            Object[] values = (Object[]) field.getValue();

            Object[] newValues = null;

            if (null == values)
            {
                // === no previous values
                newValues = new Object[newObjects_p.size()];
            }
            else
            {
                newValues = new Object[values.length + newObjects_p.size()];

                // copy values
                for (int i = 0; i < values.length; i++)
                {
                    newValues[i] = values[i];
                }
            }

            // add new items to field value
            final int newObjectSize = newObjects_p.size();
            for (int i = 0; i < newObjectSize; i++)
            {
                newValues[newValues.length - newObjectSize + i] = newObjects_p.get(i);
            }

            // set new value
            field.setValue(newValues);
        }
        else
        {
            field.setValue(newObjects_p.get(0));
        }
    }

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
