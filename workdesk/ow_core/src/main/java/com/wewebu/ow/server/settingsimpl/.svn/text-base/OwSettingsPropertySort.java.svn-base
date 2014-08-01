package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSettingsProperty;
import com.wewebu.ow.server.app.OwSettingsSet;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwString2;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A single settings Sort property that can be configured to sort the columns.
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
 *@since 2.5.2.0
 */
public class OwSettingsPropertySort extends OwSettingsPropertyBaseImpl
{
    /** attribute name of a combo select item display name for list properties */
    protected static final String COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME = "displayname";

    /** node name of a combo select item for list properties */
    protected static final String COMBO_SELECT_ITEM_NODE_NAME = "comboselect";

    /** node name in the setting set */
    protected static final String COLUMN_INFO_NODE_NAME = "columninfo";

    /** ID prefix for sort list values */
    protected static final String SORT_PREFIX = "_sort";

    /** attribute name of the property sort order */
    protected static final String SORT_ATTRIBUTE_NAME = "ascending";

    /** default sort order */
    public static final boolean ASC_SORT_ORDER_FLAG = true;

    /** plugin-id */
    private String m_pluginID;

    /** default properties list **/
    private OwSettingsProperty m_columnListProperty;

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#init(org.w3c.dom.Node, org.w3c.dom.Node, java.lang.String)
     */
    public void init(Node propertyDefinitionNode_p, Node valueNode_p, String strSetName_p) throws Exception
    {
        super.init(propertyDefinitionNode_p, valueNode_p, strSetName_p);
        m_pluginID = strSetName_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#createValueList(org.w3c.dom.Node)
     */
    protected List createValueList(Node valueNode_p)
    {
        List valueList = new ArrayList();

        // iterate over the child nodes
        for (Node n = valueNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals(ITEM_VALUE_NODE))
            {
                boolean sortValue = OwXMLDOMUtil.getSafeBooleanAttributeValue(n, SORT_ATTRIBUTE_NAME, ASC_SORT_ORDER_FLAG);

                OwSortCriteria owSortCriteria = new OwSortCriteria((String) getSingleValue(n), sortValue);

                // create list entry
                valueList.add(owSortCriteria);
            }
        }

        return valueList;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#onAdd(javax.servlet.http.HttpServletRequest)
     */
    public void onAdd(HttpServletRequest request_p) throws Exception
    {
        if (canAdd())
        {
            // add item
            OwSortCriteria owSortCriteria = new OwSortCriteria((String) getDefaultListItemValue(), ASC_SORT_ORDER_FLAG);
            ((List) getValue()).add(owSortCriteria);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#canAdd()
     */
    protected boolean canAdd()
    {
        List defaultList = new ArrayList();
        try
        {
            defaultList = getColumnValueList();
        }
        catch (Exception e)
        {
            // ignore
        }
        return ((getValue() == null) || (((List) getValue()).size() < defaultList.size() && ((List) getValue()).size() < ((OwMainAppContext) getContext()).getMaxSortCriteriaCount()));
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#createSingleClonedValue(java.lang.Object)
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return oSingleValue_p;
    }

    /**
     * get the current Plugin ID
     * 
     * @return String ID of the plugin
     */
    private String getPluginID() throws Exception
    {
        return m_pluginID;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#isList()
     */
    public boolean isList()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#updateExternalFormTarget(javax.servlet.http.HttpServletRequest, boolean)
     */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        m_strError = null;

        try
        {
            if (isList())
            {
                // === list value
                List list = (List) getValue();
                int iSize = list.size();

                List newList = new ArrayList();
                for (int i = 0; i < iSize; i++)
                {
                    String sKey = request_p.getParameter(String.valueOf(this.hashCode()) + ITEM_PREFIX + String.valueOf(i));

                    boolean sortValue = Boolean.valueOf(request_p.getParameter(String.valueOf(this.hashCode()) + SORT_PREFIX + String.valueOf(i))).booleanValue();

                    OwSortCriteria owSortCriteria = new OwSortCriteria(sKey, sortValue);

                    newList.add(owSortCriteria);
                }

                // copy new list
                ((List) m_value).clear();
                ((List) m_value).addAll(newList);
            }

        }
        catch (Exception e)
        {
            m_strError = e.getLocalizedMessage();
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#insertFormField(java.io.Writer)
     */
    public void insertFormField(Writer w_p) throws Exception
    {
        if (isList())
        {

            // Script to tackle duplicate selections
            w_p.write("<script type=\"text/javascript\">\n");
            w_p.write("function containsValue(comboBox, value) {\n" + //
                    "	for (i=comboBox.options.length-1;i>=0;i--){\n" + //
                    "   	if (comboBox.options[i].value == value){\n" + //
                    "       	return 'true';\n" + // 
                    "       }\n" + //
                    "   }\n" + //
                    "   return 'false';\n" + //
                    "}\n"); //
            w_p.write("function change(comboBox, currentValue) {\n" + //
                    "     var idParts = comboBox.id.split('_');\n" + //                    
                    "     var formId  = idParts[0];\n" + //
                    "     var comboId  = idParts[1];\n" + //
                    "     var i = 0;\n" + //
                    "     \n" + // 
                    "     while(document.getElementById(formId+'_item'+i) != null) {\n" + //
                    "         if (comboId != 'item'+i) {\n" + //
                    "            otherCombo = document.getElementById(formId+'_item'+i);\n" + //       
                    "            for (m=otherCombo.options.length-1;m>=0;m--){\n" + //
                    "              if (otherCombo.options[m].value == currentValue){\n" + //
                    "                 otherCombo.options[m] = null;\n" + // 
                    "              }\n" + //
                    "            }\n" + //
                    "            for (k=comboBox.options.length-1;k>=0;k--){\n" + //
                    "              if (comboBox.options[k].value != currentValue && (containsValue(otherCombo, comboBox.options[k].value) == 'false')){\n" + //
                    "                 otherCombo.options[otherCombo.options.length]=new Option(comboBox.options[k].text, comboBox.options[k].value)\n" + //
                    "              }\n" + //
                    "            }\n" + //
                    "         }\n" + //
                    "        i=i+1;\n" + //
                    "      }\n" + //
                    "}\n"); //
            w_p.write("</script>\n");

            // === list value
            w_p.write("<table cellspacing='0' cellpadding='0' border='0' style='padding-top: 3pt;'>");

            // Get the loaded sort list
            List sortValuesList = (List) getValue();

            List columnValueList = getColumnValueList();

            List removalList = new ArrayList();

            if (null != sortValuesList)
            {
                // First filter the out-dated columns
                for (int i = 0; i < sortValuesList.size(); i++)
                {
                    OwSortCriteria owSortCriteria = (OwSortCriteria) sortValuesList.get(i);

                    // This sort column no more exists in the current column
                    // list
                    if (!columnValueList.contains(owSortCriteria.getPropertyName()))
                    {
                        removalList.add(owSortCriteria);
                        continue;

                    }
                }

                // Remove the out-dated columns
                if (!removalList.isEmpty())
                {
                    Iterator itr = removalList.iterator();
                    while (itr.hasNext())
                    {
                        Object object = itr.next();
                        ((List) getValue()).remove(object);
                    }
                }

                // Get the filtered sort list
                List newSortValuesList = (List) getValue();

                // render the titles

                for (int i = 0; i < newSortValuesList.size(); i++)
                {
                    OwSortCriteria owSortCriteria = (OwSortCriteria) newSortValuesList.get(i);

                    w_p.write("\n<tr><td>");

                    // insert the value edit box
                    insertFormValue(w_p, owSortCriteria.getPropertyName(), String.valueOf(this.hashCode()) + ITEM_PREFIX + String.valueOf(i), i);

                    w_p.write("</td><td style='vertical-align: middle; margin: 0px;'>");

                    writeDeleteButton(w_p, i);

                    w_p.write("</td>");

                    // render the radio buttons
                    String sSortID = String.valueOf(this.hashCode()) + SORT_PREFIX + String.valueOf(i);
                    String ascChecked = "", descChecked = "";

                    if (owSortCriteria.getAscFlag())
                    {
                        ascChecked = " checked";
                    }
                    else
                    {
                        descChecked = " checked";
                    }

                    w_p.write("<td style='vertical-align: middle;'>");
                    w_p.write("<div class='OwSortOption'>");
                    String id1 = "option1_" + Integer.toString(i + 1);

                    w_p.write("<label for='" + id1 + "'>" + getContext().localize("settingsimpl.OwSettingsPropertySort.ascending", "Ascending") + "</label>");
                    w_p.write("<input type='radio' id='" + id1 + "' name='" + sSortID + "' value='true'" + ascChecked + ">");
                    String id2 = "option2_" + Integer.toString(i + 1);
                    w_p.write("<label for='" + id2 + "'>" + getContext().localize("settingsimpl.OwSettingsPropertySort.descending", "Descending") + "</label>");
                    w_p.write("<input type='radio' id='" + id2 + "' name='" + sSortID + "' value='false'" + descChecked + ">");

                    w_p.write("</div>");
                    w_p.write("</td>");

                    w_p.write("</tr>\n");
                }
            }

            if (canAdd())
            {
                w_p.write("\n<tr><td colspan='2'>");

                writeAddNewItemButton(w_p);

                w_p.write("\n</td></tr>");
            }

            w_p.write("</table>");

        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#insertFormValue(java.io.Writer, java.lang.Object, java.lang.String, int)
     */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        // === read the combo select items: OwComboSelectItem
        List comboItems = createComboSelectList(getPropertyDefinition());
        List sortValuesList = (List) getValue();

        String indexString = "";

        if (comboItems != null)
        {
            List comboItemsModel = new LinkedList();
            for (int i = 0; i < comboItems.size(); i++)
            {
                OwDefaultComboItem item = (OwDefaultComboItem) comboItems.get(i);
                indexString = item.getValue();

                // Don't render if already in the sort list
                if (!(value_p.toString()).equals(item.getValue()) && containsValue(sortValuesList, item.getValue()))
                {
                    continue;
                }
                if (item.getValue() == null)
                {
                    comboItemsModel.add(new OwDefaultComboItem("", item.getDisplayName()));
                }
                else
                {
                    comboItemsModel.add(item);
                }
            }
            OwComboModel model = new OwDefaultComboModel(true, true, value_p == null ? null : value_p.toString(), comboItemsModel);

            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, strID_p, null, null, new OwString2("settingsimpl.OwSettingsPropertyStringCombo.index.combo.title", "%1 %2", getDisplayName(), indexString));

            renderer.addEvent("onchange", "change(this, this.value)");
            renderer.setFieldId(strID_p);
            renderer.setModel(model);
            renderer.setContext((OwMainAppContext) getContext());
            String idIndex = extractIDIndex(strID_p, ITEM_PREFIX, 1);
            OwString description = null;
            if (idIndex != null)
            {
                description = new OwString1("settingsimpl.OwSettingsPropertySort.indexed.sort.property", "Sort property number %1", idIndex);
            }
            else
            {
                description = new OwString("settingsimpl.OwSettingsPropertySort.sort.property", "Sort property ");
            }
            renderer.setFieldDescription(description);
            OwInsertLabelHelper.insertLabelValue(w_p, description.getString(getContext().getLocale()), strID_p);
            ;
            renderer.renderCombo(w_p);

        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#getValueNode(org.w3c.dom.Document)
     */
    public Node getValueNode(Document doc_p)
    {
        // === create a value element node
        Node valueNode = doc_p.createElement(getName());

        // === multiple values
        List list = (List) getValue();
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            OwSortCriteria owSortCriteria = (OwSortCriteria) it.next();

            Node valueListEntryNode = doc_p.createElement(ITEM_VALUE_NODE);
            Node sortAttr = doc_p.createAttribute(SORT_ATTRIBUTE_NAME);

            sortAttr.setNodeValue(Boolean.valueOf(owSortCriteria.getAscFlag()).toString());
            valueListEntryNode.getAttributes().setNamedItem(sortAttr);

            valueNode.appendChild(valueListEntryNode);

            valueListEntryNode.appendChild(valueListEntryNode.getOwnerDocument().createTextNode(owSortCriteria.getPropertyName()));
        }

        return valueNode;
    }

    /**
     * get the property definition
     * 
     * @return OwSettingsProperty if property definition found, null otherwise
     */
    private OwSettingsProperty getPropertyDefinition() throws Exception
    {
        if (m_columnListProperty != null)
        {
            return m_columnListProperty;
        }

        OwSettingsSet settingSet = ((OwMainAppContext) getContext()).getSettings().getSettingsInfo(getPluginID());

        if (settingSet != null && settingSet.getProperties() != null)
        {
            Map properties = settingSet.getProperties();

            Iterator itr = properties.entrySet().iterator();

            while (itr.hasNext())
            {
                Map.Entry entry = (Map.Entry) itr.next();
                if (entry.getKey().equals(COLUMN_INFO_NODE_NAME))
                {
                    m_columnListProperty = (OwSettingsPropertyBaseImpl) entry.getValue();
                    break;
                }
            }
        }

        return m_columnListProperty;
    }

    /**
     * create a list of values, if property is a list
     * 
     * @param property_p
     *            Node with child value nodes
     * @return List of Objects representing values
     */
    protected List createComboSelectList(OwSettingsProperty property_p) throws Exception
    {
        Node definitionNode = ((OwSettingsPropertyBaseImpl) property_p).getPropertyDefinitionNode();
        OwXMLUtil nodeWrapper = new OwStandardXMLUtil(definitionNode);
        List valueList = new ArrayList();
        HashSet duplicateDedectionSet = new HashSet();

        String type = nodeWrapper.getSafeStringAttributeValue("type", "");

        // The user can only select these types, cannot add new values from
        // settingset
        if (type.equals("com.wewebu.ow.server.settingsimpl.OwSettingsPropertyStringConfigurationBox") || type.equals("com.wewebu.ow.server.settingsimpl.OwSettingsPropertyStringCombo"))
        {
            List currentColmnList = getColumnValueList();

            // iterate over the child nodes
            for (Node n = definitionNode.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeName().equals(COMBO_SELECT_ITEM_NODE_NAME))
                {
                    // create list entry
                    OwXMLUtil currentNodeWrapper = new OwStandardXMLUtil(n);

                    String strDisplayName = null;
                    String strValue = currentNodeWrapper.getSafeTextValue(null);

                    if (strValue == null || duplicateDedectionSet.contains(strValue) || !currentColmnList.contains(strValue))
                    {
                        continue;
                    }

                    duplicateDedectionSet.add(strValue);

                    // there is a label defined for the value
                    strDisplayName = ((OwMainAppContext) getContext()).localizeLabel(strValue);
                    if (strDisplayName.equals(strValue))
                    {
                        // no label defined for value so get display name
                        strDisplayName = currentNodeWrapper.getSafeStringAttributeValue(COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME, "?");
                    }
                    valueList.add(new OwDefaultComboItem(strValue, strDisplayName));
                }
            }
        }
        else
        {
            Iterator itr = getColumnValueList().iterator();

            while (itr.hasNext())
            {
                String strDisplayName = null;
                String strValue = (String) itr.next();

                if (strValue == null || duplicateDedectionSet.contains(strValue))
                {
                    continue;
                }

                duplicateDedectionSet.add(strValue);

                if (((OwMainAppContext) getContext()).hasLabel(strValue))
                {
                    // there is a label defined for the value
                    strDisplayName = ((OwMainAppContext) getContext()).localizeLabel(strValue);
                }
                else
                {
                    // no label defined for value
                    strDisplayName = strValue;
                }

                valueList.add(new OwDefaultComboItem(strValue, strDisplayName));
            }

        }

        return valueList;
    }

    private boolean containsValue(List valueList_p, String value_p)
    {
        boolean isValueContained = false;

        for (int i = 0; i < valueList_p.size(); i++)
        {
            OwSortCriteria sortCriteria = (OwSortCriteria) valueList_p.get(i);
            String propertyName = sortCriteria.getPropertyName();

            if (propertyName != null && propertyName.equals(value_p))
            {
                isValueContained = true;
                break;
            }
        }
        return isValueContained;
    }

    private List getColumnValueList() throws Exception
    {
        OwSettingsProperty property = getPropertyDefinition();
        List valueList = new ArrayList();

        if (property != null)
        {
            valueList = ((List) property.getValue());
        }

        return valueList;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.settingsimpl.OwSettingsPropertyBaseImpl#getDefaultListItemValue()
     */
    protected Object getDefaultListItemValue() throws Exception
    {
        List defaultList = getColumnValueList();
        List sortValuesList = (List) getValue();
        Object itemValue = null;

        Iterator itr = defaultList.iterator();

        while (itr.hasNext())
        {
            String strValue = (String) itr.next();

            if (sortValuesList == null || !containsValue(sortValuesList, strValue))
            {
                itemValue = strValue;
            }
        }

        return itemValue;
    }
}