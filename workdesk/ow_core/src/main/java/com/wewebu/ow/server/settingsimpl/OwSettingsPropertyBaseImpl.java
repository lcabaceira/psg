package com.wewebu.ow.server.settingsimpl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Base Implementation of the OwSettingsProperty class.
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
public abstract class OwSettingsPropertyBaseImpl extends OwAbstractSettingsPropertyControl
{

    /** Logger for this class */
    private static final Logger LOG = OwLogCore.getLogger(OwSettingsPropertyBaseImpl.class);

    /** default value of property */
    protected Object m_defaultValue;

    /** query key for the item parameter for the delete event */
    protected static final String ITEM_QUERY_KEY = "item";

    /** ID prefix for list values */
    protected static final String ITEM_PREFIX = "_item";

    /** true = value is list of values */
    protected boolean m_fList;

    /** current value of property */
    protected Object m_value;

    /** a error message for this field, if update failed */
    protected String m_strError;

    public OwSettingsPropertyBaseImpl()
    {
        this.m_externalFormEventTarget = this;
    }

    /** get current value of property
     * @return Object if isList() is true, Object otherwise
     */
    public Object getValue()
    {
        return m_value;
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        // default implementation. 
        // Without a control to submit requests, we don't need a clone.
        return m_value;
    }

    /** true = value was modified, false otherwise
     */
    public boolean isModified()
    {
        /////////////////////////////////
        // TODO: work with cloned value
        return false;
    }

    /** signal that value was saved and modified flag can be reset */
    public void saved()
    {
        /////////////////////////////////
        // TODO: work with cloned value
    }

    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue() throws Exception
    {
        // default returns null
        return null;
    }

    /** get flag indicating list or scalar value
     * @return boolean true = list value, otherwise scalar
     */
    public boolean isList()
    {
        return m_fList;
    }

    /** set current value of property, to be overridden
     *
     * @param value_p Object
     */
    public void setValue(Object value_p)
    {
        // set current value list from value node
        m_value = value_p;
    }

    /** update the target after a form event, so it can set its form fields
     *
     * @param request_p HttpServletRequest
     * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
     *
     * @return true = field data was valid, false = field data was invalid
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

                LinkedList newList = new LinkedList();
                for (int i = 0; i < iSize; i++)
                {
                    newList.add(getSingleValueFromRequest(request_p, String.valueOf(this.hashCode()) + ITEM_PREFIX + String.valueOf(i)));
                }

                // copy new list
                ((List) m_value).clear();
                ((List) m_value).addAll(newList);
            }
            else
            {
                // === single value
                m_value = getSingleValueFromRequest(request_p, String.valueOf(this.hashCode()));
            }
        }
        catch (Exception e)
        {
            m_strError = e.getLocalizedMessage();
        }

        return true;
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p) throws Exception
    {
        return request_p.getParameter(strID_p);
    }

    /** gets a error message for this field, if update failed, clears the message automatically 
     *
     * @return String error message or an empty string
     */
    public String getSafePropertyError()
    {
        String strRet;

        if (m_strError == null)
        {
            strRet = "";
        }
        else
        {
            strRet = m_strError;
        }

        m_strError = null;

        return strRet;
    }

    /**
     * @see com.wewebu.ow.server.app.OwSettingsPropertyControl#hasError()
     */
    public boolean hasError()
    {
        return m_strError != null;
    }

    /**
     * @see com.wewebu.ow.server.app.OwSettingsPropertyControl#getPropertyError()
     */
    public String getPropertyError()
    {
        return m_strError == null ? "" : m_strError;
    }

    /** insert the property into a HTML form for editing
     *
     * @param w_p Writer to write HTML code to
     */
    public void insertFormField(Writer w_p) throws Exception
    {
        if (isList())
        {
            // === list value
            w_p.write("<table>");

            List list = (List) getValue();
            if (null != list)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    w_p.write("\n<tr><td>");

                    // insert the value edit box
                    insertFormValue(w_p, list.get(i), String.valueOf(this.hashCode()) + ITEM_PREFIX + String.valueOf(i), i);

                    w_p.write("</td><td>");

                    writeDeleteButton(w_p, i);

                    w_p.write("</td></tr>\n");
                }
            }

            if (canAdd())
            {
                w_p.write("\n<tr><td colspan='2'>");

                writeAddNewItemButton(w_p);

                w_p.write("\n</td><tr>");
            }

            w_p.write("</table>");
        }
        else
        {
            // === single value
            insertFormValue(w_p, getValue(), String.valueOf(this.hashCode()), 0);
        }
    }

    /**
     * Render the add new item button.
     * @param w_p - the {@link Writer} object
     * @throws Exception
     */
    protected void writeAddNewItemButton(Writer w_p) throws Exception
    {
        // insert add button
        String addButtonTooltip = getContext().localize("app.OwEditableProperty.add", "add");
        w_p.write("<a title=\"");
        w_p.write(addButtonTooltip);
        w_p.write("\" href=\"");
        w_p.write(getFormEventURL("Add", null));
        w_p.write("\"><img src=\"");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/addbtn.png\"");
        if (getDisplayName() != null)
        {
            addButtonTooltip = getContext().localize1("settingsimpl.OwSettingsPropertyBaseImpl.addarrayitemtooltipimg", "Add new element for property %1", getDisplayName());
        }
        w_p.write(" alt=\"");
        w_p.write(addButtonTooltip);
        w_p.write("\" title=\"");
        w_p.write(addButtonTooltip);
        w_p.write("\"/></a>");
    }

    /**
     * Render the delete button.
     * @param w_p - the {@link Writer} object
     * @param position_p - the position index
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void writeDeleteButton(Writer w_p, int position_p) throws Exception
    {
        // add delete button
        String tooltip = getContext().localize("app.OwEditableProperty.delete", "delete");
        w_p.write("&nbsp;<a title=\"");
        w_p.write(tooltip);
        w_p.write("\" href=\"");
        w_p.write(getFormEventURL("Delete", ITEM_QUERY_KEY + "=" + String.valueOf(position_p)));
        w_p.write("\">");
        String displayName = getDisplayName();
        if (displayName != null)
        {
            tooltip = getContext().localize2("settingsimpl.OwSettingsPropertyBaseImpl.delarrayitemindexed", "Delete element at position %1 from property %2", "" + (position_p + 1), displayName);
        }
        w_p.write("<img style=\"vertical-align:middle;\" src=\"");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/deletebtn.png\" alt=\"");
        w_p.write(tooltip);
        w_p.write("\" title=\"");
        w_p.write(tooltip);
        w_p.write("\"/></a>");
    }

    /** called when user clicked a delete button on a list entry
     *
     * @param request_p HttpServletRequest
     */
    public void onDelete(HttpServletRequest request_p) throws Exception
    {
        // get item to be deleted
        int iItem = Integer.parseInt(request_p.getParameter(ITEM_QUERY_KEY));
        // remove item
        ((List) getValue()).remove(iItem);
    }

    /** check if additional items can be added to the list value */
    protected boolean canAdd()
    {
        return ((0 == getMaxListSize()) || (getValue() == null) || (((List) getValue()).size() < getMaxListSize()));
    }

    /** called when user clicked the add item button for a list property
     *
     * @param request_p HttpServletRequest
     */
    public void onAdd(HttpServletRequest request_p) throws Exception
    {
        if (canAdd())
        {
            if (getValue() == null)
            {
                throw new OwObjectNotFoundException("OwSettingsPropertyBaseImpl.onAdd: The current value of the property is null, possible cause: Attribute Bag or DB DataSource is missing or wrong configured....");
            }
            else
            {
                // add item
                ((List) getValue()).add(getDefaultListItemValue());
            }
        }
    }

    /** overridable to insert a single value into a edit HTML form
     *
     * @param w_p Writer to write HTML code to
     * @param value_p the property value to edit
     * @param strID_p String the ID of the HTML element for use in onApply
    * @param iIndex_p int Index of item if it is a list
    */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        String strValue = "";
        if (null != value_p)
        {
            strValue = value_p.toString();
        }
        String title = getDisplayName();
        String indexTitleString = extractIDIndex(strID_p, ITEM_PREFIX, 1);
        if (indexTitleString != null)
        {
            title = getContext().localize2("settingsimpl.OwSettingsPropertyBaseImpl.indexed.string.tite", "%1 . Entry number %2.", getDisplayName(), indexTitleString);
        }

        w_p.write("<input class=\"OwInputControl\" type=\"text\" title=\"");
        w_p.write(title);
        w_p.write("\" name=\"");
        w_p.write(strID_p);
        w_p.write("\" id=\"");
        w_p.write(strID_p);
        w_p.write("\" value=\"");
        w_p.write(strValue);
        w_p.write("\" />");
    }

    /** set the default value
     */
    public void setDefault()
    {
        if (isList())
        {
            // === clone the list
            m_value = new ArrayList();
            ((List) m_value).addAll((List) m_defaultValue);
        }
        else
        {
            m_value = m_defaultValue;
        }
    }

    /** return the property value as a DOM Node for serialization
     *
     * @param doc_p DOM Document to add to
     *
     * @return Node
     */
    public Node getValueNode(Document doc_p)
    {
        // === create a value element node
        Node valueNode = doc_p.createElement(getName());

        // === add sub value node/s
        if (m_fList)
        {
            // === multiple values
            List list = (List) getValue();
            Iterator it = list.iterator();
            while (it.hasNext())
            {
                Object value = it.next();
                if (value != null && !"".equals(value))
                {
                    Node valueListEntryNode = doc_p.createElement(ITEM_VALUE_NODE);
                    valueNode.appendChild(valueListEntryNode);
                    appendSingleValueNode(valueListEntryNode, value);
                }
                else
                {
                    LOG.debug("OwSettingsPropertyBaseImpl.getValueNode: Empty setting value was removed, empty value for setting set can not be saved...");
                }
            }
        }
        else
        {
            // === single value
            Object value = getValue();
            if (value != null && !"".equals(value))
            {
                Node valueListEntryNode = doc_p.createElement(ITEM_VALUE_NODE);
                valueNode.appendChild(valueListEntryNode);
                appendSingleValueNode(valueListEntryNode, value);
            }
            else
            {
                LOG.debug("OwSettingsPropertyBaseImpl.getValueNode: Empty setting value was removed, empty value for setting set can not be saved...");
            }
        }

        return valueNode;
    }

    /** overridable, return the given value as a DOM Node for serialization
     *
     * @param valueRootNode_p root Node of the property
     * @param value_p Object to append as DOM Node
     *
     */
    protected void appendSingleValueNode(Node valueRootNode_p, Object value_p)
    {
        valueRootNode_p.appendChild(valueRootNode_p.getOwnerDocument().createTextNode(value_p.toString()));
    }

    /**
     * 
     * @param strID_p a field control ID String 
     * @param indexSeparator_p numeric index separator of the strID_p
     * @param indexElement_p the index of the string index after strID_p split around   indexElement_p
     * @return the String value index based title information for array and complex values
     * @since 3.0.0.0 
     */
    protected String extractIDIndex(String strID_p, String indexSeparator_p, int indexElement_p)
    {
        String[] idElements = strID_p.split(indexSeparator_p);
        if (idElements.length > indexElement_p)
        {
            try
            {
                return "" + (Integer.parseInt(idElements[indexElement_p]) + 1);
            }
            catch (NumberFormatException e)
            {
                LOG.error("OwSettingsPropertyBaseImpl.getValueIndexTitle(): Invalid indexed field ID component string : " + idElements[indexElement_p]);
            }
        }
        return null;
    }

    /** get the maximum count of list items allowed
     * 
     * @return int max list size or 0 for infinity size
     */
    protected int getMaxListSize()
    {
        return OwXMLDOMUtil.getSafeIntegerAttributeValue(getPropertyDefinitionNode(), OwBaseConfiguration.PLUGIN_SETATTR_MAX_SIZE, 0);
    }

    /** set current value of property, to be overridden
     *
     * @param propertyDefinitionNode_p the node which defines the property in the plugin descriptors setting
     * @param valueNode_p the node with the current value
     * @param strSetName_p name of the property set for which the property is created
     */
    public void init(Node propertyDefinitionNode_p, Node valueNode_p, String strSetName_p) throws Exception
    {
        super.init(propertyDefinitionNode_p, valueNode_p, strSetName_p);

        m_fList = OwXMLDOMUtil.getSafeBooleanAttributeValue(propertyDefinitionNode_p, OwBaseConfiguration.PLUGIN_SETATTR_LIST, false);

        // === init value
        Object value;

        if (m_fList)
        {
            // === multiple values
            // set current value list from value node
            value = createValueList(valueNode_p);
            // set default value list from description node
            m_defaultValue = createValueList(propertyDefinitionNode_p);
        }
        else
        {
            // === single value
            // set current value from value node
            value = createValue(valueNode_p);
            // set default value from description node
            m_defaultValue = createValue(propertyDefinitionNode_p);
        }

        // call base class init
        setValue(value);
    }

    /** overridable to create a single value for the given node
     * @return Object with value
     */
    protected Object getSingleValue(Node valueNode_p)
    {
        // default implementation returns string from XML Text node child
        if (valueNode_p.getFirstChild() == null)
        {
            return null;
        }
        else
        {
            return valueNode_p.getFirstChild().getNodeValue();
        }
    }

    /** to create a single value for the given definition node
     *  NOTE: Reads the value from the  ITEM_VALUE_NODE node.
     *
     * @return Object with value
     */
    protected Object createValue(Node valueNode_p)
    {
        for (Node n = valueNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals(ITEM_VALUE_NODE))
            {
                // create list entry
                return getSingleValue(n);
            }
        }

        return null;
    }

    /** create a list of values, if property is a list 
     * @param valueNode_p Node with child value nodes
     * @return List of Objects representing values 
     */
    protected List createValueList(Node valueNode_p)
    {
        List valueList = new ArrayList();

        // iterate over the child nodes
        for (Node n = valueNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals(ITEM_VALUE_NODE))
            {
                // create list entry
                valueList.add(getSingleValue(n));
            }
        }

        return valueList;
    }

    public String toString()
    {
        StringBuilder ret = new StringBuilder();

        ret.append("OwSettingsPropertyBaseImpl: [");
        ret.append(", getValue() = ").append(getValue());
        ret.append(", isList() = ").append(isList());
        ret.append(", getDisplayName() = ").append(getDisplayName());
        ret.append(", getName() = ").append(getName());
        ret.append("]\r\n");

        return ret.toString();
    }

    @Override
    public void insertLabel(Writer w_p) throws Exception
    {
        insertLabelValue(w_p, getDisplayName(), String.valueOf(this.hashCode()), 0);
    }

    protected void insertLabelValue(Writer w_p, String displayName, String strID_p, int iIndex_p) throws IOException
    {

        if (!isList())
        {
            w_p.write("<label for=\"" + strID_p + "\">");
            w_p.write(displayName + ":");
            w_p.write("</label>");
        }
        else
        {
            w_p.write(displayName + ":");
        }
    }
}