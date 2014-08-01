package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString2;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A single settings String property, with combo select box.
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
public class OwSettingsPropertyStringCombo extends OwSettingsPropertyBaseImpl
{

    /** node name of a combo select item for list properties */
    protected static final String COMBO_SELECT_ITEM_NODE_NAME = "comboselect";

    /** attribute name of a combo select item display name for list properties */
    protected static final String COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME = "displayname";

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return oSingleValue_p;
    }

    /** create a list of values, if property is a list 
     * @param valueNode_p Node with child value nodes
     * @return List of Objects representing values 
     */
    protected List createComboSelectList(Node valueNode_p) throws Exception
    {
        List valueList = new LinkedList();

        // iterate over the child nodes
        for (Node n = valueNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals(COMBO_SELECT_ITEM_NODE_NAME))
            {
                // create list entry
                OwXMLUtil nodeWrapper = new OwStandardXMLUtil(n);

                String strDisplayName = null;
                String strValue = nodeWrapper.getSafeTextValue(null);

                if (null != strValue)
                {
                    // there is a label defined for the value
                    strDisplayName = ((OwMainAppContext) getContext()).localizeLabel(strValue);
                    if (strDisplayName.equals(strValue))
                    {
                        // no label defined for value so get display name
                        strDisplayName = nodeWrapper.getSafeStringAttributeValue(COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME, "?");
                    }
                    valueList.add(new OwDefaultComboItem(strValue, strDisplayName));
                }
            }
        }

        return valueList;
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
        // === read the combo select items: OwComboSelectItem
        List comboItems = createComboSelectList(getPropertyDefinitionNode());

        OwComboModel model = new OwDefaultComboModel(value_p == null ? null : value_p.toString(), comboItems);
        String indexId = extractIDIndex(strID_p, ITEM_PREFIX, 1);
        String indexString = "";

        if (indexId != null)
        {
            indexString = getContext().localize1("settingsimpl.OwSettingsPropertyStringCombo.index.title", " number %1", indexId);
        }
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, strID_p, null, null, new OwString2("settingsimpl.OwSettingsPropertyStringCombo.index.combo.title", "%1 %2", getDisplayName(), indexString));
        renderer.renderCombo(w_p);

    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p)
    {
        return request_p.getParameter(strID_p);
    }

    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue() throws Exception
    {
        // === read the combo select items: OwComboItem
        return ((OwComboItem) createComboSelectList(getPropertyDefinitionNode()).get(0)).getValue();
    }
}