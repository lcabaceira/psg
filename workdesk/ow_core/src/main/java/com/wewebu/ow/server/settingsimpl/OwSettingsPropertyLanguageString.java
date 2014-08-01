package com.wewebu.ow.server.settingsimpl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * A single settings String property that can be configured language dependent.
 * e.g. you can set a different string for each language.
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
public class OwSettingsPropertyLanguageString extends OwAbstractSettingsPropertyControl
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSettingsPropertyLanguageString.class);

    private static final String LANGUAGE_ATTRIBUTE_NAME = "lang";

    /** default value of property */
    protected Map<?, ?> m_defaultValue;

    /** query key for the item parameter for the delete event */
    protected static final String ITEM_QUERY_KEY = "item";

    /** current value of property */
    protected Map m_value;

    /** a error message for this field, if update failed */
    protected String m_strError;

    /** current locale */
    private Locale m_Locale;

    /** get current value of property
     * @return Object if isList() is true, Object otherwise
     */
    public Object getValue()
    {
        Object ret = m_value.get(m_Locale.getLanguage());

        if (ret == null)
        {
            ret = "dd.MM.yyyy (HH:mm)";
        }

        return ret;
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
        return "";
    }

    /** get flag indicating list or scalar value
     * @return boolean true = list value, otherwise scalar
     */
    public boolean isList()
    {
        return true;
    }

    /** set current value of property, to be overridden
     *
     * @param value_p Object
     */
    public void setValue(Object value_p)
    {
        // set current value list from value node
        m_value = (Map) value_p;
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
            int iSize = m_value.keySet().size();

            m_value.clear();

            for (int i = 0; i < iSize; i++)
            {
                String sValue = request_p.getParameter(String.valueOf(this.hashCode()) + "_item" + String.valueOf(i));
                String sKey = request_p.getParameter(String.valueOf(this.hashCode()) + "_key" + String.valueOf(i));

                m_value.put(sKey, sValue);
            }
        }
        catch (Exception e)
        {
            m_strError = e.getLocalizedMessage();
        }

        return true;
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
     * @since 2.5.2.0
     */
    public boolean hasError()
    {
        return m_strError != null;
    }

    /**
     * @see com.wewebu.ow.server.app.OwSettingsPropertyControl#getPropertyError()
     * @since 2.5.2.0
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
            w_p.write("<table cellspacing='0' cellpadding='0' border='0'>");

            Iterator it = m_value.keySet().iterator();
            int i = 0;
            String designRoot = getContext().getDesignURL();
            String displayName = getDisplayName();
            while (it.hasNext())
            {
                String sKey = (String) it.next();
                String sValue = (String) m_value.get(sKey);
                if (sValue == null)
                {
                    sValue = "";
                }

                w_p.write("\n<tr><td>");

                w_p.write("<div style='clear:both'>");
                // insert the value edit box
                String sValueID = String.valueOf(this.hashCode()) + "_item" + String.valueOf(i);
                String sKeyID = String.valueOf(this.hashCode()) + "_key" + String.valueOf(i);
                w_p.write("<div style='float:left'>");
                String dateFormatDisplayName = getContext().localize1("OwSettingsPropertyLanguageString.dateFormatIndexed1", "Date format for element at position %1", "" + (i + 1));
                if (displayName != null)
                {
                    dateFormatDisplayName = getContext().localize2("OwSettingsPropertyLanguageString.dateFormatIndexed2", "Date format for element at position %1 from %2", "" + (i + 1), displayName);
                }
                insertLabelValue(w_p, dateFormatDisplayName, sValueID);
                w_p.write("<input type='text' name='" + sValueID + "' value='" + sValue + "' id='" + sValueID + "'>");
                //w_p.write("<input size='3' type='text' name='" + sKeyID + "' value='" + sKey + "'>");
                w_p.write("</div>");
                List languageList = createLanguageListItems();
                OwComboModel model = new OwDefaultComboModel(false, false, sKey, languageList);
                OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, sKeyID, null, null, null);
                w_p.write("<div style='float:left'>");
                String languageDisplayName = getContext().localize1("OwSettingsPropertyLanguageString.languageindexed1", "Language for element at position %1", "" + (i + 1));
                if (displayName != null)
                {
                    languageDisplayName = getContext().localize2("OwSettingsPropertyLanguageString.languageindexed2", "Language for element at position %1 from %2", "" + (i + 1), displayName);
                }
                insertLabelValue(w_p, languageDisplayName, sKeyID);
                renderer.renderCombo(w_p);
                w_p.write("</div>");
                w_p.write("</div>");
                w_p.write("</td><td>");

                // add delete button
                String tooltip = getContext().localize("app.OwEditableProperty.delete", "delete");
                w_p.write("&nbsp;<a title='");
                w_p.write(tooltip);
                w_p.write("' href=\"");
                w_p.write(getFormEventURL("Delete", ITEM_QUERY_KEY + "=" + sKey));
                w_p.write("\"><img border='0' src='");
                w_p.write(designRoot);
                w_p.write("/images/deletebtn.png'");

                if (displayName != null)
                {
                    tooltip = getContext().localize2("settingsimpl.OwSettingsPropertyBaseImpl.delarrayitemindexed", "Delete element at position %1 from %2", "" + (i + 1), displayName);
                }
                w_p.write(" alt='");
                w_p.write(tooltip);
                w_p.write("' title='");
                w_p.write(tooltip);
                w_p.write("'/></a>");

                w_p.write("</td></tr>\n");

                i++;
            }

            w_p.write("\n<tr><td colspan='2'>");

            // insert add button
            String tooltip = getContext().localize("app.OwEditableProperty.add", "add");
            w_p.write("<a title='");
            w_p.write(tooltip);
            w_p.write("' href=\"");
            w_p.write(getFormEventURL("Add", null));
            w_p.write("\"><img border='0' src='");
            w_p.write(designRoot);
            w_p.write("/images/addbtn.png'");
            if (getDisplayName() != null)
            {
                tooltip = getContext().localize1("settingsimpl.OwSettingsPropertyBaseImpl.addarrayitemtooltipimg", "Add new item for %1", getDisplayName());
            }
            w_p.write(" alt='");
            w_p.write(tooltip);
            w_p.write("' title='");
            w_p.write(tooltip);
            w_p.write("'/></a>");

            w_p.write("\n</td><tr>");

            w_p.write("</table>");
        }
        else
        {
            // === single value
            String msg = "OwSettingsPropertyLanguageString.insertFormField: must have the setting, list=true.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
    }

    /**
     * Creates a list with {@link OwComboItem} objects, for language representation.
     * @return a list with {@link OwComboItem} objects.
     * @since 3.0.0.0
     */
    private List createLanguageListItems()
    {
        List result = new LinkedList();
        Iterator itLang = ((OwMainAppContext) getContext()).getConfiguration().getAvailableLanguages().iterator();

        while (itLang.hasNext())
        {
            org.w3c.dom.Node langNode = (org.w3c.dom.Node) itLang.next();

            String strLang = langNode.getFirstChild().getNodeValue();
            String strDisplayName = OwXMLDOMUtil.getSafeStringAttributeValue(langNode, "displayname", strLang);
            OwDefaultComboItem item = new OwDefaultComboItem(strLang, strDisplayName);
            result.add(item);
        }
        return result;
    }

    /** called when user clicked a delete button on a list entry
     *
     * @param request_p HttpServletRequest
     */
    public void onDelete(HttpServletRequest request_p) throws Exception
    {
        // get item to be deleted
        String sKey = request_p.getParameter(ITEM_QUERY_KEY);
        // remove item
        m_value.remove(sKey);
    }

    /** called when user clicked the add item button for a list property
     *
     * @param request_p HttpServletRequest
     */
    public void onAdd(HttpServletRequest request_p) throws Exception
    {
        // add item
        m_value.put("", getDefaultListItemValue());
    }

    public void setDefault()
    {
        if (isList())
        {
            // === clone the list
            m_value.clear();
            m_value.putAll(m_defaultValue);
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
        Iterator it = m_value.keySet().iterator();
        while (it.hasNext())
        {
            String sKey = (String) it.next();
            String sValue = (String) m_value.get(sKey);

            Node valueListEntryNode = doc_p.createElement(ITEM_VALUE_NODE);
            Node langattr = doc_p.createAttribute(LANGUAGE_ATTRIBUTE_NAME);
            langattr.setNodeValue(sKey);
            valueListEntryNode.getAttributes().setNamedItem(langattr);

            valueNode.appendChild(valueListEntryNode);

            if (sValue != null)
            {
                valueListEntryNode.appendChild(valueListEntryNode.getOwnerDocument().createTextNode(sValue));
            }
        }

        return valueNode;
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
        // === set members
        boolean fList = OwXMLDOMUtil.getSafeBooleanAttributeValue(propertyDefinitionNode_p, OwBaseConfiguration.PLUGIN_SETATTR_LIST, false);

        // === init value
        Object value;

        if (fList)
        {
            // === multiple values
            // set current value list from value node
            value = createValueMap(valueNode_p);
            // set default value list from description node
            m_defaultValue = createValueMap(propertyDefinitionNode_p);
        }
        else
        {
            // === single value
            String msg = "OwSettingsPropertyLanguageString.init: Must have the setting - list=true in plugin " + strSetName_p + " settings.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
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

    /** create a list of values, if property is a list 
     * @param valueNode_p Node with child value nodes
     * @return List of Objects representing values 
     */
    protected Map createValueMap(Node valueNode_p)
    {
        Map valueMap = new LinkedHashMap();

        // iterate over the child nodes
        for (Node n = valueNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals(ITEM_VALUE_NODE))
            {
                String sKey = OwXMLDOMUtil.getSafeStringAttributeValue(n, LANGUAGE_ATTRIBUTE_NAME, "");

                // create list entry
                valueMap.put(sKey, getSingleValue(n));
            }
        }

        return valueMap;
    }

    /**
     * set locale from context
     * @param mainContext_p
     * @since 3.1.0.4
     */
    public void setLocaleFromContext(OwMainAppContext mainContext_p)
    {
        m_Locale = mainContext_p.getLocale();
    }

    /**
     * get locale from context
     * @return m_Locale current locale
     * @since 3.1.0.4
     */
    public Locale getLocaleFromContext()
    {
        return m_Locale;
    }

    @Override
    public void insertLabel(Writer w_p) throws Exception
    {
        w_p.write(getDisplayName() + ":");

    }

    protected void insertLabelValue(Writer w_p, String displayName, String strID_p) throws IOException
    {
        w_p.write("<label class=\"accessibility\" for=\"" + strID_p + "\">");
        w_p.write(displayName + ":");
        w_p.write("</label>");
    }
}